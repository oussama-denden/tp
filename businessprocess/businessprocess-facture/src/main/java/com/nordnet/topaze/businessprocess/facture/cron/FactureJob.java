package com.nordnet.topaze.businessprocess.facture.cron;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nordnet.topaze.businessprocess.facture.service.FactureService;
import com.nordnet.topaze.client.rest.RestClientFacture;
import com.nordnet.topaze.client.rest.business.facturation.ContratBillingInfo;
import com.nordnet.topaze.exception.TopazeException;

/**
 * 
 * @author akram-moncer
 * 
 */
public class FactureJob extends QuartzJobBean {

	/**
	 * facturation Service.
	 */
	@Autowired
	protected FactureService factureService;

	/**
	 * Le client REST du module contrat.
	 */
	@Autowired
	protected RestClientFacture restClientFacture;

	/**
	 * Declaration du log.
	 */
	protected final static Logger LOGGER = Logger.getLogger(FactureJob.class);

	@Override
	protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
		// lance la creation du billing recurrent dans un thread separe.

		try {

			if (isJobRunning(ctx)) {
				LOGGER.info("Il y un job en cours d'execution.");
				return;
			}

			LOGGER.info("Cron: Billing Recurrent");
			/*
			 * appel vers brique contrat pour avoir les references des contrats valides.
			 */
			List<String> references = restClientFacture.getReferencesContratLivrer();
			ContratBillingInfo[] contratBillingInfos = null;
			for (String referenceContrat : references) {
				// appel vers billing recurrent.
				contratBillingInfos = restClientFacture.getContratBillingInformation(referenceContrat, 0);
				for (ContratBillingInfo contratBillingInfo : contratBillingInfos) {
					// si contrat de vente(periodicite null) donc pas de billing recurrent.
					if (contratBillingInfo.getPeriodicite() != null) {
						factureService.calculerBillingRecurrent(contratBillingInfo);
					}
				}

			}
		} catch (TopazeException e) {
			LOGGER.error("Error occurs during call of  FactureJob.executeInternal()", e);
		}
	}

	/**
	 * 
	 * @param factureService
	 *            methode factureService.
	 */
	public void setFactureService(FactureService factureService) {
		this.factureService = factureService;
	}

	/**
	 * @param restClientFacture
	 *            {@link RestClientFacture}.
	 */
	public void setRestClientFacture(RestClientFacture restClientFacture) {
		this.restClientFacture = restClientFacture;
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