package com.nordnet.topaze.contrat.outil.controller;

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

import com.nordnet.adminpackager.ConverterException;
import com.nordnet.adminpackager.DriverException;
import com.nordnet.adminpackager.NotFoundException;
import com.nordnet.adminpackager.NullException;
import com.nordnet.adminpackager.PackagerException;
import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.topaze.client.rest.business.contrat.ElementsRenouvellemtnInfo;
import com.nordnet.topaze.contrat.outil.service.ContratOutilService;
import com.nordnet.topaze.contrat.outil.util.Constants;
import com.nordnet.topaze.contrat.outil.util.PropertiesUtil;
import com.nordnet.topaze.exception.InfoErreur;
import com.nordnet.topaze.exception.TopazeException;
import com.wordnik.swagger.annotations.Api;

/**
 * Gerer l'ensemble des requetes qui ont en rapport avec les contrats.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Api(value = "contrat-outil", description = "Contrat Outil")
@Controller
@RequestMapping("/contrat/outil/")
public class ContratOutilController {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(ContratOutilController.class);

	/**
	 * The contrat service. {@link ContratOutilService}.
	 */
	@Autowired
	private ContratOutilService contratServiceOutilService;

	/**
	 * Alert service.
	 */
	@Autowired
	SendAlert sendAlert;

	/**
	 * chercher les etas de chaqye element contractuelle dans la liste envoyée
	 * 
	 * @param elementsRenouvellemtnInfo
	 *            {@link ElementsRenouvellemtnInfo}.
	 * @return liste des status des contrats.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws ConverterException
	 * @throws DriverException
	 * @throws PackagerException
	 * @throws com.nordnet.adminpackager.NullException
	 * @throws com.nordnet.adminpackager.NotFoundException
	 */
	@RequestMapping(value = "/ElementsState", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public Object[] getElementStateInformation(@RequestBody ElementsRenouvellemtnInfo elementsRenouvellemtnInfo)
			throws TopazeException, NullException, NotFoundException, PackagerException, DriverException,
			ConverterException {
		LOGGER.info(":::ws-rec:::getElementStateInformation");
		Object[] contratStateInfoList = contratServiceOutilService.getElementsState(elementsRenouvellemtnInfo);
		return contratStateInfoList;

	}

	/**
	 * valider via 'netEquipment' s'il n'y a pas un equipement qui a plusieurs numero de serie.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param idClient
	 *            id du client.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/{idClient:.+}/validerSerialNumber", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Boolean validerSerialNumber(@PathVariable String referenceContrat, @PathVariable String idClient)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::validerSerialNumber");
		return contratServiceOutilService.validerSerialNumber(referenceContrat, idClient);
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
	@ResponseStatus(value = HttpStatus.OK)
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
