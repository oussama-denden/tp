package com.nordnet.topaze.contrat.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.TypeResiliationDeserializer;

/**
 * Entité copie de l'entité {@link Contrat}, pour garder l'istorique des migration d'un contrat.
 * 
 * @author akram-moncer
 * 
 */
@Entity
@Table(name = "contrathistorique")
@JsonIgnoreProperties({ "id", "avenant", "minPeriodicite" })
public class ContratHistorique {

	/**
	 * cle primaire.
	 */
	@Id
	@GeneratedValue
	private Integer id;

	/**
	 * reference du contrat.
	 */
	@NotNull
	private String reference;

	/**
	 * id de client.
	 */
	private String idClient;

	/**
	 * segment TVA du client.
	 */
	private String segmentTVA;

	/**
	 * titre(titre de contrat par exemple: Contrat d'abonnement pour le produit Kit satellite 2Giga).
	 */
	@NotNull
	private String titre;

	/**
	 * liste des sous contrats.
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "contratParent", fetch = FetchType.EAGER)
	private Set<ElementContractuelHistorique> sousContrats = new HashSet<>();

	/**
	 * date de preparation du contrat.
	 */
	private Date datePreparation;

	/**
	 * date de validation du contrat.
	 */
	private Date dateValidation;

	/**
	 * date qui indique la fin de livraison et la debut de facturation.
	 */
	private Date dateDebutFacturation;

	/**
	 * date du derniere facture.
	 */
	private Date dateDerniereFacture;

	/**
	 * date du facture resiliation.
	 */
	private Date dateFactureResiliation;

	/**
	 * date de fin du sous contrat.
	 */
	private Date dateFinContrat;

	/**
	 * Le type resiliation. {@link TypeResiliation}.
	 */
	@JsonDeserialize(using = TypeResiliationDeserializer.class)
	@Enumerated(EnumType.STRING)
	private TypeResiliation typeResiliation;

