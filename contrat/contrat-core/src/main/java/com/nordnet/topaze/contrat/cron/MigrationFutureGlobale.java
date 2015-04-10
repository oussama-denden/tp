package com.nordnet.topaze.contrat.cron;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.topaze.contrat.domain.Avenant;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.service.AvenantService;
import com.nordnet.topaze.contrat.service.ContratService;
import com.nordnet.topaze.contrat.service.MigrationService;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.contrat.util.Utils;
import com.nordnet.topaze.exception.TopazeException;

/**
 * 
 * @author anisselmane.
 * 
 */
public class MigrationFutureGlobale extends QuartzJobBean {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(MigrationFutureGlobale.class);

	/**
	 * reference du service migration.
	 */
	private MigrationService migrationService;

	/**
	 * {@link AvenantService}.
	 */
	private AvenantService avenantService;

	/**
	 * {@link ContratService}.
	 */
	private ContratService contratService;

	/**
	 * Alert service.
	 */
	@Autowired
	SendAlert sendAlert;

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {

		LOGGER.info("Cron: Migration future globale");
		List<Integer> idAvenants = avenantService.findReferenceAvenantAvecMigrationActive();
		for (Integer id : idAvenants) {
			try {
				Avenant avenant = avenantService.findByID(id);
				if (Utils.compareDate(avenant.getPolitiqueMigration().getDateAction(), PropertiesUtil.getInstance()
						.getDateDuJour().toDate()) == Constants.ZERO) {
					try {
						Contrat contrat = contratService.findByReference(avenant.getReferenceContrat());
						migrationService.migrerContratFutur(contrat);
					} catch (TopazeException | IOException | CloneNotSupportedException | JSONException e) {
						LOGGER.error("error occurs during call of cron migration futur", e);
						try {
							sendAlert.send(System.getProperty(Constants.PRODUCT_ID),
									"error occurs during call of cron migration futur", "caused by "
											+ e.getCause().getLocalizedMessage(), e.getMessage());
						} catch (Exception ex) {
							LOGGER.error("fail to send alert", ex);
						}
					}

				}
			} catch (TopazeException e) {
				LOGGER.error("error occurs during call of cron changer moyen paiement", e);
				try {
					sendAlert.send(System.getProperty(Constants.PRODUCT_ID),
							"error occurs during call of cron changer moyen paiement", "caused by "
									+ e.getCause().getLocalizedMessage(), e.getMessage());
				} catch (Exception ex) {
					LOGGER.error("fail to send alert", ex);
				}
			}
		}
	}

	/**
	 * Sets the migration service.
	 * 
	 * @param migrationService
	 *            migration service
	 */
	public void setMigrationService(MigrationService migrationService) {
		this.migrationService = migrationService;
	}

	/**
	 * 
	 * @param avenantService
	 *            {@link #avenantService}.
	 */
	public void setAvenantService(AvenantService avenantService) {
		this.avenantService = avenantService;
	}

	/**
	 * 
	 * @param contratService
	 *            {@link #contratService}.
	 */
	public void setContratService(ContratService contratService) {
		this.contratService = contratService;
	}

}
