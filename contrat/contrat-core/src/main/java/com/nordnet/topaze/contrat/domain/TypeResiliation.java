package com.nordnet.topaze.contrat.domain;

/**
 * Enumeration que definit le type de resiliation d'un contrat.
 * <p>
 * deux types :
 * </p>
 * <ul>
 * <li><b>RIN</b> : Resiliation à l'Initiative de NordNet.</li>
 * <li><b>RIC</b> : Resiliation à l'Initiative de client.</li>
 * </ul>
 * 
 * @author Denden-OUSSAMA
 * 
 */
public enum TypeResiliation {

	/**
	 * resiliation à l'Initiative de NordNet.
	 */
	RIN,

	/**
	 * resiliation à l'Initiative de client.
	 */
	RIC;

	/**
	 * Cette methode retourne {@link TypeResiliation} a partir d'un String.
	 * 
	 * @param typeResiliation
	 *            le type de resiliation.
	 * @return {@link TypeResiliation}.
	 */
	public static TypeResiliation fromString(final String typeResiliation) {
		switch (typeResiliation) {
		case "RIN":
			return RIN;
		case "RIC":
			return RIC;
		}
		return null;
	}
}
