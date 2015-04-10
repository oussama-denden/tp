package com.nordnet.topaze.contrat.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import com.nordnet.topaze.contrat.business.ContratMigrationInfo;
import com.nordnet.topaze.contrat.business.ContratRenouvellementInfo;
import com.nordnet.topaze.contrat.business.ContratValidationInfo;
import com.nordnet.topaze.contrat.business.PaiementInfo;
import com.nordnet.topaze.contrat.business.PolitiqueResiliation;
import com.nordnet.topaze.contrat.business.Produit;
import com.nordnet.topaze.contrat.business.ProduitMigration;
import com.nordnet.topaze.contrat.business.ProduitRenouvellement;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.domain.ModePaiement;
import com.nordnet.topaze.contrat.domain.PolitiqueValidation;
import com.nordnet.topaze.contrat.domain.TypeResiliation;
import com.nordnet.topaze.contrat.validator.ContratValidator;
import com.nordnet.topaze.exception.TopazeException;

public class ContratUtils {

	/**
	 * Verifier si un element contractuelle n'a pas du mode paiement autre.
	 * 
	 * @param elementContractuel
	 *            element contractuelle
	 * @return true si element possede un mode paiement autre.
	 */
	public static boolean verifierModePaiementAutre(ElementContractuel elementContractuel) {
		if (elementContractuel.getModePaiement().equals(ModePaiement.AUTRE)) {
			return true;
		}
		return false;

	}

	/**
	 * Créer l'arborescence de prouits.
	 * 
	 * @param produits
	 *            the produits
	 * @param sousContrats
	 *            the sous contrats
	 * @throws TopazeException
	 *             the description contrat non valide exception
	 * @throws TopazeException
	 *             the contrat exception {@link Produit}. {@link ElementContractuel}. {@link TopazeException}.
	 *             {@link TopazeException}.
	 */
	public static void creerArborescence(List<Produit> produits, Set<ElementContractuel> sousContrats)
			throws TopazeException {
		for (Produit produit : produits) {

			if (produit.getNumECParent() != null) {

				for (ElementContractuel elementContractuel : sousContrats) {

					if (elementContractuel.getNumEC() == produit.getNumEC()) {

						for (ElementContractuel elementContractuelParent : sousContrats) {

							if (elementContractuelParent.getNumEC() == produit.getNumECParent()) {

								elementContractuel.setElementContractuelParent(elementContractuelParent);

							}

						}
					}
				}
			}

		}

		// verifier arboriscence si on n'a pas une structure en boucle.
		for (ElementContractuel elementContractuel : sousContrats) {
			if (elementContractuel.hasParent()) {

				ContratValidator.verifierArborescence(elementContractuel);

			}
		}

	}

	/**
	 * Checks if is resiliation globale.
	 * 
	 * @param elementContractuel
	 *            the element contractuel
	 * @return true, if is resiliation globale
	 */
	public static boolean isResiliationGlobale(ElementContractuel elementContractuel) {
		return (elementContractuel.getElementContractuelParent() != null && !(elementContractuel.isResilier() && !elementContractuel
				.getElementContractuelParent().isResilier()));

	}

	/**
	 * parcour tous les produits d'un contrat renouvelé et tracer tous les durees
	 * 
	 * @param conratRenouvele
	 * @return
	 */
	public static String traceRenouvellementProduit(Contrat conratRenouvele) {
		StringBuilder stringBuilder = new StringBuilder();
		for (ElementContractuel elementContractuel : conratRenouvele.getSousContrats()) {
			stringBuilder.append("-" + elementContractuel.getContratParent().getReference() + "-"
					+ elementContractuel.getNumEC() + " pour une duree de " + elementContractuel.getDuree() + " mois");
		}
		return stringBuilder.toString();
	}

	/**
	 * Creates the contrat validation info à partir contrat migration info.
	 * 
	 * @param contratGlobal
	 *            the contrat global
	 * @param contratMigrationInfo
	 *            the contrat migration info
	 * @return the contrat validation info
	 */
	public static ContratValidationInfo createContratValidationInfoFromContratMigrationInfo(Contrat contratGlobal,
			ContratMigrationInfo contratMigrationInfo) {

		ContratValidationInfo contratValidationInfo = new ContratValidationInfo();
		contratValidationInfo.setUser(contratMigrationInfo.getUser());
		contratValidationInfo.setIdClient(contratGlobal.getIdClient());
		contratValidationInfo.setSegmentTVA(contratGlobal.getSegmentTVA());
		ElementContractuel elementContractuel = (ElementContractuel) contratGlobal.getSousContrats().toArray()[0];
		contratValidationInfo.setIdAdrFacturation(elementContractuel.getIdAdrFacturation());

		// creer la politique de validation
		PolitiqueValidation politiqueValidation = new PolitiqueValidation();
		politiqueValidation.setFraisCreation(contratMigrationInfo.getPolitiqueMigration().isFraisCreation());
		politiqueValidation.setCheckIsPackagerCreationPossible(false);
		contratValidationInfo.setPolitiqueValidation(politiqueValidation);

		// creer les paiements informations.
		List<PaiementInfo> paiementInfos = new ArrayList<>();
		for (ProduitMigration produitMigration : contratMigrationInfo.getProduitsMigration()) {
			PaiementInfo paiementInfo = new PaiementInfo();
			paiementInfo.setNumEC(produitMigration.getNumEC());

			if (produitMigration.getPrix() != null) {
				paiementInfo.setReferenceModePaiement(produitMigration.getReferenceDePaiement());
				paiementInfo.setIdAdrLivraison(elementContractuel.getIdAdrLivraison());
			} else {
				for (ElementContractuel ECNonMigrer : contratGlobal.getSousContrats()) {
					if (ECNonMigrer.getNumEC() == produitMigration.getNumEC()) {

						paiementInfo.setReferenceModePaiement(ECNonMigrer.getReferenceModePaiement());
						paiementInfo.setIdAdrLivraison(elementContractuel.getIdAdrLivraison());
					}
				}
			}

			paiementInfo.setReferenceProduit(produitMigration.getReferenceProduitDestination());

			paiementInfos.add(paiementInfo);

		}

		contratValidationInfo.setPaiementInfos(paiementInfos);

		return contratValidationInfo;
	}

