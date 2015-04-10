package com.nordnet.topaze.businessprocess.core.cron;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.topaze.businessprocess.core.util.Constants;
import com.nordnet.topaze.client.rest.RestClientLivraison;
import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.logger.rest.RestClient;

/**
 * Cette classe est responsable verifier si les services sont activÃ©es chaque jours Ã  12 PM.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class BRGlobalRecupereJob extends QuartzJobBean {

	/**
	 * Declaration du log.
	 */
	protected final static Logger LOGGER = Logger.getLogger(BRGlobalRecupereJob.class);

	/**
	 * {@link RestClient}.
	 */
	protected RestClientLivraison restClientLivraison;

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

			LOGGER.info("Cron: Is BR Global recupere");

			// appel vers brique Livraison Core pour chercher les br global non recuperer.
			List<BonPreparation> bonPreparations = restClientLivraison.getBRGlobalEncoursRecuperation();
			for (BonPreparation bonPreparation : bonPreparations) {
				boolean isRecupere = true;
				for (ElementLivraison elementLivraison : bonPreparation.getElementLivraisons()) {
					if (!elementLivraison.isTermine()) {
						isRecupere = false;
					}
				}
				if (isRecupere) {
					// marquer BR Global comme recupere.
					restClientLivraison.marquerRecupere(bonPreparation.getReference());
				}

			}

		} catch (TopazeException e) {
			LOGGER.error("Error occurs during call of  BRGlobalRecupereJob.executeInternal()", e);
			try {
				LOGGER.error("error occurs during call of cron BR Global recupere", e);
				sendAlert.send(System.getProperty(Constants.PRODUCT_ID),
						"Ereur lors de l'execution du cron: Is BR Global recupere", e.getMessage(), e.getMessage());
			} catch (Exception ex) {
				LOGGER.error("fail to send alert", ex);
			}
		}
	}

	public void setRestClientLivraison(RestClientLivraison restClientLivraison) {
		this.restClientLivraison = restClientLivraison;
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
