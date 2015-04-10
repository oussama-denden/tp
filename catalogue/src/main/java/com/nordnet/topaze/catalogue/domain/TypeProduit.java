package com.nordnet.topaze.catalogue.domain;

/**
 * Enumeration que definit les differents type d'un {@link Produit}.
 * <p>
 * deux types :
 * </p>
 * <ul>
 * <li><b>Bien</b> : Concerne principalement les contrats de vente ou de location.</li>
 * <li><b>Service</b> : Concerne principalement les contrats d'abonnement).</li>
 * </ul>
 * 
 * @author Denden-OUSSAMA
 * 
 */
public enum TypeProduit {
	/**
	 * Indique que le {@link Produit} est {@link #BIEN}.
	 */
	BIEN,
	/**
	 * Indique que le {@link Produit} est {@link #SERVICE}.
	 */
	SERVICE;

	/**
	 * Cette methode retourne {@link TypeProduit} a partir d'un String.
	 * 
	 * @param typeProduit
	 *            le type de prix.
	 * @return {@link TypeProduit}.
	 */
	public static TypeProduit fromString(final String typeProduit) {

		switch (typeProduit) {

		case "BIEN":
			return BIEN;

		case "SERVICE":
			return SERVICE;
		}
		return null;
	}

}
