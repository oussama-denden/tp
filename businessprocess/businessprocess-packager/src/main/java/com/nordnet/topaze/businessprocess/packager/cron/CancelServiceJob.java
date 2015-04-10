package com.nordnet.topaze.businessprocess.packager.cron;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nordnet.adminpackager.NotFoundException;
import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.topaze.businessprocess.packager.service.PackagerService;
import com.nordnet.topaze.businessprocess.packager.util.Constants;
import com.nordnet.topaze.client.rest.RestClientPackager;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Les package suspendus depuis plus de la periode definit et qui n'ont pas été réactivés seront résilier
 * définitivement.
 * 
 * @author Oussama Denden
 * 
 */
public class CancelServiceJob extends QuartzJobBean {

	/**
	 * Declaration du log.
	 */
	protected final static Logger LOGGER = Logger.getLogger(ServiceActiveJob.class);

	/**
	 * {@link PackagerService}.
	 */
	protected PackagerService packagerService;

	/**
	 * {@link RestClientPackager}.
	 */
	protected RestClientPackager restClientPackager;

	/**
	 * Alert service.
	 */
	@Autowired
	protected SendAlert sendAlert;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				LOGGER.info("Cron: Cancel Service");
				try {
					// appel vers brique Livraison Core pour chercher les services en cours d'activation.
					List<ElementLivraison> elementLivraisons = restClientPackager.getServicesSuspendu();

					for (ElementLivraison elementLivraison : elementLivraisons) {

						if (elementLivraison.getRetailerPackagerId() != null) {
							// appel vers packager pour verifier si un service est active
							try {
								if (packagerService.checkServiceSuspendu(elementLivraison)) {
									// Marker le BP comme livrer
									packagerService.cancelPackager(elementLivraison);
								}
							} catch (NotFoundException e) {
								LOGGER.error("Error occurs during call of CancelServiceJob.executeInternal()", e);
							}

						}
					}
				} catch (TopazeException e) {
					LOGGER.error("Error occurs during call of  CancelServiceJob.executeInternal()", e);
					try {
						sendAlert.send(System.getProperty(Constants.PRODUCT_ID),
								"Ereur lors de l'execution du cron : Cancel Service", e.getMessage(), e.getMessage());
					} catch (Exception ex) {
						LOGGER.error("fail to send alert", ex);
					}
				}
			}
		};

		new Thread(runnable).start();
	}

	/**
	 * @param packagerService
	 *            {@link PackagerService}.
	 */
	public void setPackagerService(PackagerService packagerService) {
		this.packagerService = packagerService;
	}

	/**
	 * @param restClientPackager
	 *            {@link RestClientPackager}.
	 */
	public void setRestClientPackager(RestClientPackager restClientPackager) {
		this.restClientPackager = restClientPackager;
	}

}
