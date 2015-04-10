package com.nordnet.topaze.finder.business.welcome;

import com.nordnet.topaze.exception.TopazeException;

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
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static final Double getMeilleurReduction(Reduction reductionContrat, Reduction reductionEC,
			Integer periodicite, Double montant) throws TopazeException {

		Double montantReduction = null;

		if (reductionContrat != null && reductionEC == null && reductionContrat.isEligible()) {
			montantReduction = calculerMontantReduction(montant, reductionContrat, periodicite);
		}

		else if (reductionContrat == null && reductionEC != null && reductionEC.isEligible()) {
			montantReduction = calculerMontantReduction(montant, reductionEC, periodicite);
		}

		else if (reductionContrat != null && reductionEC != null) {
			if (reductionContrat.isEligible() && !reductionEC.isEligible()) {
				montantReduction = calculerMontantReduction(montant, reductionContrat, periodicite);
			} else if (!reductionContrat.isEligible() && reductionEC.isEligible()) {
				montantReduction = calculerMontantReduction(montant, reductionEC, periodicite);
			} else {
				Double montantContrat = calculerMontantReduction(montant, reductionContrat, periodicite);
				Double montantEC = calculerMontantReduction(montant, reductionEC, periodicite);
				montantReduction = montantContrat.compareTo(montantEC) > 0 ? montantContrat : montantEC;
			}
		}
		return montantReduction;
	}

	/**
	 * Calcule la valeur de la reduction.
	 * 
	 * @param montantTotal
	 *            le montant de l'element contractuel.
	 * @param reduction
	 *            {@link Reduction}.
	 * @param periodicite
	 *            periodicite.
	 * @return la valeur du discount.
	 */
	public static final Double calculerMontantReduction(double montantTotal, Reduction reduction, int periodicite) {
		Double montantDiscount = null;
		switch (reduction.getTypeValeur()) {
		case POURCENTAGE:
			montantDiscount = montantTotal * reduction.getValeur() / 100;
			break;
		case MOIS:
			double valeurReduction = reduction.getValeur() / periodicite * 100;
			montantDiscount = montantTotal * valeurReduction / 100;
			break;
		case MONTANT:
			montantDiscount = reduction.getValeur();
			break;
		default:
			break;
		}
		return montantDiscount;
	}

}
