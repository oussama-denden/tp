package com.nordnet.topaze.catalogue.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nordnet.topaze.catalogue.util.TypeFraisDeserializer;

/**
 * Cette classe regroupe les informations qui definissent un {@link FraisProduit}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Entity
@Table(name = "fraisproduit")
@JsonIgnoreProperties({ "id", "produit" })
public class FraisProduit {

	/**
	 * cle primaire.
	 */
	@Id
	@GeneratedValue
	private Integer id;

	/**
	 * montant de frais.
	 */
	@NotNull
	private Double montant;

	/**
	 * {@link Produit}.
	 */
	@ManyToOne
	@JoinColumn(name = "produitId")
	private Produit produit;

	/**
	 * Type de Frais.
	 */
	@JsonDeserialize(using = TypeFraisDeserializer.class)
	@Enumerated(EnumType.STRING)
	@NotNull
	private TypeFrais typeFrais;

	/**
	 * constructeur par defaut.
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

	/**
	 * @return {@link #produit}.
	 */
	public Produit getProduit() {
		return produit;
	}

	/**
	 * @param produit
	 *            {@link #produit}.
	 */
	public void setProduit(Produit produit) {
		this.produit = produit;
	}

	/**
	 * @return {@link #typeFrais}.
	 */
	public TypeFrais getTypeFrais() {
		return typeFrais;
	}

	/**
	 * @param typeFrais
	 *            {@link #typeFrais}
	 */
	public void setTypeFrais(TypeFrais typeFrais) {
		this.typeFrais = typeFrais;
	}

}
