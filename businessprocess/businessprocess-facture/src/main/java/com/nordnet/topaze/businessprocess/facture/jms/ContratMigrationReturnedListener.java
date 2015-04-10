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
public class ContratMigrationReturnedListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(ContratMigrationReturnedListener.class);

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
			String referenceContrat = mapMessage.getString("referenceBM");
			ContratBillingInfo[] contratBillingInfosHistorise =
					restClientFacture.getContratBillingInformationHistorise(referenceContrat);
			// calcule de facturation pour remboursement
			factureService.calculeDernierBilligMigration(contratBillingInfosHistorise, false, true, false, false);

		} catch (JMSException e) {
			LOGGER.error("Error occurs during call of  ContratMigrationReturnedListener.onMessage()", e);
		} catch (TopazeException e) {
			LOGGER.error("Error occurs during call of  ContratMigrationReturnedListener.onMessage()", e);
		} catch (Exception e) {
			LOGGER.error("Error occurs during call of  ContratMigrationReturnedListener.onMessage()", e);
		}

	}
}
