package com.nordnet.topaze.livraison.core.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.domain.ElementLivraison;
import com.nordnet.topaze.livraison.core.domain.TypeBonPreparation;
import com.nordnet.topaze.livraison.core.domain.TypeContrat;
import com.nordnet.topaze.livraison.core.jms.LivraisonMessagesSender;
import com.nordnet.topaze.livraison.core.jms.MigrationMessagesSender;
import com.nordnet.topaze.livraison.core.jms.RecuperationMessagesSender;
import com.nordnet.topaze.livraison.core.repository.BonPreparationRepository;
import com.nordnet.topaze.livraison.core.repository.ElementLivraisonRepository;
import com.nordnet.topaze.livraison.core.util.Constants;
import com.nordnet.topaze.livraison.core.util.LivraisonUtils;
import com.nordnet.topaze.livraison.core.util.PropertiesUtil;
import com.nordnet.topaze.livraison.core.util.spring.ApplicationContextHolder;
import com.nordnet.topaze.livraison.core.validator.BonPreparationValidator;
import com.nordnet.topaze.livraison.core.validator.ElementLivraisonValidator;
import com.nordnet.topaze.logger.service.TracageService;

/**
 * implementation du {@link BonRecuperationService}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Service("bonRecuperationService")
public class BonRecuperationServiceImpl implements BonRecuperationService {

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
	private RecuperationMessagesSender recuperationMessagesSender;

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
	@Override
	public ElementLivraison findElementRecuperation(String referenceER) throws TopazeException {
		BonPreparationValidator.verifierReferenceBP(referenceER);
		return elementLivraisonRepository.findERByReference(referenceER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void initierBR(String reference) throws TopazeException {
		BonPreparationValidator.verifierReferenceBP(reference);
		BonPreparation bonPreparation = bonPreparationRepository.findBPByReference(reference);
		// BonPreparationValidator.checkLivrer(reference, bonPreparation);
		BonPreparationValidator.checkExist(reference, bonPreparation);
		BonPreparation bonRecuperation = bonPreparationRepository.findBRByReference(reference);
		BonPreparationValidator.checkNonRecuperer(bonRecuperation, bonPreparation);
		if (bonRecuperation == null) {
			// une resiliation partiel n'a ete effectuer sur la bon de retour.
			bonRecuperation = new BonPreparation();
			bonRecuperation.setReference(bonPreparation.getReference());
			bonRecuperation.setIdClient(bonPreparation.getIdClient());
			bonRecuperation.setTypeBonPreparation(TypeBonPreparation.RETOUR);
		}
		bonRecuperation.setDateInitiation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		for (ElementLivraison elementLivraison : bonPreparation.getElementLivraisons()) {
			if (!elementLivraison.getTypeContrat().equals(TypeContrat.VENTE)) {
				ElementLivraison elementRetour =
						elementLivraisonRepository.findERByReference(elementLivraison.getReference());
				if (elementRetour == null) {

					elementRetour = LivraisonUtils.creerElementRetour(elementLivraison, bonRecuperation, false);

					bonRecuperation.getElementLivraisons().add(elementRetour);
				}
			}
		}

		bonPreparationRepository.save(bonRecuperation);

		recuperationMessagesSender.sendReturnInitiatedEvent(bonRecuperation);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initierER(String reference) throws TopazeException {
		ElementLivraisonValidator.verifierReferenceEL(reference);
		ElementLivraison elementRetour = elementLivraisonRepository.findERByReference(reference);
		ElementLivraisonValidator.checkNonRecuperer(elementRetour);
		ElementLivraison elementLivraison = elementLivraisonRepository.findByReference(reference);
		ElementLivraisonValidator.checkLivrer(reference, elementLivraison);
		BonPreparation bonPreparation = elementLivraison.getBonPreparationParent();
		BonPreparation bonRecuperation = bonPreparationRepository.findBRByReference(bonPreparation.getReference());
		// verifier si une resiliation partiel precedente a ete effectuer
		if (bonRecuperation == null) {
			// creer un bon de retour parent.
			bonRecuperation = new BonPreparation();
			bonRecuperation.setReference(bonPreparation.getReference());
			bonRecuperation.setIdClient(bonPreparation.getIdClient());
			bonRecuperation.setTypeBonPreparation(TypeBonPreparation.RETOUR);
			bonRecuperation.setDateInitiation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		}
		// creation d'un element de retour.
		elementRetour = LivraisonUtils.creerElementRetour(elementLivraison, bonRecuperation, true);

		Set<ElementLivraison> elementRetours = new HashSet<>();
		elementRetours.add(elementRetour);

		bonRecuperation.setElementLivraison(elementRetours);

		bonPreparationRepository.save(bonRecuperation);

		recuperationMessagesSender.sendSubReturnInitiatedEvent(bonRecuperation);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preparerBR(final BonPreparation bonRecuperation) throws TopazeException {
		// tracer l'opération de résiliation global
		getTracageService().ajouterTrace(Constants.PRODUCT, bonRecuperation.getReference(),
				"Retour du contrat " + bonRecuperation.getReference() + " – Préparation du retour",
				Constants.INTERNAL_USER);

		final BonPreparation localBonRecuperation =
				bonPreparationRepository.findBRByReference(bonRecuperation.getReference());
		if (localBonRecuperation.getDatePreparation() == null) {
			localBonRecuperation.setDatePreparation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		}

		Set<ElementLivraison> elementLivraisonRetours = new HashSet<>();
		for (ElementLivraison elementLivraisonRetour : localBonRecuperation.getElementLivraisons()) {
			// verifier si l'element de retour a ete resilier au paravent ou pas.
			if (elementLivraisonRetour.getDatePreparation() == null) {
				elementLivraisonRetour.setDatePreparation(PropertiesUtil.getInstance().getDateDuJour().toDate());
				elementLivraisonRetours.add(elementLivraisonRetour);
			}
		}

		bonPreparationRepository.save(localBonRecuperation);
		// n'envoyer que les element resilier au cours de la derniere opration de resiliation.
		localBonRecuperation.setElementLivraison(elementLivraisonRetours);
		// Envoyer un evenement pour faire la traduction d'un BR.
		recuperationMessagesSender.sendPrepareReturnEvent(localBonRecuperation);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preparerER(final BonPreparation bonRecuperation) throws TopazeException {
		final BonPreparation localBonRecuperation =
				bonPreparationRepository.findBRByReference(bonRecuperation.getReference());
		BonPreparationValidator.checkNonPrepare(localBonRecuperation);
		// recuperer le bon preparation associe au bon de recuperation.
		BonPreparation bonPreparation = bonPreparationRepository.findBPByReference(localBonRecuperation.getReference());
		// verifier si tout les element ont ete resilier.
		if (localBonRecuperation.getElementLivraisons().size() == bonPreparation.getElementRetours().size()) {
			localBonRecuperation.setDatePreparation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		}
		Set<ElementLivraison> elementLivraisons = new HashSet<>();
		for (ElementLivraison elementLivraisonRetour : localBonRecuperation.getElementLivraisons()) {
			if (elementLivraisonRetour.getDatePreparation() == null) {
				// tracer l'opération de résiliation global
				getTracageService()
						.ajouterTrace(
								Constants.PRODUCT,
								localBonRecuperation.getReference(),
								"Retour du contrat " + localBonRecuperation.getReference()
										+ " – Préparation du retour du produit "
										+ elementLivraisonRetour.getReferenceProduit(), Constants.INTERNAL_USER);
				elementLivraisonRetour.setDatePreparation(PropertiesUtil.getInstance().getDateDuJour().toDate());
				elementLivraisons.add(elementLivraisonRetour);
			}
		}

		bonPreparationRepository.save(localBonRecuperation);
		// Envoyer a la traduction quse les element qui ont ete resilier aucours de resiliation partiel.
		localBonRecuperation.setElementLivraison(elementLivraisons);
		// Envoyer un evenement pour faire la traduction d'un BR.
		recuperationMessagesSender.sendPrepareReturnEvent(localBonRecuperation);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marquerELRecupere(String referenceEL) throws TopazeException {
		ElementLivraisonValidator.verifierReferenceEL(referenceEL);
		ElementLivraison elementRetour = elementLivraisonRepository.findERByReference(referenceEL);
		ElementLivraisonValidator.checkExist(referenceEL, elementRetour);
		elementRetour.setDateRetourTermine(PropertiesUtil.getInstance().getDateDuJour().toDate());
		elementLivraisonRepository.save(elementRetour);

		// tracer l'opération de résiliation global
		getTracageService().ajouterTrace(
				Constants.PRODUCT,
				referenceEL.split("-")[0],
				"Retour du contrat " + referenceEL.split("-")[0] + " – Fin du retour du produit "
						+ elementRetour.getReferenceProduit(), Constants.INTERNAL_USER);
		if (elementRetour.getBonPreparationParent().getTypeBonPreparation().equals(TypeBonPreparation.MIGRATION)) {
			migrationMessagesSender.sendSubDeliveryMigrationReturnedEvent(elementRetour.getBonPreparationParent());
		} else if (elementRetour.isResiliationPartiel()) {
			Map<String, Object> recuperationEvent = new HashMap<>();
			recuperationEvent.put("referenceContrat", elementRetour.getBonPreparationParent().getReference());
			recuperationEvent.put("isResiliationPartiel", true);
			recuperationEvent.put("numEC", elementRetour.getNumEC());
			recuperationMessagesSender.sendReturnReturnedEvent(recuperationEvent);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marquerRecupere(String referenceBR) throws TopazeException {
		BonPreparationValidator.verifierReferenceBP(referenceBR);
		BonPreparation bonRecuperation = bonPreparationRepository.findBRByReference(referenceBR);
		BonPreparationValidator.checkPreparer(referenceBR, bonRecuperation);
		bonRecuperation.setDateRetourTermine(PropertiesUtil.getInstance().getDateDuJour().toDate());

		bonPreparationRepository.save(bonRecuperation);
		Map<String, Object> recuperationEvent = new HashMap<>();
		recuperationEvent.put("referenceContrat", bonRecuperation.getReference());
		recuperationEvent.put("numEC", null);

		// tracer l'opération de résiliation global
		getTracageService().ajouterTrace(Constants.PRODUCT, referenceBR,
				"Retour du contrat " + referenceBR + " – Fin du retour ", Constants.INTERNAL_USER);
		recuperationMessagesSender.sendReturnReturnedEvent(recuperationEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ElementLivraison> getBiensEnCoursRecuperation() {
		return elementLivraisonRepository.findBiensEnCoursRecuperation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ElementLivraison> getServicesEnCoursSuspension() {
		return elementLivraisonRepository.findServicesEnCoursSuspension();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ElementLivraison> getServicesSuspendu() {
		return elementLivraisonRepository.findServicesSuspendu();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<BonPreparation> getBRGlobalEncoursRecuperation() {
		return bonPreparationRepository.findBRGlobalEncoursRecuperation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BonPreparation findBRByReference(String referenceBP) throws TopazeException {
		return bonPreparationRepository.findBRByReference(referenceBP);
	}
}
