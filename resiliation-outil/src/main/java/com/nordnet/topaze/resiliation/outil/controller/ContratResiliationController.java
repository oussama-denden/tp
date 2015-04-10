package com.nordnet.topaze.resiliation.outil.controller;

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
import com.nordnet.topaze.resiliation.outil.business.ContratResiliationInfo;
import com.nordnet.topaze.resiliation.outil.business.ECResiliationInfo;
import com.nordnet.topaze.resiliation.outil.business.PolitiqueResiliation;
import com.nordnet.topaze.resiliation.outil.service.ContratResiliationService;
import com.nordnet.topaze.resiliation.outil.util.Constants;
import com.nordnet.topaze.resiliation.outil.util.PropertiesUtil;
import com.wordnik.swagger.annotations.Api;

/**
 * Outils de resiliation des contrats.
 * 
 * @author Denden Oussama
 * 
 */
@Api(value = "ResiliationOutil", description = "ResiliationOutil")
@Controller
@RequestMapping("/resiliation")
public class ContratResiliationController {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(ContratResiliationController.class);

	/**
	 * Autowiring du Service contratResiliation.
	 */
	@Autowired
	private ContratResiliationService contratResiliationService;

	/**
	 * Alert service.
	 */
	@Autowired
	SendAlert sendAlert;

	/**
	 * Simuler la resiliation d'un contrat global.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param politiqueResiliation
	 *            {@link PolitiqueResiliation}.
	 * @return {@link ContratResiliationInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/simuler", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ContratResiliationInfo simulerResiliation(@PathVariable String referenceContrat,
			@RequestBody PolitiqueResiliation politiqueResiliation) throws TopazeException {
		LOGGER.info(":::ws-rec:::simulerResiliation");
		return contratResiliationService.simulerResiliation(referenceContrat, politiqueResiliation);
	}

	/**
	 * Simuler la resiliation partiel d'un contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param numEC
	 *            numEC de produit.
	 * @param politiqueResiliation
	 *            {@link PolitiqueResiliation}.
	 * @return {@link ECResiliationInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/partiel/{numEC:[1-9]}/simuler", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ContratResiliationInfo simulerResiliationPartiel(@PathVariable String referenceContrat,
			@PathVariable Integer numEC, @RequestBody PolitiqueResiliation politiqueResiliation) throws TopazeException {
		LOGGER.info(":::ws-rec:::simulerResiliationPartiel");
		return contratResiliationService.simulerResiliationPartiel(referenceContrat, numEC, politiqueResiliation);
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