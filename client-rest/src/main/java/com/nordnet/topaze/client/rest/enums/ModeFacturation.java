package com.nordnet.topaze.client.rest.enums;

/**
 * 
 * Enumeration que definit le differents modes de facturation.
 * <p>
 * Deux types des modes de facturations:
 * </p>
 * <ul>
 * <li><b>{@link #PREMIER_MOIS}</b></li>
 * <li><b>{@link #DATE_ANNIVERSAIRE}</b></li>
 * </ul>
 * 
 * @author Ahmed-Mehdi-Laabidi
 * 
 */
public enum ModeFacturation {

	/**
	 * premier de chaque mois.
	 */
	PREMIER_MOIS,

	/**
	 * date d'anniversaire du facturation.
	 */
	DATE_ANNIVERSAIRE;

	/**
	 * Cette methode sera utiliser par le {@link ModeFacturationDeserializer} pour faire la deserialisation.
	 * 
	 * @param modeFacturation
	 *            le mode de facturation
	 * @return {@link ModeFacturation}.
	 */
	public static ModeFacturation fromString(final String modeFacturation) {
		switch (modeFacturation) {
		case "PREMIER_MOIS":
			return PREMIER_MOIS;
		case "DATE_ANNIVERSAIRE":
			return DATE_ANNIVERSAIRE;
		}
		return null;
	}

}
