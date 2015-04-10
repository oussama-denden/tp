package com.nordnet.topaze.contrat.domain;

/**
 * Enumeration qui definit le mode de resiliation du contrat.
 * 
 * @author akram-moncer
 * 
 */
public enum StatutContrat {

	/**
	 * Contrat preparer.
	 */
	PREPARER,

	/**
	 * Contrat valider.
	 */
	VALIDER,

	/**
	 * Contrat resilier.
	 */
	RESILIER;

	/**
	 * Cette methode retourne {@link statutContrat} a partir d'un String.
	 * 
	 * @param statutContrat
	 *            la status du contrat.
	 * @return {@link StatutContrat}.
	 */
	public static StatutContrat fromString(final String statutContrat) {
		switch (statutContrat) {

		case "PREPARER":
			return PREPARER;

		case "VALIDER":
			return VALIDER;

		case "RESILIER":
			return RESILIER;

		}
		return null;
	}

}