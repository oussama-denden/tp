package com.nordnet.topaze.businessprocess.facture.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.facture.service.FactureService;
import com.nordnet.topaze.businessprocess.facture.util.Constants;
import com.nordnet.topaze.businessprocess.facture.utils.spring.ApplicationContextHolder;
import com.nordnet.topaze.client.rest.RestClientFacture;
import com.nordnet.topaze.client.rest.RestClientNetDelivery;
import com.nordnet.topaze.client.rest.business.facturation.ContratBillingInfo;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.logger.service.TracageService;

/**
 * listener de l'evenement 'LunchSuccessionBilling' pour le clacul du remboursement au ancient client et la billing
 * recurrent pour le nouveau client.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class LunchRenewalBillingListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(LunchRenewalBillingListener.class);

	/**
	 * Autowiring du Mouvement Service.
	 */
	@Autowired
	private FactureService factureService;

	/**
	 * {@link RestClientNetDelivery}.
	 */
	@Autowired
	private RestClientFacture restClientFacture;

	/**
	 * {@link TracageService}.
	 */
	private TracageService tracageService;

	@Override
	public void onMessage(Message message) {
		try {
			LOGGER.info("Reception de l'evenement: LunchRenewalBillingListener");
			final MapMessage mapMessage = (MapMessage) message;
			String referenceContrat = mapMessage.getString("referenceContrat");
			getTracageService().ajouterTrace(Constants.PRODUCT, referenceContrat,
					"lancement du billing aprés renouvellement du contrat " + referenceContrat + ": billig recurrent",
					Constants.INTERNAL_USER);
			ContratBillingInfo[] contratBillingInfos =
					restClientFacture.getContratBillingInformation(referenceContrat, 0);
			for (ContratBillingInfo contratBillingInfo : contratBillingInfos) {

				// si contrat de vente(periodicite null) donc pas de billing recurrent.
				if (contratBillingInfo.getPeriodicite() != null) {
					factureService.calculerBillingRecurrent(contratBillingInfo);
				}
			}

		} catch (JMSException e) {
			LOGGER.error("Error occurs during call of  LunchRenewalBillingListener.onMessage()", e);
		} catch (TopazeException e) {
			LOGGER.error("Error occurs during call of  LunchRenewalBillingListener.onMessage()", e);
		} catch (Exception e) {
			LOGGER.error("Error occurs during call of  LunchRenewalBillingListener.onMessage()", e);
		}
	}

	/**
	 * 
	 * @return {@link TracageService}.
	 */
	private TracageService getTracageService() {
		if (tracageService == null) {
			if (System.getProperty("log.useMock").equals("true")) {
				tracageService = (TracageService) ApplicationContextHolder.getBean("tracageServiceMock");
			} else {
				tracageService = (TracageService) ApplicationContextHolder.getBean("tracageService");
			}
		}
		return tracageService;
	}

}
