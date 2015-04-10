package com.nordnet.topaze.businessprocess.netdelivery.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.netdelivery.service.NetDeliveryService;
import com.nordnet.topaze.client.rest.enums.TypeBonPreparation;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette classe reste a l'ecoute d'un evenemet "TraductionLivraisonBien" pour commencer la traduction vers packager.
 * 
 * @author akram-moncer
 * 
 */
public class PrepareEquipementDeliveryListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(PrepareEquipementDeliveryListener.class);

	/**
	 * {@link NetDeliveryService}.
	 */
	@Autowired
	private NetDeliveryService netDeliveryService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: PrepareEquipementDelivery");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference de bon de preparation global.
				String referenceBP = mapMessage.getString("referenceBP");
				TypeBonPreparation typeBonPreparation =
						TypeBonPreparation.fromString(mapMessage.getString("typeBonPreparation"));
				netDeliveryService.traductionNetDelivery(referenceBP, typeBonPreparation);
			} catch (JMSException | TopazeException exception) {
				LOGGER.error("JMS error while listening to PrepareEquipementDelivery event", exception);
			}
		}
	}
}