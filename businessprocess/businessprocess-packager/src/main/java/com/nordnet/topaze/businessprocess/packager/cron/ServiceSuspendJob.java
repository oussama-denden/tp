package com.nordnet.topaze.businessprocess.packager.cron;

import java.util.ArrayList;
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
import com.nordnet.topaze.client.rest.business.livraison.ElementContractuelInfo;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette classe est responsable verifier si les services sont activÃ©es chaque jours Ã  12 PM.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class ServiceSuspendJob extends QuartzJobBean {

	/**
	 * Declaration du log.
	 */
	protected final static Logger LOGGER = Logger.getLogger(ServiceSuspendJob.class);

	/**
	 * {@link NetDeliveryService}.
	 */
	@Autowired
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
				LOGGER.info("Cron: Is service suspendu");
				try {
					// appel vers brique Livraison Core pour chercher les services en cours de suspension.
					List<ElementLivraison> elementRetours = restClientPackager.getServicesEnCoursSuspension();
					List<ElementLivraison> elementLivraisonsOptionPlus = new ArrayList<>();
					for (ElementLivraison elementRetour : elementRetours) {
						/*
						 * appel vers packager pour verifier si un service est suspendu un element Service qui n'a pas
						 * de code produit c'est qu'il est une option plus.
						 */
						try {
							if (elementRetour.getRetailerPackagerId() != null
									&& packagerService.checkServiceSuspendu(elementRetour)) {
								// Marker le BR comme retourner
								restClientPackager.marquerSuspendu(elementRetour.getReference());
							} else if (elementRetour.getRetailerPackagerId() == null) {

								/*
								 * on les ajoute dans une autre liste pour laisser le temps au process de verifier les
								 * parents en premier.
								 */
								elementLivraisonsOptionPlus.add(elementRetour);

							}
						} catch (NotFoundException e) {
							LOGGER.error("Error occurs during call of  ServiceSuspendJob.executeInternal()", e);
						}

					}

					for (ElementLivraison elementRetour : elementLivraisonsOptionPlus) {
						/*
						 * on marque une option plus comme suspendu, lorsque son parent est suspendu aussi (cas de
						 * resiliation global ou en cascade).
						 */
						String referenceContrat =
								restClientPackager.getReferenceBonPreparationParent(elementRetour.getReference(),
										elementRetour.getReferenceProduit(), true);
						ElementContractuelInfo parentInfo =
								restClientPackager.getParentInfo(referenceContrat, elementRetour.getNumEC());

						ElementLivraison elementRetourParent =
								restClientPackager.getElementRecuperation(parentInfo.getReferenceContrat() + "-"
										+ parentInfo.getNumEC());
						if (elementRetourParent != null && elementRetourParent.isTermine()) {
							/*
							 * si le parent est retourner alors l'option plus doit etre marquer comme retourner aussi.
							 */
							restClientPackager.marquerSuspendu(elementRetour.getReference());
						}
					}
				} catch (TopazeException e) {
					LOGGER.error("Error occurs during call of  ServiceSuspendJob.executeInternal()", e);
					try {
						LOGGER.error("error occurs during call of cron service suspendu", e);
						sendAlert.send(System.getProperty(Constants.PRODUCT_ID),
								"Ereur lors de l'execution du cron : Is service suspendu", e.getMessage(),
								e.getMessage());
					} catch (Exception ex) {
						LOGGER.error("fail to send alert", ex);
					}
					LOGGER.error(e.getMessage());
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
