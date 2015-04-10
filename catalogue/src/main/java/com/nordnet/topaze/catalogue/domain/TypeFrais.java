package com.nordnet.topaze.catalogue.domain;

/**
 * Enumeration qui definie le type de FRAIS Resiliation ou CREATION.
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
	RESILIATION;

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
		}
		return null;
	}

}
