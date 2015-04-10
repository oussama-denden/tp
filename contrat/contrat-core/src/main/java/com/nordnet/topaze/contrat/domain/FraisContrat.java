package com.nordnet.topaze.contrat.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nordnet.topaze.contrat.business.Produit;
import com.nordnet.topaze.contrat.util.TypeFraisDeserializer;
import com.nordnet.topaze.contrat.util.Utils;

/**
 * Cette classe regroupe les informations qui definissent un {@link FraisContrat}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Entity
@Table(name = "fraiscontrat")
@JsonIgnoreProperties({ "id", "contrat" })
public class FraisContrat {

	/**
	 * cle primaire.
	 */
	@Id
	@GeneratedValue
	private Integer id;

	/**
	 * titre de frais par exmemple: Frais de dossier.
	 */
	private String titre;

	/**
	 * montant de frais.
	 */
	@NotNull
	private Double montant;

	/**
	 * Type de Frais.
	 */
	@JsonDeserialize(using = TypeFraisDeserializer.class)
	@Enumerated(EnumType.STRING)
	@NotNull
	private TypeFrais typeFrais;

	/**
	 * l'order d'application des frais de resiliation.
	 */
	private Integer ordre;

	/**
	 * le nombre de mois que le frais de resiliation reste valable ex: pour un {@link #nombreMois} egal 12, si une
	 * resiliation est effectue lors des 12 premier mois ce frais sera applique.
	 */
	private Integer nombreMois;

	/**
	 * constructeur par defaut.
	 */
	public FraisContrat() {

	}

	@Override
	public String toString() {
		return "FraisContrat [id=" + id + ", montant=" + montant + ", typeFrais=" + typeFrais + ", contrat="
				+ ", ordre=" + ordre + ", nombreMois=" + nombreMois + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Produit)) {
			return false;
		}
		FraisContrat rhs = (FraisContrat) obj;
		return new EqualsBuilder().append(id, rhs.id).append(montant, rhs.montant).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(43, 11).append(id).append(montant).toHashCode();
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
	 * @return {@link #titre}.
	 */
	public String getTitre() {
		if (Utils.isStringNullOrEmpty(titre)) {
			switch (typeFrais) {
			case CREATION:
				return "Frais Creation";
			case RESILIATION:
				return "Frais Resiliation";
			case MIGRATION:
				return "Frais Migration";

			}
		}
		return titre;
	}

	/**
	 * @param titre
	 *            {@link #titre}.
	 */
	public void setTitre(String titre) {
		this.titre = titre;
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
	 * @return {@link #typeFrais}.
	 */
	public TypeFrais getTypeFrais() {
		return typeFrais;
	}

	/**
	 * @param typeFrais
	 *            {@link #typeFrais}.
	 */
	public void setTypeFrais(TypeFrais typeFrais) {
		this.typeFrais = typeFrais;
	}

	/**
	 * 
	 * @return {@link #ordre}
	 */
	public Integer getOrdre() {
		return ordre;
	}

	/**
	 * 
	 * @param ordre
	 *            {@link #ordre}
	 */
	public void setOrdre(Integer ordre) {
		this.ordre = ordre;
	}

	/**
	 * 
	 * @return {@link #nombreMois}
	 */
	public Integer getNombreMois() {
		return nombreMois;
	}

	/**
	 * 
	 * @param nombreMois
	 *            {@link #nombreMois}
	 */
	public void setNombreMois(Integer nombreMois) {
		this.nombreMois = nombreMois;
	}

	/**
	 * creer une copie avec id = null.
	 * 
	 * @return {@link FraisContrat}.
	 */
	public FraisContrat copy() {
		FraisContrat fraisContrat = new FraisContrat();
		fraisContrat.setTitre(titre);
		fraisContrat.setMontant(montant);
		fraisContrat.setTypeFrais(typeFrais);
		fraisContrat.setOrdre(ordre);
		fraisContrat.setNombreMois(nombreMois);

		return fraisContrat;
	}

}
