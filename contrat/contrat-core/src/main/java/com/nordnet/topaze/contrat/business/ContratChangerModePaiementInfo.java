package com.nordnet.topaze.contrat.business;

import java.util.Date;
import java.util.List;

/**
 * Cette classe regroupe les informations qui definissent un {@link ContratChangerModePaiementInfo}. Ces informations
 * seront utilisée pour la validation d'un contrat.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class ContratChangerModePaiementInfo {

	/**
	 * Info de paiement de produit {@link PaiementInfo}.
	 */
	private List<PaiementModificationInfo> produits;

	/**
	 * L'usager.
	 */
	private String user;

	/**
	 * commentaire.
	 */
	private String commentaire;

	/**
	 * La date action de changement.
	 */
	private Date dateAction;

	/**
	 * constructeur par defaut.
	 */
	public ContratChangerModePaiementInfo() {

	}

	/* Getters & Setters */

	/**
	 * 
	 * @return {@link #List<PaiementModificationInfo>}.
	 */
	public List<PaiementModificationInfo> getProduits() {
		return produits;
	}

	/**
	 * 
	 * @param produits
	 *            {@link #List<PaiementModificationInfo>}.
	 */
	public void setProduits(List<PaiementModificationInfo> produits) {
		this.produits = produits;
	}

	/**
	 * 
	 * @return {@link #commentaire}.
	 */
	public String getCommentaire() {
		return commentaire;
	}

	/**
	 * 
	 * @param commentaire
	 *            {@link #commentaire}.
	 */
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	/**
	 * 
	 * @return {@link #dateAction}.
	 */
	public Date getDateAction() {
		return dateAction;
	}

	/**
	 * 
	 * @param dateAction
	 *            {@link #dateAction}
	 */
	public void setDateAction(Date dateAction) {
		this.dateAction = dateAction;
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
