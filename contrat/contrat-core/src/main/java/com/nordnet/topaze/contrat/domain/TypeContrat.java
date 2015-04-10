package com.nordnet.topaze.contrat.domain;

import com.nordnet.topaze.contrat.util.TypeContratDeserializer;

/**
 * Enumeration que definit le differents type d'un {@link ElementContractuel}.
 * <p>
 * Trois types de Contrats:
 * </p>
 * <ul>
 * <li><b>{@link #ABONNEMENT}</b></li>
 * <li><b>{@link #LOCATION}</b></li>
 * <li><b>{@link #VENTE}</b></li>
 * </ul>
 * 
 * @author Denden-OUSSAMA
 * 
 */
public enum TypeContrat {

	/**
	 * Contrat d'abonnement.
	 */
	ABONNEMENT,
	/**
	 * Contrat de location.
	 */
	LOCATION,
	/**
	 * Contrat de vente.
	 */
	VENTE;

	/**
	 * Cette methode sera utiliser par le {@link TypeContratDeserializer} pour faire la deserialisation.
	 * 
	 * @param typeContrat
	 *            type contrat.
	 * @return null si la valeur de string n'est pas parmi les valeurs de {@link TypeContrat}.
	 */
	public static TypeContrat fromString(String typeContrat) {
		switch (typeContrat) {

		case "ABONNEMENT":
			return ABONNEMENT;
		case "LOCATION":
			return LOCATION;
		case "VENTE":
			return VENTE;
		}
		return null;
	}
}
