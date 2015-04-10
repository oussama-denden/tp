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
 * Ecoute l'evenement 'contratMigrationFinished': qui est lance à la fin de migration d'un contrat.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class ContratMigrationLivredListener implements MessageListener {

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
		LOGGER.info("Reception de l'evenement: ContratMigrationFinished");
		try {
			final MapMessage mapMessage = (MapMessage) message;
			String referenceContrat = mapMessage.getString("referenceContrat");

			ContratBillingInfo[] contratBillingInfosNouveau =
					restClientFacture.getContratBillingInformation(referenceContrat, 0);

			// calcule de facturation pour premier billing , prorata et billing recurrent
			for (ContratBillingInfo contratBillingInfo : contratBillingInfosNouveau) {

				factureService.calculerPremierBilling(contratBillingInfo, true);

				// si contrat de vente(periodicite null) donc pas de billing recurrent.
				if (contratBillingInfo.getPeriodicite() != null) {
					factureService.calculerBillingRecurrent(contratBillingInfo);
				}
			}

		} catch (JMSException e) {
			LOGGER.error("Error occurs during call of  ContratMigrationLivredListener.onMessage()", e);
		} catch (TopazeException e) {
			LOGGER.error("Error occurs during call of  ContratMigrationLivredListener.onMessage()", e);
		} catch (Exception e) {
			LOGGER.error("Error occurs during call of  ContratMigrationLivredListener.onMessage()", e);
		}

	}
}
