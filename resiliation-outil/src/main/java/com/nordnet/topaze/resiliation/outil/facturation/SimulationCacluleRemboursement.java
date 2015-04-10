package com.nordnet.topaze.resiliation.outil.facturation;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.resiliation.outil.business.PolitiqueResiliation;
import com.nordnet.topaze.resiliation.outil.business.Remboursement;
import com.nordnet.topaze.resiliation.outil.business.RemboursementBillingInfo;
import com.nordnet.topaze.resiliation.outil.business.ResiliationBillingInfo;
import com.nordnet.topaze.resiliation.outil.rest.RestClient;

/**
 * Simuler le calcule de remboursement.
 * 
 * @author Oussama Denden
 * 
 */
@Component("remboursementCalculator")
public class SimulationCacluleRemboursement {

	/**
	 * {@link RestClient}.
	 */
	@Autowired
	private RestClient restClient;

	/**
	 * Calcule de remboursement avec montant definit.
	 * 
	 * @param remboursementParent
	 *            {@link ResiliationBillingInfo}.
	 * @param politiqueResiliation
	 *            {@link PolitiqueResiliation}.
	 * @return {@link Remboursement}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public RemboursementBillingInfo remboursementMontantDefini(ResiliationBillingInfo remboursementParent,
			PolitiqueResiliation politiqueResiliation) throws TopazeException {
		Date dateRembourcement =
				politiqueResiliation.getDateRemboursement() != null ? politiqueResiliation.getDateRemboursement()
						: remboursementParent.getDateFinContrat();
		RemboursementBillingInfo remboursementBillingInfo =
				restClient.getRemboursementBillingInfoMontantDefinit(remboursementParent.getPeriodicite(),
						politiqueResiliation.getMontantRemboursement(), remboursementParent.getDateDerniereFacture(),
						dateRembourcement);
		return remboursementBillingInfo;

	}

	/**
	 * Calcule de remboursement.
	 * 
	 * @param resiliationBillingInfo
	 *            {@link ResiliationBillingInfo}.
	 * @param politiqueResiliation
	 *            {@link PolitiqueResiliation}
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void remboursement(ResiliationBillingInfo resiliationBillingInfo, PolitiqueResiliation politiqueResiliation)
			throws TopazeException {

		Date dateRembourcement =
				politiqueResiliation.getDateRemboursement() != null ? politiqueResiliation.getDateRemboursement()
						: resiliationBillingInfo.getDateFinContrat();
		RemboursementBillingInfo remboursementBillingInfo =
				restClient.getRemboursementBillingInfo(resiliationBillingInfo.getReferenceContrat(),
						resiliationBillingInfo.getNumEC(), resiliationBillingInfo.getVersion(),
						resiliationBillingInfo.getPeriodicite(), resiliationBillingInfo.getMontant(),
						resiliationBillingInfo.getDateDerniereFacture(),
						resiliationBillingInfo.getDateDebutFacturation(), dateRembourcement);

		resiliationBillingInfo.getResiliationInfo().setRemboursementBillingInfo(remboursementBillingInfo);

	}

}
