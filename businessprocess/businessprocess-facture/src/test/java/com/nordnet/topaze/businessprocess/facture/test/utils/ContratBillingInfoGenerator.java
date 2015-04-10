package com.nordnet.topaze.businessprocess.facture.test.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;

import com.nordnet.topaze.businessprocess.facture.util.Utils;
import com.nordnet.topaze.client.rest.business.facturation.ContratBillingInfo;
import com.nordnet.topaze.client.rest.business.facturation.Frais;
import com.nordnet.topaze.client.rest.business.facturation.PolitiqueResiliation;
import com.nordnet.topaze.client.rest.enums.ModeFacturation;
import com.nordnet.topaze.client.rest.enums.ModePaiement;
import com.nordnet.topaze.client.rest.enums.TypeContrat;
import com.nordnet.topaze.client.rest.enums.TypeFrais;
import com.nordnet.topaze.client.rest.enums.TypeProduit;
import com.nordnet.topaze.client.rest.enums.TypeTVA;

/**
 * @author anisselmane.
 * 
 */
public class ContratBillingInfoGenerator {

	/**
	 * Generation d'un {@link ContratBillingInfo}.
	 * 
	 * @param referenceContrat
	 *            reference du contrat global.
	 * @param contratBillingInfoParent
	 *            {@link ContratBillingInfo}.
	 * @return {@link ContratBillingInfo}
	 */
	public static ContratBillingInfo getContratBillingInformation(String referenceContrat,
			ContratBillingInfo contratBillingInfoParent) {

		Calendar dateDebutFacturation = Calendar.getInstance();
		ContratBillingInfo contratBillingInfo = null;
		if (referenceContrat.equals("REF_C_A_PM_M")) {
			contratBillingInfo =
					creerContratBillingInfo("REF_C_A_PM_M", TypeContrat.ABONNEMENT, ModeFacturation.PREMIER_MOIS, 1,
							dateDebutFacturation.getTime());
		} else if (referenceContrat.equals("REF_C_A_PM_T")) {
			dateDebutFacturation.add(Calendar.MONTH, -3);
			contratBillingInfo =
					creerContratBillingInfo("REF_C_A_PM_T", TypeContrat.ABONNEMENT, ModeFacturation.PREMIER_MOIS, 3,
							dateDebutFacturation.getTime());
		} else if (referenceContrat.equals("REF_C_A_PM_A")) {
			dateDebutFacturation.add(Calendar.YEAR, -1);
			contratBillingInfo =
					creerContratBillingInfo("REF_C_A_PM_A", TypeContrat.ABONNEMENT, ModeFacturation.PREMIER_MOIS, 12,
							dateDebutFacturation.getTime());
		} else if (referenceContrat.equals("REF_C_A_DA_M")) {
			dateDebutFacturation.setTime(new Date());
			contratBillingInfo =
					creerContratBillingInfo("REF_C_A_DA_M", TypeContrat.ABONNEMENT, ModeFacturation.DATE_ANNIVERSAIRE,
							1, dateDebutFacturation.getTime());
		} else if (referenceContrat.equals("REF_C_A_DA_T")) {
			dateDebutFacturation.add(Calendar.MONTH, -3);
			contratBillingInfo =
					creerContratBillingInfo("REF_C_A_DA_T", TypeContrat.ABONNEMENT, ModeFacturation.DATE_ANNIVERSAIRE,
							3, dateDebutFacturation.getTime());
		} else if (referenceContrat.equals("REF_C_A_DA_A")) {
			dateDebutFacturation.add(Calendar.YEAR, -1);
			contratBillingInfo =
					creerContratBillingInfo("REF_C_A_DA_A", TypeContrat.ABONNEMENT, ModeFacturation.DATE_ANNIVERSAIRE,
							12, dateDebutFacturation.getTime());
		}

		// préparation contrat 1 Abonnement
		else if (referenceContrat.equals("Ref_Contrat_1")) {
			dateDebutFacturation.add(Calendar.MONTH, -1);
			contratBillingInfo =
					creerContratBillingInfo("REF_C_A", TypeContrat.ABONNEMENT, ModeFacturation.DATE_ANNIVERSAIRE, 12,
							dateDebutFacturation.getTime());
			contratBillingInfo.setDateDerniereFacture(null);
		}

		// préparation contrat 2 Vente
		else if (referenceContrat.equals("Ref_Contrat_2")) {
			dateDebutFacturation.add(Calendar.MONTH, -1);
			contratBillingInfo =
					creerContratBillingInfo("REF_C_V", TypeContrat.VENTE, ModeFacturation.PREMIER_MOIS, 12,
							dateDebutFacturation.getTime());

			contratBillingInfo.setDateDerniereFacture(null);
		}

		else if (referenceContrat.equals("Ref_Contrat_3")) {
			dateDebutFacturation.add(Calendar.MONTH, -1);
			contratBillingInfo =
					creerContratVenteBienBillingInfo("REF_C_V", TypeContrat.VENTE, ModeFacturation.PREMIER_MOIS, 12,
							dateDebutFacturation.getTime());

			contratBillingInfo.setDateDerniereFacture(null);

		}

		else if (referenceContrat.equals("Ref_Contrat_4")) {
			dateDebutFacturation.add(Calendar.MONTH, -1);
			contratBillingInfo =
					creerContratVenteServiceBillingInfo("REF_C_V", TypeContrat.VENTE, ModeFacturation.PREMIER_MOIS, 12,
							dateDebutFacturation.getTime());

			contratBillingInfo.setDateDerniereFacture(null);

		} else {
			return null;
		}

		if (contratBillingInfoParent != null) {
			contratBillingInfo.setContratBillingInfoParent(contratBillingInfoParent);
		}
		contratBillingInfo.setMigre(false);
		return contratBillingInfo;

	}

