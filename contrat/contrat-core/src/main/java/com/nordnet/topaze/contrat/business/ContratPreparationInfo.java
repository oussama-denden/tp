package com.nordnet.topaze.contrat.business;

import java.util.List;

/**
 * Cette classe regroupe les informations qui definissent un {@link ContratPreparationInfo}. Ces informations seront
 * utilisée pour la préparation d'un contrat.
 * 
 * @author anisselmane.
 * 
 */
public class ContratPreparationInfo {

	/**
	 * Liste produits.
	 */
	private List<Produit> produits;

	/**
	 * l'usager.
	 */
	private String user;

	/**
	 * constructeur par defaut.
	 */
	public ContratPreparationInfo() {

	}

	/* Getters & Setters */

	/**
	 * Gets the produits.
	 * 
	 * @return the produits
	 */
	public List<Produit> getProduits() {
		return produits;
	}

	/**
	 * Sets the produits.
	 * 
	 * @param produits
	 *            the new produits
	 */
	public void setProduits(List<Produit> produits) {
		this.produits = produits;
	}

	/**
	 * Gets the user.
	 * 
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 * 
	 * @param user
	 *            the new user
	 */
	public void setUser(String user) {
		this.user = user;
	}

}
