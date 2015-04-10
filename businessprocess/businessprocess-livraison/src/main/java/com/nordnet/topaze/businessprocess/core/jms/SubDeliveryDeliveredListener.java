package com.nordnet.topaze.businessprocess.core.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.core.service.BusinessProcessService;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette Classe reste à l'écoute du activeMq pour detecter l'evenement "SousBPLivre" afin de maarquer un BPGLobal comme
 * livere.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class SubDeliveryDeliveredListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(SubDeliveryDeliveredListener.class);

	/**
	 * {@link BusinessProcessService}.
	 */
	@Autowired
	private BusinessProcessService businessProcessService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: SubDeliveryDelivered");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference contrat resilier par le client de la brique contrat
				String referenceBP = mapMessage.getString("referenceBP");
				if (message instanceof MapMessage) {
					if (referenceBP != null) {
						businessProcessService.marquerBPGlobalLivre(referenceBP);
					}
				}
			} catch (JMSException | TopazeException exception) {
				LOGGER.error("JMS error while listening to SubDeliveryDelivered event", exception);
			}
		}
	}
}