package com.nordnet.topaze.contrat.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nordnet.topaze.contrat.util.MotifResiliationDeserializer;
import com.nordnet.topaze.contrat.util.TypeResiliationDeserializer;

/**
 * contient tout les informations necessaire pour la politique de resiliation.
 */
@Entity
@Table(name = "politiqueresiliation")
@JsonIgnoreProperties({ "id" })
public class PolitiqueResiliation {

	/**
	 * cle primaire.
	 */
	@Id
	@GeneratedValue
	private Integer id;

	/**
	 * Flag remboursement.
	 */
	private boolean remboursement;

	/**
	 * frais de resiliation.
	 */
	private boolean fraisResiliation;

	/**
	 * Penalite de resiliation.
	 */
	private boolean penalite;

	/**
	 * Le montant defini pour le remboursement.
	 */
	private Double montantRemboursement;

	/**
	 * Le montant de frais resiliation.
	 */
	private Double montantResiliation;

	/**
	 * Type de résiliation RIN ou RIC.
	 */
	@JsonDeserialize(using = TypeResiliationDeserializer.class)
	@Enumerated(EnumType.STRING)
	private TypeResiliation typeResiliation;

	/**
	 * Boolean résiliation en cascade.
	 */
	private boolean enCascade;

	/**
	 * La Date de résiliation.
	 */
	private Date dateResiliation;

	/**
	 * la date d'annulation.
	 */
	@Temporal(TemporalType.DATE)
	private Date dateAnnulation;

	/**
	 * le commentaire d'annulation.
	 */
	@Column(columnDefinition = "TINYTEXT")
	private String commentaireAnnulation;

	/**
	 * commentaire de resiliation.
	 */
	@Column(columnDefinition = "TINYTEXT")
	private String commentaire;

	/**
	 * motif de resiliation.
	 */
	@JsonDeserialize(using = MotifResiliationDeserializer.class)
	@Enumerated(EnumType.STRING)
	private MotifResiliation motif;

	/**
	 * La Date de remboursement.
	 */
	@Temporal(TemporalType.DATE)
	private Date dateRemboursement;

	/**
	 * delai de securite.
	 */
	private Boolean delaiDeSecurite;

	/**
	 * Instantiates a new politique resiliation.
	 */
	public PolitiqueResiliation() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PolitiqueResiliation [id=" + id + ", remboursement=" + remboursement + ", fraisResiliation="
				+ fraisResiliation + ", penalite=" + penalite + ", montantRemboursement=" + montantRemboursement
				+ ", montantResiliation=" + montantResiliation + ", typeResiliation=" + typeResiliation
				+ ", enCascade=" + enCascade + ", commentaire=" + commentaire + "]";
	}

	/* Getters & Setters */
	/**
	 * Checks if is remboursement.
	 * 
	 * @return true, if is remboursement
	 */
	public boolean isRemboursement() {
		return remboursement;
	}

	/**
	 * Checks if is frais resiliation.
	 * 
	 * @return true, if is frais resiliation
	 */
	public boolean isFraisResiliation() {
		return fraisResiliation;
	}

	/**
	 * Sets the frais resiliation.
	 * 
	 * @param fraisResiliation
	 *            the new frais resiliation
	 */
	public void setFraisResiliation(boolean fraisResiliation) {
		this.fraisResiliation = fraisResiliation;
	}

	/**
	 * Checks if is penalite.
	 * 
	 * @return true, if is penalite
	 */
	public boolean isPenalite() {
		return penalite;
	}

