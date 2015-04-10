package com.nordnet.topaze.businessprocess.swap.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.swap.service.SwapService;
import com.nordnet.topaze.exception.TopazeException;

/**
 * listener de l'evenement 'PrepareEquipementSuccession' pour lancer la cession des bien au nouveau client.
 * 
 * @author akram-moncer
 * 
 */
public class PrepareEquipementSuccessionListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(PrepareEquipementSuccessionListener.class);

	/**
	 * {@link SwapService}.
	 */
	@Autowired
	private SwapService swapService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: PrepareEquipementSuccession");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference de bon de preparation global.
				String referenceBP = mapMessage.getString("referenceBP");
				if (referenceBP != null) {
					swapService.cederEquipement(referenceBP);
				}
			} catch (JMSException | TopazeException exception) {
				LOGGER.error("Error occurs during call of  ContratMigrationReturnedListener.onMessage()", exception);
			}
		}
	}
}