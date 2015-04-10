package com.nordnet.topaze.finder.business.welcome;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nordnet.topaze.finder.enums.ModePaiement;
import com.nordnet.topaze.finder.enums.TypeTVA;
import com.nordnet.topaze.finder.util.DateSerializer;

/**
 * Vue detaillee du contrat pour Welcome.
 * 
 * @author Oussama Denden
 * 
 */
public class WelcomeContratInfo {

	/**
	 * Reference du contrat.
	 */
	private String numAbonnement;

	/**
	 * Le numéro de commande de l'EC Parent.
	 */
	private String numeroCommande;

	/**
	 * Reference de l'EC Parent.
	 */
	private String offre;

	/**
	 * Label de l'EC.
	 */
	private String label;

	/**
	 * id client.
	 */
	private String idClient;

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
	 * C'est la date de modification dans l'avenant.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateRenouvellement;

	/**
	 * periodicite.
	 */
	private Integer periodicite;

	/**
	 * {@link ModePaiement}.
	 */
	private ModePaiement typePaiement;

	/**
	 * C'est la date de fin d'engagement du contrat.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateFinEngagement;

	/**
	 * true si resiliation anticipee.
	 */
	private boolean frais;

	/**
	 * tarif global sans reduction du contrat.
	 */
	private Double montantTarif;

	/**
	 * montant de reduction sur tarif global.
	 */
	private Double montantReduction;

	/**
	 * {@link TypeTVA}.
	 */
	private TypeTVA TVA;

	/**
	 * Les informations detaillées de tarification du contrat.
	 */
	private List<Tarif> tarifs = new ArrayList<>();

	/**
	 * Constructeur par defaut.
	 */
	public WelcomeContratInfo() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WelcomeContratInfo [numAbonnement=" + numAbonnement + ", numeroCommande=" + numeroCommande + ", offre="
				+ offre + ", label=" + label + ", idClient=" + idClient + ", dateCreation=" + dateCreation
				+ ", dateDebutFacturation=" + dateDebutFacturation + ", dateFin=" + dateFin + ", duree=" + duree
				+ ", dateActionResiliation=" + dateActionResiliation + ", remboursementResiliation="
				+ remboursementResiliation + ", dateAnnulationResiliation=" + dateAnnulationResiliation
				+ ", dateActionMigration=" + dateActionMigration + ", dateRenouvellement=" + dateRenouvellement
				+ ", periodicite=" + periodicite + ", typePaiement=" + typePaiement + ", dateFinEngagement="
				+ dateFinEngagement + ", frais=" + frais + ", montantTarif=" + montantTarif + ", montantReduction="
				+ montantReduction + ", TVA=" + TVA + ", tarifs=" + tarifs + "]";
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
		if (!(obj instanceof WelcomeContratInfo)) {
			return false;
		}

		WelcomeContratInfo rhs = (WelcomeContratInfo) obj;
		return new EqualsBuilder().append(numAbonnement, rhs.numAbonnement).append(numeroCommande, rhs.numeroCommande)
				.append(offre, rhs.offre).append(idClient, rhs.idClient).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(43, 11).append(numAbonnement).append(numeroCommande).append(offre).append(idClient)
				.toHashCode();
	}

	/**
	 * @return {@link #numAbonnement}.
	 */
	public String getNumAbonnement() {
		return numAbonnement;
	}

	/**
	 * @param numAbonnement
	 *            {@link #numAbonnement}.
	 */
	public void setNumAbonnement(String numAbonnement) {
		this.numAbonnement = numAbonnement;
	}

	/**
	 * @return {@link #numeroCommande}.
	 */
	public String getNumeroCommande() {
		return numeroCommande;
	}

	/**
	 * @param numeroCommande
	 *            {@link #numeroCommande}.
	 */
	public void setNumeroCommande(String numeroCommande) {
		this.numeroCommande = numeroCommande;
	}

	/**
	 * @return {@link #offre}.
	 */
	public String getOffre() {
		return offre;
	}

	/**
	 * @param offre
	 *            {@link #offre}.
	 */
	public void setOffre(String offre) {
		this.offre = offre;
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
	 * @return {@link #idClient}.
	 */
	public String getIdClient() {
		return idClient;
	}

	/**
	 * @param idClient
	 *            {@link #idClient}.
	 */
	public void setIdClient(String idClient) {
		this.idClient = idClient;
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

	/**
	 * @return {@link #dateRenouvellement}.
	 */
	public Date getDateRenouvellement() {
		return dateRenouvellement;
	}

	/**
	 * @param dateRenouvellement
	 *            {@link #dateRenouvellement}.
	 */
	public void setDateRenouvellement(Date dateRenouvellement) {
		this.dateRenouvellement = dateRenouvellement;
	}

	/**
	 * @return {@link #periodicite}.
	 */
	public Integer getPeriodicite() {
		return periodicite;
	}

	/**
	 * @param periodicite
	 *            {@link #periodicite}.
	 */
	public void setPeriodicite(Integer periodicite) {
		this.periodicite = periodicite;
	}

	/**
	 * @return {@link #typePaiement}.
	 */
	public ModePaiement getTypePaiement() {
		return typePaiement;
	}

	/**
	 * @param typePaiement
	 *            {@link #typePaiement}.
	 */
	public void setTypePaiement(ModePaiement typePaiement) {
		this.typePaiement = typePaiement;
	}

	/**
	 * @return {@link #dateFinEngagement}.
	 */
	public Date getDateFinEngagement() {
		return dateFinEngagement;
	}

	/**
	 * @param dateFinEngagement
	 *            {@link #dateFinEngagement}.
	 */
	public void setDateFinEngagement(Date dateFinEngagement) {
		this.dateFinEngagement = dateFinEngagement;
	}

	/**
	 * @return {@link #frais}.
	 */
	public boolean isFrais() {
		return frais;
	}

	/**
	 * @param frais
	 *            {@link #frais}.
	 */
	public void setFrais(boolean frais) {
		this.frais = frais;
	}

	/**
	 * @return {@link #montantTarif}.
	 */
	public Double getMontantTarif() {
		return montantTarif;
	}

	/**
	 * @param montantTarif
	 *            {@link #montantTarif}.
	 */
	public void setMontantTarif(Double montantTarif) {
		this.montantTarif = montantTarif;
	}

	/**
	 * @return {@link #montantReduction}.
	 */
	public Double getMontantReduction() {
		return montantReduction;
	}

	/**
	 * @param montantReduction
	 *            {@link #montantReduction}.
	 */
	public void setMontantReduction(Double montantReduction) {
		this.montantReduction = montantReduction;
	}

	/**
	 * @return {@link #TVA}.
	 */
	public TypeTVA getTVA() {
		return TVA;
	}

	/**
	 * @param TVA
	 *            {@link #TVA}.
	 */
	public void setTVA(TypeTVA TVA) {
		this.TVA = TVA;
	}

	/**
	 * @return {@link #tarifs}.
	 */
	public List<Tarif> getTarifs() {
		return tarifs;
	}

	/**
	 * @param tarifs
	 *            {@link #tarifs}.
	 */
	public void setTarifs(List<Tarif> tarifs) {
		this.tarifs = tarifs;
	}

}
