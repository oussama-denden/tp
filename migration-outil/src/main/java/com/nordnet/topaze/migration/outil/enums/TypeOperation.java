package com.nordnet.topaze.migration.outil.enums;

/**
 * Enumeration que definit le differents operations qui suit la migration.
 * <p>
 * Trois types de Contrats:
 * </p>
 * <ul>
 * <li><b>{@link #LIVRAISON}</b></li>
 * <li><b>{@link #RETOUR}</b></li>
 * <li><b>{@link #SUSPENSION}</b></li>
 * <li><b>{@link #ECHANGE}</b></li>
 * </ul>
 * 
 * @author Oussama Denden
 * 
 */
public enum TypeOperation {

	/**
	 * Livraison des biens ou des services.
	 */
	LIVRAISON,
	/**
	 * Retour des biens.
	 */
	RETOUR,
	/**
	 * suspension des services.
	 */
	SUSPENSION,

	ECHANGE;

	/**
	 * Cette methode retourne un {@link TypeOperation} a partir d'un String.
	 * 
	 * @param typeOperation
	 *            type de l'operation.
	 * @return null si la valeur de string n'est pas parmi les valeurs de {@link typeOperation}.
	 */
	public static TypeOperation fromString(String typeOperation) {
		switch (typeOperation) {

		case "LIVRAISON":
			return LIVRAISON;
		case "RETOUR":
			return RETOUR;

		case "SUSPENSION":
			return SUSPENSION;

		case "ECHANGE":
			return ECHANGE;

		}

		return null;
	}
}
