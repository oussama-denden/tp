package com.nordnet.topaze.livraison.core.domain;

import com.nordnet.topaze.livraison.core.util.TypePrixDeserializer;

/**
 * Enumeration que definit le type de prix de {@link DetailProduit}.
 * <p>
 * Trois types Prix:
 * </p>
 * <ul>
 * <li><b>One shot</b> : Concerne principalement les contrats de vente.</li>
 * <li>RECURRENT : Concerne principalement les contrats de location ou d'abonement.</li>
 * <li>Trois fois sans frais : c'est une sorte de mix des 2 precedents.</li>
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
	 * c'est une sorte de mix des 2 pr�c�dents.
	 */
	TROIS_FOIS_SANS_FRAIS;

	/**
	 * Cette methode sera utiliser par le {@link TypePrixDeserializer} pour faire la deserialisation.
	 * 
	 * @param typePrix
	 *            on retourne null si la valeur de string n'est pas ONE_SHOT ou RECURRENT.
	 * @return null si la valeur de string n'est pas ONE_SHOT ou RECURRENT.
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

