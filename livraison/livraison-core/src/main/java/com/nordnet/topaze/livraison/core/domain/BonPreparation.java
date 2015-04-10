package com.nordnet.topaze.livraison.core.domain;

import java.io.IOException;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.livraison.core.util.PropertiesUtil;
import com.nordnet.topaze.livraison.core.util.TypeBonPreparationDeserializer;

/**
 * contient tout les informations necessaire pour un bon de praparation.
 * 
 * @author akram-moncer
 * @author anisselmane.
 * 
 */

@Entity
@Table(name = "bonpreparation")
@JsonIgnoreProperties({ "id", "initier", "preparer", "livrer", "termine", "nonLivrer", "annuler", "statut",
		"elementRetours", "cede", "enCoursPreparation" })
public class BonPreparation {

	/**
	 * cle primaire.
	 */
	@Id
	@GeneratedValue
	private Integer id;

	/**
	 * reference du BonPreparation.
	 */
	@NotNull
	private String reference;

	/**
	 * idClient.
	 */
	private String idClient;

	/**
	 * date d'initiation du bon de preparation.
	 */
	private Date dateInitiation;

	/**
	 * date du preparation du bon de livraision.
	 */
	private Date datePreparation;

	/**
	 * date de debut de preparation du bon de livraison.
	 */
	private Date dateDebutPreparation;

	/**
	 * date de fin de livraison en cas de migration.
	 */
	private Date dateLivraisonTermine;

	/**
	 * date de retour depend du {@link #typeBonPreparation}.
	 */
	private Date dateRetourTermine;

	/**
	 * date d'annulation du bp.
	 */
	private Date dateAnnulation;

	/**
	 * type du bon de preparation.
	 */
	@JsonDeserialize(using = TypeBonPreparationDeserializer.class)
	@Enumerated(EnumType.STRING)
	private TypeBonPreparation typeBonPreparation;

	/**
	 * liste des sous bon de preparation.
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "bonPreparationParent", fetch = FetchType.EAGER)
	private Set<ElementLivraison> elementLivraisons = new HashSet<>();

	/**
	 * Cause de non livrason.
	 */
	private String causeNonlivraison;

