package com.nordnet.topaze.finder.business;


/**
 * Cette classe regroupe les informations qui definissent un {@link ElementContractuel}.
 * 
 * @author anisselmane.
 * 
 */
public class ElementContractuel {

	/**
	 * referenceProduit(reference de produit auquel ce Contrat sera associe).
	 */
	private String referenceProduit;

	/**
	 * titre(titre de contrat par exemple: Contrat d'abonnement pour le produit Kit satellite 2Giga).
	 */
	private String titre;

	/**
	 * retailerPackagerId.
	 */
	private String retailerPackagerId;

	/**
	 * constructeur par defaut.
	 */
	public ElementContractuel() {

	}

	/* Getters & Setters */

	/**
	 * 
	 * @param titre
	 *            {@link #titre}.
	 */
	public void setTitre(String titre) {
		this.titre = titre;
	}

	/**
	 * 
	 * @return {@link #titre}.
	 */
	public String getTitre() {
		return titre;
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
	 * @return {@link String}.
	 */
	public String getRetailerPackagerId() {
		return retailerPackagerId;
	}

	/**
	 * 
	 * @param retailerPackagerId
	 *            {@link String}.
	 */
	public void setRetailerPackagerId(String retailerPackagerId) {
		this.retailerPackagerId = retailerPackagerId;
	}

}
