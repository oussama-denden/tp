package com.nordnet.topaze.migration.outil.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.migration.outil.business.ContratMigrationInfo;
import com.nordnet.topaze.migration.outil.business.ContratMigrationSimulationInfo;
import com.nordnet.topaze.migration.outil.business.ECMigrationSimulationInfo;
import com.nordnet.topaze.migration.outil.business.PolitiqueMigration;
import com.nordnet.topaze.migration.outil.business.ProduitMigration;
import com.nordnet.topaze.migration.outil.business.ResultatSimulation;
import com.nordnet.topaze.migration.outil.enums.TypeProduit;
import com.nordnet.topaze.migration.outil.facturation.SimulateurBillingMigration;
import com.nordnet.topaze.migration.outil.rest.RestClient;
import com.nordnet.topaze.migration.outil.validator.MigrationOutilValidator;

/**
 * L'implementation de service {@link SimulateurMigrationService}.
 * 
 * @author Oussama Denden
 * 
 */
@Service("simulateurMigrationService")
public class SimulateurMigrationServiceImpl implements SimulateurMigrationService {

	/**
	 * {@link RestClient}.
	 */
	@Autowired
	private RestClient restClient;

	/**
	 * {@link SimulateurBillingMigration}.
	 */
	@Autowired
	private SimulateurBillingMigration simulateurBillingMigration;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResultatSimulation simulerMigration(String referenceContrat, ContratMigrationInfo contratMigrationInfo)
			throws TopazeException {

		/*
		 * Cherche les informations utiles pour la simulation de migration, a partir de la brique contrat.
		 */
		ContratMigrationSimulationInfo contratInfo = restClient.getContratInfoPourSimulationMigration(referenceContrat);

		return getSimulationMigrationInfo(contratInfo, contratMigrationInfo);

	}

	/**
	 * Simuler le calcule frais, le remboursement et le penalite ainsi que identifier les biens a retourner, apres
	 * migration.
	 * 
	 * @param contratInfo
	 *            {@link ContratMigrationSimulationInfo}.
	 * @param contratMigrationInfo
	 *            {@link ContratMigrationInfo}.
	 * @return {@link ContratMigrationSimulationInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	private ResultatSimulation getSimulationMigrationInfo(ContratMigrationSimulationInfo contratInfo,
			ContratMigrationInfo contratMigrationInfo) throws TopazeException {

		PolitiqueMigration politiqueMigration = contratMigrationInfo.getPolitiqueMigration();

		MigrationOutilValidator.checkPolitiqueMigration(politiqueMigration);

		for (ProduitMigration produitMigration : contratMigrationInfo.getProduitsMigration()) {
			MigrationOutilValidator.checkProduitMigration(produitMigration);
		}

		/*
		 * liste des elements ajoute.
		 */
		List<ECMigrationSimulationInfo> elementsAjoute = new ArrayList<>();

		/*
		 * liste des element resilie.
		 */
		List<ECMigrationSimulationInfo> elementsResilie = new ArrayList<>();

		/*
		 * liste des elements migre.
		 */
		List<ECMigrationSimulationInfo> elementsMigre = new ArrayList<>();

		/*
		 * liste des elements migre.
		 */
		List<ECMigrationSimulationInfo> elementsMigrationAdministrative = new ArrayList<>();

		/*
		 * liste des biens et des services a retourner.
		 */
		List<ECMigrationSimulationInfo> elementsRetour = new ArrayList<>();

		/*
		 * information des migration des elements contractuel.
		 */
		List<ProduitMigration> infoMigrations = contratMigrationInfo.getProduitsMigration();

		/*
		 * information des elements contractuel present.
		 */
		Set<ECMigrationSimulationInfo> infoContratActuel = contratInfo.getPaymentInfos();

		/*
		 * On crée le resultat de migration.
		 */
		ResultatSimulation resultatSimulation = new ResultatSimulation();
		resultatSimulation.setReferenceContrat(contratInfo.getReferenceContrat());

		resultatSimulation =
				simulateurBillingMigration.calculeBillingContratActuel(resultatSimulation, infoContratActuel,
						politiqueMigration);

		/*
		 * On cherche les elements de livraison. Ces sont les elements qui existe dans l'avenant et n'existe pas dans le
		 * bon de preparation de l'ancien contrat.
		 */
		for (ProduitMigration produitMigration : infoMigrations) {
			boolean estPresent = false;
			ChercherECLivraison: for (ECMigrationSimulationInfo elementContractuelInfo : infoContratActuel) {
				if (produitMigration.getNumEC() == elementContractuelInfo.getNumEC()) {
					estPresent = true;
					break ChercherECLivraison;
				}
			}

			/*
			 * S’il y a un element de livraison, alors copier le comportement de la livraison.
			 */
			if (!estPresent) {
				elementsAjoute.add(produitMigration.getElementContractuelInfo());
				resultatSimulation.getLivraisonInfos().add(
						produitMigration.getElementContractuelInfo().getLivraisonInfo());
			}
		}

