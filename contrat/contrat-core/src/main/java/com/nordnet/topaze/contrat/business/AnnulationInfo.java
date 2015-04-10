package com.nordnet.topaze.contrat.business;

/**
 * Cette classe regroupe les informations qui definissent un {@link AnnulationInfo}. Ces informations seront utilisée
 * pour l'annulation d'une migration
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class AnnulationInfo {

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

	/**
	 * Gets the commentaire.
	 * 
	 * @return the commentaire
	 */
	public String getCommentaire() {
		if (commentaire == null) {
			return "";
		}
		return commentaire;
	}

	/**
	 * Sets the commentaire.
	 * 
	 * @param commentaire
	 *            the new commentaire
	 */
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

}
