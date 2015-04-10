package com.nordnet.topaze.businessprocess.netretour.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.netretour.service.NetRetourService;
import com.nordnet.topaze.client.rest.enums.TypeBonPreparation;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette classe est responsable d'ecouter l'evenement "TraductionRecuperationBien" pour commencer la traduction d'un BR
 * vers NetRetour.
 * 
 * @author akram-moncer
 * @author Denden-OUSSAMA
 * 
 */
public class PrepareEquipementReturnListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(PrepareEquipementReturnListener.class);

	/**
	 * {@link NetDeliveryService}.
	 */
	@Autowired
	private NetRetourService netRetourService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: PrepareEquipementReturn");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference contrat resilier par le client de la brique contrat
				String referenceBP = mapMessage.getString("referenceBP");
				TypeBonPreparation typeBonPreparation =
						TypeBonPreparation.fromString(mapMessage.getString("typeBonPreparation"));
				if (referenceBP != null) {
					netRetourService.traductionNetRetour(referenceBP, typeBonPreparation);
				}
			} catch (JMSException | TopazeException exception) {
				LOGGER.error("Error occurs during call of  PrepareEquipementReturnListener.onMessage()", exception);
			}
		}
	}
}