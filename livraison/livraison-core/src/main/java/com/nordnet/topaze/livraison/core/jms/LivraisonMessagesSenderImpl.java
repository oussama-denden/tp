package com.nordnet.topaze.livraison.core.jms;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.topaze.livraison.core.domain.BonPreparation;

/**
 * Implementation de l'interface {@link LivraisonMessagesSender}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Component("livraisonMessagesSender")
public class LivraisonMessagesSenderImpl implements LivraisonMessagesSender {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(LivraisonMessagesSender.class);

	/**
	 * {@link JmsTemplate}.
	 */
	@Autowired
	private JmsTemplate jmsTemplate;

	/**
	 * Alert service.
	 */
	@Autowired
	SendAlert sendAlert;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendDeliveryInitiatedEvent(BonPreparation bonPreparationGlobal) {
		LOGGER.info("Sending event: DeliveryInitiated");
		Destination destination = new ActiveMQQueue("DeliveryInitiatedQueue");
		Map<String, String> bpInitieEvent = new HashMap<>();
		bpInitieEvent.put("referenceBP", bonPreparationGlobal.getReference());
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(bpInitieEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendPrepareDeliveryEvent(BonPreparation bonPreparationGlobal) {
		LOGGER.info("Sending events: PrepareEquipementDelivery, PrepareServiceDelivery");
		Destination destination = new ActiveMQQueue("PrepareEquipementDeliveryQueue, PrepareServiceDeliveryQueue");
		Map<String, String> prepareDeliveryEvent = new HashMap<>();
		prepareDeliveryEvent.put("referenceBP", bonPreparationGlobal.getReference());
		prepareDeliveryEvent.put("typeBonPreparation", bonPreparationGlobal.getTypeBonPreparation().toString());
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(prepareDeliveryEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendSubDeliveryPreparedEvent(BonPreparation bonPreparationGlobal) {
		LOGGER.info("Sending event: SubDeliveryPrepared");
		Destination destination = new ActiveMQQueue("SubDeliveryPreparedQueue");
		Map<String, String> sousBPPrepareEvent = new HashMap<>();
		sousBPPrepareEvent.put("referenceBP", bonPreparationGlobal.getReference());
		sousBPPrepareEvent.put("typeBonPreparation", bonPreparationGlobal.getTypeBonPreparation().toString());
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(sousBPPrepareEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendSubDeliveryDeliveredEvent(BonPreparation bpGlobal) {
		LOGGER.info("Sending event: SubDeliveryDelivered");
		Destination destination = new ActiveMQQueue("SubDeliveryDeliveredQueue");
		Map<String, String> sousBPLivreEvent = new HashMap<>();
		sousBPLivreEvent.put("referenceBP", bpGlobal.getReference());
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(sousBPLivreEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendDeliveryDeliveredEvent(String referenceContrat) {
		LOGGER.info("Sending event: DeliveryDelivered");
		Destination destination = new ActiveMQQueue("DeliveryDeliveredQueue");
		Map<String, String> bpGlobalLivreEvent = new HashMap<>();
		bpGlobalLivreEvent.put("referenceContrat", referenceContrat);
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(bpGlobalLivreEvent);
	}

}