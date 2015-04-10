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
 * Cette Classe reste à l'écoute du activeMq pour detecter l'evenement "Contract Renewed Event" afin de demarrer le
 * processus de renouvellement .
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class ContractRenewedListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(ContractMigratedListener.class);

	/**
	 * {@link BusinessProcessService}.
	 */
	@Autowired
	private BusinessProcessService businessProcessService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: ContractRenewed");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation de bon de preparation pour les elements renouveles
				String referenceContrat = mapMessage.getString("referenceContrat");
				if (referenceContrat != null) {
					businessProcessService.initierBRE(referenceContrat);
				}
			} catch (JMSException | TopazeException exception) {
				LOGGER.error("JMS error while listening to ContractRenewed event", exception);
			}
		}
	}
}