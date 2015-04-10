package com.nordnet.topaze.businessprocess.netdelivery.cron;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nordnet.net_delivery.ConverterException;
import com.nordnet.net_delivery.NetDeliveryException;
import com.nordnet.net_delivery.NotFoundException;
import com.nordnet.net_delivery.NullException;
import com.nordnet.topaze.businessprocess.netdelivery.service.NetDeliveryService;
import com.nordnet.topaze.client.rest.RestClientNetDelivery;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette classe est responsable verifier si les biens sont livrees chaque jours Ã  12 PM.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class NetDeliveryJob extends QuartzJobBean {

	/**
	 * Declaration du log.
	 */
	protected final static Logger LOGGER = Logger.getLogger(NetDeliveryJob.class);

	/**
	 * {@link NetDeliveryService}.
	 */
	@Autowired
	protected NetDeliveryService netDeliveryService;

	/**
	 * {@link RestClientNetDelivery}.
	 */
	protected RestClientNetDelivery restClientNetDelivery;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				LOGGER.info("Cron: Is Goods Delivered");
				List<ElementLivraison> elementLivraisons;
				try {
					// appel vers brique Livraison Core pour chercher les biens en cours de livraison.
					elementLivraisons = restClientNetDelivery.getBiensEnCoursLivraison();
					for (ElementLivraison elementLivraison : elementLivraisons) {
						try {
							// appel vers netdelivery pour verifier si un bien est livre
							if (netDeliveryService.checkBienLivre(elementLivraison)) {
								// Marquer le BP comme livrer
								restClientNetDelivery.marquerSousBPLivre(elementLivraison);
							}
						} catch (TopazeException | NumberFormatException | NotFoundException | NetDeliveryException
								| NullException | ConverterException e) {
							LOGGER.error("Error occurs during call of  NetDeliveryJob.executeInternal()", e);
							final String causeNonLivraison = e.getMessage();
							// marquer le BP comme non livre
							restClientNetDelivery.marquerNonLivre(elementLivraison.getReference(), causeNonLivraison);
							LOGGER.error(e.getMessage());
							// marquer le BP comme non livre
							restClientNetDelivery.marquerNonLivre(elementLivraison.getReference(), e.getMessage());
						}
					}
				} catch (TopazeException e) {
					LOGGER.error("Error occurs during call of  NetDeliveryJob.executeInternal()", e);
				}
			}
		};

		new Thread(runnable).start();
	}

	/**
	 * @param netDeliveryService
	 *            {@link NetDeliveryService}.
	 */
	public void setNetDeliveryService(
			com.nordnet.topaze.businessprocess.netdelivery.service.NetDeliveryService netDeliveryService) {
		this.netDeliveryService = netDeliveryService;
	}

	/**
	 * @param restClientNetDelivery
	 *            {@link RestClientNetDelivery}.
	 */
	public void setRestClientNetDelivery(RestClientNetDelivery restClientNetDelivery) {
		this.restClientNetDelivery = restClientNetDelivery;
	}

}
