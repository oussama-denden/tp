package com.nordnet.topaze.businessprocess.swap.cron;

import java.util.List;

import nordnet.client.ws.netequipment.faults.NotFoundFault;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nordnet.topaze.businessprocess.swap.service.SwapService;
import com.nordnet.topaze.client.rest.RestClientSwap;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cron pour traite les Biens de migration retourne.
 * 
 * @author Oussama Denden
 * 
 */
public class SwapRetourJob extends QuartzJobBean {

	/**
	 * Declaration du log.
	 */
	protected final static Logger LOGGER = Logger.getLogger(SwapRetourJob.class);

	/**
	 * service contient tout le operation lie a NetRetour.
	 */
	@Autowired
	protected SwapService swapService;

	/**
	 * Le client REST du module contrat.
	 */
	protected RestClientSwap restClientSwap;

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				LOGGER.info("Cron: Is Biens de migration retourné");
				List<ElementLivraison> elementLivraisons;
				try {
					// appel vers brique Livraison Core pour chercher les biens de migration en cours de retourné.
					elementLivraisons = restClientSwap.getElementEnCoursRetour();
					for (ElementLivraison elementLivraison : elementLivraisons) {
						try {
							// appel vers swap mock pour verifier si un bien de migration est retourné
							if (swapService.consulterEtatRetourEM(elementLivraison)) {
								// Marquer le em comme retourné
								restClientSwap.marquerEMRetourne(elementLivraison);
							}
						} catch (TopazeException | NumberFormatException e) {
							// Cause de non livraison.
							if (e instanceof NotFoundFault) {
								LOGGER.error(((NotFoundFault) e).getReason(), e);
							} else {
								LOGGER.error("Error occurs during call of  SwapRetourJob.executeInternal()", e);
							}
						}
					}
				} catch (TopazeException e) {
					LOGGER.error("Error occurs during call of  SwapRetourJob.executeInternal()", e);
				}
			}
		};

		new Thread(runnable).start();

	}

	/**
	 * 
	 * @param restClientSwap
	 *            {@link RestClientSwap}
	 */
	public void setRestClientSwap(RestClientSwap restClientSwap) {
		this.restClientSwap = restClientSwap;
	}

	/**
	 * 
	 * @param swapService
	 *            {@link SwapService }
	 */
	public void setSwapService(SwapService swapService) {
		this.swapService = swapService;
	}

}
