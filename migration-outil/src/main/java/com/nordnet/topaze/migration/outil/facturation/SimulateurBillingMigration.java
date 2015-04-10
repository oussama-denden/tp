package com.nordnet.topaze.migration.outil.facturation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.migration.outil.business.ECMigrationSimulationInfo;
import com.nordnet.topaze.migration.outil.business.FraisMigrationSimulation;
import com.nordnet.topaze.migration.outil.business.PenaliteBillingInfo;
import com.nordnet.topaze.migration.outil.business.PolitiqueMigration;
import com.nordnet.topaze.migration.outil.business.ReductionInfo;
import com.nordnet.topaze.migration.outil.business.RemboursementBillingInfo;
import com.nordnet.topaze.migration.outil.business.ResultatSimulation;
import com.nordnet.topaze.migration.outil.business.ResultatSimulationEC;
import com.nordnet.topaze.migration.outil.enums.TypeContrat;

/**
 * Simuler le calcule de Dernier Billing.
 * 
 * @author Oussama Denden
 * 
 */
@Component("simulateurBillingMigration")
public class SimulateurBillingMigration {

	/**
	 * {@link SimulateurCalculeFrais}.
	 */
	@Autowired
	private SimulateurCalculeFrais calculateurFrais;

	/**
	 * {@link SimulateurCacluleRemboursement}.
	 */
	@Autowired
	private SimulateurCacluleRemboursement calculateurRemboursement;

	/**
	 * {@link SimulateurCaclulePenalite}.
	 */
	@Autowired
	private SimulateurCaclulePenalite calculateurPenalite;

	private List<FraisMigrationSimulation> fraisCreation(ECMigrationSimulationInfo elementContractuelInfo) {
		List<FraisMigrationSimulation> fraisCreation =
				calculateurFrais.fraisCreationPourMigration(elementContractuelInfo);
		return fraisCreation;
	}

	private FraisMigrationSimulation fraisMigration(PolitiqueMigration politiqueMigration,
			List<ECMigrationSimulationInfo> elementsContractuelInfo) {
		FraisMigrationSimulation frais = null;
		/*
		 * facturation des frais de migration.
		 */
		if (politiqueMigration.getFraisMigration() != null) {
			ReductionInfo reductionSurFraisMigration = null;
			for (ECMigrationSimulationInfo elementContractuelInfo : elementsContractuelInfo) {
				if (elementContractuelInfo.getReductionSurFraisMigration() != null) {
					reductionSurFraisMigration = elementContractuelInfo.getReductionSurFraisMigration();
				}
			}

			frais = calculateurFrais.fraisMigration(politiqueMigration.getFraisMigration(), reductionSurFraisMigration);
		}

		return frais;
	}

	public ResultatSimulation calculeBillingElementsAjoute(ResultatSimulation resultatSimulation,
			List<ECMigrationSimulationInfo> elementsAjoute, PolitiqueMigration politiqueMigration) {
		if (politiqueMigration.isFraisCreation()) {
			List<ECMigrationSimulationInfo> localElementsAjoute = fraisCreation(elementsAjoute);
			for (ECMigrationSimulationInfo elementContractuelInfo : localElementsAjoute) {
				ResultatSimulationEC resultatSimulationEC = elementContractuelInfo.getEcSimulationResultat();
				resultatSimulation.getPaymentInfosLivraison().add(resultatSimulationEC);
			}
		}

		return resultatSimulation;
	}

	public ResultatSimulation calculeBillingElementsMigre(ResultatSimulation resultatSimulation,
			List<ECMigrationSimulationInfo> elementsMigre, PolitiqueMigration politiqueMigration)
			throws TopazeException {

		resultatSimulation.getFrais().add(fraisMigration(politiqueMigration, elementsMigre));
		List<ECMigrationSimulationInfo> localElementsMigre = elementsMigre;
		if (politiqueMigration.isFraisCreation()) {
			localElementsMigre = fraisCreation(elementsMigre);
		}

		// facturation des frais de resiliation
		if (politiqueMigration.isFraisResiliation() && politiqueMigration.getMontantResiliation() == null) {
			/*
			 * calcule de frais de resiliation.
			 */
			localElementsMigre = fraisResiliation(localElementsMigre);
		}

		if (politiqueMigration.isRemboursement() && politiqueMigration.getMontantRemboursement() == null) {
			localElementsMigre = remboursement(localElementsMigre, resultatSimulation.getReferenceContrat());
		}

		for (ECMigrationSimulationInfo elementContractuelInfo : localElementsMigre) {
			ResultatSimulationEC resultatSimulationEC = elementContractuelInfo.getEcSimulationResultat();
			resultatSimulation.getPaymentInfosMigration().add(resultatSimulationEC);
		}

		return resultatSimulation;
	}

