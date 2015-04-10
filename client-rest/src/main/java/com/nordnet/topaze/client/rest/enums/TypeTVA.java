package com.nordnet.topaze.client.rest.enums;

/**
 * Enumeration que d�finit le type de Tva.
 * <p>
 * quatre de type TVA:
 * </p>
 * <ul>
 * <li><b>S</b> : ?.</li>
 * <li><b>R</b> : ?.</li>
 * <li><b>SR</b> : ?</li>
 * <li><b>P</b> : ?</li>
 * </ul>
 * 
 * @author Ahmed-Mehdi-Laabidi
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
	 * 
	 * @param typeTVA type TVA.
	 * @return {@link #TypeTVA()} 
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
