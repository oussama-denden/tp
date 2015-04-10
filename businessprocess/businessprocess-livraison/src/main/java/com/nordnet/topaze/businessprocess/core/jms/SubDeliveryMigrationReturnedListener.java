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
 * Cette Classe reste à l'écoute du activeMq pour detecter l'evenement "SousBMRetourne" afin de marquer un BMGLobal
 * comme retourne
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */

public class SubDeliveryMigrationReturnedListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(SubDeliveryMigrationReturnedListener.class);

	/**
	 * {@link BusinessProcessService}.
	 */
	@Autowired
	private BusinessProcessService businessProcessService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: SubDeliveryMigrationReturned");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference du bon de migration
				String referenceBM = mapMessage.getString("referenceBM");
				if (referenceBM != null) {
					businessProcessService.marquerBMRetourne(referenceBM);
				}
			} catch (JMSException | TopazeException e) {
				LOGGER.error("JMS error while listening to SubDeliveryMigrationReturned event", e);
			}
		}
	}
}