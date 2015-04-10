package com.nordnet.topaze.resiliation.outil.validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.resiliation.outil.business.PolitiqueResiliation;
import com.nordnet.topaze.resiliation.outil.business.ResiliationBillingInfo;
import com.nordnet.topaze.resiliation.outil.util.Constants;
import com.nordnet.topaze.resiliation.outil.util.PropertiesUtil;
import com.nordnet.topaze.resiliation.outil.util.Utils;

/**
 * Valider les donnees de simulation.
 * 
 * @author Oussama Denden
 * 
 */
public class ResiliationOutilValidator {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(ResiliationOutilValidator.class);

	/**
	 * {@link PropertiesUtil}.
	 */
	private static PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();

	/**
	 * Valider les informations de simulation de resiliation global.
	 * 
	 * @param referenceContrat
	 *            reference de contrat.
	 * @param politiqueResiliation
	 *            {@link PolitiqueResiliation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validerSimulerResiliation(String referenceContrat, PolitiqueResiliation politiqueResiliation)
			throws TopazeException {

		if (Utils.isStringNullOrEmpty(referenceContrat)) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "referenceContrat"), "0.1.1");
		}

		validerPolitiqueResiliation(politiqueResiliation);

	}

	/**
	 * Valider les informations de simulation de resiliation partiel.
	 * 
	 * @param referenceContrat
	 *            reference de contrat.
	 * @param numEC
	 *            numEC de produit.
	 * @param politiqueResiliation
	 *            {@link PolitiqueResiliation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validerSimulerResiliationPartiel(String referenceContrat, Integer numEC,
			PolitiqueResiliation politiqueResiliation) throws TopazeException {

		if (Utils.isStringNullOrEmpty(referenceContrat)) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "referenceContrat"), "0.1.1");
		}

		if (numEC < Constants.UN) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "numEC"), "0.1.1");
		}

		validerPolitiqueResiliation(politiqueResiliation);

	}

	/**
	 * Valider le politique de resiliation.
	 * 
	 * @param politiqueResiliation
	 *            {@link PolitiqueResiliation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	private static void validerPolitiqueResiliation(PolitiqueResiliation politiqueResiliation) throws TopazeException {
		if (politiqueResiliation == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.17", "résiliation"), "1.1.17");
		}

		if (politiqueResiliation.isFraisResiliation() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PolitiqueResiliation.fraisResiliation"),
					"0.1.1");
		}
		if (politiqueResiliation.isPenalite() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PolitiqueResiliation.penalite"), "0.1.1");
		}
		if (politiqueResiliation.isRemboursement() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PolitiqueResiliation.remboursement"),
					"0.1.1");
		}
		if (politiqueResiliation.getDateResiliation() != null
				&& Utils.compareDate(verifierDateWithoutTimeValide(politiqueResiliation.getDateResiliation()),
						new Date()) == -1) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.92", "PolitiqueResiliation.dateResiliation"),
					"1.1.92");
		}
		if (!politiqueResiliation.isFraisResiliation() && politiqueResiliation.getMontantResiliation() != null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.102"), "1.1.102");
		}

	}

	/**
	 * Verifier date valide.
	 * 
	 * @param stringDate
	 *            date en format string
	 * @return date
	 * @throws TopazeException
	 *             forcontrat exception
	 */
	private static Date verifierDateWithoutTimeValide(String stringDate) throws TopazeException {
		DateFormat formatter = com.nordnet.topaze.resiliation.outil.util.Constants.DEFAULT_DATE_WITHOUT_TIME_FORMAT;
		Date date = null;
		try {
			date = formatter.parse(stringDate);
		} catch (ParseException e) {

			LOGGER.error("Error occurs during parse date", e);
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.8", stringDate), "0.1.8");
		}
		return date;

	}

	/**
	 * Controle sur la date de remboursement.
	 * 
	 * @param politiqueResiliation
	 *            {@link PolitiqueResiliation}.
	 * @param resiliationBillingInfos
	 *            {@link ResiliationBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void checkDateRemboursement(PolitiqueResiliation politiqueResiliation,
			ResiliationBillingInfo[] resiliationBillingInfos) throws TopazeException {
		if (politiqueResiliation.getDateRemboursement() != null) {
			if (Utils.compareDateWithoutTime(politiqueResiliation.getDateRemboursement(),
					resiliationBillingInfos[0].getDateValidation()) < 0) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.147"), "1.1.147");
			}

			Integer periodicite = resiliationBillingInfos[0].getPeriodicite();
			LocalDate dateFinPeriode =
					LocalDate.fromDateFields(resiliationBillingInfos[0].getDateDerniereFacture()).plusMonths(
							periodicite);

			if (Utils.compareDateWithoutTime(politiqueResiliation.getDateRemboursement(), dateFinPeriode.toDate()) > 0) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.148"), "1.1.148");
			}
		}

	}

}
