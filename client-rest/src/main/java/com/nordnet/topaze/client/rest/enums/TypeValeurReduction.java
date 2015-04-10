package com.nordnet.topaze.client.rest.enums;

/**
 * Enumeration que definit le type de valeur de {@link Reduction}.
 * 
 * <p>
 * Trois types de valeur:
 * </p>
 * <ul>
 * <li><b>MONTANT</b> : la promotion est une reduction en Montant.</li>
 * <li><b>POURCENTAGE</b> : la promotion est une reduction en pourcentage.</li>
 * <li><b>MOIS</b> : la promotion est en mois gratuit offert.</li>
 * </ul>
 * 
 * @author akram-moncer
 * 
 */
public enum TypeValeurReduction {

	/**
	 * la promotion est une reduction en Montant.
	 */
	MONTANT,

	/**
	 * la promotion est une reduction en pourcentage.
	 */
	POURCENTAGE,

	/**
	 * la promotion est en mois gratuit offert.
	 */
	MOIS;

	/**
	 * Cette methode sera utiliser par le {@link TypeValeurReductionDeserializer} pour faire la deserialisation.
	 * 
	 * @param typeValeurReduction
	 *            on retourne null si la valeur de string ne figure pas dans l'enumeration.
	 * @return null si la valeur de string ne figure pas dans l'enumeration.
	 */
	public static TypeValeurReduction fromString(String typeValeurReduction) {
		switch (typeValeurReduction) {
		case "MONTANT":
			return MONTANT;
		case "POURCENTAGE":
			return POURCENTAGE;
		case "MOIS":
			return MOIS;
		}
		return null;
	}

}
