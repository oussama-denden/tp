package com.nordnet.topaze.client.rest.business.facturation;

import java.util.Date;

/**
 * Cette classe regroupe les informations qui definissent un {@link PolitiqueResiliation}.
 * 
 * @author anisselmane.
 * 
 */

public class PolitiqueResiliation {

	/**
	 * Flag remboursement.
	 */
	private boolean remboursement;

	/**
	 * frais de resiliation.
	 */
	private boolean fraisResiliation;

	/**
	 * Penalite de resiliation.
	 */
	private boolean penalite;

	/**
	 * Le montant defini pour le remboursement.
	 */
	private Double montantRemboursement;

	/**
	 * Le montant de frais resiliation.
	 */
	private Double montantResiliation;

	/**
	 * Boolean résiliation en cascade.
	 */
	private boolean enCascade;

	/**
	 * le commentaire .
	 */
	private String commentaire;

	/**
	 * La Date de remboursement.
	 */
	private Date dateRemboursement;

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
	 * Sets the remboursement.
	 * 
	 * @param remboursement
	 *            the new remboursement
	 */
	public void setRemboursement(boolean remboursement) {
		this.remboursement = remboursement;
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
	 * @return {@link #montantResiliation}.
	 */
	public Double getMontantResiliation() {
		return montantResiliation;
	}

	/**
	 * 
	 * @return {@link #montantRemboursement}.
	 */
	public Double getMontantRemboursement() {
		return montantRemboursement;
	}

	/**
	 * 
	 * @param montantRemboursement
	 *            {@link #montantRemboursement}.
	 */
	public void setMontantRemboursement(Double montantRemboursement) {
		this.montantRemboursement = montantRemboursement;
	}

	/**
	 * @param montantResiliation
	 *            {@link #montantResiliation}.
	 */
	public void setMontantResiliation(Double montantResiliation) {
		this.montantResiliation = montantResiliation;
	}

	/**
	 * Checks if is en cascade.
	 * 
	 * @return true, if is en cascade
	 */
	public boolean isEnCascade() {
		return enCascade;
	}

	/**
	 * Sets the en cascade.
	 * 
	 * @param enCascade
	 *            the new en cascade
	 */
	public void setEnCascade(boolean enCascade) {
		this.enCascade = enCascade;
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
	 * 
	 * @return {@link #dateRemboursement}
	 */
	public Date getDateRemboursement() {
		return dateRemboursement;
	}

	/**
	 * 
	 * @param dateRembourcement
	 *            {@link #dateRembourcement}
	 */
	public void setDateRemboursement(Date dateRemboursement) {
		this.dateRemboursement = dateRemboursement;
	}

}