	/**
	 * Sets the penalite.
	 * 
	 * @param penalite
	 *            the new penalite
	 */
	public void setPenalite(boolean penalite) {
		this.penalite = penalite;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Sets the remboursement.
	 * 
	 * @param remboursement
	 *            the new remboursement
	 */
	public void setRemboursement(boolean remboursement) {
		this.remboursement = remboursement;
	}

	/**
	 * @return {@link #montantResiliation}.
	 */
	public Double getMontantResiliation() {
		return montantResiliation;
	}

	/**
	 * 
	 * @return {@link # montantRemboursement}.
	 */
	public Double getMontantRemboursement() {
		return montantRemboursement;
	}

	/**
	 * 
	 * @param montantRemboursement
	 *            {@link # montantRemboursement}.
	 */
	public void setMontantRemboursement(Double montantRemboursement) {
		this.montantRemboursement = montantRemboursement;
	}

	/**
	 * @param montantResiliation
	 *            {@link #montantResiliation}.
	 */
	public void setMontantResiliation(Double montantResiliation) {
		this.montantResiliation = montantResiliation;
	}

	/**
	 * Gets the type resiliation.
	 * 
	 * @return the type resiliation
	 */
	public TypeResiliation getTypeResiliation() {
		return typeResiliation;
	}

	/**
	 * Sets the type resiliation.
	 * 
	 * @param typeResiliation
	 *            the new type resiliation
	 */
	public void setTypeResiliation(TypeResiliation typeResiliation) {
		this.typeResiliation = typeResiliation;
	}

	/**
	 * Checks if is en cascade.
	 * 
	 * @return true, if is en cascade
	 */
	public boolean isEnCascade() {
		return enCascade;
	}

	/**
	 * Sets the en cascade.
	 * 
	 * @param enCascade
	 *            the new en cascade
	 */
	public void setEnCascade(boolean enCascade) {
		this.enCascade = enCascade;
	}

	/**
	 * Gets the date resiliation.
	 * 
	 * @return the date resiliation
	 */
	public Date getDateResiliation() {
		return dateResiliation;
	}

	/**
	 * Sets the date resiliation.
	 * 
	 * @param dateResiliation
	 *            the new date resiliation
	 */
	public void setDateResiliation(Date dateResiliation) {
		this.dateResiliation = dateResiliation;
	}

	/**
	 * 
	 * @return {@link #commentaire}
	 */
	public String getCommentaire() {
		return commentaire;
	}

	/**
	 * 
	 * @param commentaire
	 *            {@link #commentaire}
	 */
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
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

	/**
	 * 
	 * @return {@link #commentaireAnnulation}
	 */
	public String getCommentaireAnnulation() {
		return commentaireAnnulation;
	}

	/**
	 * 
	 * @param commentaireAnnulation
	 *            {@link #commentaireAnnutlaion}
	 */
	public void setCommentaireAnnulation(String commentaireAnnulation) {
		this.commentaireAnnulation = commentaireAnnulation;
	}

	/**
	 * get the motif .
	 * 
	 * @return {@link MotifResiliation}
	 */
	public MotifResiliation getMotif() {
		return motif;
	}

	/**
	 * set the motif.
	 * 
	 * @param motif
	 *            the new {@link MotifResiliation}
	 */
	public void setMotif(MotifResiliation motif) {
		this.motif = motif;
	}

	/**
	 * 
	 * @return {@link #dateRemboursement}
	 */
	public Date getDateRemboursement() {
		return dateRemboursement;
	}

	/**
	 * 
	 * @param dateRemboursement
	 *            {@link #dateRemboursement}
	 */
	public void setDateRemboursement(Date dateRemboursement) {
		this.dateRemboursement = dateRemboursement;
	}

	/**
	 * get delai de securite.
	 * 
	 * @return {@link #delaiDeSecurite}
	 */
	public Boolean getDelaiDeSecurite() {
		return delaiDeSecurite;
	}

	/**
	 * set delai de securite.
	 * 
	 * @param delaiDeSecurite
	 */
	public void setDelaiDeSecurite(Boolean delaiDeSecurite) {
		this.delaiDeSecurite = delaiDeSecurite;
	}

	/**
	 * creer une copie avec id = null.
	 * 
	 * @return {@link PolitiqueResiliation}.
	 */
	public PolitiqueResiliation copy() {
		PolitiqueResiliation politiqueResiliation = new PolitiqueResiliation();
		politiqueResiliation.setRemboursement(remboursement);
		politiqueResiliation.setFraisResiliation(fraisResiliation);
		politiqueResiliation.setPenalite(penalite);
		politiqueResiliation.setMontantRemboursement(montantRemboursement);
		politiqueResiliation.setMontantResiliation(montantResiliation);
		politiqueResiliation.setTypeResiliation(typeResiliation);
		politiqueResiliation.setEnCascade(enCascade);
		politiqueResiliation.setDateResiliation(dateResiliation);
		politiqueResiliation.setCommentaire(commentaire);
		return politiqueResiliation;
	}

	/**
	 * Mapping politique resiliation domain to business.
	 * 
	 * @return the politique resiliation
	 */
	public com.nordnet.topaze.contrat.business.PolitiqueResiliationFacture toBusiness() {
		com.nordnet.topaze.contrat.business.PolitiqueResiliationFacture politiqueResiliationBus =
				new com.nordnet.topaze.contrat.business.PolitiqueResiliationFacture();

		politiqueResiliationBus.setFraisResiliation(fraisResiliation);
		politiqueResiliationBus.setMontantRemboursement(montantRemboursement);
		politiqueResiliationBus.setMontantResiliation(montantResiliation);
		politiqueResiliationBus.setPenalite(penalite);
		politiqueResiliationBus.setRemboursement(remboursement);
		politiqueResiliationBus.setEnCascade(enCascade);
		politiqueResiliationBus.setDateRemboursement(dateRemboursement);
		return politiqueResiliationBus;
	}

}
