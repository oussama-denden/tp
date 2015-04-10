package com.nordnet.topaze.contrat.controller;

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
import com.nordnet.topaze.contrat.business.ContratReduction;
import com.nordnet.topaze.contrat.business.ReductionInterface;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.service.ReductionService;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.exception.InfoErreur;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Controller qui contrient les appels rest lie a la reduction.
 * 
 * @author akram-moncer
 * 
 */
@Controller
@RequestMapping(value = "/contrat")
public class ReductionController {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(ReductionController.class);

	/**
	 * {@link ReductionService}.
	 */
	@Autowired
	public ReductionService reductionService;

	/**
	 * Alert service.
	 */
	@Autowired
	SendAlert sendAlert;

	/**
	 * Ajouter reduction.
	 * 
	 * @param referenceContrat
	 *            the reference contrat
	 * @param contratReduction
	 *            {@link ContratReduction}.
	 * @throws TopazeException
	 *             the topaze exception
	 * @throws JSONException
	 */

	@RequestMapping(value = "/{referenceContrat:.+}/reduction/ajouter", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Object ajouterReductionContrat(@PathVariable String referenceContrat,
			@RequestBody ContratReduction contratReduction) throws TopazeException, JSONException {
		LOGGER.info(":::ws-rec:::ajouterReduction");

		String referenceReduction = reductionService.ajouterReductionContrat(contratReduction, referenceContrat);
		JSONObject reductionResponse = new JSONObject();
		reductionResponse.put("referenceReduction", referenceReduction);
		return reductionResponse.toString();
	}

	/**
	 * Ajouter reduction a l {@link ElementContractuel}.
	 * 
	 * @param referenceContrat
	 *            reference {@link Contrat}
	 * @param numEC
	 *            numero {@link ElementContractuel}.
	 * @param contratReduction
	 *            {@link ContratReduction}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws JSONException
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/{numEC:[0-9]*}/reduction/ajouter", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Object ajouterReductionElementContractuelle(@PathVariable String referenceContrat,
			@PathVariable Integer numEC, @RequestBody ContratReduction contratReduction)
			throws TopazeException, JSONException {
		LOGGER.info(":::ws-rec:::ajouterReduction");
		String referenceReduction =
				reductionService.ajouterReductionElementContractuelle(contratReduction, referenceContrat, numEC);
		JSONObject reductionResponse = new JSONObject();
		reductionResponse.put("referenceReduction", referenceReduction);
		return reductionResponse.toString();
	}

	/**
	 * utiliser une reduction pour un {@link Contrat} ou un {@link ElementContractuel}: augmenter le nombre
	 * d'utilisation de 1.
	 * 
	 * @param referenceContrat
	 *            reference {@link Contrat} ou {@link ElementContractuel}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/{numEC:[0-9]*}/reduction/utiliser", method = RequestMethod.POST)
	@ResponseBody
	public void utiliserReduction(@PathVariable String referenceContrat, @PathVariable Integer numEC)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::utiliserReduction");
		reductionService.utiliserReduction(referenceContrat, numEC);
	}

	/**
	 * Retourne les reduction associe a un element contractuel.
	 * 
	 * @param refrenceElementContractuel
	 *            reference {@link ElementContractuel}.
	 * @param version
	 *            version du contrat.
	 * @return les reduction associe a un {@link ElementContractuel}.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/{numEC:.+}/{version:.+}/reduction", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Object[] getReductionAssocie(@PathVariable String referenceContrat, @PathVariable Integer numEC,
			@PathVariable Integer version) throws TopazeException {
		LOGGER.info(":::ws-rec:::getReductionAssocie");
		return reductionService.getReductionAssocie(referenceContrat, numEC, version).toArray();
	}

	/**
	 * Retourne les reduction associe a un element contractuel.
	 * 
	 * @param refrenceElementContractuel
	 *            reference {@link ElementContractuel}.
	 * @return les reduction associe a un {@link ElementContractuel}.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/{numEC:.+}/reduction", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Object[] getReductionAssocie(@PathVariable String referenceContrat, @PathVariable Integer numEC)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::getReductionAssocie");
		return reductionService.getReductionAssocie(referenceContrat, numEC, null).toArray();
	}

	/**
	 * annuler une reduction associe a un contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param idReduction
	 *            id du reduction.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/reduction/{referenceReduction:.+}/supprimer", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public void annulerReductionContrat(@PathVariable String referenceContrat, @PathVariable String referenceReduction)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::annulerReductionContrat");
		reductionService.annulerReductionContrat(referenceContrat, referenceReduction);
	}

	/**
	 * annuler une reduction associe a un element contractuelle.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param numEC
	 *            numero element contractuelle.
	 * @param idReduction
	 *            id reduction.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/{numEC:.+}/reduction/{referenceReduction:.+}/supprimer", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public void annulerReductionElementContractuelle(@PathVariable String referenceContrat,
			@PathVariable Integer numEC, @PathVariable String referenceReduction) throws TopazeException {
		LOGGER.info(":::ws-rec:::annulerReductionElementContractuelle");
		reductionService.annulerReductionElementContractuelle(referenceContrat, numEC, referenceReduction);

	}

	/**
	 * Chercher la liste de reduction valide pour un contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return {@link ReductionInterface}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceContrat:.+}/reductions", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<ReductionInterface> getReductionsValide(@PathVariable String referenceContrat) throws TopazeException {
		LOGGER.info(":::ws-rec:::getReductionsValide");
		return reductionService.getReductionsValide(referenceContrat);
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
