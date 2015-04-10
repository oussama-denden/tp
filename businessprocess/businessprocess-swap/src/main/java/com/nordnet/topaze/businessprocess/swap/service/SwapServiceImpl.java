package com.nordnet.topaze.businessprocess.swap.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import nordnet.client.ws.netequipment.WebServiceNetEquipment;
import nordnet.client.ws.netequipment.WebServiceNetEquipmentMethods;
import nordnet.client.ws.netequipment.requests.REquipment;
import nordnet.client.ws.netequipment.requests.RReturnAuthorization;
import nordnet.client.ws.netequipment.types.TEquipment;
import nordnet.client.ws.netequipment.types.TShippingInformation;
import nordnet.customer.NetCustomerClient;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nordnet.backoffice.ws.NetCustomer;
import com.nordnet.backoffice.ws.types.customer.Handle;
import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.swap.ws.types.TAddress;
import com.nordnet.swap.ws.types.TArrayOfShippingEquipment;
import com.nordnet.swap.ws.types.TArrayOfTReturnEquipment;
import com.nordnet.swap.ws.types.TReturnEquipment;
import com.nordnet.swap.ws.types.TReturnEquipmentDemand;
import com.nordnet.swap.ws.types.TShippingEquipment;
import com.nordnet.swap.ws.types.TShippingEquipmentDemand;
import com.nordnet.swap.ws.types.UserInfos;
import com.nordnet.topaze.businessprocess.swap.mock.NetCustomerMock;
import com.nordnet.topaze.businessprocess.swap.mock.NetEquipmentMock;
import com.nordnet.topaze.businessprocess.swap.mock.SwapMock;
import com.nordnet.topaze.businessprocess.swap.mock.TRequestStatus;
import com.nordnet.topaze.businessprocess.swap.util.Constants;
import com.nordnet.topaze.businessprocess.swap.util.spring.ApplicationContextHolder;
import com.nordnet.topaze.client.rest.RestClientSwap;
import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;
import com.nordnet.topaze.client.rest.business.livraison.ClientInfo;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.client.rest.enums.OutilLivraison;
import com.nordnet.topaze.client.rest.enums.TypeProduit;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.logger.service.TracageService;

