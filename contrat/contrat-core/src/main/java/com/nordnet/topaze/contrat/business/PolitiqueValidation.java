package com.nordnet.topaze.contrat.business;

/**
 * La politique de resiliation.
 */
public class PolitiqueValidation {

	/**
	 * frais de création.
	 */
	private boolean fraisCreation;

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
	 * Mapping politique validation domain à business.
	 * 
	 * @param politiqueValidation
	 *            the politique validation
	 * @return the politique validation
	 */
	public void mappingPolitique(com.nordnet.topaze.contrat.domain.PolitiqueValidation politiqueValidation) {
		this.fraisCreation = politiqueValidation.isFraisCreation();

	}

}