	/**
	 * Creer contrat billing info.
	 * 
	 * @param referenceContrat
	 *            the reference contrat
	 * @param typeContrat
	 *            the type contrat
	 * @param modeFacturation
	 *            the mode facturation
	 * @param periodicite
	 *            the periodicite
	 * @param dateDebutFacturation
	 *            the date debut facturation
	 * @return the contrat billing info
	 */
	private static ContratBillingInfo creerContratBillingInfo(String referenceContrat, TypeContrat typeContrat,
			ModeFacturation modeFacturation, Integer periodicite, Date dateDebutFacturation) {
		ContratBillingInfo contratBillingInfo = new ContratBillingInfo();
		contratBillingInfo.setReferenceContrat("REF_GLOBAL");
		if (!Utils.isStringNullOrEmpty(referenceContrat)) {
			contratBillingInfo.setReferenceContrat(referenceContrat);
			contratBillingInfo.setIdClient("ID_CLIENT_TST");
			contratBillingInfo.setReferenceProduit("avfw.essentiel");
			contratBillingInfo.setTitre("Contrat pour le produit : Max");
			contratBillingInfo.setTypeContrat(typeContrat);
			contratBillingInfo.setMontant(1000d);
			contratBillingInfo.setEngagement(24);
			contratBillingInfo.setDureeContrat(48);
			contratBillingInfo.setPeriodicite(periodicite);
			contratBillingInfo.setModePaiement(ModePaiement.CB);
			contratBillingInfo.setDateDebutFacturation(dateDebutFacturation);
			contratBillingInfo.setTypeProduit(TypeProduit.BIEN);

			contratBillingInfo.setModeFacturation(modeFacturation);
			List<Frais> frais = new ArrayList<>();
			Frais fraisProduitResil = new Frais();
			fraisProduitResil.setMontant(50d);
			fraisProduitResil.setTypeFrais(TypeFrais.RESILIATION);
			fraisProduitResil.setOrdre(1);
			fraisProduitResil.setNombreMois(2);
			fraisProduitResil.setTitre("Frais Cloture");
			frais.add(fraisProduitResil);
			fraisProduitResil = new Frais();
			fraisProduitResil.setMontant(30d);
			fraisProduitResil.setTypeFrais(TypeFrais.RESILIATION);
			fraisProduitResil.setOrdre(2);
			fraisProduitResil.setNombreMois(5);
			frais.add(fraisProduitResil);
			fraisProduitResil = new Frais();
			fraisProduitResil.setMontant(10d);
			fraisProduitResil.setTypeFrais(TypeFrais.RESILIATION);
			fraisProduitResil.setOrdre(3);
			frais.add(fraisProduitResil);
			Frais fraisProduit = new Frais();
			fraisProduit.setMontant(100d);
			fraisProduit.setTypeFrais(TypeFrais.CREATION);
			fraisProduit.setTitre("Frais de dossier");
			frais.add(fraisProduit);
			contratBillingInfo.setFrais(frais);
			contratBillingInfo.setTypeTVA(TypeTVA.P);
			contratBillingInfo.setReferenceModePaiement("R1234567");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.WEEK_OF_YEAR, -1);

			// initialiser date debut facturation premier de mois
			// Calendar cal = Calendar.getInstance();
			// cal.set(Calendar.YEAR, 2014);
			// cal.set(Calendar.MONTH, 11);
			// cal.set(Calendar.DAY_OF_MONTH, 1);
			//
			// Date date = cal.getTime();
			//
			// contratBillingInfo.setDateDebutFacturation(date);
			contratBillingInfo.setDateDerniereFacture(null);

		}
		return contratBillingInfo;
	}

	/**
	 * Cree {@link ContratBillingInfo} pour contrat de vente des biens.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param typeContrat
	 *            {@link TypeContrat}.
	 * @param modeFacturation
	 *            {@link ModeFacturation}.
	 * @param periodicite
	 *            periodicitie.
	 * @param dateDebutFacturation
	 *            date debut de facturation.
	 * @return {@link ContratBillingInfo}.
	 */
	private static ContratBillingInfo creerContratVenteBienBillingInfo(String referenceContrat,
			TypeContrat typeContrat, ModeFacturation modeFacturation, Integer periodicite, Date dateDebutFacturation) {
		ContratBillingInfo contratBillingInfo = new ContratBillingInfo();

		if (!Utils.isStringNullOrEmpty(referenceContrat)) {
			contratBillingInfo.setReferenceContrat(referenceContrat);
			contratBillingInfo.setIdClient("ID_CLIENT_TST");
			contratBillingInfo.setReferenceProduit(referenceContrat + "-01");
			contratBillingInfo.setTitre("Contrat pour le produit : Max");
			contratBillingInfo.setTypeContrat(typeContrat);
			contratBillingInfo.setMontant(1000d);
			contratBillingInfo.setEngagement(24);
			contratBillingInfo.setDureeContrat(48);
			contratBillingInfo.setPeriodicite(periodicite);
			contratBillingInfo.setModePaiement(ModePaiement.CB);
			contratBillingInfo.setDateDebutFacturation(dateDebutFacturation);
			contratBillingInfo.setTypeProduit(TypeProduit.BIEN);

			contratBillingInfo.setModeFacturation(modeFacturation);
			List<Frais> frais = new ArrayList<>();
			Frais fraisProduitResil = new Frais();
			fraisProduitResil.setMontant(50d);
			fraisProduitResil.setTypeFrais(TypeFrais.RESILIATION);
			frais.add(fraisProduitResil);
			Frais fraisProduit = new Frais();
			fraisProduit.setMontant(100d);
			fraisProduit.setTypeFrais(TypeFrais.CREATION);
			frais.add(fraisProduit);
			contratBillingInfo.setFrais(frais);
			contratBillingInfo.setTypeTVA(TypeTVA.P);
			contratBillingInfo.setReferenceModePaiement("R1234567");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.WEEK_OF_YEAR, -1);

			// initialiser date debut facturation premier de mois
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2014);
			cal.set(Calendar.MONTH, 11);
			cal.set(Calendar.DAY_OF_MONTH, 1);

			Date date = cal.getTime();

			contratBillingInfo.setDateDebutFacturation(date);
			contratBillingInfo.setDateDerniereFacture(null);

		}
		return contratBillingInfo;
	}

	/**
	 * Cree {@link ContratBillingInfo} pour contrat de vente des services.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param typeContrat
	 *            {@link TypeContrat}.
	 * @param modeFacturation
	 *            {@link ModeFacturation}.
	 * @param periodicite
	 *            periodicite.
	 * @param dateDebutFacturation
	 *            date debut de facturation.
	 * @return {@link ContratBillingInfo}.
	 */
	private static ContratBillingInfo creerContratVenteServiceBillingInfo(String referenceContrat,
			TypeContrat typeContrat, ModeFacturation modeFacturation, Integer periodicite, Date dateDebutFacturation) {
		ContratBillingInfo contratBillingInfo = new ContratBillingInfo();

		if (!Utils.isStringNullOrEmpty(referenceContrat)) {
			contratBillingInfo.setReferenceContrat(referenceContrat);
			contratBillingInfo.setIdClient("ID_CLIENT_TST");
			contratBillingInfo.setReferenceProduit(referenceContrat + "-01");
			contratBillingInfo.setTitre("Contrat pour le produit : Max");
			contratBillingInfo.setTypeContrat(typeContrat);
			contratBillingInfo.setMontant(1000d);
			contratBillingInfo.setEngagement(24);
			contratBillingInfo.setDureeContrat(48);
			contratBillingInfo.setPeriodicite(periodicite);
			contratBillingInfo.setModePaiement(ModePaiement.CB);
			contratBillingInfo.setDateDebutFacturation(dateDebutFacturation);
			contratBillingInfo.setTypeProduit(TypeProduit.SERVICE);
			contratBillingInfo.setDuree(1);

			contratBillingInfo.setModeFacturation(modeFacturation);
			List<Frais> frais = new ArrayList<>();
			Frais fraisProduitResil = new Frais();
			fraisProduitResil.setMontant(50d);
			fraisProduitResil.setTypeFrais(TypeFrais.RESILIATION);
			frais.add(fraisProduitResil);
			Frais fraisProduit = new Frais();
			fraisProduit.setMontant(100d);
			fraisProduit.setTypeFrais(TypeFrais.CREATION);
			frais.add(fraisProduit);
			contratBillingInfo.setFrais(frais);
			contratBillingInfo.setTypeTVA(TypeTVA.P);
			contratBillingInfo.setReferenceModePaiement("R1234567");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.WEEK_OF_YEAR, -1);

			// initialiser date debut facturation premier de mois
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2014);
			cal.set(Calendar.MONTH, 11);
			cal.set(Calendar.DAY_OF_MONTH, 1);

			Date date = cal.getTime();

			contratBillingInfo.setDateDebutFacturation(date);
			contratBillingInfo.setDateDerniereFacture(null);

		}
		return contratBillingInfo;
	}

	/**
	 * Creer un contrat billing information pour les tests de penalite.
	 * 
	 * @return {@link ContratBillingInfo}
	 */
	public static ContratBillingInfo getContratBillingInfoPourPenalite() {
		ContratBillingInfo contratBillingInfo = new ContratBillingInfo();
		contratBillingInfo.setReferenceContrat("7038e3e0");
		contratBillingInfo.setReferenceContrat("7038e3e001");
		contratBillingInfo.setIdClient("idClient");
		contratBillingInfo.setDateDebutFacturation(LocalDate.parse("2014-06-12").toDate());
		contratBillingInfo.setDateDerniereFacture(LocalDate.parse("2014-08-12").toDate());
		contratBillingInfo.setDateFinContrat(LocalDate.parse("2014-09-15").toDate());
		contratBillingInfo.setDureeContrat(24);
		contratBillingInfo.setEngagement(12);
		contratBillingInfo.setDateFinEngagement(LocalDate.parse("2015-06-12").toDate());
		contratBillingInfo.setModeFacturation(ModeFacturation.DATE_ANNIVERSAIRE);
		contratBillingInfo.setModePaiement(ModePaiement.CB);
		contratBillingInfo.setMontant(40d);
		contratBillingInfo.setPeriodicite(2);
		contratBillingInfo.setReferenceModePaiement("R1234567");
		contratBillingInfo.setReferenceProduit("jet.5Giga");
		contratBillingInfo.setTitre("Contrat ABONNEMENT pour le produit : Max.ADSL.5Giga");
		contratBillingInfo.setTypeContrat(TypeContrat.ABONNEMENT);
		contratBillingInfo.setTypeTVA(TypeTVA.SR);

		return contratBillingInfo;
	}

	/**
	 * Creer un contrat billing information pour les tests de penalite.
	 * 
	 * @param cas
	 *            cas.
	 * @return {@link ContratBillingInfo}
	 */
	public static ContratBillingInfo getContratBillingInfoPourRemboursement(int cas) {

		ContratBillingInfo contratBillingInfo = new ContratBillingInfo();
		PolitiqueResiliation politiqueResiliation = new PolitiqueResiliation();
		switch (cas) {
		case 1:
			contratBillingInfo.setReferenceContrat("7038e3e001");
			contratBillingInfo.setIdClient("idClient");
			contratBillingInfo.setDateDebutFacturation(LocalDate.parse("2014-06-12").toDate());
			contratBillingInfo.setDateDerniereFacture(LocalDate.parse("2014-09-01").toDate());
			contratBillingInfo.setDateFinContrat(LocalDate.parse("2014-09-15").toDate());
			contratBillingInfo.setDureeContrat(24);
			contratBillingInfo.setEngagement(12);
			contratBillingInfo.setDateFinEngagement(LocalDate.parse("2015-06-12").toDate());
			contratBillingInfo.setModeFacturation(ModeFacturation.PREMIER_MOIS);
			contratBillingInfo.setModePaiement(ModePaiement.CB);
			contratBillingInfo.setMontant(40d);
			contratBillingInfo.setPeriodicite(2);
			contratBillingInfo.setReferenceModePaiement("R1234567");
			contratBillingInfo.setReferenceProduit("jet.5Giga");
			contratBillingInfo.setTitre("Contrat ABONNEMENT pour le produit : Max.ADSL.5Giga");
			contratBillingInfo.setTypeContrat(TypeContrat.ABONNEMENT);
			contratBillingInfo.setTypeTVA(TypeTVA.SR);
			politiqueResiliation.setPenalite(true);
			contratBillingInfo.setPolitiqueResiliation(politiqueResiliation);
			break;
		case 2:
			contratBillingInfo.setReferenceContrat("7038e3e001");
			contratBillingInfo.setIdClient("idClient");
			contratBillingInfo.setDateDebutFacturation(LocalDate.parse("2014-06-12").toDate());
			contratBillingInfo.setDateDerniereFacture(LocalDate.parse("2014-09-01").toDate());
			contratBillingInfo.setDateFinContrat(LocalDate.parse("2014-09-15").toDate());
			contratBillingInfo.setDureeContrat(24);
			contratBillingInfo.setEngagement(12);
			contratBillingInfo.setModeFacturation(ModeFacturation.PREMIER_MOIS);
			contratBillingInfo.setModePaiement(ModePaiement.CB);
			contratBillingInfo.setMontant(40d);
			contratBillingInfo.setPeriodicite(2);
			contratBillingInfo.setReferenceModePaiement("R1234567");
			contratBillingInfo.setReferenceProduit("jet.5Giga");
			contratBillingInfo.setTitre("Contrat ABONNEMENT pour le produit : Max.ADSL.5Giga");
			contratBillingInfo.setTypeContrat(TypeContrat.ABONNEMENT);
			contratBillingInfo.setTypeTVA(TypeTVA.SR);
			politiqueResiliation.setPenalite(true);
			contratBillingInfo.setPolitiqueResiliation(politiqueResiliation);
			break;
		case 3:
			contratBillingInfo.setReferenceContrat("7038e3e001");
			contratBillingInfo.setIdClient("idClient");
			contratBillingInfo.setDateDebutFacturation(LocalDate.parse("2014-06-12").toDate());
			contratBillingInfo.setDateDerniereFacture(LocalDate.parse("2014-09-01").toDate());
			contratBillingInfo.setDateFinContrat(LocalDate.parse("2014-09-15").toDate());
			contratBillingInfo.setDureeContrat(24);
			contratBillingInfo.setEngagement(12);
			contratBillingInfo.setModeFacturation(ModeFacturation.PREMIER_MOIS);
			contratBillingInfo.setModePaiement(ModePaiement.CB);
			contratBillingInfo.setMontant(40d);
			contratBillingInfo.setPeriodicite(2);
			contratBillingInfo.setReferenceModePaiement("R1234567");
			contratBillingInfo.setReferenceProduit("jet.5Giga");
			contratBillingInfo.setTitre("Contrat ABONNEMENT pour le produit : Max.ADSL.5Giga");
			contratBillingInfo.setTypeContrat(TypeContrat.ABONNEMENT);
			contratBillingInfo.setTypeTVA(TypeTVA.SR);
			politiqueResiliation.setPenalite(true);
			contratBillingInfo.setPolitiqueResiliation(politiqueResiliation);
			break;
		default:
			break;
		}
		return contratBillingInfo;
	}

	// private static ContratBillingInfo contratBillingInfoPremierDeMois(String referenceContrat, TypeContrat
	// typeContrat,
	// ModeFacturation modeFacturation, Integer periodicite, Date dateDebutFacturation) {
	//
	// ContratBillingInfo contratBillingInfoPremierDeMois = new ContratBillingInfo();
	// if (!Utils.isStringNullOrEmpty(referenceContrat)) {
	//
	// contratBillingInfoPremierDeMois.setReferenceContrat(referenceContrat);
	// contratBillingInfoPremierDeMois.setTypeContrat(typeContrat);
	// contratBillingInfoPremierDeMois.setModeFacturation(ModeFacturation.PREMIER_MOIS);
	// contratBillingInfoPremierDeMois.setPeriodicite(6);
	// contratBillingInfoPremierDeMois.setDateDebutFacturation(dateDebutFacturation);
	//
	// contratBillingInfoPremierDeMois.setTitre("Contrat pour le Produit Satellite");
	// contratBillingInfoPremierDeMois.setIdClient("30");
	// contratBillingInfoPremierDeMois.setEngagement(3);
	// contratBillingInfoPremierDeMois.setDureeContrat(12);
	//
	// }
	//
	// return null;
	//
	// }

}
