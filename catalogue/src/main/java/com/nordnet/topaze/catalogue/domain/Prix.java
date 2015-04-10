package com.nordnet.topaze.catalogue.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nordnet.topaze.catalogue.util.TypePrixDeserializer;

/**
 * Cette classe regroupe les informations qui definissent un {@link Prix}.
 * <ul>
 * <li>Un prix est montant qui peut evoluer suite a des decisions de NordNet.</li>
 * <li>Ces modifications ne doivent pas influencer les tarifs des sous-contrats, relies aux produits dont le prix change
 * !</li>
 * </ul>
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Entity
@Table(name = "prix")
@JsonIgnoreProperties({ "id", "produit" })
public class Prix {

	/**
	 * cle primaire.
	 */
	@Id
	@GeneratedValue
	private Integer id;

	/**
	 * montant de prix.
	 */
	@NotNull
	private Double montant;

	/**
	 * {@link TypePrix}.
	 */
	@JsonDeserialize(using = TypePrixDeserializer.class)
	@Enumerated(EnumType.STRING)
	@NotNull
	private TypePrix typePrix;

	/**
	 * engagement par exmple 24 mois.
	 */
	private Integer engagement;

	/**
	 * {@link Produit}.
	 */
	@OneToOne
	@JoinColumn(name = "produitId")
	private Produit produit;

	/**
	 * constructeur par defaut.
	 */
	public Prix() {

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
	 * @return {@link #typePrix}.
	 */
	public TypePrix getTypePrix() {
		return typePrix;
	}

	/**
	 * @param typePrix
	 *            {@link #typePrix}.
	 */
	public void setTypePrix(TypePrix typePrix) {
		this.typePrix = typePrix;
	}

	/**
	 * @return {@link #engagement}.
	 */
	public Integer getEngagement() {
		return engagement;
	}

	/**
	 * @param engagement
	 *            {@link #engagement}.
	 */
	public void setEngagement(Integer engagement) {
		this.engagement = engagement;
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

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Prix)) {
			return false;
		}
		Prix rhs = (Prix) obj;
		return new EqualsBuilder().append(id, rhs.id).append(montant, rhs.montant).append(typePrix, rhs.typePrix)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(43, 11).append(id).append(montant).append(typePrix).toHashCode();
	}

}
