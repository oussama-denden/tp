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
import com.nordnet.topaze.livraison.core.service.BonRecuperationService;
import com.nordnet.topaze.livraison.core.util.Constants;
import com.nordnet.topaze.livraison.core.util.PropertiesUtil;
import com.wordnik.swagger.annotations.Api;

/**
 * Gerer l'ensemble des requetes qui ont en rapport avec le retour des biens et des services.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Api(value = "recuperation", description = "recuperation")
@Controller
@RequestMapping("/recuperation")
public class RecuperationController {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(RecuperationController.class);

	/**
	 * {@link BonPreparationService} service.
	 */
	@Autowired
	private BonRecuperationService bonRecuperationService;

	/**
	 * Alert service.
	 */
	@Autowired
	SendAlert sendAlert;

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
	public BonPreparation getBonRecuperation(@PathVariable String referenceBP) throws TopazeException {
		LOGGER.info(":::ws-rec:::getBonRecuperation");
		return bonRecuperationService.findBRByReference(referenceBP);
	}

	/**
	 * Chercher un element de recuperation par reference.
	 * 
	 * @param referenceER
	 *            reference du ER.
	 * @return {@link ElementLivraison} de recuperation.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	@RequestMapping(value = "/search/element/{referenceER:.+}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ElementLivraison getElementRecuperation(@PathVariable String referenceER) throws TopazeException {
		LOGGER.info(":::ws-rec:::getElementRecuperation");
		return bonRecuperationService.findElementRecuperation(referenceER);
	}

	/**
	 * Editer un bon de préparation Afin de répertorier les éléments que le client doit retourner, et les services a
	 * désactiver.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/contrat/{referenceContrat:.+}/initierBR", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
	@ResponseBody
	public void initierBR(@PathVariable String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-rec:::initierBR");
		bonRecuperationService.initierBR(referenceContrat);

	}

	/**
	 * Initier un element de retour apres une resiliation partiel.
	 * 
	 * @param referenceElementContractuel
	 *            reference {@link ElementLivraison}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/contrat/{referenceElementContractuel:.+}/initierER", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
	@ResponseBody
	public void initierER(@PathVariable String referenceElementContractuel) throws TopazeException {
		LOGGER.info(":::ws-rec:::initierER");
		bonRecuperationService.initierER(referenceElementContractuel);

	}

	/**
	 * Preparer un bon de retour afin de répertorier les éléments que le client doit retourner, et les services a
	 * désactiver.
	 * 
	 * @param bonRecuperation
	 *            Bon de Recuperation.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/preparer", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void preparerBR(@RequestBody BonPreparation bonRecuperation) throws TopazeException {
		LOGGER.info(":::ws-rec:::preparerBR");
		bonRecuperationService.preparerBR(bonRecuperation);

	}

	/**
	 * Preparer les {@link ElementLivraison} deja initié. Sa entraine d'emettre les appels vers Packager et vers
	 * NetRetour.
	 * 
	 * @param bonRecuperation
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/preparerER", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void preparerER(@RequestBody BonPreparation bonRecuperation) throws TopazeException {
		LOGGER.info(":::ws-rec:::preparerER");
		bonRecuperationService.preparerER(bonRecuperation);

	}

	/**
	 * Marquer un BP comme recuperer.
	 * 
	 * @param referenceBR
	 *            reference de BR.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceBR:.+}/marquerRecupere", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void marquerRecupere(@PathVariable String referenceBR) throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerRecupere");
		bonRecuperationService.marquerRecupere(referenceBR);
	}

	/**
	 * Indiquer que une service est suspendu.
	 * 
	 * @param referenceEL
	 *            reference de EL.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceEL:.+}/marquerSuspendu", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void marquerSuspendu(@PathVariable String referenceEL) throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerSuspendu");
		bonRecuperationService.marquerELRecupere(referenceEL);
	}

	/**
	 * Indiquer que un bien est retourner.
	 * 
	 * @param referenceEL
	 *            reference de EL.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceEL:.+}/marquerRetourner", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void marquerRetourner(@PathVariable String referenceEL) throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerRetourner");
		bonRecuperationService.marquerELRecupere(referenceEL);
	}

	/**
	 * retourne la liste des sous {@link BonPreparation} de type bien en cours de recuperation.
	 * 
	 * @return la list des sous {@link BonPreparation} de type bien en cours de recuperation.
	 */
	@RequestMapping(value = "/biensEnCoursRecuperation", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ElementLivraison[] getBiensEnCoursRecuperation() {
		LOGGER.info(":::ws-rec:::getBiensEnCoursRecuperation");
		List<ElementLivraison> elementLivraisonsList = bonRecuperationService.getBiensEnCoursRecuperation();
		ElementLivraison[] elementLivraisonsArray = new ElementLivraison[elementLivraisonsList.size()];
		for (int i = 0; i < elementLivraisonsList.size(); i++) {
			elementLivraisonsArray[i] = elementLivraisonsList.get(i);
		}
		return elementLivraisonsArray;

	}

	/**
	 * retourne la liste des sous {@link BonPreparation} de type service en cours de recuperation.
	 * 
	 * @return la list des sous {@link BonPreparation} de type service en cours de recuperation.
	 */
	@RequestMapping(value = "/servicesEnCoursSuspension", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ElementLivraison[] getServicesEnCoursSuspension() {
		LOGGER.info(":::ws-rec:::getServicesEnCoursSuspension");
		List<ElementLivraison> elementLivraisonsList = bonRecuperationService.getServicesEnCoursSuspension();
		ElementLivraison[] elementLivraisonsArray = new ElementLivraison[elementLivraisonsList.size()];
		for (int i = 0; i < elementLivraisonsList.size(); i++) {
			elementLivraisonsArray[i] = elementLivraisonsList.get(i);
		}
		return elementLivraisonsArray;

	}

	/**
	 * retourne la liste des sous {@link BonPreparation} de type service qui sont suspendu.
	 * 
	 * @return la list des sous {@link BonPreparation} de type service qui sont suspendu.
	 */
	@RequestMapping(value = "/servicesSuspendu", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ElementLivraison[] getServicesSuspendu() {
		LOGGER.info(":::ws-rec:::getServicesSuspendu");
		List<ElementLivraison> elementLivraisonsList = bonRecuperationService.getServicesSuspendu();
		ElementLivraison[] elementLivraisonsArray = new ElementLivraison[elementLivraisonsList.size()];
		for (int i = 0; i < elementLivraisonsList.size(); i++) {
			elementLivraisonsArray[i] = elementLivraisonsList.get(i);
		}
		return elementLivraisonsArray;

	}

	/**
	 * @return liste de BR Global non recupere.
	 */
	@RequestMapping(value = "/encoursRecuperation", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public BonPreparation[] getBRGlobalEncoursRecuperation() {
		LOGGER.info(":::ws-rec:::getServicesEnCoursSuspension");
		List<BonPreparation> bonPreparationsList = bonRecuperationService.getBRGlobalEncoursRecuperation();
		BonPreparation[] bonPreparationsArray = new BonPreparation[bonPreparationsList.size()];
		for (int i = 0; i < bonPreparationsList.size(); i++) {
			bonPreparationsArray[i] = bonPreparationsList.get(i);
		}
		return bonPreparationsArray;

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
