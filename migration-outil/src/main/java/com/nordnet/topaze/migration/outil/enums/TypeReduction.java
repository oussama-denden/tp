package com.nordnet.topaze.migration.outil.enums;

/**
 * Enumeration que definit le type de {@link Reduction}.
 * 
 * <p>
 * Deux types de valeur:
 * </p>
 * <ul>
 * <li><b>CONTRAT</b> : la reduction peut etre associe qu'a un element contractuel.</li>
 * <li><b>FRAIS</b> : la reduction peut etre associe qu'a un frais.</li>
 * </ul>
 * 
 * @author akram-moncer
 * 
 */
public enum TypeReduction {

	/**
	 * la reduction peut etre associe qu'a un element contractuel.
	 */
	CONTRAT,

	/**
	 * la reduction peut etre associe qu'a un frais.
	 */
	FRAIS;

	/**
	 * Cette methode sera utiliser par le {@link TypeReductionDeserializer} pour faire la deserialisation.
	 * 
	 * @param typeReduction
	 *            on retourne null si la valeur de string ne figure pas dans l'enumeration.
	 * @return null si la valeur de string ne figure pas dans l'enumeration.
	 */
	public static TypeReduction fromString(String typeReduction) {
		switch (typeReduction) {
		case "CONTRAT":
			return CONTRAT;
		case "FRAIS":
			return FRAIS;
		}
		return null;
	}

}
