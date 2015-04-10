package com.nordnet.topaze.client.rest.enums;


/**
 * Enumeration que d�finit le diff�rents type d'un {@link Contrat}.
 * <p>
 * Trois types de Contrats:
 * </p>
 * <ul>
 * <li><b>{@link #ABONNEMENT}</b></li>
 * <li><b>{@link #LOCATION}</b></li>
 * <li><b>{@link #VENTE}</b></li>
 * </ul>
 * 
 * @author Ahmed-Mehdi-Laabidi
 * 
 */
public enum TypeContrat {
	/**
	 * Contrat que peut contenir des sous contrats.
	 */
	GLOBAL,
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
	 * Cette methode sera utiliser par le {@link TypeContratDeserializer} pour
	 * faire la deserialisation.
	 * 
	 * @param typeContrat
	 *            type contrat.
	 * @return null si la valeur de string n'est pas parmi les valeurs de
	 *         {@link TypeContrat}.
	 */
	public static TypeContrat fromString(String typeContrat) {
		switch (typeContrat) {
		case "GLOBAL":
			return GLOBAL;
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
