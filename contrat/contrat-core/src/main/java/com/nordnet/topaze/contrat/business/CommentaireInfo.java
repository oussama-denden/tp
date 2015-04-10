package com.nordnet.topaze.contrat.business;

/**
 * Cette classe regroupe les informations qui definissent un {@link CommentaireInfo}. Ces informations seront utilisée
 * pour l'annulation d'une migration
 * 
 * @author anisselmane.
 * 
 */
public class CommentaireInfo {

	/**
	 * l'usager.
	 */
	private String user;

	/**
	 * le commentaire .
	 */
	private String commentaire;

	/* Getters & Setters */

	/**
	 * 
	 * @return {@link #user}
	 */
	public String getUser() {
		return user;
	}

	/**
	 * 
	 * @param user
	 *            {@link #user}
	 */
	public void setUser(String user) {
		this.user = user;
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

}
