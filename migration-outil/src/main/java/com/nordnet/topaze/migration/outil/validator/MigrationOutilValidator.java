package com.nordnet.topaze.migration.outil.validator;

import java.util.Arrays;
import java.util.Date;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.migration.outil.business.PolitiqueMigration;
import com.nordnet.topaze.migration.outil.business.ProduitMigration;
import com.nordnet.topaze.migration.outil.enums.TypeProduit;
import com.nordnet.topaze.migration.outil.util.PropertiesUtil;
import com.nordnet.topaze.migration.outil.util.Utils;

/**
 * Valider la simulation de migration.
 * 
 * @author Oussama Denden
 * 
 */
public class MigrationOutilValidator {

	/** The properties util. {@link PropertiesUtil}. */
	private static PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();

	/**
	 * Verifier que la politique de migration est obligatoire.
	 * 
	 * @param politiqueMigration
	 *            the politique Migration
	 * @throws TopazeException
	 *             the contrat exception
	 */
	public static void checkPolitiqueMigration(PolitiqueMigration politiqueMigration) throws TopazeException {
		if (politiqueMigration == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.17", "migration"), "1.1.17");
		} else if (politiqueMigration.isReductionAncienne() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PolitiqueMigration.reductionAncienne"),
					"0.1.1");
		} else if (politiqueMigration.isPenalite() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PolitiqueMigration.penalite"), "0.1.1");
		} else if (politiqueMigration.isFraisCreation() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PolitiqueMigration.fraisCreation"),
					"0.1.1");
		} else if (politiqueMigration.isFraisResiliation() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PolitiqueMigration.fraisResiliation"),
					"0.1.1");
		} else if (politiqueMigration.isRemboursement() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PolitiqueMigration.remboursement"),
					"0.1.1");
		}
		if (politiqueMigration.getDateAction() != null
				&& Utils.compareDate(politiqueMigration.getDateAction(), new Date()) == -1) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.110", "PolitiqueResiliation.dateAction"),
					"1.1.110");
		}
		if (politiqueMigration.isRemboursement() == false && politiqueMigration.getMontantRemboursement() != null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.125"), "1.1.125");
		}

	}

	/**
	 * Valider ligne de produit migration.
	 * 
	 * @param produitMigration
	 *            {@link ProduitMigration}
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public static void checkProduitMigration(ProduitMigration produitMigration) throws TopazeException {
		if (produitMigration.getNumEC() == null
				&& (!Utils.isStringNullOrEmpty(produitMigration.getReferenceProduitSource()) || Utils
						.isStringNullOrEmpty(produitMigration.getReferenceGammeSource()))) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.74"), "1.1.74");
		} else if (produitMigration.getNumEC() != null
				&& (Utils.isStringNullOrEmpty(produitMigration.getReferenceProduitSource()) || Utils
						.isStringNullOrEmpty(produitMigration.getReferenceGammeSource()))) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.75"), "1.1.75");
		}

		if (produitMigration.getNumEC() != null
				&& (Utils.isStringNullOrEmpty(produitMigration.getReferenceProduitDestination()) || Utils
						.isStringNullOrEmpty(produitMigration.getReferenceGammeDestination()))) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.87", produitMigration.getNumEC()), "1.1.87");
		}

		if (produitMigration.getTypeProduit() == null) {
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.1.11",
					"ProduitMigration.typeProduit", Arrays.asList(TypeProduit.values())), "0.1.11");
		}

	}
}
