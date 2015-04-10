package com.nordnet.topaze.livraison.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nordnet.topaze.exception.TopazeException;
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
import com.nordnet.topaze.logger.service.TracageService;

/**
 * implementation du {@link BonPreparationServiceMock}.
 * 
 * @author akram_moncer
 * @author Denden-OUSSAMA
 * 
 */
@Service("bonPreparationService")
public class BonPreparationServiceImpl implements BonPreparationService {

	/**
	 * {@link BonPreparationRepository}.
	 */
	@Autowired
	private BonPreparationRepository bonPreparationRepository;

	/**
	 * {@link ElementLivraisonRepository}.
	 */
	@Autowired
	private ElementLivraisonRepository elementLivraisonRepository;

	/**
	 * {@link LivraisonMessagesSender}.
	 */
	@Autowired
	private LivraisonMessagesSender livraisonMessagesSender;

	/**
	 * {@link MigrationMessagesSender}.
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
	@Transactional
	@Override
	public void initierBP(BonPreparation bonPreparationGlobal) throws TopazeException {
		// tracer l'operation
		getTracageService().ajouterTrace(Constants.PRODUCT, bonPreparationGlobal.getReference(),
				"Livraison du contrat " + bonPreparationGlobal.getReference() + " – Préparation de la livraison",
				Constants.INTERNAL_USER);
		bonPreparationGlobal.setDateInitiation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		for (ElementLivraison elementLivraison : bonPreparationGlobal.getElementLivraisons()) {
			elementLivraison.setDateInitiation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		}
		bonPreparationRepository.save(bonPreparationGlobal);

		// envoyer un evenement a la fin de l'initiation.
		livraisonMessagesSender.sendDeliveryInitiatedEvent(bonPreparationGlobal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preparerBP(String referenceBPGlobal) throws TopazeException {
		BonPreparation bonPreparationGlobal = bonPreparationRepository.findBPByReference(referenceBPGlobal);
		bonPreparationGlobal.setDateDebutPreparation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		for (ElementLivraison elementLivraison : bonPreparationGlobal.getElementLivraisons()) {
			elementLivraison.setDateDebutPreparation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		}
		bonPreparationRepository.save(bonPreparationGlobal);

		// Envoyer un evenement pour faire la traduction d'un BP.
		livraisonMessagesSender.sendPrepareDeliveryEvent(bonPreparationGlobal);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marquerBienPreparer(BonPreparation bonPreparationGlobal) throws TopazeException {
		boolean bienPreparer = false;
		BonPreparation localBonPreparationGlobal = bonPreparationGlobal;
		for (ElementLivraison elementLivraison : localBonPreparationGlobal.getElementLivraisons()) {
			if (elementLivraison.getActeur().equals(OutilLivraison.NETDELIVERY)) {
				ElementLivraison elementLivraisonLocal = null;
				elementLivraisonLocal = elementLivraisonRepository.findByReference(elementLivraison.getReference());
				elementLivraisonLocal.setDatePreparation(PropertiesUtil.getInstance().getDateDuJour().toDate());
				elementLivraisonLocal.setCodeColis(elementLivraison.getCodeColis());
				elementLivraisonRepository.save(elementLivraisonLocal);
				// tracer l'operation
				getTracageService().ajouterTrace(Constants.PRODUCT, localBonPreparationGlobal.getReference(),
						"Préparation de la livraison du produit " + elementLivraison.getReferenceProduit(),
						Constants.INTERNAL_USER);
				bienPreparer = true;
			}
		}
		// Envoyer un evenement si un Bien est preparer.
		if (bienPreparer) {
			if (bonPreparationGlobal.getTypeBonPreparation().equals(TypeBonPreparation.LIVRAISON)) {
				localBonPreparationGlobal =
						bonPreparationRepository.findBPByReference(bonPreparationGlobal.getReference());
			} else if (bonPreparationGlobal.getTypeBonPreparation().equals(TypeBonPreparation.MIGRATION)) {
				localBonPreparationGlobal =
						bonPreparationRepository.findBMByReference(bonPreparationGlobal.getReference());
			}

			livraisonMessagesSender.sendSubDeliveryPreparedEvent(localBonPreparationGlobal);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marquerServicePreparer(ElementLivraison elementLivraison) throws TopazeException {
		ElementLivraison elementLivraisonLocal =
				elementLivraisonRepository.findByReferenceAndReferenceProduit(elementLivraison.getReference(),
						elementLivraison.getReferenceProduit());
		elementLivraisonLocal.setDatePreparation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		elementLivraisonLocal.setRetailerPackagerId(elementLivraison.getRetailerPackagerId());
		elementLivraisonRepository.save(elementLivraisonLocal);
		// tracer l'operation
		getTracageService().ajouterTrace(Constants.PRODUCT,
				elementLivraisonLocal.getBonPreparationParent().getReference(),
				"Préparation de la livraison du produit " + elementLivraison.getReferenceProduit(),
				Constants.INTERNAL_USER);
		livraisonMessagesSender.sendSubDeliveryPreparedEvent(elementLivraisonLocal.getBonPreparationParent());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marquerBPGlobalPrepare(String referenceBPGlobal) throws TopazeException {
		BonPreparation bonPreparationGlobal = bonPreparationRepository.findBPByReference(referenceBPGlobal);
		bonPreparationGlobal.setDatePreparation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		bonPreparationRepository.save(bonPreparationGlobal);
		// tracer l'operation
		getTracageService().ajouterTrace(Constants.PRODUCT, referenceBPGlobal,
				"Livraison du contrat " + referenceBPGlobal + " – Début de livraison", Constants.INTERNAL_USER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void marquerLivre(ElementLivraison elementLivraison) throws TopazeException {
		ElementLivraison foundElementLivraison =
				elementLivraisonRepository.findByReferenceAndReferenceProduit(elementLivraison.getReference(),
						elementLivraison.getReferenceProduit());
		foundElementLivraison.setDateLivraisonTermine(PropertiesUtil.getInstance().getDateDuJour().toDate());
		elementLivraisonRepository.save(foundElementLivraison);
		BonPreparation bonPreparationGlobal = foundElementLivraison.getBonPreparationParent();

		if (bonPreparationGlobal.getTypeBonPreparation().equals(TypeBonPreparation.MIGRATION)) {
			migrationMessagesSender.sendSubDeliveryMigrationDeliveredEvent(bonPreparationGlobal);
		} else {
			livraisonMessagesSender.sendSubDeliveryDeliveredEvent(bonPreparationGlobal);
			// tracer l'operation
			// getTracageService().ajouterTrace(Constants.INTERNAL_USER,
			// "Livraison du contrat " + elementLivraison.getReference() + " – Fin de livraison");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marquerLivre(String referenceBPGlobal) throws TopazeException {
		BonPreparation bonPreparationGlobal = bonPreparationRepository.findBPByReference(referenceBPGlobal);
		bonPreparationGlobal.setDateLivraisonTermine(PropertiesUtil.getInstance().getDateDuJour().toDate());
		bonPreparationRepository.save(bonPreparationGlobal);
		livraisonMessagesSender.sendDeliveryDeliveredEvent(bonPreparationGlobal.getReference());
		// tracer l'operation
		getTracageService().ajouterTrace(Constants.PRODUCT, referenceBPGlobal,
				"Livraison du contrat " + referenceBPGlobal + " – Fin de livraison", Constants.INTERNAL_USER);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void marquerSousBPNonLivre(String referenceSousBP, String causeNonLivraison) throws TopazeException {
		ElementLivraison elementLivraison = elementLivraisonRepository.findByReference(referenceSousBP);
		if (elementLivraison != null) {
			elementLivraison.setCauseNonlivraison(causeNonLivraison);
			BonPreparation bonPreparationGlobal = elementLivraison.getBonPreparationParent();
			String appender = "";
			if (!Utils.isStringNullOrEmpty(bonPreparationGlobal.getCauseNonlivraison())) {
				appender += bonPreparationGlobal.getCauseNonlivraison() + ", ";
			}
			bonPreparationGlobal.setCauseNonlivraison(appender + elementLivraison.getReference() + ": "
					+ elementLivraison.getCauseNonlivraison());
			elementLivraisonRepository.save(elementLivraison);
			bonPreparationRepository.save(bonPreparationGlobal);
			BonPreparationValidator.alertBPNonLivrer(bonPreparationGlobal);
		} else {
			/*
			 * marquer le bon non livrer 'generalement ce bloc est appele si il y a des erreurs technique du coté
			 * packager/netdelivery'.
			 */
			BonPreparation bonPreparation = bonPreparationRepository.findBPByReference(referenceSousBP);
			if (bonPreparation != null) {
				bonPreparation.setCauseNonlivraison(causeNonLivraison);
				bonPreparationRepository.save(bonPreparation);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void annulerBonPreparation(final String referenceBPGlobal) throws TopazeException {
		BonPreparation bonPreparation = bonPreparationRepository.findBPByReference(referenceBPGlobal);
		BonPreparationValidator.checkExist(referenceBPGlobal, bonPreparation);
		BonPreparationValidator.validerAnnulerBonPreparation(bonPreparation);
		bonPreparation.annuler();
		bonPreparationRepository.save(bonPreparation);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ElementLivraison> getServicesEnCoursActivation() {
		return elementLivraisonRepository.findServicesEnCoursActivation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ElementLivraison> getBiensEnCoursLivraison() {
		return elementLivraisonRepository.findBiensEnCoursLivraison();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public BonPreparation findByReference(String reference) throws TopazeException {
		BonPreparationValidator.verifierReferenceBP(reference);
		return bonPreparationRepository.findBPByReference(reference);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public ElementLivraison findElementLivraisonByReferenceAndReferenceProduit(String reference, String referenceProduit)
			throws TopazeException {
		return elementLivraisonRepository.findByReferenceAndReferenceProduit(reference, referenceProduit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BonPreparation findBPByReference(String referenceBP) throws TopazeException {
		return bonPreparationRepository.findBPByReference(referenceBP);
	}

	@Override
	public String getReferenceBonPreparationParent(String referenceEL, String referenceProduit, Boolean isRetour)
			throws TopazeException {
		ElementLivraison elementLivraison = null;
		if (isRetour) {
			elementLivraison = elementLivraisonRepository.findERByReference(referenceEL);
		} else {
			elementLivraison =
					elementLivraisonRepository.findByReferenceAndReferenceProduit(referenceEL, referenceProduit);
		}

		if (elementLivraison != null) {
			return elementLivraison.getBonPreparationParent().getReference();
		}
		return null;
	}
}