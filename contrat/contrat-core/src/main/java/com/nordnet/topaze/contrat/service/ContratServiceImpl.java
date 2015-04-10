package com.nordnet.topaze.contrat.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nordnet.topaze.client.rest.enums.TypeProduit;
import com.nordnet.topaze.contrat.business.ContratAvenant;
import com.nordnet.topaze.contrat.business.ContratAvenantInfo;
import com.nordnet.topaze.contrat.business.ContratBP;
import com.nordnet.topaze.contrat.business.ContratBillingInfo;
import com.nordnet.topaze.contrat.business.ContratChangerModePaiementInfo;
import com.nordnet.topaze.contrat.business.ContratModification;
import com.nordnet.topaze.contrat.business.ContratValidationInfo;
import com.nordnet.topaze.contrat.business.ElementContractuelInfo;
import com.nordnet.topaze.contrat.business.Frais;
import com.nordnet.topaze.contrat.business.PaiementInfo;
import com.nordnet.topaze.contrat.business.PaiementModificationInfo;
import com.nordnet.topaze.contrat.business.PolitiqueResiliation;
import com.nordnet.topaze.contrat.business.Produit;
import com.nordnet.topaze.contrat.business.ProduitMigration;
import com.nordnet.topaze.contrat.business.ReductionInfo;
import com.nordnet.topaze.contrat.domain.Actions;
import com.nordnet.topaze.contrat.domain.Avenant;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ContratHistorique;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.domain.ElementContractuelHistorique;
import com.nordnet.topaze.contrat.domain.FraisContrat;
import com.nordnet.topaze.contrat.domain.ModeFacturation;
import com.nordnet.topaze.contrat.domain.Modification;
import com.nordnet.topaze.contrat.domain.PolitiqueCession;
import com.nordnet.topaze.contrat.domain.PolitiqueMigration;
import com.nordnet.topaze.contrat.domain.PolitiqueRenouvellement;
import com.nordnet.topaze.contrat.domain.Reduction;
import com.nordnet.topaze.contrat.domain.StatutContrat;
import com.nordnet.topaze.contrat.domain.TypeAvenant;
import com.nordnet.topaze.contrat.domain.TypeContrat;
import com.nordnet.topaze.contrat.domain.TypeFrais;
import com.nordnet.topaze.contrat.jms.ContratMessagesSender;
import com.nordnet.topaze.contrat.repository.AvenantRepository;
import com.nordnet.topaze.contrat.repository.ContratHistoriqueRepository;
import com.nordnet.topaze.contrat.repository.ContratRepository;
import com.nordnet.topaze.contrat.repository.ElementContractuelHistoriqueRepository;
import com.nordnet.topaze.contrat.repository.ElementContractuelRepository;
import com.nordnet.topaze.contrat.repository.ReductionRepository;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.ContratUtils;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.contrat.util.Utils;
import com.nordnet.topaze.contrat.util.spring.ApplicationContextHolder;
import com.nordnet.topaze.contrat.validator.ContratValidator;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.logger.service.TracageService;

