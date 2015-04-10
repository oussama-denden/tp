package com.nordnet.topaze.catalogue.domain;

/**
 * Enumeration que definit le type de prix de {@link Produit}.
 * <p>
 * trois types Prix:
 * </p>
 * <ul>
 * <li><b>One shot</b> : Concerne principalement les contrats de vente.</li>
 * <li><b>RECURRENT</b> : Concerne principalement les contrats de location ou d'abonement.</li>
 * <li><b>Trois fois sans frais</b> : c'est une sorte de mix des 2 precedents.</li>
 * </ul>
 * 
 * @author Denden-OUSSAMA
 * 
 */
public enum TypePrix {
	/**
	 * Indique que le {@link TypePrix} de {@link DetailProduit} est {@link #ONE_SHOT}.
	 */
	ONE_SHOT,
	/**
	 * Indique que le {@link TypePrix} de {@link DetailProduit} est {@link #RECURRENT}.
	 */
	RECURRENT,

	/**
	 * c'est une sorte de mix des 2 precedents.
	 */
	TROIS_FOIS_SANS_FRAIS;

	/**
	 * Cette methode retourne {@link TypeResiliation} a partir d'un String.
	 * 
	 * @param typePrix
	 *            le type de prix.
	 * @return {@link TypePrix}.
	 */
	public static TypePrix fromString(String typePrix) {
		switch (typePrix) {
		case "ONE_SHOT":
			return ONE_SHOT;
		case "RECURRENT":
			return RECURRENT;
		case "TROIS_FOIS_SANS_FRAIS":
			return TROIS_FOIS_SANS_FRAIS;
		}
		return null;
	}

}
