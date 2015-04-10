package com.nordnet.topaze.businessprocess.core.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.core.service.BusinessProcessService;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette Classe reste à l'écoute du activeMq pour detecter l'evenement "Contract Migrated Event" afin de demarrer le
 * processus de migration.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class ContractMigratedListener implements MessageListener {

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
		LOGGER.info("Reception de l'evenement: ContractMigrated");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference contrat resilier par le client de la brique contrat
				String referenceContrat = mapMessage.getString("referenceContrat");
				if (referenceContrat != null) {
					businessProcessService.initierBM(referenceContrat);
				}
			} catch (JMSException | TopazeException | JSONException exception) {
				LOGGER.error("JMS error while listening to ContractMigrated event", exception);
			}
		}
	}
}