/**
 * L'implementation de service {@link ContratService}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Service("contratService")
public class ContratServiceImpl implements ContratService {

	/**
	 * {@link ContratRepository}.
	 */
	@Autowired
	private ContratRepository contratRepository;

	/**
	 * {@link ContratHistoriqueRepository}.
	 */
	@Autowired
	private ContratHistoriqueRepository contratHistoriqueRepository;

	/**
	 * {@link AvenantRepository}.
	 */
	@Autowired
	private AvenantRepository avenantRepository;

	/**
	 * {@link ElementContractuelRepository}.
	 */
	@Autowired
	private ElementContractuelRepository elementContractuelRepository;

	/**
	 * {@link ElementContractuelHistoriqueRepository}.
	 */
	@Autowired
	private ElementContractuelHistoriqueRepository elementContractuelHistoriqueRepository;

	/**
	 * {@link ContratMessagesSender}.
	 */
	@Autowired
	private ContratMessagesSender contratMessagesSender;

	/**
	 * {@link TracageService}.
	 */
	private TracageService tracageService;

	/**
	 * {@link ReductionRepository}.
	 */
	@Autowired
	private ReductionRepository reductionRepository;

	/**
	 * {@link KeygenService}.
	 */
	@Autowired
	private KeygenService keygenService;

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
	public Contrat getContratByReference(String reference) throws TopazeException {
		Contrat contrat = contratRepository.findByReference(reference);
		ContratValidator.checkExist(reference, contrat);
		return contrat;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ElementContractuel preparerElementContractuel(Produit produit) throws TopazeException {
		ElementContractuel elementContractuel = new ElementContractuel();
		elementContractuel.setReferenceProduit(produit.getReference());
		elementContractuel.setReferenceTarif(produit.getReferenceTarif());
		elementContractuel.setNumEC(produit.getNumEC());
		elementContractuel.setNumeroCommande(produit.getNumeroCommande());
		elementContractuel.setTypeProduit(produit.getTypeProduit());
		elementContractuel.setRemboursable(produit.isRemboursable() == null ? true : produit.isRemboursable());

		if (produit.getPrix() != null) {
			elementContractuel.setTypeTVA(produit.getPrix().getTypeTVA());
			Set<FraisContrat> fraisContrats = new HashSet<>();
			for (Frais frais : produit.getPrix().getFrais()) {
				FraisContrat fraisContrat = new FraisContrat();
				fraisContrat.setMontant(frais.getMontant());
				fraisContrat.setTypeFrais(frais.getTypeFrais());
				fraisContrat.setNombreMois(frais.getNombreMois());
				fraisContrat.setOrdre(frais.getOrdre());
				fraisContrat.setTitre(frais.getTitre());
				fraisContrats.add(fraisContrat);
			}
			elementContractuel.setFrais(fraisContrats);
			elementContractuel.setMontant(produit.getPrix().getMontant());
			elementContractuel.setEngagement(produit.getPrix().getEngagement());
			elementContractuel.setPeriodicite(produit.getPrix().getPeriodicite());
			elementContractuel.setDuree(produit.getPrix().getDuree());
			elementContractuel.setModePaiement(produit.getPrix().getModePaiement());
			elementContractuel.setModeFacturation(produit.getPrix().getModeFacturation());
		}
		elementContractuel.setTitre(produit.getLabel());
		elementContractuel.setDatePreparation(PropertiesUtil.getInstance().getDateDuJour().toDate());

		return elementContractuel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Contrat preparerContrat(List<Produit> produits, String user, boolean isMigration, String refContrat,
			boolean preMigration) throws TopazeException {

		// tracer l'operation
		if (!preMigration && !isMigration) {
			getTracageService().ajouterTrace(Constants.PRODUCT, refContrat, "Préparation d’un contrat", user);
		}
		ContratValidator.checkUser(user);
		ContratValidator.validerProduitsPourPreparationContrat(produits, false);
		Contrat contratParent = new Contrat();
		if (!isMigration) {
			contratParent.setReference(keygenService.getNextKey(Contrat.class));
			contratParent.setTitre("Contrat Global: " + contratParent.getReference());
		} else {
			contratParent.setReference(refContrat);
			contratParent.setTitre("Contrat Global: " + refContrat);
		}

		Set<ElementContractuel> sousContrats = new HashSet<ElementContractuel>();
		for (Produit produit : produits) {
			ElementContractuel sousContrat = preparerElementContractuel(produit);
			sousContrat.setContratParent(contratParent);
			sousContrats.add(sousContrat);
		}

		ContratUtils.creerArborescence(produits, sousContrats);

		contratParent.setSousContrats(sousContrats);
		contratParent.setDatePreparation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		if (!preMigration) {
			contratRepository.save(contratParent);
		}

		// tracer l'opération de préparation contrat
		if (!preMigration && !isMigration) {
			getTracageService().ajouterTrace(Constants.PRODUCT, contratParent.getReference(),
					"Contrat " + contratParent.getReference() + " préparé", user);
		}
		return contratParent;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void validerContrat(String referenceContrat, ContratValidationInfo contratValidationInfo)
			throws TopazeException {

		ContratValidator.checkUser(contratValidationInfo.getUser());
		ContratValidator.checkPolitiqueValidation(contratValidationInfo);
		// tracer l'operation
		if (!contratValidationInfo.isMigration()) {
			getTracageService().ajouterTrace(
					Constants.PRODUCT,
					referenceContrat,
					"Validation du contrat " + referenceContrat + " "
							+ contratValidationInfo.getPolitiqueValidation().getCommentaire(),
					contratValidationInfo.getUser());
		}

		Contrat contratGlobal = contratRepository.findByReference(referenceContrat);

		ContratValidator.checkContratNonValider(referenceContrat, contratGlobal);

		ContratValidator.validerContratNonResiler(contratGlobal);

		ContratValidator.validerInfoValidationContratGlobal(contratValidationInfo, contratGlobal.getSousContrats());

		contratGlobal.setIdClient(contratValidationInfo.getIdClient());
		contratGlobal.setSegmentTVA(contratValidationInfo.getSegmentTVA());
		Set<ElementContractuel> sousContrats = contratGlobal.getSousContrats();

		List<PaiementInfo> paiementInfos = contratValidationInfo.getPaiementInfos();

		ContratValidator.validerCorrectSousContratsEtProduits(paiementInfos, sousContrats, referenceContrat);

		for (ElementContractuel sousContrat : sousContrats) {

			sousContrat.setIdAdrFacturation(contratValidationInfo.getIdAdrFacturation());

			PaiementInfo info = new PaiementInfo();
			info.setNumEC(sousContrat.getNumEC());
			info.setReferenceProduit(sousContrat.getReferenceProduit());
			for (PaiementInfo paiementInfo : paiementInfos) {
				if (paiementInfo.equals(info)) {
					sousContrat.setReferenceModePaiement(paiementInfo.getReferenceModePaiement());
					sousContrat.setIdAdrLivraison(paiementInfo.getIdAdrLivraison());
					sousContrat.setDateValidation(PropertiesUtil.getInstance().getDateDuJour().toDate());
				}
			}

		}

		contratGlobal.setDateValidation(PropertiesUtil.getInstance().getDateDuJour().toDate());

		contratGlobal.setPolitiqueValidation(contratValidationInfo.getPolitiqueValidation());

		contratRepository.save(contratGlobal);

		/*
		 * Contrat valide lancer un evenement vers apach ActiveMQ.
		 */

		if (!contratValidationInfo.isMigration()) {
			Map<String, Object> contratValideInfo = new HashMap<>();
			contratValideInfo.put("referenceContrat", contratGlobal.getReference());
			contratMessagesSender.sendContratValidatedEvent(contratValideInfo);
			contratMessagesSender.sendControlVenteEvent(contratValideInfo);
			// tracer l'opération de validation contrat
			getTracageService().ajouterTrace(Constants.PRODUCT, referenceContrat,
					"Contrat " + referenceContrat + " validé", contratValidationInfo.getUser());
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date changerDateDebutFacturation(String referenceContrat, Date dateDebutFacturation) throws TopazeException {
		Contrat contrat = contratRepository.findByReference(referenceContrat);
		ContratValidator.checkExist(referenceContrat, contrat);
		ContratHistorique contratHistorique = contratHistoriqueRepository.findDerniereVersion(referenceContrat);
		Avenant avenant =
				contratHistorique != null ? avenantRepository.findByReferenceContratAndVersion(
						contratHistorique.getReference(), contratHistorique.getVersion()) : null;
		PolitiqueCession politiqueCession = avenant != null ? avenant.getPolitiqueCession() : null;
		PolitiqueMigration politiqueMigration = avenant != null ? avenant.getPolitiqueMigration() : null;
		PolitiqueRenouvellement politiqueRenouvellement = avenant != null ? avenant.getPolitiqueRenouvellement() : null;

		ContratValidator.validerChangerDateDebutFacturation(dateDebutFacturation, contrat);

		contrat.setDateDebutFacturation(dateDebutFacturation);

		boolean isLivraison = false;
		boolean isMigration = false;
		boolean isCession = false;
		boolean isRenouvellement = false;
		/*
		 * identifier le process.
		 */
		if (contratHistorique == null) {
			/*
			 * process de facturation normale apres la livraison.
			 */
			isLivraison = true;
		} else if (politiqueCession != null) {
			/*
			 * c'est une cession
			 */
			isCession = true;
		} else if (politiqueMigration != null) {
			/*
			 * c'est une migration.
			 */
			isMigration = true;
		} else if (politiqueRenouvellement != null) {
			/*
			 * c'est un renouvellement
			 */
			isRenouvellement = true;
		}
		for (ElementContractuel elementContractuel : contrat.getSousContrats()) {

			if (isLivraison) {
				// calculer la date fin duree.
				elementContractuel.setDateDebutFacturation(dateDebutFacturation);

			} else if (isMigration) {
				if (elementContractuel.getDateDebutFacturation() == null) {
					elementContractuel.setDateDebutFacturation(dateDebutFacturation);
				}
			}

			/*
			 * calcul de la nouvelle date d'engagement.
			 */
			if (elementContractuel.getEngagement() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(dateDebutFacturation);
				cal.add(Calendar.MONTH, elementContractuel.getEngagementMax());
				elementContractuel.setDateFinEngagement(cal.getTime());
			}

			// calculer la date fin duree.
			if (elementContractuel.getDuree() != null) {
				Calendar dateFinDuree = Calendar.getInstance();
				dateFinDuree.setTime(dateDebutFacturation);
				dateFinDuree.add(Calendar.MONTH, elementContractuel.getDuree());
				elementContractuel.setDateFinDuree(dateFinDuree.getTime());
			}
		}

		contratRepository.save(contrat);

		/*
		 * envoie de l'evenement adequat vers la facture pour la facturation.
		 */
		if (isLivraison) {
			contratMessagesSender.sendLaunchBillingEvent(referenceContrat);
			contratMessagesSender.sendContractDeliveredEvent(referenceContrat);
		} else if (isCession) {
			contratMessagesSender.sendLunchSuccessionBillingEvent(referenceContrat);
		} else if (isMigration) {
			contratMessagesSender.sendContratMigrationLivredBillingEvent(referenceContrat);
		} else if (isRenouvellement) {
			contratMessagesSender.sendLunchRenewalBillingEvent(referenceContrat);
		}

		return dateDebutFacturation;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Contrat> findContratsGlobalValider() {
		return contratRepository.findContratsGlobalValider();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getReferencesContratsGlobalValider() {
		List<String> referencesContratsGlobalValider = new ArrayList<>();
		List<Contrat> contratsGlobalValider = findContratsGlobalValider();
		for (Contrat c : contratsGlobalValider)
			referencesContratsGlobalValider.add(c.getReference());
		return referencesContratsGlobalValider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getAssociatedProductsRefrences(String reference) {
		List<String> references = new ArrayList<>();
		Contrat contrat = contratRepository.findByReference(reference);
		if (contrat != null && contrat.getSousContrats().size() > 0) {
			Set<ElementContractuel> sousContrats = contrat.getSousContrats();
			for (ElementContractuel c : sousContrats) {
				references.add(c.getReferenceProduit());
			}
		}
		return references;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ContratBillingInfo> getContratBillingInformation(String referenceContrat, Integer numEC)
			throws TopazeException {

		ArrayList<ContratBillingInfo> contratBillingInfos = new ArrayList<>();

		Contrat contrat = contratRepository.findByReference(referenceContrat);

		if (contrat != null && numEC == 0) {
			ContratValidator.validerGetContratBillingInformation(referenceContrat, contrat);
			for (ElementContractuel elementContractuel : contrat.getSousContrats()) {

				if (!elementContractuel.isCloture() && !ContratUtils.verifierModePaiementAutre(elementContractuel)) {

					ContratBillingInfo contratBillingInfo = creerContratBillingInfo(elementContractuel, contrat, false);
					contratBillingInfo.setEngagement(elementContractuel.getEngagementMax());
					contratBillingInfo.setDateFinEngagement(elementContractuel.getDateFinEngagement());
					contratBillingInfo.setReferenceContrat(referenceContrat);
					contratBillingInfo.setDateFinContratProbable(elementContractuel.getModeFacturation().equals(
							ModeFacturation.DATE_ANNIVERSAIRE) ? Utils.dateMinusMonth(contrat.getMaxDateFinDuree(), 1)
							: contrat.getMaxDateFinDuree());

					if (elementContractuel.getElementContractuelParent() != null) {
						for (ContratBillingInfo contratBillingInfoParent : contratBillingInfos) {
							if (elementContractuel.getElementContractuelParent().getContratParent().getReference()
									.equals(contratBillingInfoParent.getReferenceContrat())
									&& elementContractuel.getElementContractuelParent().getNumEC()
											.equals(contratBillingInfoParent.getNumEC())) {
								contratBillingInfo.setContratBillingInfoParent(contratBillingInfoParent);
							}
						}
					}

					if (elementContractuel.getSousElementContractuel() != null) {
						for (ElementContractuel contractuel : elementContractuel.getSousElementContractuel()) {
							for (ContratBillingInfo contratBillingInfoFils : contratBillingInfos) {
								if (contratBillingInfoFils.getReferenceContrat().equals(
										contractuel.getContratParent().getReference())
										&& contratBillingInfoFils.getNumEC().equals(contractuel.getNumEC())) {
									contratBillingInfoFils.setContratBillingInfoParent(contratBillingInfo);
								}
							}
						}
					}

					contratBillingInfos.add(contratBillingInfo);
				}
			}
		} else {

			ElementContractuel elementContractuel =
					elementContractuelRepository.findByNumECAndReferenceContrat(numEC, referenceContrat);
			ElementContractuelHistorique elementContractuelHistorique =
					elementContractuelHistoriqueRepository.findByReferenceContratAndNumEC(referenceContrat, numEC);

			if (elementContractuel == null && elementContractuelHistorique != null) {
				elementContractuel = elementContractuelHistorique.toElementContractuel();
				ContratHistorique contratHistoriqueParent = elementContractuelHistorique.getContratParent();
				elementContractuel.setContratParent(contratHistoriqueParent.toContrat());
			}
			if (elementContractuel != null && !ContratUtils.verifierModePaiementAutre(elementContractuel)) {
				ContratValidator.validerGetContratBillingInformation(referenceContrat, elementContractuel);
				contrat = elementContractuel.getContratParent();
				ContratBillingInfo contratBillingInfo = creerContratBillingInfo(elementContractuel, contrat, false);
				contratBillingInfo.setEngagement(elementContractuel.getEngagementMax());
				contratBillingInfo.setDateFinEngagement(elementContractuel.getDateFinEngagement());
				contratBillingInfo.setReferenceContrat(referenceContrat);
				contratBillingInfo.setDateFinContratProbable(Utils.dateMinusMonth(contrat.getMaxDateFinDuree(), 1));

				if (ContratUtils.isResiliationGlobale(elementContractuel)) {
					creerContratBillingInfo(elementContractuel.getElementContractuelParent(), contrat, false);

					contratBillingInfo.setContratBillingInfoParent(creerContratBillingInfo(
							elementContractuel.getElementContractuelParent(), contrat, false));

				}

				contratBillingInfos.add(contratBillingInfo);
			}
		}

		return contratBillingInfos;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContratBP getContratBP(String referenceContrat) throws TopazeException {
		Contrat contrat = contratRepository.findByReference(referenceContrat);
		ContratValidator.validerContratExistValider(referenceContrat, contrat);

		return creeContratBP(contrat, false);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContratBP getContratBPHistorique(String referenceContrat) throws TopazeException {
		ContratHistorique contratHistorique = contratHistoriqueRepository.findDerniereVersion(referenceContrat);
		ContratValidator.checkExist(referenceContrat, contratHistorique);
		return creeContratBP(contratHistorique.toContrat(), true);
	}

	/**
	 * Cree un {@link ContratBP} a partir d'un {@link Contrat}.
	 * 
	 * @param contrat
	 *            {@link Contrat}.
	 * @return {@link ContratBP}.
	 */
	private ContratBP creeContratBP(Contrat contrat, boolean isMigration) {
		ContratBP contratBP = new ContratBP();
		contratBP.setReferenceContrat(contrat.getReference());
		contratBP.setIdClient(contrat.getIdClient());
		ArrayList<ContratBP> sousContratsBP = new ArrayList<>();

		for (ElementContractuel elementContractuel : contrat.getSousContrats()) {
			if (isMigration) {
				if (!elementContractuel.getTypeContrat().equals(TypeContrat.VENTE)
						&& (!elementContractuel.getStatut().equals(StatutContrat.RESILIER) || elementContractuel
								.isMigre())) {
					sousContratsBP.add(creeSousContratBP(elementContractuel));
				}

			} else {
				if (!elementContractuel.getStatut().equals(StatutContrat.RESILIER) || elementContractuel.isMigre()) {
					sousContratsBP.add(creeSousContratBP(elementContractuel));
				}
			}

		}

		contratBP.setSousContratsBP(sousContratsBP);

		return contratBP;
	}

	/**
	 * Cree un {@link ContratBP} a partir d'un {@link ElementContractuel}.
	 * 
	 * @param elementContractuel
	 *            {@link ElementContractuel}.
	 * @return {@link ContratBP}.
	 */
	private ContratBP creeSousContratBP(ElementContractuel elementContractuel) {
		ContratBP sousContratBP = new ContratBP();
		sousContratBP.setReferenceContrat(elementContractuel.getContratParent().getReference());
		sousContratBP.setTypeContrat(elementContractuel.getTypeContrat());
		sousContratBP.setReferenceProduit(elementContractuel.getReferenceProduit());
		sousContratBP.setNumEC(elementContractuel.getNumEC());
		sousContratBP.setIdAdrLivraison(elementContractuel.getIdAdrLivraison());
		sousContratBP.setTypeProduit(elementContractuel.getTypeProduit());
		ElementContractuel elementContractuelParent = elementContractuel.getElementContractuelParent();

		if (elementContractuelParent != null) {

			while (elementContractuelParent.getElementContractuelParent() != null) {
				elementContractuelParent = elementContractuelParent.getElementContractuelParent();
			}

			sousContratBP.setContratBPParent(creeSousContratBP(elementContractuelParent));
		}

		return sousContratBP;
	}

	@Override
	public List<String> getReferencesContratLivrer() {

		return contratRepository.findReferencesContratLivrer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changerMoyenDePaiement(String refContratGlobal, ContratChangerModePaiementInfo changerModePaiementInfo)
			throws TopazeException {

		Contrat contratGlobal = contratRepository.findByReference(refContratGlobal);
		ContratValidator.checkExist(refContratGlobal, contratGlobal);
		Set<ElementContractuel> sousContrats = contratGlobal.getSousContrats();
		List<PaiementModificationInfo> paiementInfos = changerModePaiementInfo.getProduits();

		// Validation des informations.
		ContratValidator.validerChangerModePaiment(changerModePaiementInfo, sousContrats, refContratGlobal);

		ContratAvenant contratAvenant = new ContratAvenant();
		contratAvenant.setCommentaire(changerModePaiementInfo.getCommentaire());
		contratAvenant.setUser(changerModePaiementInfo.getUser());
		contratAvenant.setDateAction(changerModePaiementInfo.getDateAction());

		// créer un contratModification pour lancer la modification.
		List<ContratModification> contratModifications = new ArrayList<>();

		for (PaiementModificationInfo paiementModificationInfo : paiementInfos) {

			for (ElementContractuel sousContrat : sousContrats) {

				PaiementModificationInfo info = new PaiementModificationInfo();
				info.setNumEC(sousContrat.getNumEC());
				info.setReferenceProduit(sousContrat.getReferenceProduit());

				if (paiementModificationInfo.equals(info)) {

					// modification pour le mode de paiement.
					ContratModification modificationMP = new ContratModification();
					modificationMP.setRefContrat(contratGlobal.getReference());
					modificationMP.setNumEC(sousContrat.getNumEC());
					modificationMP.setAction(Actions.MODIFMODEPAIEMENT.name());
					modificationMP.setValeur(paiementModificationInfo.getModePaiement().name());
					contratModifications.add(modificationMP);

					// modification pour la référence mode de paiement.
					ContratModification modificationREFMP = new ContratModification();
					modificationREFMP.setRefContrat(contratGlobal.getReference());
					modificationREFMP.setAction(Actions.MODIFREFMODEPAIEMENT.name());
					modificationREFMP.setValeur(paiementModificationInfo.getReferenceModePaiement());
					contratModifications.add(modificationREFMP);
				}

			}

		}

		contratAvenant.setContratModifications(contratModifications);

	}

	/**
	 * Creer contrat billing info.
	 * 
	 * @param elementContractuel
	 *            the element contractuel
	 * @param contrat
	 *            the contrat
	 * @param isMigre
	 *            true si l' element contractuel est migre.
	 * @return the contrat billing info
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	private ContratBillingInfo creerContratBillingInfo(ElementContractuel elementContractuel, Contrat contrat,
			Boolean isMigre) throws TopazeException {

		ContratBillingInfo contratBillingInfo = new ContratBillingInfo();
		contratBillingInfo.setReferenceProduit(elementContractuel.getReferenceProduit());
		contratBillingInfo.setIdProduit(elementContractuel.getIdProduit());
		contratBillingInfo.setReferenceContrat(contrat.getReference());
		contratBillingInfo.setNumeroCommande(elementContractuel.getNumeroCommande());
		contratBillingInfo.setTitre(elementContractuel.getTitre());
		contratBillingInfo.setTypeContrat(elementContractuel.getTypeContrat());
		contratBillingInfo.setNumEC(elementContractuel.getNumEC());

		if (elementContractuel.getMontant() != null) {
			contratBillingInfo.setMontant(elementContractuel.getMontant());
			contratBillingInfo.setEngagement(elementContractuel.getEngagement());
			contratBillingInfo.setDureeContrat(elementContractuel.getDuree());
			contratBillingInfo.setPeriodicite(elementContractuel.getPeriodicite());
			contratBillingInfo.setModePaiement(elementContractuel.getModePaiement());
			contratBillingInfo.setModeFacturation(elementContractuel.getModeFacturation());
			contratBillingInfo.setTypeTVA(elementContractuel.getTypeTVA());
			contratBillingInfo.setFrais(getFrais(contrat.getReference(), elementContractuel.getNumEC(), null,
					elementContractuel.getFrais()));
			contratBillingInfo.setDuree(elementContractuel.getDuree());
		}

		contratBillingInfo.setDateFinContrat(elementContractuel.getDateFinContrat());
		contratBillingInfo.setReferenceModePaiement(elementContractuel.getReferenceModePaiement());
		contratBillingInfo.setIdClient(contrat.getIdClient());
		contratBillingInfo.setDateDebutFacturation(contrat.getDateDebutFacturation());
		contratBillingInfo.setPolitiqueValidation(contrat.getPolitiqueValidation().toBusiness());
		contratBillingInfo.setPolitiqueMigration(mappingPolitiqueMigrationDomainToBusiness(contrat.getReference()));
		contratBillingInfo.setTypeProduit(elementContractuel.getTypeProduit());
		contratBillingInfo.setDateDerniereFacture(elementContractuel.getDateDerniereFacture());

		contratBillingInfo.setRemboursable(elementContractuel.isRemboursable());
		contratBillingInfo.setMigre(isMigre);

		if (contrat.getPolitiqueResiliation() != null) {
			contratBillingInfo.setPolitiqueResiliation(contrat.getPolitiqueResiliation().toBusiness());
		} else {
			if (elementContractuel.getPolitiqueResiliation() != null) {
				contratBillingInfo.setPolitiqueResiliation(elementContractuel.getPolitiqueResiliation().toBusiness());
			}
		}

		List<Reduction> reductionGlobals = reductionRepository.findReductionGlobales(contrat.getReference(), null);
		for (Reduction reductionGlobal : reductionGlobals) {
			if (reductionGlobal != null && reductionGlobal.isEligible() && !reductionGlobal.isAnnule()) {
				ReductionInfo reductionContrat = reductionGlobal.toReductionInfo();
				contratBillingInfo.addReductionContrat(reductionContrat);
			}
		}

		Reduction reduction =
				reductionRepository.findReductionPartiel(contrat.getReference(), null, elementContractuel.getNumEC());
		if (reduction != null && reduction.isEligible() && !reduction.isAnnule()) {
			ReductionInfo reductionEC = reduction.toReductionInfo();
			contratBillingInfo.setReductionEC(reductionEC);
		}

		ContratHistorique contratHistorique = contratHistoriqueRepository.findDerniereVersion(contrat.getReference());

		if (contratHistorique != null) {

			ElementContractuelHistorique element =
					elementContractuelHistoriqueRepository.findByReferenceAndVersion(contrat.getReference(),
							elementContractuel.getNumEC(), contratHistorique.getVersion());

			if (element != null) {
				// if (elementContractuel.getMontant() == element.getMontant()) {
				// contratBillingInfo.setDateDerniereFacture(elementContractuel.getDateDerniereFacture());
				// } else if (!elementContractuel.isMigrationAdministrative(element)) {
				// contratBillingInfo.setDateDerniereFacture(elementContractuel.getDateDerniereFacture());
				// }

				if (element.getReferenceProduit().equals(elementContractuel.getReferenceProduit())) {
					contratBillingInfo.setMigre(false);
					contratBillingInfo.setNouveau(false);
				} else {
					contratBillingInfo.setMigre(true);
					contratBillingInfo.setNouveau(false);
				}
			} else {
				contratBillingInfo.setMigre(false);
				contratBillingInfo.setNouveau(true);
			}
			contratBillingInfo.setResilie(false);
		} else {
			contratBillingInfo.setDateDerniereFacture(elementContractuel.getDateDerniereFacture());
		}

		return contratBillingInfo;
	}

	/**
	 * Creer contrat billing info.
	 * 
	 * @param elementContractuel
	 *            {@link ElementContractuelHistorique}.
	 * @param contrat
	 *            {@link Contrat}.
	 * @return {@link ContratBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	private ContratBillingInfo creerContratBillingInfo(ElementContractuelHistorique elementContractuel,
			ContratHistorique contrat) throws TopazeException {

		Avenant avenant =
				avenantRepository.findByReferenceContratAndVersion(contrat.getReference(), contrat.getVersion());
		ContratBillingInfo contratBillingInfo = new ContratBillingInfo();
		contratBillingInfo.setVersion(contrat.getVersion());
		contratBillingInfo.setReferenceProduit(elementContractuel.getReferenceProduit());
		contratBillingInfo.setNumeroCommande(elementContractuel.getNumeroCommande());
		contratBillingInfo.setTitre(elementContractuel.getTitre());
		contratBillingInfo.setTypeContrat(elementContractuel.getTypeContrat());
		contratBillingInfo.setNumEC(elementContractuel.getNumEC());
		contratBillingInfo.setReferenceContrat(contrat.getReference());
		if (elementContractuel.getMontant() != null) {
			contratBillingInfo.setMontant(elementContractuel.getMontant());
			contratBillingInfo.setEngagement(elementContractuel.getEngagement());
			contratBillingInfo.setDureeContrat(elementContractuel.getDuree());
			contratBillingInfo.setPeriodicite(elementContractuel.getPeriodicite());
			contratBillingInfo.setModePaiement(elementContractuel.getModePaiement());
			contratBillingInfo.setModeFacturation(elementContractuel.getModeFacturation());
			contratBillingInfo.setTypeTVA(elementContractuel.getTypeTVA());
			contratBillingInfo.setFrais(getFrais(contrat.getReference(), elementContractuel.getNumEC(),
					contrat.getVersion(), elementContractuel.getFrais()));
			contratBillingInfo.setDuree(elementContractuel.getDuree());
		}
		contratBillingInfo.setDateDerniereFacture(elementContractuel.getDateDerniereFacture());
		contratBillingInfo.setReferenceModePaiement(elementContractuel.getReferenceModePaiement());
		contratBillingInfo.setIdClient(contrat.getIdClient());
		contratBillingInfo.setDateDebutFacturation(contrat.getDateDebutFacturation());
		contratBillingInfo.setPolitiqueValidation(contrat.getPolitiqueValidation().toBusiness());
		contratBillingInfo.setPolitiqueMigration(mappingPolitiqueMigrationDomainToBusiness(contrat.getReference()));
		contratBillingInfo.setTypeProduit(elementContractuel.getTypeProduit());
		contratBillingInfo.setRemboursable(elementContractuel.isRemboursable());

		PolitiqueCession politiqueCession = avenant != null ? avenant.getPolitiqueCession() : null;
		PolitiqueMigration politiqueMigration = avenant != null ? avenant.getPolitiqueMigration() : null;
		if (politiqueMigration != null) {
			PolitiqueResiliation politiqueResiliation = politiqueMigration.getPRFromPM();
			com.nordnet.topaze.contrat.domain.PolitiqueResiliation politiqueResiliationDomain =
					politiqueResiliation.toDomain();
			contratBillingInfo.setPolitiqueResiliation(politiqueResiliationDomain.toBusiness());
			contratBillingInfo.setDateFinContrat(politiqueMigration.getDateAction());
		} else if (politiqueCession != null) {
			PolitiqueResiliation politiqueResiliation = politiqueCession.getPRFromPC();
			com.nordnet.topaze.contrat.domain.PolitiqueResiliation politiqueResiliationDomain =
					politiqueResiliation.toDomain();
			contratBillingInfo.setPolitiqueResiliation(politiqueResiliationDomain.toBusiness());
			contratBillingInfo.setDateFinContrat(politiqueCession.getDateAction());
		} else {
			if (elementContractuel.getPolitiqueResiliation() != null) {
				contratBillingInfo.setPolitiqueResiliation(elementContractuel.getPolitiqueResiliation().toBusiness());
			}
		}

		if (politiqueCession != null) {
			contratBillingInfo.setPolitiqueCession(politiqueCession.toBusiness());
		}

		List<Reduction> reductionGlobals =
				reductionRepository.findReductionGlobales(contrat.getReference(), contrat.getVersion());
		for (Reduction reductionGlobal : reductionGlobals) {
			if (reductionGlobal != null && reductionGlobal.isEligible() && !reductionGlobal.isAnnule()) {
				ReductionInfo reductionContrat = reductionGlobal.toReductionInfo();
				contratBillingInfo.addReductionContrat(reductionContrat);
			}
		}

		Reduction reduction =
				reductionRepository.findReductionPartiel(contrat.getReference(), contrat.getVersion(),
						elementContractuel.getNumEC());
		if (reduction != null && reduction.isEligible() && !reduction.isAnnule()) {
			ReductionInfo reductionEC = reduction.toReductionInfo();
			contratBillingInfo.setReductionEC(reductionEC);
		}

		Reduction reductionSurFraisMigration =
				reductionRepository.findReductionGlobalSurFrais(contrat.getReference(), contrat.getVersion(),
						TypeFrais.MIGRATION);
		if (reductionSurFraisMigration != null && reductionSurFraisMigration.isEligible()
				&& !reductionSurFraisMigration.isAnnule()) {
			contratBillingInfo.setReductionSurFraisMigration(reductionSurFraisMigration.toReductionInfo());
		}

		if (elementContractuel.getPolitiqueResiliation() != null && elementContractuel.getDateFinContrat() != null) {
			contratBillingInfo.setMigre(false);
			contratBillingInfo.setNouveau(false);
			contratBillingInfo.setResilie(false);
		} else {
			ElementContractuel element =
					elementContractuelRepository.findByNumECAndReferenceContrat(elementContractuel.getNumEC(),
							contrat.getReference());
			if (element != null) {
				contratBillingInfo.setResilie(false);
				if (element.getReferenceProduit().equals(elementContractuel.getReferenceProduit())) {
					if (!element.getMontant().equals(elementContractuel.getMontant())
							|| element.getTypeTVA() != elementContractuel.getTypeTVA()
							|| !element.getPeriodicite().equals(elementContractuel.getPeriodicite())
							|| element.getModeFacturation() != elementContractuel.getModeFacturation()) {
						contratBillingInfo.setMigre(true);
					} else {
						contratBillingInfo.setMigre(false);
					}
					contratBillingInfo.setNouveau(false);
				} else {
					contratBillingInfo.setMigre(true);
					contratBillingInfo.setNouveau(false);
				}
			} else {
				contratBillingInfo.setResilie(true);
				contratBillingInfo.setMigre(false);
				contratBillingInfo.setNouveau(false);
			}
		}

		return contratBillingInfo;
	}

	/**
	 * Creation de liste de {@link Frais} à partir d'une liste de {@link FraisContrat}.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param numEC
	 *            numero element contractuel.
	 * @param version
	 *            version du contrat.
	 * @param frais
	 *            liste des {@link FraisContrat}.
	 * @return liste des {@link Frais}.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	private Set<Frais> getFrais(String referenceContrat, Integer numEC, Integer version, Set<FraisContrat> frais)
			throws TopazeException {
		Set<Frais> fraisSet = new HashSet<>();
		ReductionInfo info = null;
		for (FraisContrat fraisContrat : frais) {
			Reduction reductionGlobal =
					reductionRepository.findReductionGlobalSurFrais(referenceContrat, version,
							fraisContrat.getTypeFrais());
			Reduction reductionPartiel =
					reductionRepository.findReductionPartielSurFrais(referenceContrat, version, numEC,
							fraisContrat.getTypeFrais());
			Frais f = new Frais();
			f.setMontant(fraisContrat.getMontant());
			f.setNombreMois(fraisContrat.getNombreMois());
			f.setOrdre(fraisContrat.getOrdre());
			f.setTypeFrais(fraisContrat.getTypeFrais());
			f.setTitre(fraisContrat.getTitre());
			if (reductionGlobal != null && reductionGlobal.isEligible() && !reductionGlobal.isAnnule()) {
				info = reductionGlobal.toReductionInfo();
				f.setReductionInfoContrat(info);
			}

			if (reductionPartiel != null && reductionPartiel.isEligible() && !reductionPartiel.isAnnule()) {
				info = reductionPartiel.toReductionInfo();
				f.setReductionInfoEC(info);
			}

			fraisSet.add(f);
		}

		return fraisSet;
	}

	/**
	 * 
	 * Mapping politique Migration domain à business.
	 * 
	 * @param politiqueMigration
	 *            the politique validation
	 * @return the politique validation
	 */
	private com.nordnet.topaze.contrat.business.PolitiqueMigration mappingPolitiqueMigrationDomainToBusiness(
			String refContrat) {

		ContratHistorique contratHistorique = contratHistoriqueRepository.findDerniereVersion(refContrat);
		if (contratHistorique != null) {
			Avenant avenant =
					avenantRepository.findByReferenceContratAndVersionAndTypeAvenant(refContrat,
							contratHistorique.getVersion(), TypeAvenant.MIGRATION);
			PolitiqueMigration politiqueMigration = avenant != null ? avenant.getPolitiqueMigration() : null;
			if (politiqueMigration != null) {
				return politiqueMigration.toBusiness();
			}
			return null;

		}

		return null;
	}

	@Override
	public void changerDateDerniereFacture(String referenceContrat, Integer numEC) throws TopazeException {
		Contrat contrat = contratRepository.findByReference(referenceContrat);
		LocalDate dateDuJour = PropertiesUtil.getInstance().getDateDuJour().toLocalDate();
		if (contrat != null && numEC == 0) {
			contrat.setDateDerniereFacture(dateDuJour.toDate());
			contratRepository.save(contrat);

		} else {
			ElementContractuel elementContractuel =
					elementContractuelRepository.findByNumECAndReferenceContrat(numEC, referenceContrat);
			ElementContractuelHistorique elementContractuelHistorique =
					elementContractuelHistoriqueRepository.findByReferenceContratAndNumEC(referenceContrat, numEC);
			if (elementContractuel != null) {
				elementContractuel.setDateDerniereFacture(PropertiesUtil.getInstance().getDateDuJour().toDate());
				elementContractuelRepository.save(elementContractuel);
			} else if (elementContractuelHistorique != null) {
				elementContractuelHistorique.setDateDerniereFacture(PropertiesUtil.getInstance().getDateDuJour()
						.toDate());
				elementContractuelHistoriqueRepository.save(elementContractuelHistorique);
			}
		}
	}

	@Override
	public void changerDateFactureResiliation(String referenceContrat, Integer numEC) throws TopazeException {
		Contrat contrat = contratRepository.findByReference(referenceContrat);
		LocalDate dateDuJour = PropertiesUtil.getInstance().getDateDuJour().toLocalDate();
		if (contrat != null && numEC == 0) {
			contrat.setDateFactureResiliation(dateDuJour.toDate());
			for (ElementContractuel elementContractuel : contrat.getSousContrats()) {
				elementContractuel.setDateFactureResiliation(dateDuJour.toDate());
			}
			contratRepository.save(contrat);

		} else {
			ElementContractuel elementContractuel =
					elementContractuelRepository.findByNumECAndReferenceContrat(numEC, referenceContrat);
			ElementContractuelHistorique elementContractuelHistorique =
					elementContractuelHistoriqueRepository.findByReferenceContratAndNumEC(referenceContrat, numEC);
			if (elementContractuel != null) {
				elementContractuel.setDateFactureResiliation(dateDuJour.toDate());
				elementContractuelRepository.save(elementContractuel);
			} else if (elementContractuelHistorique != null) {
				elementContractuelHistorique.setDateFactureResiliation(dateDuJour.toDate());
				elementContractuelHistoriqueRepository.save(elementContractuelHistorique);
			}
		}
	}

	@Override
	public void changerDateFinContrat(String referenceContrat, Integer numEC) throws TopazeException {
		Contrat contrat = contratRepository.findByReference(referenceContrat);
		if (contrat != null && numEC == 0) {
			contrat.setDateFinContrat(PropertiesUtil.getInstance().getDateDuJour().toDate());
			contratRepository.save(contrat);
		} else {
			ElementContractuel elementContractuel =
					elementContractuelRepository.findByNumECAndReferenceContrat(numEC, referenceContrat);
			elementContractuel.setDateFinContrat(PropertiesUtil.getInstance().getDateDuJour().toDate());
			elementContractuelRepository.save(elementContractuel);
		}
	}

	@Override
	public ElementContractuelInfo getParentInfo(String refContrat, Integer numEC) throws TopazeException {
		ElementContractuel elementContractuel =
				elementContractuelRepository.findByNumECAndReferenceContrat(numEC, refContrat);
		ContratValidator.validerGetReferenceParent(elementContractuel, refContrat);
		ElementContractuel elementParent = elementContractuel.getElementContractuelParent();
		ElementContractuelInfo elementParentInfo = new ElementContractuelInfo();
		elementParentInfo.setReferenceContrat(refContrat);
		elementParentInfo.setNumEC(elementParent.getNumEC());
		elementParentInfo.setReferenceProduit(elementParent.getReferenceProduit());
		return elementParentInfo;
	}

	@Override
	public Integer getEngagement(String referenceContrat) throws TopazeException {

		Integer maxEngagement = 0;
		// chercher contrat par réference
		Contrat contrat = contratRepository.findByReference(referenceContrat);

		for (ElementContractuel element : contrat.getSousContrats()) {
			if (element.getEngagement() != null && element.getEngagement() > maxEngagement) {
				maxEngagement = element.getEngagement();
			}
		}

		return maxEngagement;
	}

	@Override
	public List<ElementContractuelInfo> getFilsService(String referenceContrat, Integer numEC) throws TopazeException {
		ElementContractuel elementContractuel =
				elementContractuelRepository.findByNumECAndReferenceContrat(numEC, referenceContrat);
		ContratValidator.checkExist(referenceContrat, elementContractuel);
		List<ElementContractuel> fils = elementContractuelRepository.findFils(elementContractuel);
		List<ElementContractuelInfo> elementContractuelInfos = new ArrayList<>();
		for (ElementContractuel elContractuel : fils) {
			if (elContractuel.getTypeProduit() == TypeProduit.SERVICE && !elContractuel.isResilier()) {
				ElementContractuelInfo elementContractuelInfo = new ElementContractuelInfo();
				elementContractuelInfo.setReferenceContrat(referenceContrat);
				elementContractuelInfo.setNumEC(elContractuel.getNumEC());
				elementContractuelInfo.setReferenceProduit(elContractuel.getReferenceProduit());
				if (elContractuel.getMontant() == null) {
					elementContractuelInfo.setHasPrix(false);
				} else {
					elementContractuelInfo.setHasPrix(true);
				}
				elementContractuelInfos.add(elementContractuelInfo);
			}
		}

		return elementContractuelInfos;
	}

	@Override
	public boolean checkIsPackagerCreationPossible(String referenceContrat) throws TopazeException {
		Contrat contrat = contratRepository.findByReference(referenceContrat);
		ContratValidator.checkExist(referenceContrat, contrat);
		return contrat.getPolitiqueValidation().getCheckIsPackagerCreationPossible();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Avenant> findAll() {
		return avenantRepository.findAll();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ContratHistorique historiser(Contrat contrat) throws TopazeException {

		ContratHistorique contratHistorique = new ContratHistorique();
		contratHistorique.setReference(contrat.getReference());
		contratHistorique.setIdClient(contrat.getIdClient());
		contratHistorique.setSegmentTVA(contrat.getSegmentTVA());
		contratHistorique.setTitre(contrat.getTitre());
		contratHistorique.setDatePreparation(contrat.getDatePreparation());
		contratHistorique.setDateValidation(contrat.getDateValidation());
		contratHistorique.setDateDebutFacturation(contrat.getDateDebutFacturation());
		contratHistorique.setDateDerniereFacture(contrat.getDateDerniereFacture());
		contratHistorique.setDateFinContrat(contrat.getDateFinContrat());
		contratHistorique.setTypeResiliation(contrat.getTypeResiliation());
		com.nordnet.topaze.contrat.domain.PolitiqueValidation politiqueValidation = contrat.getPolitiqueValidation();
		if (politiqueValidation != null) {
			contratHistorique.setPolitiqueValidation(politiqueValidation.copy());
		}
		com.nordnet.topaze.contrat.domain.PolitiqueResiliation politiqueResiliation = contrat.getPolitiqueResiliation();
		if (politiqueResiliation != null) {
			contratHistorique.setPolitiqueResiliation(politiqueResiliation.copy());
		}
		List<ContratHistorique> contratHistoriques =
				contratHistoriqueRepository.findByReference(contrat.getReference());
		contratHistorique.setVersion(contratHistoriques.size());
		ElementContractuelHistorique elementContractuelHistorique;
		Set<ElementContractuelHistorique> elementContractuelHistoriques = new HashSet<>();
		Map<Integer, ElementContractuelHistorique> elementContractuelHistoriqueMap = new HashMap<>();

		for (ElementContractuel elementContractuel : contrat.getSousContrats()) {
			elementContractuelHistorique = elementContractuel.toElementContractuelHistorique();
			elementContractuelHistorique.setContratParent(contratHistorique);
			elementContractuelHistoriques.add(elementContractuelHistorique);
			elementContractuelHistoriqueMap.put(elementContractuelHistorique.getNumEC(), elementContractuelHistorique);

			/*
			 * historisation des reduction.
			 */
			List<Reduction> reductionGlobals = reductionRepository.findReductionGlobales(contrat.getReference(), null);
			for (Reduction reductionGlobal : reductionGlobals) {
				reductionGlobal.setVersion(contratHistorique.getVersion());
				reductionRepository.save(reductionGlobal);
			}

			Reduction reduction =
					reductionRepository.findReductionPartiel(contrat.getReference(), null,
							elementContractuel.getNumEC());
			if (reduction != null) {
				reduction.setVersion(contratHistorique.getVersion());
				reductionRepository.save(reduction);
			}

			/*
			 * historisation des reduction sur les frais.
			 */
			Reduction reductionSurFraisCreation =
					reductionRepository.findReductionGlobalSurFrais(contrat.getReference(), null, TypeFrais.CREATION);
			if (reductionSurFraisCreation != null) {
				Reduction reductionSurFraisCreationHistorise = reductionSurFraisCreation.copy();
				reductionSurFraisCreationHistorise.setVersion(contratHistorique.getVersion());
				reductionRepository.save(reductionSurFraisCreationHistorise);
			}

			Reduction reductionSurFraisResiliation =
					reductionRepository.findReductionGlobalSurFrais(contrat.getReference(), null, TypeFrais.CREATION);
			if (reductionSurFraisResiliation != null) {
				Reduction reductionSurFraisResiliationHistorise = reductionSurFraisResiliation.copy();
				reductionSurFraisResiliationHistorise.setVersion(contratHistorique.getVersion());
				reductionRepository.save(reductionSurFraisResiliationHistorise);
			}

			Reduction reductionSurFraisMigration =
					reductionRepository.findReductionGlobalSurFrais(contrat.getReference(), null, TypeFrais.MIGRATION);
			if (reductionSurFraisMigration != null) {
				reductionSurFraisMigration.setVersion(contratHistorique.getVersion());
				reductionRepository.save(reductionSurFraisMigration);
			}

		}

		contratHistorique.setSousContrats(elementContractuelHistoriques);

		/*
		 * definir la relation de parenter dans ElementcontractuelHistorique comme elles sont dans les
		 * elementContractuels.
		 */
		for (ElementContractuel elementContractuel : contrat.getSousContrats()) {
			if (elementContractuel.getElementContractuelParent() != null) {
				elementContractuelHistorique = elementContractuelHistoriqueMap.get(elementContractuel.getNumEC());
				ElementContractuelHistorique elementContractuelHistoriqueParent =
						elementContractuelHistoriqueMap
								.get(elementContractuel.getElementContractuelParent().getNumEC());
				elementContractuelHistorique.setElementContractuelParent(elementContractuelHistoriqueParent);
			}
		}

		contratHistoriqueRepository.save(contratHistorique);

		// tracer l'operation de versionning.
		getTracageService().ajouterTrace(
				Constants.PRODUCT,
				contratHistorique.getReference(),
				"Versionning du contrat " + contratHistorique.getReference() + " – version "
						+ contratHistorique.getVersion(), Constants.INTERNAL_USER);

		return contratHistorique;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContratAvenantInfo getContratAvenantInfo(String referenceContrat) throws TopazeException, IOException {
		Contrat contrat = contratRepository.findByReference(referenceContrat);
		ContratValidator.validerContratExistValider(referenceContrat, contrat);
		ContratAvenantInfo avenantInfo = new ContratAvenantInfo();
		avenantInfo.setDateAction(PropertiesUtil.getInstance().getDateDuJour().toDate());
		avenantInfo.setUser(Constants.INTERNAL_USER);

		List<ContratBP> modifications = new ArrayList<>();
		for (ElementContractuel elementContractuel : contrat.getSousContrats()) {
			ContratBP modification = creeSousContratBP(elementContractuel);
			ProduitMigration produitMigration =
					getProduitMigrationByNumEC(referenceContrat, elementContractuel.getNumEC());
			modification.setReferenceGammeDestination(produitMigration.getReferenceGammeDestination());
			modification.setReferenceGammeSource(produitMigration.getReferenceGammeSource());
			modifications.add(modification);
		}

		avenantInfo.setContratModifications(modifications);

		return avenantInfo;
	}

	/**
	 * Chercher le produit migration par numEC.
	 * 
	 * @param contrat
	 *            {@link Contrat}.
	 * @param numEC
	 *            numEC.
	 * @return {@link ProduitMigration}.
	 * @throws IOException
	 *             {@link IOException}.
	 */
	private ProduitMigration getProduitMigrationByNumEC(String referenceContrat, Integer numEC) throws IOException {
		ContratHistorique contratHistorique = contratHistoriqueRepository.findDerniereVersion(referenceContrat);
		Avenant avenant =
				avenantRepository.findByReferenceContratAndVersionAndTypeAvenant(referenceContrat,
						contratHistorique.getVersion(), TypeAvenant.MIGRATION);
		for (Modification modification : avenant.getModifContrat()) {
			if (modification.getNumEC().equals(numEC)) {
				return ProduitMigration.fromJsonToProduitMigration(modification.getTrameJson());
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ContratBillingInfo> getContratBillingInformationHistorise(String referenceContrat)
			throws TopazeException {

		ArrayList<ContratBillingInfo> contratBillingInfos = new ArrayList<>();

		ContratHistorique contratHistorique = contratHistoriqueRepository.findDerniereVersion(referenceContrat);

		if (contratHistorique != null) {
			for (ElementContractuelHistorique elementContractuelHistorique : contratHistorique.getSousContrats()) {

				if (!elementContractuelHistorique.isCloture()
						&& !ContratUtils.verifierModePaiementAutre(elementContractuelHistorique.toElementContractuel())) {

					ContratBillingInfo contratBillingInfo =
							creerContratBillingInfo(elementContractuelHistorique, contratHistorique);
					contratBillingInfo.setEngagement(elementContractuelHistorique.getEngagementMax());
					contratBillingInfo.setDateFinEngagement(elementContractuelHistorique.getDateFinEngagement());

					if (elementContractuelHistorique.getElementContractuelParent() != null) {
						for (ContratBillingInfo contratBillingInfoParent : contratBillingInfos) {
							if (elementContractuelHistorique.getElementContractuelParent().getContratParent()
									.getReference().equals(contratBillingInfoParent.getReferenceContrat())
									&& elementContractuelHistorique.getElementContractuelParent().getNumEC()
											.equals(contratBillingInfoParent.getNumEC())) {
								contratBillingInfo.setContratBillingInfoParent(contratBillingInfoParent);
							}
						}
					}

					if (elementContractuelHistorique.getSousElementContractuel() != null) {
						for (ElementContractuelHistorique contractuel : elementContractuelHistorique
								.getSousElementContractuel()) {
							for (ContratBillingInfo contratBillingInfoFils : contratBillingInfos) {
								if (contratBillingInfoFils.getReferenceContrat().equals(
										contractuel.getContratParent().getReference())
										&& contratBillingInfoFils.getNumEC().equals(contractuel.getNumEC())) {
									contratBillingInfoFils.setContratBillingInfoParent(contratBillingInfo);
								}
							}
						}
					}

					contratBillingInfos.add(contratBillingInfo);
				}
			}
		}

		return contratBillingInfos;
	}

	@Override
	public Contrat findByReference(String referenceContrat) {
		return contratRepository.findByReference(referenceContrat);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ContratBillingInfo> getContratBillingInformationMigrationAdministrative(String referenceContrat)
			throws TopazeException {

		ArrayList<ContratBillingInfo> contratBillingInfos = new ArrayList<>();

		Contrat contrat = contratRepository.findByReference(referenceContrat);

		ContratValidator.validerGetContratBillingInformation(referenceContrat, contrat);
		for (ElementContractuel elementContractuel : contrat.getSousContrats()) {

			ContratHistorique contratHistorique =
					contratHistoriqueRepository.findDerniereVersion(contrat.getReference());

			if (contratHistorique != null) {

				ElementContractuelHistorique element =
						elementContractuelHistoriqueRepository.findByReferenceAndVersion(elementContractuel
								.getContratParent().getReference(), elementContractuel.getNumEC(), contratHistorique
								.getVersion());
				if (!elementContractuel.isCloture() && elementContractuel.isMigrationAdministrative(element)
						&& !ContratUtils.verifierModePaiementAutre(elementContractuel)) {

					ContratBillingInfo contratBillingInfo = creerContratBillingInfo(elementContractuel, contrat, false);
					contratBillingInfo.setEngagement(elementContractuel.getEngagementMax());
					contratBillingInfo.setDateFinEngagement(elementContractuel.getDateFinEngagement());
					contratBillingInfo.setDateFinContratProbable(elementContractuel.getModeFacturation().equals(
							ModeFacturation.DATE_ANNIVERSAIRE) ? Utils.dateMinusMonth(contrat.getMaxDateFinDuree(), 1)
							: contrat.getMaxDateFinDuree());

					if (elementContractuel.getElementContractuelParent() != null) {
						for (ContratBillingInfo contratBillingInfoParent : contratBillingInfos) {
							if (elementContractuel.getElementContractuelParent().getContratParent().getReference()
									.equals(contratBillingInfoParent.getReferenceContrat())
									&& elementContractuel.getElementContractuelParent().getNumEC()
											.equals(contratBillingInfoParent.getNumEC())) {
								contratBillingInfo.setContratBillingInfoParent(contratBillingInfoParent);
							}
						}
					}

					if (elementContractuel.getSousElementContractuel() != null) {
						for (ElementContractuel contractuel : elementContractuel.getSousElementContractuel()) {
							for (ContratBillingInfo contratBillingInfoFils : contratBillingInfos) {
								if (contratBillingInfoFils.getReferenceContrat().equals(
										contractuel.getContratParent().getReference())
										&& contratBillingInfoFils.getNumEC().equals(contractuel.getNumEC())) {
									contratBillingInfoFils.setContratBillingInfoParent(contratBillingInfo);
								}
							}
						}
					}

					contratBillingInfos.add(contratBillingInfo);
					contratBillingInfo.setMigre(true);
				}
			}
		}

		return contratBillingInfos;
	}

	@Override
	public String getReferenceCommande(String referenceContrat) throws TopazeException {
		Contrat contrat = contratRepository.findByReference(referenceContrat);
		ContratValidator.checkExist(referenceContrat, contrat);
		for (ElementContractuel elementContractuel : contrat.getSousContrats()) {
			return elementContractuel.getNumeroCommande();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void ajouterCommentaire(String referenceContrat, String commentaire, String user) throws TopazeException {
		Contrat contrat = contratRepository.findByReference(referenceContrat);
		ContratValidator.checkExist(referenceContrat, contrat);
		getTracageService().ajouterTrace(Constants.PRODUCT, referenceContrat, commentaire, user);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> findReferenceContratsGlobalValider() {
		return contratRepository.findReferenceContratsGlobalValider();
	}

	@Override
	public ElementContractuel getElementContractuelleParId(Integer id) throws TopazeException {
		return elementContractuelRepository.findOne(id);
	}
}