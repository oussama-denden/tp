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
 * Cette classe est responsable d'ecouter l'evenement "PrepareSawapping" pour commencer la traduction d'un BM vers swap.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class PrepareSawappingListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(PrepareSawappingListener.class);

	/**
	 * {@link SwapService}.
	 */
	@Autowired
	private SwapService swapService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: PrepareSwapping");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference de bon de preparation global.
				String referenceBP = mapMessage.getString("referenceBP");
				if (referenceBP != null) {
					swapService.traductionSwap(referenceBP);
				}
			} catch (JMSException | TopazeException e) {
				LOGGER.error("Error occurs during call of  PrepareSwappingListener.onMessage()", e);
			}
		}
	}
}