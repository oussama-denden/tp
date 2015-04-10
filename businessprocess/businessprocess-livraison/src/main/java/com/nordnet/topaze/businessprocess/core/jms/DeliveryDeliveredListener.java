package com.nordnet.topaze.businessprocess.core.jms;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.core.service.BusinessProcessService;
import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;

/**
 * 
 * Cette Classe reste à l'écoute du activeMq pour indiqué la date début facturation une fois le {@link BonPreparation}
 * est livré interception de l'evenement "BPGlobalLivre".
 * 
 * @author anisselmane.
 * 
 */
public class DeliveryDeliveredListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(DeliveryDeliveredListener.class);

	/**
	 * {@link BusinessProcessService}.
	 */
	@Autowired
	private BusinessProcessService businessProcessService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: DeliveryDelivered");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference contrat resilier par le client de la brique contrat
				String referenceContrat = mapMessage.getString("referenceContrat");
				if (referenceContrat != null) {
					businessProcessService.changerDateDebutFacturation(referenceContrat);
				}
			} catch (Exception exception) {
				LOGGER.error("JMS error while listening to DeliveryDelivered event", exception);
			}
		}
	}
}