package com.nordnet.topaze.businessprocess.facture.calcule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nordnet.topaze.client.rest.RestClientFacture;
import com.nordnet.topaze.client.rest.RestClientNetDelivery;
import com.nordnet.topaze.client.rest.business.facturation.ContratBillingInfo;
import com.nordnet.topaze.client.rest.business.facturation.PenaliteBillingInfo;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Calcule de penelite.
 * 
 * @author Oussama Denden
 * 
 */
@Component("calculateurPenalite")
public class CalculateurPenalite {

	/**
	 * {@link RestClientNetDelivery}.
	 */
	@Autowired
	private RestClientFacture restClientFacture;

	/**
	 * {@link MouvementService}.
	 */
	@Autowired
	private MouvementService mouvementService;

	/**
	 * Calcule de Penalite.
	 * 
	 * @param contratBillingInfo
	 *            {@link ContratBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void penalite(ContratBillingInfo contratBillingInfo) throws TopazeException {

		if (contratBillingInfo.getEngagement() != null && contratBillingInfo.getEngagement() != 0) {
			PenaliteBillingInfo penaliteBillingInfo =
					restClientFacture.getPenaliteBillingInfo(contratBillingInfo.getReferenceContrat(),
							contratBillingInfo.getNumEC(), contratBillingInfo.getVersion(),
							contratBillingInfo.getEngagement(), contratBillingInfo.getPeriodicite(),
							contratBillingInfo.getMontant(), contratBillingInfo.getDateDerniereFacture(),
							contratBillingInfo.getDateDebutFacturation(), contratBillingInfo.getDateFinEngagement(),
							contratBillingInfo.getDateFinContrat());

			if (penaliteBillingInfo != null) {
				mouvementService.sendMouvementFraisPenalite(contratBillingInfo,
						penaliteBillingInfo.getMontantPenalite(), penaliteBillingInfo.getDiscount(),
						penaliteBillingInfo.getTimePeriod());

			}
		}
	}

}
