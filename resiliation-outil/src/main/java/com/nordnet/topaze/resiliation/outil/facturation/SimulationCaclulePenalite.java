package com.nordnet.topaze.resiliation.outil.facturation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.resiliation.outil.business.PenaliteBillingInfo;
import com.nordnet.topaze.resiliation.outil.business.ResiliationBillingInfo;
import com.nordnet.topaze.resiliation.outil.rest.RestClient;

/**
 * Simuler le calcule de penalite.
 * 
 * @author Oussama Denden
 * 
 */
@Component("penaliteCalculator")
public class SimulationCaclulePenalite {

	/**
	 * {@link RestClient}.
	 */
	@Autowired
	private RestClient restClient;

	/**
	 * Calcule de penalite.
	 * 
	 * @param resiliationBillingInfo
	 *            {@link ResiliationBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void penalite(ResiliationBillingInfo resiliationBillingInfo) throws TopazeException {
		PenaliteBillingInfo penaliteBillingInfo =
				restClient.getPenaliteBillingInfo(resiliationBillingInfo.getReferenceContrat(),
						resiliationBillingInfo.getNumEC(), resiliationBillingInfo.getVersion(),
						resiliationBillingInfo.getEngagement(), resiliationBillingInfo.getPeriodicite(),
						resiliationBillingInfo.getMontant(), resiliationBillingInfo.getDateDerniereFacture(),
						resiliationBillingInfo.getDateDebutFacturation(),
						resiliationBillingInfo.getDateFinEngagement(), resiliationBillingInfo.getDateFinContrat());

		resiliationBillingInfo.getResiliationInfo().setPenaliteBillingInfo(penaliteBillingInfo);
	}
}
