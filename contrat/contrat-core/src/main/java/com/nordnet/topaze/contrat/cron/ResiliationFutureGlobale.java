package com.nordnet.topaze.contrat.cron;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.service.ContratService;
import com.nordnet.topaze.contrat.service.ResiliationService;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.exception.TopazeException;

/**
 * 
 * @author akram-moncer
 * 
 */
public class ResiliationFutureGlobale extends QuartzJobBean {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(ResiliationFutureGlobale.class);

	/**
	 * reference du service de resiliation du contrat.
	 */
	private ResiliationService resiliationService;

	/**
	 * {@link ContratService}
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
	 *            {@link ContratService}
	 */
	public void setContratService(ContratService contratService) {
		this.contratService = contratService;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {

		LOGGER.info("Cron: Résiliation future globale");

		List<String> refrenceContratsEncours = null;
		try {
			refrenceContratsEncours =
					resiliationService.findReferenceContratsResiliationFuture(PropertiesUtil.getInstance()
							.getDateDuJour().toString());
			for (String reference : refrenceContratsEncours) {
				Contrat contrat = contratService.getContratByReference(reference);
				resiliationService.resilierContrat(contrat.getReference(),
						mappingPolitiqueResiliationDomainToBusiness(contrat.getPolitiqueResiliation()),
						Constants.INTERNAL_USER, false, false, false, false, true, false);
			}
		} catch (TopazeException e) {
			try {
				LOGGER.error("error occurs during call of cron resiliation future globale", e);
				sendAlert.send(System.getProperty(Constants.PRODUCT_ID),
						"error occurs during call of cron  resiliation future globale", "caused by "
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
}
