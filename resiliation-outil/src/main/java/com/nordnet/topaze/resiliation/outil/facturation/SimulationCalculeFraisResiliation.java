package com.nordnet.topaze.resiliation.outil.facturation;

import java.util.Collections;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import com.nordnet.common.valueObject.date.TimePeriod;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.resiliation.outil.business.DiscountInfo;
import com.nordnet.topaze.resiliation.outil.business.Frais;
import com.nordnet.topaze.resiliation.outil.business.ResiliationBillingInfo;
import com.nordnet.topaze.resiliation.outil.enums.TypeFrais;
import com.nordnet.topaze.resiliation.outil.util.Constants;
import com.nordnet.topaze.resiliation.outil.util.PropertiesUtil;

/**
 * Simuler le calcule de frais de resiliation d'un contrat.
 * 
 * @author Oussama Denden
 * 
 */
@Component("fraisResiliationCalculator")
public class SimulationCalculeFraisResiliation {

	/**
	 * Calcule de Frais avec montant definit.
	 * 
	 * @param montantFraisResiliation
	 *            montant definit.
	 * @return {@link Frais}.
	 */
	public Frais fraisResiliationMontantDefinit(Double montantFraisResiliation) {
		Frais frais = new Frais();
		frais.setTitre("Frais de resiliation à montant definit");
		frais.setMontant(montantFraisResiliation);
		frais.setTypeFrais(TypeFrais.RESILIATION);
		return frais;
	}

	/**
	 * Calcule des frais de resiliation.
	 * 
	 * @param resiliationBillingInfo
	 *            {@link ResiliationBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void fraisResiliation(ResiliationBillingInfo resiliationBillingInfo) throws TopazeException {
		if (resiliationBillingInfo.getFrais() != null && resiliationBillingInfo.getFrais().size() > 0) {
			Frais frais = getFraisResiliationActif(resiliationBillingInfo);
			if (frais != null) {
				double montantpayment = frais.getMontant();

				// chercher la meilleur reduction.
				DiscountInfo discountInfo =
						ReductionUtils.getMeilleurReduction(frais.getReductionInfoContrat(),
								frais.getReductionInfoEC(), resiliationBillingInfo.getPeriodicite(), montantpayment);
				if (discountInfo != null) {
					frais.setDiscountInfo(discountInfo);
				}
				resiliationBillingInfo.getResiliationInfo().setFrais(frais);
			}
		}

	}

	/**
	 * retourne le frais a applique lors de la resiliation.
	 * 
	 * @param resiliationBillingInfo
	 *            {@link ResiliationBillingInfo}
	 * @return {@link Frais}
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	private static Frais getFraisResiliationActif(ResiliationBillingInfo resiliationBillingInfo) throws TopazeException {
		Collections.sort(resiliationBillingInfo.getFraisResiliations());
		LocalDate dateDebut = LocalDate.fromDateFields(resiliationBillingInfo.getDateDebutFacturation());
		LocalDate dateJour = PropertiesUtil.getInstance().getDateDuJour();
		for (Frais frais : resiliationBillingInfo.getFraisResiliations()) {
			if (frais.getNombreMois() != null) {
				LocalDate dateFin = dateDebut.plusMonths(frais.getNombreMois()).minusDays(Constants.UN);
				TimePeriod period = new TimePeriod(dateDebut, dateFin);
				if (period.contains(dateJour)) {
					return frais;
				}
				dateDebut = dateFin.plusDays(Constants.UN);
			} else {
				return frais;
			}
		}
		return null;
	}

}
