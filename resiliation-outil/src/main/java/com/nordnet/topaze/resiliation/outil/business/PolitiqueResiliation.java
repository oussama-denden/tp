package com.nordnet.topaze.resiliation.outil.business;

import java.util.Date;

/**
 * contient tout les informations necessaire pour la politique de resiliation.
 */

public class PolitiqueResiliation {

	/**
	 * Flag remboursement.
	 */
	private Boolean remboursement;

	/**
	 * frais de resiliation.
	 */
	private Boolean fraisResiliation;

	/**
	 * Penalite de resiliation.
	 */
	private Boolean penalite;

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
	private Boolean enCascade = true;

	/**
	 * commentaire de resiliation.
	 */
	private String commentaire;

	/**
	 * La Date de résiliation.
	 */
	private String dateResiliation;

	/**
	 * La Date de remboursement.
	 */
	private Date dateRemboursement;

	/**
	 * Instantiates a new politique resiliation.
	 */
	public PolitiqueResiliation() {
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
	 * 
	 * @return {@link PolitiqueResiliation#montantRemboursement}.
	 */
	public Double getMontantRemboursement() {
		return montantRemboursement;
	}

	/**
	 * 
	 * @param montantRemboursement
	 *            {@link PolitiqueResiliation#montantRemboursement}.
	 */
	public void setMontantRemboursement(Double montantRemboursement) {
		this.montantRemboursement = montantRemboursement;
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
	 * Checks if is en cascade.
	 * 
	 * @return true, if is en cascade
	 */
	public Boolean isEnCascade() {
		return enCascade;
	}

	/**
	 * Sets the en cascade.
	 * 
	 * @param enCascade
	 *            the new en cascade
	 */
	public void setEnCascade(Boolean enCascade) {
		this.enCascade = enCascade;
	}

	/**
	 * Gets the date resiliation.
	 * 
	 * @return the date resiliation
	 */
	public String getDateResiliation() {
		return dateResiliation;
	}

	/**
	 * Sets the date resiliation.
	 * 
	 * @param dateResiliation
	 *            the new date resiliation
	 */
	public void setDateResiliation(String dateResiliation) {
		this.dateResiliation = dateResiliation;
	}

	/**
	 * 
	 * @return {@link #commentaire}
	 */
	public String getCommentaire() {
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
	 * @param dateRemboursement
	 *            {@link #dateRemboursement}
	 */
	public void setDateRemboursement(Date dateRemboursement) {
		this.dateRemboursement = dateRemboursement;
	}

}
