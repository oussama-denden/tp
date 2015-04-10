package com.nordnet.topaze.billing.outil.service;

import java.util.Date;

import com.nordnet.topaze.billing.outil.business.PenaliteBillingInfo;
import com.nordnet.topaze.exception.TopazeException;

/**
 * L'ensemeble des methodes de calcule de penalite.
 * 
 * @author Oussama Denden
 * 
 */
public interface PenaliteService {

	/**
	 * Calcule de penalite est creation de {@link PenaliteBillingInfo}.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param numEC
	 *            numero elementcontractuel.
	 * @param version
	 *            version.
	 * @param engagement
	 *            engagement.
	 * @param periodicite
	 *            periodicite.
	 * @param montant
	 *            montant.
	 * @param derniereFactureDate
	 *            derniere date de facturation.
	 * @param debutFacturationDate
	 *            date debut facturation.
	 * @param finEngagementDate
	 *            date fin engagement.
	 * @param finContratDate
	 *            date fin contrat.
	 * @return {@link PenaliteBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	PenaliteBillingInfo getPenaliteBillingInfo(String referenceContrat, Integer numEC, Integer version,
			Integer engagement, Integer periodicite, Double montant, Date derniereFactureDate,
			Date debutFacturationDate, Date finEngagementDate, Date finContratDate) throws TopazeException;

}
