package com.nordnet.topaze.resiliation.outil.enums;

/**
 * 
 * @author Ahmed-Mehdi-Laabidi Enumeration qui definie le type de FRAIS Resiliation ou CREATION.
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
	 * Cette methode sera utiliser par le {@link TypeFraisDeserializer} pour faire la deserialisation.
	 * 
	 * @param typeFrais
	 *            on retourne null si la valeur de string n'est pas CREATION ou RESILIATION.
	 * @return null si la valeur de string n'est pas CREATION ou RESILIATION.
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
