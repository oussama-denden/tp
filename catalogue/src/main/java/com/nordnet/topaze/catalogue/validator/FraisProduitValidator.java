package com.nordnet.topaze.catalogue.validator;

import com.nordnet.topaze.catalogue.domain.FraisProduit;
import com.nordnet.topaze.catalogue.domain.Produit;
import com.nordnet.topaze.catalogue.util.PropertiesUtil;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Logique de validation d'un {@link FraisProduit}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class FraisProduitValidator {

	/**
	 * {@link PropertiesUtil}.
	 */
	private static PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();

	/**
	 * Valider les frais d'un produit.
	 * 
	 * @param produit
	 *            {@link Produit}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validateFraisPrix(Produit produit) throws TopazeException {
		if (produit.getFrais() != null && produit.getFrais().size() > 0) {
			for (FraisProduit fraisProduit : produit.getFrais()) {
				if (fraisProduit.getMontant() == null || fraisProduit.getMontant() < 0)
					throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "Produit.Frais.montant"), "0.1.1");
			}
		}
	}
}