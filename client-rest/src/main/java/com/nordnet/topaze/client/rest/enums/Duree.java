package com.nordnet.topaze.client.rest.enums;

/**
 * Enumeration que définit les type duree.
 * <p>
 * Duree :
 * </p>
 * <ul>
 * <li><b>RECONDUIT </b></li>
 * <li><b>RENOUVELLEMENT</b></li>
 * </ul>
 * 
 * @author anisselmane.
 * 
 */
public enum Duree {
	/**
	 * Indique que le {@link Duree} est de type {@link #RECONDUIT}.
	 */
	RECONDUIT,
	/**
	 * Indique que le {@link Duree} est de type {@link #RENOUVELLEMENT}.
	 */
	RENOUVELLEMENT;

	/**
	 * Cette methode sera utiliser par le {@link DureeDeserializer} pour faire la désérialisation.
	 * 
	 * @param duree
	 *            l duree.
	 * @return null si la valeur de string n'est pas RECONDUIT,RENOUVELLEMENT.
	 */
	public static Duree fromString(String duree) {
		switch (duree) {

		case "RECONDUIT":
			return RECONDUIT;

		case "RENOUVELLEMENT":
			return RENOUVELLEMENT;
		}

		return null;
	}

}
