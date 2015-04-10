package com.nordnet.topaze.contrat.business;

import java.util.Date;
import java.util.List;

/**
 * Cette classe regroupe les informations qui definissent un {@link ContratAvenant}.
 * 
 * @author anisselmane.
 * 
 */
public class ContratAvenant {

	/**
	 * le commentaire de l avenant.
	 */
	private String commentaire;

	/**
	 * La valeur a modifier.
	 */
	private List<ContratModification> contratModifications;

	/**
	 * Date modification.
	 */
	private Date dateAction;

	/**
	 * L'usager.
	 */
	private String user;

	/**
	 * constructeur par defaut.
	 */
	public ContratAvenant() {

	}

	/**
	 * Getter le commentaire.
	 * 
	 * @return commentaire
	 */
	public String getCommentaire() {
		return commentaire;
	}

	/**
	 * Setter le commentaire.
	 * 
	 * @param commentaire
	 *            commentaire
	 */
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	/**
	 * lister les modifications.
	 * 
	 * @return liste des modifications.
	 */
	public List<ContratModification> getContratModifications() {
		return contratModifications;
	}

	/**
	 * Setter les modifications.
	 * 
	 * @param contratModifications
	 *            liste des modifications.
	 */
	public void setContratModifications(List<ContratModification> contratModifications) {
		this.contratModifications = contratModifications;
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
	 *            {@link #dateAction}.
	 * 
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
