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
 * Implementation de l'interface {@link MigrationMessagesSender}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Component("migrationMessagesSender")
public class MigrationMessagesSenderImpl implements MigrationMessagesSender {

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
	public void sendMigrationInitiatedEvent(BonPreparation bonMigration) {
		LOGGER.info("Sending event: MigrationInitiated");
		Destination destination = new ActiveMQQueue("MigrationInitiatedQueue");
		Map<String, String> bmInitieEvent = new HashMap<>();
		bmInitieEvent.put("referenceBM", bonMigration.getReference());
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(bmInitieEvent);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendSubDeliveryMigrationDeliveredEvent(BonPreparation bmGlobal) {
		LOGGER.info("Sending event: SubDeliveryMigrationDelivered");
		Destination destination = new ActiveMQQueue("SubDeliveryMigrationDeliveredQueue");
		Map<String, String> sousBMLivreEvent = new HashMap<>();
		sousBMLivreEvent.put("referenceBM", bmGlobal.getReference());
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(sousBMLivreEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendSubDeliveryMigrationReturnedEvent(BonPreparation bmGlobal) {
		LOGGER.info("Sending event: SubDeliveryMigrationReturned");
		Destination destination = new ActiveMQQueue("SubDeliveryMigrationReturnedQueue");
		Map<String, String> sousBMLivreEvent = new HashMap<>();
		sousBMLivreEvent.put("referenceBM", bmGlobal.getReference());
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(sousBMLivreEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMigrationDeliveredEvent(String referenceContrat) {
		LOGGER.info("Sending event: MigrationDelivered");
		Destination destination = new ActiveMQQueue("MigrationDeliveredQueue");
		Map<String, String> bmGlobalLivreEvent = new HashMap<>();
		bmGlobalLivreEvent.put("referenceContrat", referenceContrat);
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(bmGlobalLivreEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMigrationReturnedEvent(String referenceContrat) {
		LOGGER.info("Sending event: MigrationReturned");
		Destination destination = new ActiveMQQueue("MigrationReturnedQueue");
		Map<String, String> bmGlobaRetourneEvent = new HashMap<>();
		bmGlobaRetourneEvent.put("referenceContrat", referenceContrat);
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(bmGlobaRetourneEvent);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendPrepareMigrationEvent(BonPreparation bonMigration) {
		LOGGER.info("Sending events: PrepareEquipementDelivery, PrepareEquipementReturn, PrepareServiceMigration, PrepareSawapping");
		Destination destination =
				new ActiveMQQueue(
						"PrepareEquipementDeliveryQueue, PrepareEquipementReturnQueue, PrepareServiceMigrationQueue, PrepareSawappingQueue");
		Map<String, String> prepareMigrationEvent = new HashMap<>();
		prepareMigrationEvent.put("referenceBP", bonMigration.getReference());
		prepareMigrationEvent.put("typeBonPreparation", bonMigration.getTypeBonPreparation().toString());
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(prepareMigrationEvent);

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

	@Override
	public void sendPrepareSuccessionEvent(BonPreparation bonPreparation) {
		LOGGER.info("Sending events: PrepareEquipementSuccession, PrepareServiceSuccession");
		Destination destination = new ActiveMQQueue("PrepareEquipementSuccessionQueue, PrepareServiceSuccessionQueue");
		Map<String, String> prepareSuccessionEvent = new HashMap<>();
		prepareSuccessionEvent.put("referenceBP", bonPreparation.getReference());
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(prepareSuccessionEvent);
	}

	@Override
	public void sendSubDeliverySuccessedEvent(String referenceBonPreparation) {
		LOGGER.info("Sending event: SubDeliverySuccessed");
		Destination destination = new ActiveMQQueue("SubDeliverySuccessedQueue");
		Map<String, String> elCedeEvent = new HashMap<>();
		elCedeEvent.put("referenceBP", referenceBonPreparation);
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(elCedeEvent);
	}

	@Override
	public void sendDeliverySuccessedEvent(String referenceBonPreparation) {
		LOGGER.info("Sending event: DeliverySuccessed");
		Destination destination = new ActiveMQQueue("DeliverySuccessedQueue");
		Map<String, String> elCedeEvent = new HashMap<>();
		elCedeEvent.put("referenceBP", referenceBonPreparation);
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(elCedeEvent);
	}

	@Override
	public void sendMigrationDelivredEvent(String referenceBonMigration) {
		LOGGER.info("Sending event: MigrationTerminated");
		Destination destination = new ActiveMQQueue("MigrationDelivredQueue");
		Map<String, String> bmGlobatermineEvent = new HashMap<>();
		bmGlobatermineEvent.put("referenceBM", referenceBonMigration);
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(bmGlobatermineEvent);
	}

	@Override
	public void sendMigrationReturnedBillingEvent(String referenceBonMigration) {
		LOGGER.info("Sending event: MigrationReturnedBilling");
		Destination destination = new ActiveMQQueue("MigrationReturnedBillingQueue");
		Map<String, String> bmGlobalReturnedEvent = new HashMap<>();
		bmGlobalReturnedEvent.put("referenceBM", referenceBonMigration);
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(bmGlobalReturnedEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendPrepareRenewalEvent(BonPreparation bonRenouvellement) {
		LOGGER.info("Sending events:PrepareServiceRenewal");
		Destination destination = new ActiveMQQueue("PrepareServiceRenewalQueue,PrepareEquipementRenewalQueue");
		Map<String, String> prepareServiceRenewal = new HashMap<>();
		prepareServiceRenewal.put("referenceBP", bonRenouvellement.getReference());
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(prepareServiceRenewal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendDeliveryRenewedEvent(String referenceBonRenouvellement) {

		LOGGER.info("Sending event: DeliveryRenewed");
		Destination destination = new ActiveMQQueue("DeliveryRenewedQueue");
		Map<String, String> elCedeEvent = new HashMap<>();
		elCedeEvent.put("referenceBP", referenceBonRenouvellement);
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(elCedeEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendSubDeliveryRenewedEvent(String referenceBonRenouvellement) {
		LOGGER.info("Sending event: SubDeliveryRenewed");
		Destination destination = new ActiveMQQueue("SubDeliveryRenewedQueue");
		Map<String, String> elCedeEvent = new HashMap<>();
		elCedeEvent.put("referenceBP", referenceBonRenouvellement);
		jmsTemplate.setDefaultDestination(destination);
		jmsTemplate.convertAndSend(elCedeEvent);
	}

}
