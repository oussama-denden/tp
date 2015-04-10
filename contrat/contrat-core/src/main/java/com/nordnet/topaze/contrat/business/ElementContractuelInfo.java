package com.nordnet.topaze.contrat.business;

/**
 * Sert a transiter des informations sur un element Contractuel entre la brique contrat et le bp-packager.
 * 
 * @author akram-moncer
 * 
 */
public class ElementContractuelInfo {

	/**
	 * reference du contrat.
	 */
	private String referenceContrat;

	/**
	 * numero element contractuelle
	 */
	private Integer numEC;

	/**
	 * reference du produit.
	 */
	private String referenceProduit;

	/**
	 * true l'element contractuel à un prix ou pas.
	 */
	private boolean hasPrix;

	/**
	 * Constructeur par defaut.
	 */
	public ElementContractuelInfo() {

	}

	/**
	 * 
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
	 * the numero element contractuelle
	 * 
	 * @return {@link #numEC}
	 */
	public Integer getNumEC() {
		return numEC;
	}

	/**
	 * sets the new numero element contractuelle
	 * 
	 * @param numEC
	 *            the new {@link #numEC}
	 */
	public void setNumEC(Integer numEC) {
		this.numEC = numEC;
	}

	/**
	 * 
	 * @return {@link #referenceProduit}.
	 */
	public String getReferenceProduit() {
		return referenceProduit;
	}

	/**
	 * 
	 * @param referenceProduit
	 *            {@link #referenceProduit}.
	 */
	public void setReferenceProduit(String referenceProduit) {
		this.referenceProduit = referenceProduit;
	}

	/**
	 * 
	 * @return {@link #hasPrix}.
	 */
	public boolean isHasPrix() {
		return hasPrix;
	}

	/**
	 * 
	 * @param hasPrix
	 *            {@link #hasPrix}.
	 */
	public void setHasPrix(boolean hasPrix) {
		this.hasPrix = hasPrix;
	}

}
