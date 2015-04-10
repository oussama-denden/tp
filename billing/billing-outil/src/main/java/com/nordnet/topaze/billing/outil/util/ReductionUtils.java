package com.nordnet.topaze.billing.outil.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.nordnet.common.valueObject.constants.DiscountType;
import com.nordnet.common.valueObject.money.Discount;
import com.nordnet.common.valueObject.number.Amount;
import com.nordnet.topaze.billing.outil.business.DiscountInfo;
import com.nordnet.topaze.client.rest.business.facturation.ReductionInfo;

/**
 * Gere les reductions.
 * 
 * @author Oussama Denden
 * 
 */
public class ReductionUtils {

	/**
	 * Calcule de reduction.
	 * 
	 * @param periodicite
	 *            periodicite.
	 * @param montant
	 *            montant de reduction.
	 * @param reductionInfos
	 *            liste des {@link ReductionInfo}.
	 * @return liste des {@link DiscountInfo} a envoye vers saphir.
	 */
	public static List<DiscountInfo> calculerReduction(Integer periodicite, Double montant,
			List<ReductionInfo> reductionInfos) {

		Collections.sort(reductionInfos);
		List<DiscountInfo> discountInfos = new ArrayList<>();
		for (ReductionInfo reductionInfo : reductionInfos) {
			Discount discount = getDiscount(reductionInfo, periodicite);
			discountInfos.add(new DiscountInfo(reductionInfo.getTitre(), discount, reductionInfo
					.getIsAffichableSurFacture()));
		}

		/*
		 * calculer les reduction selon leur ordre.
		 */
		Double montantTotal = montant;
		if (discountInfos.size() > Constants.UN && isOrdreNecessaire(discountInfos)) {
			for (DiscountInfo discountInfo : discountInfos) {
				double montantDiscount = calculerMontantDiscount(montantTotal, discountInfo.getDiscount());
				discountInfo.setDiscount(montantDiscount, DiscountType.VALUE_HT);
				montantTotal -= montantDiscount;
			}
		}

		return discountInfos;

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
	public static final Discount getDiscount(ReductionInfo reduction, Integer periodicite) {
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

	/**
	 * Il y a deux cas ou l'ordre des reduction n'a pas d'effet :
	 * <ul>
	 * <li>Si les reduction sont tous de type MONTANT.</li>
	 * <li>Si les reduction sont tous de type POUCENTAGE et affichableSurFacture = false.</li>
	 * </ul>
	 * 
	 * @param discountInfos
	 *            {@link DiscountInfo}.
	 * @return true si l'ordre est necessaire.
	 */
	private static boolean isOrdreNecessaire(List<DiscountInfo> discountInfos) {
		if (discountInfos.size() <= Constants.UN) {
			return false;
		}
		boolean contienReductionPourcentage = false;
		boolean contientAffichableSurFacture = false;
		boolean contientReductionHT = false;
		for (DiscountInfo discountInfo : discountInfos) {
			if (discountInfo.getDiscount().getDiscountType() == DiscountType.PERCENT) {
				contienReductionPourcentage = true;
			} else {
				contientReductionHT = true;
			}
			if (discountInfo.isAffichableSurFacture()) {
				contientAffichableSurFacture = true;
			}
		}

		if (contienReductionPourcentage && contientReductionHT) {
			return true;
		} else if (contienReductionPourcentage && contientAffichableSurFacture) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Calcule la valeur de la discount.
	 * 
	 * @param montantTotal
	 *            le montant de l'element contractuel.
	 * @param discount
	 *            le discount associe.
	 * @return la valeur du discount.
	 */
	public static final double calculerMontantDiscount(double montantTotal, Discount discount) {
		double montantDiscount = 0;
		switch (discount.getDiscountType()) {
		case PERCENT:
			montantDiscount = montantTotal * discount.getAmount().getAmount().doubleValue() / 100;
			break;
		case VALUE_HT:
			montantDiscount = discount.getAmount().getAmount().doubleValue();
			if (montantTotal == Constants.ZERO) {
				montantDiscount = Constants.ZERO;
			} else if (montantTotal < montantDiscount) {
				montantDiscount = montantTotal;
			}
			break;
		default:
			break;
		}
		return montantDiscount;
	}

}
