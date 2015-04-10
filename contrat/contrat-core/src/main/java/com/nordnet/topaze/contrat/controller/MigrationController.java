package com.nordnet.topaze.contrat.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.topaze.contrat.business.AnnulationInfo;
import com.nordnet.topaze.contrat.business.ClientInfo;
import com.nordnet.topaze.contrat.business.ContratCession;
import com.nordnet.topaze.contrat.business.ContratMigrationInfo;
import com.nordnet.topaze.contrat.business.ContratMigrationSimulationInfo;
import com.nordnet.topaze.contrat.business.ContratRenouvellementInfo;
import com.nordnet.topaze.contrat.business.ContratResiliationtInfo;
import com.nordnet.topaze.contrat.business.ProduitRenouvellement;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.repository.ContratHistoriqueRepository;
import com.nordnet.topaze.contrat.repository.ContratRepository;
import com.nordnet.topaze.contrat.service.ContratService;
import com.nordnet.topaze.contrat.service.MigrationService;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.exception.InfoErreur;
import com.nordnet.topaze.exception.TopazeException;
import com.wordnik.swagger.annotations.Api;

/**
 * Gerer l'ensemble des requetes qui ont en rapport avec la migration du contrats.
 * 
 * @author anisselmane.
 * 
 */
@Api(value = "migration", description = "migration")
@Controller
@RequestMapping("/contrat")
public class MigrationController {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(MigrationController.class);

	/**
	 * The contrat service. {@link MigrationService}.
	 */

	@Autowired
	private MigrationService migrationService;

	@Autowired
	private ContratService contratService;

	@Autowired
	private ContratRepository contratRepository;

	@Autowired
	private ContratHistoriqueRepository contratHistoriqueRepository;

	/**
	 * Alert service.
	 */
	@Autowired
	SendAlert sendAlert;

