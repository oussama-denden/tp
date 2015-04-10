package com.nordnet.topaze.client.rest.enums;

/**
 * Enumeration que definit l'etat d'un {@link BonPreparation}.
 * <p>
 * deux etats:
 * </p>
 * <ul>
 * <li><b>LIVRAISOn: </b>bon de preparation pour la livraison.</li>
 * <li><b>RETOUR: </b>bon de preparation pour le retour.</li>
 * </ul>
 * 
 * @author Denden-OUSSAMA
 * 
 */
public enum EtatBonPreparation {

	/**
	 * bon de preparation pour la livraison.
	 */
	LIVRAISON,

	/**
	 * bon de preparation pour le recuperation des biens et des services.
	 */
	RECUPERATION;

	/**
	 * Cette methode retourne un {@link EtatBonPreparation} a partir d'un String.
	 * 
	 * @param etatBonPreparation
	 *            doit etre conforme aux valeurs de l'enum.
	 * 
	 * @return null si la valeur de string n'est pas conforme aux valeurs de l'enum.
	 */
	public static EtatBonPreparation fromString(String etatBonPreparation) {
		switch (etatBonPreparation) {
		case "LIVRAISON":
			return LIVRAISON;
		case "RECUPERATION":
			return RECUPERATION;
		}
		return null;
	}

}
