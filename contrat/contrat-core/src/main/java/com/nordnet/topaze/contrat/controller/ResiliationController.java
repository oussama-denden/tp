package com.nordnet.topaze.contrat.controller;

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
import com.nordnet.topaze.contrat.business.AnnulationInfo;
import com.nordnet.topaze.contrat.business.ContratResiliationtInfo;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.service.ResiliationService;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.exception.InfoErreur;
import com.nordnet.topaze.exception.TopazeException;
import com.wordnik.swagger.annotations.Api;

/**
 * Gerer l'ensemble des requetes qui ont en rapport avec la resiliation du contrats.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Api(value = "resiliation", description = "resiliation")
@Controller
@RequestMapping("/contrat")
public class ResiliationController {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(ResiliationController.class);

	/**
	 * service de resiliation {@link ResiliationService}.
	 */
	@Autowired
	private ResiliationService resiliationService;

	/**
	 * Alert service.
	 */
	@Autowired
	SendAlert sendAlert;

	/**
	 * resilier un contrat global.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param contratResiliationtInfo
	 *            the contrat resiliationt info {@link ContratResiliationtInfo}.
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/resilier", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public void resilierContrat(@PathVariable String referenceContrat,
			@RequestBody ContratResiliationtInfo contratResiliationtInfo) throws TopazeException {
		LOGGER.info(":::ws-rec:::resilierContratParClient");
		resiliationService.resilierContrat(referenceContrat, contratResiliationtInfo.getPolitiqueResiliation(),
				contratResiliationtInfo.getUser(), false, false, false, false, false, false);
	}

	/**
	 * Resilier plusieurs contrats d'un seul coup.
	 * 
	 * @param contratResiliationtInfo
	 *            the contrat resiliationt info {@link ContratResiliationtInfo}.
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 */
	@RequestMapping(value = "/resilier", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public void resilierContrats(@RequestBody ContratResiliationtInfo contratResiliationtInfo) throws TopazeException {
		LOGGER.info(":::ws-rec:::resilierContrats");
		resiliationService.resilierContrats(contratResiliationtInfo);
	}

	/**
	 * resilier un produit dans un contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param numEC
	 *            numero {@link ElementContractuel}.
	 * @param contratResiliationtInfo
	 *            the contrat resiliationt info {@link ContratResiliationtInfo}.
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/{numEC:[0-9]*}/resilier", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public void resilierPartielContrat(@PathVariable String referenceContrat, @PathVariable Integer numEC,
			@RequestBody ContratResiliationtInfo contratResiliationtInfo) throws TopazeException {
		LOGGER.info(":::ws-rec:::resilierPartielContrat");
		resiliationService.resiliationPartiel(referenceContrat, numEC,
				contratResiliationtInfo.getPolitiqueResiliation(), contratResiliationtInfo.getUser(), false, false);
	}

	/**
	 * annuler la demande de resiliation
	 * 
	 * @param referenceContrat
	 * @param annulationResiliationInfo
	 * @throws TopazeException
	 */

	@RequestMapping(value = "/{referenceContrat:.+}/resilier/annuler", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public void annulerResiliation(@PathVariable String referenceContrat,
			@RequestBody AnnulationInfo annulationResiliationInfo) throws TopazeException {
		LOGGER.info(":::ws-rec:::resilierContratParClient");
		resiliationService.annulerResiliation(referenceContrat, annulationResiliationInfo);

	}

	/**
	 * annuler la demande de resiliation partielle
	 * 
	 * @param referenceContrat
	 * @param numEC
	 * @param numEC
	 * @param contratResiliationtInfo
	 * @throws TopazeException
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/{numEC:[0-9]*}/resilier/annuler", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public void annulerResiliationPartiel(@PathVariable String referenceContrat, @PathVariable Integer numEC,
			@RequestBody AnnulationInfo annulationResiliationInfo) throws TopazeException {
		LOGGER.info(":::ws-rec:::resilierPartielContrat");
		resiliationService.annulerResiliationPartiel(referenceContrat, numEC, annulationResiliationInfo);
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
