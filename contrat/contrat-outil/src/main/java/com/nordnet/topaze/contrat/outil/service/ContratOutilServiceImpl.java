package com.nordnet.topaze.contrat.outil.service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nordnet.architecture.exceptions.explicit.WebServiceException;
import nordnet.client.ws.netequipment.WebServiceNetEquipment;
import nordnet.client.ws.netequipment.WebServiceNetEquipmentMethods;
import nordnet.client.ws.netequipment.requests.REquipment;
import nordnet.client.ws.netequipment.requests.RReturnAuthorization;
import nordnet.client.ws.netequipment.types.TEquipment;
import nordnet.packager.PackagerClient;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nordnet.adminpackager.ConverterException;
import com.nordnet.adminpackager.DriverException;
import com.nordnet.adminpackager.NotFoundException;
import com.nordnet.adminpackager.NullException;
import com.nordnet.adminpackager.PackagerException;
import com.nordnet.topaze.client.rest.RestClientContratOutil;
import com.nordnet.topaze.client.rest.business.contrat.ElementStateInfo;
import com.nordnet.topaze.client.rest.business.contrat.ElementsRenouvellemtnInfo;
import com.nordnet.topaze.client.rest.enums.TypeProduit;
import com.nordnet.topaze.contrat.outil.mock.NetEquipmentMock;
import com.nordnet.topaze.contrat.outil.mock.PackagerMock;
import com.nordnet.topaze.contrat.outil.mock.WebServiceNetRetourMock;
import com.nordnet.topaze.contrat.outil.util.PropertiesUtil;
import com.nordnet.topaze.exception.TopazeException;

/**
 * implementation du {@link ContratOutilService}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Service("contratOutilService")
public class ContratOutilServiceImpl implements ContratOutilService {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(ContratOutilServiceImpl.class);

	/**
	 * {@link RestClientContratOutil}.
	 */
	@Autowired
	private RestClientContratOutil restClientContratOutil;

	/**
	 * {@link com.nordnet.adminpackager.PackagerService}.
	 */
	private com.nordnet.adminpackager.PackagerService packagerService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean validerSerialNumber(String referenceContrat, String idClient) throws TopazeException {
		try {
			WebServiceNetEquipmentMethods netEquipmentClient = null;
			if (System.getProperty("ws.netEquipment.useMock").equals("true")) {
				netEquipmentClient = new NetEquipmentMock();
			} else {
				WebServiceNetEquipment webServiceNetEquipment = new WebServiceNetEquipment();
				netEquipmentClient = webServiceNetEquipment.getWebServiceClient();
			}
			REquipment requestEquipment = new REquipment();
			requestEquipment.setCustomerId(idClient);
			requestEquipment.setSubscriptionId(referenceContrat);
			RReturnAuthorization requestReturnAuthorization = new RReturnAuthorization();
			TEquipment[] equipments =
					netEquipmentClient.searchEquipments(requestEquipment, requestReturnAuthorization, 5);
			List<TEquipment> contratEquipment = filterBySubscriptionId(Arrays.asList(equipments), referenceContrat);
			Map<String, String> equipementInfo = new HashMap<>();
			for (TEquipment equipment : contratEquipment) {
				String serialNumber = equipementInfo.get(equipment.getEquipmentType().getProductRef());
				if (serialNumber != null && !serialNumber.equals(equipment.getSerialNumber())) {
					throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.100",
							equipment.getEquipmentType().getProductRef(), idClient), "1.1.100");
				}
				equipementInfo.put(equipment.getEquipmentType().getProductRef(), equipment.getSerialNumber());
			}

		} catch (RemoteException | WebServiceException e) {
			LOGGER.error("error occurs during call of netEquipment", e);
			throw new TopazeException("Erreur dans l'appel vers netEquipment", e.getLocalizedMessage());
		}

		return true;

	}

	/**
	 * method pour filter la liste des equipement par subscriptionID.
	 */
	private static List<TEquipment> filterBySubscriptionId(List<TEquipment> tEquipmentList, String subscriptionId) {
		List<TEquipment> tEquipmentFiltred = new ArrayList<>();

		for (TEquipment tEquipment : tEquipmentList) {
			if (tEquipment.getShippingInformations()[(tEquipment.getShippingInformations().length - 1)]
					.getSubscriptionId().equals(subscriptionId)) {
				tEquipmentFiltred.add(tEquipment);
			}
		}
		return tEquipmentFiltred;

	}

	/**
	 * @throws ConverterException
	 * @throws DriverException
	 * @throws PackagerException
	 * @throws NotFoundException
	 * @throws NullException
	 * @throws TopazeException
	 * 
	 */
	@Override
	public Object[] getElementsState(ElementsRenouvellemtnInfo elementsRenouvellemtnInfo)
			throws NullException, NotFoundException, PackagerException, DriverException, ConverterException,
			TopazeException {

		// mock net retour
		WebServiceNetRetourMock netRetourMock = new WebServiceNetRetourMock();

		List<ElementStateInfo> elementStateInfos = elementsRenouvellemtnInfo.getElementStateInfos();
		TypeProduit typeProduit = elementsRenouvellemtnInfo.getTypeProduit();

		if (elementStateInfos.size() > 0 && typeProduit.equals(TypeProduit.SERVICE)) {

			// on fait appel vers livraison core pour recuperer le code produit de chaque element
			elementStateInfos = restClientContratOutil.getElementCodeProduits(elementsRenouvellemtnInfo);

			// on fait appel vers packager pour recuperer l'etat chaque element
			for (ElementStateInfo elementStateInfo : elementStateInfos) {
				elementStateInfo.setTState(getPackagerService().getPackagerInstance(elementStateInfo.getCodeProduit())
						.getCurrentState().toString());
			}

		}

		else if (elementStateInfos.size() > 0 && typeProduit.equals(TypeProduit.BIEN)) {

			// on fait appel vers livraison core pour recuperer le code produit de chaque element
			elementStateInfos = restClientContratOutil.getElementCodeColis(elementsRenouvellemtnInfo);

			// on fait appel vers netRetour pour recuperer l'etat chaque element
			for (ElementStateInfo elementStateInfo : elementStateInfos) {

				if (netRetourMock.checkBienRecupere(elementStateInfo.getCodeColis())) {
					elementStateInfo.setRetourne(true);
				} else {
					elementStateInfo.setRetourne(false);
				}

			}

		}
		return elementStateInfos.toArray();

	}

	/**
	 * @return {@link #packagerService}.
	 */
	public com.nordnet.adminpackager.PackagerService getPackagerService() {
		if (packagerService == null) {
			PackagerClient packagerClient = null;
			if (System.getProperty("ws.packager.useMock").equals("true")) {
				packagerClient =
						PackagerClient.builder().withMockedPort(new PackagerMock()).enableMocking()
								.build(PackagerClient.class);
			} else {
				packagerClient =
						PackagerClient
								.builder()
								.withWsseCredentials(System.getProperty("ws.packager.wsseUser"),
										System.getProperty("ws.packager.wssePwd"))
								.withConnectionTimeout(
										Long.valueOf(System.getProperty("ws.packager.connectionTimeout")))
								.withReceiveTimeout(Long.valueOf(System.getProperty("ws.packager.receiveTimeout")))
								.withLogger(System.getProperty("ws.packager.Logger"))
								.withEndpoint(System.getProperty("ws.packager.endpoint"))
								.withServers(System.getProperty("ws.packager.servers").split(",")).disableMocking()
								.build(PackagerClient.class);
			}

			packagerService = packagerClient.service();
		}
		return packagerService;
	}

}
