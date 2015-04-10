package com.nordnet.topaze.businessprocess.facture.test.mock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nordnet.topaze.client.rest.RestClientFacture;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Mock du client rest {@link RestClientFacture} pour les tests.
 * 
 * @author akram-moncer
 * 
 */
public class RestClientMock extends RestClientFacture {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(RestClientMock.class);

	@Override
	public void changerDateDerniereFacture(String referenceContrat, Integer numEC) throws TopazeException {
		LOGGER.info("Appel de la brique contrat pour modifier la date de derniere facture.");
	}

	@Override
	public void changerDateFinContrat(String referenceContrat, Integer numEC) throws TopazeException {
		LOGGER.info("Appel de la brique contrat pour modifier la date de fin contrat.");
	}

	// @Override
	// public List<ReductionInfo> getReductionAssocie(String referenceContrat, Integer NumEC, Integer version)
	// throws TopazeException {
	// LOGGER.info("Appel de la brique contrat pour recuperer les reduction associe a l'element contractuel.");
	//
	// ReductionInfo reductionInfo = new ReductionInfo();
	// reductionInfo.setValeur(5d);
	// reductionInfo.setTypeValeur(TypeValeurReduction.EURO);
	// reductionInfo.setNbUtilisationMax(10);
	// List<ReductionInfo> reductionInfos = Arrays.asList(reductionInfo);
	//
	// return reductionInfos;
	// }

}
