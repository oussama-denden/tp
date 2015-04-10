package com.nordnet.topaze.businessprocess.facture.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.facture.service.FactureService;
import com.nordnet.topaze.client.rest.RestClientFacture;
import com.nordnet.topaze.client.rest.business.facturation.ContratBillingInfo;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette Classe reste à l'écoute du activeMq pour effectuer le billing apres la reception de l'evenement
 * "ResiliationPartiel".
 * 
 * @author akram-moncer
 * 
 */
public class ContractPartialResiliatedListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(ContractPartialResiliatedListener.class);

	/**
	 * Autowiring du Mouvement Service.
	 */
	@Autowired
	private FactureService factureService;

	/**
	 * {@link RestClient}.
	 */
	@Autowired
	private RestClientFacture restClientFacture;

	@Override
	public void onMessage(Message message) {

		LOGGER.info("Reception de l'evenement: ContractPartialResiliated");
		if (message instanceof MapMessage) {
			try {
				final MapMessage mapMessage = (MapMessage) message;
				String referenceContrat = mapMessage.getString("referenceContrat");
				Integer numEC = mapMessage.getInt("numEC");
				ContratBillingInfo[] contratBillingInfos =
						restClientFacture.getContratBillingInformation(referenceContrat, numEC);

				// calculer le dernier billing du bon retour selon la politique de resiliation.
				factureService.calculeDernierBilling(contratBillingInfos, true, false, true, false);
			} catch (JMSException e) {
				LOGGER.error("Error occurs during call of  ContractPartialResiliatedListener.onMessage()", e);
			} catch (TopazeException e) {
				LOGGER.error("Error occurs during call of  ContractPartialResiliatedListener.onMessage()", e);
			} catch (Exception e) {
				LOGGER.error("Error occurs during call of  ContractPartialResiliatedListener.onMessage()", e);
			}
		}
	}
}
