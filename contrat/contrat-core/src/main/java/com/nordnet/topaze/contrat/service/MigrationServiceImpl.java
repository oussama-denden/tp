package com.nordnet.topaze.contrat.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nordnet.topaze.client.rest.RestClientContratCore;
import com.nordnet.topaze.client.rest.business.contrat.ElementStateInfo;
import com.nordnet.topaze.client.rest.business.contrat.ElementsRenouvellemtnInfo;
import com.nordnet.topaze.client.rest.enums.TypeContrat;
import com.nordnet.topaze.client.rest.enums.TypeProduit;
import com.nordnet.topaze.contrat.business.AnnulationInfo;
import com.nordnet.topaze.contrat.business.ClientInfo;
import com.nordnet.topaze.contrat.business.ContratCession;
import com.nordnet.topaze.contrat.business.ContratMigrationInfo;
import com.nordnet.topaze.contrat.business.ContratMigrationSimulationInfo;
import com.nordnet.topaze.contrat.business.ContratReduction;
import com.nordnet.topaze.contrat.business.ContratRenouvellementInfo;
import com.nordnet.topaze.contrat.business.ContratValidationInfo;
import com.nordnet.topaze.contrat.business.ECMigrationSimulationInfo;
import com.nordnet.topaze.contrat.business.Frais;
import com.nordnet.topaze.contrat.business.FraisMigrationSimulation;
import com.nordnet.topaze.contrat.business.PolitiqueCession;
import com.nordnet.topaze.contrat.business.PolitiqueResiliation;
import com.nordnet.topaze.contrat.business.Produit;
import com.nordnet.topaze.contrat.business.ProduitCession;
import com.nordnet.topaze.contrat.business.ProduitMigration;
import com.nordnet.topaze.contrat.business.ProduitRenouvellement;
import com.nordnet.topaze.contrat.business.ReductionInfo;
import com.nordnet.topaze.contrat.business.ReductionMigration;
import com.nordnet.topaze.contrat.domain.Avenant;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ContratHistorique;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.domain.ElementContractuelHistorique;
import com.nordnet.topaze.contrat.domain.FraisContrat;
import com.nordnet.topaze.contrat.domain.Modification;
import com.nordnet.topaze.contrat.domain.PolitiqueMigration;
import com.nordnet.topaze.contrat.domain.PolitiqueRenouvellement;
import com.nordnet.topaze.contrat.domain.Reduction;
import com.nordnet.topaze.contrat.domain.TypeAvenant;
import com.nordnet.topaze.contrat.domain.TypeResiliation;
import com.nordnet.topaze.contrat.jms.ContratMessagesSender;
import com.nordnet.topaze.contrat.repository.AvenantRepository;
import com.nordnet.topaze.contrat.repository.ContratHistoriqueRepository;
import com.nordnet.topaze.contrat.repository.ContratRepository;
import com.nordnet.topaze.contrat.repository.ElementContractuelRepository;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.ContratUtils;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.contrat.util.spring.ApplicationContextHolder;
import com.nordnet.topaze.contrat.validator.ContratValidator;
import com.nordnet.topaze.contrat.validator.ReductionValidator;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.logger.service.TracageService;

/**
 * L'implementation de service {@link MigrationService}.
 * 
 * @author anisselmane.
 * 
 */
@Service("migrationService")
public class MigrationServiceImpl implements MigrationService {

	/**
	 * Loggeur sur la Classe ContratServiceImpl.
	 */
	private static final Logger LOGGER = Logger.getLogger(MigrationServiceImpl.class);

	/**
	 * le contrat repository. {@link ContratRepository}.
	 */
	@Autowired
	private ContratRepository contratRepository;

	/**
	 * le contrat service. {@link ContratService}.
	 */
	@Autowired
	private ContratService contratService;

	/**
	 * le resiliation service. {@link ResiliationService}.
	 */
	@Autowired
	private ResiliationService resiliationService;

	/**
	 * l avenant repository. {@link AvenantRepository}.
	 */
	@Autowired
	private AvenantRepository avenantRepository;

	/**
	 * {@link ContratHistoriqueRepository}.
	 */
	@Autowired
	private ContratHistoriqueRepository contratHistoriqueRepository;

	/**
	 * {@link ElementContractuelRepository}.
	 */
	@Autowired
	private ElementContractuelRepository elementContractuelRepository;

	/**
	 * Le contrat messages sender. {@link ContratMessagesSender}.
	 */
	@Autowired
	private ContratMessagesSender contratMessagesSender;

	/**
	 * le reduction service. {@link reductionService}.
	 */
	@Autowired
	private ReductionService reductionService;

