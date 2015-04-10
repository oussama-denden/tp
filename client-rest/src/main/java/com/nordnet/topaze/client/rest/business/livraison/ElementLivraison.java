package com.nordnet.topaze.client.rest.business.livraison;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.lang.model.element.TypeElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nordnet.topaze.client.rest.enums.OutilLivraison;
import com.nordnet.topaze.client.rest.enums.StatusBonPreparation;
import com.nordnet.topaze.client.rest.enums.TypeBonPreparation;
import com.nordnet.topaze.client.rest.enums.TypeContrat;
import com.nordnet.topaze.client.rest.enums.TypeProduit;

/**
 * contient tout les informations necessaire pour un element de livraison.
 * 
 * @author akram-moncer
 * 
 */
@JsonIgnoreProperties({ "bonPreparationParent", "optionInfos", "initier", "preparer", "livrer", "termine", "nonLivrer",
		"statut", "cede" })
public class ElementLivraison {

	/**
	 * reference de l'element de livraison.
	 */
	private String reference;

	/**
	 * le numero de EC.
	 */
	private Integer numEC;

	/**
	 * reference du produit.
	 */
	private String referenceProduit;

	/**
	 * reference ancien produit.
	 */
	private String referenceAncienProduit;

	/**
	 * nom court de produit destination par exemple: Satellite.Jet.
	 */
	private String referenceGammeDestination;

	/**
	 * nom court de produit source par exemple: Satellite.Jet.
	 */
	private String referenceGammeSource;

	/**
	 * retailerPackagerId.
	 */
	private String retailerPackagerId;

	/**
	 * ancien retailerPackagerId.
	 */
	private String ancienRetailerPackagerId;

	/**
	 * code du colis reçue par netdelivery.
	 */
	private String codeColis;

	/**
	 * ancien code du colis reçue par netdelivery.
	 */
	private String ancienCodeColis;

