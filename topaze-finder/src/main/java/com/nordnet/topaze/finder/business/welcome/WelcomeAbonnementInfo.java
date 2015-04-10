package com.nordnet.topaze.finder.business.welcome;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nordnet.topaze.finder.util.DateSerializer;

/**
 * Les informations des abonnements en cours.
 * 
 * @author Oussama Denden
 * 
 */
public class WelcomeAbonnementInfo {

	/**
	 * Label de l'EC Parent.
	 */
	private String label;

	/**
	 * reference du produit de l'EC.
	 */
	private String referenceProduit;

	/**
	 * Reference du contrat.
	 */
	private String reference;

	/**
	 * Date creation contrat.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateCreation;

	/**
	 * Date debut facturation.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateDebutFacturation;

	/**
	 * Date fin contrat.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateFin;

	/**
	 * Duree de contrat.
	 */
	private Integer duree;

	/**
	 * Date de Resiliation en Cours.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateActionResiliation;

	/**
	 * Indique si on doit calculer le remboursent lors de resiliation.
	 */
	private boolean remboursementResiliation;

	/**
	 * Date d'annulation de la demande de resiliation.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateAnnulationResiliation;

	/**
	 * Date de migration.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateActionMigration;

	/**
	 * Constructeur par defaut.
	 */
	public WelcomeAbonnementInfo() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WelcomeAbonnementInfo [label=" + label + ", referenceProduit=" + referenceProduit + ", reference="
				+ reference + ", dateCreation=" + dateCreation + ", dateDebutFacturation=" + dateDebutFacturation
				+ ", dateFin=" + dateFin + ", duree=" + duree + ", dateActionResiliation=" + dateActionResiliation
				+ ", remboursementResiliation=" + remboursementResiliation + ", dateAnnulationResiliation="
				+ dateAnnulationResiliation + ", dateActionMigration=" + dateActionMigration + "]";
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
		if (!(obj instanceof WelcomeAbonnementInfo)) {
			return false;
		}

		WelcomeAbonnementInfo rhs = (WelcomeAbonnementInfo) obj;
		return new EqualsBuilder().append(label, rhs.label).append(reference, rhs.reference).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(43, 11).append(label).append(reference).toHashCode();
	}

	/**
	 * @return {@link #label}.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            {@link #label}.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return {@link #referenceProduit}.
	 */
	public String getReferenceProduit() {
		return referenceProduit;
	}

	/**
	 * @param referenceProduit
	 *            {@link #referenceProduit}.
	 */
	public void setReferenceProduit(String referenceProduit) {
		this.referenceProduit = referenceProduit;
	}

	/**
	 * @return {@link #reference}.
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference
	 *            {@link #reference}.
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return {@link #dateCreation}.
	 */
	public Date getDateCreation() {
		return dateCreation;
	}

	/**
	 * @param dateCreation
	 *            {@link #dateCreation}.
	 */
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	/**
	 * @return {@link #dateDebutFacturation}.
	 */
	public Date getDateDebutFacturation() {
		return dateDebutFacturation;
	}

	/**
	 * @param dateDebutFacturation
	 *            {@link #dateDebutFacturation}.
	 */
	public void setDateDebutFacturation(Date dateDebutFacturation) {
		this.dateDebutFacturation = dateDebutFacturation;
	}

	/**
	 * @return {@link #dateFin}.
	 */
	public Date getDateFin() {
		return dateFin;
	}

	/**
	 * @param dateFin
	 *            {@link #dateFin}.
	 */
	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	/**
	 * @return {@link #duree}.
	 */
	public Integer getDuree() {
		return duree;
	}

	/**
	 * @param duree
	 *            {@link #duree}.
	 */
	public void setDuree(Integer duree) {
		this.duree = duree;
	}

	/**
	 * @return {@link #dateActionResiliation}.
	 */
	public Date getDateActionResiliation() {
		return dateActionResiliation;
	}

	/**
	 * @param dateActionResiliation
	 *            {@link #dateActionResiliation}.
	 */
	public void setDateActionResiliation(Date dateActionResiliation) {
		this.dateActionResiliation = dateActionResiliation;
	}

	/**
	 * @return {@link #remboursementResiliation}.
	 */
	public boolean isRemboursementResiliation() {
		return remboursementResiliation;
	}

	/**
	 * @param remboursementResiliation
	 *            {@link #remboursementResiliation}.
	 */
	public void setRemboursementResiliation(boolean remboursementResiliation) {
		this.remboursementResiliation = remboursementResiliation;
	}

	/**
	 * @return {@link #dateAnnulationResiliation}.
	 */
	public Date getDateAnnulationResiliation() {
		return dateAnnulationResiliation;
	}

	/**
	 * @param dateAnnulationResiliation
	 *            {@link #dateAnnulationResiliation}.
	 */
	public void setDateAnnulationResiliation(Date dateAnnulationResiliation) {
		this.dateAnnulationResiliation = dateAnnulationResiliation;
	}

	/**
	 * @return {@link #dateActionMigration}.
	 */
	public Date getDateActionMigration() {
		return dateActionMigration;
	}

	/**
	 * @param dateActionMigration
	 *            {@link #dateActionMigration}.
	 */
	public void setDateActionMigration(Date dateActionMigration) {
		this.dateActionMigration = dateActionMigration;
	}

}