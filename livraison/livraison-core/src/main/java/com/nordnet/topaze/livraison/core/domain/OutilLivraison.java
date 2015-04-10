package com.nordnet.topaze.livraison.core.domain;

import com.nordnet.topaze.livraison.core.util.OutilLivraisonDeserializer;

/**
 * 
 * @author Ahmed-Mehdi-Laabidi
 * 
 */
public enum OutilLivraison {

	/**
	 * Indique que le type {@link #OutilLivraison} est {@link #NETDELIVERY}.
	 */
	NETDELIVERY,

	/**
	 * Indique que le type {@link #OutilLivraison} est {@link #PACKAGER}.
	 */
	PACKAGER,

	/**
	 * Indique que le type {@link #OutilLivraison} est {@link #NETRETOUR}.
	 */
	NETRETOUR,

	/**
	 * Indique que le type {@link #OutilLivraison} est {@link #SWAP}.
	 */
	SWAP;

	/**
	 * Cette methode sera utiliser par le {@link OutilLivraisonDeserializer} pour faire la deserialisation.
	 * 
	 * @param outilLivraison
	 *            type de OutilLivraison.
	 * @return {@link OutilLivraison}.
	 */
	public static OutilLivraison fromString(String outilLivraison) {
		switch (outilLivraison) {

		case "PACKAGER":
			return PACKAGER;

		case "NETDELIVERY":
			return NETDELIVERY;

		case "NETRETOUR":
			return NETRETOUR;

		case "SWAP":
			return SWAP;

		}
		return null;

	}
}