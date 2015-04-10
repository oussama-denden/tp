package com.nordnet.topaze.businessprocess.facture.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.facture.service.FactureService;
import com.nordnet.topaze.client.rest.RestClientFacture;
import com.nordnet.topaze.client.rest.RestClientNetDelivery;
import com.nordnet.topaze.client.rest.business.facturation.ContratBillingInfo;
import com.nordnet.topaze.client.rest.business.facturation.Frais;
import com.nordnet.topaze.client.rest.enums.TypeFrais;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette Classe reste à l'écoute du activeMq pour effectuer le billing apres la reception d'un evenement
 * "DebutFacturation" Event.
 * 
 * @author akram-moncer
 * 
 */
public class LaunchBillingListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(LaunchBillingListener.class);

	/**
	 * Autowiring du Mouvement Service.
	 */
	@Autowired
	private FactureService factureService;

	/**
	 * {@link RestClientNetDelivery}.
	 */
	@Autowired
	private RestClientFacture restClientFacture;

	@Override
	public void onMessage(Message message) {

		LOGGER.info("Reception de l'evenement: LaunchBilling");
		if (message instanceof MapMessage) {
			try {
				final MapMessage mapMessage = (MapMessage) message;
				String referenceContrat = mapMessage.getString("referenceContrat");
				ContratBillingInfo[] contratBillingInfos =
						restClientFacture.getContratBillingInformation(referenceContrat, 0);
				/**
				 * calculer le premier billing du bon livrer.
				 */
				for (ContratBillingInfo contratBillingInfo : contratBillingInfos) {
					if (contratBillingInfo.getPolitiqueResiliation() == null) {
						if (contratBillingInfo.getPolitiqueValidation() != null
								&& !contratBillingInfo.getPolitiqueValidation().isFraisCreation()) {
							for (Frais frais : contratBillingInfo.getFrais()) {
								if (frais.getTypeFrais().equals(TypeFrais.CREATION)) {
									frais.setMontant(0.0);
								}
							}
						}
						factureService.calculerPremierBilling(contratBillingInfo, false);
						// si contrat de vente(periodicite null) donc pas de billing recurrent.
						if (contratBillingInfo.getPeriodicite() != null) {
							factureService.calculerBillingRecurrent(contratBillingInfo);
						}
					}
				}

			} catch (JMSException e) {
				LOGGER.error("Error occurs during call of  LaunchBillingListener.onMessage()", e);
			} catch (TopazeException e) {
				LOGGER.error("Error occurs during call of  LaunchBillingListener.onMessage()", e);
			} catch (Exception e) {
				LOGGER.error("Error occurs during call of  LaunchBillingListener.onMessage()", e);
			}
		}
	}
}
