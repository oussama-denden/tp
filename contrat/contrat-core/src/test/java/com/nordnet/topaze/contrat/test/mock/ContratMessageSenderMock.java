package com.nordnet.topaze.contrat.test.mock;

import java.util.Map;

import org.apache.log4j.Logger;

import com.nordnet.topaze.contrat.jms.ContratMessagesSender;

public class ContratMessageSenderMock implements ContratMessagesSender {

	private final static Logger LOGGER = Logger.getLogger(ContratMessageSenderMock.class);

	@Override
	public void sendContratValidatedEvent(Map<String, Object> contratValidatedInfo) {
		LOGGER.info("ContratValidated Event");
	}

	@Override
	public void sendContractResiliatedEvent(String referenceContrat, Integer numEC, boolean isResiliationPartiel) {
		LOGGER.info("ContractResiliated Event");
	}

	@Override
	public void sendContractPartialResiliatedEvent(String referenceContratResilier, Integer numEC) {
		LOGGER.info("ContractPartialResiliated Event");

	}

	@Override
	public void sendLaunchBillingEvent(String referenceContrat) {
		LOGGER.info("LaunchBilling Event");

	}

	@Override
	public void sendContratMigratedEvent(String referenceContratMigrer) {
		LOGGER.info("ContratMigrated Event");

	}

	@Override
	public void sendContratMigratedBillingEvent(String referenceContratMigre) {
		LOGGER.info("ContratMigratedBilling Event");

	}

	@Override
	public void sendContratSuccessionBillingEvent(String referenceContrat) {
		LOGGER.info("ContratSuccessionBilling Event");
	}

	@Override
	public void sendContratSuccessionEvent(String referenceContrat) {
		LOGGER.info("ContratSuccession Event");
	}

	@Override
	public void sendLunchSuccessionBillingEvent(String referenceContrat) {
		LOGGER.info("LunchSuccessionBilling Event");

	}

	@Override
	public void sendContratMigrationLivredBillingEvent(String referenceContrat) {
		LOGGER.info("ContratMigrationLivredBilling Event");

	}

	@Override
	public void sendContractRenewalEvent(String referenceContrat) {
		LOGGER.info("ContratMigrationLivredBilling Event");
	}

	@Override
	public void sendLunchRenewalBillingEvent(String referenceContrat) {
		LOGGER.info("LunchRenewelBillingEvent Event");
	}

	@Override
	public void sendControlVenteEvent(Map<String, Object> contratValideInfo) {
		LOGGER.info("ControlVenteEvent Event");

	}

	@Override
	public void sendContractDeliveredEvent(String referenceContrat) {
		LOGGER.info("ContractDelivered Event");

	}

}
