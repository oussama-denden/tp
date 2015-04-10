package com.nordnet.topaze.contrat.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nordnet.topaze.contrat.business.PolitiqueResiliation;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.contrat.util.Utils;
import com.nordnet.topaze.exception.TopazeException;

/**
 * contient tout les informations necessaire pour la politique de Migration {@link PolitiqueMigration}.
 * 
 * @author anisselmane.
 */
@Entity
@Table(name = "politiquemigration")
@JsonIgnoreProperties({ "id" })
public class PolitiqueMigration {

	/**
	 * cle primaire.
	 */
	@Id
	@GeneratedValue
	private Integer id;

	/**
	 * Le frais de migration.
	 */
	private Double fraisMigration;

	/**
	 * frais de resiliation.
	 */
	private boolean fraisResiliation;

	/**
	 * Le montant de frais resiliation.
	 */
	private Double montantResiliation;

	/**
	 * Flag remboursement.
	 */
	private boolean remboursement;

	/**
	 * Le montant defini pour le remboursement.
	 */
	private Double montantRemboursement;

	/**
	 * Penalite de resiliation.
	 */
	private boolean penalite;

	/**
	 * La Date de résiliation.
	 */
	@Temporal(TemporalType.DATE)
	private Date dateAction;

	/**
	 * Appliquer les anciennes réductions.
	 */
	private boolean reductionAncienne;

	/**
	 * frais de creation.
	 */
	private boolean fraisCreation;

	/**
	 * utilisateur acteur dans le processus
	 */
	private String user;

	/**
	 * Instantiates a new politique resiliation.
	 */
	public PolitiqueMigration() {
		super();
	}

	/* Getters & Setters */
	/**
	 * Checks if is remboursement.
	 * 
	 * @return true, if is remboursement
	 */
	public boolean isRemboursement() {
		return remboursement;
	}

	/**
	 * Gets the frais migration.
	 * 
	 * @return the frais migration
	 */
	public Double getFraisMigration() {
		return fraisMigration;
	}

	/**
	 * Sets the frais migration.
	 * 
	 * @param fraisMigration
	 *            the new frais migration
	 */
	public void setFraisMigration(Double fraisMigration) {
		this.fraisMigration = fraisMigration;
	}

	/**
	 * Checks if is frais resiliation.
	 * 
	 * @return true, if is frais resiliation
	 */
	public boolean isFraisResiliation() {
		return fraisResiliation;
	}

	/**
	 * Sets the frais resiliation.
	 * 
	 * @param fraisResiliation
	 *            the new frais resiliation
	 */
	public void setFraisResiliation(boolean fraisResiliation) {
		this.fraisResiliation = fraisResiliation;
	}

	/**
	 * Checks if is penalite.
	 * 
	 * @return true, if is penalite
	 */
	public boolean isPenalite() {
		return penalite;
	}

