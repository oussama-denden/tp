package com.nordnet.topaze.livraison.core.domain;

/**
 * Enumeration que definit l'etat d'un {@link BonPreparation}.
 * <p>
 * deux etats:
 * </p>
 * <ul>
 * <li><b>LIVRAISON: </b>bon de preparation pour la livraison.</li>
 * <li><b>RETOUR: </b>bon de preparation pour le retour.</li>
 * <li><b>MIGRATION: </b>bon de preparation pour la migration.</li>
 * <li><b>CESSION: </b>bon de preparation pour la cession.</li>
 * </ul>
 * 
 * @author Denden-OUSSAMA
 * 
 */
public enum TypeBonPreparation {

	/**
	 * bon de preparation pour la livraison.
	 */
	LIVRAISON,

	/**
	 * bon de preparation pour le recuperation des biens et des services.
	 */
	RETOUR,

	/**
	 * bon de preparation pour la migration des biens et des services.
	 */
	MIGRATION,

	/**
	 * bon de preparation pour la cession des biens/services.
	 */
	CESSION,

	/**
	 * bon de preparation pour le renouvellement des biens/services.
	 */
	RENOUVELLEMENT;

	/**
	 * Cette methode retourne un {@link TypeBonPreparation} a partir d'un String.
	 * 
	 * @param etatBonPreparation
	 *            doit etre conforme aux valeurs de l'enum.
	 * 
	 * @return null si la valeur de string n'est pas conforme aux valeurs de l'enum.
	 */
	public static TypeBonPreparation fromString(String etatBonPreparation) {
		switch (etatBonPreparation) {
		case "LIVRAISON":
			return LIVRAISON;
		case "RETOUR":
			return RETOUR;

		case "MIGRATION":
			return MIGRATION;
		case "CESSION":
			return CESSION;
		case "RENOUVELLEMENT":
			return RENOUVELLEMENT;
		}
		return null;
	}

}
