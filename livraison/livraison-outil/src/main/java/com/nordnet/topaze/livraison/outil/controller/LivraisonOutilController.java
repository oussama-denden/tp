package com.nordnet.topaze.livraison.outil.controller;

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

import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;
import com.nordnet.topaze.client.rest.business.livraison.ContratMigrationInfo;
import com.nordnet.topaze.exception.InfoErreur;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.livraison.outil.service.LivraisonOutilService;
import com.nordnet.topaze.livraison.outil.util.Constants;
import com.nordnet.topaze.livraison.outil.util.PropertiesUtil;
import com.wordnik.swagger.annotations.Api;

/**
 * Gerer l'ensemble des requetes qui ont en rapport avec la livraison des biens et des services.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Api(value = "LivraisonOutil", description = "LivraisonOutil")
@Controller
@RequestMapping("/livraison/outil/")
public class LivraisonOutilController {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(LivraisonOutilController.class);

	/**
	 * {@link ProduitService} service.
	 */
	@Autowired
	private LivraisonOutilService livraisonOutilService;

	/**
	 * Alert service.
	 */
	@Autowired
	SendAlert sendAlert;

	/**
	 * @param referenceBP
	 *            reference du contrat.
	 * @return retourne le bon de preparation d'un contrat global.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceBP:.+}/bonPreparation", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public BonPreparation getBonPreparation(@PathVariable String referenceBP) throws TopazeException {
		LOGGER.info(":::ws-rec:::getBonPreparation");
		return livraisonOutilService.getBonPreparation(referenceBP);
	}

	/**
	 * Recuperer les informations de migration d'un contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return {@link ContratMigrationInfo}
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/migrationInfo", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ContratMigrationInfo getContratMigrationInfo(@PathVariable String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-rec:::getContratMigrationInfo");
		return livraisonOutilService.getContratMigrationInfo(referenceContrat);
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
