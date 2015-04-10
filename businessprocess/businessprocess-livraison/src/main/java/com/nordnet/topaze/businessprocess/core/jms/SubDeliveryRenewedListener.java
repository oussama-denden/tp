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
 * cette classe est à l'ecoute de l'evennement jms SubDeliveryRenewed afin de marquer le bon parent comme renouvele
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class SubDeliveryRenewedListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(SubDeliveryRenewedListener.class);

	/**
	 * {@link BusinessProcessService}.
	 */
	@Autowired
	private BusinessProcessService businessProcessService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: SubDeliveryRenewed");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				String referenceBP = mapMessage.getString("referenceBP");
				if (referenceBP != null) {
					businessProcessService.marquerBPGlobalRenouvele(referenceBP);
				}
			} catch (JMSException | TopazeException e) {
				LOGGER.error("JMS error while listening to SubDeliveryRenewed event", e);
			}
		}
	}
}