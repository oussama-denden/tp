package com.nordnet.topaze.businessprocess.netretour.mock;

import org.apache.log4j.Logger;

import com.nordnet.topaze.businessprocess.netretour.netcustomer.mock.Client;
import com.nordnet.topaze.businessprocess.netretour.netcustomer.mock.Handle;
import com.nordnet.topaze.businessprocess.netretour.netcustomer.mock.NetCustomerMock;
import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;

/**
 * Cette classe est cree afin de prévoir les méthodes appelant NetRetour.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class WebServiceNetRetourMock {

	/**
	 * default logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(WebServiceNetRetourMock.class.getName());

	/**
	 * une méthode appelant NetRetour, afin d'éditer un Bon de Retour des biens.
	 * 
	 * @param idClient
	 *            id client.
	 * @param equipments
	 *            list des equipements.
	 */
	public void createBienRetourRequest(String idClient, String[] equipments) {
		NetCustomerMock netCustomerMock = new NetCustomerMock();

		Client client = netCustomerMock.getByKey(idClient);

		Handle profile = client.getDefaultHandle();

		TDeliveryAddress address =
				new TDeliveryAddress(profile.getAddress1(), profile.getAddress2(), "address3", profile.getZipCode(),
						profile.getZipCode(), profile.getCountry(), "cedexCode", "cedexLabel");

		TReceiverContact receiverContact =
				new TReceiverContact(Long.valueOf(idClient), profile.getFirstName(), profile.getName(),
						profile.getCompany(), profile.getEmail(), profile.getPhone(), TCivility.M, address);

		LOGGER.info("Appel vers net retoure client : " + receiverContact.getFirstName() + ", address : "
				+ profile.getAddress1() + ", equipments" + equipments.toString());
	}

	/**
	 * methode appelle NetRetour pour verifier si un bien est retourne ou pas.
	 * 
	 * @param codeColis
	 *            le code colis du {@link BonPreparation}
	 * 
	 * @return true si le bien est retourne.
	 */
	public Boolean checkBienRecupere(String codeColis) {
		if (codeColis != null) {
			return true;
		}
		return true;
	}
}