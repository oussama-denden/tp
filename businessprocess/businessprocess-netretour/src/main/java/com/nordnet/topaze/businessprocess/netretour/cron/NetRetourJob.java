package com.nordnet.topaze.businessprocess.netretour.cron;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.topaze.businessprocess.netretour.service.NetRetourService;
import com.nordnet.topaze.businessprocess.netretour.util.Constants;
import com.nordnet.topaze.client.rest.RestClientNetRetour;
import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette classe est responsable de verifier si les sous {@link BonPreparation} de type bien sont bien retourne et apple
 * la brique livraison core pour le marquer comme retourne en cas de reponse positive.
 * 
 * @author akram-moncer
 * 
 */
public class NetRetourJob extends QuartzJobBean {

	/**
	 * Declaration du log.
	 */
	protected final static Logger LOGGER = Logger.getLogger(NetRetourJob.class);

	/**
	 * service contient tout le operation lie a NetRetour.
	 */
	protected NetRetourService netRetourService;

	/**
	 * Le client REST du module contrat.
	 */
	protected RestClientNetRetour restClientNetRetour;

	/**
	 * Alert service.
	 */
	@Autowired
	protected SendAlert sendAlert;

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {

		LOGGER.info("Cron : Is Goods Returned");
		Runnable runnable = new Runnable() {

			@Override
			public void run() {

				List<ElementLivraison> elementLivraisons;
				try {
					// appel vers brique Livraison Core pour chercher les Biens non recupere.
					elementLivraisons = restClientNetRetour.getBiensEnCoursRecuperation();
					for (ElementLivraison el : elementLivraisons) {
						if (netRetourService.checkBienRecupere(el)) {
							restClientNetRetour.markerRetourner(el.getReference());
						}
					}
				} catch (TopazeException e) {
					LOGGER.error("Error occurs during call of  NetRetourJob.executeInternal()", e);
					try {
						LOGGER.error("error occurs during call of cron Is biens retourner", e);
						sendAlert.send(System.getProperty(Constants.PRODUCT_ID),
								"Erreur lors de l'execution du cron : Is biens retourner", e.getMessage(),
								e.getMessage());
					} catch (Exception ex) {
						LOGGER.error("fail to send alert", ex);
					}
				}
			}
		};

		new Thread(runnable).start();
	}

	/**
	 * 
	 * @param netRetourService
	 *            {@link NetRetourService}
	 */
	public void setNetRetourService(NetRetourService netRetourService) {
		this.netRetourService = netRetourService;
	}

	/**
	 * @param restClientNetRetour
	 *            {@link RestClientNetRetour}.
	 */
	public void setRestClientNetRetour(RestClientNetRetour restClientNetRetour) {
		this.restClientNetRetour = restClientNetRetour;
	}

}
