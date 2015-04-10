package com.nordnet.topaze.businessprocess.packager.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.nordnet.topaze.businessprocess.packager.service.PackagerService;
import com.nordnet.topaze.businessprocess.packager.util.Constants;
import com.nordnet.topaze.businessprocess.packager.util.PropertiesUtil;
import com.nordnet.topaze.exception.InfoErreur;
import com.nordnet.topaze.exception.TopazeException;
import com.wordnik.swagger.annotations.Api;

/**
 * Gerer l'ensemble des requetes pour assurer la maintenance operationnelle de process packager.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Api(value = "process", description = "process")
@Controller
@RequestMapping("/process")
public class BusinessProcessPackagerController {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(BusinessProcessPackagerController.class);

	/**
	 * {@link PackagerService}.
	 */
	@Autowired
	private PackagerService packagerService;

	/**
	 * Maintenance operationnelle de process PrepareServiceDelivery.
	 * 
	 * @param referenceBP
	 *            reference de bon de preparation.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/retenterPrepareServiceDelivery/{referenceBP}", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public void forceProcessPrepareServiceDelivery(final @PathVariable String referenceBP) throws TopazeException {
		LOGGER.info(":::wsprocess-rec:::forceProcessPrepareServiceDelivery");
		packagerService.traductionPackagerPourLivraison(referenceBP);
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
