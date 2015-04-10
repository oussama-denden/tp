package com.nordnet.topaze.catalogue.validator;

import com.nordnet.topaze.catalogue.domain.Prix;
import com.nordnet.topaze.catalogue.util.PropertiesUtil;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Logique de validation d'un {@link Prix}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class PrixValidator {

	/**
	 * {@link PropertiesUtil}.
	 */
	private static PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();

	/**
	 * valider {@link Prix}.
	 * 
	 * @param prix
	 *            {@link Prix}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validatePrix(Prix prix) throws TopazeException {

		if (prix == null)
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "Prix"), "0.1.1");

		if (prix.getMontant() == null || prix.getMontant() <= 0)
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "Prix.montant"), "0.1.1");

		if (prix.getTypePrix() == null)
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "Prix.typePrix"), "0.1.1");
	}
}
