package com.nordnet.topaze.migration.outil.facturation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.migration.outil.business.ECMigrationSimulationInfo;
import com.nordnet.topaze.migration.outil.business.PenaliteBillingInfo;
import com.nordnet.topaze.migration.outil.rest.RestClient;

/**
 * Simuler le calcule de penalite.
 * 
 * @author Oussama Denden
 * 
 */
@Component("calculateurPenalite")
public class SimulateurCaclulePenalite {

	/**
	 * {@link RestClient}.
	 */
	@Autowired
	private RestClient restClient;

	/**
	 * Calcule de penalite.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * 
	 * @param elementContractuelInfo
	 *            {@link ECMigrationSimulationInfo}.
	 * @return {@link PenaliteBillingInfo}
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public PenaliteBillingInfo penalite(String referenceContrat, ECMigrationSimulationInfo elementContractuelInfo)
			throws TopazeException {
		PenaliteBillingInfo penaliteBillingInfo =
				restClient.getPenaliteBillingInfo(referenceContrat, elementContractuelInfo.getNumEC(),
						elementContractuelInfo.getVersion(), elementContractuelInfo.getEngagement(),
						elementContractuelInfo.getPeriodicite(), elementContractuelInfo.getMontant(),
						elementContractuelInfo.getDateDerniereFacture(),
						elementContractuelInfo.getDateDebutFacturation(),
						elementContractuelInfo.getDateFinEngagement(), elementContractuelInfo.getDateFinContrat());

		return penaliteBillingInfo;
	}

}
