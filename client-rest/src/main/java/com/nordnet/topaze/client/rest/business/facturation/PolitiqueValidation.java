package com.nordnet.topaze.client.rest.business.facturation;

/**
 * La politique de resiliation.
 */
public class PolitiqueValidation {

	/**
	 * frais de création.
	 */
	private boolean fraisCreation;

	/**
	 * le commentaire .
	 */
	private String commentaire;

	/**
	 * Instantiates a new politique validation.
	 */
	public PolitiqueValidation() {
		super();
	}

	/* Getters & Setters */

	/**
	 * Checks if is frais creation.
	 * 
	 * @return true, if is frais creation
	 */
	public boolean isFraisCreation() {
		return fraisCreation;
	}

	/**
	 * Sets the frais creation.
	 * 
	 * @param fraisCreation
	 *            the new frais creation
	 */
	public void setFraisCreation(boolean fraisCreation) {
		this.fraisCreation = fraisCreation;
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
}
