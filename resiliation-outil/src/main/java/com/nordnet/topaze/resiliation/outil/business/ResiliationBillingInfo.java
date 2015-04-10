package com.nordnet.topaze.resiliation.outil.business;

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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nordnet.topaze.resiliation.outil.enums.ModeFacturation;
import com.nordnet.topaze.resiliation.outil.enums.TypeContrat;
import com.nordnet.topaze.resiliation.outil.enums.TypeFrais;
import com.nordnet.topaze.resiliation.outil.enums.TypeProduit;

/**
 * Les information necaissaire pour faire le calcule les montant que le client doit payer.
 * 
 * @author Oussama Denden
 * 
 */
public class ResiliationBillingInfo {

	/**
	 * reference du contrat global.
	 */
	private String referenceContrat;

	/**
	 * numero element contractuelle.
	 */
	private Integer numEC;

	/**
	 * version du contrat associe.
	 */
	private Integer version;

	/**
	 * periodicite d'un sous contrat.
	 */
	private Integer periodicite;

	/**
	 * engagement.
	 */
	private Integer engagement;

	/**
	 * Par default le date debut de facturation est normalement 15 jours apres la validation de contrat. Mais il peut
	 * etre modifier manuellement.
	 */
	private Date dateDebutFacturation;

	/**
	 * montant du produit.
	 */
	private Double montant;

	/**
	 * The mode facturation. {@link ModeFacturation}.
	 */
	private ModeFacturation modeFacturation;

	/**
	 * date de fin du contrat.
	 */
	private Date dateFinContrat;

	/**
	 * durree du sous contrat.
	 */
	private Integer dureeContrat;

	/**
	 * date du derniere facture.
	 */
	private Date dateDerniereFacture;

	/**
	 * la reference du produit.
	 */
	private String referenceProduit;

	/**
	 * liste des {@link Frais}.
	 */
	private List<Frais> frais = new ArrayList<>();

	/**
	 * indique que l'element est remboursable ou non.
	 */
	private Boolean remboursable;

	/**
	 * Reduction sur les frais contrat.
	 */
	private List<ReductionInfo> reductionInfoContrats = new ArrayList<>();

	/**
	 * Reduction sur les frais element contractuel.
	 */
	private ReductionInfo reductionInfoEC;

	/**
	 * date de fin engagement pour le contrat.
	 */
	private Date dateFinEngagement;

	/**
	 * contrat billing info parent.
	 */
	private ResiliationBillingInfo resiliationBillingInfoParent;

	/**
	 * {@link ECResiliationInfo} associe.
	 */
	private ECResiliationInfo resiliationInfo;

	/**
	 * {@link TypeElement}.
	 */
	private TypeProduit typeElement;

	/**
	 * Type d'operation= vente, location ou abonnement.
	 */
	private TypeContrat typeContrat;

	/**
	 * date de validation du contrat.
	 */
	private Date dateValidation;

	/**
	 * constructeur par defaut.
	 */
	public ResiliationBillingInfo() {

	}

	/**
	 * @return true si l'elemenContractuel est parent.
	 */
	@JsonIgnore
	public boolean isParent() {
		if (resiliationBillingInfoParent == null) {
			return true;
		}
		return false;
	}

	/**
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
	 * the numero element contractuelle.
	 * 
	 * @return {@link #numEC}
	 */
	public Integer getNumEC() {
		return numEC;
	}

