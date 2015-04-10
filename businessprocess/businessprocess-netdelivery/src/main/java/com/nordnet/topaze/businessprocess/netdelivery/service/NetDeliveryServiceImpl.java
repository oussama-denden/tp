package com.nordnet.topaze.businessprocess.netdelivery.service;

import java.util.ArrayList;
import java.util.List;

import nordnet.customer.NetCustomerClient;
import nordnet.delivery.NetDelivery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nordnet.backoffice.ws.NetCustomer;
import com.nordnet.backoffice.ws.types.customer.Handle;
import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.generic.types.TCivility;
import com.nordnet.net_delivery.ConverterException;
import com.nordnet.net_delivery.NetDeliveryException;
import com.nordnet.net_delivery.NotFoundException;
import com.nordnet.net_delivery.NotRespectedRulesException;
import com.nordnet.net_delivery.NullException;
import com.nordnet.net_delivery.types.TArrayOfString;
import com.nordnet.net_delivery.types.TArrayOfString.Items;
import com.nordnet.net_delivery.types.TArrayOfTProperty;
import com.nordnet.net_delivery.types.TDeliveryAddress;
import com.nordnet.net_delivery.types.TProperty;
import com.nordnet.net_delivery.types.TReceiverContact;
import com.nordnet.net_delivery.types.TShippingDemand;
import com.nordnet.net_delivery.types.TShippingStatus;
import com.nordnet.topaze.businessprocess.netdelivery.mock.NetCustomerMock;
import com.nordnet.topaze.businessprocess.netdelivery.mock.NetDeliveryMock;
import com.nordnet.topaze.businessprocess.netdelivery.util.Constants;
import com.nordnet.topaze.businessprocess.netdelivery.util.PropertiesUtil;
import com.nordnet.topaze.businessprocess.netdelivery.util.spring.ApplicationContextHolder;
import com.nordnet.topaze.client.rest.RestClientNetDelivery;
import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.client.rest.enums.OutilLivraison;
import com.nordnet.topaze.client.rest.enums.TypeBonPreparation;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.logger.service.TracageService;

