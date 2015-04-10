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
 * Implementation de l'interface {@link RecuperationMessagesSender}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Component("recuperationMessagesSender")
public class RecuperationMessagesSenderImpl implements RecuperationMessagesSender {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(RecuperationMessagesSender.class);

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
	public void sendReturnInitiatedEvent(BonPreparation bonRecuperation) {
		LOGGER.info("Sending event: ReturnInitiated");
		Destination destination = new ActiveMQQueue("ReturnInitiatedQueue");
		Map<String, String> returnInitiatedEvent = new HashMap<>();
		returnInitiatedEvent.put("referenceBP", bonRecuperation.getReference());
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(returnInitiatedEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendSubReturnInitiatedEvent(BonPreparation bonRecuperation) {
		LOGGER.info("sending event: SubReturnInitiated");
		Destination destination = new ActiveMQQueue("SubReturnInitiatedQueue");
		Map<String, String> subReturnInitiatedEvent = new HashMap<>();
		subReturnInitiatedEvent.put("referenceBP", bonRecuperation.getReference());
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(subReturnInitiatedEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendPrepareReturnEvent(BonPreparation bonRecuperationGlobal) {
		LOGGER.info("Sending events: PrepareEquipementReturn, PrepareServiceReturn");
		Destination destination = new ActiveMQQueue("PrepareEquipementReturnQueue, PrepareServiceReturnQueue");
		Map<String, String> prepareReturnEvent = new HashMap<>();
		prepareReturnEvent.put("referenceBP", bonRecuperationGlobal.getReference());
		prepareReturnEvent.put("typeBonPreparation", bonRecuperationGlobal.getTypeBonPreparation().toString());
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(prepareReturnEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendReturnReturnedEvent(Map<String, Object> recuperationEvent) {
		LOGGER.info("Sending event: ReturnReturned");
		Destination destination = new ActiveMQQueue("ReturnReturnedQueue");
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(recuperationEvent);
	}

}