	public ResultatSimulation calculeBillingElementsResilier(ResultatSimulation resultatSimulation,
			List<ECMigrationSimulationInfo> elementsResilie, PolitiqueMigration politiqueMigration)
			throws TopazeException {
		List<ECMigrationSimulationInfo> localElementsResilie = elementsResilie;
		// facturation des frais de resiliation
		if (politiqueMigration.isFraisResiliation() && politiqueMigration.getMontantResiliation() == null) {

			/*
			 * calcule de frais de resiliation.
			 */
			localElementsResilie = fraisResiliation(elementsResilie);
		}

		if (politiqueMigration.isRemboursement() && politiqueMigration.getMontantRemboursement() == null) {
			localElementsResilie = remboursement(elementsResilie, resultatSimulation.getReferenceContrat());
		}

		for (ECMigrationSimulationInfo elementContractuelInfo : localElementsResilie) {
			ResultatSimulationEC resultatSimulationEC = elementContractuelInfo.getEcSimulationResultat();
			resultatSimulation.getPaymentInfosResiliation().add(resultatSimulationEC);
		}

		return resultatSimulation;
	}

	private List<ECMigrationSimulationInfo> fraisCreation(List<ECMigrationSimulationInfo> elementsContractuelInfo) {
		for (ECMigrationSimulationInfo elementContractuelInfo : elementsContractuelInfo) {
			List<FraisMigrationSimulation> fraisCreation = fraisCreation(elementContractuelInfo);
			ResultatSimulationEC resultatSimulationEC = elementContractuelInfo.getEcSimulationResultat();
			resultatSimulationEC.getFrais().addAll(fraisCreation);
		}
		return elementsContractuelInfo;
	}

	private List<ECMigrationSimulationInfo> penalite(List<ECMigrationSimulationInfo> elementsContractuelInfo,
			String referenceContrat) throws TopazeException {
		/*
		 * Calculer les penalites sur uniquement les Ã©lÃ©ment parents les plus haut. On a autant de mouvements de
		 * pÃ©nalitÃ©s que dâ€™EC parent comportant un engagement.
		 */
		for (ECMigrationSimulationInfo elementContractuelInfo : elementsContractuelInfo) {
			if (elementContractuelInfo.isParent()
					&& elementContractuelInfo.getTypeContrat().equals(TypeContrat.ABONNEMENT)
					&& elementContractuelInfo.getDateDerniereFacture() != null) {
				PenaliteBillingInfo penaliteBillingInfo =
						calculateurPenalite.penalite(referenceContrat, elementContractuelInfo);
				ResultatSimulationEC resultatSimulationEC = elementContractuelInfo.getEcSimulationResultat();
				resultatSimulationEC.setPenaliteBillingInfo(penaliteBillingInfo);
			}
		}
		return elementsContractuelInfo;
	}

	private List<ECMigrationSimulationInfo> fraisResiliation(List<ECMigrationSimulationInfo> elementsContractuelInfo)
			throws TopazeException {

		/*
		 * si non pour chaque contrat billing info resilie ou migre pour chaque frais de resiliation en envoi un
		 * mouvement.
		 */
		for (ECMigrationSimulationInfo elementContractuelInfo : elementsContractuelInfo) {
			if (elementContractuelInfo.getDateDerniereFacture() != null) {
				FraisMigrationSimulation frais = calculateurFrais.fraisResiliation(elementContractuelInfo);
				ResultatSimulationEC resultatSimulationEC = elementContractuelInfo.getEcSimulationResultat();
				resultatSimulationEC.getFrais().add(frais);
			}
		}

		return elementsContractuelInfo;
	}

	private List<ECMigrationSimulationInfo> remboursement(List<ECMigrationSimulationInfo> elementsContractuelInfo,
			String referenceContrat) throws TopazeException {

		/*
		 * si non on clacule de manier normale le remboursement de chaque element contractuel apart.
		 */
		for (ECMigrationSimulationInfo elementContractuelInfo : elementsContractuelInfo) {
			if (elementContractuelInfo.isRemboursable() && elementContractuelInfo.getDateDerniereFacture() != null) {
				RemboursementBillingInfo remboursementBillingInfo =
						calculateurRemboursement.remboursement(referenceContrat, elementContractuelInfo);
				ResultatSimulationEC resultatSimulationEC = elementContractuelInfo.getEcSimulationResultat();
				resultatSimulationEC.setRemboursementBillingInfo(remboursementBillingInfo);
			}
		}
		return elementsContractuelInfo;
	}

