package com.nordnet.topaze.livraison.test.mock;

import org.apache.log4j.Logger;

import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.jms.MigrationMessagesSender;

/**
 * Mock de la class {@link MigrationMessagesSender}.
 * 
 * @author Oussama Denden
 * 
 */
public class MigrationMessagesSenderMock implements MigrationMessagesSender {

	/**
	 * Declaration log.
	 */
	private final static Logger LOGGER = Logger.getLogger(MigrationMessagesSenderMock.class);

	@Override
	public void sendMigrationInitiatedEvent(BonPreparation bonMigration) {
		LOGGER.info("MigrationInitiated Event");

	}

	@Override
	public void sendSubDeliveryMigrationDeliveredEvent(BonPreparation sousBM) {
		LOGGER.info("SubDeliveryMigrationDelivered Event");

	}

	@Override
	public void sendSubDeliveryMigrationReturnedEvent(BonPreparation sousBM) {
		LOGGER.info("SubDeliveryMigrationReturned Event");

	}

	@Override
	public void sendMigrationDeliveredEvent(String referenceContrat) {
		LOGGER.info("MigrationDelivered Event");

	}

	@Override
	public void sendMigrationReturnedEvent(String referenceContrat) {
		LOGGER.info("MigrationReturned Event");

	}

	@Override
	public void sendPrepareMigrationEvent(BonPreparation bonMigration) {
		LOGGER.info("PrepareMigration Event");

	}

	@Override
	public void sendSubDeliveryPreparedEvent(BonPreparation bonMigration) {
		LOGGER.info("SubDeliveryPrepared Event");

	}

	@Override
	public void sendPrepareSuccessionEvent(BonPreparation bonPreparation) {
		LOGGER.info("PrepareSuccession Event");

	}

	@Override
	public void sendSubDeliverySuccessedEvent(String referenceBonPreparation) {
		LOGGER.info("SubDeliverySuccessed Event");

	}

	@Override
	public void sendDeliverySuccessedEvent(String referenceBonPreparation) {
		LOGGER.info("DeliverySuccessed Event");

	}

	@Override
	public void sendMigrationReturnedBillingEvent(String referenceBonMigration) {
		LOGGER.info("MigrationReturnedBilling Event");
	}

	@Override
	public void sendMigrationDelivredEvent(String referenceBonMigration) {
		LOGGER.info("MigrationDelivred Event");
	}

	@Override
	public void sendPrepareRenewalEvent(BonPreparation bonRenouvellement) {
		LOGGER.info("PrepareRenewalEvent Event");
	}

	@Override
	public void sendDeliveryRenewedEvent(String referenceBonRenouvellement) {
		LOGGER.info("DeliveryRenewed Event");
	}

	@Override
	public void sendSubDeliveryRenewedEvent(String referenceBonRenouvellement) {
		LOGGER.info("SubDeliveryRenewed Event");
	}

}
