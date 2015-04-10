package com.nordnet.topaze.migration.outil.facturation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import com.nordnet.common.valueObject.date.TimePeriod;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.migration.outil.business.DiscountInfo;
import com.nordnet.topaze.migration.outil.business.ECMigrationSimulationInfo;
import com.nordnet.topaze.migration.outil.business.FraisMigrationSimulation;
import com.nordnet.topaze.migration.outil.business.ReductionInfo;
import com.nordnet.topaze.migration.outil.enums.TypeFrais;
import com.nordnet.topaze.migration.outil.util.Constants;
import com.nordnet.topaze.migration.outil.util.PropertiesUtil;

/**
 * Simuler le calcule de frais de resiliation d'un contrat.
 * 
 * @author Oussama Denden
 * 
 */
@Component("calculateurFrais")
public class SimulateurCalculeFrais {

	/**
	 * Calcule de Frais avec montant definit.
	 * 
	 * @param montantFraisResiliation
	 *            montant definit.
	 * @return {@link Frais}.
	 */
	public FraisMigrationSimulation fraisResiliationMontantDefinit(Double montantFraisResiliation) {
		FraisMigrationSimulation frais = new FraisMigrationSimulation();
		frais.setTitre("Frais de resiliation à montant definit");
		frais.setMontant(montantFraisResiliation);
		frais.setTypeFrais(TypeFrais.RESILIATION);
		return frais;
	}

	/**
	 * Calcule des frais de resiliation.
	 * 
	 * @param elementContractuelInfo
	 *            {@link ECMigrationSimulationInfo}.
	 * @return {@link FraisMigrationSimulation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public FraisMigrationSimulation fraisResiliation(ECMigrationSimulationInfo elementContractuelInfo)
			throws TopazeException {
		FraisMigrationSimulation frais = null;
		if (elementContractuelInfo.getFrais() != null && elementContractuelInfo.getFrais().size() > 0) {
			frais = getFraisResiliationActif(elementContractuelInfo);
			if (frais != null) {
				double montantpayment = frais.getMontant();

				// chercher la meilleur reduction.
				DiscountInfo discountInfo =
						ReductionUtils.getMeilleurReduction(frais.getReductionInfoContrat(),
								frais.getReductionInfoEC(), elementContractuelInfo.getPeriodicite(), montantpayment);
				if (discountInfo != null) {
					frais.setDiscountInfo(discountInfo);
				}
			}
		}

		return frais;
	}

	/**
	 * retourne le frais a applique lors de la resiliation.
	 * 
	 * @param elementContractuelInfo
	 *            {@link ECMigrationSimulationInfo}
	 * @return {@link Frais}
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	private static FraisMigrationSimulation getFraisResiliationActif(ECMigrationSimulationInfo elementContractuelInfo)
			throws TopazeException {
		Collections.sort(elementContractuelInfo.getFraisResiliations());
		LocalDate dateDebut = LocalDate.fromDateFields(elementContractuelInfo.getDateDebutFacturation());
		LocalDate dateJour = PropertiesUtil.getInstance().getDateDuJour();
		for (FraisMigrationSimulation frais : elementContractuelInfo.getFraisResiliations()) {
			if (frais.getNombreMois() != null) {
				LocalDate dateFin = dateDebut.plusMonths(frais.getNombreMois()).minusDays(Constants.UN);
				TimePeriod period = new TimePeriod(dateDebut, dateFin);
				if (period.contains(dateJour)) {
					return frais;
				}
				dateDebut = dateFin.plusDays(Constants.UN);
			} else {
				return frais;
			}
		}
		return null;
	}

	/**
	 * Facturer frais migration.
	 * 
	 * @param montantMigration
	 *            le montant du frais de migration.
	 * @param reductionSurFraisMigration
	 *            la reduction associe au frais de migration.
	 * @return le frais de migration.
	 */
	public FraisMigrationSimulation fraisMigration(Double montantMigration, ReductionInfo reductionSurFraisMigration) {

		FraisMigrationSimulation frais = null;

		frais = new FraisMigrationSimulation();
		frais.setTypeFrais(TypeFrais.MIGRATION);
		frais.setMontant(montantMigration);
		frais.setTitre("Frais migration");
		DiscountInfo discountInfo =
				ReductionUtils.getMeilleurReduction(reductionSurFraisMigration, null, Constants.UN, montantMigration);
		frais.setDiscountInfo(discountInfo);

		return frais;
	}

	/**
	 * Envoie des frais de creation pour le nouveau contrat apres une migration.
	 * 
	 * @param elementsContractuelInfo
	 *            list des {@link ECMigrationSimulationInfo} associe au nouveau contrat.
	 * @return {@link FraisMigrationSimulation}.
	 */
	public List<FraisMigrationSimulation> fraisCreationPourMigration(ECMigrationSimulationInfo elementContractuelInfo) {
		List<FraisMigrationSimulation> frais = new ArrayList<>();
		for (FraisMigrationSimulation fraisMigrationSimulation : elementContractuelInfo.getFrais()) {
			if (fraisMigrationSimulation.getTypeFrais().equals(TypeFrais.CREATION)) {
				DiscountInfo discountInfo =
						ReductionUtils.getMeilleurReduction(fraisMigrationSimulation.getReductionInfoContrat(),
								fraisMigrationSimulation.getReductionInfoEC(), elementContractuelInfo.getPeriodicite(),
								fraisMigrationSimulation.getMontant());
				fraisMigrationSimulation.setDiscountInfo(discountInfo);
				frais.add(fraisMigrationSimulation);
			}
		}
		return frais;
	}
}