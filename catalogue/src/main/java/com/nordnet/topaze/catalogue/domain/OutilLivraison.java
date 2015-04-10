package com.nordnet.topaze.catalogue.domain;

/**
 * Enumeration que definit le type d'outil de livraison d'un {@link Produit}.
 * <p>
 * deux types Prix:
 * </p>
 * <ul>
 * <li><b>NETDELIVERY</b> : Si produit de type Bien alors l'outil de Livraison est NETDELIVERY.</li>
 * <li><b>PACKAGER</b> : Si produit de type Service alors l'outil de Livraison est PACKAGER.</li>
 * </ul>
 * 
 * @author Ahmed-Mehdi-Laabidi
 * @author Denden-OUSSAMA
 */
public enum OutilLivraison {

	/**
	 * Si produit de type Bien alors l'outil de Livraison est NETDELIVERY.
	 */
	NETDELIVERY,
	/**
	 * Si produit de type Service alors l'outil de Livraison est PACKAGER.
	 */

	PACKAGER;

	/**
	 * Cette methode retourne {@link OutilLivraison} a partir d'un String.
	 * 
	 * @param outilLivraison
	 *            type de OutilLivraison.
	 * @return {@link OutilLivraison}.
	 */
	public static OutilLivraison fromString(String outilLivraison) {
		switch (outilLivraison) {
		case "NETDELIVERY":
			return NETDELIVERY;
		case "PACKAGER":
			return PACKAGER;

		}
		return null;

	}
}