	public ResultatSimulation calculeBillingElementsMigrationAdministrative(ResultatSimulation resultatSimulation,
			List<ECMigrationSimulationInfo> elementsMigrationAdministrative, PolitiqueMigration politiqueMigration)
			throws TopazeException {
		List<ECMigrationSimulationInfo> localElementsMigrationAdministrative = elementsMigrationAdministrative;
		if (politiqueMigration.isFraisCreation()) {
			localElementsMigrationAdministrative = fraisCreation(localElementsMigrationAdministrative);
		}

		// facturation des frais de resiliation
		if (politiqueMigration.isFraisResiliation() && politiqueMigration.getMontantResiliation() == null) {
			/*
			 * calcule de frais de resiliation.
			 */
			localElementsMigrationAdministrative = fraisResiliation(localElementsMigrationAdministrative);
		}

		if (politiqueMigration.isRemboursement() && politiqueMigration.getMontantRemboursement() == null) {
			localElementsMigrationAdministrative =
					remboursement(localElementsMigrationAdministrative, resultatSimulation.getReferenceContrat());
		}

		for (ECMigrationSimulationInfo elementContractuelInfo : localElementsMigrationAdministrative) {
			ResultatSimulationEC resultatSimulationEC = elementContractuelInfo.getEcSimulationResultat();
			resultatSimulation.getPaymentInfosMigrationAdministrative().add(resultatSimulationEC);
		}

		return resultatSimulation;
	}

	public ResultatSimulation calculeBillingContratActuel(ResultatSimulation resultatSimulation,
			Set<ECMigrationSimulationInfo> infoContratActuel, PolitiqueMigration politiqueMigration)
			throws TopazeException {

		if (politiqueMigration.isPenalite()) {
			List<ECMigrationSimulationInfo> infos = new ArrayList<>();
			infos.addAll(infoContratActuel);
			penalite(infos, resultatSimulation.getReferenceContrat());
		}

		/*
		 * calcule de frais de resiliation.
		 */
		if (politiqueMigration.isFraisResiliation() && politiqueMigration.getMontantResiliation() != null) {

			/*
			 * On a definit un frais de resiliation manuelement. Donc il faut envoyer un et un seul mouvement vers
			 * saphir. Pour sela il faut determiner le premier plus haut parent du contrat.
			 */
			ECMigrationSimulationInfo resiliationParent = null;
			ChercherParentResiliation: for (ECMigrationSimulationInfo elementContractuelInfo : infoContratActuel) {
				/*
				 * dans le cas ou le resiliation est en cascade il faut envoyer un mouvement seulement pour
				 * l'elementContractuel parent.
				 */
				if (elementContractuelInfo.isParent()) {
					resiliationParent = elementContractuelInfo;
					break ChercherParentResiliation;
				}
			}

			if (resiliationParent != null) {
				FraisMigrationSimulation frais =
						calculateurFrais.fraisResiliationMontantDefinit(politiqueMigration.getMontantResiliation());
				resultatSimulation.getFrais().add(frais);
			}
		}

		if (politiqueMigration.isRemboursement() && politiqueMigration.getMontantRemboursement() != null) {
			/*
			 * Il faut envoyer un et un seul mouvement vers saphir.
			 */
			ECMigrationSimulationInfo remboursementParent = null;
			ChercherParentRemboursement: for (ECMigrationSimulationInfo elementContractuelInfo : infoContratActuel) {
				/*
				 * On a definit un montant definit pour la rembourssement. Donc pour le cas d'un resiliation en cascade
				 * il faut envoyer un et un seul mouvement vers saphir. Pour sela il faut determiner
				 * l'elementContractuel parent.
				 */
				if (elementContractuelInfo.isParent()) {
					/*
					 * Pour le cas d'un resiliation en cascade. Tester si l'element contractuel est parent ou non.
					 */
					remboursementParent = elementContractuelInfo;
					break ChercherParentRemboursement;
				}
			}

			if (remboursementParent != null && remboursementParent.getDateDerniereFacture() != null) {
				RemboursementBillingInfo remboursementBillingInfo =
						calculateurRemboursement.remboursementMontantDefini(remboursementParent,
								politiqueMigration.getMontantRemboursement());
				resultatSimulation.setRemboursementBillingInfo(remboursementBillingInfo);
			}

		}

		return resultatSimulation;
	}
}
