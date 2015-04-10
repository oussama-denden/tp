package com.nordnet.topaze.contrat.cron;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.service.ContratService;
import com.nordnet.topaze.contrat.service.ResiliationService;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.contrat.util.Utils;
import com.nordnet.topaze.exception.TopazeException;

/**
 * cron qui se charge de traiter les resiliations differees.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class ResiliationDifferee extends QuartzJobBean {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(ResiliationDifferee.class);

	/**
	 * reference du service de resiliation du contrat.
	 */
	private ResiliationService resiliationService;

	/**
	 * contrat service {@link ContratService}.
	 */
	private ContratService contratService;

	/**
	 * Alert service.
	 */
	@Autowired
	SendAlert sendAlert;

	/**
	 * Set {@link ResiliationService}.
	 * 
	 * @param resiliationService
	 *            service de resiliation.
	 */
	public void setResiliationService(ResiliationService resiliationService) {
		this.resiliationService = resiliationService;
	}

	/**
	 * 
	 * @param contratService
	 *            {@link ContratService}.
	 */
	public void setContratService(ContratService contratService) {
		this.contratService = contratService;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("null")
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		try {

			if (isJobRunning(context)) {
				LOGGER.info("Il y un job en cours d'execution.");
				return;
			}

			LOGGER.info("Cron: Résiliation Differee");

			List<String> contratsEncours = null;
			contratsEncours =
					resiliationService.findContratsResiliationDifferee(PropertiesUtil.getInstance().getDateDuJour()
							.toString());

			for (String reference : contratsEncours) {
				boolean isEligible = false;
				Contrat contrat = null;
				try {
					contrat = contratService.getContratByReference(reference);

					isEligible =
							contrat != null ? Utils.compareDatePourresiliationDifferee(PropertiesUtil.getInstance()
									.getDateDuJourWithTime().toDate(), contrat.getPolitiqueResiliation()
									.getDateResiliation()) : false;

					if (isEligible) {

						resiliationService.resilierContrat(contrat.getReference(),
								mappingPolitiqueResiliationDomainToBusiness(contrat.getPolitiqueResiliation()),
								Constants.INTERNAL_USER, false, false, false, false, true, false);
					}
				} catch (TopazeException e) {
					LOGGER.error("error occurs during call of cron resiliation future differee", e);
					try {
						sendAlert.send(System.getProperty(Constants.PRODUCT_ID),
								"error occurs during call of cron resiliation future differee", "caused by "
										+ e.getCause().getLocalizedMessage(), e.getMessage());
					} catch (Exception ex) {
						LOGGER.error("fail to send alert", ex);
					}
				}
			}

		} catch (TopazeException e) {
			try {
				LOGGER.error("error occurs during call of cron resiliation future differee", e);
				sendAlert.send(System.getProperty(Constants.PRODUCT_ID),
						"error occurs during call of cron  resiliation differee", "caused by "
								+ e.getCause().getLocalizedMessage(), e.getMessage());
			} catch (Exception ex) {
				LOGGER.error("fail to send alert", ex);
			}
		}

	}

	/**
	 * Mapping politique resiliation domain to business.
	 * 
	 * @param politiqueResiliation
	 *            the politique resiliation
	 * @return the politique resiliation
	 */
	private static com.nordnet.topaze.contrat.business.PolitiqueResiliation mappingPolitiqueResiliationDomainToBusiness(
			com.nordnet.topaze.contrat.domain.PolitiqueResiliation politiqueResiliation) {
		com.nordnet.topaze.contrat.business.PolitiqueResiliation politiqueResiliationBus =
				new com.nordnet.topaze.contrat.business.PolitiqueResiliation();

		politiqueResiliationBus.setFraisResiliation(politiqueResiliation.isFraisResiliation());
		politiqueResiliationBus.setMontantRemboursement(politiqueResiliation.getMontantRemboursement());
		politiqueResiliationBus.setMontantResiliation(politiqueResiliation.getMontantResiliation());
		politiqueResiliationBus.setPenalite(politiqueResiliation.isPenalite());
		politiqueResiliationBus.setRemboursement(politiqueResiliation.isRemboursement());
		politiqueResiliationBus.setEnCascade(politiqueResiliation.isEnCascade());
		politiqueResiliationBus.setDateResiliation(politiqueResiliation.getDateResiliation().toString());
		politiqueResiliationBus.setTypeResiliation(politiqueResiliation.getTypeResiliation());
		return politiqueResiliationBus;
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
