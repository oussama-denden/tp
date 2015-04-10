package com.nordnet.topaze.contrat.controller;

import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.topaze.contrat.business.CommentaireInfo;
import com.nordnet.topaze.contrat.business.ContratChangerModePaiementInfo;
import com.nordnet.topaze.contrat.business.ContratPreparationInfo;
import com.nordnet.topaze.contrat.business.ContratValidationInfo;
import com.nordnet.topaze.contrat.domain.ChangerDateFacturationInfos;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.service.ContratService;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.exception.InfoErreur;
import com.nordnet.topaze.exception.TopazeException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * Gerer l'ensemble des requetes qui ont en rapport avec les contrats.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Api(value = "contrat", description = "contrat")
@Controller
@RequestMapping("/contrat")
public class ContratController {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(ContratUtilsController.class);

	/**
	 * The contrat service. {@link ContratService}.
	 */
	@Autowired
	private ContratService contratService;

	/**
	 * Alert service.
	 */
	@Autowired
	SendAlert sendAlert;

	/**
	 * Gets the contrat by reference.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return {@link ElementContractuel}.
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Contrat getContratByReference(
			@ApiParam(name = "referenceContrat", value = "The Id of the product to be viewed", required = true) @PathVariable String referenceContrat)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::getContratByReference");
		return contratService.getContratByReference(referenceContrat);
	}

	/**
	 * Generer une liste des contrats a partir d'une liste des produits.
	 * 
	 * @param contratPreparationInfo
	 *            the contrat preparation info {@link ContratPreparationInfo}.
	 * @return reference contrat {@link String}.
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 * @throws JSONException
	 *             {@link JSONException}
	 */
	@RequestMapping(value = "/preparer", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String preparerContrat(@RequestBody ContratPreparationInfo contratPreparationInfo)
			throws TopazeException, JSONException {
		LOGGER.info(":::ws-rec:::preparerContrat");

		Contrat contrat =
				contratService.preparerContrat(contratPreparationInfo.getProduits(), contratPreparationInfo.getUser(),
						false, null, false);
		JSONObject preparerContratResponse = new JSONObject();
		preparerContratResponse.put("referenceContrat", contrat.getReference());
		return preparerContratResponse.toString();

	}

	/**
	 * Validation du contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param contratValidationInfo
	 *            the contrat validation info
	 * @throws TopazeException
	 *             the topaze exception {@link ContratValidationInfo}. {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/valider", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public void validerContrat(final @PathVariable String referenceContrat,
			@RequestBody ContratValidationInfo contratValidationInfo) throws TopazeException {
		LOGGER.info(":::ws-rec:::validerContrat");
		contratService.validerContrat(referenceContrat, contratValidationInfo);
	}

	/**
	 * Le date debut de facturation peut etre modifier manuellement.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param changement
	 *            changement de date debut facturation.
	 * @return date debut facturation.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/changerDateDebutFacturation", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public Date changerDateDebutFacturation(@PathVariable String referenceContrat,
			@RequestBody ChangerDateFacturationInfos changement) throws TopazeException {
		LOGGER.info(":::ws-rec:::changerDateDebutFacturation");
		return contratService.changerDateDebutFacturation(referenceContrat, changement.getDateDebutFacturation());
	}

	/**
	 * Chercher les contrat global valider.
	 * 
	 * @return {@link ElementContractuel}.
	 */
	@RequestMapping(value = "/search/globalValider", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Contrat> findContratGlobalValider() {
		LOGGER.info(":::ws-rec:::findContratGlobalValider");
		return contratService.findContratsGlobalValider();
	}

	/**
	 * Chercher les references du contrats global valider.
	 * 
	 * @return {les references du contrats global valider.
	 */
	@RequestMapping(value = "/search/globalValider/reference", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<String> getReferencesContratsGlobalValider() {
		LOGGER.info(":::ws-rec:::getReferencesContratsGlobalValider");
		return contratService.getReferencesContratsGlobalValider();
	}

	/**
	 * Modifier la date de derniere facture.
	 * 
	 * @param referenceContrat
	 *            reference d'un contrat global ou dans sous contrat.
	 * @param numEC
	 *            numero EL.
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/{numEC:.+}/changerDateDerniereFacture", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public void changerDateDerniereFacture(@PathVariable String referenceContrat, @PathVariable Integer numEC)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::changerDateDerniereFacture");
		contratService.changerDateDerniereFacture(referenceContrat, numEC);
	}

	/**
	 * Modifier la date de derniere facture.
	 * 
	 * @param referenceContrat
	 *            reference d'un contrat global ou dans sous contrat.
	 * @param numEC
	 *            numero EL.
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/{numEC:.+}/changerDateFactureResiliation", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public void changerDateFactureResiliation(@PathVariable String referenceContrat, @PathVariable Integer numEC)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::changerDateFactureResiliation");
		contratService.changerDateFactureResiliation(referenceContrat, numEC);
	}

	/**
	 * changer date du fin du contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/{numEC:.+}/changerDateFinContrat", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public void changerDateFinContrat(@PathVariable String referenceContrat, @PathVariable Integer numEC)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::changerDateFinContrat");
		contratService.changerDateFinContrat(referenceContrat, numEC);
	}

	/**
	 * Changer le moyen de paiement.
	 * 
	 * @param referenceContrat
	 *            the reference contrat
	 * @param changerModePaiementInfo
	 *            the changer mode paiement info
	 * @throws TopazeException
	 *             the topaze exception
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/changerMoyenDePaiement", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public void changerMoyenDePaiement(@PathVariable String referenceContrat,
			@RequestBody ContratChangerModePaiementInfo changerModePaiementInfo) throws TopazeException {
		LOGGER.info(":::ws-rec:::changerMoyenDePaiement");
		contratService.changerMoyenDePaiement(referenceContrat, changerModePaiementInfo);
	}

	/**
	 * retourner la reference de la commande associe.
	 * 
	 * @param referenceContrat
	 *            reference {@link Contrat}.
	 * @return reference commande.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/getReferenceCommande", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public String getReferenceCommande(@PathVariable String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-rec:::getReferenceCommande");
		return contratService.getReferenceCommande(referenceContrat);
	}

	/**
	 * Ajouter un commantaire sur le contrat.
	 * 
	 * @param referenceContrat
	 *            reference contrat
	 * @param commentaireInfo
	 *            {@link CommentaireInfo}.
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/commentaire/", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public void ajouterCommentaire(@PathVariable String referenceContrat, @RequestBody CommentaireInfo commentaireInfo)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::ajouterCommentaire");

		contratService
				.ajouterCommentaire(referenceContrat, commentaireInfo.getCommentaire(), commentaireInfo.getUser());

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