package com.nordnet.topaze.businessprocess.facture.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.facture.service.FactureService;
import com.nordnet.topaze.client.rest.RestClientFacture;
import com.nordnet.topaze.client.rest.RestClientNetDelivery;
import com.nordnet.topaze.client.rest.business.facturation.ContratBillingInfo;
import com.nordnet.topaze.exception.TopazeException;

/**
 * listener pour écouter l'evenement 'ContratSuccessionBilling' afin de facturer les frais de cession d'un contrat.
 * 
 * @author akram-moncer
 * 
 */
public class ContratSuccessionListener implements MessageListener {

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
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(ContratSuccessionListener.class);

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: ContratSuccessionBilling");
		try {
			final MapMessage mapMessage = (MapMessage) message;
			String referenceContrat = mapMessage.getString("referenceContrat");

			ContratBillingInfo[] contratBillingInfos =
					restClientFacture.getContratBillingInformationHistorise(referenceContrat);

			factureService.calculeFraisCession(contratBillingInfos);

		} catch (JMSException e) {
			LOGGER.error("Error occurs during call of  ContratSuccessionListener.onMessage()", e);
		} catch (TopazeException e) {
			LOGGER.error("Error occurs during call of  ContratSuccessionListener.onMessage()", e);
		} catch (Exception e) {
			LOGGER.error("Error occurs during call of  ContratSuccessionListener.onMessage()", e);
		}
	}

}
