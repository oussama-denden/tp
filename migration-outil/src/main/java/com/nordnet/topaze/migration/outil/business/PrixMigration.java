package com.nordnet.topaze.migration.outil.business;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nordnet.topaze.migration.outil.enums.ModeFacturation;
import com.nordnet.topaze.migration.outil.enums.ModeFacturationDeserializer;
import com.nordnet.topaze.migration.outil.enums.TypeTVA;
import com.nordnet.topaze.migration.outil.enums.TypeTVADeserializer;

/**
 * Cette classe regroupe les informations qui definissent un {@link PrixMigration}.
 * <ul>
 * <li>Un prix est montant qui peut evoluer suite a des decisions de NordNet.</li>
 * <li>Ces modifications ne doivent pas influencer les tarifs des sous-contrats, relies aux produits dont le prix
 * change!</li>
 * </ul>
 * 
 * @author anisselmane.
 * 
 */
public class PrixMigration {

	/**
	 * La période de facturation.
	 */
	private Integer periodicite;

	/**
	 * The type tva. {@link TypeTVA}.
	 */
	@JsonDeserialize(using = TypeTVADeserializer.class)
	private TypeTVA typeTVA;

	/**
	 * montant de prix.
	 */
	private Double montant;

	/**
	 * liste des frais.
	 */
	private Set<FraisMigrationSimulation> frais = new HashSet<>();

	/**
	 * The mode facturation. {@link ModeFacturation}.
	 */
	@JsonDeserialize(using = ModeFacturationDeserializer.class)
	private ModeFacturation modeFacturation;

	/**
	 * engagement par exmple 24 mois.
	 */
	private Integer engagement;

	/**
	 * duree du sous contrat en mois.
	 */
	private Integer duree;

	/**
	 * constructeur par defaut.
	 */
	public PrixMigration() {

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
		if (!(obj instanceof PrixMigration)) {
			return false;
		}
		PrixMigration rhs = (PrixMigration) obj;
		return new EqualsBuilder().append(montant, rhs.montant).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(43, 11).append(montant).toHashCode();
	}

	/* Getters & Setters */

	/**
	 * Gets the mode facturation.
	 * 
	 * @return {@link #modeFacturation}.
	 */
	public ModeFacturation getModeFacturation() {
		return modeFacturation;
	}

	/**
	 * Sets the mode facturation.
	 * 
	 * @param modeFacturation
	 *            the new mode facturation {@link #modeFacturation}.
	 */
	public void setModeFacturation(ModeFacturation modeFacturation) {
		this.modeFacturation = modeFacturation;
	}

	/**
	 * Gets the montant.
	 * 
	 * @return {@link #montant}.
	 */
	public Double getMontant() {
		return montant;
	}

	/**
	 * Sets the montant.
	 * 
	 * @param montant
	 *            the new montant {@link #montant}.
	 */
	public void setMontant(Double montant) {
		this.montant = montant;
	}

	/**
	 * Gets the periodicite.
	 * 
	 * @return {@link #periodicite}.
	 */
	public Integer getPeriodicite() {
		return periodicite;
	}

	/**
	 * Sets the periodicite.
	 * 
	 * @param periodicite
	 *            the new periodicite {@link #periodicite}.
	 */
	public void setPeriodicite(Integer periodicite) {
		this.periodicite = periodicite;
	}

	/**
	 * Gets the type tva.
	 * 
	 * @return {@link #typeTVA}.
	 */
	public TypeTVA getTypeTVA() {
		return typeTVA;
	}

	/**
	 * Sets the type tva.
	 * 
	 * @param typeTVA
	 *            the new type tva {@link #typeTVA}.
	 */
	public void setTypeTVA(TypeTVA typeTVA) {
		this.typeTVA = typeTVA;
	}

	/**
	 * Gets the frais.
	 * 
	 * @return the frais
	 */
	public Set<FraisMigrationSimulation> getFrais() {
		return frais;
	}

	/**
	 * Sets the frais.
	 * 
	 * @param frais
	 *            the new frais
	 */
	public void setFrais(Set<FraisMigrationSimulation> frais) {
		this.frais = frais;
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
	 * 
	 * @return {@link PrixMigration#duree}.
	 */
	public Integer getDuree() {
		return duree;
	}

	/**
	 * 
	 * @param duree
	 *            {@link PrixMigration#duree}.
	 */
	public void setDuree(Integer duree) {
		this.duree = duree;
	}

}
