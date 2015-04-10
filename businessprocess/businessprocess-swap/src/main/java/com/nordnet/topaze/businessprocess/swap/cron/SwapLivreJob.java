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
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class SwapLivreJob extends QuartzJobBean {

	/**
	 * Declaration du log.
	 */
	protected final static Logger LOGGER = Logger.getLogger(SwapLivreJob.class);

	/**
	 * 
	 */
	@Autowired
	protected SwapService swapService;

	/**
	 * {@link RestClientSwap}.
	 */
	protected RestClientSwap restClientSwap;

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				LOGGER.info("Cron: Is Biens de migration Livre");
				List<ElementLivraison> elementLivraisons;
				try {
					// appel vers brique Livraison Core pour chercher les biens de migration en cours de livraison.
					elementLivraisons = restClientSwap.getElementMigrationEnCoursLivraison();
					for (ElementLivraison elementLivraison : elementLivraisons) {
						try {
							// appel vers swap mock pour verifier si un bien de migration est livre
							if (swapService.consulterEtatLivraisonEM(elementLivraison)) {
								// Marquer le em comme livre
								restClientSwap.marquerEMLivre(elementLivraison);
							}
						} catch (TopazeException | NumberFormatException e) {
							// Cause de non livraison.
							if (e instanceof NotFoundFault) {
								LOGGER.error(((NotFoundFault) e).getReason(), e);
							} else {
								LOGGER.error("Error occurs during call of  SwapLivreJob.executeInternal()", e);
							}
						}
					}
				} catch (TopazeException e) {
					LOGGER.error("Error occurs during call of  SwapLivreJob.executeInternal()", e);
				}
			}
		};

		new Thread(runnable).start();
	}

	/**
	 * 
	 * @param restClientSwap
	 *            {@link restClientSwap}
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
