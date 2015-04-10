package com.nordnet.topaze.billing.outil.controller;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.topaze.billing.outil.business.PenaliteBillingInfo;
import com.nordnet.topaze.billing.outil.service.PenaliteService;
import com.nordnet.topaze.billing.outil.util.Constants;
import com.nordnet.topaze.billing.outil.util.PropertiesUtil;
import com.nordnet.topaze.billing.outil.util.Utils;
import com.nordnet.topaze.exception.InfoErreur;
import com.nordnet.topaze.exception.TopazeException;
import com.wordnik.swagger.annotations.Api;

/**
 * Gerer le calcule de penealite.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Api(value = "penalite", description = "Penalite")
@Controller
@RequestMapping("/penalite")
public class PenaliteController {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(PenaliteController.class);

	/**
	 * The contrat service. {@link PenaliteService}.
	 */
	@Autowired
	private PenaliteService penaliteService;

	/**
	 * Alert service.
	 */
	@Autowired
	SendAlert sendAlert;

	/**
	 * Calcule de penalite est creation de {@link PenaliteBillingInfo}.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param numEC
	 *            numero elementcontractuel.
	 * @param version
	 *            version.
	 * @param engagement
	 *            engagement.
	 * @param periodicite
	 *            periodicite.
	 * @param montant
	 *            montant.
	 * @param dateDerniereFacture
	 *            derniere date de facturation.
	 * @param dateDebutFacturation
	 *            date debut facturation.
	 * @param dateFinEngagement
	 *            date fin engagement.
	 * @param dateFinContrat
	 *            date fin contrat.
	 * @return {@link PenaliteBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws ParseException
	 *             {@link ParseException}.
	 * @throws NumberFormatException
	 *             {@link NumberFormatException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/{numEC:^[1-9][0-9]*$}/penaliteBillingInfo", params = { "version",
			"engagement", "periodicite", "montant", "dateDerniereFacture", "dateDebutFacturation", "dateFinEngagement",
			"dateFinContrat" }, method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public PenaliteBillingInfo getPenaliteBillingInfo(@PathVariable String referenceContrat,
			@PathVariable Integer numEC, @RequestParam(value = "version", required = false) String version,
			@RequestParam(value = "engagement") String engagement,
			@RequestParam(value = "periodicite") String periodicite, @RequestParam(value = "montant") String montant,
			@RequestParam(value = "dateDerniereFacture") String dateDerniereFacture,
			@RequestParam(value = "dateDebutFacturation") String dateDebutFacturation,
			@RequestParam(value = "dateFinEngagement") String dateFinEngagement,
			@RequestParam(value = "dateFinContrat") String dateFinContrat)
			throws TopazeException, NumberFormatException, ParseException {
		LOGGER.info(":::ws-rec:::getPenaliteBillingInfo");
		return penaliteService.getPenaliteBillingInfo(referenceContrat, Integer.valueOf(numEC),
				Utils.isStringNullOrEmpty(version) ? null : Integer.valueOf(version), Integer.valueOf(engagement),
				Integer.valueOf(periodicite), Double.valueOf(montant),
				Constants.DEFAULT_DATE_FORMAT.parse(dateDerniereFacture),
				Constants.DEFAULT_DATE_FORMAT.parse(dateDebutFacturation),
				Constants.DEFAULT_DATE_FORMAT.parse(dateFinEngagement),
				Constants.DEFAULT_DATE_FORMAT.parse(dateFinContrat));
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
