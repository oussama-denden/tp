package com.nordnet.topaze.businessprocess.facture.jms;

import java.util.ArrayList;
import java.util.List;

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
import com.nordnet.topaze.logger.rest.RestClient;

/**
 * Cette Classe reste à l'écoute du activeMq pour effectuer le billing apres la reception de l'evenement "BRRecupere".
 * 
 * @author akram-moncer
 * 
 */
public class ReturnReturnedListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(ReturnReturnedListener.class);

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

		LOGGER.info("Reception de l'evenement: ReturnReturned");
		if (message instanceof MapMessage) {
			try {
				final MapMessage mapMessage = (MapMessage) message;
				String referenceContrat = mapMessage.getString("referenceContrat");
				Integer numEC = mapMessage.getInt("numEC");
				boolean isResiliationPartiel = mapMessage.getBoolean("isResiliationPartiel");
				ContratBillingInfo[] contratBillingInfos = null;
				if (isResiliationPartiel) {
					contratBillingInfos = restClientFacture.getContratBillingInformation(referenceContrat, numEC);
				} else {
					contratBillingInfos = restClientFacture.getContratBillingInformation(referenceContrat, 0);
				}

				List<ContratBillingInfo> contratBillingInfosResiliation = new ArrayList<>();
				for (ContratBillingInfo contratBillingInfo : contratBillingInfos) {
					if (contratBillingInfo.getPeriodicite() != null
							&& contratBillingInfo.getDateDerniereFacture() != null) {
						contratBillingInfosResiliation.add(contratBillingInfo);
					}

				}

				ContratBillingInfo[] contratBillingRes = new ContratBillingInfo[contratBillingInfosResiliation.size()];
				// calculer le dernier billing du bon retour selon la politique de resiliation.
				if (isResiliationPartiel) {
					factureService.calculeDernierBilling(contratBillingInfosResiliation.toArray(contratBillingRes),
							isResiliationPartiel, true, false, false);
				} else {
					factureService.calculeDernierBilling(contratBillingInfosResiliation.toArray(contratBillingRes),
							isResiliationPartiel, true, true, true);
				}
				restClientFacture.changerDateFactureResiliation(referenceContrat, numEC);
			} catch (JMSException e) {
				LOGGER.error("Error occurs during call of  ReturnReturnedListener.onMessage()", e);
			} catch (TopazeException e) {
				LOGGER.error("Error occurs during call of  ReturnReturnedListener.onMessage()", e);
			} catch (Exception e) {
				LOGGER.error("Error occurs during call of  ReturnReturnedListener.onMessage()", e);
			}
		}
	}
}