		/*
		 * On cherche les elements de migration. Ces sont les elements qui existent dans le nouveau contrat et l'ancien
		 * contrat.
		 */
		for (ProduitMigration produitMigration : infoMigrations) {
			for (ECMigrationSimulationInfo elementContractuelInfo : infoContratActuel) {

				/*
				 * Lorsque le reference change donc c'est une migration.
				 */
				if (produitMigration.getNumEC() == elementContractuelInfo.getNumEC()
						&& !produitMigration.getReferenceProduitDestination().equals(
								elementContractuelInfo.getReferenceProduit())) {
					/*
					 * si ça concerne des biens. On effectue l’échange de matériel (SWAP).
					 */
					if (produitMigration.getTypeProduit().equals(TypeProduit.BIEN)) {
						ECMigrationSimulationInfo elementContractuelInfoMigration =
								produitMigration.getElementContractuelInfo(elementContractuelInfo);
						elementsMigre.add(elementContractuelInfoMigration);

						resultatSimulation.getEchangeInfos()
								.add(elementContractuelInfo.getEchangeInfo(produitMigration
										.getReferenceProduitDestination()));
					}

					/*
					 * Si ça concerne des services.
					 */
					else if (produitMigration.getTypeProduit().equals(TypeProduit.SERVICE)) {

						/*
						 * Si c'est une migration dans la même gamme.
						 */
						if (produitMigration.getReferenceGammeDestination().equals(
								produitMigration.getReferenceGammeSource())) {

							ECMigrationSimulationInfo elementContractuelInfoMigration =
									produitMigration.getElementContractuelInfo(elementContractuelInfo);
							elementsMigre.add(elementContractuelInfoMigration);
							elementsRetour.add(elementContractuelInfo);
							resultatSimulation.getEchangeInfos().add(
									elementContractuelInfo.getEchangeInfo(produitMigration
											.getReferenceProduitDestination()));

						}

						/*
						 * Si c'est une migration dans une autre gamme.
						 */
						else {
							elementsAjoute.add(produitMigration.getElementContractuelInfo(elementContractuelInfo));
							elementsResilie.add(elementContractuelInfo);
							resultatSimulation.getLivraisonInfos().add(
									produitMigration.getElementContractuelInfo().getLivraisonInfo());
							resultatSimulation.getRetourInfos().add(elementContractuelInfo.getRetourInfo());

							// elementsRetour.add(elementContractuelInfo);

						}

					}

				}
			}
		}

		/*
		 * On cherche les elements de retour. Ces sont les elements qui existe dans l'ancien contrat et n'existe pas
		 * dans l'avenant.
		 */
		for (ProduitMigration produitMigration : infoMigrations) {
			for (ECMigrationSimulationInfo elementContractuelInfo : infoContratActuel) {

				if (produitMigration.isMigrationAdministrative(elementContractuelInfo)) {
					ECMigrationSimulationInfo elementMigrationAdministrative =
							produitMigration.getElementContractuelInfo(elementContractuelInfo);
					elementMigrationAdministrative.setEcSimulationResultat(elementContractuelInfo
							.getEcSimulationResultat());
					elementsMigrationAdministrative.add(elementMigrationAdministrative);
				}
			}
		}

		/*
		 * On cherche les elements de retour. Ces sont les elements qui existe dans l'ancien contrat et n'existe pas
		 * dans l'avenant.
		 */
		for (ECMigrationSimulationInfo elementContractuelInfo : infoContratActuel) {
			boolean estPresent = false;
			ChercherECRetour: for (ProduitMigration produitMigration : infoMigrations) {
				if (elementContractuelInfo.getNumEC() == produitMigration.getNumEC()) {
					estPresent = true;
					break ChercherECRetour;
				}
			}

			/*
			 * S’il y a un element de retour, alors copier le comportement de la retour.
			 */
			if (!estPresent) {
				elementsResilie.add(elementContractuelInfo);
				resultatSimulation.getRetourInfos().add(elementContractuelInfo.getRetourInfo());
			}
		}

		resultatSimulation =
				simulateurBillingMigration.calculeBillingElementsAjoute(resultatSimulation, elementsAjoute,
						politiqueMigration);
		resultatSimulation =
				simulateurBillingMigration.calculeBillingElementsMigre(resultatSimulation, elementsMigre,
						politiqueMigration);
		resultatSimulation =
				simulateurBillingMigration.calculeBillingElementsMigrationAdministrative(resultatSimulation,
						elementsMigrationAdministrative, politiqueMigration);
		resultatSimulation =
				simulateurBillingMigration.calculeBillingElementsResilier(resultatSimulation, elementsResilie,
						politiqueMigration);

		return resultatSimulation;

	}
}