	/**
	 * Politique création.
	 */
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "politiqueValidationId", nullable = true)
	private PolitiqueValidation politiqueValidation;

	/**
	 * Politique résiliation.
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "politiqueResiliationId", nullable = true)
	private PolitiqueResiliation politiqueResiliation;

	/**
	 * engagement maximal de contrat.
	 */
	@Transient
	private Integer engagementMax;

	/**
	 * version du contrat.
	 */
	private Integer version;

	/**
	 * constructeur par defaut.
	 */
	public ContratHistorique() {

	}

	/**
	 * Determiner la statut d'un contrat.
	 * 
	 * @return {@link StatutContrat}.
	 */
	public StatutContrat getStatut() {

		if (typeResiliation != null) {
			return StatutContrat.RESILIER;
		}

		if (dateValidation != null) {
			return StatutContrat.VALIDER;
		}

		if (datePreparation != null) {
			return StatutContrat.PREPARER;
		}

		return null;
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
		if (!(obj instanceof ContratHistorique)) {
			return false;
		}
		ContratHistorique rhs = (ContratHistorique) obj;
		return new EqualsBuilder().append(id, rhs.id).append(reference, rhs.reference).append(idClient, rhs.idClient)
				.append(titre, rhs.titre).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(43, 11).append(id).append(reference).append(idClient).append(titre).toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Contrat [id=" + id + ", reference=" + reference + ", idClient=" + idClient + ", titre=" + titre
				+ ", sousContrats=" + sousContrats + ", datePreparation=" + datePreparation + ", dateValidation="
				+ dateValidation + ", dateDebutFacturation=" + dateDebutFacturation + ", dateDerniereFacture="
				+ dateDerniereFacture + ", dateFinContrat=" + dateFinContrat + ", typeResiliation=" + typeResiliation
				+ "]";
	}

	/**
	 * Testet si un BP est Resilier.
	 * 
	 * @return true si BP est Resilier.
	 */
	public boolean isResilier() {
		return getStatut().equals(StatutContrat.RESILIER);
	}

	/**
	 * Testet si un BP est Valider.
	 * 
	 * @return true si BP est Valider.
	 */
	public boolean isValider() {
		return getStatut().equals(StatutContrat.VALIDER);
	}

	/**
	 * Testet si un BP est Preparer.
	 * 
	 * @return true si BP est Preparer.
	 */
	public boolean isPreparer() {
		return getStatut().equals(StatutContrat.PREPARER);
	}

	/* Getters & Setters */

	/**
	 * Gets the id.
	 * 
	 * @return {@link #id}.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id {@link #id}.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the reference.
	 * 
	 * @return {@link #reference}.
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * Sets the reference.
	 * 
	 * @param reference
	 *            the new reference {@link #reference}.
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * Gets the id client.
	 * 
	 * @return {@link #idClient}.
	 */
	public String getIdClient() {
		return idClient;
	}

	/**
	 * Sets the id client.
	 * 
	 * @param idClient
	 *            the new id client {@link #idClient}.
	 */
	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}

	/**
	 * 
	 * @return {@link #segmentTVA}.
	 */
	public String getSegmentTVA() {
		return segmentTVA;
	}

	/**
	 * 
	 * @param segmentTVA
	 *            {@link #segmentTVA}.
	 */
	public void setSegmentTVA(String segmentTVA) {
		this.segmentTVA = segmentTVA;
	}

	/**
	 * Gets the titre.
	 * 
	 * @return {@link #titre}.
	 */
	public String getTitre() {
		return titre;
	}

	/**
	 * Sets the titre.
	 * 
	 * @param titre
	 *            the new titre {@link #titre}.
	 */
	public void setTitre(String titre) {
		this.titre = titre;
	}

	/**
	 * Gets the sous contrats.
	 * 
	 * @return {@link #sousContrats}.
	 */
	public Set<ElementContractuelHistorique> getSousContrats() {
		return sousContrats;
	}

	/**
	 * Sets the sous contrats.
	 * 
	 * @param sousContrats
	 *            the new sous contrats {@link #sousContrats}.
	 */
	public void setSousContrats(Set<ElementContractuelHistorique> sousContrats) {
		this.sousContrats = sousContrats;
	}

	/**
	 * Gets the date preparation.
	 * 
	 * @return {@link datePreparation}.
	 */
	public Date getDatePreparation() {
		return datePreparation;
	}

	/**
	 * Sets the date preparation.
	 * 
	 * @param datePreparation
	 *            the new date preparation {@link datePreparation}.
	 */
	public void setDatePreparation(Date datePreparation) {
		this.datePreparation = datePreparation;
	}

	/**
	 * Gets the date validation.
	 * 
	 * @return {@link #dateValidation}.
	 */
	public Date getDateValidation() {
		return dateValidation;
	}

	/**
	 * Sets the date validation.
	 * 
	 * @param dateValidation
	 *            the new date validation {@link #dateValidation}.
	 */
	public void setDateValidation(Date dateValidation) {
		this.dateValidation = dateValidation;
	}

	/**
	 * Gets the date debut facturation.
	 * 
	 * @return {@link #dateDebutFacturation}.
	 */
	public Date getDateDebutFacturation() {
		return dateDebutFacturation;
	}

	/**
	 * Sets the date debut facturation.
	 * 
	 * @param dateDebutFacturation
	 *            the new date debut facturation {@link #dateDebutFacturation}.
	 */
	public void setDateDebutFacturation(Date dateDebutFacturation) {
		this.dateDebutFacturation = dateDebutFacturation;
	}

	/**
	 * Gets the date derniere facture.
	 * 
	 * @return {@link #dateDerniereFacture}.
	 */
	public Date getDateDerniereFacture() {
		return dateDerniereFacture;
	}

	/**
	 * Sets the date derniere facture.
	 * 
	 * @param dateDerniereFacture
	 *            the new date derniere facture {@link #dateDerniereFacture}.
	 */
	public void setDateDerniereFacture(Date dateDerniereFacture) {
		this.dateDerniereFacture = dateDerniereFacture;
	}

	/**
	 * 
	 * @return {@link #dateFactureResiliation}
	 */
	public Date getDateFactureResiliation() {
		return dateFactureResiliation;
	}

	/**
	 * 
	 * @param dateFactureResiliation
	 *            {@link #dateFactureResiliation}
	 */
	public void setDateFactureResiliation(Date dateFactureResiliation) {
		this.dateFactureResiliation = dateFactureResiliation;
	}

	/**
	 * Gets the date fin contrat.
	 * 
	 * @return {@link #dateFinContrat}.
	 */
	public Date getDateFinContrat() {
		return dateFinContrat;
	}

	/**
	 * Sets the date fin contrat.
	 * 
	 * @param dateFinContrat
	 *            the new date fin contrat {@link #dateFinContrat}.
	 */
	public void setDateFinContrat(Date dateFinContrat) {
		this.dateFinContrat = dateFinContrat;
	}

	/**
	 * Gets the type resiliation.
	 * 
	 * @return {@link #typeResiliation}.
	 */
	public TypeResiliation getTypeResiliation() {
		return typeResiliation;
	}

	/**
	 * Sets the type resiliation.
	 * 
	 * @param typeResiliation
	 *            the new type resiliation {@link #typeResiliation}.
	 */
	public void setTypeResiliation(TypeResiliation typeResiliation) {
		this.typeResiliation = typeResiliation;
	}

	/**
	 * Gets the politique validation.
	 * 
	 * @return the politique validation
	 */
	public PolitiqueValidation getPolitiqueValidation() {
		return politiqueValidation;
	}

	/**
	 * Sets the politique validation.
	 * 
	 * @param politiqueValidation
	 *            the new politique validation
	 */
	public void setPolitiqueValidation(PolitiqueValidation politiqueValidation) {
		this.politiqueValidation = politiqueValidation;
	}

	/**
	 * Gets politique resiliation.
	 * 
	 * @return the politique resiliation
	 */
	public PolitiqueResiliation getPolitiqueResiliation() {
		return politiqueResiliation;
	}

	/**
	 * Sets politique resiliation.
	 * 
	 * @param politiqueResiliation
	 *            the new politique resiliation
	 */
	public void setPolitiqueResiliation(PolitiqueResiliation politiqueResiliation) {
		this.politiqueResiliation = politiqueResiliation;
	}

	/**
	 * @return le maximum d'engagement des sous contrat.
	 */
	public Integer getEngagementMax() {
		engagementMax = 0;
		for (ElementContractuelHistorique elementContractuel : sousContrats) {
			if (elementContractuel.getEngagement() != null && elementContractuel.getEngagement() > engagementMax) {
				engagementMax = elementContractuel.getEngagement();
			}
		}
		return engagementMax;
	}

	/**
	 * @return {@link #version}.
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            {@link #version}.
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * @return le minimum des periodicites.
	 */
	public Integer getMinPeriodicite() {
		List<Integer> periodicites = new ArrayList<>();
		for (ElementContractuelHistorique elementContractuel : sousContrats) {
			if (elementContractuel.getPeriodicite() != null) {
				periodicites.add(elementContractuel.getPeriodicite());
			}
		}
		if (periodicites.size() > Constants.ZERO) {
			Collections.sort(periodicites);
			return periodicites.get(Constants.ZERO);
		}
		return null;
	}

	public Contrat toContrat() {
		Contrat contrat = new Contrat();
		contrat.setDateDebutFacturation(dateDebutFacturation);
		contrat.setDateDerniereFacture(dateDerniereFacture);
		contrat.setDateFinContrat(dateFinContrat);
		contrat.setDatePreparation(datePreparation);
		contrat.setDateValidation(dateValidation);
		contrat.setIdClient(idClient);
		contrat.setPolitiqueResiliation(politiqueResiliation);
		contrat.setPolitiqueValidation(politiqueValidation);
		contrat.setReference(reference);
		contrat.setTitre(titre);
		contrat.setTypeResiliation(typeResiliation);
		Set<ElementContractuel> elementContractuels = new HashSet<>();

		for (ElementContractuelHistorique elementContractuelHistorique : sousContrats) {
			ElementContractuel elementContractuel = elementContractuelHistorique.toElementContractuel();
			elementContractuel.setContratParent(contrat);
			elementContractuels.add(elementContractuel);
		}

		contrat.setSousContrats(elementContractuels);
		return contrat;
	}
}
