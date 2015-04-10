package com.nordnet.topaze.client.rest.business.livraison;

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
	private Double montantDefini;

	/**
	 * Le montant de frais resiliation.
	 */
	private Double montantResiliation;

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
	 * Gets the montant defini.
	 * 
	 * @return the montant defini
	 */
	public Double getMontantDefini() {
		return montantDefini;
	}

	/**
	 * Sets the montant defini.
	 * 
	 * @param montantDefini
	 *            the new montant defini
	 */
	public void setMontantDefini(Double montantDefini) {
		this.montantDefini = montantDefini;
	}

	/**
	 * @return {@link #montantResiliation}.
	 */
	public Double getMontantResiliation() {
		return montantResiliation;
	}

	/**
	 * @param montantResiliation
	 *            {@link #montantResiliation}.
	 */
	public void setMontantResiliation(Double montantResiliation) {
		this.montantResiliation = montantResiliation;
	}

}
