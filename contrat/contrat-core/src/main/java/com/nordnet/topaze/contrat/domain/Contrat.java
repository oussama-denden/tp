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
import org.hibernate.annotations.Index;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.DateSerializer;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.contrat.util.TypeResiliationDeserializer;
import com.nordnet.topaze.contrat.util.Utils;
import com.nordnet.topaze.contrat.validator.ContratValidator;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette classe regroupe les informations qui definissent un {@link Contrat}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Entity
@Table(name = "contrat")
@JsonIgnoreProperties({ "id", "avenant", "minPeriodicite" })
public class Contrat {

	/**
	 * cle primaire.
	 */
	@Id
	@GeneratedValue
	@Index(columnNames = "id", name = "index_id")
	private Integer id;

	/**
	 * reference du contrat.
	 */
	@NotNull
	@Index(columnNames = "reference", name = "index_reference")
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
	@Index(columnNames = "titre", name = "index_titre")
	private String titre;

	/**
	 * liste des sous contrats.
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "contratParent", fetch = FetchType.EAGER)
	private Set<ElementContractuel> sousContrats = new HashSet<>();

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
	 * date de fin du sous contrat.
	 */
	@JsonSerialize(using = DateSerializer.class)
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
	 * constructeur par defaut.
	 */
	public Contrat() {

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
		if (!(obj instanceof Contrat)) {
			return false;
		}
		Contrat rhs = (Contrat) obj;
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
	public Set<ElementContractuel> getSousContrats() {
		return sousContrats;
	}

	/**
	 * Sets the sous contrats.
	 * 
	 * @param sousContrats
	 *            the new sous contrats {@link #sousContrats}.
	 */
	public void setSousContrats(Set<ElementContractuel> sousContrats) {
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
		for (ElementContractuel elementContractuel : sousContrats) {
			if (elementContractuel.getEngagementMax() > engagementMax) {
				engagementMax = elementContractuel.getEngagementMax();
			}
		}
		return engagementMax;
	}

	/**
	 * @return le minimum des periodicites.
	 */
	public Integer getMinPeriodicite() {
		List<Integer> periodicites = new ArrayList<>();
		for (ElementContractuel elementContractuel : sousContrats) {
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

	/**
	 * Resilier.
	 * 
	 * @param typeResiliation
	 *            the type resiliation
	 * @param politiqueResiliation
	 *            the politique resiliation
	 * @param dateFinContrat
	 *            date fin contrat.
	 * @param isResiliationFuture
	 *            the is resiliation future
	 * @{link TopazeException }
	 */
	public void resilier(TypeResiliation typeResiliation, PolitiqueResiliation politiqueResiliation,
			Date dateFinContrat, boolean isResiliationFuture, boolean isMigration) {

		for (ElementContractuel c : this.getSousContrats()) {
			if (!c.isResilier()) {
				if (!c.getTypeContrat().equals(TypeContrat.VENTE)) {

					// test si le cron de la resiliation est l'appelant de la méthode.
					if (politiqueResiliation.getDateResiliation() == null || !isResiliationFuture) {
						c.setTypeResiliation(typeResiliation);
						c.setDateFinContrat(dateFinContrat);
					}

					c.setMigre(isMigration);

				}
			}
		}

		// vérifier si ce n'est pas une résiliation future ou un applle du batch résiliation future.
		if (politiqueResiliation.getDateResiliation() == null || !isResiliationFuture) {
			this.setTypeResiliation(typeResiliation);
			this.setDateFinContrat(dateFinContrat);

		}

		if (this.getPolitiqueResiliation() == null || this.getPolitiqueResiliation().getDateAnnulation() != null) {
			this.setPolitiqueResiliation(politiqueResiliation);
		}

	}

	/**
	 * Resiliation partiel.
	 * 
	 * @param typeResiliation
	 *            the type resiliation
	 * @param referenceProduit
	 *            reference du produit.
	 * @param numEC
	 *            numero element contractuel.
	 * @param politiqueResiliation
	 *            the politique resiliation
	 * @param dateFinContrat
	 *            date fin contrat.
	 * @param isResiliationFuture
	 *            the is resiliation future
	 * @return liste des references du {@link ElementContractuel} resilier.
	 * @throws TopazeException
	 *             the contrat resiliation exception {@link TypeResiliation}. {@link PolitiqueResiliation}.
	 *             {@link TopazeException}.
	 */
	public List<Integer> resilierPartiel(TypeResiliation typeResiliation, String referenceProduit, Integer numEC,
			PolitiqueResiliation politiqueResiliation, Date dateFinContrat, boolean isResiliationFuture)
			throws TopazeException {

		List<Integer> referenceContratsResilier = new ArrayList<>();

		for (ElementContractuel c : this.getSousContrats()) {
			if (c.getReferenceProduit().equals(referenceProduit) && c.getNumEC() == numEC) {

				// test si le cron de la resiliation est l'appelant de la méthode.
				if (politiqueResiliation.getDateResiliation() == null || !isResiliationFuture) {
					c.setTypeResiliation(typeResiliation);
					ContratValidator.checkSousContratNonResilier(c);
					c.setDateFinContrat(dateFinContrat);
				}

				if (this.getPolitiqueResiliation() == null) {
					c.setPolitiqueResiliation(politiqueResiliation);
				}

				referenceContratsResilier.add(c.getNumEC());
			}
		}

		return referenceContratsResilier;
	}

	/**
	 * Resiliation partiel en cascade.
	 * 
	 * @param dateResiliation
	 *            date resiliation.
	 * @param typeResiliation
	 *            the type resiliation
	 * @param numEC
	 *            numero element contractuel.
	 * @param politiqueResiliation
	 *            the politique resiliation
	 * @param dateFinContrat
	 *            date fin contrat.
	 * @param isResiliationFuture
	 *            the is resiliation future
	 * @return liste des references du {@link ElementContractuel} resilier.
	 * @throws TopazeException
	 *             the contrat resiliation exception {@link TypeResiliation}. {@link PolitiqueResiliation}.
	 *             {@link TopazeException}.
	 */
	public List<Integer> resilierCascade(Date dateResiliation, TypeResiliation typeResiliation, Integer numEC,
			PolitiqueResiliation politiqueResiliation, Date dateFinContrat, boolean isResiliationFuture)
			throws TopazeException {

		List<Integer> referenceContratsResilier = new ArrayList<>();

		for (ElementContractuel c : this.getSousContrats()) {

			if (c.getNumEC() == numEC) {
				ContratValidator.checkSousContratNonResilier(c);
				// test si le cron de la resiliation est l'appelant de la méthode.
				if (politiqueResiliation.getDateResiliation() == null || !isResiliationFuture) {
					c.setTypeResiliation(typeResiliation);
					c.setDateFinContrat(dateFinContrat);
				}

				if (this.getPolitiqueResiliation() == null) {
					c.setPolitiqueResiliation(politiqueResiliation);
				}

				referenceContratsResilier.add(c.getNumEC());
			}

			if (!c.getTypeContrat().equals(TypeContrat.VENTE) && c.getElementContractuelParent() != null
					&& !c.isResilier() && c.getElementContractuelParent().getNumEC() == numEC) {

				referenceContratsResilier.addAll(resilierCascade(dateResiliation, typeResiliation, c.getNumEC(),
						politiqueResiliation, dateFinContrat, isResiliationFuture));
			}
		}
		return referenceContratsResilier;
	}

	/**
	 * Convertir un {@link Contrat} en {@link ContratHistorique}.
	 * 
	 * @return {@link ContratHistorique}.
	 */
	public ContratHistorique toContratHistorique() {

		ContratHistorique contratHistorique = new ContratHistorique();
		contratHistorique.setReference(reference);
		contratHistorique.setIdClient(idClient);
		contratHistorique.setTitre(titre);
		contratHistorique.setDatePreparation(datePreparation);
		contratHistorique.setDateValidation(dateValidation);
		contratHistorique.setDateDebutFacturation(dateDebutFacturation);
		contratHistorique.setDateDerniereFacture(dateDerniereFacture);
		contratHistorique.setDateFinContrat(dateFinContrat);
		contratHistorique.setTypeResiliation(typeResiliation);
		contratHistorique.setPolitiqueValidation(politiqueValidation);
		contratHistorique.setPolitiqueResiliation(politiqueResiliation);
		return contratHistorique;
	}

	/**
	 * 
	 * @return true si touts les address livraison des EC sont les mémes.
	 */
	public boolean isMemeIdAdrLivraison() {
		boolean isMemeIdAdrLivraison = true;
		String idAdrLivraison = null;
		for (ElementContractuel elementContractuel : sousContrats) {
			if (idAdrLivraison != null) {
				if (!idAdrLivraison.equalsIgnoreCase(elementContractuel.getIdAdrLivraison())) {
					isMemeIdAdrLivraison = false;
					break;
				}
			}
			idAdrLivraison = elementContractuel.getIdAdrLivraison();
		}

		return isMemeIdAdrLivraison;
	}

	/**
	 * @return le minimum des periodicites.
	 */
	public Integer getMaxDuree() {
		List<Integer> durees = new ArrayList<>();
		for (ElementContractuel elementContractuel : sousContrats) {
			if (elementContractuel.getDuree() != null) {
				durees.add(elementContractuel.getDuree());
			}
		}
		if (durees.size() > Constants.ZERO) {
			Collections.sort(durees);
			return durees.get(durees.size() - 1);
		}
		return null;

	}

	/**
	 * @return la date fin duree maximale.
	 */
	@JsonIgnore
	public Date getMaxDateFinDuree() {

		Date dureeMax =
				sousContrats.size() != 0
						? sousContrats.toArray(new ElementContractuel[sousContrats.size()])[Constants.ZERO]
								.getDateFinDuree() : null;

		for (ElementContractuel elementContractuel : sousContrats) {
			if (elementContractuel.getDateFinDuree() != null
					&& Utils.compareDate(elementContractuel.getDateFinDuree(), dureeMax) == 1) {
				dureeMax = elementContractuel.getDateFinDuree();
			}
		}

		return dureeMax;
	}

	/**
	 * Vérifier si l'element contracutelle est le premier qui va changer la nb utilisation dans la reduction globale.
	 * 
	 * @param elementContractuel
	 *            {@link ElementContractuel}
	 * @return true si l'element est le premier.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public Boolean isElementfirst(ElementContractuel elementContractuel) throws TopazeException {

		boolean isPremier = true;
		for (ElementContractuel el : sousContrats) {

			if (!el.equals(elementContractuel)
					&& el.getDateDerniereFacture() != null
					&& Utils.compareDateWithoutTime(PropertiesUtil.getInstance().getDateDuJour().toDate(),
							el.getDateDerniereFacture()) == 0) {
				isPremier = false;
			}
		}
		return isPremier;
	}

	/**
	 * verifier si un contrat global est un contrat de vente, un contrat global est un contrat de vente si tous ses sous
	 * contrats sont de type vente.
	 * 
	 * @return true si le contrat est un contrat de vente.
	 */
	@JsonIgnore
	public Boolean isVente() {
		for (ElementContractuel elementContractuel : sousContrats) {
			if (elementContractuel.getTypeContrat() != TypeContrat.VENTE)
				return false;
		}
		return true;
	}

}