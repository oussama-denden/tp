package com.nordnet.topaze.livraison.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.livraison.core.business.ElementStateInfo;
import com.nordnet.topaze.livraison.core.business.ElementsRenouvellemtnInfo;
import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.domain.ElementLivraison;
import com.nordnet.topaze.livraison.core.domain.OutilLivraison;
import com.nordnet.topaze.livraison.core.domain.TypeBonPreparation;
import com.nordnet.topaze.livraison.core.jms.LivraisonMessagesSender;
import com.nordnet.topaze.livraison.core.jms.MigrationMessagesSender;
import com.nordnet.topaze.livraison.core.repository.BonPreparationRepository;
import com.nordnet.topaze.livraison.core.repository.ElementLivraisonRepository;
import com.nordnet.topaze.livraison.core.util.Constants;
import com.nordnet.topaze.livraison.core.util.PropertiesUtil;
import com.nordnet.topaze.livraison.core.util.Utils;
import com.nordnet.topaze.livraison.core.util.spring.ApplicationContextHolder;
import com.nordnet.topaze.livraison.core.validator.BonPreparationValidator;
import com.nordnet.topaze.livraison.core.validator.ElementLivraisonValidator;
import com.nordnet.topaze.logger.service.TracageService;

/**
 * implementation du {@link BonMigrationService}.
 * 
 * @author Oussama Denden
 * 
 */
@Service("bonMigrationService")
public class BonMigrationServiceImpl implements BonMigrationService {

	/**
	 * {@link ElementLivraisonRepository}.
	 */
	@Autowired
	private ElementLivraisonRepository elementLivraisonRepository;

	/**
	 * {@link BonPreparationRepository}.
	 */
	@Autowired
	private BonPreparationRepository bonPreparationRepository;

	/**
	 * {@link LivraisonMessagesSender}.
	 */
	@Autowired
	private MigrationMessagesSender migrationMessagesSender;

	/**
	 * {@link TracageService}.
	 */
	private TracageService tracageService;

