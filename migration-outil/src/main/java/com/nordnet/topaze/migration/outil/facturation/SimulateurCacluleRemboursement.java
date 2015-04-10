package com.nordnet.topaze.migration.outil.facturation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.migration.outil.business.ECMigrationSimulationInfo;
import com.nordnet.topaze.migration.outil.business.RemboursementBillingInfo;
import com.nordnet.topaze.migration.outil.rest.RestClient;

/**
 * Simuler le calcule de remboursement.
 * 
 * @author Oussama Denden
 * 
 */
@Component("calculateurRemboursement")
public class SimulateurCacluleRemboursement {

	/**
	 * {@link RestClient}.
	 */
	@Autowired
	private RestClient restClient;

	/**
	 * Calcule de remboursement avec montant definit.
	 * 
	 * @param remboursementParent
	 *            {@link ECMigrationSimulationInfo}.
	 * @param montantRemboursement
	 *            montant definit.
	 * @return {@link RemboursementBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public RemboursementBillingInfo remboursementMontantDefini(ECMigrationSimulationInfo remboursementParent,
			Double montantRemboursement) throws TopazeException {
		RemboursementBillingInfo remboursementBillingInfo =
				restClient.getRemboursementBillingInfoMontantDefinit(remboursementParent.getPeriodicite(),
						montantRemboursement, remboursementParent.getDateDerniereFacture(),
						remboursementParent.getDateFinContrat());
		return remboursementBillingInfo;

	}

	/**
	 * Calcule de remboursement.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param elementContractuelInfo
	 *            {@link ECMigrationSimulationInfo}.
	 * @return {@link RemboursementBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public RemboursementBillingInfo remboursement(String referenceContrat,
			ECMigrationSimulationInfo elementContractuelInfo) throws TopazeException {
		RemboursementBillingInfo remboursementBillingInfo =
				restClient.getRemboursementBillingInfo(referenceContrat, elementContractuelInfo.getNumEC(),
						elementContractuelInfo.getVersion(), elementContractuelInfo.getPeriodicite(),
						elementContractuelInfo.getMontant(), elementContractuelInfo.getDateDerniereFacture(),
						elementContractuelInfo.getDateDebutFacturation(), elementContractuelInfo.getDateFinContrat());
		return remboursementBillingInfo;
	}

}
