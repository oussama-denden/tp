package com.nordnet.topaze.resiliation.outil.business;

/**
 * Information de resiliation d'un ElementContractuel.
 * 
 * @author Denden Oussama
 * 
 */
public class ECResiliationInfo {

	/**
	 * reference du contrat global.
	 */
	private String referenceProduit;

	/**
	 * reference contrat global.
	 */
	private String referenceContrat;

	/**
	 * {@link PenaliteBillingInfo}.
	 */
	private PenaliteBillingInfo penaliteBillingInfo;

	/**
	 * {@link RemboursementBillingInfo}.
	 */
	private RemboursementBillingInfo remboursementBillingInfo;

	/**
	 * frais de resiliation.
	 */
	private Frais frais;

	/**
	 * constructeur par defaut.
	 */
	public ECResiliationInfo() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public String toString() {
		return "ECResiliationInfo [referenceProduit=" + referenceProduit + ", penaliteBillingInfo="
				+ penaliteBillingInfo + ", remboursementBillingInfo=" + remboursementBillingInfo + ", frais=" + frais
				+ "]";
	}

	/**
	 * @return {@link Frais}.
	 */
	public Frais getFrais() {
		return frais;
	}

	/**
	 * @param frais
	 *            {@link Frais}.
	 */
	public void setFrais(Frais frais) {
		this.frais = frais;
	}

	/**
	 * Gets the reference produit.
	 * 
	 * @return the reference produit
	 */
	public String getReferenceProduit() {
		return referenceProduit;
	}

	/**
	 * Sets the reference produit.
	 * 
	 * @param referenceProduit
	 *            the new reference produit
	 */
	public void setReferenceProduit(String referenceProduit) {
		this.referenceProduit = referenceProduit;
	}

	/**
	 * @return {@link #referenceContrat}.
	 */
	public String getReferenceContrat() {
		return referenceContrat;
	}

	/**
	 * 
	 * @param referenceContrat
	 *            {@link #referenceContrat}.
	 */
	public void setReferenceContrat(String referenceContrat) {
		this.referenceContrat = referenceContrat;
	}

	/**
	 * @return {@link #penaliteBillingInfo}.
	 */
	public PenaliteBillingInfo getPenaliteBillingInfo() {
		return penaliteBillingInfo;
	}

	/**
	 * @param penaliteBillingInfo
	 *            {@link #penaliteBillingInfo}.
	 */
	public void setPenaliteBillingInfo(PenaliteBillingInfo penaliteBillingInfo) {
		this.penaliteBillingInfo = penaliteBillingInfo;
	}

	/**
	 * @return {@link #remboursementBillingInfo}.
	 */
	public RemboursementBillingInfo getRemboursementBillingInfo() {
		return remboursementBillingInfo;
	}

	/**
	 * @param remboursementBillingInfo
	 *            {@link #remboursementBillingInfo}.
	 */
	public void setRemboursementBillingInfo(RemboursementBillingInfo remboursementBillingInfo) {
		this.remboursementBillingInfo = remboursementBillingInfo;
	}

}