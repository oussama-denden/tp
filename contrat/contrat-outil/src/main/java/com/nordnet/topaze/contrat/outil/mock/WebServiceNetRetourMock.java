package com.nordnet.topaze.contrat.outil.mock;

import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;

/**
 * Cette classe est cree afin de prévoir les méthodes appelant NetRetour.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class WebServiceNetRetourMock {

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
			return false;
		}
		return false;
	}
}
