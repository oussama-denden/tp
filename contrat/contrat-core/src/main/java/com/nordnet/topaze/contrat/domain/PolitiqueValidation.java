package com.nordnet.topaze.contrat.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Cette classe regroupe les informations qui definissent un {@link PolitiqueValidation}.
 * 
 * @author anisselmane.
 * 
 */
@Entity
@Table(name = "politiquevalidation")
@JsonIgnoreProperties({ "id", "contrat" })
public class PolitiqueValidation {

	/**
	 * cle primaire.
	 */
	@Id
	@GeneratedValue
	private Integer id;

	/**
	 * frais de création.
	 */
	private Boolean fraisCreation;

	/**
	 * Indiquer si le test 'isPackagerCreationPossible' doit etre effectuer ou non lors de l'envoi des services a
	 * packager.
	 */
	private Boolean checkIsPackagerCreationPossible;

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
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Checks if is frais creation.
	 * 
	 * @return true, if is frais creation
	 */
	public Boolean isFraisCreation() {
		return fraisCreation;
	}

	/**
	 * Sets the frais creation.
	 * 
	 * @param fraisCreation
	 *            the new frais creation
	 */
	public void setFraisCreation(Boolean fraisCreation) {
		this.fraisCreation = fraisCreation;
	}

	/**
	 * 
	 * @return {@link #checkIsPackagerCreationPossible}.
	 */
	public Boolean getCheckIsPackagerCreationPossible() {
		return checkIsPackagerCreationPossible;
	}

	/**
	 * 
	 * @param checkIsPackagerCreationPossible
	 *            {@link #checkIsPackagerCreationPossible}.
	 */
	public void setCheckIsPackagerCreationPossible(Boolean checkIsPackagerCreationPossible) {
		this.checkIsPackagerCreationPossible = checkIsPackagerCreationPossible;
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

	/**
	 * creer une copie avec id = null.
	 * 
	 * @return {@link PolitiqueValidation}.
	 */
	public PolitiqueValidation copy() {
		PolitiqueValidation politiqueValidation = new PolitiqueValidation();
		politiqueValidation.setFraisCreation(fraisCreation);
		politiqueValidation.setCheckIsPackagerCreationPossible(checkIsPackagerCreationPossible);
		return politiqueValidation;
	}

	/**
	 * 
	 * Mapping politique validation domain à business.
	 * 
	 * @return the politique validation
	 */
	public com.nordnet.topaze.contrat.business.PolitiqueValidation toBusiness() {
		com.nordnet.topaze.contrat.business.PolitiqueValidation politiqueValidationBus =
				new com.nordnet.topaze.contrat.business.PolitiqueValidation();
		politiqueValidationBus.setFraisCreation(this.fraisCreation);

		return politiqueValidationBus;
	}

}
