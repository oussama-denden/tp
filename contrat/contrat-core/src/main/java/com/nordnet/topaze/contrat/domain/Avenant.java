package com.nordnet.topaze.contrat.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Optional;

/**
 * Cette classe regroupe les informations qui definissent un {@link Avenant}.
 * 
 * @author anisselmane.
 * 
 */
@Entity
@Table(name = "avenant", uniqueConstraints = @UniqueConstraint(columnNames = { "referenceContrat", "version" }))
@JsonIgnoreProperties({ "id", "referenceContrat", "version", "reference" })
public class Avenant {

	/**
	 * primery key.
	 */
	@Id
	@GeneratedValue
	private Integer id;

	/**
	 * reference {@link Contrat}.
	 */
	private String referenceContrat;

	/**
	 * version du contrat associe à l'avenant.
	 */
	private Integer version;

	/**
	 * Date de modification.
	 */
	private Date dateModification;

	/**
	 * Les modifications.
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "avenant", fetch = FetchType.EAGER)
	private List<Modification> modifications;

	/**
	 * Le commentaire.
	 */

	@Column(columnDefinition = "TINYTEXT")
	private String commentaire;
	/**
	 * Le commentaire d'annulation.
	 */
	@Column(columnDefinition = "TINYTEXT")
	private String commentaireAnnulation;

	/**
	 * {@link TypeAvenant}.
	 */
	@Enumerated(EnumType.STRING)
	@NotNull
	private TypeAvenant typeAvenant;

	/**
	 * {@link PolitiqueMigration}.
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "politiqueMigrationId", nullable = true)
	private PolitiqueMigration politiqueMigration;

	/**
	 * {@link PolitiqueCession}.
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "politiqueCessionId", nullable = true)
	private PolitiqueCession politiqueCession;

	/**
	 * {@link PolitiqueCession}.
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "politiqueRenouvellementId", nullable = true)
	private PolitiqueRenouvellement politiqueRenouvellement;

	/**
	 * la date d'annulation
	 */
	@Temporal(TemporalType.DATE)
	private Date dateAnnulation;

	/**
	 * constructeur par defaut.
	 */
	public Avenant() {

	}

	/* Getters & Setters */

	/**
	 * 
	 * @return {@link #id}.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            {@link #id}.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return {@link #referenceContrat}.
	 */
	public String getReferenceContrat() {
		return referenceContrat;
	}

	/**
	 * 
	 * @param referenceContrat
	 *            {@link #referenceContrat}.
	 */
	public void setReferenceContrat(String referenceContrat) {
		this.referenceContrat = referenceContrat;
	}

	/**
	 * 
	 * @return {@link #version}.
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * 
	 * @param version
	 *            {@link #version}.
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * Getter la date modification.
	 * 
	 * @return the date modification
	 */
	public Date getDateModification() {
		return dateModification;
	}

	/**
	 * Setter la date modification.
	 * 
	 * @param dateModification
	 *            date modification
	 */
	public void setDateModification(Date dateModification) {
		this.dateModification = dateModification;
	}

	/**
	 * Getter la liste des modification.
	 * 
	 * @return date modification
	 * 
	 */
	public List<Modification> getModifContrat() {
		return modifications;
	}

	/**
	 * Setter la liste des modification.
	 * 
	 * @param modifications
	 *            modifications
	 */
	public void setModifContrat(List<Modification> modifications) {
		this.modifications = modifications;
	}

	/**
	 * Getter le commentaire.
	 * 
	 * @return commentaire
	 */
	public String getCommentaire() {
		return commentaire;
	}

	/**
	 * Setter le commentaire.
	 * 
	 * @param commentaire
	 *            le commentaire
	 */
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	/**
	 * Getter le commentaire annulation.
	 * 
	 * @return commentaire annulation
	 */
	public String getCommentaireAnnulation() {
		return commentaireAnnulation;
	}

	/**
	 * Setter le commentaire annulation.
	 * 
	 * @param commentaireAnnulation
	 *            le commentaire annulation
	 */
	public void setCommentaireAnnulation(String commentaireAnnulation) {
		this.commentaireAnnulation = commentaireAnnulation;
	}

	/**
	 * 
	 * @return {@link TypeAvenant}.
	 */
	public TypeAvenant getTypeAvenant() {
		return typeAvenant;
	}

	/**
	 * 
	 * @param typeAvenant
	 *            {@link TypeAvenant}.
	 */
	public void setTypeAvenant(TypeAvenant typeAvenant) {
		this.typeAvenant = typeAvenant;
	}

	/**
	 * 
	 * @return {@link #politiqueMigration}.
	 */
	public PolitiqueMigration getPolitiqueMigration() {
		return politiqueMigration;
	}

	/**
	 * 
	 * @param politiqueMigration
	 *            {@link #politiqueMigration}.
	 */
	public void setPolitiqueMigration(PolitiqueMigration politiqueMigration) {
		this.politiqueMigration = politiqueMigration;
	}

	/**
	 * 
	 * @return {@link #politiqueCession}.
	 */
	public PolitiqueCession getPolitiqueCession() {
		return politiqueCession;
	}

	/**
	 * 
	 * @param politiqueCession
	 *            {@link #politiqueCession}.
	 */
	public void setPolitiqueCession(PolitiqueCession politiqueCession) {
		this.politiqueCession = politiqueCession;
	}

	/**
	 * get the politique de renouvellement
	 * 
	 * @return {@linkc #politiqueRenouvellement}
	 */
	public PolitiqueRenouvellement getPolitiqueRenouvellement() {
		return politiqueRenouvellement;
	}

	/**
	 * the new politique de renouvellement.
	 * 
	 * @param politiqueRenouvellement
	 *            the new {@link #politiqueRenouvellement}
	 */
	public void setPolitiqueRenouvellement(PolitiqueRenouvellement politiqueRenouvellement) {
		this.politiqueRenouvellement = politiqueRenouvellement;
	}

	/**
	 * 
	 * @return {@link #dateAnnulation}
	 */
	public Date getDateAnnulation() {
		return dateAnnulation;
	}

	/**
	 * 
	 * @param dateAnnulation
	 *            {@link #dateAnnulation}
	 */
	public void setDateAnnulation(Date dateAnnulation) {
		this.dateAnnulation = dateAnnulation;
	}

	public boolean isDemandeAnnule() {
		Optional<Date> dateAnnulationOp = Optional.fromNullable(dateAnnulation);
		return dateAnnulationOp.isPresent();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Avenant)) {
			return false;
		}
		// HistoriqueContrat rhs = (HistoriqueContrat) obj;
		// return new EqualsBuilder().append(id, rhs.id).append(titre, rhs.titre)
		// .append(referenceProduit, rhs.referenceProduit).isEquals();
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
		// return new HashCodeBuilder(43, 11).append(id).append(titre).append(referenceProduit).toHashCode();
	}

}
