package com.nordnet.topaze.livraison.core.validator;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.livraison.core.domain.ElementLivraison;
import com.nordnet.topaze.livraison.core.domain.StatusBonPreparation;
import com.nordnet.topaze.livraison.core.util.PropertiesUtil;
import com.nordnet.topaze.livraison.core.util.Utils;

/**
 * Valider un {@link ElementLivraison}.
 * 
 * @author akram-moncer
 * 
 */
public class ElementLivraisonValidator {

	/**
	 * {@link PropertiesUtil}.
	 */
	private static PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();

	/**
	 * 
	 * @param reference
	 *            reference element livraison.
	 * @param elementLivraison
	 *            {@link ElementLivraison}
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void checkExist(String reference, ElementLivraison elementLivraison) throws TopazeException {
		if (elementLivraison == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.2", reference), "0.1.2");
		}
	}

	/**
	 * Verifier reference de l'element de livraison.
	 * 
	 * @param reference
	 *            the reference
	 * @throws TopazeException
	 *             the livraison exception
	 */
	public static void verifierReferenceEL(String reference) throws TopazeException {
		if (Utils.isStringNullOrEmpty(reference)) {
			throw new TopazeException(
					propertiesUtil.getErrorMessage("0.1.4", "la référence de l'element de livraison"), "0.1.4");
		}

	}

	/**
	 * Test si un EL exist et preparer.
	 * 
	 * @param referenceEL
	 *            reference du EL.
	 * @param elementLivraison
	 *            {@link ElementLivraison}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void checkPreparer(String referenceEL, ElementLivraison elementLivraison) throws TopazeException {
		checkExist(referenceEL, elementLivraison);
		if (!elementLivraison.isPreparer()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("3.1.6", elementLivraison.getReference()), "3.1.6");
		}

	}

	/**
	 * Tester si le processus de recuperation des biens et des services a commencer ou pas.
	 * 
	 * @param elementLivraisonRetour
	 *            {@link ElementLivraison}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void checkNonRecuperer(ElementLivraison elementLivraisonRetour) throws TopazeException {
		if (elementLivraisonRetour != null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("3.1.7", elementLivraisonRetour.getReference()
					.split("-")[1]), "3.1.7");
		}
	}

	/**
	 * Tester si un bon de preparation global est livree ou pas.
	 * 
	 * @param reference
	 *            reference du contrat associe au bon de preparation.
	 * @param elementLivraison
	 *            {@link ElementLivraison}
	 * @throws TopazeException
	 *             {@link TopazeException} si l'element de livraison n'est pas livrer.
	 */
	public static void checkLivrer(String reference, ElementLivraison elementLivraison) throws TopazeException {
		checkExist(reference, elementLivraison);

		if (!elementLivraison.getStatut().equals(StatusBonPreparation.TERMINER)) {
			throw new TopazeException(propertiesUtil.getErrorMessage("3.1.4", elementLivraison.getReference()), "3.1.4");
		}

	}

}