/**
 * implementation du {@link SwapService}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
@Service("swapService")
public class SwapServiceImpl implements SwapService {

	/**
	 * default logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(SwapServiceImpl.class.getName());

	/**
	 * instance of swap mock.
	 * 
	 */
	@Autowired
	private SwapMock swapMock;

	/**
	 * instance of NetEquipementMock.
	 * 
	 */
	private WebServiceNetEquipmentMethods netEquipmentClient;

	/**
	 * {@link NetCustomer}.
	 */
	private NetCustomer netCustomerService;

	/**
	 * {@link RestClientSwap}.
	 */
	@Autowired
	private RestClientSwap restClientSwap;

	/**
	 * Alert service.
	 */
	SendAlert sendAlert;

	/**
	 * {@link TracageService}.
	 */
	private TracageService tracageService;

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
	@Override
	public boolean consulterEtatLivraisonEM(ElementLivraison elementLivraison) throws TopazeException {

		LOGGER.info("debut methode consulterEtatLivraisonEM");
		try {
			TRequestStatus tRequestStatus = swapMock.getRequestStatus(elementLivraison.getReference());
			if (tRequestStatus.equals(TRequestStatus.INRETURNPROGRESS)) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("Error occurs during call of SwapServiceImpl.consulterEtatLivraisonEM() ", e);
			try {
				getSendAlert().send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
						"error occurs during call of " + "SwapServiceImpl.consulterEtatLivraisonEM()", e.getMessage());
			} catch (Exception exception) {
				LOGGER.error("fail to send alert", exception);
				throw new TopazeException(exception.getMessage(), exception);
			}
		}
		return false;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean consulterEtatRetourEM(ElementLivraison elementLivraison) throws TopazeException {

		LOGGER.info("debut methode consulterEtatRetourEM ");
		try {
			TRequestStatus tRequestStatus = swapMock.getRequestStatus(elementLivraison.getReference());
			if (tRequestStatus.equals(TRequestStatus.CLOSED)) {
				return true;
			}

		} catch (Exception e) {
			LOGGER.error("Error occurs during call of SwapServiceImpl.consulterEtatRetourEM() ", e);
			try {
				getSendAlert().send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
						"error occurs during call of " + "SwapServiceImpl.consulterEtatRetourEM()", e.getMessage());
			} catch (Exception exception) {
				LOGGER.error("fail to send alert", exception);
				throw new TopazeException(exception.getMessage(), exception);
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String recupererSerialNumber(String idClient, String refContrat) throws TopazeException {

		LOGGER.info("debut methode recupererSerialNumber");

		List<TEquipment> tEquipmentList = null;
		REquipment rEquipment = new REquipment();
		rEquipment.setCustomerId(idClient);
		rEquipment.setSubscriptionId(refContrat);
		RReturnAuthorization rReturnAuthorization = new RReturnAuthorization();

		try {

			TEquipment[] tequipments = getNetEquipmentClient().searchEquipments(rEquipment, rReturnAuthorization, 1);

			tEquipmentList = Arrays.asList(tequipments);

			if (tEquipmentList.size() == 1) {
				return tEquipmentList.get(0).getSerialNumber();

			} else if (tEquipmentList.size() > 1) {

				List<TEquipment> tEquipmentListSubscription = filterBySubscriptionId(tEquipmentList, refContrat);
				if (tEquipmentListSubscription.size() == 1) {
					return tEquipmentListSubscription.get(0).getSerialNumber();

				} else if (tEquipmentListSubscription.size() > 1) {

					TEquipment tEquipmentDate = getTheRecent(tEquipmentListSubscription);
					if (tEquipmentDate != null) {
						return tEquipmentDate.getSerialNumber();
					}

				}
			}

		} catch (Exception e) {
			LOGGER.error("Error occurs during call of SwapServiceImpl.recupererSerialNumber() ", e);
			try {
				getSendAlert().send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
						"error occurs during call of " + "SwapServiceImpl.recupererSerialNumber()", e.getMessage());
			} catch (Exception exception) {
				LOGGER.error("fail to send alert", exception);
				throw new TopazeException(exception.getMessage(), exception);
			}
		}
		return null;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void traductionSwap(BonPreparation bonMigration) throws TopazeException {
		try {

			boolean isPrepare = false;
			TReturnEquipmentDemand returnEquipmentDemand = null;
			TReturnEquipment tReturnEquipment = null;
			TArrayOfTReturnEquipment tArrayOfTReturnEquipment = null;
			TShippingEquipmentDemand shippingEquipmentDemand = null;
			TShippingEquipment tShippingEquipment = null;
			TArrayOfShippingEquipment tArrayOfShippingEquipment = null;
			List<TShippingEquipment> tShippingEquipments = new ArrayList<>();
			List<TReturnEquipment> tReturnEquipments = new ArrayList<>();

			Handle handle =
					getNetCustomerClient().getHandleByKey(
							bonMigration.getElementLivraisons().iterator().next().getAddresseLivraison());

			TAddress address = new TAddress();
			address.setAddress1(handle.getAddress1().getValue());
			address.setAddress2(handle.getAddress2().getValue());
			address.setZipCode(handle.getZipCode().getValue());
			address.setCountry(handle.getCountry().getValue());
			for (ElementLivraison elementLivraison : bonMigration.getElementLivraisons()) {
				if (elementLivraison.getActeur().equals(OutilLivraison.SWAP)) {
					// tracer l'operation
					getTracageService().ajouterTrace(
							Constants.PRODUCT,
							bonMigration.getReference(),
							"Migration du contrat " + bonMigration + " – Début de la migration du produit "
									+ elementLivraison.getReferenceAncienProduit() + "vers le produit "
									+ elementLivraison.getReferenceProduit(), Constants.INTERNAL_USER);

					elementLivraison.setCodeColis(recupererSerialNumber(bonMigration.getIdClient(),
							bonMigration.getReference()));

					returnEquipmentDemand = new TReturnEquipmentDemand();
					returnEquipmentDemand.setAddress(address);

					tReturnEquipment = new TReturnEquipment();
					tReturnEquipment.setSerialNumber(elementLivraison.getCodeColis());
					tReturnEquipment.setEquipmentRef(elementLivraison.getReferenceAncienProduit());
					tReturnEquipments.add(tReturnEquipment);

					tArrayOfTReturnEquipment = new TArrayOfTReturnEquipment();
					tArrayOfTReturnEquipment.setItem(tReturnEquipments);

					returnEquipmentDemand.setEquipments(tArrayOfTReturnEquipment);

					shippingEquipmentDemand = new TShippingEquipmentDemand();
					shippingEquipmentDemand.setAddress(address);

					tShippingEquipment = new TShippingEquipment();
					tShippingEquipment.setEquipmentRef(elementLivraison.getReferenceProduit());
					tShippingEquipments.add(tShippingEquipment);

					tArrayOfShippingEquipment = new TArrayOfShippingEquipment();
					tArrayOfShippingEquipment.setEquipment(tShippingEquipments);

					shippingEquipmentDemand.setEquipments(tArrayOfShippingEquipment);

					UserInfos userInfos = new UserInfos();
					userInfos.setHttpUserAgent(bonMigration.getIdClient());

					swapMock.createSwapDemand(userInfos, null, null, returnEquipmentDemand, shippingEquipmentDemand,
							null);

					isPrepare = true;
				}
			}

			if (isPrepare) {
				restClientSwap.marquerBienMigrationPrepare(bonMigration);
			}

		} catch (Exception e) {
			LOGGER.error("Error occurs during call of SwapServiceImpl.traductionSwap() ", e);
			try {
				getSendAlert().send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
						"error occurs during call of " + "SwapServiceImpl.traductionSwap()", e.getMessage());
			} catch (Exception exception) {
				LOGGER.error("fail to send alert", exception);
				throw new TopazeException(exception.getMessage(), exception);
			}
			throw new TopazeException(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cederEquipement(BonPreparation bonPreparation) throws TopazeException {
		try {
			boolean isCede = false;
			ClientInfo clientInfo = restClientSwap.getInfoAvantCession(bonPreparation.getReference());
			REquipment requestEquipment = new REquipment();
			requestEquipment.setCustomerId(clientInfo.getIdClient());
			requestEquipment.setSubscriptionId(bonPreparation.getReference());
			RReturnAuthorization requestReturnAuthorization = new RReturnAuthorization();
			/*
			 * recuperer les equipements de l'ancient client, puis recuperer que les equipements associés au contrat
			 * cedé.
			 */
			TEquipment[] equipments =
					getNetEquipmentClient().searchEquipments(requestEquipment, requestReturnAuthorization, 5);
			List<TEquipment> equipmentContrat =
					filterBySubscriptionId(Arrays.asList(equipments), bonPreparation.getReference());
			for (TEquipment equipment : equipmentContrat) {
				/*
				 * changer l'ancient client par le nouveau.
				 */
				for (TShippingInformation shippingInformation : equipment.getShippingInformations()) {
					shippingInformation.setCustomerId(bonPreparation.getIdClient());
				}
				getNetEquipmentClient().changeEquipment(equipment);
				isCede = true;
			}

			if (isCede) {
				for (ElementLivraison elementLivraison : bonPreparation.getElementLivraisons()) {
					if (elementLivraison.getTypeElement() == TypeProduit.BIEN) {
						restClientSwap.marquerELCede(elementLivraison);
						getTracageService().ajouterTrace(
								Constants.PRODUCT,
								bonPreparation.getReference(),
								"cession du service " + elementLivraison.getReference()
										+ " - appel swap: 'changeEquipment'", Constants.INTERNAL_USER);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occurs during call of SwapServiceImpl.cederEquipement() ", e);
			try {
				getSendAlert().send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
						"error occurs during call of " + "SwapServiceImpl.cederEquipement()", e.getMessage());
			} catch (Exception exception) {
				LOGGER.error("fail to send alert", exception);
				throw new TopazeException(exception.getMessage(), exception);
			}
			throw new TopazeException(e.getLocalizedMessage(), e);
		}

	}

	/**
	 * method pour filter la liste des equipement par subscriptionID.
	 * 
	 * @param equipmentList
	 *            liste des equipements.
	 * @param subscriptionId
	 *            id de souscription.
	 * @return liste des equipement filtre.
	 */
	private static List<TEquipment> filterBySubscriptionId(List<TEquipment> equipmentList, String subscriptionId) {
		List<TEquipment> equipmentFiltred = new ArrayList<>();
		for (TEquipment tEquipment : equipmentList) {
			if (tEquipment.getShippingInformations()[(tEquipment.getShippingInformations().length - 1)]
					.getSubscriptionId().equals(subscriptionId)) {
				equipmentFiltred.add(tEquipment);
			}
		}
		return equipmentFiltred;

	}

	/**
	 * method pour trier les equipements par date et faire sortir le plus recent.
	 * 
	 * @param equipmentList
	 *            liste des equipement.
	 * @return {@link TEquipment}.
	 */
	private static TEquipment getTheRecent(List<TEquipment> equipmentList) {
		List<Date> equipementDates = new ArrayList<>();

		for (TEquipment tEquipment : equipmentList) {
			equipementDates.add(tEquipment.getCreationDate().getTime());
		}
		Collections.sort(equipementDates);
		Date dateRecent = equipementDates.get(equipementDates.size() - 1);
		for (TEquipment tEquipment : equipmentList) {
			if (tEquipment.getCreationDate().getTime().compareTo(dateRecent) == 0) {
				return tEquipment;
			}
		}
		return null;

	}

	/**
	 * Effectue la connexion avec NetCustomer et retourn le service contenant les operations fournit par netCustomer.
	 * 
	 * @return {@link NetCustomer}.
	 */
	public NetCustomer getNetCustomerClient() {
		if (netCustomerService == null) {
			NetCustomerClient netCustomerClient = null;
			if (System.getProperty("ws.netCustomer.useMock").equals("true")) {
				netCustomerClient =
						NetCustomerClient.builder().withMockedPort(new NetCustomerMock()).enableMocking()
								.build(NetCustomerClient.class);
			} else {
				netCustomerClient =
						NetCustomerClient
								.builder()
								.withWsseCredentials(System.getProperty("ws.netCustomer.wsseUser"),
										System.getProperty("ws.netCustomer.wssePwd"))
								.withConnectionTimeout(
										Long.valueOf(System.getProperty("ws.netCustomer.connectionTimeout")))
								.withReceiveTimeout(Long.valueOf(System.getProperty("ws.netCustomer.receiveTimeout")))
								.withLogger(System.getProperty("ws.netCustomer.inLogger"))
								.withServers(System.getProperty("ws.netCustomer.servers").split(",")).disableMocking()
								.build(NetCustomerClient.class);
			}
			netCustomerService = netCustomerClient.service();
		}
		return netCustomerService;
	}

	/**
	 * creation du service netEquipment.
	 * 
	 * @return {@link WebServiceNetEquipmentMethods}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public WebServiceNetEquipmentMethods getNetEquipmentClient() throws TopazeException {
		try {
			if (netEquipmentClient == null) {

				if (System.getProperty("ws.netEquipment.useMock").equals("true")) {
					netEquipmentClient = new NetEquipmentMock();
				} else {
					WebServiceNetEquipment webServiceNetEquipment = new WebServiceNetEquipment();
					netEquipmentClient = webServiceNetEquipment.getWebServiceClient();
				}
			}
		} catch (Exception exception) {
			LOGGER.error("Fail to call WebServiceNetEquipment", exception);
			throw new TopazeException(exception.getMessage(), exception);
		}

		return netEquipmentClient;

	}

	@Override
	public void cederEquipement(String referenceBP) throws TopazeException {
		BonPreparation bonPreparation = restClientSwap.getBonPreparation(referenceBP);
		cederEquipement(bonPreparation);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void traductionSwap(String referenceBP) throws TopazeException {
		// recuperation de bon de migration
		BonPreparation bonMigration = restClientSwap.getBonMigration(referenceBP);
		// marker un BM preparer on cas ou on fait une traduction Swap.
		traductionSwap(bonMigration);
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