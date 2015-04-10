package com.nordnet.topaze.resiliation.outil.test.mock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.resiliation.outil.business.ContratResiliationInfo;
import com.nordnet.topaze.resiliation.outil.business.ResiliationBillingInfo;
import com.nordnet.topaze.resiliation.outil.enums.ModeFacturation;
import com.nordnet.topaze.resiliation.outil.enums.TypeContrat;
import com.nordnet.topaze.resiliation.outil.enums.TypeProduit;
import com.nordnet.topaze.resiliation.outil.rest.RestClient;

/**
 * Resiliation outil rest client mock.
 * 
 * @author Denden Oussama
 * 
 */
public class RestClientMock extends RestClient {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(RestClientMock.class);

	/**
	 * constructeur par defaut.
	 */
	public RestClientMock() {

	}

	/**
	 * chercher un Contrat a partir de la brique Contrat.
	 * 
	 * @param referenceContrat
	 *            reference de Contrat.
	 * @return {@link ContratResiliationInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@Override
	public ResiliationBillingInfo[] getResiliationBillingInfo(String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-call:::getContratResiliationInfo");
		return getResiliationBillingInfos();

	}

	@Override
	public ResiliationBillingInfo[] getResiliationBillingInfo(String referenceContrat, Integer numEC)
			throws TopazeException {
		LOGGER.info(":::ws-call:::getContratResiliationInfo");
		return getResiliationBillingInfos();
	};

	/**
	 * @return liste de {@link ResiliationBillingInfo}.
	 */
	private static ResiliationBillingInfo[] getResiliationBillingInfos() {
		ResiliationBillingInfo[] resiliationBillingInfos = new ResiliationBillingInfo[3];

		LocalDate date = new LocalDate(2014, 01, 01);

		ResiliationBillingInfo info1 = new ResiliationBillingInfo();
		info1.setTypeContrat(TypeContrat.ABONNEMENT);
		info1.setDateDebutFacturation(date.toDate());
		info1.setDateDerniereFacture(date.plusMonths(3).toDate());
		info1.setDateFinContrat(date.plusMonths(6).toDate());
		info1.setDateFinEngagement(date.plusYears(1).toDate());
		info1.setRemboursable(true);
		info1.setEngagement(12);
		info1.setDureeContrat(24);
		info1.setPeriodicite(3);
		info1.setMontant(55d);
		info1.setModeFacturation(ModeFacturation.DATE_ANNIVERSAIRE);
		info1.setReferenceProduit("PROD-01");
		info1.setTypeElement(TypeProduit.SERVICE);

		ResiliationBillingInfo info2 = new ResiliationBillingInfo();
		info2.setTypeContrat(TypeContrat.ABONNEMENT);
		info2.setDateDebutFacturation(date.toDate());
		info2.setDateDerniereFacture(date.plusYears(2).toDate());
		info2.setDateFinContrat(date.plusYears(2).plusMonths(2).toDate());
		info2.setDateFinEngagement(date.plusYears(1).toDate());
		info2.setRemboursable(true);
		info2.setEngagement(12);
		info2.setDureeContrat(36);
		info2.setPeriodicite(5);
		info2.setMontant(20d);
		info2.setModeFacturation(ModeFacturation.DATE_ANNIVERSAIRE);
		info2.setReferenceProduit("PROD-02");
		info2.setTypeElement(TypeProduit.BIEN);

		ResiliationBillingInfo info3 = new ResiliationBillingInfo();
		info3.setTypeContrat(TypeContrat.ABONNEMENT);
		info3.setDateDebutFacturation(date.toDate());
		info3.setDateDerniereFacture(date.plusMonths(6).toDate());
		info3.setDateFinContrat(date.plusMonths(8).toDate());
		info3.setDateFinEngagement(date.plusYears(2).toDate());
		info3.setRemboursable(true);
		info3.setEngagement(24);
		info3.setDureeContrat(24);
		info3.setPeriodicite(3);
		info3.setMontant(180d);
		info3.setModeFacturation(ModeFacturation.DATE_ANNIVERSAIRE);
		info3.setReferenceProduit("PROD-03");
		info3.setTypeElement(TypeProduit.SERVICE);

		resiliationBillingInfos[0] = info1;
		resiliationBillingInfos[1] = info2;
		resiliationBillingInfos[2] = info3;

		return resiliationBillingInfos;
	}

}