/**
 * implementation du {@link NetDeliveryService}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Service("netDeliveryService")
public class NetDeliveryServiceImpl implements NetDeliveryService {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(NetDeliveryServiceImpl.class);

	/**
	 * {@link RestClientNetDelivery}.
	 */
	@Autowired
	private RestClientNetDelivery restClientNetDelivery;

	/**
	 * {@link nordnet.client.ws.netdelivery.impl.NetDeliveryService}.
	 */
	private com.nordnet.net_delivery.NetDeliveryService netDeliveryService;

	/**
	 * {@link NetCustomer}.
	 */
	private NetCustomer netCustomerService;

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
	public void traductionNetDelivery(String referenceBP, TypeBonPreparation typeBonPreparation) throws TopazeException {

		try {
			BonPreparation bonPreparationGlobal = null;
			if (typeBonPreparation.equals(TypeBonPreparation.LIVRAISON)) {
				bonPreparationGlobal = restClientNetDelivery.getBonPreparation(referenceBP);
			} else if (typeBonPreparation.equals(TypeBonPreparation.MIGRATION)) {
				bonPreparationGlobal = restClientNetDelivery.getBonMigration(referenceBP);
			} else {
				throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.1.1", "TypeBonPreparation"),
						"1.1.93");
			}
			ArrayList<String> equipments = new ArrayList<>();

			ElementLivraison elementLivraisonAdress = null;
			for (ElementLivraison elementLivraison : bonPreparationGlobal.getElementLivraisons()) {
				if (elementLivraison.getActeur().equals(OutilLivraison.NETDELIVERY)) {

					equipments.add(elementLivraison.getReferenceProduit());
					elementLivraisonAdress = elementLivraison;
				}

			}

			if (equipments.size() > 0 && elementLivraisonAdress != null) {
				Handle handle = getNetCustomerClient().getHandleByKey(elementLivraisonAdress.getAddresseLivraison());

				TDeliveryAddress address = new TDeliveryAddress();
				address.setAddress1(handle.getAddress1().getValue());
				address.setAddress2(handle.getAddress2().getValue());
				// ERR_TODO address3 n'existe pas du cote netCustomer
				// address.setAddress3("address3");
				address.setZipCode(handle.getZipCode().getValue());
				address.setCountry(handle.getCountry().getValue());
				address.setCedexCode("cedexCode");
				address.setCedexLabel("cedexLabel");

				TReceiverContact receiverContact = new TReceiverContact();
				receiverContact.setId(Long.valueOf(bonPreparationGlobal.getIdClient()));
				receiverContact.setFirstName(handle.getFirstName().getValue());
				receiverContact.setLastName(handle.getName().getValue());
				receiverContact.setEmail(handle.getEmail().getValue());
				receiverContact.setPhoneNumber(handle.getPhone().getValue());
				receiverContact.setCivility(TCivility.fromValue(handle.getCivility().getValue()));
				receiverContact.setAddress(address);

				TArrayOfString shippers = new TArrayOfString();

				/*
				 * Lors de l'appel à NetDelivery, il faut fournir le paramètre suivant : packager.retailerPackagerId =
				 * {refContrat} or refContrat est la même que le ref de bon de preparation.
				 */
				TProperty property1 = new TProperty();
				property1.setName(Constants.PACKAGER_RETAILER_PACKAGER_ID);
				property1.setValue(bonPreparationGlobal.getReference());

				TArrayOfTProperty.Properties properties = new TArrayOfTProperty.Properties();
				List<TProperty> propertiesList = properties.getProperty();
				propertiesList.add(property1);
				TArrayOfTProperty pShippingProperties = new TArrayOfTProperty();
				pShippingProperties.setProperties(properties);

				Items items = new Items();
				List<String> itemsList = items.getItem();
				itemsList.addAll(equipments);

				// This attribute contains all the equipments which are known into the Mock NetDelivery web services.
				TArrayOfString pEquipments = new TArrayOfString();
				pEquipments.setItems(items);

				TShippingDemand demand =
						getNetDeliveryService().createShippingDemand("valid_template1",
								bonPreparationGlobal.getIdClient(), receiverContact, pEquipments, shippers,
								pShippingProperties);

				for (ElementLivraison elementLivraison : bonPreparationGlobal.getElementLivraisons()) {
					if (elementLivraison.getActeur().equals(OutilLivraison.NETDELIVERY)) {
						elementLivraison.setDatePreparation(demand.getCreationDate().toGregorianCalendar().getTime());
						elementLivraison.setCodeColis(String.valueOf(demand.getId()));

					}
					// tracer l'operation
					getTracageService().ajouterTrace(
							Constants.PRODUCT,
							bonPreparationGlobal.getReference(),
							"Livraison du contrat " + bonPreparationGlobal.getReference()
									+ " – Début de livraison du produit " + elementLivraison.getReferenceProduit(),
							Constants.INTERNAL_USER);
				}
				restClientNetDelivery.marquerBienPreparer(bonPreparationGlobal);
			}

		} catch (NetDeliveryException | NullException | NotFoundException | ConverterException
				| NotRespectedRulesException netDeliveryException) {
			LOGGER.error("Error occurs during call of NetDeliveryServiceImpl.traductionNetDelivery ",
					netDeliveryException);
			try {
				getSendAlert().send(
						System.getProperty(Constants.PRODUCT_ID),
						"Unable to perform request",
						"error occurs during call of "
								+ "NetDeliveryServiceImpl.getNetDeliveryService().createShippingDemand()",
						netDeliveryException.getMessage());
			} catch (Exception e) {
				LOGGER.error("fail to send alert", e);
				throw new TopazeException(e.getMessage(), e);
			}
		} catch (Exception exception) {
			LOGGER.error("Error occurs during call of NetDeliveryServiceImpl.traductionNetDelivery ", exception);
			throw new TopazeException(exception.getLocalizedMessage(), exception.getLocalizedMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NetDeliveryException
	 * @throws ConverterException
	 * @throws NotFoundException
	 * @throws NullException
	 * @throws NumberFormatException
	 * 
	 */
	@Override
	public Boolean checkBienLivre(ElementLivraison elementLivraison)
			throws TopazeException, NotFoundException, NetDeliveryException, NumberFormatException, NullException,
			ConverterException {

		try {
			TShippingDemand demand;

			demand = getNetDeliveryService().getShippingDemand(Long.valueOf(elementLivraison.getCodeColis()));

			TShippingStatus status = demand.getCurrentStatus();

			if (status.equals(TShippingStatus.DELIVERED)) {
				// tracer l'operation
				getTracageService().ajouterTrace(
						Constants.PRODUCT,
						elementLivraison.getReference().split("-")[0],
						"Livraison du contrat " + elementLivraison.getReference().split("-")[0]
								+ " – Fin de livraison du produit " + elementLivraison.getReferenceProduit(),
						Constants.INTERNAL_USER);
				return true;
			} else if (status.equals(TShippingStatus.PROBLEM)) {
				throw new TopazeException("Bien non livre a cause de depassement de delai maximale", "0.5.2");
			} else {
				return false;
			}

		} catch (NetDeliveryException | NullException | NotFoundException | ConverterException netDeliveryException) {
			LOGGER.error("Error occurs during call of NetDeliveryServiceImpl.traductionNetDelivery ",
					netDeliveryException);
			try {
				getSendAlert().send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
						"error occurs during call of " + "NetDeliveryServiceImpl.checkBienLivre()",
						netDeliveryException.getMessage());
			} catch (Exception e) {
				LOGGER.error("fail to send alert", e);
				throw new TopazeException(e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * @return {@link #netDeliveryService}.
	 */
	public com.nordnet.net_delivery.NetDeliveryService getNetDeliveryService() {

		if (netDeliveryService == null) {
			NetDelivery netDelivery = null;
			if (System.getProperty("ws.netDelivery.useMock").equals("true")) {
				netDelivery =
						NetDelivery.builder().withMockedPort(new NetDeliveryMock()).enableMocking()
								.build(NetDelivery.class);
			} else {
				netDelivery =
						NetDelivery
								.builder()
								.withWsseCredentials(System.getProperty("ws.netDelivery.wsseUser"),
										System.getProperty("ws.netDelivery.wssePwd"))
								.withConnectionTimeout(
										Long.valueOf(System.getProperty("ws.netDelivery.connectionTimeout")))
								.withReceiveTimeout(Long.valueOf(System.getProperty("ws.netDelivery.receiveTimeout")))
								.withLogger(System.getProperty("ws.netDelivery.Logger"))
								.withEndpoint(System.getProperty("ws.netDelivery.endpoint"))
								.withServers(System.getProperty("ws.netDelivery.servers").split(",")).disableMocking()
								.build(NetDelivery.class);
			}
			netDeliveryService = netDelivery.service();
		}
		return netDeliveryService;
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
								.withLogger(System.getProperty("ws.netCustomer.Logger"))
								.withEndpoint(System.getProperty("ws.netCustomer.endpoint"))
								.withServers(System.getProperty("ws.netCustomer.servers").split(",")).disableMocking()
								.build(NetCustomerClient.class);
			}
			netCustomerService = netCustomerClient.service();
		}
		return netCustomerService;
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