	/**
	 * Mapping produit migration à un produit.
	 * 
	 * @param produitMigrations
	 *            the produit migrations
	 * @return the list
	 */
	public static List<Produit> mappingToProduit(List<ProduitMigration> produitMigrations) {
		List<Produit> produits = new ArrayList<>();
		for (ProduitMigration produitMigration : produitMigrations) {
			produits.add(produitMigration.toProduit());
		}
		return produits;
	}

	/**
	 * Mapping produit de renouvellement à un produit
	 * 
	 * @param produitRenouvellements
	 *            liste de produits renouvellement.
	 * @return liste des produits.
	 */
	public static List<Produit> mappingRenouvelleToProduit(List<ProduitRenouvellement> produitRenouvellements) {
		List<Produit> produits = new ArrayList<>();
		for (ProduitRenouvellement pRenouvellement : produitRenouvellements) {
			produits.add(pRenouvellement.toProduit());
		}
		return produits;
	}

	/**
	 * Creates the contrat validation info à partir contrat migration info.
	 * 
	 * @param contratGlobal
	 *            the contrat global
	 * @param contratMigrationInfo
	 *            the contrat migration info
	 * @return the contrat validation info
	 */
	public static ContratValidationInfo createContratValidationInfoFromContratRenouvellementInfo(Contrat contratGlobal,
			ContratRenouvellementInfo contratRenouvellementInfo) {

		ContratValidationInfo contratValidationInfo = new ContratValidationInfo();
		contratValidationInfo.setUser(contratRenouvellementInfo.getUser());
		contratValidationInfo.setIdClient(contratGlobal.getIdClient());
		contratValidationInfo.setSegmentTVA(contratGlobal.getSegmentTVA());
		ElementContractuel elementContractuel = (ElementContractuel) contratGlobal.getSousContrats().toArray()[0];
		contratValidationInfo.setIdAdrFacturation(elementContractuel.getIdAdrFacturation());

		// creer la politique de validation
		PolitiqueValidation politiqueValidation = new PolitiqueValidation();
		politiqueValidation.setCheckIsPackagerCreationPossible(false);
		politiqueValidation.setFraisCreation(false);
		contratValidationInfo.setPolitiqueValidation(politiqueValidation);
		contratValidationInfo.setMigration(true);

		// creer les paiements informations.
		List<PaiementInfo> paiementInfos = new ArrayList<>();
		for (ProduitRenouvellement produitRenouvellement : contratRenouvellementInfo.getProduitRenouvellements()) {
			PaiementInfo paiementInfo = new PaiementInfo();
			paiementInfo.setNumEC(produitRenouvellement.getNumEC());

			if (produitRenouvellement.getPrix() != null) {
				paiementInfo.setReferenceModePaiement(produitRenouvellement.getPrix().getReferenceModePaiement());
				paiementInfo.setIdAdrLivraison(elementContractuel.getIdAdrLivraison());
			}

			paiementInfo.setReferenceProduit(produitRenouvellement.getReferenceProduit());

			paiementInfos.add(paiementInfo);

		}

		contratValidationInfo.setPaiementInfos(paiementInfos);

		return contratValidationInfo;
	}

	/**
	 * Gets politique de resiliation à partir de politique de renouvellement.
	 * 
	 * @param politiqueMigration
	 *            the politique migration
	 * @return the pR from pm
	 */
	public static PolitiqueResiliation getPRFromPR() {
		PolitiqueResiliation politiqueResiliation = new PolitiqueResiliation();
		politiqueResiliation.setFraisResiliation(false);
		politiqueResiliation.setPenalite(false);
		politiqueResiliation.setRemboursement(false);
		politiqueResiliation.setTypeResiliation(TypeResiliation.RIC);

		return politiqueResiliation;
	}

	/**
	 * recuperer la date de fin du contrat qui est la date debut facturation augmente par le max de duree
	 */
	public static Date getDateFinContrat(Contrat contrat) {
		if (contrat != null && contrat.getDateDebutFacturation() != null && contrat.getMaxDuree() != null) {
			LocalDate localDate = LocalDate.fromDateFields(contrat.getDateDebutFacturation());
			LocalDate dateFinContrat = localDate.plusMonths(contrat.getMaxDuree());
			return dateFinContrat.toDate();
		}
		return null;
	}

}