package com.nordnet.topaze.contrat.domain;

import com.nordnet.topaze.contrat.util.TypeOperationDeserializer;

/**
 * Enumeration que definit le differents type d'opération d'un {@link Tracage}.
 * <p>
 * Trois types de Contrats:
 * </p>
 * <ul>
 * <li><b>{@link #CREATION}</b></li>
 * <li><b>{@link #MODIFICATION}</b></li>
 * </ul>
 * 
 * @author anisselmane.
 * 
 */
public enum TypeOperation {

	/**
	 * Opération de création.
	 */
	CREATION,
	/**
	 * Opération de modification.
	 */
	MODIFICATION;

	/**
	 * Cette methode sera utiliser par le {@link TypeOperationDeserializer} pour faire la deserialisation.
	 * 
	 * @param typeOperation
	 *            type opération.
	 * @return null si la valeur de string n'est pas parmi les valeurs de {@link typeOperation}.
	 */
	public static TypeOperation fromString(String typeOperation) {
		switch (typeOperation) {

		case "CREATION":
			return CREATION;
		case "MODIFICATION":
			return MODIFICATION;
		}
		return null;
	}
}
