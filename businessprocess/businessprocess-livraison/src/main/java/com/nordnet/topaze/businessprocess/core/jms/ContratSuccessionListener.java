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
 * listener de l'evenement 'ContratSuccession'.
 * 
 * @author akram-moncer
 * 
 */
public class ContratSuccessionListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(ContratSuccessionListener.class);

	/**
	 * {@link BusinessProcessService}.
	 */
	@Autowired
	private BusinessProcessService businessProcessService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: ContratSuccession");
		if (message instanceof MapMessage) {
			try {
				final MapMessage mapMessage = (MapMessage) message;
				String referenceContrat = mapMessage.getString("referenceContrat");
				if (referenceContrat != null) {
					businessProcessService.initierCession(referenceContrat);
				}
			} catch (JMSException | TopazeException exception) {
				LOGGER.error("JMS error while listening to ContratSuccession event", exception);
			}
		}
	}
}
