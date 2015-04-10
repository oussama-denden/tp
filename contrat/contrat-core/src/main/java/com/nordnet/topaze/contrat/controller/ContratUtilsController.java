package com.nordnet.topaze.contrat.controller;

import java.io.IOException;
import java.util.List;

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
import com.nordnet.topaze.contrat.business.ContratAvenantInfo;
import com.nordnet.topaze.contrat.business.ContratBP;
import com.nordnet.topaze.contrat.business.ContratBillingInfo;
import com.nordnet.topaze.contrat.business.ElementContractuelInfo;
import com.nordnet.topaze.contrat.business.ResiliationBillingInfo;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.service.ContratService;
import com.nordnet.topaze.contrat.service.ResiliationService;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.exception.InfoErreur;
import com.nordnet.topaze.exception.TopazeException;
import com.wordnik.swagger.annotations.Api;

/**
 * Gerer l'ensemble des requetes qui definit des fonctionnalites utiles en rapport au contrat.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Api(value = "utils", description = "utils")
@Controller
@RequestMapping("/contrat")
public class ContratUtilsController {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(ContratUtilsController.class);

	/**
	 * {@link ContratService}.
	 */
	@Autowired
	private ContratService contratService;

	/**
	 * {@link ResiliationService}.
	 */
	@Autowired
	private ResiliationService resiliationService;

	/**
	 * Alert service.
	 */
	@Autowired
	SendAlert sendAlert;

	/**
	 * chercher la liste des contrats billing info lier a un contrat global.
	 * 
	 * @param referenceContrat
	 *            reference d'un contrat.
	 * @return liste des {@link ContratBillingInfo}
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/{numEC:.+}/billingInformation", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Object[] getContratBillingInformation(@PathVariable String referenceContrat, @PathVariable Integer numEC)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::getContratBillingInformation");
		List<ContratBillingInfo> contratBillingInfoList =
				contratService.getContratBillingInformation(referenceContrat, numEC);
		return contratBillingInfoList.toArray();
	}

	/**
	 * Gets the contrat resiliation information.
	 * 
	 * @param referenceContrat
	 *            the reference contrat
	 * @return the contrat resiliation information
	 * @throws TopazeException
	 *             the topaze exception
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/resiliationBillingInformation", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResiliationBillingInfo[] getResiliationBillingIformation(@PathVariable String referenceContrat)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::getResiliationBillingIformation: global");
		List<ResiliationBillingInfo> resiliationBillingInfoList =
				resiliationService.getResiliationBillingIformation(referenceContrat);
		ResiliationBillingInfo[] contratResiliationInfoArray =
				new ResiliationBillingInfo[resiliationBillingInfoList.size()];
		for (int i = 0; i < resiliationBillingInfoList.size(); i++) {
			contratResiliationInfoArray[i] = resiliationBillingInfoList.get(i);
		}
		return contratResiliationInfoArray;
	}

	/**
	 * Gets the contrat resiliation information.
	 * 
	 * @param referenceContrat
	 *            the reference contrat
	 * @return the contrat resiliation information
	 * @throws TopazeException
	 *             the topaze exception
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/partiel/{numEC:[0-9]*}/resiliationBillingInformation", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResiliationBillingInfo[] getResiliationBillingIformation(@PathVariable String referenceContrat,
			@PathVariable Integer numEC) throws TopazeException {
		LOGGER.info(":::ws-rec:::getResiliationBillingIformation: partiel");
		List<ResiliationBillingInfo> resiliationBillingInfoList =
				resiliationService.getResiliationBillingIformation(referenceContrat, numEC);
		ResiliationBillingInfo[] contratResiliationInfoArray =
				new ResiliationBillingInfo[resiliationBillingInfoList.size()];
		for (int i = 0; i < resiliationBillingInfoList.size(); i++) {
			contratResiliationInfoArray[i] = resiliationBillingInfoList.get(i);
		}
		return contratResiliationInfoArray;
	}

	/**
	 * Preparer les information d'un BP pour un contrat global.
	 * 
	 * @param referenceContrat
	 *            reference du contrat global.
	 * @return {@link ContratBP}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/bonPreparation", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ContratBP getContratBP(@PathVariable String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-rec:::getContratBP");
		return contratService.getContratBP(referenceContrat);
	}

	/**
	 * Preparer les information d'un BP pour un contrat global.
	 * 
	 * @param referenceContrat
	 *            reference du contrat global.
	 * @return {@link ContratBP}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/historique/bonPreparation", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ContratBP getContratBPHistorique(@PathVariable String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-rec:::getContratBPHistorique");
		return contratService.getContratBPHistorique(referenceContrat);
	}

	/**
	 * Preparer les information d'un Avenant necaissaire a la migration d'un contrat global.
	 * 
	 * @param referenceContrat
	 *            reference du contrat global.
	 * @return {@link ContratBP}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws IOException
	 *             {@link IOException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/avenantInfo", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ContratAvenantInfo getContratAvenantInfo(@PathVariable String referenceContrat)
			throws TopazeException, IOException {
		LOGGER.info(":::ws-rec:::getContratAvenantInfo");
		return contratService.getContratAvenantInfo(referenceContrat);
	}

	/**
	 * 
	 * @return liste des references des contrat livre non resilier.
	 */
	@RequestMapping(value = "/referencesContratLivrer", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<String> getReferencesContratLivrer() {
		LOGGER.info(":::ws-rec:::getReferencesContratLivrer");
		return contratService.getReferencesContratLivrer();
	}

	/**
	 * retourner les infos des fils de type 'SERVICE' d'un {@link ElementContractuel}.
	 * 
	 * @param referenceContrat
	 *            reference {@link ElementContractuel}.
	 * @param numEC
	 *            numero {@link ElementContractuel}.
	 * @return liste des {@link ElementContractuelInfo} fils de type 'SERVICE'.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/{numEC:[0-9]*}/enfantsService", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Object[] getFils(@PathVariable String referenceContrat, @PathVariable Integer numEC) throws TopazeException {
		LOGGER.info(":::ws-rec:::getFilsService");
		return contratService.getFilsService(referenceContrat, numEC).toArray();
	}

	/**
	 * Retourne la reference de {@link ElementContractuel} parent.
	 * 
	 * @param refContrat
	 *            reference du {@link Contrat}.
	 * @param numEC
	 *            numero de {@link ElementContractuel}.
	 * @return {@link ElementContractuelInfo} associe au parent.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{refContrat:.+}/{numEC:[0-9]*}/parentInfo", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ElementContractuelInfo getParentInfo(@PathVariable String refContrat, @PathVariable Integer numEC)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::getParentInfo");
		return contratService.getParentInfo(refContrat, numEC);
	}

	/**
	 * Verifier si un {@link ElementContractuel} est resilier ou non.
	 * 
	 * @param referenceElementContractuel
	 *            reference de {@link ElementContractuel}.
	 * @return true si l'{@link ElementContractuel} est resilier.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/{numEC:.+}/isElementContractuelResilier", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Boolean isElementContractuelResilier(@PathVariable String referenceContrat, @PathVariable Integer numEC)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::isElementContractuelResilier");
		return resiliationService.isElementContractuelResilier(referenceContrat, numEC);
	}

	/**
	 * retourne si le test 'isPackagerCreationPossible' doit étre fait avant de creer le package ou non.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @return true si le test de creation du package doit etre fait avant de lancer une demande de creation vers
	 *         packager.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/checkIsPackagerCreationPossible", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Boolean checkIsPackagerCreationPossible(@PathVariable String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-rec:::checkIsPackagerCreationPossible");
		return contratService.checkIsPackagerCreationPossible(referenceContrat);
	}

	/**
	 * chercher les information du derniere historisation du contrat.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @return liste des {@link ContratBillingInfo} lie au dernier historique du contrat.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/billingInformationHistorise", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Object[] getContratBillingInformationHistorise(@PathVariable String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-rec:::getContratBillingInformationHistorise");
		List<ContratBillingInfo> contratBillingInfoList =
				contratService.getContratBillingInformationHistorise(referenceContrat);
		return contratBillingInfoList.toArray();
	}

	/**
	 * chercher les information du migration administrative du contrat.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @return liste des {@link ContratBillingInfo} lie au dernier historique du contrat.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/migrationAdministrative/billingInformation", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Object[] getContratMigrationAdministrativeBillingInfo(@PathVariable String referenceContrat)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::getContratMigrationAdministrativeBillingInfo");
		List<ContratBillingInfo> contratBillingInfoList =
				contratService.getContratBillingInformationMigrationAdministrative(referenceContrat);
		return contratBillingInfoList.toArray();
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
