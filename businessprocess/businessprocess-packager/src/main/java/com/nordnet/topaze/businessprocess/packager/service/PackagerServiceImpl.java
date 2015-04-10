package com.nordnet.topaze.businessprocess.packager.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import nordnet.customer.NetCustomerClient;
import nordnet.packager.PackagerClient;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.LocalDate;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.google.common.base.Optional;
import com.nordnet.adminpackager.ConverterException;
import com.nordnet.adminpackager.DeliveryException;
import com.nordnet.adminpackager.DriverException;
import com.nordnet.adminpackager.MalformedXMLException;
import com.nordnet.adminpackager.NotFoundException;
import com.nordnet.adminpackager.NotRespectedRulesException;
import com.nordnet.adminpackager.NullException;
import com.nordnet.adminpackager.PackagerException;
import com.nordnet.backoffice.ws.CustomerException;
import com.nordnet.backoffice.ws.NetCustomer;
import com.nordnet.backoffice.ws.types.customer.Handle;
import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.netcatalog.ws.entity.PackagerConfig;
import com.nordnet.packager.types.delivery.TDeliveryAddress;
import com.nordnet.packager.types.delivery.TReceiverCivility;
import com.nordnet.packager.types.delivery.TReceiverContact;
import com.nordnet.packager.types.feasibility.TFeasibilityResult;
import com.nordnet.packager.types.packager.TPackagerInstance;
import com.nordnet.packager.types.packager.TState;
import com.nordnet.packager.types.request.TDeliveryRequest;
import com.nordnet.packager.types.request.TPackagerRequest;
import com.nordnet.packager.types.request.TPackagerTransformationRequest;
import com.nordnet.packager.types.request.TProductRequest;
import com.nordnet.topaze.businessprocess.packager.adapter.PackagerAdapter;
import com.nordnet.topaze.businessprocess.packager.adapter.TopazeCatalogAdapter;
import com.nordnet.topaze.businessprocess.packager.mock.NetCustomerMock;
import com.nordnet.topaze.businessprocess.packager.mock.OpaleMock;
import com.nordnet.topaze.businessprocess.packager.mock.PackagerMock;
import com.nordnet.topaze.businessprocess.packager.util.Constants;
import com.nordnet.topaze.businessprocess.packager.util.ConstantsConnexion;
import com.nordnet.topaze.businessprocess.packager.util.PropertiesUtil;
import com.nordnet.topaze.businessprocess.packager.util.spring.ApplicationContextHolder;
import com.nordnet.topaze.client.rest.RestClientPackager;
import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;
import com.nordnet.topaze.client.rest.business.livraison.ElementContractuelInfo;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.client.rest.enums.OutilLivraison;
import com.nordnet.topaze.client.rest.enums.TypeBonPreparation;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.logger.service.TracageService;

