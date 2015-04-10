package com.nordnet.topaze.finder.enums;

/**
 * Enumeration que d�finit le type de Tva de ?.
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
	 * Cette methode sera utiliser par le {@link TypeTVADeserializer} pour faire la deserialisation.
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
