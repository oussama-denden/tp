package com.nordnet.topaze.contrat.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Cette classe regroupe les informations qui definissent un {@link ModifContrat}.
 * 
 * @author anisselmane.
 * 
 */
@Entity
@Table(name = "modification")
@JsonIgnoreProperties({ "id", "avenant" })
public class Modification {

	/**
	 * cle primaire.
	 */
	@Id
	@GeneratedValue
	private Integer id;

	/**
	 * L'avenant.
	 */
	@ManyToOne
	@JoinColumn(name = "avenantId")
	private Avenant avenant;

	/**
	 * Numero de l'element contractuel.
	 */
	private Integer numEC;

	/**
	 * La valeur apres la modification.
	 */
	@Type(type = "text")
	private String trameJson;

	/**
	 * Constructeur.
	 */
	public Modification() {
		super();
	}

	/* Getter & Setter */

	/**
	 * Get id.
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Set id.
	 * 
	 * @param id
	 *            id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Getter l avenant.
	 * 
	 * @return avenant
	 */
	public Avenant getAvenant() {
		return avenant;
	}

	/**
	 * Setter l avenant.
	 * 
	 * @param avenant
	 *            avenant
	 */
	public void setAvenant(Avenant avenant) {
		this.avenant = avenant;
	}

	/**
	 * Gets the num ec.
	 * 
	 * @return the num ec
	 */
	public Integer getNumEC() {
		return numEC;
	}

	/**
	 * Sets the num ec.
	 * 
	 * @param numEC
	 *            the new num ec
	 */
	public void setNumEC(Integer numEC) {
		this.numEC = numEC;
	}

	/**
	 * Gets the trame json.
	 * 
	 * @return the trame json
	 */
	public String getTrameJson() {
		return trameJson;
	}

	/**
	 * Sets the trame json.
	 * 
	 * @param trameJson
	 *            the new trame json
	 */
	public void setTrameJson(String trameJson) {
		this.trameJson = trameJson;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Modification)) {
			return false;
		}
		Modification rhs = (Modification) obj;
		return new EqualsBuilder().append(numEC, rhs.numEC).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(43, 11).append(numEC).toHashCode();
	}
}