	/**
	 * {@link RestClientContratCore}.
	 */
	@Autowired
	private RestClientContratCore restClientContratCore;

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
	 * 
	 * @throws JSONException
	 * 
	 */
	@Override
	public void migrerContrat(String referenceContrat, ContratMigrationInfo contratMigrationInfo)
			throws JsonProcessingException, TopazeException, CloneNotSupportedException, JSONException {

		LOGGER.info("Entrer methode migrerContrat");

		ContratValidator.checkUser(contratMigrationInfo.getUser());
		Contrat contratGlobal = contratRepository.findByReference(referenceContrat);
		ContratValidator.checkExist(referenceContrat, contratGlobal);

		ContratValidator.checkMigration(contratGlobal);

		// tester si la migration est possible :pas de migration ,cession ou renouvellement future
		checkActionInFuture(referenceContrat, Constants.ZERO, true, false);

		// debut creation avenant.
		Avenant avenant = new Avenant();

		ContratValidator.checkPolitiqueMigration(contratMigrationInfo.getPolitiqueMigration());

		getTracageService()
				.ajouterTrace(
						Constants.PRODUCT,
						referenceContrat,
						"Demande de migration pour le contrat " + referenceContrat + " "
								+ contratMigrationInfo.getPolitiqueMigration().getCommentaire(),
						contratMigrationInfo.getUser());

		for (ProduitMigration produitMigration : contratMigrationInfo.getProduitsMigration()) {
			// Valider les lignes de produits migration.
			ElementContractuel elementContractuel =
					elementContractuelRepository.findByNumECAndReferenceContrat(produitMigration.getNumEC(),
							referenceContrat);
			ContratValidator.checkProduitMigration(produitMigration, elementContractuel);
		}

		if (contratMigrationInfo.getPolitiqueMigration().getDateAction() != null) {
			avenant.setDateModification(contratMigrationInfo.getPolitiqueMigration().getDateAction());

		} else {
			avenant.setDateModification(PropertiesUtil.getInstance().getDateDuJour().toDate());
		}

		// verifer que la date de migration ne depasse pas la date du fin contrat
		ContratValidator.checkActionPossibleInDate(avenant.getDateModification(), contratGlobal.getMaxDateFinDuree(),
				referenceContrat, "Migration", false);

		List<Modification> modifications = new ArrayList<>();
		for (ProduitMigration produitMigration : contratMigrationInfo.getProduitsMigration()) {

			// affecter les reduction globales.
			List<ReductionMigration> reductionMigrations = new ArrayList<>();
			for (ReductionMigration reductionMigration : contratMigrationInfo.getReductions()) {
				reductionMigrations.add(reductionMigration);
			}
			produitMigration.setReductionsGlobale(reductionMigrations);

			Modification modification = new Modification();

			modification.setNumEC(produitMigration.getNumEC());

			ObjectMapper mapper = new ObjectMapper();
			modification.setTrameJson(mapper.writeValueAsString(produitMigration));
			modification.setAvenant(avenant);
			modifications.add(modification);

		}
		avenant.setModifContrat(modifications);
		avenant.setTypeAvenant(TypeAvenant.MIGRATION);

		// règles de préparation.
		List<Produit> produits = ContratUtils.mappingToProduit(contratMigrationInfo.getProduitsMigration());
		ContratValidator.validerProduitsPourPreparationContrat(produits, true);

		// règles validant la validation d’un contrat.

		Contrat contratPostMigration =
				contratService.preparerContrat(produits, contratMigrationInfo.getUser(), true, referenceContrat, true);

		contratPostMigration.setIdClient(contratGlobal.getIdClient());
		contratPostMigration.setSegmentTVA(contratGlobal.getSegmentTVA());
		ElementContractuel elementContractuelOld = (ElementContractuel) contratGlobal.getSousContrats().toArray()[0];
		ElementContractuel elementContractuelMigre =
				(ElementContractuel) contratPostMigration.getSousContrats().toArray()[0];
		elementContractuelMigre.setIdAdrFacturation(elementContractuelOld.getIdAdrFacturation());
		elementContractuelMigre.setIdAdrLivraison(elementContractuelOld.getIdAdrLivraison());

		ContratValidationInfo contratValidationInfo =
				ContratUtils.createContratValidationInfoFromContratMigrationInfo(contratPostMigration,
						contratMigrationInfo);
		ContratValidator.checkPolitiqueValidation(contratValidationInfo);
		ContratValidator.validerInfoValidationContratGlobal(contratValidationInfo,
				contratPostMigration.getSousContrats());
		// valider reductions globale.

		for (ReductionMigration reduction : contratMigrationInfo.getReductions()) {
			ReductionValidator.validerReduction(contratPostMigration, reduction.toReduction());
		}

		// valider reductions par EC.

		for (ProduitMigration produitMigration : contratMigrationInfo.getProduitsMigration()) {
			ElementContractuel elementEnReduction = null;
			for (ElementContractuel elementContractuel : contratPostMigration.getSousContrats()) {
				if (elementContractuel.getNumEC() == produitMigration.getNumEC()) {
					elementEnReduction = elementContractuel;
					break;
				}
			}
			for (ReductionMigration reduction : produitMigration.getReductions()) {
				ReductionValidator.validerReduction(elementEnReduction, reduction.toReduction());
			}
		}

		// persister la politique de migration.
		PolitiqueMigration politiqueMigration = contratMigrationInfo.getPolitiqueMigration().toDomain();
		politiqueMigration.setUser(contratMigrationInfo.getUser());
		avenant.setPolitiqueMigration(politiqueMigration);

		// Tester si c'est une migration future.
		if (!politiqueMigration.isMigrationFutur()) {

			// Historisation de l'ancien contrat.
			ContratHistorique contratHistorique = contratService.historiser(contratGlobal);

			// resiliation totale du contrat.
			PolitiqueResiliation politiqueResiliation = politiqueMigration.getPRFromPM();

			resiliationService.resilierContrat(referenceContrat, politiqueResiliation, contratMigrationInfo.getUser(),
					true, false, false, false, false, false);

			contratRepository.delete(contratGlobal);
			// creer l avenant en relation avec l ancien contrat.
			avenant.setReferenceContrat(contratHistorique.getReference());
			avenant.setVersion(contratHistorique.getVersion());
			avenantRepository.save(avenant);

			// preparation du nouveau contrat.
			contratService.preparerContrat(produits, contratMigrationInfo.getUser(), true, referenceContrat, false);

			Contrat contratMigre = contratRepository.findByReference(referenceContrat);

			// Appliquer les reductions contrat.
			if (!politiqueMigration.isReductionAncienne()) {
				// appliquer les nouvelles reductions reductions globales.
				for (ReductionMigration reduction : contratMigrationInfo.getReductions()) {
					ContratReduction contratReduction = new ContratReduction();
					contratReduction.setUser(contratMigrationInfo.getUser());
					contratReduction.setReduction(reduction.toReduction());
					reductionService.ajouterReductionContrat(contratReduction, referenceContrat);
				}

				// Reduction par EC.
				Integer numECReduction = null;
				for (ProduitMigration produitMigration : contratMigrationInfo.getProduitsMigration()) {
					for (ReductionMigration reduction : produitMigration.getReductions()) {
						for (ElementContractuel elementContractuel : contratMigre.getSousContrats()) {
							if (elementContractuel.getNumEC() == produitMigration.getNumEC()) {
								numECReduction = elementContractuel.getNumEC();
								break;
							}
						}

						ContratReduction contratReduction = new ContratReduction();
						contratReduction.setUser(contratMigrationInfo.getUser());
						contratReduction.setReduction(reduction.toReduction());
						reductionService.ajouterReductionElementContractuelle(contratReduction, referenceContrat,
								numECReduction);

					}
				}

			} else {

				ContratHistorique contratHistoriqueReduction =
						contratHistoriqueRepository.findDerniereVersion(referenceContrat);
				Reduction reduction = null;
				Integer numECReduction = null;

				List<Reduction> reductionGlobales =
						reductionService.findReductionGlobales(contratHistoriqueReduction.getReference(),
								contratHistoriqueReduction.getVersion());

				// Reduction globale.
				for (Reduction reductionGlobale : reductionGlobales) {

					reductionGlobale = reductionGlobale.clone();
					reductionGlobale.setId(null);
					reductionGlobale.setVersion(null);
					ContratReduction contratReduction = new ContratReduction();
					contratReduction.setUser(contratMigrationInfo.getUser());
					contratReduction.setReduction(reductionGlobale);

					try {
						reductionService.ajouterReductionContrat(contratReduction, referenceContrat);

					} catch (TopazeException e) {
						LOGGER.error("reduction invalide : " + e.getMessage());
						throw new TopazeException("reduction invalide", e);
					}
				}

				// appliquer la reduction à l element contractuel et globale.
				for (ProduitMigration produitMigration : contratMigrationInfo.getProduitsMigration()) {

					for (ElementContractuelHistorique contractuelHistorique : contratHistoriqueReduction
							.getSousContrats()) {

						if (contractuelHistorique.getNumEC() == produitMigration.getNumEC()) {
							reduction =
									reductionService.findReductionPartiel(contratHistoriqueReduction.getReference(),
											contratHistoriqueReduction.getVersion(), contractuelHistorique.getNumEC());

							numECReduction = contractuelHistorique.getNumEC();
							break;
						}
					}

					// Reduction par EC.
					if (reduction != null) {
						reduction = reduction.clone();
						reduction.setId(null);
						reduction.setVersion(null);
						ContratReduction contratReduction = new ContratReduction();
						contratReduction.setUser(contratMigrationInfo.getUser());
						contratReduction.setReduction(reduction);

						try {
							reductionService.ajouterReductionElementContractuelle(contratReduction, referenceContrat,
									numECReduction);
						} catch (TopazeException e) {
							LOGGER.error("reduction invalide : " + e.getMessage());
							throw new TopazeException("reduction invalide", e);
						}
					}

				}
			}

			// copier les données dynamiques des elments inchanges et changer la datedebut facturation des elements
			// migés
			recupererInfoPourMigration(referenceContrat);
			// validation du nouveau contrat.
			contratValidationInfo.setMigration(true);
			contratService.validerContrat(referenceContrat, contratValidationInfo);

			// envoi de l'evt contratMigrer pour la livraison.
			contratMessagesSender.sendContratMigratedEvent(referenceContrat);
			// envoi de l'evt contratMigrer pour la facturation.
			contratMessagesSender.sendContratMigratedBillingEvent(referenceContrat);
			getTracageService().ajouterTrace(Constants.PRODUCT, referenceContrat,
					"Contrat " + referenceContrat + " migré", contratMigrationInfo.getUser());
		} else {

			// creer l avenant en relation avec l ancien contrat.
			avenant.setReferenceContrat(contratGlobal.getReference());
			avenantRepository.save(avenant);
		}

		LOGGER.info("Fin methode migrerContrat");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws JSONException
	 * 
	 */
	@Override
	public void migrerContratFutur(final Contrat contrat)
			throws TopazeException, IOException, CloneNotSupportedException, JSONException {
		LOGGER.info("Entrer methode migrerContratFutur");

		Avenant avenant =
				avenantRepository.findByReferenceContratAndVersionAndTypeAvenant(contrat.getReference(), null,
						TypeAvenant.MIGRATION);

		PolitiqueMigration politiqueMigration = avenant.getPolitiqueMigration();

		// Historisation de l'ancien contrat.
		contratRepository.flush();
		final Contrat foundContrat = contratRepository.findByReference(contrat.getReference());
		ContratHistorique contratHistorique = contratService.historiser(foundContrat);

		// resiliation totale du contrat.
		PolitiqueResiliation politiqueResiliation = politiqueMigration.getPRFromPM();
		politiqueResiliation.setTypeResiliation(TypeResiliation.RIC);

		resiliationService.resilierContrat(foundContrat.getReference(), politiqueResiliation, Constants.INTERNAL_USER,
				true, false, true, false, false, false);

		contratRepository.delete(foundContrat);

		// creer l avenant en relation avec l ancien contrat.
		avenant.setReferenceContrat(contratHistorique.getReference());
		avenant.setVersion(contratHistorique.getVersion());
		avenantRepository.save(avenant);

		List<ProduitMigration> produitMigrations = new ArrayList<>();

		for (Modification modification : avenant.getModifContrat()) {
			ProduitMigration produitMigration =
					ProduitMigration.fromJsonToProduitMigration(modification.getTrameJson());
			produitMigrations.add(produitMigration);

		}

		// preparation du nouveau contrat.
		contratService.preparerContrat(ContratUtils.mappingToProduit(produitMigrations), Constants.INTERNAL_USER, true,
				foundContrat.getReference(), false);

		Contrat contratMigre = contratRepository.findByReference(contrat.getReference());

		// Appliquer les reductions contrat.
		if (!politiqueMigration.isReductionAncienne()) {
			// appliquer les nouvelles reductions reductions globales.
			for (ReductionMigration reduction : produitMigrations.get(0).getReductionsGlobale()) {
				ContratReduction contratReduction = new ContratReduction();
				contratReduction.setUser(Constants.INTERNAL_USER);
				contratReduction.setReduction(reduction.toReduction());
				reductionService.ajouterReductionContrat(contratReduction, contrat.getReference());
			}

			// Reduction par EC.
			Integer numECReduction = null;
			for (ProduitMigration produitMigration : produitMigrations) {
				for (ReductionMigration reduction : produitMigration.getReductions()) {
					for (ElementContractuel elementContractuel : contratMigre.getSousContrats()) {
						if (elementContractuel.getNumEC() == produitMigration.getNumEC()) {
							numECReduction = elementContractuel.getNumEC();
							break;
						}
					}

					ContratReduction contratReduction = new ContratReduction();
					contratReduction.setUser(Constants.INTERNAL_USER);
					contratReduction.setReduction(reduction.toReduction());
					reductionService.ajouterReductionElementContractuelle(contratReduction, contrat.getReference(),
							numECReduction);

				}
			}

		} else {

			ContratHistorique contratHistoriqueReduction =
					contratHistoriqueRepository.findDerniereVersion(contrat.getReference());
			Reduction reduction = null;
			Integer numECReduction = null;

			// appliquer la reduction globale

			List<Reduction> reductionGlobales =
					reductionService.findReductionGlobales(contratHistoriqueReduction.getReference(),
							contratHistoriqueReduction.getVersion());

			// Reduction globale.
			for (Reduction reductionGlobale : reductionGlobales) {
				reductionGlobale = reductionGlobale.clone();
				reductionGlobale.setId(null);
				reductionGlobale.setVersion(null);
				ContratReduction contratReduction = new ContratReduction();
				contratReduction.setUser(Constants.INTERNAL_USER);
				contratReduction.setReduction(reductionGlobale);
				try {
					reductionService.ajouterReductionContrat(contratReduction, contrat.getReference());
				} catch (TopazeException e) {
					LOGGER.error("reduction invalide : " + e.getMessage());
					throw new TopazeException("reduction invalide", e);
				}
			}

			// appliquer la reduction à l element contractuel.
			for (ProduitMigration produitMigration : produitMigrations) {

				for (ElementContractuelHistorique contractuelHistorique : contratHistoriqueReduction.getSousContrats()) {

					if (contractuelHistorique.getNumEC() == produitMigration.getNumEC()) {
						reduction =
								reductionService.findReductionPartiel(contratHistoriqueReduction.getReference(),
										contratHistoriqueReduction.getVersion(), contractuelHistorique.getNumEC());

						numECReduction = contractuelHistorique.getNumEC();
						break;
					}
				}

				// Reduction par EC.
				if (reduction != null) {
					reduction = reduction.clone();
					reduction.setId(null);
					reduction.setVersion(null);
					ContratReduction contratReduction = new ContratReduction();
					contratReduction.setUser(Constants.INTERNAL_USER);
					contratReduction.setReduction(reduction);
					try {
						reductionService.ajouterReductionElementContractuelle(contratReduction, contrat.getReference(),
								numECReduction);
					} catch (TopazeException e) {
						LOGGER.error("reduction invalide : " + e.getMessage());
						throw new TopazeException("reduction invalide", e);
					}
				}

			}
		}

		// copier les données dynamiques des elments inchanges et changer la datedebut facturation des elements
		// migés
		recupererInfoPourMigration(contrat.getReference());
		// validation du nouveau contrat.
		ContratMigrationInfo contratMigrationInfo = new ContratMigrationInfo();
		contratMigrationInfo.setProduitsMigration(produitMigrations);
		contratMigrationInfo.setPolitiqueMigration(politiqueMigration.toBusiness());
		contratMigrationInfo.setUser(politiqueMigration.getUser());
		ContratValidationInfo contratValidationInfo =
				ContratUtils.createContratValidationInfoFromContratMigrationInfo(foundContrat, contratMigrationInfo);
		contratValidationInfo.setMigration(true);

		contratService.validerContrat(contrat.getReference(), contratValidationInfo);

		// envoi de l'evt contratMigrer pour la livraison.
		contratMessagesSender.sendContratMigratedEvent(contrat.getReference());
		// envoi de l'evt contratMigrer pour la facturation.
		contratMessagesSender.sendContratMigratedBillingEvent(contrat.getReference());
		LOGGER.info("Fin methode migrerContratFutur");
	}

	/**
	 * {@inheritDoc }.
	 */
	@Override
	public void annulerMigration(String referenceContrat, AnnulationInfo migrationAnnulationInfo)
			throws JsonProcessingException, TopazeException {

		LOGGER.info("Entrer methode annulerMigration");
		Contrat contratGlobal = contratRepository.findByReference(referenceContrat);
		Avenant avenant = avenantRepository.findAvenantAvecMigrationActive(referenceContrat);
		ContratValidator.checkAvenantExist(avenant);
		PolitiqueMigration politiqueMigration = avenant.getPolitiqueMigration();
		ContratValidator.checkExist(referenceContrat, contratGlobal);
		// traçage de l'operation.
		getTracageService().ajouterTrace(Constants.PRODUCT, referenceContrat,
				"Annulation de la demande de migration pour le contrat " + referenceContrat,
				migrationAnnulationInfo.getUser());

		ContratValidator.checkPolitiqueMigrationExist(politiqueMigration, referenceContrat);
		ContratValidator.checkAnnulationMigrationPossible(politiqueMigration);

		avenant.setDateAnnulation(PropertiesUtil.getInstance().getDateDuJour().toDate());

		avenant.setCommentaireAnnulation(migrationAnnulationInfo.getCommentaire());
		avenantRepository.save(avenant);

		LOGGER.info("Fin methode annulerMigration");

	}

	/**
	 * {@inheritDoc }.
	 */
	@Override
	public void cederContrat(String referenceContrat, ContratCession contratCession)
			throws TopazeException, JsonProcessingException {

		Contrat contrat = contratRepository.findByReference(referenceContrat);
		// tester si la cession est possible :pas de migration ,resiliation,cession ou renouvellement future
		checkActionInFuture(referenceContrat, Constants.ZERO, false, false);

		ContratValidator.checkExist(referenceContrat, contrat);
		ContratValidator.checkContratPourCession(contrat);
		ContratValidator.validerContratCession(contratCession, contrat);
		PolitiqueCession politiqueCessionBusiness = contratCession.getPolitiqueCession();

		getTracageService().ajouterTrace(
				Constants.PRODUCT,
				referenceContrat,
				"Cession du contrat " + referenceContrat + " de " + contratCession.getClientSource().getIdClient()
						+ " au " + contratCession.getClientSource().getIdClient() + " "
						+ contratCession.getPolitiqueCession().getCommentaire(), contratCession.getUser());
		ContratValidator.checkUser(contratCession.getUser());

		/*
		 * creation de l'avenant.
		 */
		Avenant avenant = new Avenant();
		avenant.setTypeAvenant(TypeAvenant.CESSION);
		if (contratCession.getPolitiqueCession().getDateAction() != null) {
			avenant.setDateModification(contratCession.getPolitiqueCession().getDateAction());
		} else {
			avenant.setDateModification(PropertiesUtil.getInstance().getDateDuJour().toDate());
		}

		// verifer que la date de migration ne depasse pas la date du fin contrat
		ContratValidator.checkActionPossibleInDate(avenant.getDateModification(), contrat.getMaxDateFinDuree(),
				referenceContrat, "Cession", false);
		Modification modification = new Modification();
		modification.setTrameJson(contratCession.getClientDestination().toJson());
		modification.setAvenant(avenant);
		List<Modification> modifications = new ArrayList<>();
		modifications.add(modification);
		for (ProduitCession produitCession : contratCession.getProduitsCession()) {
			modification = new Modification();
			modification.setNumEC(produitCession.getNumEC());
			modification.setTrameJson(produitCession.toJson());
			modification.setAvenant(avenant);
			modifications.add(modification);
		}
		avenant.setModifContrat(modifications);
		com.nordnet.topaze.contrat.domain.PolitiqueCession pCession = politiqueCessionBusiness.toDomain();
		avenant.setPolitiqueCession(pCession);
		if (politiqueCessionBusiness.isCessionFuture()) {
			avenant.setReferenceContrat(contrat.getReference());
			avenantRepository.save(avenant);
			contratRepository.save(contrat);
		} else {

			/*
			 * historiser l'ancient contrat.
			 */
			ContratHistorique contratHistorique = contratService.historiser(contrat);

			/*
			 * resiliation du contrat
			 */
			PolitiqueResiliation politiqueResiliation = politiqueCessionBusiness.toDomain().getPRFromPC();
			politiqueResiliation.setTypeResiliation(TypeResiliation.RIC);
			resiliationService.resilierContrat(contrat.getReference(), politiqueResiliation, contratCession.getUser(),
					false, true, false, false, false, false);
			contratRepository.delete(contrat);

			avenant.setReferenceContrat(contratHistorique.getReference());
			avenant.setVersion(contratHistorique.getVersion());
			avenantRepository.save(avenant);

			/*
			 * sauvegarde du nouveau contrat.
			 */
			ClientInfo clientDestination = contratCession.getClientDestination();
			contrat.setIdClient(clientDestination.getIdClient());
			contrat.setDateDebutFacturation(null);
			contrat.setDateFinContrat(null);
			contrat.setTypeResiliation(null);
			contrat.setPolitiqueResiliation(null);
			contrat.setPolitiqueValidation(contrat.getPolitiqueValidation().copy());
			Set<ElementContractuel> elementContractuels = contrat.getSousContrats();
			for (ElementContractuel elementContractuel : elementContractuels) {
				elementContractuel.setId(null);
				elementContractuel.setTypeResiliation(null);
				elementContractuel.setIdAdrFacturation(clientDestination.getIdAdrFacturation());
				if (!elementContractuel.getTypeContrat().equals(TypeContrat.VENTE)) {
					elementContractuel.setDateDebutFacturation(null);
					elementContractuel.setDateFinContrat(null);
					elementContractuel.setDateDerniereFacture(null);
					elementContractuel.setDateFinEngagement(null);
					for (ProduitCession produitCession : contratCession.getProduitsCession()) {
						if (produitCession.getNumEC() == elementContractuel.getNumEC()) {
							elementContractuel.setReferenceModePaiement(produitCession.getReferenceModePaiement());
							elementContractuel.setModePaiement(produitCession.getModePaiement());
							if (produitCession.getEngagement() != null) {
								elementContractuel.setEngagement(produitCession.getEngagement());
								elementContractuel.setDateFinEngagement(null);
							}

							if (produitCession.getDuree() != null) {
								elementContractuel.setDuree(produitCession.getDuree());
							}
						}
					}
				}

				if (pCession.isConserverAncienneReduction()) {

					/*
					 * conserver la reduction partiel.
					 */
					Reduction reductionPartiel =
							reductionService.findReductionPartiel(contratHistorique.getReference(),
									contratHistorique.getVersion(), elementContractuel.getNumEC());
					if (reductionPartiel != null && reductionPartiel.isEligible() && !reductionPartiel.isAnnule()) {
						reductionPartiel = reductionPartiel.copy();
						reductionPartiel.setVersion(null);
						reductionService.save(reductionPartiel);
					}

				}
			}

			if (pCession.isConserverAncienneReduction()) {

				/*
				 * conserver la reduction global.
				 */
				List<Reduction> reductionGlobales =
						reductionService.findReductionGlobales(contratHistorique.getReference(),
								contratHistorique.getVersion());
				for (Reduction reductionGlobal : reductionGlobales) {
					if (!reductionGlobal.isAnnule()) {
						reductionGlobal = reductionGlobal.copy();
						reductionGlobal.setVersion(null);
						reductionService.save(reductionGlobal);
					}
				}
			}
			contratRepository.save(contrat);

			/*
			 * envoi d'un event de cession.
			 */
			contratMessagesSender.sendContratSuccessionBillingEvent(referenceContrat);
			contratMessagesSender.sendContratSuccessionEvent(referenceContrat);

		}

	}

	/**
	 * {@inheritDoc }.
	 */
	@SuppressWarnings("null")
	@Override
	public void cessionFuture(Contrat contrat) throws IOException, TopazeException {

		Avenant avenant = avenantRepository.findAvenantAvecCessionActive(contrat.getReference());
		getTracageService().ajouterTrace(
				Constants.PRODUCT,
				contrat.getReference(),
				"Cession du contrat " + contrat.getReference() + " - cession programmÃ© depuis le "
						+ avenant.getDateModification().toString(), Constants.INTERNAL_USER);
		ClientInfo clientDestination = null;
		Map<Integer, ProduitCession> produitsCessionMap = new HashMap<>();
		for (Modification modification : avenant.getModifContrat()) {
			if (modification.getNumEC() != null) {
				produitsCessionMap.put(modification.getNumEC(),
						ProduitCession.fromJsonToProduitCession(modification.getTrameJson()));
			} else {
				clientDestination = ClientInfo.fromJsonToClientInfo(modification.getTrameJson());
			}
		}
		com.nordnet.topaze.contrat.domain.PolitiqueCession politiqueCession = avenant.getPolitiqueCession();

		/*
		 * historiser l'ancient contrat.
		 */
		ContratHistorique contratHistorique = contratService.historiser(contrat);

		/*
		 * resiliation de l'ancient contrat.
		 */
		resiliationService.resilierContrat(contrat.getReference(), politiqueCession.getPRFromPC(),
				Constants.INTERNAL_USER, false, true, false, true, false, false);
		contratRepository.delete(contrat);

		avenant.setReferenceContrat(contratHistorique.getReference());
		avenant.setVersion(contratHistorique.getVersion());
		contrat.setIdClient(clientDestination.getIdClient());
		contrat.setDateDebutFacturation(null);
		contrat.setDateFinContrat(null);
		contrat.setPolitiqueResiliation(null);
		contrat.setTypeResiliation(null);
		contrat.setPolitiqueValidation(contrat.getPolitiqueValidation().copy());
		Set<ElementContractuel> elementContractuels = contrat.getSousContrats();
		for (ElementContractuel elementContractuel : elementContractuels) {
			elementContractuel.setId(null);
			elementContractuel.setTypeResiliation(null);
			elementContractuel.setIdAdrFacturation(clientDestination.getIdAdrFacturation());
			if (!elementContractuel.getTypeContrat().equals(TypeContrat.VENTE)) {
				elementContractuel.setDateDebutFacturation(null);
				elementContractuel.setDateFinContrat(null);
				elementContractuel.setDateDerniereFacture(null);
				ProduitCession produitCession = produitsCessionMap.get(elementContractuel.getNumEC());
				elementContractuel.setReferenceModePaiement(produitCession.getReferenceModePaiement());
				elementContractuel.setModePaiement(produitCession.getModePaiement());
				if (produitCession.getEngagement() != null) {
					elementContractuel.setEngagement(produitCession.getEngagement());
					elementContractuel.setDateFinEngagement(null);
				}

				if (produitCession.getDuree() != null) {
					elementContractuel.setDuree(produitCession.getDuree());
				}
			}

			if (politiqueCession.isConserverAncienneReduction()) {

				/*
				 * conserver la reduction partiel.
				 */
				Reduction reductionPartiel =
						reductionService.findReductionPartiel(contratHistorique.getReference(),
								contratHistorique.getVersion(), elementContractuel.getNumEC());
				if (reductionPartiel != null && reductionPartiel.isEligible() && !reductionPartiel.isAnnule()) {
					reductionPartiel = reductionPartiel.copy();
					reductionPartiel.setVersion(null);
					reductionService.save(reductionPartiel);
				}
			}
		}

		if (politiqueCession.isConserverAncienneReduction()) {

			/*
			 * conserver les reductions globales.
			 */
			List<Reduction> reductionGlobales =
					reductionService.findReductionGlobales(contratHistorique.getReference(),
							contratHistorique.getVersion());
			for (Reduction reductionGlobal : reductionGlobales) {
				if (!reductionGlobal.isAnnule()) {
					reductionGlobal = reductionGlobal.copy();
					reductionGlobal.setVersion(null);
					reductionService.save(reductionGlobal);
				}
			}
		}

		contratRepository.save(contrat);
		avenantRepository.save(avenant);

		/*
		 * envoi d'un event de cession.
		 */
		contratMessagesSender.sendContratSuccessionBillingEvent(contrat.getReference());
		contratMessagesSender.sendContratSuccessionEvent(contrat.getReference());

	}

	/**
	 * {@inheritDoc }.
	 */
	@Override
	public void annulerCession(String referenceContrat, AnnulationInfo annulationCessionInfo) throws TopazeException {

		Contrat contrat = contratRepository.findByReference(referenceContrat);
		ContratValidator.checkExist(referenceContrat, contrat);

		Avenant avenant = avenantRepository.findAvenantAvecCessionActive(referenceContrat);

		// traçage de l'operation.
		getTracageService().ajouterTrace(Constants.PRODUCT, referenceContrat,
				"Annulation de la demande de cession pour le contrat " + referenceContrat,
				annulationCessionInfo.getUser());
		ContratHistorique contratHistorique = contratHistoriqueRepository.findDerniereVersion(referenceContrat);
		Avenant avenantHistorise = null;
		if (contratHistorique != null) {
			avenantHistorise =
					avenantRepository.findByReferenceContratAndVersionAndTypeAvenant(referenceContrat,
							contratHistorique.getVersion(), TypeAvenant.CESSION);
		}
		ContratValidator.checkAnnulationCession(avenant, avenantHistorise);
		avenant.setDateAnnulation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		avenant.setCommentaireAnnulation(annulationCessionInfo.getCommentaire());
		avenantRepository.save(avenant);
	}

	/**
	 * {@inheritDoc }.
	 */
	@Override
	public ClientInfo getInfoAvantCession(String referenceContrat) throws TopazeException {
		ContratHistorique contratHistorique = contratHistoriqueRepository.findDerniereVersion(referenceContrat);
		ContratValidator.checkExist(referenceContrat, contratHistorique);
		ClientInfo clientInfo = new ClientInfo();
		clientInfo.setIdClient(contratHistorique.getIdClient());
		for (ElementContractuelHistorique elementContractuelHistorique : contratHistorique.getSousContrats()) {
			clientInfo.setIdAdrFacturation(elementContractuelHistorique.getIdAdrFacturation());
			break;
		}
		return clientInfo;
	}

	/**
	 * cette methode set conçue pour synchroniser les données entre l'ancien contrat et le conrat migre.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @throws TopazeException
	 * @{@link TopazeException}.
	 */
	private void recupererInfoPourMigration(String referenceContrat) throws TopazeException {

		Contrat contratMigre = contratRepository.findByReference(referenceContrat);
		ContratHistorique contratHistorique = contratHistoriqueRepository.findDerniereVersion(referenceContrat);
		Avenant avenant =
				avenantRepository.findByReferenceContratAndVersionAndTypeAvenant(contratHistorique.getReference(),
						contratHistorique.getVersion(), TypeAvenant.MIGRATION);
		boolean isMigreAdministrativement = true;
		for (ElementContractuel elementContractuel : contratMigre.getSousContrats()) {

			for (ElementContractuelHistorique elementContractuelHistorique : contratHistorique.getSousContrats()) {

				// tester si les deux elementx ont le mm numEC et le mm refrence produit donc l'element n'est pas change
				if (elementContractuel.getNumEC() == elementContractuelHistorique.getNumEC()
						&& elementContractuel.getReferenceProduit().equals(
								elementContractuelHistorique.getReferenceProduit())) {

					if (!elementContractuel.isMigrationAdministrative(elementContractuelHistorique)) {
						isMigreAdministrativement = false;
						elementContractuel.setDateDebutFacturation(elementContractuelHistorique
								.getDateDebutFacturation());
						elementContractuel
								.setDateDerniereFacture(elementContractuelHistorique.getDateDerniereFacture());
						elementContractuel.setModeFacturation(elementContractuelHistorique.getModeFacturation());

					} else {
						if (elementContractuel.getMontant().equals(elementContractuelHistorique.getMontant())) {
							elementContractuel.setDateDebutFacturation(elementContractuelHistorique
									.getDateDebutFacturation());
							elementContractuel.setDateDerniereFacture(elementContractuelHistorique
									.getDateDerniereFacture());

						} else {
							elementContractuel.setDateDebutFacturation(PropertiesUtil.getInstance().getDateDuJour()
									.toDate());
						}

					}

					elementContractuel.setModeFacturation(elementContractuelHistorique.getModeFacturation());

					elementContractuelRepository.save(elementContractuel);

				}

				// tester si les deux elementx ont le mm numEC et avec un refrence produit different donc l'element est
				// change
				else if (elementContractuel.getNumEC() == elementContractuelHistorique.getNumEC()
						&& !elementContractuel.getReferenceProduit().equals(
								elementContractuelHistorique.getReferenceProduit())) {
					elementContractuel.setDateDebutFacturation(avenant.getDateModification());
					elementContractuelRepository.save(elementContractuel);

				}

			}

		}
		if (isMigreAdministrativement) {
			contratMigre.setDateDebutFacturation(PropertiesUtil.getInstance().getDateDuJour().toDate());
			contratRepository.save(contratMigre);
		}
	}

	/**
	 * {@inheritDoc }
	 * 
	 * @throws JsonProcessingException
	 * @throws CloneNotSupportedException
	 * @throws JSONException
	 * 
	 */
	@Override
	public void renouvelerContrat(String referenceContrat, ContratRenouvellementInfo contratRenouvellementInfo)
			throws TopazeException, JsonProcessingException, CloneNotSupportedException, JSONException {

		LOGGER.info("Entrer methode renouvelerContrat");

		// valider l'existance du contrat ainsi que l'utilisateur qui a passe la commande
		ContratValidator.checkUser(contratRenouvellementInfo.getUser());
		Contrat contratGlobal = contratRepository.findByReference(referenceContrat);

		isContratRenouvelable(referenceContrat, contratRenouvellementInfo.getProduitRenouvellements());

		// verfifer le politique de renouvellement
		ContratValidator.checkPolitiqueRenouvellement(contratRenouvellementInfo.getPolitiqueRenouvellement());

		getTracageService().ajouterTrace(
				Constants.PRODUCT,
				referenceContrat,
				"Demande de renouvellement  pour le contrat" + referenceContrat + " "
						+ contratRenouvellementInfo.getPolitiqueRenouvellement().getCommentaire(),
				contratRenouvellementInfo.getUser());
		// traçage de l'action
		getTracageService().ajouterTrace(Constants.PRODUCT, referenceContrat,
				"Renouvellement du contrat " + referenceContrat + " – Préparation de le renouvellement",
				contratRenouvellementInfo.getUser());

		// debut creation avenant.
		Avenant avenant = new Avenant();
		if (contratGlobal.isResilier()) {
			avenant.setDateModification(PropertiesUtil.getInstance().getDateDuJour().toDate());
		}
		// creer la liste des modifications
		List<Modification> modifications = new ArrayList<>();
		for (ProduitRenouvellement produitRenouvellement : contratRenouvellementInfo.getProduitRenouvellements()) {
			Modification modification = new Modification();
			modification.setNumEC(produitRenouvellement.getNumEC());
			ObjectMapper mapper = new ObjectMapper();
			modification.setTrameJson(mapper.writeValueAsString(produitRenouvellement));
			modification.setAvenant(avenant);
			modifications.add(modification);
		}
		avenant.setModifContrat(modifications);
		avenant.setTypeAvenant(TypeAvenant.RENOUVELLEMENT);

		// règles de préparation.
		List<Produit> produits =
				ContratUtils.mappingRenouvelleToProduit(contratRenouvellementInfo.getProduitRenouvellements());
		ContratValidator.validerProduitsPourPreparationContrat(produits, false);

		// preparer le contrat
		Contrat contratPostRenouvellement =
				contratService.preparerContrat(produits, contratRenouvellementInfo.getUser(), true, referenceContrat,
						true);
		contratPostRenouvellement.setIdClient(contratGlobal.getIdClient());
		contratPostRenouvellement.setSegmentTVA(contratGlobal.getSegmentTVA());

		// creer les régles de validation
		ContratValidationInfo contratValidationInfo =
				ContratUtils.createContratValidationInfoFromContratRenouvellementInfo(contratGlobal,
						contratRenouvellementInfo);
		ContratValidator.checkPolitiqueValidation(contratValidationInfo);
		ContratValidator.validerInfoValidationContratGlobal(contratValidationInfo,
				contratPostRenouvellement.getSousContrats());

		// persister la politique de renouvellement.
		PolitiqueRenouvellement politiqueRenouvellement =
				contratRenouvellementInfo.getPolitiqueRenouvellement().toDomain();
		politiqueRenouvellement.setUser(contratRenouvellementInfo.getUser());

		// politiqueRenouvellement.setUser(contratRenouvellementInfo.getUser());
		avenant.setPolitiqueRenouvellement(politiqueRenouvellement);

		// tester si le contrat est resilie sinon le renouvellement sera rapporte a la fin du contrat
		if (contratGlobal.isResilier()) {
			// verfifer les etats des produits dans net packager et net retour
			checkEtatProduitRenouvellement(produits, politiqueRenouvellement, referenceContrat);

			// Historisation de l'ancien contrat.
			ContratHistorique contratHistorique = contratService.historiser(contratGlobal);
			contratRepository.delete(contratGlobal);

			// creer l avenant en relation avec l'ancien contrat.
			avenant.setReferenceContrat(contratHistorique.getReference());
			avenant.setVersion(contratHistorique.getVersion());
			avenantRepository.save(avenant);

			// preparation du nouveau contrat.
			Contrat contratRenouvele =
					contratService.preparerContrat(produits, contratRenouvellementInfo.getUser(), true,
							referenceContrat, false);

			// copier les données dynamiques des elments inchanges et changer la datedebut facturation des elements
			// renouvelés
			recupererInfoPourRenouvellement(referenceContrat);

			// conserver les anciennes reductions
			ConserverLesAnciennesReduction(contratRenouvellementInfo.getProduitRenouvellements(),
					politiqueRenouvellement, referenceContrat);

			// valider le nouveau contrat
			contratService.validerContrat(referenceContrat, contratValidationInfo);

			// envoi de l'evt contratMigrer pour la livraison.
			contratMessagesSender.sendContractRenewalEvent(referenceContrat);

			getTracageService().ajouterTrace(
					Constants.PRODUCT,
					referenceContrat,
					"Contrat " + referenceContrat + " renouvelé pour les produits: "
							+ ContratUtils.traceRenouvellementProduit(contratRenouvele),
					contratRenouvellementInfo.getUser());
		} else {

			// creer l avenant en relation avec l ancien contrat.
			avenant.setReferenceContrat(contratGlobal.getReference());
			avenantRepository.save(avenant);
		}

		LOGGER.info("Fin methode renouvelerContrat");

	}

	@Override
	public void isContratRenouvelable(String referenceContrat, List<ProduitRenouvellement> produitRenouvellements)
			throws TopazeException {

		Contrat contratGlobal = contratRepository.findByReference(referenceContrat);
		ContratValidator.checkExist(referenceContrat, contratGlobal);

		// verfifier si le contrat est valide :on peut pas renouvele un contratqui est pas valide
		ContratValidator.checkRenouvellement(contratGlobal);

		// tester si la migration est possible :pas de migration ,resiliation,cession ou renouvellement future
		checkActionInFuture(referenceContrat, Constants.ZERO, false, true);

		// verifier que les produit dans la trame existe ains qu'ils ont une duree
		for (ProduitRenouvellement produitRenouvellement : produitRenouvellements) {
			ElementContractuel elementContractuel =
					elementContractuelRepository.findByNumECAndReferenceContrat(produitRenouvellement.getNumEC(),
							referenceContrat);

			// tester si l'élément a une resiliation partielle programmé
			ElementContractuel elementContractuelResilier =
					elementContractuelRepository.findECResiliationFuture(referenceContrat,
							produitRenouvellement.getNumEC());
			ContratValidator.checkProduitRenouvellement(produitRenouvellement, elementContractuel,
					elementContractuelResilier, contratGlobal);
		}
	}

	/**
	 * cette methode set conçue pour synchroniser les données entre l'ancien contrat et le conrat migre.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 */
	private void recupererInfoPourRenouvellement(String referenceContrat) {

		Contrat contratRenouvellement = contratRepository.findByReference(referenceContrat);
		ContratHistorique contratHistorique = contratHistoriqueRepository.findDerniereVersion(referenceContrat);
		Avenant avenant =
				avenantRepository.findByReferenceContratAndVersionAndTypeAvenant(contratHistorique.getReference(),
						contratHistorique.getVersion(), TypeAvenant.RENOUVELLEMENT);

		for (ElementContractuel elementContractuel : contratRenouvellement.getSousContrats()) {

			for (ElementContractuelHistorique elementContractuelHistorique : contratHistorique.getSousContrats()) {
				// tester si les deux elementx ont le mm numEC don l'element est renouvelee
				if (elementContractuel.getNumEC() == elementContractuelHistorique.getNumEC()) {
					elementContractuel.setDateDebutFacturation(avenant.getDateModification());
					elementContractuelRepository.save(elementContractuel);
				}

			}

		}
	}

	@Override
	public void renouvelerContratFutur(final Contrat contrat)
			throws TopazeException, IOException, CloneNotSupportedException, JSONException {

		LOGGER.info("Entrer methode renouvelerContratFutur");

		// recuperer l'avenant associe au renouvellement
		Avenant avenant = avenantRepository.findAvenantAvecRenouvellementActive(contrat.getReference());

		// recuperer le poltitque de renouvellement
		PolitiqueRenouvellement politiqueRenouvellement = avenant.getPolitiqueRenouvellement();

		// creer la liste des produits de renouvellements apartir du modification
		List<ProduitRenouvellement> produitRenouvellements = new ArrayList<>();
		for (Modification modification : avenant.getModifContrat()) {
			ProduitRenouvellement produitRenouvellement =
					ProduitRenouvellement.fromJsonToProduitRenouvellement(modification.getTrameJson());
			produitRenouvellements.add(produitRenouvellement);

		}

		// creer la liste des produits apartir de la liste des modifications
		List<Produit> produits = ContratUtils.mappingRenouvelleToProduit(produitRenouvellements);

		// verfifer les etats des produits dans net packager et net retour
		checkEtatProduitRenouvellement(produits, politiqueRenouvellement, contrat.getReference());

		// Historisation de l'ancien contrat.
		contratRepository.flush();
		final Contrat foundContrat = contratRepository.findByReference(contrat.getReference());
		ContratHistorique contratHistorique = contratService.historiser(foundContrat);

		// resiliation totale du contrat.
		PolitiqueResiliation politiqueResiliation = ContratUtils.getPRFromPR();
		politiqueResiliation.setTypeResiliation(TypeResiliation.RIC);

		resiliationService.resilierContrat(contrat.getReference(), politiqueResiliation, Constants.INTERNAL_USER, true,
				false, false, false, false, true);

		contratRepository.delete(foundContrat);

		// creer l avenant en relation avec l ancien contrat.
		avenant.setReferenceContrat(contratHistorique.getReference());
		avenant.setVersion(contratHistorique.getVersion());
		avenantRepository.save(avenant);

		// preparation du nouveau contrat.
		Contrat contratRenouvele =
				contratService.preparerContrat(produits, Constants.INTERNAL_USER, true, contrat.getReference(), false);

		// copier les données dynamiques des elments inchanges et changer la datedebut facturation des elements
		// renouvelés
		recupererInfoPourRenouvellement(contrat.getReference());

		// validation du nouveau contrat.
		ContratRenouvellementInfo contratRenouvellementInfo = new ContratRenouvellementInfo();
		contratRenouvellementInfo.setProduitRenouvellements(produitRenouvellements);
		contratRenouvellementInfo.setPolitiqueRenouvellement(politiqueRenouvellement.toBuisness());
		contratRenouvellementInfo.setUser(politiqueRenouvellement.getUser());
		ContratValidationInfo contratValidationInfo =
				ContratUtils.createContratValidationInfoFromContratRenouvellementInfo(foundContrat,
						contratRenouvellementInfo);
		contratValidationInfo.setMigration(true);

		// conserver les reduction ancienne
		ConserverLesAnciennesReduction(produitRenouvellements, politiqueRenouvellement, contrat.getReference());

		contratService.validerContrat(contrat.getReference(), contratValidationInfo);

		// envoi de l'evt contratMigrer pour la livraison.
		contratMessagesSender.sendContractRenewalEvent(contrat.getReference());

		getTracageService().ajouterTrace(
				Constants.PRODUCT,
				contrat.getReference(),
				"Contrat " + contrat.getReference() + " renouvelé pour les produits: "
						+ ContratUtils.traceRenouvellementProduit(contratRenouvele),
				contratRenouvellementInfo.getUser());

		LOGGER.info("Fin methode renouvelerContratFutur");

	}

	@SuppressWarnings("null")
	@Override
	public void annulerRenouvellement(String referenceContrat, AnnulationInfo annulationRenouvellementInfo)
			throws TopazeException {
		LOGGER.info("Entrer methode annulerRenouvellement");

		Contrat contratGlobal = contratRepository.findByReference(referenceContrat);
		Avenant avenant = avenantRepository.findAvenantAvecRenouvellementActive(referenceContrat);
		ContratValidator.checkAvenantExist(avenant);
		PolitiqueRenouvellement politiqueRenouvellement = avenant != null ? avenant.getPolitiqueRenouvellement() : null;
		ContratValidator.checkExist(referenceContrat, contratGlobal);
		// traçage de l'operation.
		getTracageService().ajouterTrace(Constants.PRODUCT, referenceContrat,
				"Annulation de la demande de renouvellement pour le contrat " + referenceContrat,
				annulationRenouvellementInfo.getUser());

		ContratValidator.checkPolitiqueRenouvellementExist(politiqueRenouvellement, referenceContrat);
		ContratValidator.checkAnnulationRenouvellementPossible(avenant);

		avenant.setDateAnnulation(PropertiesUtil.getInstance().getDateDuJour().toDate());

		avenant.setCommentaireAnnulation(annulationRenouvellementInfo.getCommentaire());
		avenantRepository.save(avenant);

		LOGGER.info("Fin methode annulerRenouvellement");
	}

	/**
	 * methode qui fait appel vers contrat util qui s'occupe de verifier l'etat de chaque produit dans netreour et net
	 * packager
	 * 
	 * @throws TopazeException
	 * 
	 */
	private void checkEtatProduitRenouvellement(List<Produit> produits,
			PolitiqueRenouvellement politiqueRenouvellement, String referenceContrat) throws TopazeException {

		// préparer les deux listes de bien et de service pour contacter net packger et net retour afin de recuperer
		// les états de chaque element
		List<ElementStateInfo> elementContractuelsService = new ArrayList<>();
		List<ElementStateInfo> elementContractuelsBien = new ArrayList<>();

		for (Produit produitRenouvellement : produits) {
			String reference = referenceContrat + "-" + produitRenouvellement.getNumEC();

			// creer les element state info qui seront communiques au packager , netretounr et livraison core
			ElementStateInfo elementState = new ElementStateInfo();
			if (produitRenouvellement.getTypeProduit().equals(TypeProduit.BIEN)) {
				elementState.setRefenceElementContractuelle(reference);
				elementContractuelsBien.add(elementState);
			} else if (produitRenouvellement.getTypeProduit().equals(TypeProduit.SERVICE)) {
				elementState.setRefenceElementContractuelle(reference);
				elementContractuelsService.add(elementState);
			}
		}

		// valider les biens et les services
		ElementsRenouvellemtnInfo el = new ElementsRenouvellemtnInfo();

		el.setElementStateInfos(elementContractuelsService);
		el.setTypeProduit(TypeProduit.SERVICE);
		elementContractuelsService = restClientContratCore.getElementStates(el);

		el.setElementStateInfos(elementContractuelsBien);
		el.setTypeProduit(TypeProduit.BIEN);
		elementContractuelsBien = restClientContratCore.getElementStates(el);

		ContratValidator.checkProduitRenouvellemntState(elementContractuelsService, elementContractuelsBien,
				politiqueRenouvellement);

	}

	@Override
	public ContratMigrationSimulationInfo getContratInfoPourSimulationMigration(String referenceContrat)
			throws TopazeException {
		ArrayList<ECMigrationSimulationInfo> ecMigrationSimulationInfos = new ArrayList<>();

		Contrat contrat = contratRepository.findByReference(referenceContrat);

		ContratValidator.validerGetContratBillingInformation(referenceContrat, contrat);

		ContratMigrationSimulationInfo contratMigrationSimulationInfo = new ContratMigrationSimulationInfo();
		contratMigrationSimulationInfo.setReferenceContrat(referenceContrat);

		Integer version = null;
		ContratHistorique contratHistorique = contratHistoriqueRepository.findDerniereVersion(referenceContrat);
		if (contratHistorique != null) {
			version = contratHistorique.getVersion();
		}

		for (ElementContractuel elementContractuel : contrat.getSousContrats()) {
			if (elementContractuel.isMigrable()) {

				ECMigrationSimulationInfo ecMigrationSimulationInfo =
						creerECMigrationSimulationInfo(elementContractuel, contrat);
				ecMigrationSimulationInfo.setVersion(version);
				ecMigrationSimulationInfo.setEngagement(elementContractuel.getEngagementMax());
				ecMigrationSimulationInfo.setDateFinEngagement(elementContractuel.getDateFinEngagement());

				if (elementContractuel.getElementContractuelParent() != null) {
					for (ECMigrationSimulationInfo ecMigrationSimulationInfoParent : ecMigrationSimulationInfos) {
						if (elementContractuel.getElementContractuelParent().getNumEC()
								.equals(ecMigrationSimulationInfoParent.getNumEC())) {

							ecMigrationSimulationInfo.setEcParentInfo(ecMigrationSimulationInfoParent);
						}
					}
				}

				if (elementContractuel.getSousElementContractuel() != null) {
					for (ElementContractuel contractuel : elementContractuel.getSousElementContractuel()) {
						for (ECMigrationSimulationInfo ecMigrationSimulationInfoFils : ecMigrationSimulationInfos) {
							if (ecMigrationSimulationInfoFils.getNumEC() == contractuel.getNumEC()) {
								ecMigrationSimulationInfoFils.setEcParentInfo(ecMigrationSimulationInfo);

							}
						}
					}
				}

				contratMigrationSimulationInfo.getPaymentInfos().add(ecMigrationSimulationInfo);
			}
		}

		return contratMigrationSimulationInfo;
	}

	/**
	 * Creer {@link ECMigrationSimulationInfo}.
	 * 
	 * @param elementContractuel
	 *            the element contractuel
	 * @param contrat
	 *            the contrat
	 * @return {@link ECMigrationSimulationInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	private ECMigrationSimulationInfo creerECMigrationSimulationInfo(ElementContractuel elementContractuel,
			Contrat contrat) throws TopazeException {

		ECMigrationSimulationInfo ecMigrationSimulationInfo = new ECMigrationSimulationInfo();
		ecMigrationSimulationInfo.setNumEC(elementContractuel.getNumEC());
		ecMigrationSimulationInfo.setReferenceProduit(elementContractuel.getReferenceProduit());
		ecMigrationSimulationInfo.setTypeContrat(elementContractuel.getTypeContrat());

		if (elementContractuel.getMontant() != null) {
			ecMigrationSimulationInfo.setMontant(elementContractuel.getMontant());
			ecMigrationSimulationInfo.setTypeTVA(elementContractuel.getTypeTVA());
			ecMigrationSimulationInfo.setEngagement(elementContractuel.getEngagement());
			ecMigrationSimulationInfo.setDureeContrat(elementContractuel.getDuree());
			ecMigrationSimulationInfo.setPeriodicite(elementContractuel.getPeriodicite());
			ecMigrationSimulationInfo.setModeFacturation(elementContractuel.getModeFacturation());
			ecMigrationSimulationInfo.getFrais().addAll(getFrais(elementContractuel));
			ecMigrationSimulationInfo.setDureeContrat(elementContractuel.getDuree());
		}

		ecMigrationSimulationInfo.setDateDerniereFacture(elementContractuel.getDateDerniereFacture());
		ecMigrationSimulationInfo.setDateFinContrat(PropertiesUtil.getInstance().getDateDuJour().toDate());
		ecMigrationSimulationInfo.setDateDebutFacturation(contrat.getDateDebutFacturation());
		ecMigrationSimulationInfo.setTypeElement(elementContractuel.getTypeProduit());

		ecMigrationSimulationInfo.setRemboursable(elementContractuel.isRemboursable());

		List<Reduction> reductionGlobales = reductionService.findReductionGlobales(contrat.getReference(), null);
		for (Reduction reductionGlobale : reductionGlobales) {
			if (reductionGlobale.isEligible() && !reductionGlobale.isAnnule()) {
				ReductionInfo reductionContrat = reductionGlobale.toReductionInfo();
				ecMigrationSimulationInfo.addReductionInfoContrat(reductionContrat);
			}
		}

		Reduction reduction =
				reductionService.findReductionPartiel(contrat.getReference(), null, elementContractuel.getNumEC());
		if (reduction != null && reduction.isEligible() && !reduction.isAnnule()) {
			ReductionInfo reductionEC = reduction.toReductionInfo();
			ecMigrationSimulationInfo.setReductionInfoEC(reductionEC);
		}

		return ecMigrationSimulationInfo;
	}

	/**
	 * Creation de liste de {@link Frais} à partir d'une liste de {@link FraisContrat}.
	 * 
	 * @param elementContractuel
	 *            {@link ElementContractuel}.
	 * @return liste de {@link Frais}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	private Set<FraisMigrationSimulation> getFrais(ElementContractuel elementContractuel) throws TopazeException {
		Set<FraisMigrationSimulation> fraisSet = new HashSet<>();
		ReductionInfo info = null;
		for (FraisContrat fraisContrat : elementContractuel.getFrais()) {
			Reduction reductionGlobal =
					reductionService.findReductionGlobalSurFrais(elementContractuel.getContratParent().getReference(),
							null, fraisContrat.getTypeFrais());
			Reduction reductionPartiel =
					reductionService.findReductionPartielSurFrais(elementContractuel.getContratParent().getReference(),
							null, elementContractuel.getNumEC(), fraisContrat.getTypeFrais());
			FraisMigrationSimulation f = new FraisMigrationSimulation();
			f.setMontant(fraisContrat.getMontant());
			f.setNombreMois(fraisContrat.getNombreMois());
			f.setOrdre(fraisContrat.getOrdre());
			f.setTypeFrais(fraisContrat.getTypeFrais());
			f.setTitre(fraisContrat.getTitre());
			if (reductionGlobal != null && reductionGlobal.isEligible() && !reductionGlobal.isAnnule()) {
				info = reductionGlobal.toReductionInfo();
				f.setReductionInfoContrat(info);
			}

			if (reductionPartiel != null && reductionPartiel.isEligible() && !reductionPartiel.isAnnule()) {
				info = reductionPartiel.toReductionInfo();
				f.setReductionInfoEC(info);
			}

			fraisSet.add(f);
		}

		return fraisSet;

	}

	/**
	 * conserveles anciennes reductions qui sont encore valide pour un contrat prevu d'etre renouvele
	 * 
	 * @param politiqueRenouvellement
	 * @throws CloneNotSupportedException
	 * @throws TopazeException
	 * @throws JSONException
	 */
	private void ConserverLesAnciennesReduction(List<ProduitRenouvellement> politiqueRenouvellements,
			PolitiqueRenouvellement politiqueRenouvellement, String referenceContrat)
			throws CloneNotSupportedException, TopazeException, JSONException {

		// conserver les reductions anciennes
		if (politiqueRenouvellement.getConserverAncienneReduction()) {
			ContratHistorique contratHistoriqueReduction =
					contratHistoriqueRepository.findDerniereVersion(referenceContrat);
			Reduction reduction = null;
			Integer numECReduction = null;

			List<Reduction> reductionGlobales =
					reductionService.findReductionGlobales(contratHistoriqueReduction.getReference(),
							contratHistoriqueReduction.getVersion());

			// Reduction globale.
			for (Reduction reductionGlobale : reductionGlobales) {
				reductionGlobale = reductionGlobale.clone();
				reductionGlobale.setId(null);
				reductionGlobale.setVersion(null);
				ContratReduction contratReduction = new ContratReduction();
				contratReduction.setUser(politiqueRenouvellement.getUser());
				contratReduction.setReduction(reductionGlobale);

				try {
					reductionService.ajouterReductionContrat(contratReduction, referenceContrat);

				} catch (TopazeException e) {
					LOGGER.error("reduction invalide : " + e.getMessage());
					throw new TopazeException("reduction invalide", e);
				}
			}

			// appliquer la reduction à l element contractuel et globale.
			for (ProduitRenouvellement produitRenouvellement : politiqueRenouvellements) {

				for (ElementContractuelHistorique contractuelHistorique : contratHistoriqueReduction.getSousContrats()) {

					if (contractuelHistorique.getNumEC() == produitRenouvellement.getNumEC()) {
						reduction =
								reductionService.findReductionPartiel(contratHistoriqueReduction.getReference(),
										contratHistoriqueReduction.getVersion(), contractuelHistorique.getNumEC());

						numECReduction = contractuelHistorique.getNumEC();
						break;
					}
				}

				// Reduction par EC.
				if (reduction != null) {
					reduction = reduction.clone();
					reduction.setId(null);
					reduction.setVersion(null);
					ContratReduction contratReduction = new ContratReduction();
					contratReduction.setUser(politiqueRenouvellement.getUser());
					contratReduction.setReduction(reduction);

					try {
						reductionService.ajouterReductionElementContractuelle(contratReduction, referenceContrat,
								numECReduction);
					} catch (TopazeException e) {
						LOGGER.error("reduction invalide : " + e.getMessage());
						throw new TopazeException("reduction invalide", e);
					}
				}

			}
		}
	}

	/**
	 * etre sur qu'une seule action active existe pour un contrat.
	 * 
	 * @param referenceContrat
	 * @throws TopazeException
	 */
	private void checkActionInFuture(String referenceContrat, Integer numEC, boolean isMigration,
			boolean isRenouvellement) throws TopazeException {
		// tester si le renouvellement est possible :pas de migration ,cession ou renouvellement future
		Avenant avenantMigration = avenantRepository.findAvenantAvecMigrationActive(referenceContrat);
		Avenant avenantCession = avenantRepository.findAvenantAvecCessionActive(referenceContrat);
		Avenant avenantRenouvellement = avenantRepository.findAvenantAvecRenouvellementActive(referenceContrat);
		Contrat contratResilier = contratRepository.findContratResiliationFuture(referenceContrat);
		ElementContractuel elementContractuel =
				elementContractuelRepository.findECResiliationFuture(referenceContrat, numEC);
		List<ElementContractuel> elementContractuels =
				elementContractuelRepository.findListECResiliationFuture(referenceContrat);
		ContratValidator.checkIsActionPossible(referenceContrat, avenantMigration, avenantCession,
				avenantRenouvellement, contratResilier, elementContractuel, numEC, elementContractuels, isMigration,
				isRenouvellement);
	}

}