	/**
	 * Sets the penalite.
	 * 
	 * @param penalite
	 *            the new penalite
	 */
	public void setPenalite(boolean penalite) {
		this.penalite = penalite;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Sets the remboursement.
	 * 
	 * @param remboursement
	 *            the new remboursement
	 */
	public void setRemboursement(boolean remboursement) {
		this.remboursement = remboursement;
	}

	/**
	 * Gets the montant Remboursement.
	 * 
	 * @return the montant Remboursement
	 */
	public Double getMontantRemboursement() {
		return montantRemboursement;
	}

	/**
	 * Sets the montant Remboursement.
	 * 
	 * @param montantRemboursement
	 *            the new montant remboursement
	 */
	public void setMontantRemboursement(Double montantRemboursement) {
		this.montantRemboursement = montantRemboursement;
	}

	/**
	 * Gets the montant resiliation.
	 * 
	 * @return {@link #montantResiliation}.
	 */
	public Double getMontantResiliation() {
		return montantResiliation;
	}

	/**
	 * Sets the montant resiliation.
	 * 
	 * @param montantResiliation
	 *            the new montant resiliation {@link #montantResiliation}.
	 */
	public void setMontantResiliation(Double montantResiliation) {
		this.montantResiliation = montantResiliation;
	}

	/**
	 * Gets the date resiliation.
	 * 
	 * @return the date resiliation
	 */
	public Date getDateAction() {
		return dateAction;
	}

	/**
	 * Sets the date resiliation.
	 * 
	 * @param dateAction
	 *            the new date action
	 */
	public void setDateAction(Date dateAction) {
		this.dateAction = dateAction;
	}

	/**
	 * Checks if is frais creation.
	 * 
	 * @return true, if is frais creation
	 */
	public boolean isFraisCreation() {
		return fraisCreation;
	}

	/**
	 * Sets the frais creation.
	 * 
	 * @param fraisCreation
	 *            the new frais creation
	 */
	public void setFraisCreation(boolean fraisCreation) {
		this.fraisCreation = fraisCreation;
	}

	/**
	 * 
	 * @return {@link PolitiqueMigration#reductionAncienne}
	 */
	public boolean isReductionAncienne() {
		return reductionAncienne;
	}

	/**
	 * 
	 * @param reductionAncienne
	 *            {@link #reductionAncienne}
	 */
	public void setReductionAncienne(boolean reductionAncienne) {
		this.reductionAncienne = reductionAncienne;
	}

	/**
	 * 
	 * @return {@link user}
	 */
	public String getUser() {
		return user;
	}

	/**
	 * 
	 * @param user
	 *            {@link String}
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Tester si c'est une résiliation futur.
	 * 
	 * @return
	 * @throws TopazeException
	 */
	public boolean isMigrationFutur() throws TopazeException {
		return Utils.compareDate(dateAction, PropertiesUtil.getInstance().getDateDuJour().toDate()) > Constants.ZERO;
	}

	/**
	 * Tester si c'est une résiliation futur.
	 * 
	 * @return
	 * @throws TopazeException
	 */
	public boolean isMigrationToDay() throws TopazeException {
		return Utils.compareDate(dateAction, PropertiesUtil.getInstance().getDateDuJour().toDate()) == 0;
	}

	/**
	 * 
	 * Mapping politique Migration domain à business.
	 * 
	 * @param politiqueMigration
	 *            la politique Migration
	 * @return la politique Migration
	 */
	public com.nordnet.topaze.contrat.business.PolitiqueMigration toBusiness() {
		com.nordnet.topaze.contrat.business.PolitiqueMigration politiqueMigrationBus =
				new com.nordnet.topaze.contrat.business.PolitiqueMigration();
		politiqueMigrationBus.setFraisMigration(fraisMigration);
		politiqueMigrationBus.setDateAction(dateAction);
		politiqueMigrationBus.setRemboursement(remboursement);
		politiqueMigrationBus.setMontantResiliation(montantResiliation);
		politiqueMigrationBus.setFraisCreation(fraisCreation);
		politiqueMigrationBus.setFraisResiliation(fraisResiliation);
		politiqueMigrationBus.setMontantRemboursement(montantRemboursement);
		politiqueMigrationBus.setPenalite(penalite);
		politiqueMigrationBus.setReductionAncienne(reductionAncienne);

		return politiqueMigrationBus;
	}

	/**
	 * Gets politique de resiliation à partir de politique de migration.
	 * 
	 * @param politiqueMigration
	 *            the politique migration
	 * @return the pR from pm
	 */
	public PolitiqueResiliation getPRFromPM() {
		PolitiqueResiliation politiqueResiliation = new PolitiqueResiliation();

		politiqueResiliation.setFraisResiliation(fraisResiliation);
		politiqueResiliation.setMontantRemboursement(montantRemboursement);
		politiqueResiliation.setMontantResiliation(montantResiliation);
		politiqueResiliation.setPenalite(penalite);
		politiqueResiliation.setRemboursement(remboursement);
		politiqueResiliation.setTypeResiliation(TypeResiliation.RIC);
		politiqueResiliation.setMotif(MotifResiliation.AUTRE);

		return politiqueResiliation;
	}

}
