package com.nordnet.topaze.contrat.domain;

import com.nordnet.topaze.contrat.util.EngagementDeserializer;

/**
 * Enumeration que définit les type engament.
 * <p>
 * Engagement :
 * </p>
 * <ul>
 * <li><b>RECONDUIT </b></li>
 * <li><b>RENOUVELLEMENT</b></li>
 * </ul>
 * 
 * @author anisselmane.
 * 
 */
public enum Engagement {
	/**
	 * Indique que le {@link Engagement} est de type {@link #RECONDUIT}.
	 */
	RECONDUIT,
	/**
	 * Indique que le {@link Engagement} est de type {@link #RENOUVELLEMENT}.
	 */
	RENOUVELLEMENT;

	/**
	 * Cette methode sera utiliser par le {@link EngagementDeserializer} pour faire la désérialisation.
	 * 
	 * @param engagement
	 *            l engagement.
	 * @return null si la valeur de string n'est pas RECONDUIT,RENOUVELLEMENT.
	 */
	public static Engagement fromString(String engagement) {
		switch (engagement) {

		case "RECONDUIT":
			return RECONDUIT;

		case "RENOUVELLEMENT":
			return RENOUVELLEMENT;
		}

		return null;
	}

}