	/**
	 * constructeur par defaut.
	 */
	public BonPreparation() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BonPreparation [id=" + id + ", reference=" + reference + ", idClient=" + idClient + ", dateInitiation="
				+ dateInitiation + ", datePreparation=" + datePreparation + ",dateDebutPreparation="
				+ dateDebutPreparation + ", typeBonPreparation=" + typeBonPreparation + ", elementLivraisons="
				+ elementLivraisons + ", causeNonlivraison=" + causeNonlivraison + "]";
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
	 *            the new id client {@link #reference}.
	 */
	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}

	/**
	 * Gets the date initiation.
	 * 
	 * @return {@link #dateInitiation}.
	 */
	public Date getDateInitiation() {
		return dateInitiation;
	}

	/**
	 * Sets the date initiation.
	 * 
	 * @param dateInitiation
	 *            the new date initiation {@link #dateInitiation}.
	 */
	public void setDateInitiation(Date dateInitiation) {
		this.dateInitiation = dateInitiation;
	}

	/**
	 * Gets the date preparation.
	 * 
	 * @return {@link #datePreparation}.
	 */
	public Date getDatePreparation() {
		return datePreparation;
	}

	/**
	 * Sets the date preparation.
	 * 
	 * @param datePreparation
	 *            the new date preparation {@link #datePreparation}.
	 */
	public void setDatePreparation(Date datePreparation) {
		this.datePreparation = datePreparation;
	}

	/**
	 * Gets the date debut preparation.
	 * 
	 * @return {@link #dateDebutPreparation}
	 */
	public Date getDateDebutPreparation() {
		return dateDebutPreparation;
	}

	/**
	 * Sets the date debut preparation.
	 * 
	 * @param dateDebutPreparation
	 *            the new date debut preparation {@link #dateDebutPreparation}
	 */
	public void setDateDebutPreparation(Date dateDebutPreparation) {
		this.dateDebutPreparation = dateDebutPreparation;
	}

	/**
	 * 
	 * @return {@link #dateLivraisonTermine}
	 */
	public Date getDateLivraisonTermine() {
		return dateLivraisonTermine;
	}

	/**
	 * 
	 * @param dateLivraisonTermine
	 *            {@link #dateLivraisonTermine}
	 */

	public void setDateLivraisonTermine(Date dateLivraisonTermine) {
		this.dateLivraisonTermine = dateLivraisonTermine;
	}

	/**
	 * @return {@link #dateRetourTermine}.
	 */
	public Date getDateRetourTermine() {
		return dateRetourTermine;
	}

	/**
	 * @param dateRetourTermine
	 *            {@link #dateRetourTermine}.
	 */
	public void setDateRetourTermine(Date dateRetourTermine) {
		this.dateRetourTermine = dateRetourTermine;
	}

	/**
	 * 
	 * @return {@link #dateAnnulation}.
	 */
	public Date getDateAnnulation() {
		return dateAnnulation;
	}

	/**
	 * 
	 * @param dateAnnulation
	 *            {@link #dateAnnulation}.
	 */
	public void setDateAnnulation(final Date dateAnnulation) {
		this.dateAnnulation = dateAnnulation;
	}

	/**
	 * Gets the type bon preparation.
	 * 
	 * @return {@link #typeBonPreparation}.
	 */
	public TypeBonPreparation getTypeBonPreparation() {
		return typeBonPreparation;
	}

	/**
	 * Sets the type bon preparation.
	 * 
	 * @param typeBonPreparation
	 *            the new type bon preparation {@link #typeBonPreparation}.
	 */
	public void setTypeBonPreparation(TypeBonPreparation typeBonPreparation) {
		this.typeBonPreparation = typeBonPreparation;
	}

	/**
	 * @return {@link #elementLivraisons}.
	 */
	public Set<ElementLivraison> getElementLivraisons() {
		return elementLivraisons;
	}

	/**
	 * @param elementLivraisons
	 *            {@link #ElementLivraison}.
	 */
	public void setElementLivraison(Set<ElementLivraison> elementLivraisons) {
		this.elementLivraisons = elementLivraisons;
	}

	/**
	 * Gets the cause nonlivraison.
	 * 
	 * @return {@link #causeNonlivraison}.
	 */
	public String getCauseNonlivraison() {
		return causeNonlivraison;
	}

	/**
	 * Sets the cause nonlivraison.
	 * 
	 * @param causeNonlivraison
	 *            the new cause nonlivraison {@link #causeNonlivraison}.
	 */
	public void setCauseNonlivraison(String causeNonlivraison) {
		this.causeNonlivraison = causeNonlivraison;
	}

	/**
	 * Testet si un BP est initier.
	 * 
	 * @return true si BP est initier.
	 */
	public boolean isInitier() {
		return getStatut().equals(StatusBonPreparation.INITIER);
	}

	/**
	 * Testet si un BP est en cours preparation.
	 * 
	 * @return true si BP est en cours preparation.
	 */
	public boolean isEnCoursPreparation() {
		return getStatut().equals(StatusBonPreparation.EN_COURS_PREPARATION);
	}

	/**
	 * Testet si un BP est preparer.
	 * 
	 * @return true si BP est preparer.
	 */
	public boolean isPreparer() {
		return getStatut().equals(StatusBonPreparation.PREPARER);
	}

	/**
	 * Testet si un BP est Termine.
	 * 
	 * @return true si BP est Termine.
	 */
	public boolean isTermine() {
		return getStatut().equals(StatusBonPreparation.TERMINER);
	}

	/**
	 * Testet si un BP est non livrer.
	 * 
	 * @return true si BP est non livrer.
	 */
	public boolean isNonLivrer() {
		return getStatut().equals(StatusBonPreparation.NON_LIVRER);
	}

	/**
	 * Testet si un BP est annule.
	 * 
	 * @return true si BP est annule.
	 */
	public boolean isAnnule() {
		return getStatut().equals(StatusBonPreparation.ANNULER);
	}

	/**
	 * Gets the statut.
	 * 
	 * @return {@link #status}.
	 */
	public StatusBonPreparation getStatut() {

		if (dateAnnulation != null) {
			return StatusBonPreparation.ANNULER;
		}

		if (datePreparation != null && causeNonlivraison != null) {
			return StatusBonPreparation.NON_LIVRER;
		}

		if ((typeBonPreparation.equals(TypeBonPreparation.MIGRATION) && dateLivraisonTermine != null && dateRetourTermine != null)
				|| (typeBonPreparation.equals(TypeBonPreparation.LIVRAISON) && dateLivraisonTermine != null || (typeBonPreparation
						.equals(TypeBonPreparation.RETOUR) && dateRetourTermine != null))) {
			return StatusBonPreparation.TERMINER;
		}

		if (datePreparation != null) {
			return StatusBonPreparation.PREPARER;
		}

		if (dateDebutPreparation != null) {
			return StatusBonPreparation.EN_COURS_PREPARATION;
		}

		if (dateInitiation != null) {
			return StatusBonPreparation.INITIER;
		}

		return null;
	}

	/**
	 * 
	 * @return les element de livraison a retourner lord de la resiliation
	 */
	public Set<ElementLivraison> getElementRetours() {
		Set<ElementLivraison> elementRetours = new HashSet<>();
		for (ElementLivraison elementLivraison : elementLivraisons) {
			if (!elementLivraison.getTypeContrat().equals(TypeContrat.VENTE)) {
				elementRetours.add(elementLivraison);
			}
		}
		return elementRetours;
	}

	/**
	 * From json to bon preparation.
	 * 
	 * @param json
	 *            l'objet {@link BonPreparation} en format json.
	 * @return {@link BonPreparation}
	 * @throws IOException
	 *             {@link IOException}
	 */
	public static BonPreparation fromJsonToBonPreparation(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, BonPreparation.class);
	}

	/**
	 * To json.
	 * 
	 * @param bonPreparation
	 *            le {@link BonPreparation} a transforme en objet {@link JSONObject}
	 * @return {@link JSONObject}
	 * @throws JsonProcessingException
	 *             {@link JsonProcessingException}
	 */
	public static String toJson(BonPreparation bonPreparation) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(bonPreparation);
	}

	/**
	 * To json array.
	 * 
	 * @param bonPreparations
	 *            collection de {@link BonPreparation}
	 * @return la colllection de {@link BonPreparation} en format json.
	 * @throws JsonProcessingException
	 *             {@link JsonProcessingException}
	 */
	public static String toJsonArray(Collection<BonPreparation> bonPreparations) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(bonPreparations);
	}

	/**
	 * vérifier si un bon de preparation est cede ou non.
	 * 
	 * @return true si le bon de preparation est cede.
	 */
	public boolean isCede() {
		if (dateLivraisonTermine != null) {
			return true;
		}
		return false;
	}

	/**
	 * annuler un BP.
	 * 
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	@JsonIgnore
	public void annuler() throws TopazeException {
		dateAnnulation = PropertiesUtil.getInstance().getDateDuJour().toDate();
	}

}
