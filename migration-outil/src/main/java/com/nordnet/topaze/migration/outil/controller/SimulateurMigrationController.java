package com.nordnet.topaze.migration.outil.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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

import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.topaze.exception.InfoErreur;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.migration.outil.business.ContratMigrationInfo;
import com.nordnet.topaze.migration.outil.business.ResultatSimulation;
import com.nordnet.topaze.migration.outil.service.SimulateurMigrationService;
import com.nordnet.topaze.migration.outil.util.Constants;
import com.nordnet.topaze.migration.outil.util.PropertiesUtil;
import com.wordnik.swagger.annotations.Api;

/**
 * Outils de simulation de migration des contrats.
 * 
 * @author Denden Oussama
 * 
 */
@Api(value = "MigrationOutil", description = "MigrationOutil")
@Controller
@RequestMapping("/migration")
public class SimulateurMigrationController {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(SimulateurMigrationController.class);

	/**
	 * Autowiring du Service SimulateurMigrationService.
	 */
	@Autowired
	private SimulateurMigrationService simulateurMigrationService;

	/**
	 * Alert service.
	 */
	@Autowired
	SendAlert sendAlert;

	/**
	 * Simuler la migration d'un contrat global.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param contratMigrationInfo
	 *            {@link ContratMigrationInfo}.
	 * @return {@link ResultatSimulation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/simuler", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ResultatSimulation simulerMigration(@PathVariable String referenceContrat,
			@RequestBody ContratMigrationInfo contratMigrationInfo) throws TopazeException {
		LOGGER.info(":::ws-rec:::simulerMigration");
		return simulateurMigrationService.simulerMigration(referenceContrat, contratMigrationInfo);
	}

	/**
	 * Gerer le cas ou on a une exception.
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
		try {
			sendAlert.send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
					"error occurs during call of " + req.getRequestURI(), ex.getMessage());
		} catch (Exception e) {
			LOGGER.error("fail to send alert", e);
		}
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