	/**
	 * sets the numero element contractuelle.
	 * 
	 * @param numEC
	 *            {@link #numEC}
	 */
	public void setNumEC(Integer numEC) {
		this.numEC = numEC;
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
	 * Gets the engagement.
	 * 
	 * @return {@link #engagement}.
	 */
	public Integer getEngagement() {
		return engagement;
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
	 * Gets the duree contrat.
	 * 
	 * @return {@link #dureeContrat}.
	 */
	public Integer getDureeContrat() {
		return dureeContrat;
	}

	/**
	 * Sets the duree contrat.
	 * 
	 * @param dureeContrat
	 *            the new duree contrat {@link #dureeContrat}.
	 */
	public void setDureeContrat(Integer dureeContrat) {
		this.dureeContrat = dureeContrat;
	}

	/**
	 * Gets the periodicite.
	 * 
	 * @return {@link #periodicite}.
	 */
	public Integer getPeriodicite() {
		return periodicite;
	}

	/**
	 * Sets the periodicite.
	 * 
	 * @param periodicite
	 *            the new periodicite {@link #periodicite}.
	 */
	public void setPeriodicite(Integer periodicite) {
		this.periodicite = periodicite;
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
	 * @return {@link #modeFacturation}.
	 */
	public ModeFacturation getModeFacturation() {
		return modeFacturation;
	}

	/**
	 * @param modeFacturation
	 *            {@link #modeFacturation}.
	 */
	public void setModeFacturation(ModeFacturation modeFacturation) {
		this.modeFacturation = modeFacturation;
	}

	/**
	 * @return {@link #frais}.
	 */
	public List<Frais> getFrais() {
		return frais;
	}

	/**
	 * @param frais
	 *            {@link #frais}.
	 */
	public void setFrais(List<Frais> frais) {
		this.frais = frais;
	}

	/**
	 * 
	 * @return {@link #dateValidation}
	 */
	public Date getDateValidation() {
		return dateValidation;
	}

	/**
	 * 
	 * @param dateValidation
	 *            {@link #dateValidation}
	 */
	public void setDateValidation(Date dateValidation) {
		this.dateValidation = dateValidation;
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
		if (!(obj instanceof ResiliationBillingInfo)) {
			return false;
		}
		ResiliationBillingInfo rhs = (ResiliationBillingInfo) obj;
		return new EqualsBuilder().append(referenceProduit, rhs.referenceProduit).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(43, 11).append(referenceProduit).toHashCode();
	}

	/**
	 * From json to bon preparation.
	 * 
	 * @param json
	 *            l'objet {@link ResiliationBillingInfo} en format json.
	 * @return {@link ResiliationBillingInfo}
	 * @throws IOException
	 *             {@link IOException}
	 */
	public static ResiliationBillingInfo fromJsonToResiliationBillingInfo(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, ResiliationBillingInfo.class);
	}

	/**
	 * From json String to array of {@link ResiliationBillingInfo}.
	 * 
	 * @param json
	 *            Collection de {@link ResiliationBillingInfo} en format json.
	 * @return tableau de {@link ResiliationBillingInfo}.
	 * @throws IOException
	 *             {@link IOException}.
	 */
	public static ResiliationBillingInfo[] fromJsonToResiliationBillingInfoArray(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, ResiliationBillingInfo[].class);
	}

	/**
	 * To json.
	 * 
	 * @param resiliationBillingInfo
	 *            le {@link ResiliationBillingInfo} a transforme en objet {@link JSONObject}
	 * @return {@link JSONObject}.
	 * @throws JsonProcessingException
	 *             {@link JsonProcessingException}.
	 */
	public static String toJson(ResiliationBillingInfo resiliationBillingInfo) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(resiliationBillingInfo);
	}

	/**
	 * To json array.
	 * 
	 * @param resiliationBillingInfos
	 *            collection de {@link ResiliationBillingInfo}
	 * @return la colllection de {@link ResiliationBillingInfo} en format json.
	 * @throws JsonProcessingException
	 *             {@link JsonProcessingException}
	 */
	public static String toJsonArray(Collection<ResiliationBillingInfo> resiliationBillingInfos)
			throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(resiliationBillingInfos);
	}

	/**
	 * 
	 * @return list des {@link Frais} de resiliation associe au produit.
	 */
	public List<Frais> getFraisResiliations() {
		List<Frais> fraisResiliation = new ArrayList<>();
		for (Frais frais : this.frais) {
			if (frais.getTypeFrais().equals(TypeFrais.RESILIATION)) {
				fraisResiliation.add(frais);
			}
		}
		return fraisResiliation;
	}

	/**
	 * @return {@link #remboursable}.
	 */
	public Boolean isRemboursable() {
		return remboursable;
	}

	/**
	 * @param remboursable
	 *            {@link #remboursable}.
	 */
	public void setRemboursable(Boolean remboursable) {
		this.remboursable = remboursable;
	}

	/**
	 * 
	 * @return {@link #reductionInfoContrats}
	 */
	public List<ReductionInfo> getReductionInfoContrats() {
		return reductionInfoContrats;
	}

	/**
	 * 
	 * @param reductionInfoContrats
	 *            {@link #reductionInfoContrats}
	 */
	public void setReductionInfoContrats(List<ReductionInfo> reductionInfoContrats) {
		this.reductionInfoContrats = reductionInfoContrats;
	}

	/**
	 * @return {@link #reductionInfoEC}.
	 */
	public ReductionInfo getReductionInfoEC() {
		return reductionInfoEC;
	}

	/**
	 * @param reductionInfoEC
	 *            {@link #reductionInfoEC}.
	 */
	public void setReductionInfoEC(ReductionInfo reductionInfoEC) {
		this.reductionInfoEC = reductionInfoEC;
	}

	/**
	 * @return {@link #dateFinEngagment}.
	 */
	public Date getDateFinEngagement() {
		return dateFinEngagement;
	}

	/**
	 * @param dateFinEngagement
	 *            {@link #dateFinEngagment}.
	 */
	public void setDateFinEngagement(Date dateFinEngagement) {
		this.dateFinEngagement = dateFinEngagement;
	}

	/**
	 * @return {@link #resiliationBillingInfoParent}.
	 */
	public ResiliationBillingInfo getResiliationBillingInfoParent() {
		return resiliationBillingInfoParent;
	}

	/**
	 * @param resiliationBillingInfoParent
	 *            {@link #resiliationBillingInfoParent}.
	 */
	public void setResiliationBillingInfoParent(ResiliationBillingInfo resiliationBillingInfoParent) {
		this.resiliationBillingInfoParent = resiliationBillingInfoParent;
	}

	/**
	 * @return {@link #resiliationInfo}.
	 */
	public ECResiliationInfo getResiliationInfo() {
		if (resiliationInfo == null) {
			resiliationInfo = new ECResiliationInfo();
			resiliationInfo.setReferenceProduit(referenceProduit);
			resiliationInfo.setReferenceContrat(referenceContrat);
		}
		return resiliationInfo;
	}

	/**
	 * @param resiliationInfo
	 *            {@link #resiliationInfo}.
	 */
	public void setResiliationInfo(ECResiliationInfo resiliationInfo) {
		this.resiliationInfo = resiliationInfo;
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
	 * Gets the type contrat.
	 * 
	 * @return {@link TypeContrat}.
	 */
	public TypeContrat getTypeContrat() {
		return typeContrat;
	}

	/**
	 * Sets the type contrat.
	 * 
	 * @param typeContrat
	 *            the new type contrat {@link TypeContrat}.
	 */
	public void setTypeContrat(TypeContrat typeContrat) {
		this.typeContrat = typeContrat;
	}

	/**
	 * Checks for parent.
	 * 
	 * @return true, if successful
	 */
	public boolean hasParent() {
		if (resiliationBillingInfoParent == null) {
			return false;
		}
		return true;
	}

}