	/**
	 * 
	 * @return {@link TracageService}.
	 */
	private TracageService getTracageService() {
		if (tracageService == null) {
			if (System.getProperty("log.useMock").equals("true")) {
				tracageService = (TracageService) ApplicationContextHolder.getBean("tracageServiceMock");
			} else {
				tracageService = (TracageService) ApplicationContextHolder.getBean("tracageService");
			}
		}
		return tracageService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initierBM(BonPreparation bonMigration) throws TopazeException {
		// tracer l'operation
		getTracageService().ajouterTrace(Constants.PRODUCT, bonMigration.getReference(),
				"Migration du contrat " + bonMigration.getReference() + " – Initiation de la migration",
				Constants.INTERNAL_USER);
		bonMigration.setDateInitiation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		for (ElementLivraison elementLivraison : bonMigration.getElementLivraisons()) {
			elementLivraison.setDateInitiation(PropertiesUtil.getInstance().getDateDuJour().toDate());
			if (elementLivraison.getTypeBonPreparation().equals(TypeBonPreparation.MIGRATION)) {
				ElementLivraison ancienEL = elementLivraisonRepository.findByReference(elementLivraison.getReference());
				elementLivraison.setAncienCodeColis(ancienEL.getCodeColis());
				elementLivraison.setAncienRetailerPackagerId(ancienEL.getRetailerPackagerId());
			} else if (elementLivraison.getTypeBonPreparation().equals(TypeBonPreparation.RETOUR)) {
				ElementLivraison ancienER = elementLivraisonRepository.findByReference(elementLivraison.getReference());
				elementLivraison.setCodeColis(ancienER.getCodeColis());
				elementLivraison.setRetailerPackagerId(ancienER.getRetailerPackagerId());
				elementLivraison.setResiliationPartiel(false);
			}
		}
		bonPreparationRepository.save(bonMigration);

		// envoyer un evenement a la fin de l'initiation.
		migrationMessagesSender.sendMigrationInitiatedEvent(bonMigration);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preparerBM(String referenceBM) throws TopazeException {

		BonPreparation bonMigration = bonPreparationRepository.findBMByReference(referenceBM);
		bonMigration.setDateDebutPreparation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		boolean migrationPrepared = false;
		for (ElementLivraison elementLivraison : bonMigration.getElementLivraisons()) {
			elementLivraison.setDateDebutPreparation(PropertiesUtil.getInstance().getDateDuJour().toDate());
			// tracer l'operation
			getTracageService().ajouterTrace(
					Constants.PRODUCT,
					referenceBM,
					"Migration du contrat " + referenceBM + "– Initiation de la migration du produit "
							+ elementLivraison.getReferenceAncienProduit() + " vers le produit "
							+ elementLivraison.getReferenceProduit(), Constants.INTERNAL_USER);
			if (elementLivraison.getTypeBonPreparation().equals(TypeBonPreparation.RETOUR)
					|| (elementLivraison.getTypeBonPreparation().equals(TypeBonPreparation.MIGRATION) && elementLivraison
							.getActeur().equals(OutilLivraison.PACKAGER))) {
				elementLivraison.setDatePreparation(PropertiesUtil.getInstance().getDateDuJour().toDate());

			}
			if (elementLivraison.getDatePreparation() == null) {
				migrationPrepared = true;
			}
		}
		if (!migrationPrepared) {
			marquerBMGlobalPrepare(referenceBM);
		}
		bonPreparationRepository.save(bonMigration);

		// Envoyer un evenement pour faire la traduction d'un BP.
		migrationMessagesSender.sendPrepareMigrationEvent(bonMigration);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marquerBMGlobalPrepare(String referenceBMGlobal) throws TopazeException {
		// tracer l'operation
		getTracageService().ajouterTrace(Constants.PRODUCT, referenceBMGlobal,
				"Migration du contrat " + referenceBMGlobal + " – Début de la migration", Constants.INTERNAL_USER);

		BonPreparation bonMigrationGlobal = bonPreparationRepository.findBMByReference(referenceBMGlobal);
		bonMigrationGlobal.setDatePreparation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		bonPreparationRepository.save(bonMigrationGlobal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marquerBienMigrationPreparer(final BonPreparation bonMigration) throws TopazeException {
		boolean bienMigrationPreparer = false;
		for (ElementLivraison elementLivraison : bonMigration.getElementLivraisons()) {
			if (elementLivraison.getActeur().equals(OutilLivraison.SWAP)) {
				ElementLivraison elementLivraisonLocal =
						elementLivraisonRepository.findEMByReference(elementLivraison.getReference());
				elementLivraisonLocal.setDatePreparation(PropertiesUtil.getInstance().getDateDuJour().toDate());
				elementLivraisonLocal.setCodeColis(elementLivraison.getCodeColis());
				elementLivraisonRepository.save(elementLivraisonLocal);

				bienMigrationPreparer = true;
			}
		}
		// Envoyer un evenement si un Bien est preparer.
		if (bienMigrationPreparer) {
			final BonPreparation foundBonMigration =
					bonPreparationRepository.findBMByReference(bonMigration.getReference());
			migrationMessagesSender.sendSubDeliveryPreparedEvent(foundBonMigration);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public BonPreparation findBMByReference(String reference) throws TopazeException {
		BonPreparationValidator.verifierReferenceBP(reference);
		return bonPreparationRepository.findBMByReference(reference);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public ElementLivraison findEMByReference(String reference) throws TopazeException {
		BonPreparationValidator.verifierReferenceBP(reference);
		return elementLivraisonRepository.findEMByReference(reference);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ElementLivraison> getBiensMigrationEnCoursLivraison() {
		return elementLivraisonRepository.findBiensMigrationEnCoursLivraison();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void marquerEMLivre(ElementLivraison elementLivraison) throws TopazeException {

		ElementLivraison el = elementLivraisonRepository.findEMByReference(elementLivraison.getReference());
		el.setDateLivraisonTermine(PropertiesUtil.getInstance().getDateDuJour().toDate());
		if (elementLivraison.getActeur() == OutilLivraison.PACKAGER
				&& elementLivraison.getTypeBonPreparation() == TypeBonPreparation.MIGRATION) {
			el.setRetailerPackagerId(elementLivraison.getRetailerPackagerId());
		}
		// tracer l'operation
		getTracageService().ajouterTrace(
				Constants.PRODUCT,
				el.getBonPreparationParent().getReference(),
				"Migration du contrat " + el.getBonPreparationParent().getReference()
						+ "– Fin  de la migration du produit " + elementLivraison.getReferenceAncienProduit()
						+ " vers le produit " + elementLivraison.getReferenceProduit(), Constants.INTERNAL_USER);
		elementLivraisonRepository.save(el);
		BonPreparation bonPreparationGlobal = el.getBonPreparationParent();
		migrationMessagesSender.sendSubDeliveryMigrationDeliveredEvent(bonPreparationGlobal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marquerBMGlobalLivre(String referenceBM) throws TopazeException {
		BonPreparationValidator.verifierReferenceBP(referenceBM);
		BonPreparation bonMigration = bonPreparationRepository.findBMByReference(referenceBM);
		BonPreparationValidator.checkPreparer(referenceBM, bonMigration);
		bonMigration.setDateLivraisonTermine(PropertiesUtil.getInstance().getDateDuJour().toDate());
		bonPreparationRepository.save(bonMigration);
		// tracer l'opération
		getTracageService().ajouterTrace(Constants.PRODUCT, referenceBM,
				"Retour du contrat " + referenceBM + " – Tous les produits ont été livrés", Constants.INTERNAL_USER);
		migrationMessagesSender.sendMigrationDelivredEvent(referenceBM);

	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public BonPreparation findByReference(String reference) throws TopazeException {
		BonPreparationValidator.verifierReferenceBP(reference);
		return bonPreparationRepository.findByReference(reference);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ElementLivraison> getBiensMigrationEnCoursRetour() {
		return elementLivraisonRepository.findBiensMigrationEnCoursRetour();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marquerEMRetourne(final ElementLivraison elementLivraison) throws TopazeException {
		ElementLivraison foundElementLivraison =
				elementLivraisonRepository.findEMByReference(elementLivraison.getReference());
		foundElementLivraison.setDateRetourTermine(PropertiesUtil.getInstance().getDateDuJour().toDate());
		elementLivraisonRepository.save(foundElementLivraison);
		// tracer l'opération
		getTracageService().ajouterTrace(
				Constants.PRODUCT,
				foundElementLivraison.getBonPreparationParent().getReference(),
				"Retour du contrat " + foundElementLivraison.getBonPreparationParent().getReference()
						+ " – Fin du retour du produit " + foundElementLivraison.getReferenceProduit(),
				Constants.INTERNAL_USER);
		BonPreparation bonPreparationGlobal = foundElementLivraison.getBonPreparationParent();
		migrationMessagesSender.sendSubDeliveryMigrationReturnedEvent(bonPreparationGlobal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marquerBMGlobalRetourne(String referenceBM) throws TopazeException {
		BonPreparationValidator.verifierReferenceBP(referenceBM);
		BonPreparation bonMigration = bonPreparationRepository.findBMByReference(referenceBM);
		BonPreparationValidator.checkPreparer(referenceBM, bonMigration);
		bonMigration.setDateRetourTermine(PropertiesUtil.getInstance().getDateDuJour().toDate());
		bonPreparationRepository.save(bonMigration);
		// tracer l'opération
		getTracageService().ajouterTrace(Constants.PRODUCT, referenceBM,
				"Retour du contrat " + referenceBM + " – Tous les produits ont été retournés", Constants.INTERNAL_USER);
		migrationMessagesSender.sendMigrationReturnedBillingEvent(referenceBM);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initierBC(BonPreparation bonPreparation) throws TopazeException {
		BonPreparation bonLivraison = bonPreparationRepository.findBPByReference(bonPreparation.getReference());
		getTracageService().ajouterTrace(
				Constants.PRODUCT,
				bonPreparation.getReference(),
				"Cession du bonPreparation " + bonPreparation.getReference() + " du client "
						+ bonLivraison.getIdClient() + " au client " + bonPreparation.getIdClient()
						+ " – Initiation de la cession", Constants.INTERNAL_USER);
		bonPreparation.setDateInitiation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		for (ElementLivraison elementLivraison : bonPreparation.getElementLivraisons()) {
			elementLivraison.setDateInitiation(PropertiesUtil.getInstance().getDateDuJour().toDate());
			elementLivraison.setBonPreparationParent(bonPreparation);
		}

		bonPreparationRepository.save(bonPreparation);
		migrationMessagesSender.sendPrepareSuccessionEvent(bonPreparation);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void marquerECCede(String referenceElementCession) throws TopazeException {

		ElementLivraison elementCession = elementLivraisonRepository.findECByReference(referenceElementCession);
		getTracageService().ajouterTrace(Constants.PRODUCT, elementCession.getBonPreparationParent().getReference(),
				"cession de l'element " + referenceElementCession, Constants.INTERNAL_USER);
		ElementLivraisonValidator.checkExist(referenceElementCession, elementCession);
		elementCession.setDateLivraisonTermine(PropertiesUtil.getInstance().getDateDuJour().toDate());
		elementLivraisonRepository.save(elementCession);
		migrationMessagesSender.sendSubDeliverySuccessedEvent(elementCession.getBonPreparationParent().getReference());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marquerBCCede(String referenceBonCession) throws TopazeException {
		getTracageService().ajouterTrace(Constants.PRODUCT, referenceBonCession,
				"cession du bon preparation " + referenceBonCession, Constants.INTERNAL_USER);
		BonPreparation bonPreparation = bonPreparationRepository.findBCByReference(referenceBonCession);
		BonPreparationValidator.checkExist(referenceBonCession, bonPreparation);
		bonPreparation.setDateLivraisonTermine(PropertiesUtil.getInstance().getDateDuJour().toDate());
		bonPreparationRepository.save(bonPreparation);
		migrationMessagesSender.sendDeliverySuccessedEvent(referenceBonCession);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BonPreparation findBCByReference(String referenceBC) {
		return bonPreparationRepository.findBCByReference(referenceBC);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ElementStateInfo> getElementsCodeProduit(ElementsRenouvellemtnInfo elementsRenouvellemtnInfo)
			throws TopazeException {

		return getTheLastElement(elementsRenouvellemtnInfo, false, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ElementStateInfo> getElementsCodeColis(ElementsRenouvellemtnInfo elementsRenouvellemtnInfo)
			throws TopazeException {
		List<ElementStateInfo> elementStateInfos = elementsRenouvellemtnInfo.getElementStateInfos();

		if (elementStateInfos.size() > 0) {

			// recuperer le code colis
			elementStateInfos = getTheLastElement(elementsRenouvellemtnInfo, true, false);

			// tester si un el existe dans un bon de recuperation et tester s'il est prépare pour le retour
			for (ElementStateInfo elementStateInfo : elementStateInfos) {
				List<ElementLivraison> elementRecuperations =
						elementLivraisonRepository.findByreferenceOrderBydateRetourTermine(elementStateInfo
								.getRefenceElementContractuelle());
				if (elementRecuperations.size() > 0) {
					ElementLivraison elementRecuperation = elementRecuperations.get(0);
					if (elementRecuperation != null && elementRecuperation.getDatePreparation() != null
							&& !elementRecuperation.getBonPreparationParent().isTermine()) {
						elementStateInfo.setPreparerPourRetour(true);

					} else {
						elementStateInfo.setPreparerPourRetour(false);
					}

				} else {
					elementStateInfo.setPreparerPourRetour(false);
				}
			}

		}
		return elementStateInfos;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initierBRE(BonPreparation bonRenouvellement) throws TopazeException {

		// tracer l'operation
		getTracageService().ajouterTrace(Constants.PRODUCT, bonRenouvellement.getReference(),
				"renouvellement du contrat " + bonRenouvellement.getReference() + " – Initiation de le renouvellement",
				Constants.INTERNAL_USER);
		bonRenouvellement.setDateInitiation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		for (ElementLivraison elementLivraison : bonRenouvellement.getElementLivraisons()) {
			elementLivraison.setDateInitiation(PropertiesUtil.getInstance().getDateDuJour().toDate());
			List<ElementLivraison> ancienELList =
					elementLivraisonRepository.findByreferenceOrderBydateLivraisonTermine(elementLivraison
							.getReference());
			if (ancienELList.size() != 0) {
				ElementLivraison ancienEL = ancienELList.get(Constants.ZERO);
				elementLivraison.setCodeColis(ancienEL.getCodeColis());
				elementLivraison.setRetailerPackagerId(ancienEL.getRetailerPackagerId());
			}
		}

		bonPreparationRepository.save(bonRenouvellement);

		// envoyer un evenement a la fin de l'initiation.
		migrationMessagesSender.sendPrepareRenewalEvent(bonRenouvellement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marquerERRenouvele(String referenceElementRenouvellement) throws TopazeException {
		ElementLivraison elementRenouvellement =
				elementLivraisonRepository.findEREByReference(referenceElementRenouvellement);
		getTracageService().ajouterTrace(Constants.PRODUCT,
				elementRenouvellement.getBonPreparationParent().getReference(),
				"renouvellement de l'element " + referenceElementRenouvellement, Constants.INTERNAL_USER);
		ElementLivraisonValidator.checkExist(referenceElementRenouvellement, elementRenouvellement);
		elementRenouvellement.setDateLivraisonTermine(PropertiesUtil.getInstance().getDateDuJour().toDate());
		elementLivraisonRepository.save(elementRenouvellement);
		migrationMessagesSender.sendSubDeliveryRenewedEvent(elementRenouvellement.getBonPreparationParent()
				.getReference());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BonPreparation findBREByReference(String referenceBC) {
		return bonPreparationRepository.findBREByReference(referenceBC);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marquerBPGlobalRenouvele(String referenceBonRenouvellement) throws TopazeException {
		getTracageService().ajouterTrace(Constants.PRODUCT, referenceBonRenouvellement,
				"renouvellement du bon preparation " + referenceBonRenouvellement, Constants.INTERNAL_USER);
		BonPreparation bonPreparation = bonPreparationRepository.findBREByReference(referenceBonRenouvellement);
		BonPreparationValidator.checkExist(referenceBonRenouvellement, bonPreparation);
		bonPreparation.setDateLivraisonTermine(PropertiesUtil.getInstance().getDateDuJour().toDate());
		bonPreparationRepository.save(bonPreparation);
		migrationMessagesSender.sendDeliveryRenewedEvent(referenceBonRenouvellement);

	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void marquerNonMigre(String referenceSousBP, String causeNonLivraison) throws TopazeException {
		ElementLivraison elementMigration = elementLivraisonRepository.findEMByReference(referenceSousBP);
		if (elementMigration != null) {
			elementMigration.setCauseNonlivraison(causeNonLivraison);
			BonPreparation bonMigrationGlobal = elementMigration.getBonPreparationParent();
			String appender = "";
			if (!Utils.isStringNullOrEmpty(bonMigrationGlobal.getCauseNonlivraison())) {
				appender += bonMigrationGlobal.getCauseNonlivraison() + ", ";
			}
			bonMigrationGlobal.setCauseNonlivraison(appender + elementMigration.getReference() + ": "
					+ elementMigration.getCauseNonlivraison());
			elementLivraisonRepository.save(elementMigration);
			bonPreparationRepository.save(bonMigrationGlobal);
			BonPreparationValidator.alertBPNonLivrer(bonMigrationGlobal);
		} else {
			/*
			 * marquer le bon non livrer 'generalement ce bloc est appele si il y a des erreurs technique du coté
			 * packager/netdelivery'.
			 */
			BonPreparation bonMigration = bonPreparationRepository.findBMByReference(referenceSousBP);
			if (bonMigration != null) {
				bonMigration.setCauseNonlivraison(causeNonLivraison);
				bonPreparationRepository.save(bonMigration);
			}
		}
	}

	/**
	 * recuperer les codes produits de derniere bon de livraison ou migration.
	 * 
	 * @param elementsRenouvellemtnInfo
	 *            {@link ElementsRenouvellemtnInfo}.
	 * @param codeColis
	 *            ture si code colis existe.
	 * @param codeProduit
	 *            true si code produit existe.
	 * @return liste des {@link ElementStateInfo}.
	 */
	private List<ElementStateInfo> getTheLastElement(ElementsRenouvellemtnInfo elementsRenouvellemtnInfo,
			boolean codeColis, boolean codeProduit) {

		List<ElementStateInfo> elementStateInfos = elementsRenouvellemtnInfo.getElementStateInfos();
		if (elementStateInfos.size() > 0) {
			for (ElementStateInfo elementStateInfo : elementStateInfos) {
				List<ElementLivraison> elementLivraisons =
						elementLivraisonRepository.findByreferenceOrderBydateLivraisonTermine(elementStateInfo
								.getRefenceElementContractuelle());
				if (elementLivraisons.size() > 0 && codeProduit) {
					elementStateInfo.setCodeProduit(elementLivraisons.get(Constants.ZERO).getRetailerPackagerId());
				} else if (elementLivraisons.size() > 0 && codeColis) {
					elementStateInfo.setCodeColis(elementLivraisons.get(Constants.ZERO).getCodeColis());
				}

			}

		}
		return elementStateInfos;

	}
}
