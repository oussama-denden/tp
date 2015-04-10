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
import com.nordnet.topaze.livraison.core.business.ElementStateInfo;
import com.nordnet.topaze.livraison.core.business.ElementsRenouvellemtnInfo;
import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.domain.ElementLivraison;
import com.nordnet.topaze.livraison.core.service.BonMigrationService;
import com.nordnet.topaze.livraison.core.util.Constants;
import com.nordnet.topaze.livraison.core.util.PropertiesUtil;
import com.wordnik.swagger.annotations.Api;

/**
 * Gerer l'ensemble des requetes qui ont en rapport avec la migration.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Api(value = "migration", description = "migration")
@Controller
@RequestMapping("/migration")
public class MigrationController {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(MigrationController.class);

	/**
	 * {@link ProduitService} service.
	 */
	@Autowired
	private BonMigrationService bonMigrationService;

	/**
	 * Alert service.
	 */
	@Autowired
	SendAlert sendAlert;

	/**
	 * Chercher un bon de migration par reference.
	 * 
	 * @param referenceBM
	 *            reference du BM.
	 * @return {@link BonPreparation} de migration.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	@RequestMapping(value = "/search/{referenceBM:.+}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public BonPreparation getBonMigration(@PathVariable String referenceBM) throws TopazeException {
		LOGGER.info(":::ws-rec:::getBonPreparation");
		return bonMigrationService.findBMByReference(referenceBM);
	}

	/**
	 * initiation d'un bon du migration à partir d'un bon de migration envoyer depuis la business process.
	 * 
	 * @param bonMigration
	 *            un {@link BonPreparation} global cree par la Business Process.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/initier", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public void initierBM(@RequestBody BonPreparation bonMigration) throws TopazeException {
		LOGGER.info(":::ws-rec:::initierBM");
		for (ElementLivraison elementLivraison : bonMigration.getElementLivraisons()) {
			elementLivraison.setBonPreparationParent(bonMigration);
		}
		bonMigrationService.initierBM(bonMigration);
	}

	/**
	 * commencer la preparation des bon de preparation.
	 * 
	 * @param referenceBM
	 *            la reference du bon de preparation global a preparer.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceBM:.+}/preparer", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void preparerBM(@PathVariable String referenceBM) throws TopazeException {
		LOGGER.info(":::ws-rec:::preparerBM");
		bonMigrationService.preparerBM(referenceBM);
	}

	/**
	 * marquer les biens a migre comme prepare.
	 * 
	 * @param bonMigration
	 *            le {@link BonPreparation} global dont les biens a migre seront marquer comme prepare.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/marquerBienMigrationPreparer", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void marquerBienMigrationPreparer(@RequestBody BonPreparation bonMigration) throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerBienMigrationPreparer");
		bonMigrationService.marquerBienMigrationPreparer(bonMigration);
	}

	/**
	 * marquer le bon de migration comme prepare.
	 * 
	 * @param referenceBMGlobal
	 *            reference du bon de migration global prepare.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceBMGlobal:.+}/marquerPrepare", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void marquerBMGlobalPrepare(@PathVariable String referenceBMGlobal) throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerBMGlobalPrepare");
		bonMigrationService.marquerBMGlobalPrepare(referenceBMGlobal);
	}

	/**
	 * retourne un tableau des sous {@link ElementLivraison} migration de type bien en cours de livraison.
	 * 
	 * @return tableau des sous {@link ElementLivraison} migration de type bien en cours de livraison.
	 */
	@RequestMapping(value = "/biensEnCoursMigration", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ElementLivraison[] getBiensEnCoursMigration() {
		LOGGER.info(":::ws-rec:::getBiensEnCoursMigration");
		List<ElementLivraison> elementLivraisonsList = bonMigrationService.getBiensMigrationEnCoursLivraison();
		ElementLivraison[] elementLivraisonsArray = new ElementLivraison[elementLivraisonsList.size()];
		for (int i = 0; i < elementLivraisonsList.size(); i++) {
			elementLivraisonsArray[i] = elementLivraisonsList.get(i);
		}
		return elementLivraisonsArray;
	}

	/**
	 * marquer un élément de migration comme livré.
	 * 
	 * @param elementLivraison
	 *            {@link #ElementLivraison}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/marquerEMLivre", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void marquerEMLivre(@RequestBody ElementLivraison elementLivraison) throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerEMLivre");
		bonMigrationService.marquerEMLivre(elementLivraison);
	}

	/**
	 * Marquer un BM comme Livre.
	 * 
	 * @param referenceBM
	 *            reference de BM.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceBM:.+}/marquerBMLivre", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void marquerBMLivre(@PathVariable String referenceBM) throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerBMLivre");
		bonMigrationService.marquerBMGlobalLivre(referenceBM);
	}

	/**
	 * retourne un tableau des sous {@link ElementLivraison} migration de type bien en cours de retour.
	 * 
	 * @return tableau des sous {@link ElementLivraison} migration de type bien en cours de livraison.
	 */
	@RequestMapping(value = "/biensEnCoursRetour", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ElementLivraison[] getBiensEnCoursRetour() {
		LOGGER.info(":::ws-rec:::getBiensEnCoursRetour");
		List<ElementLivraison> elementLivraisonsList = bonMigrationService.getBiensMigrationEnCoursRetour();
		ElementLivraison[] elementLivraisonsArray = new ElementLivraison[elementLivraisonsList.size()];
		for (int i = 0; i < elementLivraisonsList.size(); i++) {
			elementLivraisonsArray[i] = elementLivraisonsList.get(i);
		}
		return elementLivraisonsArray;
	}

	/**
	 * Marquer un BM comme retourne.
	 * 
	 * @param referenceBM
	 *            reference de BM.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceBM:.+}/marquerBMRetourne", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void marquerBMRetourne(@PathVariable String referenceBM) throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerBMLivre");
		bonMigrationService.marquerBMGlobalRetourne(referenceBM);
	}

	/**
	 * marquer un élément de migration comme retourné.
	 * 
	 * @param elementLivraison
	 *            {@link #ElementLivraison}.
	 * @throws TopazeException
	 *             {@link #TopazeException}. {@link TopazeException}
	 */
	@RequestMapping(value = "/marquerEMRetourne", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void marquerEMRetourne(@RequestBody ElementLivraison elementLivraison) throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerEMRetourne");
		bonMigrationService.marquerEMRetourne(elementLivraison);
	}

	/**
	 * initier un bon de cession.
	 * 
	 * @param bonPreparation
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/initierBC", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void initierBC(@RequestBody BonPreparation bonPreparation) throws TopazeException {
		LOGGER.info(":::ws-rec:::initierBC");
		bonMigrationService.initierBC(bonPreparation);
	}

	/**
	 * marquer un {@link ElementLivraison} cede.
	 * 
	 * @param referenceElementCession
	 *            reference element cession.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceElementCession:.+}/marquerECCede", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void marquerECCede(@PathVariable String referenceElementCession) throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerECCede");
		bonMigrationService.marquerECCede(referenceElementCession);
	}

	/**
	 * marquer un {@link BonPreparation} cede.
	 * 
	 * @param referenceBonCession
	 *            reference bon cession.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceBonCession:.+}/marquerBCCede", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void marquerBCCede(@PathVariable String referenceBonCession) throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerBCCede");
		bonMigrationService.marquerBCCede(referenceBonCession);
	}

	/**
	 * Chercher un bon de cession par reference.
	 * 
	 * @param referenceBC
	 *            reference du BC.
	 * @return {@link BonPreparation}
	 */
	@RequestMapping(value = "/search/{referenceBC:.+}/cession", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public BonPreparation getBonCession(@PathVariable String referenceBC) {
		LOGGER.info(":::ws-rec:::getBonCession");
		return bonMigrationService.findBCByReference(referenceBC);
	}

	/**
	 * recuperer le code produit des elements contractuelles.
	 * 
	 * @param elementsRenouvellemtnInfo
	 *            info elements renouvellement.
	 * @return liste de {@link ElementStateInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/getElementsCodeProduit", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Object[] getElementsCodeProduit(@RequestBody ElementsRenouvellemtnInfo elementsRenouvellemtnInfo)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::getElementsCodeProduit");
		return bonMigrationService.getElementsCodeProduit(elementsRenouvellemtnInfo).toArray();
	}

	/**
	 * recuperer les codes colis et les etats des elemnets contractuelles.
	 * 
	 * @param elementsRenouvellemtnInfo
	 *            info elements renouvellement.
	 * @return liste de {@link ElementStateInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/getElementsCodeColis", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Object[] getElementsCodeColis(@RequestBody ElementsRenouvellemtnInfo elementsRenouvellemtnInfo)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::getElementsCodeProduit");
		return bonMigrationService.getElementsCodeColis(elementsRenouvellemtnInfo).toArray();
	}

	/**
	 * initiation d'un bon du renouvelement à partir d'un bon de renouvelement envoyer depuis la business process.
	 * 
	 * @param bonRenouvellement
	 *            un {@link BonPreparation} global cree par la Business Process.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/initierBRE", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public void initierBRE(@RequestBody BonPreparation bonRenouvellement) throws TopazeException {
		LOGGER.info(":::ws-rec:::initierBRE");
		for (ElementLivraison elementLivraison : bonRenouvellement.getElementLivraisons()) {
			elementLivraison.setBonPreparationParent(bonRenouvellement);
		}
		bonMigrationService.initierBRE(bonRenouvellement);
	}

	/**
	 * marquer un {@link ElementLivraison} renouvele.
	 * 
	 * @param referenceElementRenouvellement
	 *            reference des elements des renouvellements.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	@RequestMapping(value = "/{referenceElementRenouvellement:.+}/marquerERRenouvele", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void marquerERRenouvele(@PathVariable String referenceElementRenouvellement) throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerERRenouvele");
		bonMigrationService.marquerERRenouvele(referenceElementRenouvellement);
	}

	/**
	 * Chercher un bon de renouvellement par reference.
	 * 
	 * @param referenceBC
	 *            reference du BC.
	 * @return {@link BonPreparation}
	 */
	@RequestMapping(value = "/search/{referenceBC:.+}/renouvellement", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public BonPreparation getBonRenouvellement(@PathVariable String referenceBC) {
		LOGGER.info(":::ws-rec:::getBonRenouvellement");
		return bonMigrationService.findBREByReference(referenceBC);
	}

	/**
	 * marquer un {@link BonPreparation} renouvele.
	 * 
	 * @param referenceBonRenouvellement
	 *            reference bon renouvellement.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceBonRenouvellement:.+}/marquerBRERenouvele", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void marquerBPGlobalRenouvele(@PathVariable String referenceBonRenouvellement) throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerBPGlobalRenouvele");
		bonMigrationService.marquerBPGlobalRenouvele(referenceBonRenouvellement);

	}

	/**
	 * marquer un element/bon non migre.
	 * 
	 * @param reference
	 *            reference du bon/element migration.
	 * @param causeNonLivraison
	 *            cause de non livraison.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{reference:.+}/marquerNonMigre/{causeNonLivraison:.+}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void marquerNonLivre(@PathVariable String reference, @PathVariable String causeNonLivraison)
			throws TopazeException {
		LOGGER.info(":::ws-rec:::marquerNonMigre");
		bonMigrationService.marquerNonMigre(reference, causeNonLivraison);
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
