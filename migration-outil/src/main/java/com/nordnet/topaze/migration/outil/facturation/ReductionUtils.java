package com.nordnet.topaze.migration.outil.facturation;

import com.nordnet.common.valueObject.constants.DiscountType;
import com.nordnet.common.valueObject.money.Discount;
import com.nordnet.common.valueObject.number.Amount;
import com.nordnet.topaze.migration.outil.business.DiscountInfo;
import com.nordnet.topaze.migration.outil.business.ReductionInfo;
import com.nordnet.topaze.migration.outil.util.Constants;

/**
 * Gere les reductions.
 * 
 * @author Oussama Denden
 * 
 */
public class ReductionUtils {

	/**
	 * Choisir la meilleur reduction.
	 * 
	 * @param reductionContrat
	 *            reduction sur contrat global.
	 * @param reductionEC
	 *            reduction sur element contractuel.
	 * @param periodicite
	 *            periodictite.
	 * @param montant
	 *            montant.
	 * @return la meilleur reduction.
	 */
	@SuppressWarnings("null")
	public static DiscountInfo getMeilleurReduction(ReductionInfo reductionContrat, ReductionInfo reductionEC,
			Integer periodicite, Double montant) {

		Discount discountContrat = getDiscount(reductionContrat, periodicite);
		Discount discountEC = getDiscount(reductionEC, periodicite);

		if (discountContrat == null && discountEC == null) {
			return null;
		}

		else if (discountContrat != null && discountEC == null) {
			return new DiscountInfo(reductionContrat.getTitre(), discountContrat,
					reductionContrat.getIsAffichableSurFacture());
		}

		else if (discountContrat == null && discountEC != null) {
			return new DiscountInfo(reductionEC.getTitre(), discountEC, reductionEC.getIsAffichableSurFacture());
		}

		else {
			Double montantContrat = montant;
			Double montantEC = montant;

			switch (discountContrat.getDiscountType()) {
			case PERCENT:
				montantContrat = montantContrat * discountContrat.getAmount().getAmount().doubleValue() / 100;
				break;
			default:
				montantContrat = discountContrat.getAmount().getAmount().doubleValue();
				break;

			}

			switch (discountEC.getDiscountType()) {
			case PERCENT:
				montantEC = montantEC * discountEC.getAmount().getAmount().doubleValue() / 100;
				break;
			default:
				montantEC = discountEC.getAmount().getAmount().doubleValue();
				break;

			}

			if (montantContrat.compareTo(montantEC) >= 0) {
				return new DiscountInfo(reductionContrat.getTitre(), discountContrat,
						reductionContrat.getIsAffichableSurFacture());
			}
			return new DiscountInfo(reductionEC.getTitre(), discountEC, reductionEC.getIsAffichableSurFacture());
		}

	}

	/**
	 * Get Reduction.
	 * 
	 * @param reduction
	 *            {@link ReductionInfo}.
	 * @param periodicite
	 *            la periodictie.
	 * @return la reduction.
	 */
	private static Discount getDiscount(ReductionInfo reduction, Integer periodicite) {
		Discount discount = null;
		if (reduction != null) {

			DiscountType discountType = null;
			Double valeur = null;
			switch (reduction.getTypeValeur()) {

			case MONTANT:
				valeur = reduction.getValeur();
				discountType = DiscountType.VALUE_HT;
				break;

			case MOIS:
				valeur = reduction.getValeur() / periodicite * Constants.CENT;
				discountType = DiscountType.PERCENT;
				break;

			case POURCENTAGE:
				valeur = reduction.getValeur();
				discountType = DiscountType.PERCENT;
				break;

			default:
				return null;
			}

			Amount amount = new Amount(valeur);
			discount = new Discount(amount, discountType);
		}
		return discount;
	}

}
