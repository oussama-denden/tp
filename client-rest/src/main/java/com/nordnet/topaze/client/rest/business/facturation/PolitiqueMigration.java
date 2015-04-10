package com.nordnet.topaze.client.rest.business.facturation;

import java.util.Date;

/**
 * contient tout les informations necessaire pour la politique de Migration {@link PolitiqueMigration}.
 * 
 * @author anisselmane.
 */
public class PolitiqueMigration {

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
	private Date dateAction;

	/**
	 * frais de creation.
	 */
	private boolean fraisCreation;

	/**
	 * Appliquer les anciennes réductions.
	 */
	private boolean reductionAncienne;

	/**
	 * le commentaire .
	 */
	private String commentaire;

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
	 * @return {@link #commentaire}
	 */
	public String getCommentaire() {
		if (commentaire == null)
			return "";
		return commentaire;
	}

	/**
	 * 
	 * @param commentaire
	 *            {@link #commentaire}
	 */
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
}
