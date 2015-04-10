package com.nordnet.topaze.businessprocess.facture.calcule;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nordnet.topaze.businessprocess.facture.util.Constants;
import com.nordnet.topaze.businessprocess.facture.utils.spring.ApplicationContextHolder;
import com.nordnet.topaze.client.rest.RestClientFacture;
import com.nordnet.topaze.client.rest.RestClientNetDelivery;
import com.nordnet.topaze.client.rest.business.facturation.ContratBillingInfo;
import com.nordnet.topaze.client.rest.business.facturation.RemboursementBillingInfo;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.logger.service.TracageService;

/**
 * Calcule de remboursement.
 * 
 * @author Oussama Denden
 * 
 */
@Component("calculateurRemboursement")
public class CalculateurRemboursement {

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
	 * {@link TracageService}.
	 */
	private TracageService tracageService;

	/**
	 * Retourn le {@link TracageService}.
	 * 
	 * @return {@link TracageService}
	 */
	public TracageService getTracageService() {
		if (tracageService == null) {
			if (System.getProperty("log.useMock").equals("true")) {
				tracageService = (TracageService) ApplicationContextHolder.getBean("tracageServiceMock");
			} else {
				tracageService = (TracageService) ApplicationContextHolder.getBean("tracageService");
			}
		}
		return tracageService;
	}

	/**
	 * Remboursement avec un montant defini.
	 * 
	 * @param contratBillingInfo
	 *            the contrat billing info.
	 * @param montant
	 *            the montant.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void remboursementMontantDefini(ContratBillingInfo contratBillingInfo, double montant)
			throws TopazeException {

		Date dateRembourcement =
				contratBillingInfo.getPolitiqueResiliation().getDateRemboursement() != null ? contratBillingInfo
						.getPolitiqueResiliation().getDateRemboursement() : contratBillingInfo.getDateFinContrat();
		RemboursementBillingInfo remboursementBillingInfo =
				restClientFacture.getRemboursementBillingInfoMontantDefinit(contratBillingInfo.getPeriodicite(),
						montant, contratBillingInfo.getDateDerniereFacture(), dateRembourcement);
		if (remboursementBillingInfo != null) {
			mouvementService.sendMouvementRemboursement(contratBillingInfo,
					remboursementBillingInfo.getMontantRemboursement(), remboursementBillingInfo.getDiscount(),
					remboursementBillingInfo.getTimePeriod());
		}
	}

	/**
	 * Calcule de remboursement.
	 * 
	 * @param contratBillingInfo
	 *            {@link ContratBillingInfo}
	 * 
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * 
	 */
	public void remboursement(ContratBillingInfo contratBillingInfo) throws TopazeException {

		Date dateRembourcement =
				contratBillingInfo.getPolitiqueResiliation().getDateRemboursement() != null ? contratBillingInfo
						.getPolitiqueResiliation().getDateRemboursement() : contratBillingInfo.getDateFinContrat();

		RemboursementBillingInfo remboursementBillingInfo =
				restClientFacture.getRemboursementBillingInfo(contratBillingInfo.getReferenceContrat(),
						contratBillingInfo.getNumEC(), contratBillingInfo.getVersion(),
						contratBillingInfo.getPeriodicite(), contratBillingInfo.getMontant(),
						contratBillingInfo.getDateDerniereFacture(), contratBillingInfo.getDateDebutFacturation(),
						dateRembourcement);
		if (remboursementBillingInfo != null && remboursementBillingInfo.getTimePeriod() != null) {
			// tracer l'operation
			getTracageService().ajouterTrace(
					Constants.PRODUCT,
					contratBillingInfo.getReferenceContrat().split("-")[0],
					"Remboursement du contrat " + contratBillingInfo.getReferenceContrat().split("-")[0]
							+ " pour la période du " + remboursementBillingInfo.getTimePeriod().getStartFrom() + " au "
							+ remboursementBillingInfo.getTimePeriod().getEndTo(), Constants.INTERNAL_USER);
			mouvementService.sendMouvementRemboursement(contratBillingInfo,
					remboursementBillingInfo.getMontantRemboursement(), remboursementBillingInfo.getDiscount(),
					remboursementBillingInfo.getTimePeriod());
		}

	}

}
