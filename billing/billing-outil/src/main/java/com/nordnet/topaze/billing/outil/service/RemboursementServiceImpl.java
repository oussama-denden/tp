package com.nordnet.topaze.billing.outil.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nordnet.common.valueObject.constants.DiscountType;
import com.nordnet.common.valueObject.date.TimePeriod;
import com.nordnet.common.valueObject.money.Discount;
import com.nordnet.common.valueObject.number.Amount;
import com.nordnet.topaze.billing.outil.business.DiscountInfo;
import com.nordnet.topaze.billing.outil.business.RemboursementBillingInfo;
import com.nordnet.topaze.billing.outil.util.Constants;
import com.nordnet.topaze.billing.outil.util.ReductionUtils;
import com.nordnet.topaze.client.rest.RestClientBillingOutil;
import com.nordnet.topaze.client.rest.business.facturation.ReductionInfo;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Implementation du services {@link RemboursementService}.
 * 
 * @author Oussama Denden
 * 
 */
@Service("remboursementService")
public class RemboursementServiceImpl implements RemboursementService {

	/**
	 * {@link RestClientBillingOutil}.
	 */
	@Autowired
	private RestClientBillingOutil restClientBillingOutil;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RemboursementBillingInfo getRemboursementBillingInfoMontantDefinit(Integer periodicite, Double montant,
			Date derniereFactureDate, Date finContratDate) {
		LocalDate dateDerniereFacture = LocalDate.fromDateFields(derniereFactureDate);
		LocalDate dateFinPeriode = dateDerniereFacture.plusMonths(periodicite);
		LocalDate dateFinContrat = LocalDate.fromDateFields(finContratDate);
		TimePeriod periodFacturation = new TimePeriod(dateFinContrat, dateFinPeriode);

		RemboursementBillingInfo remboursementBillingInfo = new RemboursementBillingInfo();
		remboursementBillingInfo.setMontantRemboursement(montant);
		remboursementBillingInfo.setDiscount(null);
		remboursementBillingInfo.setTimePeriod(periodFacturation);
		return remboursementBillingInfo;

	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public RemboursementBillingInfo getRemboursementBillingInfo(String referenceContrat, Integer numEC,
			Integer version, Integer periodicite, Double montant, Date derniereFactureDate, Date debutFacturationDate,
			Date finContratDate) throws TopazeException {
		LocalDate dateFinContrat = LocalDate.fromDateFields(finContratDate);
		LocalDate dateDerniereFacture = LocalDate.fromDateFields(derniereFactureDate);
		LocalDate dateFinPeriode = dateDerniereFacture.plusMonths(periodicite);
		Double rembousement = null;
		if (dateFinPeriode.compareTo(dateFinContrat) > Constants.ZERO) {

			TimePeriod periodFacturation = new TimePeriod(dateFinContrat, dateFinPeriode);

			rembousement = calculRemboursement(montant, dateDerniereFacture, dateFinContrat, dateFinPeriode);

			Discount discountRemboursement = null;

			discountRemboursement =
					getReductionPourRemboursement(referenceContrat, numEC, version, periodicite, montant,
							derniereFactureDate, finContratDate, debutFacturationDate);

			RemboursementBillingInfo remboursementBillingInfo = new RemboursementBillingInfo();
			remboursementBillingInfo.setMontantRemboursement(rembousement);
			remboursementBillingInfo.setDiscount(discountRemboursement);
			remboursementBillingInfo.setTimePeriod(periodFacturation);
			return remboursementBillingInfo;
		}

		return null;
	}

	/**
	 * Calcule de montant remboursement.
	 * 
	 * @param montant
	 *            montant a payer par periode.
	 * @param dateDerniereFacture
	 *            date derniere facture.
	 * @param dateDebutPeriodeRemboursement
	 *            date debut de la periode de remboursement.
	 * @param dateFinPeriodeRemboursement
	 *            date debut de la periode de remboursement.
	 * @return montant a payer.
	 */
	public Double calculRemboursement(Double montant, LocalDate dateDerniereFacture,
			LocalDate dateDebutPeriodeRemboursement, LocalDate dateFinPeriodeRemboursement) {
		Double remboursement = null;

		/*
		 * calculer le nombre de jour a rembourser.
		 * 
		 * calculer le prix par jour.
		 */
		int nbrJourPeriode = Days.daysBetween(dateDerniereFacture, dateFinPeriodeRemboursement).getDays();

		int nbrJourRembourser = Days.daysBetween(dateDebutPeriodeRemboursement, dateFinPeriodeRemboursement).getDays();

		Months.monthsBetween(dateDebutPeriodeRemboursement, dateFinPeriodeRemboursement);

		remboursement = nbrJourRembourser * (montant / nbrJourPeriode);

		return remboursement;
	}

	/**
	 * Calculer la reduction pour la remboursement.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param numEC
	 *            numero element contractuel.
	 * @param version
	 *            version.
	 * @param periodicite
	 *            periodicite.
	 * @param montant
	 *            montant.
	 * @param derniereFactureDate
	 *            date derniere facture.
	 * @param finContratDate
	 *            date fin contrat.
	 * @param debutFacturationDate
	 *            date debut facturation.
	 * @return reduction sur remboursement.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public Discount getReductionPourRemboursement(String referenceContrat, Integer numEC, Integer version,
			Integer periodicite, Double montant, Date derniereFactureDate, Date finContratDate,
			Date debutFacturationDate) throws TopazeException {

		Discount discountTotalRemboursement = null;

		/*
		 * recuperation des reductions pour le calcule du montant de la reduction.
		 */
		List<ReductionInfo> reductionInfos =
				restClientBillingOutil.getReductionAssocie(referenceContrat, numEC, version);

		LocalDate dateDerniereFacture = LocalDate.fromDateFields(derniereFactureDate);
		LocalDate dateDebutFacturation = LocalDate.fromDateFields(debutFacturationDate);
		List<ReductionInfo> reductionsEligible = new ArrayList<>();
		for (ReductionInfo reductionInfo : reductionInfos) {
			if (reductionInfo.isEligible(dateDebutFacturation, dateDerniereFacture, periodicite)) {
				reductionsEligible.add(reductionInfo);
			}
		}

		List<DiscountInfo> discountsRemboursement =
				ReductionUtils.calculerReduction(periodicite, montant, reductionsEligible);

		/*
		 * calcule de la somme des discount remboursement.
		 */
		for (DiscountInfo discountRemboursement : discountsRemboursement) {

			Discount discount = discountRemboursement.getDiscount();
			/*
			 * si le discount est on Euro alors on doit calculer la valeur du discount pour la periode du remboursement.
			 */
			if (discount != null && discount.getDiscountType().equals(DiscountType.VALUE_HT)) {
				LocalDate dateFinContrat = LocalDate.fromDateFields(finContratDate);
				LocalDate dateFinPeriode = dateDerniereFacture.plusMonths(periodicite);

				double valeurDiscountRemboursement =
						calculRemboursement(discount.getAmount().getAmount().doubleValue(), dateDerniereFacture,
								dateFinContrat, dateFinPeriode);
				Amount amount = new Amount(valeurDiscountRemboursement);
				discount = new Discount(amount, discount.getDiscountType());

				discountTotalRemboursement =
						new Discount(discountTotalRemboursement != null ? discountTotalRemboursement.getAmount().add(
								discount.getAmount()) : discount.getAmount(), discount.getDiscountType());
			}
		}
		return discountTotalRemboursement;
	}
}