/**
 * implementation du {@link PackagerService}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Service("packagerService")
public class PackagerServiceImpl implements PackagerService {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(PackagerServiceImpl.class);

	/**
	 * {@link com.nordnet.adminpackager.PackagerService}.
	 */
	private com.nordnet.adminpackager.PackagerService packagerService;

	/**
	 * {@link NetCustomer}.
	 */
	private NetCustomer netCustomerService;

	/**
	 * {@link RestClientPackager}.
	 */
	@Autowired
	private RestClientPackager restClientPackager;

	/**
	 * 
	 * {@link TopazeCatalogAdapter}.
	 */
	@Autowired
	private TopazeCatalogAdapter topazeCatalogAdapter;

	/**
	 * {@link PackagerAdapter}.
	 */
	@Autowired
	private PackagerAdapter packagerAdapter;

	/**
	 * {@link OpaleMock}.
	 */
	@Autowired
	private OpaleMock opaleMock;

	/**
	 * Velocity Engine.
	 */
	@Autowired
	private VelocityEngineFactoryBean velocityEngine;

	/**
	 * Alert service.
	 */
	SendAlert sendAlert;

	/**
	 * {@link TracageService}.
	 */
	private TracageService tracageService;

	/**
	 * {@link KeygenService}.
	 */
	@Autowired
	private KeygenService keygenService;

	/**
	 * liste des modèles packager necessitant un DeliveryRequest.
	 */
	List<String> needDeliveryRequestProducts = new ArrayList<>();

	/**
	 * Réecupérer la liste des modèles packager necessitant un DeliveryRequest.
	 * 
	 * @return {@link List}
	 */
	private List<String> getetNeedDeliveryRequestProducts() {
		if (needDeliveryRequestProducts.isEmpty()) {
			Properties props = System.getProperties();

			for (String propName : props.stringPropertyNames()) {
				if (propName.startsWith("needDeliveryRequest")) {
					needDeliveryRequestProducts.add(props.getProperty(propName));
				}
			}
		}
		return needDeliveryRequestProducts;
	}

	/**
	 * 
	 * @return {@link TracageService}.
	 */
	private TracageService getTracageService() {
		if (tracageService == null) {
			if (System.getProperty("log.useMock").equals("true")) {
				tracageService = (TracageService) ApplicationContextHolder.getBean("tracageServiceMock");
			} else {
				tracageService = (TracageService) ApplicationContextHolder.getBean("tracageService");
			}
		}
		return tracageService;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("null")
	@Override
	public void traductionPackager(BonPreparation bonPreparation) throws TopazeException {
		try {
			TPackagerRequest packagerRequest = null;
			TFeasibilityResult feasibilityResult = null;
			boolean isPossible = true;
			Boolean checkIsPackagerCreationPossible =
					restClientPackager.checkIsPackagerCreationPossible(bonPreparation.getReference());
			for (ElementLivraison elementLivraison : bonPreparation.getElementLivraisons()) {
				elementLivraison.setBonPreparationParent(bonPreparation);
				/*
				 * si l'element est une option alors le bon ne doit pas etre envoyer vers packager.
				 */
				if (elementLivraison.getActeur() == OutilLivraison.PACKAGER
						&& elementLivraison.getTypeBonPreparation() == TypeBonPreparation.LIVRAISON) {
					PackagerConfig packagerConfig =
							topazeCatalogAdapter.getPackagerConfig(elementLivraison.getReferenceProduit());
					if (packagerConfig.getTemplate() != null) {
						elementLivraison.setRetailerPackagerId(Constants.PREFIX_RETAILER_PACKAGER_ID
								+ keygenService.getNextKey(ElementLivraison.class));
						try {
							packagerRequest =
									creationPackagerRequest(elementLivraison, packagerConfig,
											bonPreparation.getIdClient());

							if (checkIsPackagerCreationPossible) {
								feasibilityResult = getPackagerService().isPackagerCreationPossible(packagerRequest);
								isPossible = feasibilityResult.isPossible();
							}
							if (isPossible) {
								/*
								 * traduire le BP.
								 */
								getPackagerService().createPackager(packagerRequest);

								/*
								 * marquer le service comme preparer
								 */
								restClientPackager.marquerServicePrepare(elementLivraison);

								/*
								 * verifier si l'element ne contient pas une option parmi ses fils.
								 */
								for (ElementContractuelInfo elementContractuelInfo : elementLivraison.getOptionInfos()) {
									/*
									 * la preparation de l'option n'a besoin que de la reference.
									 */
									ElementLivraison elLivraisonOption = new ElementLivraison();
									elLivraisonOption.setReference(elementContractuelInfo.getReferenceContrat() + "-"
											+ elementContractuelInfo.getNumEC());
									elLivraisonOption.setReferenceProduit(elementContractuelInfo.getReferenceProduit());
									restClientPackager.marquerServicePrepare(elLivraisonOption);
								}
							} else {
								/*
								 * marquer le BP non Livre
								 */
								restClientPackager.marquerNonLivre(elementLivraison.getReference(),
										feasibilityResult.getMotive());

								/*
								 * verifier si l'element ne contient pas une option parmi ses fils, pour le marquer non
								 * livrer.
								 */
								for (ElementContractuelInfo elementContractuelInfo : elementLivraison.getOptionInfos()) {
									restClientPackager.marquerNonLivre(elementContractuelInfo.getReferenceContrat(),
											Constants.PARENT_NON_LIVRER);
								}
							}
						} catch (FileNotFoundException e) {
							LOGGER.error("Template not found", e);
							/*
							 * si la template n'existe pas, l'element sera marque comme non librer.
							 */
							restClientPackager.marquerNonLivre(elementLivraison.getReference(),
									Constants.TEMPLATE_XML_ABSENT);
							throw new TopazeException("Template n'existe pas", e);
						}

					}
				}
			}
		} catch (NullException | NotFoundException | PackagerException | DeliveryException | ConverterException
				| MalformedXMLException | NotRespectedRulesException | DriverException exception) {
			LOGGER.error("error occurs during call of  PackagerServiceImpl.traductionPackager()", exception);
			try {
				getSendAlert().send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
						"error occurs during call of " + "PackagerServiceImpl.traductionPackager()",
						exception.getMessage());
			} catch (Exception e) {
				LOGGER.error("fail to send alert", e);
				throw new TopazeException("fail to send alert", e);
			}
		} catch (Exception e) {
			throw new TopazeException(e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void traductionPackagerPourLivraison(String referenceBP) throws TopazeException {
		BonPreparation bonPreparationLivraison = restClientPackager.getBonPreparation(referenceBP);
		if (!bonPreparationLivraison.isAnnule()) {
			traductionPackager(bonPreparationLivraison);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void traductionPackagerPourMigration(String referenceBP) throws TopazeException {
		BonPreparation bonPreparationMigration = restClientPackager.getBonMigration(referenceBP);
		traductionPackager(bonPreparationMigration);
		suspendService(bonPreparationMigration, true);
		traductionPourTransformService(bonPreparationMigration);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void suspendService(BonPreparation bonRecuperation, boolean isMigration) throws TopazeException {
		try {
			if (bonRecuperation.getElementLivraisons() != null && bonRecuperation.getElementLivraisons().size() > 0) {
				for (ElementLivraison elementLivraison : bonRecuperation.getElementLivraisons()) {

					/*
					 * il n'y a que les element 'Retour' qui vont etre suspendu.
					 */
					if (elementLivraison.getTypeBonPreparation() == TypeBonPreparation.RETOUR
							&& elementLivraison.getActeur() == OutilLivraison.PACKAGER) {

						if (elementLivraison.getRetailerPackagerId() != null) {
							TPackagerRequest packagerRequest = new TPackagerRequest();
							packagerRequest.setRetailerPackagerId(elementLivraison.getRetailerPackagerId());
							getPackagerService().suspendPackager(packagerRequest);
						} else {

							/*
							 * suspension d'une option.
							 */
							if (isMigration) {
								/*
								 * lors de la migration la suspension d'une option se fait automatique: soit avec le
								 * transform packager soit avec le suspend du parent.
								 */
								restClientPackager.marquerSuspendu(elementLivraison.getReference());
							} else {
								/*
								 * c'est une option, le suspend se fait par deux façons :
								 */
								ElementContractuelInfo parentInfo =
										restClientPackager.getParentInfo(bonRecuperation.getReference(),
												elementLivraison.getNumEC());
								ElementLivraison elementRetourParent =
										restClientPackager.getElementRecuperation(parentInfo.getReferenceContrat()
												+ "-" + parentInfo.getNumEC());
								/*
								 * si le parent est resilier, le suspend de l'option se fera d'une façon automatique
								 * avec le suspend du parent, si non le suspend se fait par un 'changeProperties'.
								 */
								if (elementRetourParent == null) {
									/*
									 * l'elementRetourParent null donc le elementContractuel parent n'as pas ete
									 * resilier, il s'agit d'une resiliation partiel de l'option.
									 */
									ElementLivraison elementLivraisonParent =
											restClientPackager.getElementLivraison(parentInfo.getReferenceContrat(),
													parentInfo.getReferenceProduit());
									PackagerConfig packagerConfig =
											topazeCatalogAdapter.getPackagerConfig(elementLivraisonParent
													.getReferenceProduit());
									elementLivraisonParent.setBonPreparationParent(bonRecuperation);
									changeProperties(elementLivraisonParent, packagerConfig,
											bonRecuperation.getIdClient());
									/*
									 * une fois que l'appel est passé vers packger l'option est marquer comme suspendu.
									 */
									restClientPackager.marquerSuspendu(elementLivraison.getReference());
								}
							}

						}
					}
				}
			}
		} catch (NullException | NotFoundException | PackagerException | ConverterException | MalformedXMLException
				| NotRespectedRulesException | DriverException exception) {
			LOGGER.error("error occurs during call of  PackagerServiceImpl.suspendService()", exception);
			try {
				getSendAlert()
						.send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
								"error occurs during call of " + "PackagerServiceImpl.suspendService()",
								exception.getMessage());
			} catch (Exception e) {
				LOGGER.error("fail to send alert", e);
				throw new TopazeException("fail to send alert", e);

			}
		} catch (Exception e) {
			throw new TopazeException(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cancelPackager(ElementLivraison elementLivraison) throws TopazeException {
		LocalDate dateJour = PropertiesUtil.getInstance().getDateDuJour();
		LocalDate dateRetourTermine = LocalDate.fromDateFields(elementLivraison.getDateRetourTermine());
		Integer delaiCancel = PropertiesUtil.getInstance().getDelaieCancel(null);
		if (dateRetourTermine.plusDays(delaiCancel).getDayOfYear() == dateJour.getDayOfYear()) {
			TPackagerRequest packagerRequest = new TPackagerRequest();
			packagerRequest.setRetailerPackagerId(elementLivraison.getRetailerPackagerId());
			try {
				LOGGER.info("Cancel Packager pour la service: " + elementLivraison.getReferenceProduit()
						+ " dont le retailer packager id est : " + elementLivraison.getRetailerPackagerId());
				getPackagerService().cancelPackager(packagerRequest);
				getTracageService().ajouterTrace(
						Constants.PRODUCT,
						elementLivraison.getReference().split("-")[0],
						"Le package n° " + elementLivraison.getRetailerPackagerId() + ", correspondant à l’EC n° "
								+ elementLivraison.getNumEC() + ", est passé à l'état CANCELED au bout de "
								+ delaiCancel + (delaiCancel > 1 ? " jours" : " jour"), Constants.INTERNAL_USER);
			} catch (NullException | NotFoundException | PackagerException | ConverterException | MalformedXMLException
					| NotRespectedRulesException | DriverException exception) {
				LOGGER.error("error occurs during call of  PackagerServiceImpl.cancelPackager()", exception);
				try {
					getSendAlert().send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
							"error occurs during call of " + "PackagerServiceImpl.cancelPackager()",
							exception.getMessage());
				} catch (Exception e) {
					LOGGER.error("fail to send alert", e);
					throw new TopazeException("fail to send alert", e);
				}
				throw new TopazeException("error pendant l'appel de PackagerServiceImpl.cancelPackager() ", exception);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void traductionPourTransformService(BonPreparation bonMigration) throws TopazeException {
		try {
			for (ElementLivraison elementLivraison : bonMigration.getElementLivraisons()) {
				if (elementLivraison.getActeur() == OutilLivraison.PACKAGER
						&& elementLivraison.getTypeBonPreparation() == TypeBonPreparation.MIGRATION
						&& elementLivraison.getAncienRetailerPackagerId() != null) {
					try {
						PackagerConfig packagerConfig =
								topazeCatalogAdapter.getPackagerConfig(elementLivraison.getReferenceProduit());
						transformPackager(elementLivraison, packagerConfig);
						restClientPackager.marquerEMLivre(elementLivraison);
						restClientPackager.marquerEMRetourne(elementLivraison);

						/*
						 * verifier si l'element ne contient pas une option parmi ses fils.
						 */
						for (ElementContractuelInfo elementContractuelInfo : elementLivraison.getOptionInfos()) {
							/*
							 * l'option n'a besoin que de la reference.
							 */
							ElementLivraison elLivraisonOption = new ElementLivraison();
							elLivraisonOption.setReference(elementContractuelInfo.getReferenceContrat() + "-"
									+ elementContractuelInfo.getNumEC());
							elLivraisonOption.setReferenceProduit(elementContractuelInfo.getReferenceProduit());
							ElementLivraison elLivraisonOptionPlus =
									restClientPackager.getElementLivraison(elementContractuelInfo.getReferenceContrat()
											+ "-" + elementContractuelInfo.getNumEC(),
											elementContractuelInfo.getReferenceProduit());
							if (elLivraisonOptionPlus != null) {
								restClientPackager.marquerServicePrepare(elLivraisonOptionPlus);
								restClientPackager.marquerSousBPLivre(elLivraisonOptionPlus);
							} else {
								restClientPackager.marquerEMLivre(elLivraisonOption);
								restClientPackager.marquerEMRetourne(elLivraisonOption);
							}
						}
					} catch (FileNotFoundException e) {
						/*
						 * si la template n'existe pas, l'element sera marque comme non librer.
						 */
						restClientPackager.marquerNonMigre(elementLivraison.getReference(),
								Constants.TEMPLATE_XML_ABSENT);
						LOGGER.error(e.getLocalizedMessage());
						throw new TopazeException("Template n'existe pas :", e);
					}
				}
			}
		} catch (NullException | NotFoundException | PackagerException | ConverterException | MalformedXMLException
				| NotRespectedRulesException | DriverException exception

		) {
			LOGGER.error("error occurs during call of  PackagerServiceImpl.traductionPourTransformService()", exception);
			try {
				getSendAlert().send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
						"error occurs during call of " + "PackagerServiceImpl.traductionPourTransformService()",
						exception.getMessage());
			} catch (Exception e) {
				LOGGER.error("fail to send alert", e);
				throw new TopazeException("fail to send alert", e);
			}
		} catch (Exception e) {
			throw new TopazeException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void traductionPourCession(BonPreparation bonPreparation) throws TopazeException {
		try {
			for (ElementLivraison elementLivraison : bonPreparation.getElementLivraisons()) {
				if (elementLivraison.getActeur() == OutilLivraison.PACKAGER
						&& elementLivraison.getRetailerPackagerId() != null) {
					elementLivraison.setBonPreparationParent(bonPreparation);
					PackagerConfig packagerConfig =
							topazeCatalogAdapter.getPackagerConfig(elementLivraison.getReferenceProduit());
					if (readTemplateXml(packagerConfig.getTemplate()).contains((Constants.CLIENT))) {
						/*
						 * lors de la cession le change properties n'est effectue que lorsque la template xml contient
						 * des info sur le client qui doivent étre mise à jour.
						 */
						changeProperties(elementLivraison, packagerConfig, bonPreparation.getIdClient());
						getTracageService().ajouterTrace(
								Constants.PRODUCT,
								elementLivraison.getReference().split("-")[0],
								"cession du service " + elementLivraison.getReference()
										+ " - appel packager: 'changeProperties'", Constants.INTERNAL_USER);
					} else {
						/*
						 * les options du produit doivent étre récuperer pour les marquer comme ceder
						 */
						List<ElementContractuelInfo> optionsInfos =
								getOptions(elementLivraison.getBonPreparationParent().getReference(),
										elementLivraison.getNumEC());
						elementLivraison.setOptionInfos(optionsInfos);
					}
					restClientPackager.marquerELCede(elementLivraison);

					/*
					 * verifier si l'element ne contient pas une option parmi ses fils.
					 */
					for (ElementContractuelInfo elementContractuelInfo : elementLivraison.getOptionInfos()) {
						/*
						 * pour marquer cede de l'option n'a besoin que de la reference.
						 */
						ElementLivraison elLivraisonOption = new ElementLivraison();
						elLivraisonOption.setReference(elementContractuelInfo.getReferenceContrat() + "-"
								+ elementContractuelInfo.getNumEC());
						restClientPackager.marquerELCede(elLivraisonOption);
						getTracageService().ajouterTrace(Constants.PRODUCT,
								elementLivraison.getReference().split("-")[0],
								"cession du service " + elementLivraison.getReference(), Constants.INTERNAL_USER);
					}
				}
			}
		} catch (Exception e) {
			throw new TopazeException(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changeProperties(ElementLivraison elementRetour, PackagerConfig packagerConfig, String idClient)
			throws TopazeException {
		try {
			TPackagerInstance packagerInstance = packagerAdapter.getPackagerInstance(elementRetour);
			Long productId = packagerInstance.getProducts().getProduct().get(Constants.ZERO).getProductId();
			Map<String, Object> configMap = getConfigurationXMLTemplate(elementRetour, packagerConfig);
			String xmlTemplate =
					VelocityEngineUtils.mergeTemplateIntoString(velocityEngine.getObject(),
							packagerConfig.getTemplate(), "UTF-8", configMap);
			TPackagerRequest packagerRequest = new TPackagerRequest();
			packagerRequest.setRetailerPackagerId(elementRetour.getRetailerPackagerId());
			TProductRequest productRequest = new TProductRequest();
			productRequest.setProperties(xmlTemplate);
			productRequest.setModel(packagerConfig.getProductModel());
			productRequest.setProductId(productId);
			TPackagerRequest.Products products = new TPackagerRequest.Products();
			products.getProduct().add(productRequest);
			packagerRequest.setRetailerPackagerId(elementRetour.getRetailerPackagerId());
			packagerRequest.setModel(packagerConfig.getPackagerModel());
			packagerRequest.setProducts(products);
			getPackagerService().changePackagerProperties(packagerRequest);
		} catch (NullException | NotFoundException | PackagerException | ConverterException | MalformedXMLException
				| NotRespectedRulesException | DriverException exception

		) {
			LOGGER.error("error occurs during call of  PackagerServiceImpl.suspendServiceParChangeProperties()",
					exception);
			try {
				getSendAlert().send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
						"error occurs during call of " + "PackagerServiceImpl.suspendServiceParChangeProperties()",
						exception.getMessage());
			} catch (Exception e) {
				LOGGER.error("fail to send alert", e);
				throw new TopazeException("fail to send alert", e);
			}
		} catch (Exception e) {
			throw new TopazeException(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NotFoundException
	 */
	@Override
	public Boolean checkServiceActive(ElementLivraison elementLivraison) throws TopazeException, NotFoundException {

		try {
			Boolean isActiver = false;
			TState state = packagerAdapter.getPackagerInstance(elementLivraison).getCurrentState();
			if (state.equals(TState.ACTIVABLE) || state.equals(TState.ACTIVE)) {
				isActiver = true;
				// tracer l'operation
				getTracageService().ajouterTrace(
						Constants.PRODUCT,
						elementLivraison.getReference().split("-")[0],
						"Livraison du contrat " + elementLivraison.getReference().split("-")[0]
								+ " – Fin de livraison du produit " + elementLivraison.getReferenceProduit(),
						Constants.INTERNAL_USER);

			}
			return isActiver;
		} catch (NullException | PackagerException | ConverterException | DriverException exception

		) {
			LOGGER.error("error occurs during call of  PackagerServiceImpl.checkServiceActive()", exception);
			try {
				getSendAlert().send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
						"error occurs during call of " + "PackagerServiceImpl.checkServiceActive()",
						exception.getMessage());

			} catch (Exception e) {
				LOGGER.error("fail to send alert", e);
				throw new TopazeException("fail to send alert", e);

			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean checkServiceSuspendu(ElementLivraison elementLivraison) throws TopazeException, NotFoundException {
		try {
			Boolean isSuspendu = false;
			TState state = packagerAdapter.getPackagerInstance(elementLivraison).getCurrentState();
			if (state.equals(TState.SUSPENDED)) {
				isSuspendu = true;
				// tracer l'operation
				getTracageService().ajouterTrace(
						Constants.PRODUCT,
						elementLivraison.getReference().split("-")[0],
						"Retour du contrat " + elementLivraison.getReference() + " –  Demande de retour du produit "
								+ elementLivraison.getReferenceProduit(), Constants.INTERNAL_USER);
			}
			return isSuspendu;
		} catch (NullException | PackagerException | ConverterException | DriverException exception

		) {
			LOGGER.error("error occurs during call of  PackagerServiceImpl.checkServiceSuspendu()", exception);
			try {
				getSendAlert().send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
						"error occurs during call of " + "PackagerServiceImpl.checkServiceSuspendu()",
						exception.getMessage());

			} catch (Exception e) {
				LOGGER.error("fail to send alert", e);
				throw new TopazeException("fail to send alert", e);
			}
		}
		return false;
	}

	/**
	 * Creation d'une requete packager.
	 * 
	 * @param elementLivraison
	 *            the element livraison
	 * @param packagerConfig
	 *            the packager config
	 * @param idClient
	 *            the id client
	 * @return {@link TPackagerRequest}.
	 * @throws TopazeException
	 *             the topaze exception
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws JSONException
	 *             the jSON exception {@link ElementLivraison} {@link TopazeException}. {@link IOException}.
	 *             {@link JsonMappingException}. {@link JsonParseException}. {@link JSONException}.
	 * @throws CustomerException
	 *             {@link CustomerException}
	 */
	private TPackagerRequest creationPackagerRequest(ElementLivraison elementLivraison, PackagerConfig packagerConfig,
			String idClient)
			throws TopazeException, JsonParseException, JsonMappingException, IOException, JSONException,
			CustomerException {

		TPackagerRequest packagerRequest = new TPackagerRequest();
		packagerRequest.setRetailerPackagerId(elementLivraison.getRetailerPackagerId());
		packagerRequest.setModel(packagerConfig.getPackagerModel());

		// la balise products ne sera pas alimente dans la cas ou la template xml est vide.
		String xmlTemplateString = readTemplateXml(packagerConfig.getTemplate());
		if (!xmlTemplateString.equals(Constants.TEMPLATE_VIDE)) {
			Map<String, Object> configMap = getConfigurationXMLTemplate(elementLivraison, packagerConfig);
			String xmlTemplate =
					VelocityEngineUtils.mergeTemplateIntoString(velocityEngine.getObject(),
							packagerConfig.getTemplate(), "UTF-8", configMap);
			TProductRequest productRequest = new TProductRequest();
			productRequest.setProperties(xmlTemplate);
			productRequest.setModel(packagerConfig.getProductModel());
			TPackagerRequest.Products products = new TPackagerRequest.Products();
			products.getProduct().add(productRequest);
			packagerRequest.setProducts(products);
		}

		if (needDeliveryRequest(elementLivraison)) {

			packagerRequest.setDeliveryRequest(addDeliveryRequest(idClient, elementLivraison.getAddresseLivraison()));
		}

		return packagerRequest;
	}

	/**
	 * Creer le {@link DeliveryRequest}.
	 * 
	 * @param idAdresse
	 *            l adresse du client
	 * 
	 * @param idClient
	 *            l identifient du client
	 * @return {@link TDeliveryRequest}
	 * @throws CustomerException
	 *             {@link CustomerException}
	 */
	private TDeliveryRequest addDeliveryRequest(String idClient, String idAdresse) throws CustomerException {

		TDeliveryRequest tDeliveryRequest = new TDeliveryRequest();
		tDeliveryRequest.setCustomerId(idClient);
		tDeliveryRequest.setSendDelivery(true);

		Handle handle = getNetCustomerClient().getHandleByKey(idAdresse);
		TReceiverContact tReceiverContact = new TReceiverContact();
		TDeliveryAddress tDeliveryAddress = new TDeliveryAddress();
		tDeliveryAddress.setAddress1(handle.getAddress1().getValue());
		tDeliveryAddress.setAddress2(handle.getAddress2().getValue());
		tDeliveryAddress.setCity(handle.getCity().getValue());
		tDeliveryAddress.setCountry(handle.getCountry().getValue());
		tDeliveryAddress.setZipCode(handle.getZipCode().getValue());
		tReceiverContact.setAddress(tDeliveryAddress);
		tReceiverContact.setCivility(TReceiverCivility.fromValue(handle.getCivility().getValue()));
		tReceiverContact.setCompany(handle.getCompany().getValue());
		tReceiverContact.setEmail(handle.getEmail().getValue());
		tReceiverContact.setFirstName(handle.getFirstName().getValue());
		tReceiverContact.setLastName(handle.getName().getValue());
		tReceiverContact.setPhoneNumber(handle.getPhone().getValue());
		tDeliveryRequest.setReceiver(tReceiverContact);
		return tDeliveryRequest;
	}

	/**
	 * Tester si on a besoin d'un {@link DeliveryRequest}.
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison}
	 * @return {@link Boolean}
	 */
	private boolean needDeliveryRequest(ElementLivraison elementLivraison) {
		if (getetNeedDeliveryRequestProducts().contains(elementLivraison.getReferenceProduit())) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison}.
	 * @param packagerConfig
	 *            {@link PackagerConfig}.
	 * @throws NullException
	 *             {@link NullException}.
	 * @throws NotFoundException
	 *             {@link NotFoundException}.
	 * @throws PackagerException
	 *             {@link PackagerException}.
	 * @throws ConverterException
	 *             {@link ConverterException}.
	 * @throws MalformedXMLException
	 *             {@link MalformedXMLException}.
	 * @throws NotRespectedRulesException
	 *             {@link NotRespectedRulesException}.
	 * @throws DriverException
	 *             {@link DriverException}.
	 * @throws JSONException
	 *             {@link JSONException}.
	 * @throws IOException
	 *             {@link IOException}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws JsonMappingException
	 *             {@link JsonMappingException}.
	 * @throws JsonParseException
	 *             {@link JsonParseException}.
	 */
	public void transformPackager(ElementLivraison elementLivraison, PackagerConfig packagerConfig)
			throws NullException, NotFoundException, PackagerException, ConverterException, MalformedXMLException,
			NotRespectedRulesException, DriverException, JsonParseException, JsonMappingException, TopazeException,
			IOException, JSONException {

		TPackagerInstance packagerInstance = packagerAdapter.getPackagerInstance(elementLivraison);
		Long productId = packagerInstance.getProducts().getProduct().get(Constants.ZERO).getProductId();
		TPackagerTransformationRequest transformPackagerRequest = new TPackagerTransformationRequest();
		TProductRequest productRequest = new TProductRequest();
		productRequest.setModel(packagerConfig.getProductModel());
		productRequest.setProductId(productId);
		Map<String, Object> configMap = getConfigurationXMLTemplate(elementLivraison, packagerConfig);
		String xmlTemplate =
				VelocityEngineUtils.mergeTemplateIntoString(velocityEngine.getObject(), packagerConfig.getTemplate(),
						"UTF-8", configMap);
		productRequest.setProperties(xmlTemplate);
		TPackagerRequest.Products products = new TPackagerRequest.Products();
		products.getProduct().add(productRequest);
		elementLivraison.setRetailerPackagerId(keygenService.getNextKey(ElementLivraison.class));
		transformPackagerRequest.setDestinationRetailerPackagerId(elementLivraison.getRetailerPackagerId());
		transformPackagerRequest.setDestinationModel(packagerConfig.getProductModel());
		transformPackagerRequest.setProducts(products);
		getPackagerService().transformPackager(transformPackagerRequest);
	}

	/**
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison}
	 * @param packagerConfig
	 *            {@link PackagerConfig}
	 * @return la template xml configure.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 * @throws IOException
	 *             {@link IOException}
	 * @throws JsonMappingException
	 *             {@link JsonMappingException}
	 * @throws JsonParseException
	 *             {@link JsonParseException}
	 * @throws JSONException
	 *             {@link JSONException}
	 */
	private Map<String, Object> getConfigurationXMLTemplate(ElementLivraison elementLivraison,
			PackagerConfig packagerConfig)
			throws TopazeException, JsonParseException, JsonMappingException, IOException, JSONException {
		Map<String, Object> configMap = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		if (packagerConfig.getConfig() != null) {
			configMap = mapper.readValue(packagerConfig.getConfig(), new TypeReference<HashMap<String, Object>>() {
				/*
				 * {@link TypeReference}.
				 */
			});
		}
		String xmlTemplateString = readTemplateXml(packagerConfig.getTemplate());
		if (xmlTemplateString.contains(Constants.PROCESS)) {
			configMap.putAll(getConfigProcess(elementLivraison, xmlTemplateString));
		}
		if (xmlTemplateString.contains(Constants.CLIENT)) {
			configMap.putAll(getConfigClient(elementLivraison.getAddresseLivraison()));
		}

		if (xmlTemplateString.contains(Constants.ORDER)) {
			configMap.putAll(opaleMock.getPackagerConfig(
					restClientPackager.getReferenceCommande(elementLivraison.getBonPreparationParent().getReference()),
					elementLivraison.getReferenceProduit()));
		}
		return configMap;
	}

	/**
	 * @return {@link #packagerService}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public com.nordnet.adminpackager.PackagerService getPackagerService() throws TopazeException {
		if (packagerService == null) {
			PackagerClient packagerClient = null;
			if (ConstantsConnexion.USE_PACKAGER_MOCK) {
				packagerClient =
						PackagerClient.builder().withLogger(ConstantsConnexion.PACKAGER_LOGGER)
								.withMockedPort(new PackagerMock()).enableMocking().build(PackagerClient.class);
			} else {
				packagerClient =
						PackagerClient.builder()
								.withWsseCredentials(ConstantsConnexion.PACKAGER_USER, ConstantsConnexion.PACKAGER_PWD)
								.withConnectionTimeout(ConstantsConnexion.PACKAGER_CONNECTION_TIME_OUT)
								.withReceiveTimeout(ConstantsConnexion.PACKAGER_RECEIVE_TIME_OUT)
								.withLogger(ConstantsConnexion.PACKAGER_LOGGER)
								.withEndpoint(ConstantsConnexion.PACKAGER_ENDPOINT)
								.withServers(ConstantsConnexion.PACKAGER_SERVERS).disableMocking()
								.build(PackagerClient.class);
			}

			packagerService = packagerClient.service();

			if (!Optional.fromNullable(packagerService).isPresent()) {
				throw new TopazeException("erreur lors d'etablissement de la connexion avec packager", "404");
			}
		}

		return packagerService;
	}

	/**
	 * Effectue la connexion avec NetCustomer et retourn le service contenant les operations fournit par netCustomer.
	 * 
	 * @return {@link NetCustomer}.
	 */
	public NetCustomer getNetCustomerClient() {
		if (netCustomerService == null) {
			NetCustomerClient netCustomerClient = null;
			if (ConstantsConnexion.USE_NETCUSTOMER_MOCK) {
				netCustomerClient =
						NetCustomerClient.builder().withMockedPort(new NetCustomerMock()).enableMocking()
								.build(NetCustomerClient.class);
			} else {
				netCustomerClient =
						NetCustomerClient
								.builder()
								.withWsseCredentials(ConstantsConnexion.NETCUSTOMER_USER,
										ConstantsConnexion.NETCUSTOMER_PWD)
								.withConnectionTimeout(ConstantsConnexion.NETCUSTOMER_CONNECTION_TIME_OUT)
								.withReceiveTimeout(ConstantsConnexion.NETCUSTOMER_RECEIVE_TIME_OUT)
								.withLogger(ConstantsConnexion.NETCUSTOMER_LOGGER)
								.withEndpoint(ConstantsConnexion.NETCUSTOMER_ENDPOINT)
								.withServers(ConstantsConnexion.NETCUSTOMER_SERVERS).disableMocking()
								.build(NetCustomerClient.class);
			}
			netCustomerService = netCustomerClient.service();
		}
		return netCustomerService;
	}

	/**
	 * retourner les informations du client pour remplir la template xml du packager.
	 * 
	 * @param idAdresse
	 *            adresse du client.
	 * @return les informations du client pour remplir la template xml du packager.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public Map<String, Object> getConfigClient(String idAdresse) throws TopazeException {
		Map<String, Object> configMap = new HashMap<>();
		try {
			Handle handle = getNetCustomerClient().getHandleByKey(idAdresse);
			configMap.put(Constants.CLIENT_FIRST_NAME, handle.getFirstName().getValue());
			configMap.put(Constants.CLIENT_LAST_NAME, handle.getName().getValue());
			configMap.put(Constants.CLIENT_EMAIL, handle.getEmail().getValue());
			configMap.put(Constants.CLIENT_ADDRESS_1, handle.getAddress1().getValue());
			configMap.put(Constants.CLIENT_ADDRESS_2, handle.getAddress2().getValue());
			configMap.put(Constants.CLIENT_ZIP, handle.getZipCode().getValue());
			configMap.put(Constants.CLIENT_CITY, handle.getCity().getValue());
			configMap.put(Constants.CLIENT_CIVILITY, handle.getCivility().getValue());
			configMap.put(Constants.CLIENT_COUNTRY, handle.getCountry().getValue());
			configMap.put(Constants.CLIENT_PROFILE_TYPE, handle.getProfileType().getValue());
		} catch (CustomerException e) {
			LOGGER.error(e.getMessage());
			throw new TopazeException(e.getMessage(), e);
		}

		return configMap;
	}

	/**
	 * retourner les config lie au process.
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison}.
	 * @param xmlTemplateString
	 *            la template xml du packager sous format.
	 * @return les config lie au process.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public Map<String, Object> getConfigProcess(ElementLivraison elementLivraison, String xmlTemplateString)
			throws TopazeException {
		Map<String, Object> configMap = new HashMap<>();
		if (xmlTemplateString.contains(Constants.USE_QOS_INPACKAGER_TRAME)) {
			boolean isVoIP =
					isContratContientVOIP(elementLivraison.getBonPreparationParent().getReference(),
							elementLivraison.getNumEC());
			if (isVoIP) {
				configMap.put(Constants.USE_QOS_INPACKAGER_TRAME, true);
			} else {
				configMap.put(Constants.USE_QOS_INPACKAGER_TRAME, false);
			}
		}
		if (xmlTemplateString.contains(Constants.OPTION_PLUS) || xmlTemplateString.contains(Constants.FREEZONE)) {
			List<ElementContractuelInfo> optionsInfos =
					getOptions(elementLivraison.getBonPreparationParent().getReference(), elementLivraison.getNumEC());
			for (ElementContractuelInfo elementContractuelInfo : optionsInfos) {
				if (Constants.REFERENCES_OPTION_PLUS.contains(elementContractuelInfo.getReferenceProduit())) {
					configMap.put(Constants.OPTION_PLUS,
							topazeCatalogAdapter.getValeurOptionPlus(elementContractuelInfo.getReferenceProduit()));
				} else if (elementContractuelInfo.getReferenceProduit().equals(Constants.REFERENCE_FREEZONE)) {
					configMap.put(Constants.FREEZONE, true);
				}
			}
			/*
			 * les infos de l'option sera stocke dans l'element de livraison pour s'en servir lors des traitement
			 * suivant.
			 */
			elementLivraison.setOptionInfos(optionsInfos);
		}

		return configMap;
	}

	/**
	 * Traformer un fichier xml en string.
	 * 
	 * @param templateName
	 *            le nom du template xml.
	 * @return le contenu du fichier xml sous format String.
	 * @throws FileNotFoundException
	 *             {@link FileNotFoundException}
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	private static String readTemplateXml(String templateName) throws FileNotFoundException, TopazeException {
		String xmlTemplatePath = getTemplatePath(templateName);
		File xmlFile = new File(xmlTemplatePath);
		Boolean exist = xmlFile.exists();
		if (!exist) {
			throw new FileNotFoundException(PropertiesUtil.getInstance().getErrorMessage("0.1.9", xmlTemplatePath));
		}
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(xmlTemplatePath));
			return new String(encoded, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Unsupported Encoding Exception", e);
			throw new TopazeException(e.getMessage(), e);
		} catch (IOException e1) {
			LOGGER.error(e1);
			throw new TopazeException(e1.getMessage(), e1);
		}
	}

	/**
	 * retourner les options parmi les fils d'un element contractuel.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param numEC
	 *            numero de l'element contractuel.
	 * @return {@link ElementContractuelInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	private List<ElementContractuelInfo> getOptions(String referenceContrat, Integer numEC) throws TopazeException {
		List<ElementContractuelInfo> optionsInfos = new ArrayList<>();
		ElementContractuelInfo[] elementContractuelInfos = restClientPackager.getFilsService(referenceContrat, numEC);
		for (ElementContractuelInfo elementContractuelInfo : elementContractuelInfos) {
			PackagerConfig packagerConfig =
					topazeCatalogAdapter.getPackagerConfig(elementContractuelInfo.getReferenceProduit());
			if (packagerConfig.getTemplate() == null) {
				optionsInfos.add(elementContractuelInfo);
			}
		}
		return optionsInfos;
	}

	/**
	 * Verifier si l'element contient une voip parmi ses fils.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param numEC
	 *            numero de l'element contractuel.
	 * @return true si l'element a une voip parmi ses fils.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	private boolean isContratContientVOIP(String referenceContrat, Integer numEC) throws TopazeException {
		ElementContractuelInfo[] elementContractuelInfos = restClientPackager.getFilsService(referenceContrat, numEC);
		for (ElementContractuelInfo elContractuel : elementContractuelInfos) {
			if (elContractuel.getReferenceProduit().equals(Constants.REFERENCE_VOIP)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param nomTemplate
	 *            nom de la template.
	 * @return le chemain de la template.
	 */
	private static String getTemplatePath(String nomTemplate) {
		String templatePathFolder = Constants.TEMPLATE_PATH_FOLDER;
		return templatePathFolder + (templatePathFolder.endsWith(File.separator) ? "" : File.separator) + nomTemplate;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws ConverterException
	 * @throws DriverException
	 * @throws PackagerException
	 * @throws NotFoundException
	 * @throws NullException
	 */
	@Override
	public void activerPourRenouvellement(BonPreparation bonRenouvellement)
			throws TopazeException, NullException, NotFoundException, PackagerException, DriverException,
			ConverterException {
		for (ElementLivraison elementLivraison : bonRenouvellement.getElementLivraisons()) {
			if (elementLivraison.getActeur() == OutilLivraison.PACKAGER) {
				TState tState = packagerAdapter.getPackagerInstance(elementLivraison).getCurrentState();
				if (tState.equals(TState.ACTIVE) || tState.equals(TState.ACTIVABLE) || tState.equals(TState.INPROGRESS)) {
					restClientPackager.marquerELRenouvele(elementLivraison);

				}

				else if (tState.equals(TState.SUSPENDED)) {

					TPackagerRequest packagerRequest = new TPackagerRequest();
					packagerRequest.setRetailerPackagerId(elementLivraison.getRetailerPackagerId());
					try {
						LOGGER.info("Activer Packager pour la service: " + elementLivraison.getReferenceProduit()
								+ " dont le retailer packager id est : " + elementLivraison.getRetailerPackagerId());
						getPackagerService().reactivatePackager(packagerRequest);
						restClientPackager.marquerELRenouvele(elementLivraison);

						getTracageService().ajouterTrace(
								Constants.PRODUCT,
								elementLivraison.getReference().split("-")[0],
								"acivation du service " + elementLivraison.getReference()
										+ " - appel packager: 'cancelPackager'", Constants.INTERNAL_USER);
					} catch (NullException | NotFoundException | PackagerException | ConverterException
							| MalformedXMLException | NotRespectedRulesException | DriverException exception) {
						try {
							LOGGER.error("error occurs during call of  PackagerServiceImpl.reactivatePackager()",
									exception);
							getSendAlert().send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
									"error occurs during call of " + "PackagerServiceImpl.reactivatePackager()",
									exception.getMessage());
						} catch (Exception e) {
							LOGGER.error("fail to send alert", e);
							throw new TopazeException("fail to send alert", e);
						}
					}
				}

			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void packagerSuspension(String referenceBP) throws TopazeException {
		BonPreparation bonRetour = restClientPackager.getBonRecuperation(referenceBP);
		suspendService(bonRetour, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void traductionPourCession(String referenceBP) throws TopazeException {
		BonPreparation bonPreparation = restClientPackager.getBonPreparation(referenceBP);
		traductionPourCession(bonPreparation);
	}

	@Override
	public void activerPourRenouvellement(String referenceBP)
			throws TopazeException, NullException, NotFoundException, PackagerException, DriverException,
			ConverterException {
		/*
		 * renouvellement des services.
		 */
		BonPreparation bonPreparation = restClientPackager.getBonPreparation(referenceBP);
		// appel vers packager pour activer les services qui sont a l'etat SUSPENDED
		activerPourRenouvellement(bonPreparation);
	}

	/**
	 * Get send alert.
	 * 
	 * @return {@link #sendAlert}
	 */
	private SendAlert getSendAlert() {
		if (this.sendAlert == null) {
			if (System.getProperty("alert.useMock").equals("true")) {
				sendAlert = (SendAlert) ApplicationContextHolder.getBean("sendAlertMock");
			} else {
				sendAlert = (SendAlert) ApplicationContextHolder.getBean("sendAlert");
			}
		}
		return sendAlert;
	}
}