	/**
	 * Migrer un contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param contratResiliationtInfo
	 *            the contrat resiliationt info {@link ContratResiliationtInfo}.
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 * @throws JsonProcessingException
	 * @throws CloneNotSupportedException
	 * @throws JSONException
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/migrer", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public void migrerContrat(@PathVariable String referenceContrat,
			@RequestBody ContratMigrationInfo contratMigrationInfo)
			throws TopazeException, JsonProcessingException, CloneNotSupportedException, JSONException {
		LOGGER.info(":::ws-rec:::migrer");
		migrationService.migrerContrat(referenceContrat, contratMigrationInfo);

	}

	/**
	 * annuler une migration.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param migrationAnnulationInfo
	 *            the migration annulation info {@link ContratResiliationtInfo}.
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/migrer/annuler", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public void annulerMigration(@PathVariable String referenceContrat,
			@RequestBody AnnulationInfo migrationAnnulationInfo) throws TopazeException, JsonProcessingException {
		LOGGER.info(":::ws-rec:::annuler migration");
		migrationService.annulerMigration(referenceContrat, migrationAnnulationInfo);

	}

	/**
	 * ceder un contrat à un autre client.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param contratCession
	 *            {@link ContratCession}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws JsonProcessingException
	 *             {@link JsonProcessingException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/ceder", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public void cederContrat(@PathVariable String referenceContrat, @RequestBody ContratCession contratCession)
			throws TopazeException, JsonProcessingException {
		LOGGER.info(":::ws-rec:::ceder contrat");
		migrationService.cederContrat(referenceContrat, contratCession);

	}

	/**
	 * annuler une demande de cession.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param annulationCessionInfo
	 *            {@link AnnulationInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/ceder/annuler", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public void annulerCession(@PathVariable String referenceContrat, @RequestBody AnnulationInfo annulationCessionInfo)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::annuler cession");
		migrationService.annulerCession(referenceContrat, annulationCessionInfo);
	}

	/**
	 * recupere les informations de l'ancient contrat changés lors de la cession.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @return {@link ClientInfo}
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/cession/getInfoAvantCession", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ClientInfo getInfoAvantCession(@PathVariable String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-rec:::get Info Avant Cession");
		return migrationService.getInfoAvantCession(referenceContrat);
	}

	/**
	 * 
	 * renouveler un contrat
	 * 
	 * @param referenceContrat
	 * @param contratMigrationInfo
	 * @throws TopazeException
	 * @throws JsonProcessingException
	 * @throws CloneNotSupportedException
	 * @throws JSONException
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/renouveler", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public void renouvelerContrat(@PathVariable String referenceContrat,
			@RequestBody ContratRenouvellementInfo contratRenouvellementInfo)
			throws TopazeException, JsonProcessingException, CloneNotSupportedException, JSONException {
		LOGGER.info(":::ws-rec:::migrer");
		migrationService.renouvelerContrat(referenceContrat, contratRenouvellementInfo);

	}

	/**
	 * valider si un contrat est renouvelable ou non.
	 * 
	 * @param referenceContrat
	 *            reference {@link Contrat}.
	 * @param produitRenouvellements
	 *            liste des {@link ProduitRenouvellement}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/isRenouvelable", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public void isContratRenouvelable(@PathVariable String referenceContrat,
			@RequestBody List<ProduitRenouvellement> produitRenouvellements) throws TopazeException {
		LOGGER.info(":::ws-rec:::isContratRenouvelable");
		migrationService.isContratRenouvelable(referenceContrat, produitRenouvellements);
	}

	/**
	 * annuler une demande de renouvellement.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param annulationCessionInfo
	 *            {@link AnnulationInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/renouveller/annuler", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public void annulerRenouvellement(@PathVariable String referenceContrat,
			@RequestBody AnnulationInfo annulationRenouvellementInfo) throws TopazeException {
		LOGGER.info(":::ws-rec:::annuler renouvellement");
		migrationService.annulerRenouvellement(referenceContrat, annulationRenouvellementInfo);
	}

	/**
	 * Information necessaire a la simulation de migration.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return {@link ContratMigrationSimulationInfo}.
	 * @throws TopazeException
	 *             {@link ContratMigrationSimulationInfo}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/simuler/migration/info", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ContratMigrationSimulationInfo getContratInfoPourSimulationMigration(@PathVariable String referenceContrat)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::annuler getContratInfoPourSimulationMigration");
		return migrationService.getContratInfoPourSimulationMigration(referenceContrat);
	}

	/**
	 * Gerer le cas ou on a une TopazeException.
	 * 
	 * @param req
	 *            requete HttpServletRequest.
	 * @param ex
	 *            exception
	 * @return {@link InfoErreur}
	 */
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ TopazeException.class })
	@ResponseBody
	InfoErreur handleTopazeException(HttpServletRequest req, Exception ex) {
		return new InfoErreur(req.getRequestURI(), ((TopazeException) ex).getErrorCode(), ex.getLocalizedMessage());
	}

	/**
	 * 
	 * Gerer le cas ou on a une {@link Exception}.
	 * 
	 * @param req
	 *            requete HttpServletRequest.
	 * @param ex
	 *            exception
	 * @return {@link InfoErreur}
	 */
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ Exception.class })
	@ResponseBody
	InfoErreur handleException(HttpServletRequest req, Exception ex) {
		LOGGER.error(PropertiesUtil.getInstance().getErrorMessage(Constants.CODE_ERREUR_PAR_DEFAUT), ex);
		return new InfoErreur(req.getRequestURI(), Constants.CODE_ERREUR_PAR_DEFAUT, PropertiesUtil.getInstance()
				.getErrorMessage(Constants.CODE_ERREUR_PAR_DEFAUT));
	}

	/**
	 * 
	 * Gerer le cas ou on a une {@link HttpMessageNotReadableException}.
	 * 
	 * @param req
	 *            requete HttpServletRequest.
	 * @return {@link InfoErreur}
	 * @throws Exception
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ HttpMessageNotReadableException.class })
	@ResponseBody
	InfoErreur handleMessageNotReadableException(HttpServletRequest req) {
		return new InfoErreur(req.getRequestURI(), Constants.CODE_ERREUR_SYNTAXE, PropertiesUtil.getInstance()
				.getErrorMessage(Constants.CODE_ERREUR_SYNTAXE));
	}

}
