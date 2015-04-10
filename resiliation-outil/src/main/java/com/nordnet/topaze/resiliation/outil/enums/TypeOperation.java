package com.nordnet.topaze.resiliation.outil.enums;

/**
 * Enumeration que definit le differents operations qui suit la resiliation.
 * <p>
 * Trois types de Contrats:
 * </p>
 * <ul>
 * <li><b>{@link #RETOUR}</b></li>
 * <li><b>{@link #SUSPENSION}</b></li>
 * </ul>
 * 
 * @author Oussama Denden
 * 
 */
public enum TypeOperation {

	/**
	 * Retour des biens.
	 */
	RETOUR,
	/**
	 * suspension des services.
	 */
	SUSPENSION;

	/**
	 * Cette methode retourne un {@link TypeOperation} a partir d'un String.
	 * 
	 * @param typeOperation
	 *            type de l'operation.
	 * @return null si la valeur de string n'est pas parmi les valeurs de {@link typeOperation}.
	 */
	public static TypeOperation fromString(String typeOperation) {
		switch (typeOperation) {

		case "RETOUR":
			return RETOUR;

		case "SUSPENSION":
			return SUSPENSION;

		}

		return null;
	}

}
