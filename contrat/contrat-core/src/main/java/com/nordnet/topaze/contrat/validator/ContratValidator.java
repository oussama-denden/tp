package com.nordnet.topaze.contrat.validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.nordnet.topaze.client.rest.RestClientContratCore;
import com.nordnet.topaze.client.rest.business.contrat.ElementStateInfo;
import com.nordnet.topaze.client.rest.enums.TypeProduit;
import com.nordnet.topaze.contrat.business.ClientInfo;
import com.nordnet.topaze.contrat.business.ContratCession;
import com.nordnet.topaze.contrat.business.ContratChangerModePaiementInfo;
import com.nordnet.topaze.contrat.business.ContratValidationInfo;
import com.nordnet.topaze.contrat.business.Frais;
import com.nordnet.topaze.contrat.business.PaiementInfo;
import com.nordnet.topaze.contrat.business.PaiementModificationInfo;
import com.nordnet.topaze.contrat.business.PolitiqueCession;
import com.nordnet.topaze.contrat.business.PolitiqueResiliation;
import com.nordnet.topaze.contrat.business.Prix;
import com.nordnet.topaze.contrat.business.Produit;
import com.nordnet.topaze.contrat.business.ProduitCession;
import com.nordnet.topaze.contrat.business.ProduitMigration;
import com.nordnet.topaze.contrat.business.ProduitRenouvellement;
import com.nordnet.topaze.contrat.business.TState;
import com.nordnet.topaze.contrat.domain.Actions;
import com.nordnet.topaze.contrat.domain.Avenant;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ContratHistorique;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.domain.FraisContrat;
import com.nordnet.topaze.contrat.domain.ModeFacturation;
import com.nordnet.topaze.contrat.domain.ModePaiement;
import com.nordnet.topaze.contrat.domain.PolitiqueRenouvellement;
import com.nordnet.topaze.contrat.domain.TypeContrat;
import com.nordnet.topaze.contrat.domain.TypeFrais;
import com.nordnet.topaze.contrat.domain.TypeResiliation;
import com.nordnet.topaze.contrat.domain.TypeTVA;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.contrat.util.Utils;
import com.nordnet.topaze.contrat.util.spring.ApplicationContextHolder;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Valider un contrat.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class ContratValidator {

	/** The properties util. {@link PropertiesUtil}. */
	private static PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(ContratValidator.class);

	/**
	 * Valider les information de preparation d'un contrat global à partir d'une liste des produit.
	 * 
	 * @param produits
	 *            liste de {@link Produit}.
	 * @param isMigration
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 */
	public static void validerProduitsPourPreparationContrat(List<Produit> produits, boolean isMigration)
			throws TopazeException {
		Integer perodicite = null;
		ModeFacturation modeFacturation = null;
		if (produits == null || produits.size() == Constants.ZERO) {
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.93"), "1.1.93");
		}

		for (Produit produit : produits) {
			if (Utils.isStringNullOrEmpty(produit.getReference())) {
				throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.1.1",
						isMigration ? "ProduitDestination.reference" : "Produit.reference"), "0.1.1");
			}

			if (Utils.containsWhiteSpace(produit.getReference())) {
				throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.1.13",
						isMigration ? "ProduitDestination.reference" : "Produit.reference"), "0.1.13");
			}

			if (Utils.isStringNullOrEmpty(produit.getLabel())) {
				throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.1.1", "Produit.label"),
						"0.1.1");
			}

			if (Utils.isStringNullOrEmpty(produit.getNumeroCommande())) {
				throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.1.1",
						"Produit.numeroCommande"), "0.1.1");
			}

			if (produit.getTypeProduit() == null) {
				throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.1.11", "Produit.typeProduit",
						Arrays.asList(TypeProduit.values())), "0.1.11");
			}

			if (produit.getNumEC() == null) {
				throw new TopazeException(PropertiesUtil.getInstance()
						.getErrorMessage("1.1.68", produit.getReference()), "1.1.68");
			}

			if (produit.getNumEC() == produit.getNumECParent()) {
				throw new TopazeException(PropertiesUtil.getInstance()
						.getErrorMessage("1.1.70", produit.getReference()), "1.1.70");
			}

			validerPrix(produit);

			if (modeFacturation != null && produit.getPrix() != null
					&& !produit.getPrix().getModeFacturation().equals(modeFacturation)) {
				throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.137",
						produit.getReference()), "1.1.137");
			}

			if (produit.getPrix() != null) {
				modeFacturation = produit.getPrix().getModeFacturation();
			}

			if (produit.getPrix() != null && produit.getPrix().getPeriodicite() != null) {
				if (perodicite != null && produit.getPrix().getPeriodicite() != perodicite) {
					throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.136",
							produit.getReference()), "1.1.136");
				}
				perodicite = produit.getPrix().getPeriodicite();
			}
		}

		Set<Produit> produitsSet = new HashSet<>(produits);

		if (produitsSet.size() < produits.size()) {
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.30"), "1.1.30");
		}

		for (Produit produit : produits) {
			if (produit.hasParent()) {
				Produit parent = new Produit();
				parent.setNumEC(produit.getNumECParent());
				if (!produits.contains(parent)) {
					throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.69",
							produit.getReference(), produit.getNumEC()), "1.1.69");
				}
			}
		}

	}

	/**
	 * Tester la validite d'un {@link Prix}.
	 * 
	 * @param produit
	 *            {@link Produit}.
	 * @throws TopazeException
	 *             the topaze exception {@link Prix}. {@link TopazeException}.
	 */
	private static void validerPrix(Produit produit) throws TopazeException {

		if (produit != null) {
			if (produit.getPrix() != null) {
				if (produit.getTypeProduit() == TypeProduit.SERVICE) {

					if (!produit.isVente(produit.getPrix())) {
						throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.35"), "1.1.35");
					}

					if (!produit.isAbonnement(produit.getPrix())) {
						throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.34"), "1.1.34");
					}

				}

				if (produit.getTypeProduit() == TypeProduit.BIEN) {

					// vente de Bien
					if (!produit.isVente(produit.getPrix())) {
						throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.36"), "1.1.36");
					}

					// contrat de LOCATION ne peut pas avoir type de paiement trois fois sans frais
					if (!produit.isLocation(produit.getPrix())) {
						throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.34"), "1.1.34");
					}

				}

				// Validation sur la periodicite et l'engagement et la duree et le montant et le mode de paiement et le
				// mode
				// facturation et le type de TVA
				validerElementContrat(produit.getPrix());
				for (Frais frais : produit.getPrix().getFrais()) {
					validerFrais(frais);
				}

				validerFraisResiliation(produit.getPrix().getFraisResiliations());
			}

		}
	}

	/**
	 * @param prix
	 *            {@link Prix}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	private static void validerElementContrat(Prix prix) throws TopazeException {

		if (prix.getPeriodicite() != null && prix.getPeriodicite() <= 0) {
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.1.12", "Prix.periodicite"),
					"0.1.12");
		}

		if ((prix.getPeriodicite() == null) && (prix.getEngagement() != null && prix.getEngagement() > 0)) {
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.12", "Produit.typeProduit"),
					"1.1.12");
		}

		if (!Arrays.asList(TypeTVA.values()).contains(prix.getTypeTVA())) {
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.1.11", "Prix.typeTVA",
					Arrays.asList(TypeTVA.values())), "0.1.11");
		}

		if (prix.getTypeTVA() == null) {
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.1.4", "Prix.typeTVA"), "0.1.4");
		}

		if (prix.getMontant() == null) {
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.1.1", "Prix.montant"), "0.1.1");
		}
		// un produit ne peut pas avoir des montants negatifs
		if (prix.getMontant() < 0) {
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.90"), "1.1.90");
		}

		if (prix.getModePaiement() == null || prix.getModePaiement().equals(ModePaiement.AUTRE)) {
			List<ModePaiement> modePaiements = new ArrayList<>(Arrays.asList(ModePaiement.values()));
			modePaiements.remove(ModePaiement.AUTRE);
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.1.11", "Prix.modePaiement",
					modePaiements), "0.1.11");
		}

		if (prix.getModeFacturation() == null) {
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.1.11", "Prix.modeFacturation",
					Arrays.asList(ModeFacturation.values())), "0.1.11");
		}
	}

	/**
	 * Tester la validite d'un {@link FraisContrat}.
	 * 
	 * @param frais
	 *            the frais
	 * @throws TopazeException
	 *             the topaze exception {@link Frais}. {@link TopazeException}.
	 */
	private static void validerFrais(Frais frais) throws TopazeException {

		if (frais.getMontant() == null) {
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.1.4", "Frais.montant"), "0.1.4");
		}

		if (frais.getTypeFrais() == null) {
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.1.11", "Frais.typeFrais",
					Arrays.asList(TypeFrais.values())), "0.1.11");
		}

		if (frais.getTypeFrais().equals(TypeFrais.CREATION)
				&& (frais.getOrdre() != null || frais.getNombreMois() != null)) {
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.43"), "1.1.43");
		}
	}

	/**
	 * Valider les frais de resiliation.
	 * 
	 * @param fraisResiliations
	 *            Liste des frais de resiliation.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	private static void validerFraisResiliation(List<Frais> fraisResiliations) throws TopazeException {
		for (Frais frais : fraisResiliations) {
			if (fraisResiliations.size() > Constants.UN && frais.getOrdre() == null) {
				throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.44"), "1.1.44");
			}
			if (frais.getNombreMois() != null && frais.getNombreMois() == 0) {
				throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.48"), "1.1.48");
			}
		}
		int size = fraisResiliations.size();
		// verifier si deux frais n'ont pas le meme ordre.
		int k = 0;
		while (k < size) {
			int nbOccurence = 0;
			Integer ordre = fraisResiliations.get(k).getOrdre();
			for (int j = 0; j < size; j++) {
				if (fraisResiliations.get(j).getOrdre() == ordre) {
					nbOccurence += 1;
				}
			}
			if (nbOccurence > 1) {
				throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.45", ordre), "1.1.45");
			}
			k++;
		}

		// ordonner les liste des frais de resiliation.
		ordonnerFraisResiliation(fraisResiliations);

		// verifier si le frais sans nombre de mois est bien le dernier dans l'ordre.
		for (int i = 0; i < size; i++) {
			if (fraisResiliations.get(i).getNombreMois() == null && i != size - 1) {
				throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.46"), "1.1.46");
			}
		}

		// verifier si le dernier frais a un nombre de mois ou pas
		if (size > Constants.ZERO && fraisResiliations.get(size - 1).getNombreMois() != null) {
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.47"), "1.1.47");
		}
	}

	/**
	 * Dans la phase du validation contrat verifier si les informations de validation sont correcte.
	 * 
	 * @param contratValidationInfo
	 *            the contrat validation info
	 * @param elementContractuels
	 *            {@link ElementContractuel}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validerInfoValidationContratGlobal(ContratValidationInfo contratValidationInfo,
			Set<ElementContractuel> elementContractuels) throws TopazeException {

		List<PaiementInfo> paiementInfos = contratValidationInfo.getPaiementInfos();

		if (contratValidationInfo.getPaiementInfos() != null && contratValidationInfo.getPaiementInfos().size() > 0) {

			if (Utils.isStringNullOrEmpty(contratValidationInfo.getIdClient()))
				throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "Contrat.idClient"), "0.1.1");
			if (Utils.isStringNullOrEmpty(contratValidationInfo.getIdAdrFacturation()))
				throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "Contrat.idAdrFacturation"), "0.1.1");
			List<String> indicatifTVA = Arrays.asList("00", "01", "10", "11");
			if (Utils.isStringNullOrEmpty(contratValidationInfo.getSegmentTVA())) {
				throw new TopazeException(propertiesUtil.getErrorMessage("0.1.4", "Contrat.segmentTVA"), "0.1.4");
			} else if (!indicatifTVA.contains(contratValidationInfo.getSegmentTVA())) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.142"), "1.1.142");
			}

			for (ElementContractuel elementContractuel : elementContractuels) {
				for (PaiementInfo paiementInfo : paiementInfos) {
					if (Utils.isStringNullOrEmpty(paiementInfo.getIdAdrLivraison())) {
						throw new TopazeException(
								propertiesUtil.getErrorMessage("0.1.1", "paiementInfo.idAdrLivraison"), "0.1.1");
					}
					if (elementContractuel.getReferenceProduit().equals(paiementInfo.getReferenceProduit())
							&& elementContractuel.getNumEC() == paiementInfo.getNumEC()) {

						if (elementContractuel.getTypeProduit().equals(TypeProduit.BIEN)
								&& Utils.isStringNullOrEmpty(paiementInfo.getIdAdrLivraison())) {
							throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.94",
									elementContractuel.getReferenceProduit()), "1.1.94");
						}

						if (elementContractuel.getModePaiement() != null
								&& (elementContractuel.getModePaiement().equals(ModePaiement.SEPA)
										|| elementContractuel.getModePaiement()
												.equals(ModePaiement.FACTURE_FIN_DE_MOIS) || elementContractuel
										.getModePaiement().equals(ModePaiement.FACTURE))
								&& Utils.isStringNullOrEmpty(paiementInfo.getReferenceModePaiement())) {
							throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.37"), "1.1.37");
						}
					}

				}

			}

			List<PaiementInfo> contratsPaiementInfos = new ArrayList<>();

			for (ElementContractuel elementContractuel : elementContractuels) {
				PaiementInfo info = new PaiementInfo();
				info.setNumEC(elementContractuel.getNumEC());
				info.setReferenceProduit(elementContractuel.getReferenceProduit());
				contratsPaiementInfos.add(info);
			}

			if (paiementInfos.size() != contratsPaiementInfos.size()) {
				throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.31"), "1.1.31");
			}

			Set<PaiementInfo> set1 = new HashSet<>();
			set1.addAll(contratsPaiementInfos);
			Set<PaiementInfo> set2 = new HashSet<>();
			set2.addAll(paiementInfos);

			if (!set1.equals(set2)) {
				throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("1.1.31"), "1.1.31");
			}

		} else {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.7"), "1.1.7");
		}

	}

	/**
	 * Valider les informations du paiement d'un sous contrats.
	 * 
	 * @param paiementInfo
	 *            the paiement info
	 * @throws TopazeException
	 *             the information validation contrat non valide exception {@link PaiementInfo}. {@link TopazeException}
	 *             .
	 */
	public static void validerPaiementInfo(PaiementInfo paiementInfo) throws TopazeException {

		if (Utils.isStringNullOrEmpty(paiementInfo.getReferenceProduit()))
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PaiementInfo.referenceProduit"), "0.1.1");

		if (Utils.isStringNullOrEmpty(paiementInfo.getReferenceModePaiement()))
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PaiementInfo.referenceModePaiement"),
					"0.1.1");

		if (Utils.isStringNullOrEmpty(paiementInfo.getIdAdrLivraison()))
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "Contrat.idAdrLivraison"), "0.1.1");

	}

	/**
	 * Tester si tous les produits fournis au cours de validation sont associee a des sous contrats.
	 * 
	 * @param paiementInfos
	 *            les informations du paiement de sous contrats.
	 * @param sousContrats
	 *            the sous contrats.
	 * @param referenceContrat
	 *            reference du contrat.
	 * @throws TopazeException
	 *             the information validation contrat non valide exception {@link Contra}. {@link TopazeException}.
	 */
	public static void validerCorrectSousContratsEtProduits(List<PaiementInfo> paiementInfos,
			Set<ElementContractuel> sousContrats, String referenceContrat) throws TopazeException {
		List<String> referencesProduits = new ArrayList<>();
		List<String> referencesProduitsValidation = new ArrayList<>();

		for (ElementContractuel c : sousContrats) {
			referencesProduits.add(c.getReferenceProduit());
		}

		for (PaiementInfo paiementInfo : paiementInfos) {
			referencesProduitsValidation.add(paiementInfo.getReferenceProduit());
		}

		if (referencesProduitsValidation.size() != referencesProduits.size())
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "modePaiement"), "0.1.1");

		for (String referenceProduitValidation : referencesProduitsValidation) {
			if (!referencesProduits.contains(referenceProduitValidation)) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.1", referenceProduitValidation,
						referenceContrat), "1.1.1");
			}
		}

	}

	/**
	 * Tester si tous les produits fournis au cours de validation sont associee a des sous contrats.
	 * 
	 * @param ContratChangerModePaiementInfo
	 *            les informations du paiement de sous contrats.
	 * @param sousContrats
	 *            les sous contrats.
	 * @param referenceContrat
	 *            reference du contrat.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validerChangerModePaiment(ContratChangerModePaiementInfo ContratChangerModePaiementInfo,
			Set<ElementContractuel> sousContrats, String referenceContrat) throws TopazeException {
		List<String> referencesProduits = new ArrayList<>();
		List<String> referencesProduitsValidation = new ArrayList<>();
		Date dateJour = PropertiesUtil.getInstance().getDateDuJour().toDate();
		Date dateAction = ContratChangerModePaiementInfo.getDateAction();

		if (Utils.compareDate(dateJour, dateAction) == 1) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.67"), "1.1.67");
		}

		for (ElementContractuel c : sousContrats) {
			referencesProduits.add(c.getReferenceProduit());
		}

		for (PaiementModificationInfo paiementInfo : ContratChangerModePaiementInfo.getProduits()) {
			referencesProduitsValidation.add(paiementInfo.getReferenceProduit());
		}

		for (String referenceProduitValidation : referencesProduitsValidation) {
			if (!referencesProduits.contains(referenceProduitValidation)) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.1", referenceProduitValidation,
						referenceContrat), "1.1.1");
			}
		}

	}

	/**
	 * Valider l'operation de changement de date debut facturation.
	 * 
	 * @param dateDebutFacturation
	 *            date debut facturation.
	 * @param contrat
	 *            the contrat
	 * @throws TopazeException
	 *             the description contrat non valide exception {@link Contrat}. {@link TopazeException}.
	 */
	public static void validerChangerDateDebutFacturation(Date dateDebutFacturation, Contrat contrat)
			throws TopazeException {
		// checkContratValider(contrat);

		if (dateDebutFacturation == null || contrat.getDateValidation().after(dateDebutFacturation)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.3", sdf.format(dateDebutFacturation),
					sdf.format(contrat.getDateValidation())), "1.1.3");
		}

	}

	/**
	 * Valider l'operation d'extraction des informations de billing d'un contrat.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param contrat
	 *            the contrat
	 * @throws TopazeException
	 *             the description contrat non valide exception {@link Contrat}. {@link TopazeException}.
	 */
	public static void validerGetContratBillingInformation(String referenceContrat, Contrat contrat)
			throws TopazeException {
		checkExist(referenceContrat, contrat);
		checkContratValiderOuReslier(contrat);
	}

	/**
	 * Check contrat valider ou reslier.
	 * 
	 * @param contrat
	 *            the contrat
	 * @throws TopazeException
	 *             the description contrat non valide exception
	 */
	private static void checkContratValiderOuReslier(Contrat contrat) throws TopazeException {
		if (!contrat.isResilier() && !contrat.isValider()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.6", contrat.getReference()), "1.1.6");
		}

	}

	/**
	 * Valider get contrat billing information.
	 * 
	 * @param referenceContrat
	 *            the reference contrat
	 * @param elementContractuel
	 *            the element contractuel
	 * @throws TopazeException
	 *             the description contrat non valide exception
	 */
	public static void validerGetContratBillingInformation(String referenceContrat,
			ElementContractuel elementContractuel) throws TopazeException {
		checkExist(referenceContrat, elementContractuel);
		checkElementContractuelValiderOuReslier(elementContractuel);
	}

	/**
	 * Valider l'operation d'extraction des informations utils a la livraison des biens et des services d'un contrat.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param contrat
	 *            the contrat
	 * @throws TopazeException
	 *             the description contrat non valide exception {@link Contrat}. {@link ElementContractuel}.
	 */
	public static void validerContratExistValider(String referenceContrat, Contrat contrat) throws TopazeException {
		checkExist(referenceContrat, contrat);
		checkContratValider(contrat);
	}

	/**
	 * Verifier action modification.
	 * 
	 * @param action
	 *            l'action
	 * @throws TopazeException
	 *             the contrat exception {@link TopazeException}.
	 */
	public static void verifierActionModification(String action) throws TopazeException {

		if (Actions.fromString(action) == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.10", action), "1.1.10");
		}
	}

	/**
	 * Tester si la resiliation est possible ou non.
	 * 
	 * @param contrat
	 *            the contrat
	 * @param isResiliationFutur
	 *            utilisé par le batch de resiliation future, le jour de resiliation cette boolean prend la valeur
	 *            false.
	 * @throws TopazeException
	 *             the description contrat non valide exception
	 * @throws TopazeException
	 *             the contrat resiliation exception {@link Contrat}. {@link TopazeException}. {@link TopazeException}.
	 */
	public static void checkResiliationPossible(Contrat contrat, boolean isResiliationFutur) throws TopazeException {

		checkContratNonResilie(contrat);

		if (!contrat.isValider()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.41"), "1.1.41");
		}

		else if (contrat.getPolitiqueResiliation() != null
				&& contrat.getPolitiqueResiliation().getDateAnnulation() == null && isResiliationFutur) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.130", "Resiliation", "contrat",
					contrat.getReference()), "1.1.130");
		}

		boolean isContratVente = true;

		CheckContratVente: for (ElementContractuel elementContractuel : contrat.getSousContrats()) {
			if (!elementContractuel.getTypeContrat().equals(TypeContrat.VENTE)) {
				isContratVente = false;
				break CheckContratVente;
			}
		}

		if (isContratVente) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.95"), "1.1.95");
		}

	}

	/**
	 * Tester si la resiliation partiel est possible ou non on validant si le produit existe dans le contrat global.
	 * 
	 * @param referenceContrat
	 *            the reference contrat
	 * @param numEC
	 *            numero de {@link ElementContractuel}.
	 * @param contrat
	 *            the contrat
	 * @param isResiliationFuture
	 *            utilisé par le batch de resiliation future, le jour de resiliation cette boolean prend la valeur
	 *            false.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void checkResiliationPartielPossible(String referenceContrat, Integer numEC, Contrat contrat,
			boolean isResiliationFuture) throws TopazeException {
		boolean exist = false;

		CheckResilier: for (ElementContractuel elementContractuel : contrat.getSousContrats()) {
			if (elementContractuel.getNumEC() == numEC) {
				if (elementContractuel.getTypeContrat().equals(TypeContrat.VENTE)) {
					throw new TopazeException(propertiesUtil.getErrorMessage("1.1.29", elementContractuel
							.getContratParent().getReference() + "-" + elementContractuel.getNumEC()), "1.1.29");
				}

				if (elementContractuel.isResilier()) {
					throw new TopazeException(propertiesUtil.getErrorMessage("1.1.14", numEC, referenceContrat),
							"1.1.14");
				} else if (elementContractuel.getPolitiqueResiliation() != null
						&& elementContractuel.getPolitiqueResiliation().getDateAnnulation() == null
						&& isResiliationFuture) {
					throw new TopazeException(propertiesUtil.getErrorMessage("1.1.130", "Resiliation",
							"element contractuel", elementContractuel.getContratParent().getReference() + "-"
									+ elementContractuel.getNumEC()), "1.1.130");
				}

				exist = true;
				break CheckResilier;
			}
		}
		if (!exist) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.71", referenceContrat, numEC), "1.1.71");
		}

		if (!contrat.isValider()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.41"), "1.1.41");
		}

	}

	/**
	 * valider qu'un {@link Contrat} n'est pas resilie.
	 * 
	 * @param contrat
	 *            {@link Contrat}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void checkContratNonResilie(final Contrat contrat) throws TopazeException {
		if (contrat.isResilier()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.9", contrat.getReference()), "1.1.9");
		}
	}

	/**
	 * Tester si un sous contrat est resilier ou non.
	 * 
	 * @param sousContrat
	 *            the sous contrat
	 * @throws TopazeException
	 *             the contrat resiliation exception {@link ElementContractuel}. {@link TopazeException}.
	 */
	public static void checkSousContratNonResilier(ElementContractuel sousContrat) throws TopazeException {
		if (sousContrat.isResilier()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.9", sousContrat.getContratParent()
					.getReference() + "-" + sousContrat.getNumEC()), "1.1.9");
		}

	}

	/**
	 * Verifier si le contrat n'est pas existant dans la base.
	 * 
	 * @param contrat
	 *            the contrat
	 * @throws TopazeException
	 *             the description contrat non valide exception {@link ElementContractuel}. {@link TopazeException}.
	 */
	public static void checkNotExist(ElementContractuel sousContrat) throws TopazeException {
		if (sousContrat != null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.3", sousContrat.getContratParent()
					.getReference() + "-" + sousContrat.getNumEC()), "0.1.3");
		}
	}

	/**
	 * Verifier si le contrat exist.
	 * 
	 * @param reference
	 *            reference du produit.
	 * @param contrat
	 *            the contrat
	 * @throws TopazeException
	 *             the description contrat non valide exception {@link Contrat}. {@link TopazeException}.
	 */
	public static void checkExist(String reference, Contrat contrat) throws TopazeException {
		if (contrat == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.2", reference), "0.1.2");
		}
	}

	/**
	 * Check exist.
	 * 
	 * @param reference
	 *            reference element contractuel.
	 * @param elementContractuel
	 *            the element contractuel
	 * @throws TopazeException
	 *             the description contrat non valide exception {@link ElementContractuel} {@link TopazeException}.
	 */
	public static void checkExist(String reference, ElementContractuel elementContractuel) throws TopazeException {
		if (elementContractuel == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.2", reference), "0.1.2");
		}
	}

	/**
	 * Check exist.
	 * 
	 * @param reference
	 *            reference element contractuel.
	 * @param elementContractuel
	 *            the element contractuel
	 * @throws TopazeException
	 *             the description contrat non valide exception {@link ElementContractuel} {@link TopazeException}.
	 */
	public static void checkExist(String reference, Integer numEC, ElementContractuel elementContractuel)
			throws TopazeException {
		if (elementContractuel == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.71", reference, numEC), "1.1.71");
		}
	}

	/**
	 * Verifier si le contratHistorique exist dans la base.
	 * 
	 * @param reference
	 *            reference du contrat.
	 * @param contratHistorique
	 *            {@link ContratHistorique}
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void checkExist(String referenceContrat, ContratHistorique contratHistorique) throws TopazeException {
		if (contratHistorique == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.2", referenceContrat), "0.1.2");
		}

	}

	/**
	 * Tester si un contrat est deja valider.
	 * 
	 * @param contrat
	 *            the contrat
	 * @throws TopazeException
	 *             the description contrat non valide exception {@link Contrat}. {@link TopazeException}.
	 */
	public static void checkContratValider(Contrat contrat) throws TopazeException {
		if (!contrat.isValider()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.6", contrat.getReference()), "1.1.6");
		}

	}

	/**
	 * Tester si un element contractuel est deja valider.
	 * 
	 * @param elementContractuel
	 *            the element contractuel
	 * @throws TopazeException
	 *             the description contrat non valide exception {@link ElementContractuel}. {@link TopazeException}.
	 */
	public static void checkElementContractuelValiderOuReslier(ElementContractuel elementContractuel)
			throws TopazeException {
		if (!elementContractuel.isResilier() && !elementContractuel.isValider()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.14", elementContractuel.getContratParent()
					.getReference() + "-" + elementContractuel.getNumEC()), "1.1.14");
		}
	}

	/**
	 * Tester si un element contractuel est deja résilié.
	 * 
	 * @param elementContractuel
	 *            the element contractuel
	 * @throws TopazeException
	 *             the description contrat non valide exception {@link ElementContractuel}. {@link TopazeException}.
	 */
	public static void checkElementContractuelReslier(ElementContractuel elementContractuel) throws TopazeException {
		if (elementContractuel.isResilier()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.32", elementContractuel.getContratParent()
					.getReference() + "-" + elementContractuel.getNumEC()), "1.1.32");
		}
	}

	/**
	 * Tester si un contrat n'est pas valider.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param contrat
	 *            the contrat
	 * @throws TopazeException
	 *             the description contrat non valide exception {@link Contrat}. {@link TopazeException}.
	 */
	public static void checkContratNonValider(String referenceContrat, Contrat contrat) throws TopazeException {
		checkExist(referenceContrat, contrat);
		if (contrat.isValider()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.5", contrat.getReference()), "1.1.5");
		}

	}

	/**
	 * valider si le {@link Contrat} resilie.
	 * 
	 * @param contrat
	 *            {@link Contrat}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validerContratNonResiler(final Contrat contrat) throws TopazeException {
		if (contrat.isResilier()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.149", contrat.getReference()), "1.1.149");
		}
	}

	/**
	 * Verifier date valide.
	 * 
	 * @param stringDate
	 *            date en format string
	 * @return date
	 * @throws TopazeException
	 *             forcontrat exception
	 */
	public static Date verifierDateValide(String stringDate) throws TopazeException {
		DateFormat formatter = Constants.DEFAULT_DATE_WITHOUT_TIME_FORMAT;
		Date date = null;
		try {
			date = formatter.parse(stringDate);
			LocalDateTime dateIn = new LocalDateTime(date);
			LocalTime time = new LocalTime();
			return dateIn.withTime(time.hourOfDay().get(), time.minuteOfHour().get(), time.secondOfMinute().get(),
					time.millisOfSecond().get()).toDate();
		} catch (ParseException e) {
			LOGGER.error("Error occurs during parse date", e);
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.8", stringDate), "0.1.8");
		}

	}

	/**
	 * Verifier date valide.
	 * 
	 * @param stringDate
	 *            date en format string
	 * @return date
	 * @throws TopazeException
	 *             forcontrat exception
	 */
	public static Date verifierDateWithoutTimeValide(String stringDate) throws TopazeException {
		DateFormat formatter = Constants.DEFAULT_DATE_WITHOUT_TIME_FORMAT;
		Date date = null;
		try {
			date = formatter.parse(stringDate);
		} catch (ParseException e) {
			LOGGER.error("Error occurs during parse date", e);
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.8", stringDate), "0.1.8");
		}
		return date;

	}

	/**
	 * Checks if is exist element contractuel.
	 * 
	 * @param referenceProduit
	 *            the reference produit
	 * @param identifiantProduit
	 *            the identifiant produit
	 * @throws TopazeException
	 *             the contrat exception
	 */
	public static void isExistElementContractuel(String referenceProduit, String identifiantProduit)
			throws TopazeException {

		throw new TopazeException(propertiesUtil.getErrorMessage("1.1.16", referenceProduit, identifiantProduit == null
				? "" : "" + identifiantProduit), "1.1.16");
	}

	/**
	 * Verifier que la politique de validation est obligatoire.
	 * 
	 * @param contratValidationInfo
	 *            the contrat validation info
	 * @throws TopazeException
	 *             the contrat exception
	 */
	public static void checkPolitiqueValidation(ContratValidationInfo contratValidationInfo) throws TopazeException {
		if (contratValidationInfo.getPolitiqueValidation() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.17", "validation"), "1.1.17");
		}
		if (contratValidationInfo.getPolitiqueValidation().isFraisCreation() == null
				&& contratValidationInfo.getPolitiqueValidation().getCheckIsPackagerCreationPossible() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.17", "validation"), "1.1.17");
		}

		if (contratValidationInfo.getPolitiqueValidation().isFraisCreation() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PolitiqueValidation.fraisCreation"),
					"0.1.1");
		}
		if (contratValidationInfo.getPolitiqueValidation().getCheckIsPackagerCreationPossible() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1",
					"PolitiqueValidation.checkIsPackagerCreationPossible"), "0.1.1");
		}

	}

	/**
	 * Verifier que la politique de validation est obligatoire.
	 * 
	 * @param politiqueResiliation
	 *            the politique resiliation
	 * @throws TopazeException
	 *             the contrat exception
	 */
	public static void checkPolitiqueResiliation(
			com.nordnet.topaze.contrat.business.PolitiqueResiliation politiqueResiliation) throws TopazeException {
		if (politiqueResiliation == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.17", "résiliation"), "1.1.17");
		} else if (politiqueResiliation.getTypeResiliation() == null
				&& politiqueResiliation.isFraisResiliation() == null && politiqueResiliation.isPenalite() == null
				&& politiqueResiliation.isRemboursement() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.17", "résiliation"), "1.1.17");
		} else if (politiqueResiliation.getTypeResiliation() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.11", "PolitiqueResiliation.typeResiliation",
					Arrays.asList(TypeResiliation.values())), "0.1.1");
		}
		if (politiqueResiliation.isFraisResiliation() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PolitiqueResiliation.fraisResiliation"),
					"0.1.1");
		}
		if (politiqueResiliation.isPenalite() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PolitiqueResiliation.penalite"), "0.1.1");
		}
		if (politiqueResiliation.isRemboursement() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PolitiqueResiliation.remboursement"),
					"0.1.1");
		}
		if (!Utils.isStringNullOrEmpty(politiqueResiliation.getDateResiliation())
				&& Utils.compareDate(
						ContratValidator.verifierDateWithoutTimeValide(politiqueResiliation.getDateResiliation()),
						PropertiesUtil.getInstance().getDateDuJour().toDate()) == -1) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.92", "PolitiqueResiliation.dateResiliation"),
					"1.1.92");
		}
		if (!politiqueResiliation.isFraisResiliation() && politiqueResiliation.getMontantResiliation() != null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.102"), "1.1.102");
		}

	}

	/**
	 * Verifier que la politique de migration est obligatoire.
	 * 
	 * @param politiqueMigration
	 *            the politique Migration
	 * @throws TopazeException
	 *             the contrat exception
	 */
	public static void checkPolitiqueMigration(com.nordnet.topaze.contrat.business.PolitiqueMigration politiqueMigration)
			throws TopazeException {
		if (politiqueMigration == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.17", "migration"), "1.1.17");
		} else if (politiqueMigration.isReductionAncienne() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PolitiqueMigration.reductionAncienne"),
					"0.1.1");
		} else if (politiqueMigration.isPenalite() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PolitiqueMigration.penalite"), "0.1.1");
		} else if (politiqueMigration.isFraisCreation() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PolitiqueMigration.fraisCreation"),
					"0.1.1");
		} else if (politiqueMigration.isFraisResiliation() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PolitiqueMigration.fraisResiliation"),
					"0.1.1");
		} else if (politiqueMigration.isRemboursement() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PolitiqueMigration.remboursement"),
					"0.1.1");
		}
		if (politiqueMigration.getDateAction() != null
				&& Utils.compareDate(politiqueMigration.getDateAction(), PropertiesUtil.getInstance().getDateDuJour()
						.toDate()) == -1) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.110", "PolitiqueResiliation.dateAction"),
					"1.1.110");
		}
		if (politiqueMigration.isRemboursement() == false && politiqueMigration.getMontantRemboursement() != null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.125", "PolitiqueMigration.remboursement"),
					"1.1.125");
		}

	}

	/**
	 * Verifier que la demande de migration est future pour etre annuler .
	 * 
	 * @param politiqueMigration
	 *            the politique Migration
	 * @throws TopazeException
	 */
	public static void checkAnnulationMigrationPossible(
			com.nordnet.topaze.contrat.domain.PolitiqueMigration politiqueMigration) throws TopazeException {
		if (!(Utils.compareDate(politiqueMigration.getDateAction(), PropertiesUtil.getInstance().getDateDuJour()
				.toDate()) == 1)) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.78", "migration"), "1.1.78");
		}

	}

	/**
	 * Verifier que la politique de migration associé a ce contrat exist.
	 * 
	 * @param politiqueMigration
	 *            the politique Migration
	 * @throws TopazeException
	 *             the contrat exception
	 */
	public static void checkPolitiqueMigrationExist(
			com.nordnet.topaze.contrat.domain.PolitiqueMigration politiqueMigration, String refContrat)
			throws TopazeException {
		if (politiqueMigration == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.79", "migration", refContrat), "1.1.79");

		}

	}

	/**
	 * Verifier que la politique de migration associé a ce contrat exist.
	 * 
	 * @param politiqueMigration
	 *            the politique Migration
	 * @throws TopazeException
	 *             the contrat exception
	 */
	public static void checkPolitiqueResiliationExist(
			com.nordnet.topaze.contrat.domain.PolitiqueResiliation politiqueMResiliation, String refContrat,
			boolean isContrat) throws TopazeException {
		if (politiqueMResiliation == null) {
			if (isContrat) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.104", refContrat), "1.1.104");
			}
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.105", refContrat), "1.1.105");

		}

	}

	/**
	 * Verifier que la demande de migration est future pour etre annuler .
	 * 
	 * @param contratResilier
	 *            {@link #contrat}
	 * @throws TopazeException
	 */
	public static void checkAnnulationResiliationPossible(Contrat contratResilier) throws TopazeException {
		if (contratResilier.getDateFinContrat() != null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.103", "resiliation"), "1.1.103");
		}

	}

	/**
	 * Verifier que la politique de renouvellement associé a ce contrat exist
	 * 
	 * @param politiqueRenouvellement
	 * @param refContrat
	 * @throws TopazeException
	 */
	public static void checkPolitiqueRenouvellementExist(
			com.nordnet.topaze.contrat.domain.PolitiqueRenouvellement politiqueRenouvellement, String refContrat)
			throws TopazeException {
		if (politiqueRenouvellement == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.79", "Renouvellement", refContrat), "1.1.79");

		}

	}

	/**
	 * Verifier que la demande de migration est future pour etre annuler .
	 * 
	 * @param politiqueMigration
	 *            the politique Migration
	 * @throws TopazeException
	 */
	public static void checkAnnulationRenouvellementPossible(Avenant avenant) throws TopazeException {
		if (avenant.getVersion() != null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.129"), "1.1.129");
		}

	}

	/**
	 * Vérifier que le contrat n'est un contrat de vente.
	 * 
	 * @param elementContractuel
	 *            element contractuel
	 * @param modification
	 *            modification
	 * @throws TopazeException
	 *             the contrat exception
	 */
	public static void isNotContratVente(ElementContractuel elementContractuel, String modification)
			throws TopazeException {
		if (elementContractuel.getTypeContrat().equals(TypeContrat.VENTE)) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.18", modification, elementContractuel
					.getContratParent().getReference() + "-" + elementContractuel.getNumEC()), "1.1.18");
		}

	}

	/**
	 * Verifier que l'element contratctuel n'est le parent de lui-même.
	 * 
	 * @param refProduitParent
	 *            la reference produit parent
	 * @throws TopazeException
	 *             the contrat exception
	 */
	public static void checkChildIsNotParent(String refProduitParent) throws TopazeException {
		throw new TopazeException(propertiesUtil.getErrorMessage("1.1.33", refProduitParent), "1.1.33");

	}

	/**
	 * Verifier arborescence.
	 * 
	 * @param elementContractuel
	 *            the element contractuel
	 * @throws TopazeException
	 *             the contrat exception
	 */
	public static void verifierArborescence(ElementContractuel elementContractuel) throws TopazeException {
		ElementContractuel premierParent = elementContractuel.getElementContractuelParent();
		ElementContractuel dernierParent = elementContractuel;
		while (premierParent.hasParent()) {
			if (premierParent.getContratParent().getReference()
					.equals(elementContractuel.getContratParent().getReference())
					&& (premierParent.getNumEC().equals(elementContractuel.getNumEC()))) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.42",
						elementContractuel.getReferenceProduit(), elementContractuel.getNumEC(),
						dernierParent.getReferenceProduit(), dernierParent.getNumEC()), "1.1.42");
			}
			dernierParent = premierParent;
			premierParent = premierParent.getElementContractuelParent();
		}

	}

	/**
	 * Valider si un element contractuel a un parent.
	 * 
	 * @param elementContractuel
	 *            {@link ElementContractuel}
	 * @param referenceElementContractuel
	 *            reference de {@link ElementContractuel}
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public static void validerGetReferenceParent(ElementContractuel elementContractuel,
			String referenceElementContractuel) throws TopazeException {
		if (elementContractuel == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.2", referenceElementContractuel), "0.1.2");
		}
		if (elementContractuel.getElementContractuelParent() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.49", referenceElementContractuel), "1.1.49");
		}
	}

	/**
	 * Ordonner la liste des frais de resiliation selon un ordre croissant.
	 * 
	 * @param fraisResiliations
	 *            frais resiliations
	 */
	private static void ordonnerFraisResiliation(List<Frais> fraisResiliations) {
		int size = fraisResiliations.size();
		boolean ordonner = true;
		Frais fraisTmp = null;
		while (ordonner) {
			ordonner = false;
			for (int i = 0; i < size - 1; i++) {
				if (fraisResiliations.get(i).getOrdre() > fraisResiliations.get(i + 1).getOrdre()) {
					fraisTmp = fraisResiliations.get(i);
					fraisResiliations.remove(i);
					fraisResiliations.add(i, fraisResiliations.get(i));
					fraisResiliations.remove(i + 1);
					fraisResiliations.add(i + 1, fraisTmp);
					ordonner = true;
				}
			}
		}
	}

	/**
	 * Valider la migration.
	 * 
	 * @param contrat
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public static void checkMigration(Contrat contrat) throws TopazeException {

		if (contrat.isResilier()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.73"), "1.1.73");
		}

		else if (!contrat.isValider()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.72"), "1.1.72");
		}

	}

	/**
	 * Valider ligne de produit migration.
	 * 
	 * @param produitMigration
	 * @param elementContractuel
	 *            {@link ElementContractuel}
	 * @throws TopazeException
	 * @{@link TopazeException}
	 */
	public static void checkProduitMigration(ProduitMigration produitMigration, ElementContractuel elementContractuel)
			throws TopazeException {
		if (elementContractuel == null
				&& (!Utils.isStringNullOrEmpty(produitMigration.getReferenceProduitSource()) || !Utils
						.isStringNullOrEmpty(produitMigration.getReferenceGammeSource()))) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.74"), "1.1.74");
		} else if (produitMigration.getNumEC() != null
				&& (Utils.isStringNullOrEmpty(produitMigration.getReferenceProduitSource()) || Utils
						.isStringNullOrEmpty(produitMigration.getReferenceGammeSource()))) {
			if (elementContractuel != null) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.75"), "1.1.75");
			}
		}

		if (produitMigration.getNumEC() != null
				&& (Utils.isStringNullOrEmpty(produitMigration.getReferenceProduitDestination()) || Utils
						.isStringNullOrEmpty(produitMigration.getReferenceGammeDestination()))) {

			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.87", produitMigration.getNumEC()), "1.1.87");
		}

		if (produitMigration.getTypeProduit() == null) {
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.1.11",
					"ProduitMigration.typeProduit", Arrays.asList(TypeProduit.values())), "0.1.11");
		}

		if (elementContractuel != null
				&& !produitMigration.getReferenceProduitSource().equals(elementContractuel.getReferenceProduit())) {

			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.76", produitMigration.getNumEC(),
					produitMigration.getReferenceProduitSource(), elementContractuel.getReferenceProduit()), "1.1.76");
		}

		if (elementContractuel != null
				&& !produitMigration.getTypeProduit().equals(elementContractuel.getTypeProduit())) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.77",
					elementContractuel.getReferenceProduit(), produitMigration.getReferenceProduitSource()), "1.1.77");
		}

		if (produitMigration.getNumECParent() == null && produitMigration.getPrix() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.131"), "1.1.131");
		}

		if (elementContractuel != null && elementContractuel.getTypeContrat().equals(TypeContrat.VENTE)) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.133"), "1.1.133");
		}
	}

	/**
	 * Valider contrat pour cession.
	 * 
	 * @param contrat
	 *            {@link Contrat}
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public static void checkContratPourCession(Contrat contrat) throws TopazeException {
		if (!contrat.isValider()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.80"), "1.1.80");
		}

		if (contrat.isResilier()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.81"), "1.1.81");
		}

	}

	/**
	 * Valider la cession d'un {@link Contrat}.
	 * 
	 * @param contratCession
	 *            {@link ContratCession}.
	 * @param contrat
	 *            {@link Contrat}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validerContratCession(ContratCession contratCession, Contrat contrat) throws TopazeException {

		if (contratCession.getClientSource() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.4", "Client Source"), "0.1.4");
		}

		if (contratCession.getClientDestination() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.4", "Client Destination"), "0.1.4");
		}

		/*
		 * valider les infos de cession
		 */
		if (contratCession.getProduitsCession().size() != contrat.getSousContrats().size()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.106"), "1.1.106");
		}

		for (ElementContractuel elementContractuel : contrat.getSousContrats()) {
			ProduitCession produitCession = new ProduitCession();
			produitCession.setNumEC(elementContractuel.getNumEC());
			if (!contratCession.getProduitsCession().contains(produitCession)) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.107", produitCession.getNumEC()),
						"1.1.107");
			}
		}

		for (ProduitCession produitCession : contratCession.getProduitsCession()) {
			if (produitCession.getModePaiement() == null) {
				throw new TopazeException(propertiesUtil.getErrorMessage("0.1.11", "modePaiement",
						Arrays.asList(ModePaiement.values())), "0.1.11");
			}

			if (Utils.isStringNullOrEmpty(produitCession.getReferenceModePaiement())) {
				throw new TopazeException(propertiesUtil.getErrorMessage("0.1.4", "referenceModePaiement"), "0.1.4");
			}

			if (produitCession.getDuree() != null && produitCession.getDuree() == Constants.ZERO) {
				throw new TopazeException(propertiesUtil.getErrorMessage("0.1.10", "ProduitCession.duree"), "0.1.10");
			}
		}

		/*
		 * valider les infos recu avec celles dans la base de données.
		 */
		if (!contrat.isMemeIdAdrLivraison()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.86"), "1.1.86");
		}

		ElementContractuel elContractuel = null;
		for (ElementContractuel elementContractuel : contrat.getSousContrats()) {
			if (contrat.isMemeIdAdrLivraison()
					&& !contratCession.getIdAdrLivraison().equals(elementContractuel.getIdAdrLivraison())) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.82", "clientSource.idAdrLivraison"),
						"1.1.82");
			}
			elContractuel = elementContractuel;
			break;
		}

		ClientInfo clientSource = contratCession.getClientSource();
		if (!clientSource.getIdClient().equals(contrat.getIdClient())) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.82", "clientSource.idClient"), "1.1.82");
		}

		if (!clientSource.getIdAdrFacturation().equals(elContractuel.getIdAdrFacturation())) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.82", "clientSource.idAdrFacturation"),
					"1.1.82");
		}

		ClientInfo clientDestination = contratCession.getClientDestination();
		if (Utils.isStringNullOrEmpty(clientDestination.getIdClient())) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "ClientDestination.idClient"), "0.1.1");
		}

		if (Utils.isStringNullOrEmpty(clientDestination.getIdAdrFacturation())) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "ClientDestination.idAdrFacturation"),
					"0.1.1");
		}

		/*
		 * la cession d'un contrat ne peut etre effectuer au meme client d'origine.
		 */
		if (clientSource.getIdAdrFacturation().equals(clientDestination.getIdAdrFacturation())
				|| clientSource.getIdClient().equals(clientDestination.getIdClient())) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.140"), "1.1.140");
		}

		/*
		 * valider la dateAction de la cession.
		 */
		PolitiqueCession pCession = contratCession.getPolitiqueCession();

		if (contratCession.getPolitiqueCession() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.4", "Politique Cession"), "0.1.4");
		}

		Date dateAction = pCession.getDateAction();

		if (pCession.isConserverAncienneReduction() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.111"), "1.1.111");
		}

		if (pCession.isRemboursement() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.4", "PolitiqueCession.remboursement"),
					"0.1.4");
		}

		if (pCession.isRemboursement() == false && pCession.getMontantRemboursement() != null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.125", "PolitiqueCession.remboursement"),
					"1.1.125");
		}

		if (dateAction != null
				&& Utils.compareDate(dateAction, PropertiesUtil.getInstance().getDateDuJour().toDate()) < Constants.ZERO) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.85"), "1.1.85");
		}

		validerSerialNumber(contrat);

	}

	/**
	 * valider via 'netEquipment' s'il n'y a pas un equipement qui a plusieurs numero de serie.
	 * 
	 * @param contrat
	 *            {@link Contrat}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validerSerialNumber(Contrat contrat) throws TopazeException {
		RestClientContratCore restClientContratCore = ApplicationContextHolder.getBean("restClientContratCore");
		restClientContratCore.validerSerialNumber(contrat.getReference(), contrat.getIdClient());
	}

	/**
	 * valider l'annulation de la demande de cession.
	 * 
	 * @param avenant
	 *            {@link Avenant}.
	 * @param avenantHistorique
	 *            {@link Avenant}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@SuppressWarnings("null")
	public static void checkAnnulationCession(Avenant avenant, Avenant avenantHistorique) throws TopazeException {
		com.nordnet.topaze.contrat.domain.PolitiqueCession politiqueCession = null;
		com.nordnet.topaze.contrat.domain.PolitiqueCession politiqueCessionHistorique = null;
		if (avenant != null) {
			politiqueCession = avenant.getPolitiqueCession();
		}
		if (avenantHistorique != null) {
			politiqueCessionHistorique = avenantHistorique.getPolitiqueCession();
		}
		if (politiqueCession == null && politiqueCessionHistorique == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.97"), "1.1.97");
		} else if (politiqueCession == null && politiqueCessionHistorique != null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.98"), "1.1.98");
		} else if (politiqueCession != null && avenant.getDateAnnulation() != null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.99"), "1.1.99");
		}
	}

	/**
	 * verifer si les regles de remboursement en cas de resiliation totale sont bien appliqués
	 * 
	 * @param contrat
	 * @param politiqueResiliation
	 * @throws TopazeException
	 */
	public static void checkRemboursementTotal(Contrat contrat,
			com.nordnet.topaze.contrat.business.PolitiqueResiliation politiqueResiliation) throws TopazeException {
		Set<ElementContractuel> elementContractuels = contrat.getSousContrats();

		Set<ElementContractuel> elementContractuellesParent = new HashSet<>();

		if (elementContractuels != null && elementContractuels.size() != 0) {

			for (ElementContractuel elementContractuel : elementContractuels) {
				if (elementContractuel.getDependDe() != null) {
					elementContractuellesParent.add(elementContractuel);
				}

			}

			if (elementContractuellesParent.size() == 1) {
				ElementContractuel elementContractuel = (new ArrayList<>(elementContractuellesParent)).get(0);
				if (!elementContractuel.isRemboursable() && politiqueResiliation.isRemboursement()
						&& politiqueResiliation.getMontantRemboursement() != null) {
					throw new TopazeException(propertiesUtil.getErrorMessage("1.1.88", elementContractuel
							.getContratParent().getReference() + "-" + elementContractuel.getNumEC()), "1.1.88");
				}
			}
		}

	}

	/**
	 * verifer si les regles de remboursement en cas de resiliation partielle sont bien appliqués
	 * 
	 * @param elementContractuel
	 * @throws TopazeException
	 */
	public static void checkRemboursementPartiel(ElementContractuel elementContractuel,
			com.nordnet.topaze.contrat.business.PolitiqueResiliation politiqueResiliation) throws TopazeException {
		if (!elementContractuel.isRemboursable() && politiqueResiliation.isRemboursement()
				&& politiqueResiliation.getMontantRemboursement() != null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.88", elementContractuel.getContratParent()
					.getReference() + "-" + elementContractuel.getNumEC()), "1.1.88");
		}
		if (!elementContractuel.isRemboursable() && politiqueResiliation.isRemboursement()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.88", elementContractuel.getContratParent()
					.getReference() + "-" + elementContractuel.getNumEC()), "1.1.88");
		}

	}

	/**
	 * verifer que l'user.
	 * 
	 * @param contrat
	 * @param politiqueResiliation
	 * @throws TopazeException
	 */
	public static void checkUser(String user) throws TopazeException {
		if (Utils.isStringNullOrEmpty(user)) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.91"), "1.1.91");
		}
	}

	/**
	 * Valider la migration.
	 * 
	 * @param contrat
	 *            {@link Contrat}.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public static void checkRenouvellement(Contrat contrat) throws TopazeException {

		if (contrat.getDateValidation() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.123"), "1.1.123");
		}

		for (ElementContractuel elementContractuel : contrat.getSousContrats()) {
			if (elementContractuel.getDuree() == null && !contrat.isResilier()) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.121", elementContractuel
						.getContratParent().getReference() + "-" + elementContractuel.getNumEC()), "1.1.121");
			}
		}

	}

	/**
	 * methode pour valider le politique de renouvellement
	 * 
	 * 
	 * @param produitRenouvellement
	 * @throws TopazeException
	 */
	public static void checkPolitiqueRenouvellement(
			com.nordnet.topaze.contrat.business.PolitiqueRenouvellement politiqueRenouvellement) throws TopazeException {
		if (politiqueRenouvellement == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.17", "renouvellement"), "1.1.17");
		} else if (politiqueRenouvellement.getConserverAncienneReduction() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1",
					"PolitiqueRenouvellement.conserverAncienneReduction"), "0.1.1");
		} else if (politiqueRenouvellement.getForce() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "PolitiqueRenouvellement.force"), "0.1.1");
		}
	}

	/**
	 * methode pour valider les produits de renouvellement
	 * 
	 * @param produitMigration
	 * @param elementContractuel
	 * @throws TopazeException
	 */
	public static void checkProduitRenouvellement(ProduitRenouvellement produitRenouvellement,
			ElementContractuel elementContractuel, ElementContractuel elementContractuelResilier, Contrat contrat)
			throws TopazeException {

		if (elementContractuel == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.120"), "1.1.120");
		}
		if (elementContractuelResilier != null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.118", elementContractuel.getContratParent()
					.getReference() + "-" + elementContractuel.getNumEC()), "1.1.118");
		}

		if (elementContractuel.getDuree() == null && !contrat.isResilier()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.121", elementContractuel.getContratParent()
					.getReference() + "-" + elementContractuel.getNumEC()), "1.1.121");
		}

		if (produitRenouvellement != null) {
			if (elementContractuel.getTypeProduit() != produitRenouvellement.getTypeProduit()) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.138"), "1.1.138");
			}
			if (!elementContractuel.getReferenceProduit().equals(produitRenouvellement.getReferenceProduit())) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.139"), "1.1.139");
			}
		}

	}

	/**
	 * valider les élements de renouvellement de type bien ou service
	 * 
	 * @param elementStateServices
	 * @param elementStateBien
	 * @throws TopazeException
	 */
	public static void checkProduitRenouvellemntState(List<ElementStateInfo> elementStateServices,
			List<ElementStateInfo> elementStateBien, PolitiqueRenouvellement politiqueRenouvellement)
			throws TopazeException {

		// valider les element de type service
		for (ElementStateInfo elementStateInfo : elementStateServices) {
			if (elementStateInfo.getState().equals(TState.CANCELED)) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.113",
						elementStateInfo.getRefenceElementContractuelle()), "1.1.113");
			}

		}

		// valider les element de type bien
		for (ElementStateInfo elementStateInfo : elementStateBien) {
			if (elementStateInfo.getRetourne() && !politiqueRenouvellement.getForceRenouvellement()) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.112",
						elementStateInfo.getRefenceElementContractuelle()), "1.1.112");
			}

			if (elementStateInfo.getPreparerPourRetour() && !politiqueRenouvellement.getForceRenouvellement()) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.122",
						elementStateInfo.getRefenceElementContractuelle()), "1.1.122");
			}

		}

	}

	/**
	 * tester s'il y une demande active dans le future
	 * 
	 * @param elementStateServices
	 * @param elementStateBien
	 * @throws TopazeException
	 */
	public static void checkIsActionPossible(String referenceContrat, Avenant avenantMigration, Avenant avenantCession,
			Avenant avenantRenouvellement, Contrat contratResilier, ElementContractuel elementContractuel,
			Integer numEC, List<ElementContractuel> elementContractuels, boolean isMigration, boolean isRenouvellement)
			throws TopazeException {
		if (avenantMigration != null && !isRenouvellement) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.130", "Migration", "contrat",
					referenceContrat), "1.1.130");
		} else if (avenantRenouvellement != null && !isMigration) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.130", "Renouvellement", "contrat",
					referenceContrat), "1.1.116");
		} else if (avenantCession != null) {
			throw new TopazeException(
					propertiesUtil.getErrorMessage("1.1.130", "Cession", "contrat", referenceContrat), "1.1.130");
		} else if (contratResilier != null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.130", "Resiliation", "contrat",
					referenceContrat), "1.1.130");
		} else if (elementContractuel != null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.130", "Resiliation ", "element",
					referenceContrat + "-" + numEC), "1.1.130");
		} else if (elementContractuels != null && elementContractuels.size() != 0) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.130", "Resiliation ", "element du contrat",
					referenceContrat), "1.1.130");
		}
	}

	/**
	 * tester si l'action ne depasse pas la date de fin du contrat.
	 * 
	 * 
	 * @param dateAction
	 * @param dateFinContrat
	 * @throws TopazeException
	 */
	public static void checkActionPossibleInDate(Date dateAction, Date dateFinContrat, String referenceContrtat,
			String action, boolean isRenouvellement) throws TopazeException {
		if (dateFinContrat != null && dateAction.compareTo(dateFinContrat) == 1 && !isRenouvellement) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.132", action, referenceContrtat), "1.1.132");
		}

	}

	/**
	 * Valider la date remboursement.
	 * 
	 * @param politiqueResiliation
	 *            {@link PolitiqueResiliation}
	 * @param contrat
	 *            {@link Contrat}
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public static void checkDateRemboursement(PolitiqueResiliation politiqueResiliation, Contrat contrat)
			throws TopazeException {
		if (!Utils.isStringNullOrEmpty(politiqueResiliation.getDateRemboursement())) {
			if (ContratValidator.verifierDateWithoutTimeValide(politiqueResiliation.getDateRemboursement()).before(
					contrat.getDateValidation())) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.147"), "1.1.147");
			}

			ElementContractuel[] elementContractuels = new ElementContractuel[contrat.getSousContrats().size()];
			Integer periodicite = contrat.getSousContrats().toArray(elementContractuels)[0].getPeriodicite();
			LocalDate dateFinPeriode =
					LocalDate.fromDateFields(elementContractuels[0].getDateDerniereFacture()).plusMonths(periodicite);

			if (ContratValidator.verifierDateWithoutTimeValide(politiqueResiliation.getDateRemboursement()).after(
					dateFinPeriode.toDate())) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.148"), "1.1.148");
			}
		}
	}

	public static void checkAvenantExist(Avenant avenant) throws TopazeException {
		if (avenant == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.2", "avenant"), "0.1.2");
		}

	}
}
