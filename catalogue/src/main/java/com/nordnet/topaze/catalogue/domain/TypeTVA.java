package com.nordnet.topaze.catalogue.domain;

/**
 * Enumeration que definit les differents types de TVA.
 * 
 * @author Ahmed
 * @author Denden-OUSSAMA
 * 
 */
public enum TypeTVA {

	/**
	 * Indique que le {@link TypeTva} est {@link #S}.
	 */
	S,

	/**
	 * Indique que le {@link TypeTva} est {@link #R}.
	 */
	R,

	/**
	 * Indique que le {@link TypeTva} est {@link #SR}.
	 */
	SR,

	/**
	 * Indique que le {@link TypeTva} est {@link #P}.
	 */
	P;

	/**
	 * Cette methode retourne {@link TypeTVA} a partir d'un String.
	 * 
	 * @param typeTVA
	 *            type de TVA.
	 * @return {@link TypeTva}.
	 */
	public static TypeTVA fromString(String typeTVA) {
		switch (typeTVA) {
		case "S":
			return S;
		case "R":
			return R;
		case "SR":
			return SR;
		case "P":
			return P;
		}
		return null;
	}

}