	/**
	 * address du livraison du produit.
	 */
	private String addresseLivraison;

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
	 * date de livraison depend du {@link #typeBonPreparation}.
	 */
	private Date dateLivraisonTermine;

	/**
	 * date de retour depend du {@link #typeBonPreparation}.
	 */
	private Date dateRetourTermine;

	/**
	 * type du bon de preparation.
	 */
	private TypeBonPreparation typeBonPreparation;

	/**
	 * type du contrat lie au bon de preparation.
	 */
	private TypeContrat typeContrat;

	/**
	 * {@link TypeElement}.
	 */
	private TypeProduit typeElement;

	/**
	 * l'outil de livraison du produit.
	 */
	private OutilLivraison acteur;

	/**
	 * Cause de non livrason.
	 */
	private String causeNonlivraison;

	/**
	 * le bon de preparation parent.
	 */
	private BonPreparation bonPreparationParent;

	/**
	 * element de livraison parent.
	 */
	private ElementLivraison elementLivraisonParent;

	/**
	 * les informations sur touts les option que contient cette element parmi ses fils.
	 */
	private List<ElementContractuelInfo> optionInfos = new ArrayList<>();

	/**
	 * indiquer si l'EL est annule ou non.
	 */
	private boolean annule;

	/**
	 * Constructeur par defaut.
	 */
	public ElementLivraison() {

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
	 * @return {@link #numEC}.
	 */
	public Integer getNumEC() {
		return numEC;
	}

	/**
	 * @param numEC
	 *            {@link #numEC}.
	 */
	public void setNumEC(Integer numEC) {
		this.numEC = numEC;
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
	 * @return {@link #referenceAncienProduit}.
	 */
	public String getReferenceAncienProduit() {
		return referenceAncienProduit;
	}

	/**
	 * @param referenceAncienProduit
	 *            {@link #referenceAncienProduit}.
	 */
	public void setReferenceAncienProduit(String referenceAncienProduit) {
		this.referenceAncienProduit = referenceAncienProduit;
	}

	/**
	 * Gets the reference gamme destination.
	 * 
	 * @return the reference gamme destination
	 */
	public String getReferenceGammeDestination() {
		return referenceGammeDestination;
	}

	/**
	 * Sets the reference gamme destination.
	 * 
	 * @param referenceGammeDestination
	 *            the new reference gamme destination
	 */
	public void setReferenceGammeDestination(String referenceGammeDestination) {
		this.referenceGammeDestination = referenceGammeDestination;
	}

	/**
	 * Gets the reference gamme source.
	 * 
	 * @return the reference gamme source
	 */
	public String getReferenceGammeSource() {
		return referenceGammeSource;
	}

	/**
	 * Sets the reference gamme source.
	 * 
	 * @param referenceGammeSource
	 *            the new reference gamme source
	 */
	public void setReferenceGammeSource(String referenceGammeSource) {
		this.referenceGammeSource = referenceGammeSource;
	}

	/**
	 * 
	 * @return {@link #retailerPackagerId}.
	 */
	public String getRetailerPackagerId() {
		return retailerPackagerId;
	}

	/**
	 * 
	 * @param retailerPackagerId
	 *            {@link #retailerPackagerId}.
	 */
	public void setRetailerPackagerId(String retailerPackagerId) {
		this.retailerPackagerId = retailerPackagerId;
	}

	/**
	 * 
	 * @return {@link #ancienRetailerPackagerId}.
	 */
	public String getAncienRetailerPackagerId() {
		return ancienRetailerPackagerId;
	}

	/**
	 * 
	 * @param ancienRetailerPackagerId
	 *            {@link #ancienRetailerPackagerId}.
	 */
	public void setAncienRetailerPackagerId(String ancienRetailerPackagerId) {
		this.ancienRetailerPackagerId = ancienRetailerPackagerId;
	}

	/**
	 * @return {@link #codeColis}.
	 */
	public String getCodeColis() {
		return codeColis;
	}

	/**
	 * @param codeColis
	 *            {@link #codeColis}.
	 */
	public void setCodeColis(String codeColis) {
		this.codeColis = codeColis;
	}

	/**
	 * @return {@link #ancienCodeColis}.
	 */
	public String getAncienCodeColis() {
		return ancienCodeColis;

	}

	/**
	 * @param ancienCodeColis
	 *            {@link #ancienCodeColis}.
	 */
	public void setAncienCodeColis(String ancienCodeColis) {
		this.ancienCodeColis = ancienCodeColis;
	}

	/**
	 * @return {@link #addresseLivraison}.
	 */
	public String getAddresseLivraison() {
		return addresseLivraison;
	}

	/**
	 * @param addresseLivraison
	 *            {@link #addresseLivraison}.
	 */
	public void setAddresseLivraison(String addresseLivraison) {
		this.addresseLivraison = addresseLivraison;
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
	 * @return {@link #dateDebutPreparation}
	 */
	public Date getDateDebutPreparation() {
		return dateDebutPreparation;
	}

	/**
	 * @param dateDebutPreparation
	 *            {@link #dateDebutPreparation}
	 */
	public void setDateDebutPreparation(Date dateDebutPreparation) {
		this.dateDebutPreparation = dateDebutPreparation;
	}

	/**
	 * @return {@link #dateLivraisonTermine}.
	 */
	public Date getDateLivraisonTermine() {
		return dateLivraisonTermine;
	}

	/**
	 * @param dateLivraisonTermine
	 *            {@link #dateLivraisonTermine}.
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
	 * @return {@link #typeElement}.
	 */
	public TypeProduit getTypeElement() {
		return typeElement;
	}

	/**
	 * @param typeElement
	 *            {@link #typeElement}.
	 */
	public void setTypeElement(TypeProduit typeElement) {
		this.typeElement = typeElement;
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
	 * @return {@link #typeContrat}
	 */
	public TypeContrat getTypeContrat() {
		return typeContrat;
	}

	/**
	 * 
	 * @param typeContrat
	 *            {@link #typeContrat}
	 */
	public void setTypeContrat(TypeContrat typeContrat) {
		this.typeContrat = typeContrat;
	}

	/**
	 * 
	 * @return {@link #acteur}.
	 */
	public OutilLivraison getActeur() {
		return acteur;
	}

	/**
	 * @param acteur
	 *            {@link #acteur}.
	 */
	public void setActeur(OutilLivraison acteur) {
		this.acteur = acteur;
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
	 * @return {@link #bonPreparationParent}.
	 */
	public BonPreparation getBonPreparationParent() {
		return bonPreparationParent;
	}

	/**
	 * @param bonPreparationParent
	 *            {@link #bonPreparationParent}.
	 */
	public void setBonPreparationParent(BonPreparation bonPreparationParent) {
		this.bonPreparationParent = bonPreparationParent;
	}

	/**
	 * @return {@link #elementLivraisonParent}.
	 */
	public ElementLivraison getElementLivraisonParent() {
		return elementLivraisonParent;
	}

	/**
	 * @param elementLivraisonParent
	 *            {@link #elementLivraisonParent}.
	 */
	public void setElementLivraisonParent(ElementLivraison elementLivraisonParent) {
		this.elementLivraisonParent = elementLivraisonParent;
	}

	/**
	 * @return {@link #optionInfos}.
	 */
	public List<ElementContractuelInfo> getOptionInfos() {
		return optionInfos;
	}

	/**
	 * @param optionInfos
	 *            {@link #optionInfos}.
	 */
	public void setOptionInfos(List<ElementContractuelInfo> optionInfos) {
		this.optionInfos = optionInfos;
	}

	/**
	 * Testet si un EL est initier.
	 * 
	 * @return true si EL est initier.
	 */
	public boolean isInitier() {
		return getStatut().equals(StatusBonPreparation.INITIER);
	}

	/**
	 * Testet si un EL est preparer.
	 * 
	 * @return true si EL est preparer.
	 */
	public boolean isPreparer() {
		return getStatut().equals(StatusBonPreparation.PREPARER);
	}

	/**
	 * Testet si un EL est Termine.
	 * 
	 * @return true si EL est Termine.
	 */
	public boolean isTermine() {
		return getStatut().equals(StatusBonPreparation.TERMINER);
	}

	/**
	 * Testet si un EL est non livrer.
	 * 
	 * @return true si EL est non livrer.
	 */
	public boolean isNonLivrer() {
		return getStatut().equals(StatusBonPreparation.NON_LIVRER);
	}

	/**
	 * 
	 * @return {@link #annule}.
	 */
	@JsonIgnore
	public boolean isAnnule() {
		return annule;
	}

	/**
	 * 
	 * @param annule
	 *            {@link #annule}.
	 */
	public void setAnnule(final boolean annule) {
		this.annule = annule;
	}

	/**
	 * @return {@link #status}.
	 */
	public StatusBonPreparation getStatut() {
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

		if (dateInitiation != null) {
			return StatusBonPreparation.INITIER;
		}
		return null;
	}

	/**
	 * 
	 * @param json
	 *            l'objet {@link ElementLivraison} en format json.
	 * @return {@link ElementLivraison}
	 * @throws IOException
	 *             {@link IOException}
	 */
	public static ElementLivraison fromJsonToElementLivraison(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, ElementLivraison.class);
	}

	/**
	 * 
	 * @param elementLivraison
	 *            le {@link ElementLivraison} a transforme en objet {@link JSONObject}.
	 * @return {@link JSONObject}.
	 * @throws IOException
	 *             {@link IOException}.
	 */
	public static String toJson(ElementLivraison elementLivraison) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(elementLivraison);

	}

	/**
	 * @param elementLivraisons
	 *            collection de {@link ElementLivraison}.
	 * @return la colllection de {@link ElementLivraison} en format json.
	 * @throws IOException
	 *             {@link IOException}.
	 */
	public static String toJsonArray(Collection<ElementLivraison> elementLivraisons) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(elementLivraisons);
	}

	/**
	 * verifier si un element de livraison est cede ou non.
	 * 
	 * @return true si l'Element de livraison est cede.
	 */
	public boolean isCede() {
		if (dateLivraisonTermine != null) {
			return true;
		}
		return false;
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
		if (!(obj instanceof ElementLivraison)) {
			return false;
		}
		ElementLivraison rhs = (ElementLivraison) obj;
		return new EqualsBuilder().append(numEC, rhs.numEC).append(reference, rhs.reference)
				.append(referenceProduit, rhs.referenceProduit)
				.append(referenceGammeDestination, rhs.referenceGammeDestination)
				.append(referenceGammeSource, rhs.referenceGammeSource).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(43, 11).append(numEC).append(reference).append(referenceProduit)
				.append(referenceGammeDestination).append(referenceGammeSource).toHashCode();
	}
}