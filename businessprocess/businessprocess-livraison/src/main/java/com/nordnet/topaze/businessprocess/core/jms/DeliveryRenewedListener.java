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
 * listener de l'evenenement 'DeliveryRenewed'.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class DeliveryRenewedListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(DeliveryRenewedListener.class);

	/**
	 * {@link BusinessProcessService}.
	 */
	@Autowired
	private BusinessProcessService businessProcessService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: DeliveryRenewedListener");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				String referenceBP = mapMessage.getString("referenceBP");
				if (referenceBP != null) {
					businessProcessService.changerDateDebutFacturation(referenceBP);
				}
			} catch (JMSException | TopazeException e) {
				LOGGER.error("JMS error while listening to DeliveryRenewedListener event", e);
			}
		}
	}
}