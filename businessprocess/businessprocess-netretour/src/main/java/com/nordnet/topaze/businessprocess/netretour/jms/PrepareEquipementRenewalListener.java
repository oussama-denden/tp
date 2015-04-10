package com.nordnet.topaze.businessprocess.netretour.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.netretour.service.NetRetourService;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette classe est responsable d'ecouter l'evenement "PrepareEquipementRenewal" pour commencer la traduction des bien
 * de renouvellement.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class PrepareEquipementRenewalListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(PrepareEquipementRenewalListener.class);

	/**
	 * {@link NetDeliveryService}.
	 */
	@Autowired
	private NetRetourService netRetourService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: PrepareEquipementRenewal");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference contrat resilier par le client de la brique contrat
				String referenceBP = mapMessage.getString("referenceBP");
				if (referenceBP != null) {
					netRetourService.activerBienRenouvellement(referenceBP);
				}
			} catch (JMSException | TopazeException exception) {
				LOGGER.error("Error occurs during call of  PrepareEquipementRenewalListener.onMessage()", exception);
			}
		}
	}
}