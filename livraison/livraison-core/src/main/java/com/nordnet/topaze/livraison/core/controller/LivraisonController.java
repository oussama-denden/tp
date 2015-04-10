package com.nordnet.topaze.livraison.core.controller;

import java.util.List;

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
import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.domain.ElementLivraison;
import com.nordnet.topaze.livraison.core.service.BonPreparationService;
import com.nordnet.topaze.livraison.core.util.Constants;
import com.nordnet.topaze.livraison.core.util.PropertiesUtil;
import com.wordnik.swagger.annotations.Api;

/**
 * Gerer l'ensemble des requetes qui ont en rapport avec la livraison des biens et des services.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Api(value = "livraison", description = "livraison")
@Controller
@RequestMapping("/livraison")
public class LivraisonController {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(LivraisonController.class);

	/**
	 * {@link ProduitService} service.
	 */
	@Autowired
	private BonPreparationService bonPreparationService;

	/**
	 * Alert service.
	 */
	@Autowired
	SendAlert sendAlert;

	/**
	 * initiation d'un bon du preparation à partir d'un bon de preparation envoyer depuis la business process.
	 * 
	 * @param bonPreparationGlobal
	 *            un {@link BonPreparation} global cree par la Business Process.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/initier", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public void initierBP(@RequestBody BonPreparation bonPreparationGlobal) throws TopazeException {
		LOGGER.info(":::ws-rec:::initierBP");
		for (ElementLivraison elementLivraison : bonPreparationGlobal.getElementLivraisons()) {
			elementLivraison.setBonPreparationParent(bonPreparationGlobal);
		}
		bonPreparationService.initierBP(bonPreparationGlobal);
	}

	/**
	 * commencer la preparation des bon de preparation.
	 * 
	 * @param referenceBPGlobal
	 *            la reference du bon de preparation global a preparer.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceBPGlobal:.+}/preparer", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void preparerBP(@PathVariable String referenceBPGlobal) throws TopazeException {
		LOGGER.info(":::ws-rec:::preparerBP");
		bonPreparationService.preparerBP(referenceBPGlobal);
	}

	/**
	 * marquer le bon de preparation comme prepare.
	 * 
	 * @param referenceBPGlobal
	 *            reference du contrat global a preparer.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceBPGlobal:.+}/marquerPrepare", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void marquerBPGlobalPrepare(@PathVariable String referenceBPGlobal) throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerBPGlobalPreparer");
		bonPreparationService.marquerBPGlobalPrepare(referenceBPGlobal);
	}

	/**
	 * marquer le bon de preparation bien comme prepare.
	 * 
	 * @param bonPreparationGlobal
	 *            le {@link BonPreparation} global dont les biens seront marquer comme prepare.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/marquerBienPreparer", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void marquerBienPreparer(@RequestBody BonPreparation bonPreparationGlobal) throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerBienPreparer");
		bonPreparationService.marquerBienPreparer(bonPreparationGlobal);
	}

	/**
	 * marquer le bon de preparation service comme prepare.
	 * 
	 * @param elementLivraison
	 *            le {@link ElementLivraison} a marquer comme prepare.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/marquerServicePreparer", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void marquerServicePreparer(@RequestBody ElementLivraison elementLivraison) throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerServicePreparer");
		bonPreparationService.marquerServicePreparer(elementLivraison);
	}

	/**
	 * marquer l'element de livraison comme livre.
	 * 
	 * @param elementLivraison
	 *            l'{@link ElementLivraison} a marquer comme livre.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/marquerLivre", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void marquerLivre(@RequestBody ElementLivraison elementLivraison) throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerSousBPLivre");
		bonPreparationService.marquerLivre(elementLivraison);
	}

	/**
	 * marquer le bon de preparation global comme livre.
	 * 
	 * @param referenceBPGlobal
	 *            la reference du bon de preparation global a marque comme livre.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceBPGlobal:.+}/marquerLivre", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void marquerLivre(@PathVariable String referenceBPGlobal) throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerBPGloablLivre");
		bonPreparationService.marquerLivre(referenceBPGlobal);
	}

	/**
	 * marquer le bon de preparation global comme livre.
	 * 
	 * @param referenceSousBP
	 *            la reference du sous {@link BonPreparation} a marque comme livre.
	 * @param causeNonLivraison
	 *            la cause de non livraison.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceSousBP:.+}/marquerNonLivre/{causeNonLivraison:.+}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void marquerNonLivre(@PathVariable String referenceSousBP, @PathVariable String causeNonLivraison)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerNonLivre");
		bonPreparationService.marquerSousBPNonLivre(referenceSousBP, causeNonLivraison);
	}

	/**
	 * annuler un BP.
	 * 
	 * @param referenceBPGlobal
	 *            reference {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	@RequestMapping(value = "/{referenceBPGlobal:.+}/annuler", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void annulerBonPreparation(@PathVariable final String referenceBPGlobal) throws TopazeException {
		LOGGER.info(":::ws-rec:::annulerBonPreparation");
		bonPreparationService.annulerBonPreparation(referenceBPGlobal);
	}

	/**
	 * retourne un tableau des sous {@link BonPreparation} de type service en cours d'activation.
	 * 
	 * @return tableau des sous {@link BonPreparation} de type service en cours de livraison.
	 */
	@RequestMapping(value = "/servicesEnCoursActivation", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ElementLivraison[] getServicesEnCoursActivation() {
		LOGGER.info(":::ws-rec:::getServicesEnCoursActivation");
		List<ElementLivraison> elementLivraisonsList = bonPreparationService.getServicesEnCoursActivation();
		ElementLivraison[] elementLivraisonsArray = new ElementLivraison[elementLivraisonsList.size()];
		for (int i = 0; i < elementLivraisonsList.size(); i++) {
			elementLivraisonsArray[i] = elementLivraisonsList.get(i);
		}
		return elementLivraisonsArray;
	}

	/**
	 * retourne un tableau des sous {@link ElementLivraison} de type bien en cours de livraison.
	 * 
	 * @return tableau des sous {@link ElementLivraison} de type bien en cours de livraison.
	 */
	@RequestMapping(value = "/biensEnCoursLivraison", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ElementLivraison[] getBiensEnCoursLivraison() {
		LOGGER.info(":::ws-rec:::getBiensEnCoursLivraison");
		List<ElementLivraison> elementLivraisonsList = bonPreparationService.getBiensEnCoursLivraison();
		ElementLivraison[] elementLivraisonsArray = new ElementLivraison[elementLivraisonsList.size()];
		for (int i = 0; i < elementLivraisonsList.size(); i++) {
			elementLivraisonsArray[i] = elementLivraisonsList.get(i);
		}
		return elementLivraisonsArray;
	}

	/**
	 * Chercher un bon de preparation par reference.
	 * 
	 * @param referenceBP
	 *            reference du BP.
	 * @return {@link BonPreparation}
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	@RequestMapping(value = "/search/{referenceBP:.+}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public BonPreparation getBonPreparation(@PathVariable String referenceBP) throws TopazeException {
		LOGGER.info(":::ws-rec:::getBonPreparation");
		return bonPreparationService.findBPByReference(referenceBP);
	}

	/**
	 * Chercher un element de livraison par reference.
	 * 
	 * @param referenceEL
	 *            reference du EL.
	 * @param referenceProduit
	 *            reference du produit.
	 * @return {@link ElementLivraison}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/search/{referenceEL:.+}/{referenceProduit:.+}/elementLivraison", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ElementLivraison getElementLivraison(@PathVariable String referenceEL, @PathVariable String referenceProduit)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::getElementLivraison");
		return bonPreparationService.findElementLivraisonByReferenceAndReferenceProduit(referenceEL, referenceProduit);
	}

	/**
	 * chercher le reference bon de preparation parent.
	 * 
	 * @param referenceEL
	 *            reference element livraison.
	 * @param referenceProduit
	 *            reference produit.
	 * @param isRetour
	 *            true si c'est retour.
	 * @return reference de l'element livraison parent.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/search/{referenceEL:.+}/{referenceProduit:.+}/{isRetour:.+}/referenceBonPreparationParent", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getBonPreparationParent(@PathVariable String referenceEL, @PathVariable String referenceProduit,
			@PathVariable Boolean isRetour) throws TopazeException {
		LOGGER.info(":::ws-rec:::getBonPreparationParent");
		return bonPreparationService.getReferenceBonPreparationParent(referenceEL, referenceProduit, isRetour);
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
