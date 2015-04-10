package com.nordnet.topaze.businessprocess.facture.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.facture.service.FactureService;
import com.nordnet.topaze.client.rest.RestClientFacture;
import com.nordnet.topaze.client.rest.business.facturation.ContratBillingInfo;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Ecoute l'evenement 'ContratMigrated': qui est lance à facturation des frais associé a la migration.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */

public class ContratMigratedListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(ContratMigrationLivredListener.class);

	/**
	 * Autowiring du Mouvement Service.
	 */
	@Autowired
	private FactureService factureService;

	/**
	 * {@link RestClient}.
	 */
	@Autowired
	private RestClientFacture restClientFacture;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: ContratMigrated");
		try {
			final MapMessage mapMessage = (MapMessage) message;
			String referenceContrat = mapMessage.getString("referenceContrat");

			ContratBillingInfo[] contratBillingInfosHistorise =
					restClientFacture.getContratBillingInformationHistorise(referenceContrat);

			ContratBillingInfo[] contratBillingInfosNouveau =
					restClientFacture.getContratBillingInformation(referenceContrat, 0);

			/**
			 * BillingInfo pour la migration administrative.
			 */
			ContratBillingInfo[] contratBillingInfosMigrationAdministrative =
					restClientFacture.getContratMigrationAdministrativeBillingInformation(referenceContrat);

			if (contratBillingInfosNouveau.length == contratBillingInfosMigrationAdministrative.length) {
				/**
				 * migration purement administrative.
				 */
				factureService.calculeBillingMigrationAdministrative(contratBillingInfosHistorise,
						contratBillingInfosMigrationAdministrative);

			} else {
				/*
				 * Caclule de billing de migration.
				 */
				factureService.calculeBillingMigration(contratBillingInfosHistorise, contratBillingInfosNouveau);

				factureService.calculeDernierBilligMigration(contratBillingInfosHistorise, true, false, true, false);
			}

		} catch (JMSException e) {
			LOGGER.error("Error occurs during call of  ContratMigratedListener.onMessage()", e);
		} catch (TopazeException e) {
			LOGGER.error("Error occurs during call of  ContratMigratedListener.onMessage()", e);
		} catch (Exception e) {
			LOGGER.error("Error occurs during call of  ContratMigratedListener.onMessage()", e);
		}

	}

}
