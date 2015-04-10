package com.nordnet.topaze.contrat.cron;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.topaze.contrat.business.PolitiqueResiliation;
import com.nordnet.topaze.contrat.domain.Avenant;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.domain.Modification;
import com.nordnet.topaze.contrat.domain.MotifResiliation;
import com.nordnet.topaze.contrat.domain.TypeContrat;
import com.nordnet.topaze.contrat.domain.TypeResiliation;
import com.nordnet.topaze.contrat.repository.AvenantRepository;
import com.nordnet.topaze.contrat.service.AvenantService;
import com.nordnet.topaze.contrat.service.ContratService;
import com.nordnet.topaze.contrat.service.MigrationService;
import com.nordnet.topaze.contrat.service.ResiliationService;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.contrat.util.Utils;
import com.nordnet.topaze.exception.TopazeException;

/**
 * 
 * @author akram-moncer
 * 
 */
public class ContratJob extends QuartzJobBean {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(ContratJob.class);

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
	 * reference du service de resiliation du contrat.
	 */
	private ResiliationService resiliationService;

	/**
	 * Alert service.
	 */
	@Autowired
	SendAlert sendAlert;

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		LOGGER.info("Cron: resiliation automatique");
		List<String> referenceContratsEncours = contratService.findReferenceContratsGlobalValider();
		for (String reference : referenceContratsEncours) {

			Date dateJour;
			try {
				Contrat contrat = contratService.getContratByReference(reference);
				Date dateFinContrat = contrat.getMaxDateFinDuree();

				dateJour = PropertiesUtil.getInstance().getDateDuJour().toDate();

				if (dateFinContrat != null && Utils.compareDate(dateJour, dateFinContrat) >= Constants.ZERO) {
					// creer le politique de resiliation
					PolitiqueResiliation politiqueResiliation = createPolitiqueResiliation();

					// tester s'il ya un renouvellement future
					Avenant avenant = avenantService.findAvenantAvecRenouvellementActive(contrat.getReference());

					if (avenant == null) {
						// resilaiation du contrat
						resiliationService.resilierContrat(contrat.getReference(), politiqueResiliation,
								Constants.INTERNAL_USER, false, false, false, false, false, false);
					}

					else {
						// tester si tous les elements sont prevus d'etre renouvele
						List<ElementContractuel> elementContractuels = getTheUnReneweledElement(contrat, avenant);

						if (elementContractuels.size() == 0) {
							migrationService.renouvelerContratFutur(contrat);
						} else {
							for (ElementContractuel elementContractuel : elementContractuels) {
								resiliationService.resiliationPartiel(contrat.getReference(),
										elementContractuel.getNumEC(), politiqueResiliation, Constants.INTERNAL_USER,
										true, false);
							}
							migrationService.renouvelerContratFutur(contrat);
						}
					}

				}
			} catch (TopazeException | IOException | CloneNotSupportedException | JSONException e) {
				LOGGER.error("erreur dans la resiliation automatique", e);
				try {
					sendAlert.send(System.getProperty(Constants.PRODUCT_ID),
							"Erreur dans le cron resilation automatique ", "cause: "
									+ e.getCause().getLocalizedMessage(), e.getMessage());
				} catch (Exception ex) {
					LOGGER.error("fail to send alert", ex);
				}
			}
		}
	}

	/**
	 * creation de politique de resiliation.
	 * 
	 * @return {@link PolitiqueResiliation}
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	private static PolitiqueResiliation createPolitiqueResiliation() throws TopazeException {
		PolitiqueResiliation politiqueResiliation = new PolitiqueResiliation();
		politiqueResiliation.setPenalite(true);
		politiqueResiliation.setFraisResiliation(true);
		politiqueResiliation.setMontantResiliation(null);
		politiqueResiliation.setRemboursement(true);
		politiqueResiliation.setMontantRemboursement(null);
		politiqueResiliation.setCommentaire("Résiliation automatique");
		politiqueResiliation.setTypeResiliation(TypeResiliation.RIC);
		politiqueResiliation.setMotif(MotifResiliation.DEMANDE_DE_NON_RENOUVELLEMENT);
		politiqueResiliation.setDateResiliation(PropertiesUtil.getInstance().getDateDuJour().toString());
		politiqueResiliation.setDelaiDeSecurite(false);
		politiqueResiliation.setRemboursement(false);
		return politiqueResiliation;

	}

	/**
	 * Obtenir les element contractuel non renouvele.
	 * 
	 * @param contrat
	 *            {@link Contrat}
	 * @param avenant
	 *            {@link Avenant}
	 * @return liste de {@link ElementContractuel}
	 */
	private static List<ElementContractuel> getTheUnReneweledElement(Contrat contrat, Avenant avenant) {
		List<Integer> numECs = new ArrayList<>();
		List<ElementContractuel> elementContractuelsNonRenouveles = new ArrayList<>();
		for (Modification modification : avenant.getModifContrat()) {
			numECs.add(modification.getNumEC());
		}
		for (ElementContractuel elementContractuel : contrat.getSousContrats()) {
			if (!numECs.contains(elementContractuel.getNumEC()) && !elementContractuel.isResilier()
					&& !elementContractuel.getTypeContrat().equals(TypeContrat.VENTE)) {
				elementContractuelsNonRenouveles.add(elementContractuel);
			}
		}
		return elementContractuelsNonRenouveles;

	}

	/* Getters and Setters */

	/**
	 * Set service de resiliation.
	 * 
	 * @param resiliationService
	 *            {@link ResiliationService}.
	 */
	public void setResiliationService(ResiliationService resiliationService) {
		this.resiliationService = resiliationService;
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
