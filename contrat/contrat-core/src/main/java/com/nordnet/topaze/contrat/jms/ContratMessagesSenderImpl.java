package com.nordnet.topaze.contrat.jms;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Component("contratMessagesSender")
public class ContratMessagesSenderImpl implements ContratMessagesSender {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(ContratMessagesSenderImpl.class);

	/**
	 * {@link JmsTemplate}.
	 */
	@Autowired
	private JmsTemplate jmsTemplate;

	@Override
	public void sendContratValidatedEvent(Map<String, Object> contratValidatedInfo) {
		LOGGER.info("Sending event: ContratValidated");
		Destination destination = new ActiveMQQueue("ContratValidatedQueue");
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(contratValidatedInfo);
	}

	@Override
	public void sendControlVenteEvent(Map<String, Object> contratValideInfo) {
		LOGGER.info("Sending event: ControlVente");
		Destination destination = new ActiveMQQueue("ControlVenteQueue");
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(contratValideInfo);

	}

	@Override
	public void sendLaunchBillingEvent(String referenceContrat) {
		LOGGER.info("Sending event: LaunchBilling");
		Destination destination = new ActiveMQQueue("LaunchBillingQueue");
		Map<String, String> debutFacturationEvent = new HashMap<>();
		debutFacturationEvent.put("referenceContrat", referenceContrat);
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(debutFacturationEvent);

	}

	@Override
	public void sendContractResiliatedEvent(String referenceContrat, Integer numEC, boolean isResiliationPartiel) {
		LOGGER.info("Sending event: ContractResiliated");
		Map<String, Object> contratResiliationEvent = new HashMap<>();
		contratResiliationEvent.put("referenceContrat", referenceContrat);
		contratResiliationEvent.put("isResiliationPartiel", isResiliationPartiel);
		contratResiliationEvent.put("numEC", numEC);
		Destination destination = new ActiveMQQueue("ContractResiliatedQueue");
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(contratResiliationEvent);
	}

	@Override
	public void sendContractPartialResiliatedEvent(String referenceContratResilier, Integer numEC) {
		LOGGER.info("Sending event: ContractPartialResiliated");
		Map<String, Object> contratResiliationEvent = new HashMap<>();
		contratResiliationEvent.put("referenceContrat", referenceContratResilier);
		contratResiliationEvent.put("numEC", numEC);
		Destination destination = new ActiveMQQueue("ContractPartialResiliatedQueue");
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(contratResiliationEvent);

	}

	@Override
	public void sendContratMigratedEvent(String referenceContratMigre) {
		LOGGER.info("Sending event: ContratMigratedEvent");
		Map<String, Object> contratMigratedEvent = new HashMap<>();
		contratMigratedEvent.put("referenceContrat", referenceContratMigre);
		Destination destination = new ActiveMQQueue("ContratMigratedQueue");
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(contratMigratedEvent);

	}

	@Override
	public void sendContratMigratedBillingEvent(String referenceContratMigre) {
		LOGGER.info("Sending event: ContratMigratedBillingEvent");
		Map<String, Object> contratMigratedEvent = new HashMap<>();
		contratMigratedEvent.put("referenceContrat", referenceContratMigre);
		Destination destination = new ActiveMQQueue("ContratMigratedBillingQueue");
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(contratMigratedEvent);

	}

	@Override
	public void sendContratSuccessionBillingEvent(String referenceContratCede) {
		LOGGER.info("Sending event: ContratSuccessionBillingEvent");
		Map<String, Object> contratSuccessionEvent = new HashMap<>();
		contratSuccessionEvent.put("referenceContrat", referenceContratCede);
		Destination destination = new ActiveMQQueue("ContratSuccessionBillingQueue");
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(contratSuccessionEvent);
	}

	@Override
	public void sendContratSuccessionEvent(String referenceContratCede) {
		LOGGER.info("Sending event: ContratSuccessionEvent");
		Map<String, Object> contratSuccessionEvent = new HashMap<>();
		contratSuccessionEvent.put("referenceContrat", referenceContratCede);
		Destination destination = new ActiveMQQueue("ContratSuccessionQueue");
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(contratSuccessionEvent);
	}

	@Override
	public void sendContratMigrationLivredBillingEvent(String referenceContrat) {
		LOGGER.info("Sending event: sendContratMigrationBillingEvent");
		Map<String, Object> contratMigratedEvent = new HashMap<>();
		contratMigratedEvent.put("referenceContrat", referenceContrat);
		Destination destination = new ActiveMQQueue("MigrationLivredBillingQueue");
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(contratMigratedEvent);
	}

	@Override
	public void sendLunchSuccessionBillingEvent(String referenceContrat) {
		LOGGER.info("Sending event: sendLunchSuccessionBillingEvent");
		Map<String, Object> contratSuccessionEvent = new HashMap<>();
		contratSuccessionEvent.put("referenceContrat", referenceContrat);
		Destination destination = new ActiveMQQueue("LunchSuccessionBillingQueue");
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(contratSuccessionEvent);
	}

	@Override
	public void sendContractRenewalEvent(String referenceContrat) {

		LOGGER.info("Sending event: ContractRenewalEvent");
		Map<String, Object> contratrenewalEvent = new HashMap<>();
		contratrenewalEvent.put("referenceContrat", referenceContrat);
		Destination destination = new ActiveMQQueue("ContractRenewedQueue");
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(contratrenewalEvent);
	}

	@Override
	public void sendLunchRenewalBillingEvent(String referenceContrat) {
		LOGGER.info("Sending event: sendLunchRenewelBillingEvent");
		Map<String, Object> contratSuccessionEvent = new HashMap<>();
		contratSuccessionEvent.put("referenceContrat", referenceContrat);
		Destination destination = new ActiveMQQueue("LunchRenewalBillingQueue");
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(contratSuccessionEvent);
	}

	@Override
	public void sendContractDeliveredEvent(String referenceContrat) {
		LOGGER.info("Sending event: sendContratDeliveredEvent");
		Map<String, Object> contratDeliveredEvent = new HashMap<>();
		contratDeliveredEvent.put("referenceContrat", referenceContrat);
		Destination destination = new ActiveMQQueue("ContractDeliveredQueue");
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(contratDeliveredEvent);

	}

}
