package com.nordnet.topaze.livraison.core.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nordnet.topaze.livraison.core.util.TypePrixDeserializer;

/**
 * Cette classe regroupe les informations qui d�finissent un {@link Prix}.
 * <ul>
 * <li>Un prix est montant qui peut �voluer suite � des d�cisions de NordNet.</li>
 * <li>Ces modifications ne doivent pas influencer les tarifs des sous-contrats, reli�s aux produits dont le prix change
 * !</li>
 * </ul>
 * 
 * @author Denden-OUSSAMA
 * 
 */
@JsonIgnoreProperties({ "id" })
public class Prix {

	/**
	 * clé primaire.
	 */
	private Integer id;

	/**
	 * montant de prix.
	 */
	private Double montant;

	/**
	 * {@link TypePrix}.
	 */
	@JsonDeserialize(using = TypePrixDeserializer.class)
	private TypePrix typePrix;

	/**
	 * engagement par exmple 24 mois.
	 */
	private Integer engagement;

	/**
	 * {@link Produit}.
	 */
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
	 * @return {@link TypePrix}.
	 */
	public TypePrix getTypePrix() {
		return typePrix;
	}

	/**
	 * @param typePrix
	 *            {@link TypePrix}.
	 */
	public void setTypePrix(TypePrix typePrix) {
		this.typePrix = typePrix;
	}

	/**
	 * @return engagement.
	 */
	public Integer getEngagement() {
		return engagement;
	}

	/**
	 * @param engagement
	 *            engagement.
	 */
	public void setEngagement(Integer engagement) {
		this.engagement = engagement;
	}

	/**
	 * @return {@link Produit}.
	 */
	public Produit getProduit() {
		return produit;
	}

	/**
	 * @param produit
	 *            {@link Produit}.
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
