package com.nordnet.topaze.client.rest.business.livraison;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nordnet.topaze.client.rest.enums.StatusBonPreparation;
import com.nordnet.topaze.client.rest.enums.TypeBonPreparation;

/**
 * contient tout les informations necessaire pour un bon de praparation.
 * 
 * @author akram-moncer
 * @author anisselmane.
 * 
 */
@JsonIgnoreProperties({ "initier", "preparer", "livrer", "termine", "nonLivrer", "statut" })
public class BonPreparation {

	/**
	 * reference du BonPreparation.
	 */
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
	private TypeBonPreparation typeBonPreparation;

	/**
	 * liste des {@link ElementLivraison} associe au bon de preparation.
	 */
	private Set<ElementLivraison> elementLivraisons = new HashSet<>();

	/**
	 * Cause de non livrason.
	 */
	private String causeNonlivraison;

	/**
	 * Politique rembourcement.
	 */
	// private PolitiqueResiliation politiqueResiliation;

	/**
	 * constructeur par defaut.
	 */
	public BonPreparation() {

	}

	@Override
	public String toString() {
		return "BonPreparation [reference=" + reference + ", idClient=" + idClient + ", dateInitiation="
				+ dateInitiation + ", datePreparation=" + datePreparation + ",dateDebutPreparation="
				+ dateDebutPreparation + ", typeBonPreparation=" + typeBonPreparation + ", elementLivraisons="
				+ elementLivraisons + ", causeNonlivraison=" + causeNonlivraison + "]";
	}

	/* Getters & Setters */

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
	 * @return {@link #idClient}.
	 */
	public String getIdClient() {
		return idClient;
	}

	/**
	 * @param idClient
	 *            {@link #reference}.
	 */
	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}

	/**
	 * @return {@link #dateInitiation}.
	 */
	public Date getDateInitiation() {
		return dateInitiation;
	}

	/**
	 * @param dateInitiation
	 *            {@link #dateInitiation}.
	 */
	public void setDateInitiation(Date dateInitiation) {
		this.dateInitiation = dateInitiation;
	}

	/**
	 * @return {@link #datePreparation}.
	 */
	public Date getDatePreparation() {
		return datePreparation;
	}

	/**
	 * @param datePreparation
	 *            {@link #datePreparation}.
	 */
	public void setDatePreparation(Date datePreparation) {
		this.datePreparation = datePreparation;
	}

	/**
	 * 
	 * @return {@link #dateDebutPreparation}
	 */
	public Date getDateDebutPreparation() {
		return dateDebutPreparation;
	}

	/**
	 * 
	 * @param dateDebutPreparation
	 *            {@link #dateDebutPreparation}
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
	 * @return {@link #typeBonPreparation}.
	 */
	public TypeBonPreparation getTypeBonPreparation() {
		return typeBonPreparation;
	}

	/**
	 * @param typeBonPreparation
	 *            {@link #typeBonPreparation}.
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
	 * @return {@link #causeNonlivraison}.
	 */
	public String getCauseNonlivraison() {
		return causeNonlivraison;
	}

	/**
	 * @param causeNonlivraison
	 *            {@link #causeNonlivraison}.
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
	 * Testet si un BP est preparer.
	 * 
	 * @return true si BP est preparer.
	 */
	public boolean isPreparer() {
		return getStatut().equals(StatusBonPreparation.PREPARER);
	}

	/**
	 * Tester si le BP est en cours de preparation.
	 * 
	 * @return true si le BP est en cours de preparation.
	 */
	@JsonIgnore
	public boolean isEnCoursPreparation() {
		return getStatut().equals(StatusBonPreparation.EN_COURS_PREPARATION);
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
	@JsonIgnore
	public boolean isAnnule() {
		return getStatut().equals(StatusBonPreparation.ANNULER);
	}

	/**
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
	 * @param json
	 *            l'objet {@link BonPreparation} en format json.
	 * @return {@link BonPreparation}
	 * @throws IOException
	 *             {@link IOException}
	 */
	public static BonPreparation fromJsonToBonPreparation(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(json, BonPreparation.class);
	}

	/**
	 * 
	 * @param bonPreparation
	 *            le {@link BonPreparation} a transforme en objet {@link JSONObject}
	 * @return {@link JSONObject}
	 * @throws IOException
	 *             {@link IOException}
	 */
	public static String toJson(BonPreparation bonPreparation) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(bonPreparation);

	}

	/**
	 * 
	 * @param bonPreparations
	 *            collection de {@link BonPreparation}
	 * @return la colllection de {@link BonPreparation} en format json.
	 * @throws IOException
	 *             {@link IOException}
	 */
	public static String toJsonArray(Collection<BonPreparation> bonPreparations) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(bonPreparations);
	}

}