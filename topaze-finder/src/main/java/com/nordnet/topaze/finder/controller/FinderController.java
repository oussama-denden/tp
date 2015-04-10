package com.nordnet.topaze.finder.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
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

import com.nordnet.topaze.exception.InfoErreur;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.finder.business.espaceclient.EspaceClientAbonnementInfo;
import com.nordnet.topaze.finder.business.welcome.WelcomeAbonnementInfo;
import com.nordnet.topaze.finder.business.welcome.WelcomeContratInfo;
import com.nordnet.topaze.finder.service.ContratService;
import com.nordnet.topaze.finder.util.Constants;
import com.nordnet.topaze.finder.util.PropertiesUtil;
import com.wordnik.swagger.annotations.Api;

/**
 * Gerer l'ensemble des requetes qui sont en rapport avec les contrats.
 * 
 * @author anisselmane.
 * 
 */
@Api(value = "finder", description = "Topaze finder")
@Controller
@RequestMapping("/contrat")
public class FinderController {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(FinderController.class);

	/**
	 * Contrat service.
	 */
	@Autowired
	ContratService contratService;

	/**
	 * Chercher les abonnements en cours pour un client.
	 * 
	 * @param idClient
	 *            id du client.
	 * @return {@link WelcomeAbonnementInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/client/{idClient:.+}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<WelcomeAbonnementInfo> getWelcomeAbonnementsClient(@PathVariable String idClient)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::getWelcomeAbonnementsClient");
		return contratService.getWelcomeAbonnementsClient(idClient);
	}

	/**
	 * Chercher les abonnements en cours d'un client pour l'Espace Client .
	 * 
	 * @param idClient
	 *            id du client.
	 * @return {@link EspaceClientAbonnementInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/espaceclient/client/{idClient:.+}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<EspaceClientAbonnementInfo> getEspaceClientAbonnementsClient(@PathVariable String idClient)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::getEspaceClientAbonnementsClient");
		return contratService.getEspaceClientAbonnementsClient(idClient);
	}

	/**
	 * Recuperer les informations detaillee du contrat pour Welcome.
	 * 
	 * @param refContrat
	 *            reference du contrat.
	 * @return {@link WelcomeContratInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{refContrat:.+}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public WelcomeContratInfo getWelcomeContratInfo(@PathVariable String refContrat) throws TopazeException {
		LOGGER.info(":::ws-rec:::getWelcomeContratInfo");
		return contratService.getWelcomeContratInfo(refContrat);
	}

	/**
	 * Recuperer le Retailer Packager ID.
	 * 
	 * @param refContrat
	 *            reference du contrat.
	 * @return Retailer Packager ID.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws JSONException
	 *             {@link JSONException}.
	 */
	@RequestMapping(value = "/{refContrat:.+}/RPID", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getRPID(@PathVariable String refContrat) throws TopazeException, JSONException {
		LOGGER.info(":::ws-rec:::getRPID");
		String rpid = contratService.getRPID(refContrat);

		JSONObject rpidJSON = new JSONObject();
		rpidJSON.put("RPID", rpid);
		return rpidJSON.toString();
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

}