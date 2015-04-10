package com.nordnet.topaze.contrat.cron;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.topaze.contrat.domain.Avenant;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.repository.AvenantRepository;
import com.nordnet.topaze.contrat.service.AvenantService;
import com.nordnet.topaze.contrat.service.ContratService;
import com.nordnet.topaze.contrat.service.MigrationService;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.contrat.util.Utils;
import com.nordnet.topaze.exception.TopazeException;

/**
 * cron responsable d'effectuer les cesison future.
 * 
 * @author akram-moncer
 * 
 */
public class CessionFuture extends QuartzJobBean {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(CessionFuture.class);

	/**
	 * {@link MigrationService}.
	 */
	private MigrationService migrationService;

	/**
	 * {@link ContratService}.
	 */
	private ContratService contratService;

	/**
	 * {@link AvenantRepository}.
	 */
	private AvenantService avenantService;

	/**
	 * Alert service.
	 */
	@Autowired
	SendAlert sendAlert;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		LOGGER.info("Cron: Cession future");
		List<Integer> idAvenants = avenantService.findReferenceAvenantAvecCessionActive();
		for (Integer id : idAvenants) {
			try {
				Avenant avenant = avenantService.findByID(id);
				if (Utils.compareDate(avenant.getPolitiqueCession().getDateAction(), PropertiesUtil.getInstance()
						.getDateDuJour().toDate()) == Constants.ZERO) {
					Contrat contrat = contratService.findByReference(avenant.getReferenceContrat());
					migrationService.cessionFuture(contrat);
				}
			} catch (IOException | TopazeException e) {
				LOGGER.error("Erreur dans le cron Cession future ", e);
				try {
					sendAlert.send(System.getProperty(Constants.PRODUCT_ID), "Erreur dans le cron Cession future ",
							"cause: " + e.getCause().getLocalizedMessage(), e.getMessage());
				} catch (Exception ex) {
					LOGGER.error("fail to send alert", ex);
				}
			}
		}
	}

	/**
	 * 
	 * @param migrationService
	 *            {@link MigrationService}.
	 */
	public void setMigrationService(MigrationService migrationService) {
		this.migrationService = migrationService;
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
	 * 
	 * @param avenantService
	 *            {@link AvenantService}.
	 */
	public void setAvenantService(AvenantService avenantService) {
		this.avenantService = avenantService;
	}

}
