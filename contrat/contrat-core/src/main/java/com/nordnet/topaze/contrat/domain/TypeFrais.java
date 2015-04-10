package com.nordnet.topaze.contrat.domain;

/**
 * Enumeration qui definie le type de FRAIS RESILIATION ou CREATION.
 * 
 * @author Ahmed-Mehdi-Laabidi
 * @author Denden-OUSSAMA
 * 
 */
public enum TypeFrais {

	/**
	 * Frais de Type CREATION.
	 */
	CREATION,

	/**
	 * FRAIS DE TYPE RESILIATION.
	 */
	RESILIATION,

	/**
	 * frais de type Migration.
	 */
	MIGRATION;

	/**
	 * Cette methode retourne {@link TypeFrais} a partir d'un String.
	 * 
	 * @param typeFrais
	 *            le type de prix.
	 * @return {@link TypeFrais}.
	 */
	public static TypeFrais fromSting(String typeFrais) {
		switch (typeFrais) {
		case "CREATION":
			return CREATION;
		case "RESILIATION":
			return RESILIATION;
		case "MIGRATION":
			return MIGRATION;
		}
		return null;
	}

}
