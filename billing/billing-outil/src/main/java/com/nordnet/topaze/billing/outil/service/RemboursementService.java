package com.nordnet.topaze.billing.outil.service;

import java.util.Date;

import com.nordnet.topaze.billing.outil.business.PenaliteBillingInfo;
import com.nordnet.topaze.billing.outil.business.RemboursementBillingInfo;
import com.nordnet.topaze.exception.TopazeException;

/**
 * L'ensemeble des methodes de calcule de remboursement.
 * 
 * @author Oussama Denden
 * 
 */
public interface RemboursementService {

	/**
	 * Calcule de remboursement est creation de {@link RemboursementBillingInfo}.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param numEC
	 *            numero elementcontractuel.
	 * @param version
	 *            version.
	 * @param periodicite
	 *            periodicite.
	 * @param montant
	 *            montant.
	 * @param dateDerniereFacture
	 *            derniere date de facturation.
	 * @param dateDebutFacturation
	 *            date debut facturation.
	 * @param dateFinContrat
	 *            date fin contrat.
	 * @return {@link PenaliteBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	RemboursementBillingInfo getRemboursementBillingInfo(String referenceContrat, Integer numEC, Integer version,
			Integer periodicite, Double montant, Date dateDerniereFacture, Date dateDebutFacturation,
			Date dateFinContrat) throws TopazeException;

	/**
	 * Remboursement montant defini.
	 * 
	 * 
	 * @param periodicite
	 *            periodicite.
	 * @param montant
	 *            montant.
	 * @param derniereFactureDate
	 *            date derniere facture.
	 * @param finContratDate
	 *            date fin contrat.
	 * @return {@link RemboursementBillingInfo}.
	 */
	public RemboursementBillingInfo getRemboursementBillingInfoMontantDefinit(Integer periodicite, Double montant,
			Date derniereFactureDate, Date finContratDate);

}
