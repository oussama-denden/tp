package com.nordnet.topaze.businessprocess.packager.cron;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nordnet.adminpackager.NotFoundException;
import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.topaze.businessprocess.packager.service.PackagerService;
import com.nordnet.topaze.businessprocess.packager.util.Constants;
import com.nordnet.topaze.client.rest.RestClientPackager;
import com.nordnet.topaze.client.rest.business.livraison.ElementContractuelInfo;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.client.rest.enums.OutilLivraison;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette classe est responsable verifier si les services sont activÃ©es chaque jours Ã  12 PM.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class ServiceActiveJob extends QuartzJobBean {

	/**
	 * Declaration du log.
	 */
	protected final static Logger LOGGER = Logger.getLogger(ServiceActiveJob.class);

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

		try {

			if (isJobRunning(context)) {
				LOGGER.info("Il y un job en cours d'execution.");
				return;
			}

			LOGGER.info("Cron: Is service active");
			// appel vers brique Livraison Core pour chercher les services en cours d'activation.
			List<ElementLivraison> elementLivraisons = restClientPackager.getServicesEnCoursActivation();
			List<ElementLivraison> elementLivraisonsOptionPlus = new ArrayList<ElementLivraison>();

			for (ElementLivraison elementLivraison : elementLivraisons) {

				if (elementLivraison.getRetailerPackagerId() != null) {
					// appel vers packager pour verifier si un service est active
					try {
						if (packagerService.checkServiceActive(elementLivraison)) {
							// Marker le BP comme livrer
							restClientPackager.marquerSousBPLivre(elementLivraison);
						}
					} catch (NotFoundException e) {
						LOGGER.error("Error occurs during call of  ServiceActiveJob.executeInternal()", e);
					}
				} else if (elementLivraison.getActeur().equals(OutilLivraison.PACKAGER)) {
					/*
					 * seul une option plus ou un produit SERVICE du meme genre qui est en cours d'activation sans avoir
					 * un code produit.
					 * 
					 * on les ajoute dans une autre liste pour laisser le temps au process de verifier les parents en
					 * premier.
					 */
					elementLivraisonsOptionPlus.add(elementLivraison);
				}

			}

			for (ElementLivraison elementLivraison : elementLivraisonsOptionPlus) {
				/*
				 * une option plus prendra le meme status (livrer/non livrer) que le parent.
				 */
				String referenceContrat =
						restClientPackager.getReferenceBonPreparationParent(elementLivraison.getReference(),
								elementLivraison.getReferenceProduit(), false);
				ElementContractuelInfo parentInfo =
						restClientPackager.getParentInfo(referenceContrat, elementLivraison.getNumEC());
				ElementLivraison elementLivraisonParent =
						restClientPackager.getElementLivraison(
								parentInfo.getReferenceContrat() + "-" + parentInfo.getNumEC(),
								parentInfo.getReferenceProduit());
				if (!elementLivraisonParent.isNonLivrer()) {
					restClientPackager.marquerSousBPLivre(elementLivraison);
				} else {
					restClientPackager.marquerNonLivre(elementLivraison.getReference(),
							elementLivraisonParent.getCauseNonlivraison());
				}
			}
		} catch (TopazeException e) {
			LOGGER.error("Error occurs during call of  ServiceActiveJob.executeInternal()", e);
			try {
				LOGGER.error("error occurs during call of cron Is service active", e);
				sendAlert.send(System.getProperty(Constants.PRODUCT_ID),
						"Ereur lors de l'execution du cron : Is service active", e.getMessage(), e.getMessage());
			} catch (Exception ex) {
				LOGGER.error("fail to send alert", ex);
			}
		}
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

	/**
	 * verifier si il ya un cron en cours d'execution.
	 * 
	 * @param ctx
	 *            {@link JobExecutionException}.
	 * @return true s'il ya un cron encore en cours d'execution.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@SuppressWarnings("unchecked")
	private boolean isJobRunning(JobExecutionContext ctx) throws TopazeException {
		try {
			List<JobExecutionContext> jobs = ctx.getScheduler().getCurrentlyExecutingJobs();

			for (JobExecutionContext job : jobs) {
				// verifier s'il ya un job en cours d'execution (il faut etre sure que le temps de lancement n'est pas
				// le meme pour etre sur qu'il ne s'agit pas de la meme instance du job).
				if (job.getJobDetail().getJobClass().getName().equals(this.getClass().getName())
						&& !job.getFireTime().equals(ctx.getFireTime())) {
					return true;
				}
			}

			return false;
		} catch (SchedulerException e) {
			throw new TopazeException("Exception genere par le Quartz", e);
		}
	}

}
