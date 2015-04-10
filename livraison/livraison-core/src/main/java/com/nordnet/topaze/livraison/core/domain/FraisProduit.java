package com.nordnet.topaze.livraison.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Cette classe regroupe les informations qui definissent un {@link FraisProduit}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@JsonIgnoreProperties({ "id", "produit" })
public class FraisProduit {

	/**
	 * cle primaire.
	 */
	private Integer id;

	/**
	 * montant de frais.
	 */
	private Double montant;

	/**
	 * constructeur par d�faut.
	 */
	public FraisProduit() {

	}

	/* Getters & Setters */

	/**
	 * @return {@link #id}.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            {@link #id}.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return {@link #montant}.
	 */
	public Double getMontant() {
		return montant;
	}

	/**
	 * @param montant
	 *            {@link #montant}.
	 */
	public void setMontant(Double montant) {
		this.montant = montant;
	}

}
