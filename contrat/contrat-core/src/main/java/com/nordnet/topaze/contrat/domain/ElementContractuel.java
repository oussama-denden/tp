package com.nordnet.topaze.contrat.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Index;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Optional;
import com.nordnet.topaze.client.rest.enums.TypeProduit;
import com.nordnet.topaze.contrat.util.DateSerializer;
import com.nordnet.topaze.contrat.util.ModeFacturationDeserializer;
import com.nordnet.topaze.contrat.util.ModePaiementDeserializer;
import com.nordnet.topaze.contrat.util.TypeProduitDeserializer;
import com.nordnet.topaze.contrat.util.TypeResiliationDeserializer;
import com.nordnet.topaze.contrat.util.TypeTVADeserializer;

/**
 * Cette classe regroupe les informations qui definissent un {@link ElementContractuel}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Entity
@Table(name = "elementcontractuel")
@JsonIgnoreProperties({ "id", "contratParent", "cloture", "elementContractuelParent" })
public class ElementContractuel {

	/**
	 * cle primaire.
	 */
	@Id
	@GeneratedValue
	@Index(columnNames = "id", name = "index_id")
	private Integer id;

	/**
	 * Le champ "BillingGroup", lorsqu'il doit être rempli, le sera avec le numéro de la commande.
	 */
	private String numeroCommande;

	/**
	 * contrat global qui englobe tous les sous contrats.
	 */
	@ManyToOne
	@JoinColumn(name = "contratParent")
	@Index(columnNames = "contratParent", name = "index_contratParent")
	private Contrat contratParent;

	/**
	 * adresse de facturation.
	 */
	private String idAdrFacturation;

	/**
	 * adresse de livraison.
	 */
	private String idAdrLivraison;

	/**
	 * referenceProduit(reference de produit auquel ce ContratABO sera associe).
	 */
	@Index(columnNames = "referenceProduit", name = "index_referenceProduit")
	private String referenceProduit;

	/**
	 * reference du tarif associe au Ec dans le catalogue.
	 */
	private String referenceTarif;

	/**
	 * le ProductId que sera utilisé dans saphir.
	 */
	@Transient
	private String idProduit;

	/**
	 * Numero de l'element contractuel.
	 */
	@Index(columnNames = "numEC", name = "index_numEC")
	private Integer numEC;

	/**
	 * le type de produit associe a l'element contractuel il peut etre BIEN/SERVICE.
	 */
	@JsonDeserialize(using = TypeProduitDeserializer.class)
	@Enumerated(EnumType.STRING)
	private TypeProduit typeProduit;

	/**
	 * titre(titre de contrat par exemple: Contrat d'abonnement pour le produit Kit satellite 2Giga).
	 */
	@NotNull
	private String titre;

	/**
	 * engagement par exmple 24 mois.
	 */
	private Integer engagement;

	/**
	 * engagement maximal de contrat.
	 */
	@Transient
	private Integer engagementMax = 0;

	/**
	 * duree du sous contrat en mois.
	 */
	private Integer duree;

	/**
	 * montant a payer.
	 */
	private Double montant;

	/** The type tva. {@link TypeTVA}. */
	@JsonDeserialize(using = TypeTVADeserializer.class)
	@Enumerated(EnumType.STRING)
	private TypeTVA typeTVA;

	/**
	 * La période de facturation.
	 */
	private Integer periodicite;

	/** The mode paiement. {@link ModePaiement}. */
	@JsonDeserialize(using = ModePaiementDeserializer.class)
	@Enumerated(EnumType.STRING)
	private ModePaiement modePaiement;

	/**
	 * reference de mode paiement.
	 */
	private String referenceModePaiement;

	/** The mode facturation. {@link ModeFacturation}. */
	@JsonDeserialize(using = ModeFacturationDeserializer.class)
	@Enumerated(EnumType.STRING)
	private ModeFacturation modeFacturation;

	/**
	 * liste des frais.
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "elementcontractuelfraiscontrat")
	private Set<FraisContrat> frais = new HashSet<>();

	/**
	 * date de preparation du contrat.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date datePreparation;

	/**
	 * date de validation du contrat.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateValidation;

	/**
	 * date qui indique la fin de livraison et la debut de facturation.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateDebutFacturation;

	/**
	 * date du derniere facture.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateDerniereFacture;

	/**
	 * date du facture resiliation.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateFactureResiliation;

	/**
	 * date fin engagement.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateFinEngagement;

	/**
	 * date fin duree.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateFinDuree;

	/**
	 * date de fin du sous contrat.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateFinContrat;

	/**
	 * boolean pour faire la difference entre les elements resilié à cause de migration et les elements resilie
	 * auparavant.
	 */
	private Boolean isMigre;

	/** The type resiliation. {@link TypeResiliation}. */
	@JsonDeserialize(using = TypeResiliationDeserializer.class)
	@Enumerated(EnumType.STRING)
	private TypeResiliation typeResiliation;

	/** The element contractuel parent. */
	@ManyToOne
	@JoinColumn(name = "dependDe")
	private ElementContractuel elementContractuelParent;

	/** The sous element contractuel. */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "elementContractuelParent", fetch = FetchType.EAGER)
	private Set<ElementContractuel> sousElementContractuel = new HashSet<>();

	/**
	 * Politique résiliation.
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "politiqueResiliationId", nullable = true)
	private PolitiqueResiliation politiqueResiliation;

	/**
	 * un EL peut etre remboursable ou non.
	 */
	private Boolean remboursable;

	/**
	 * constructeur par defaut.
	 */
	public ElementContractuel() {

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

	/**
	 * Gets the type contrat.
	 * 
	 * @return {@link TypeContrat}.
	 */
	public TypeContrat getTypeContrat() {
		Optional<Integer> periodiciteOptional = Optional.fromNullable(periodicite);
		if (!periodiciteOptional.isPresent()) {
			return TypeContrat.VENTE;
		}
		if (typeProduit == TypeProduit.SERVICE) {
			return TypeContrat.ABONNEMENT;
		} else if (typeProduit == TypeProduit.BIEN) {
			return TypeContrat.LOCATION;
		}
		return null;
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
	 * Tester si un element contractuel est cloture(resilier + facturer).
	 * 
	 * @return true si le contrat est cloture.
	 */
	public boolean isCloture() {

		if (!getTypeContrat().equals(TypeContrat.VENTE) && dateFinContrat != null && dateFactureResiliation != null) {
			return true;
		}
		return false;
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
		if (!(obj instanceof ElementContractuel)) {
			return false;
		}
		ElementContractuel rhs = (ElementContractuel) obj;
		return new EqualsBuilder().append(id, rhs.id).append(idAdrFacturation, rhs.idAdrFacturation)
				.append(idAdrLivraison, rhs.idAdrLivraison).append(numEC, rhs.numEC).append(titre, rhs.titre)
				.isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(43, 11).append(id).append(titre).append(referenceProduit).toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ElementContractuel [id=" + id + ", idAdrFacturation=" + idAdrFacturation + ", idAdrLivraison="
				+ idAdrLivraison + ", referenceProduit=" + referenceProduit + ", titre=" + titre + ", typeContrat="
				+ getTypeContrat() + ", engagement=" + engagement + ", duree=" + duree + ", montant=" + montant
				+ ", typeTVA=" + typeTVA + ", periodicite=" + periodicite + ", modePaiement=" + modePaiement
				+ ", referenceModePaiement=" + referenceModePaiement + ", modeFacturation=" + modeFacturation
				+ ", frais=" + frais + ", datePreparation=" + datePreparation + ", dateValidation=" + dateValidation
				+ ", dateDerniereFacture=" + dateDerniereFacture + ", dateFinContrat=" + dateFinContrat
				+ ", typeResiliation=" + typeResiliation + "]";
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
	 * Gets the contrat parent.
	 * 
	 * @return {@link Contrat}.
	 */
	public Contrat getContratParent() {
		return contratParent;
	}

	/**
	 * Sets the contrat parent.
	 * 
	 * @param contratParent
	 *            the new contrat parent {@link Contrat}.
	 */
	public void setContratParent(Contrat contratParent) {
		this.contratParent = contratParent;
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
	 * @param nomProduit
	 *            the new titre {@link #nomProduit}.
	 */
	public void setTitre(String nomProduit) {
		this.titre = nomProduit;
	}

	/**
	 * Gets the reference produit.
	 * 
	 * @return {@link #referenceProduit}.
	 */
	public String getReferenceProduit() {
		return referenceProduit;
	}

	/**
	 * Sets the reference produit.
	 * 
	 * @param referenceProduit
	 *            the new reference produit {@link #referenceProduit}.
	 */
	public void setReferenceProduit(String referenceProduit) {
		this.referenceProduit = referenceProduit;
	}

	/**
	 * 
	 * @return {@link #referenceTarif}.
	 */
	public String getReferenceTarif() {
		return referenceTarif;
	}

	/**
	 * 
	 * @param referenceTarif
	 *            {@link #referenceTarif}.
	 */
	public void setReferenceTarif(String referenceTarif) {
		this.referenceTarif = referenceTarif;
	}

	/**
	 * @return {@link #idProduit}.
	 */
	public String getIdProduit() {
		// idProduit =
		idProduit = referenceProduit.replaceAll("\\s+", ".");
		if (elementContractuelParent != null) {
			return elementContractuelParent.getIdProduit() + System.getProperty("separator") + idProduit;
		}
		return idProduit;
	}

	/**
	 * @param idProduit
	 *            {@link #idProduit}.
	 */
	public void setIdProduit(String idProduit) {
		this.idProduit = idProduit;
	}

	/**
	 * 
	 * @return {@link #numEC}.
	 */
	public Integer getNumEC() {
		return numEC;
	}

	/**
	 * 
	 * @param numEC
	 *            {@link #numEC}.
	 */
	public void setNumEC(Integer numEC) {
		this.numEC = numEC;
	}

	/**
	 * Gets the type produit.
	 * 
	 * @return {@link #typeProduit}
	 */
	public TypeProduit getTypeProduit() {
		return typeProduit;
	}

	/**
	 * Sets the type produit.
	 * 
	 * @param typeProduit
	 *            the new type produit {@link #typeProduit}
	 */
	public void setTypeProduit(TypeProduit typeProduit) {
		this.typeProduit = typeProduit;
	}

	/**
	 * Gets the engagement.
	 * 
	 * @return {@link #engagement}.
	 */
	public Integer getEngagement() {
		return engagement;
	}

	/**
	 * @return le maximum d'engagement des sous contrat.
	 */
	public Integer getEngagementMax() {
		if (engagement != null) {
			engagementMax = engagement;
		}

		for (ElementContractuel elementContractuel : contratParent.getSousContrats()) {
			if (elementContractuel.getDependDe() != null && elementContractuel.getDependDe().equals(numEC)
					&& elementContractuel.getEngagementMax() > engagementMax) {
				engagementMax = elementContractuel.getEngagementMax();
			}
		}
		return engagementMax;
	}

	/**
	 * @param engagementMax
	 *            {@link #engagementMax}.
	 */
	public void setEngagementMax(Integer engagementMax) {
		this.engagementMax = engagementMax;
	}

	/**
	 * Sets the engagement.
	 * 
	 * @param engagement
	 *            the new engagement {@link #engagement}.
	 */
	public void setEngagement(Integer engagement) {
		this.engagement = engagement;
	}

	/**
	 * Gets the duree.
	 * 
	 * @return {@link #duree}.
	 */
	public Integer getDuree() {
		return duree;
	}

	/**
	 * Sets the duree.
	 * 
	 * @param duree
	 *            the new duree {@link #duree}.
	 */
	public void setDuree(Integer duree) {
		this.duree = duree;
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
	 * Gets the periodicite.
	 * 
	 * @return {@link periodicite}.
	 */
	public Integer getPeriodicite() {
		return periodicite;
	}

	/**
	 * Sets the periodicite.
	 * 
	 * @param periodicite
	 *            the new periodicite {@link periodicite}.
	 */
	public void setPeriodicite(Integer periodicite) {
		this.periodicite = periodicite;
	}

	/**
	 * Gets the mode paiement.
	 * 
	 * @return {@link ModePaiement}.
	 */
	public ModePaiement getModePaiement() {
		return modePaiement;
	}

	/**
	 * Sets the mode paiement.
	 * 
	 * @param modePaiement
	 *            the new mode paiement {@link ModePaiement}.
	 */
	public void setModePaiement(ModePaiement modePaiement) {
		this.modePaiement = modePaiement;
	}

	/**
	 * Gets the reference mode paiement.
	 * 
	 * @return {@link referenceModePaiement}.
	 */
	public String getReferenceModePaiement() {
		return referenceModePaiement;
	}

	/**
	 * Sets the reference mode paiement.
	 * 
	 * @param referenceModePaiement
	 *            the new reference mode paiement {@link referenceModePaiement}.
	 */
	public void setReferenceModePaiement(String referenceModePaiement) {
		this.referenceModePaiement = referenceModePaiement;
	}

	/**
	 * Gets the frais.
	 * 
	 * @return {@link FraisContrat}.
	 */
	public Set<FraisContrat> getFrais() {
		return frais;
	}

	/**
	 * Sets the frais.
	 * 
	 * @param frais
	 *            the new frais {@link FraisContrat}.
	 */
	public void setFrais(Set<FraisContrat> frais) {
		this.frais = frais;
	}

	/**
	 * Gets the id adr facturation.
	 * 
	 * @return {@link #idAdrFacturation}.
	 */
	public String getIdAdrFacturation() {
		return idAdrFacturation;
	}

	/**
	 * Sets the id adr facturation.
	 * 
	 * @param idAdrFacturation
	 *            the new id adr facturation {@link #idAdrFacturation}.
	 */
	public void setIdAdrFacturation(String idAdrFacturation) {
		this.idAdrFacturation = idAdrFacturation;
	}

	/**
	 * Gets the id adr livraison.
	 * 
	 * @return {@link #idAdrLivraison}.
	 */
	public String getIdAdrLivraison() {
		return idAdrLivraison;
	}

	/**
	 * Sets the id adr livraison.
	 * 
	 * @param idAdrLivraison
	 *            the new id adr livraison {@link #idAdrLivraison}.
	 */
	public void setIdAdrLivraison(String idAdrLivraison) {
		this.idAdrLivraison = idAdrLivraison;
	}

	/**
	 * Gets the mode facturation.
	 * 
	 * @return {@link ModeFacturation}.
	 */
	public ModeFacturation getModeFacturation() {
		return modeFacturation;
	}

	/**
	 * Sets the mode facturation.
	 * 
	 * @param modeFacturation
	 *            the new mode facturation {@link ModeFacturation}.
	 */
	public void setModeFacturation(ModeFacturation modeFacturation) {
		this.modeFacturation = modeFacturation;
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
	 * Sets the date validation.
	 * 
	 * @param dateValidation
	 *            the new date validation {@link #dateValidation}.
	 */
	public void setDateValidation(Date dateValidation) {
		this.dateValidation = dateValidation;
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
	 * 
	 * @return {@link #dateFinEngagement}.
	 */
	public Date getDateFinEngagement() {
		return dateFinEngagement;
	}

	/**
	 * 
	 * @param dateFinEngagement
	 *            {@link #dateFinEngagement}.
	 */
	public void setDateFinEngagement(Date dateFinEngagement) {
		this.dateFinEngagement = dateFinEngagement;
	}

	/**
	 * 
	 * @return {@link #dateFinDuree}.
	 */
	public Date getDateFinDuree() {
		return dateFinDuree;
	}

	/**
	 * 
	 * @param dateFinDuree
	 *            {@link #dateFinDuree}.
	 */
	public void setDateFinDuree(Date dateFinDuree) {
		this.dateFinDuree = dateFinDuree;
	}

	/**
	 * Gets the date fin contrat.
	 * 
	 * @return {@link #dateFinContrat}.
	 */
	public Date getDateFinContrat() {
		if (dateFinContrat == null && contratParent.getDateDebutFacturation() != null && duree != null) {
			return LocalDate.fromDateFields(contratParent.getDateDebutFacturation()).plusMonths(duree).toDate();
		}
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
	 * @return {@link #isMigre}.
	 */
	public Boolean isMigre() {
		return isMigre;
	}

	/**
	 * @param isMigre
	 *            {@link #isMigre}.
	 */
	public void setMigre(Boolean isMigre) {
		this.isMigre = isMigre;
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
	 * Gets the element contractuel parent.
	 * 
	 * @return the element contractuel parent
	 */
	public ElementContractuel getElementContractuelParent() {
		return elementContractuelParent;
	}

	/**
	 * Sets the element contractuel parent.
	 * 
	 * @param elementContractuelParent
	 *            the new element contractuel parent
	 */
	public void setElementContractuelParent(ElementContractuel elementContractuelParent) {
		this.elementContractuelParent = elementContractuelParent;
	}

	/**
	 * Gets the sous element contractuel.
	 * 
	 * @return the sous element contractuel
	 */
	public Set<ElementContractuel> getSousElementContractuel() {
		return sousElementContractuel;
	}

	/**
	 * Sets the sous element contractuel.
	 * 
	 * @param sousElementContractuel
	 *            the new sous element contractuel
	 */
	public void setSousElementContractuel(Set<ElementContractuel> sousElementContractuel) {
		this.sousElementContractuel = sousElementContractuel;
	}

	/**
	 * Gets the politique resiliation.
	 * 
	 * @return the politique resiliation
	 */
	public PolitiqueResiliation getPolitiqueResiliation() {
		return politiqueResiliation;
	}

	/**
	 * Sets the politique resiliation.
	 * 
	 * @param politiqueResiliation
	 *            the new politique resiliation
	 */
	public void setPolitiqueResiliation(PolitiqueResiliation politiqueResiliation) {
		this.politiqueResiliation = politiqueResiliation;
	}

	/**
	 * 
	 * @return indique que l'element est remboursable ou non.
	 */
	public Boolean isRemboursable() {
		return remboursable;
	}

	/**
	 * 
	 * @param remboursable
	 *            remboursable.
	 */
	public void setRemboursable(Boolean remboursable) {
		this.remboursable = remboursable;
	}

	/**
	 * @return reference du elementcontractuel parent.
	 */
	@JsonProperty("numECParent")
	public Integer getDependDe() {
		Integer dependDe = null;
		if (elementContractuelParent != null) {
			dependDe = elementContractuelParent.getNumEC();
		}
		return dependDe;
	}

	/**
	 * Verifier si l'element contractuel a un parent.
	 * 
	 * @return true si l'element contratctuel a un parent.
	 */
	public boolean hasParent() {
		if (this.getElementContractuelParent() != null) {
			return true;
		}
		return false;
	}

	/**
	 * creation de {@link ElementContractuelHistorique} à partir de {@link ElementContractuel}.
	 * 
	 * @return {@link ElementContractuelHistorique}.
	 */
	public ElementContractuelHistorique toElementContractuelHistorique() {
		ElementContractuelHistorique elementContractuelHistorique = new ElementContractuelHistorique();
		elementContractuelHistorique.setNumeroCommande(numeroCommande);
		elementContractuelHistorique.setIdAdrFacturation(idAdrFacturation);
		elementContractuelHistorique.setIdAdrLivraison(idAdrLivraison);
		elementContractuelHistorique.setReferenceProduit(referenceProduit);
		elementContractuelHistorique.setReferenceTarif(referenceTarif);
		elementContractuelHistorique.setNumEC(numEC);
		elementContractuelHistorique.setTypeProduit(typeProduit);
		elementContractuelHistorique.setTitre(titre);
		elementContractuelHistorique.setEngagement(engagement);
		elementContractuelHistorique.setDuree(duree);
		elementContractuelHistorique.setMontant(montant);
		elementContractuelHistorique.setTypeTVA(typeTVA);
		elementContractuelHistorique.setPeriodicite(periodicite);
		elementContractuelHistorique.setModePaiement(modePaiement);
		elementContractuelHistorique.setReferenceModePaiement(referenceModePaiement);
		elementContractuelHistorique.setModeFacturation(modeFacturation);
		elementContractuelHistorique.setDatePreparation(datePreparation);
		elementContractuelHistorique.setDateValidation(dateValidation);
		elementContractuelHistorique.setDateDebutFacturation(dateDebutFacturation);
		elementContractuelHistorique.setDateDerniereFacture(dateDerniereFacture);
		elementContractuelHistorique.setDateFinEngagement(dateFinEngagement);
		elementContractuelHistorique.setDateFinDuree(dateFinDuree);
		elementContractuelHistorique.setDateFinContrat(dateFinContrat);
		elementContractuelHistorique.setTypeResiliation(typeResiliation);
		elementContractuelHistorique.setRemboursable(remboursable);
		if (isMigre == null) {
			elementContractuelHistorique.setMigre(false);
		} else {
			elementContractuelHistorique.setMigre(isMigre);

		}
		if (politiqueResiliation != null) {
			elementContractuelHistorique.setPolitiqueResiliation(politiqueResiliation.copy());
		}
		for (FraisContrat fraisContrat : frais) {
			elementContractuelHistorique.getFrais().add(fraisContrat.copy());
		}
		return elementContractuelHistorique;
	}

	public boolean isMigration(ElementContractuelHistorique elementContractuelHistorique) {
		if (numEC == elementContractuelHistorique.getNumEC()
				&& (!referenceProduit.equals(elementContractuelHistorique.getReferenceProduit())
						|| montant != elementContractuelHistorique.getMontant()
						|| !typeTVA.equals(elementContractuelHistorique.getTypeTVA())
						|| periodicite != elementContractuelHistorique.getPeriodicite() || !modeFacturation
							.equals(elementContractuelHistorique.getModeFacturation()))) {
			return true;
		}
		return false;
	}

	public boolean isMigrable() {
		return !getTypeContrat().equals(TypeContrat.VENTE) && !isCloture();
	}

	public boolean isResiliable() {
		return !getTypeContrat().equals(TypeContrat.VENTE) && !isCloture();
	}

	/**
	 * Tester s'il y a une migration administrative.
	 * 
	 * @param element
	 *            {@link ElementContractuelHistorique}.
	 * @return true si migration administrative.
	 */
	public boolean isMigrationAdministrative(ElementContractuelHistorique element) {
		if (element != null
				&& element.getReferenceProduit().equals(referenceProduit)
				&& (element.getMontant() != montant || element.getTypeTVA() != typeTVA
						|| element.getPeriodicite() != periodicite || element.getModeFacturation() != modeFacturation)) {
			return true;
		}
		return false;
	}
}