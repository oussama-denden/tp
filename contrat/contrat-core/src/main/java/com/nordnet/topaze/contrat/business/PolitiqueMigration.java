package com.nordnet.topaze.contrat.business;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.exception.TopazeException;

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
	private Boolean fraisResiliation = null;

	/**
	 * Le montant de frais resiliation.
	 */
	private Double montantResiliation;

	/**
	 * Flag remboursement.
	 */
	private Boolean remboursement = null;

	/**
	 * Le montant defini pour le remboursement.
	 */
	private Double montantRemboursement;

	/**
	 * Penalite de resiliation.
	 */
	private Boolean penalite = null;

	/**
	 * La Date de résiliation.
	 */
	@Temporal(TemporalType.DATE)
	private Date dateAction;

	/**
	 * frais de creation.
	 */
	private Boolean fraisCreation = null;

	/**
	 * Appliquer les anciennes réductions.
	 */
	private Boolean reductionAncienne = null;

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
	public Boolean isRemboursement() {
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
	public Boolean isFraisResiliation() {
		return fraisResiliation;
	}

	/**
	 * Sets the frais resiliation.
	 * 
	 * @param fraisResiliation
	 *            the new frais resiliation
	 */
	public void setFraisResiliation(Boolean fraisResiliation) {
		this.fraisResiliation = fraisResiliation;
	}

	/**
	 * Checks if is penalite.
	 * 
	 * @return true, if is penalite
	 */
	public Boolean isPenalite() {
		return penalite;
	}

	/**
	 * Sets the penalite.
	 * 
	 * @param penalite
	 *            the new penalite
	 */
	public void setPenalite(Boolean penalite) {
		this.penalite = penalite;
	}

	/**
	 * Sets the remboursement.
	 * 
	 * @param remboursement
	 *            the new remboursement
	 */
	public void setRemboursement(Boolean remboursement) {
		this.remboursement = remboursement;
	}

	/**
	 * 
	 * @param fraisCreation
	 *            {@link #fraisCreation}
	 */
	public void setFraisCreation(Boolean fraisCreation) {
		this.fraisCreation = fraisCreation;
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
	public Boolean isFraisCreation() {
		return fraisCreation;
	}

	/**
	 * 
	 * @return {@link PolitiqueMigration#reductionAncienne}
	 */
	public Boolean isReductionAncienne() {
		return reductionAncienne;
	}

	/**
	 * 
	 * @param reductionAncienne
	 *            {@link #reductionAncienne}
	 */
	public void setReductionAncienne(Boolean reductionAncienne) {
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

	/**
	 * Mapping politique de migration buisness to domain.
	 * 
	 * @return the politique migration
	 * 
	 * @throws TopazeException
	 *             the topaze exception
	 */
	public com.nordnet.topaze.contrat.domain.PolitiqueMigration toDomain() throws TopazeException {
		com.nordnet.topaze.contrat.domain.PolitiqueMigration politiqueMigration =
				new com.nordnet.topaze.contrat.domain.PolitiqueMigration();

		if (dateAction != null) {
			politiqueMigration.setDateAction(dateAction);
		} else {
			politiqueMigration.setDateAction(PropertiesUtil.getInstance().getDateDuJour().toDate());
		}
		politiqueMigration.setFraisCreation(fraisCreation);
		politiqueMigration.setFraisMigration(fraisMigration);
		politiqueMigration.setFraisResiliation(fraisResiliation);
		politiqueMigration.setMontantRemboursement(montantRemboursement);
		politiqueMigration.setMontantResiliation(montantResiliation);
		politiqueMigration.setPenalite(penalite);
		politiqueMigration.setRemboursement(remboursement);
		politiqueMigration.setReductionAncienne(reductionAncienne);

		return politiqueMigration;

	}

}
