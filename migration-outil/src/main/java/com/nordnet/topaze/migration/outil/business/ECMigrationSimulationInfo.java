package com.nordnet.topaze.migration.outil.business;

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
import com.nordnet.topaze.migration.outil.enums.ModeFacturation;
import com.nordnet.topaze.migration.outil.enums.TypeContrat;
import com.nordnet.topaze.migration.outil.enums.TypeFrais;
import com.nordnet.topaze.migration.outil.enums.TypeOperation;
import com.nordnet.topaze.migration.outil.enums.TypeProduit;
import com.nordnet.topaze.migration.outil.enums.TypeTVA;

/**
 * Information de resiliation d'un ElementContractuel.
 * 
 * @author Denden Oussama
 * 
 */
public class ECMigrationSimulationInfo {

	/**
	 * reference du contrat global.
	 */
	private String referenceProduit;

	/**
	 * Numero de l' element Contractuel.
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
	 * {@link TypeTVA}.
	 */
	private TypeTVA typeTVA;

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
	 * liste des {@link Frais}.
	 */
	private List<FraisMigrationSimulation> frais = new ArrayList<>();

	/**
	 * indique que l'element est remboursable ou non.
	 */
	private Boolean remboursable;

	/**
	 * Reduction sur les frais contrat.
	 */
	private ReductionInfo reductionInfoContrat;

	/**
	 * Reduction sur les frais element contractuel.
	 */
	private ReductionInfo reductionInfoEC;

	/**
	 * la reduction appliquer pour les frais de 'MIGRATION'.
	 */
	private ReductionInfo reductionSurFraisMigration;

	/**
	 * date de fin engagement pour le contrat.
	 */
	private Date dateFinEngagement;

	/**
	 * {@link TypeElement}.
	 */
	private TypeProduit typeElement;

	/**
	 * Type d'operation= vente, location ou abonnement.
	 */
	private TypeContrat typeContrat;

	/**
	 * contrat billing info parent.
	 */
	private ECMigrationSimulationInfo ecParentInfo;

	/**
	 * {@link ResultatSimulationEC}.
	 */
	private ResultatSimulationEC ecSimulationResultat;

	/**
	 * constructeur par defaut.
	 */
	public ECMigrationSimulationInfo() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public String toString() {
		return "ECMigrationSimulationInfo [referenceProduit=" + referenceProduit + ", numEC=" + numEC
				+ ", periodicite=" + periodicite + ", engagement=" + engagement + ", dateDebutFacturation="
				+ dateDebutFacturation + ", montant=" + montant + ", modeFacturation=" + modeFacturation
				+ ", dateFinContrat=" + dateFinContrat + ", dureeContrat=" + dureeContrat + ", dateDerniereFacture="
				+ dateDerniereFacture + ", frais=" + frais + ", remboursable=" + remboursable
				+ ", reductionInfoContrat=" + reductionInfoContrat + ", reductionInfoEC=" + reductionInfoEC
				+ ", reductionSurFraisMigration=" + reductionSurFraisMigration + ", dateFinEngagement="
				+ dateFinEngagement + ", typeElement=" + typeElement + ", typeContrat=" + typeContrat
				+ ", ecParentInfo=" + ecParentInfo + ", ecSimulationResultat=" + ecSimulationResultat + "]";
	}

	/**
	 * Gets the reference produit.
	 * 
	 * @return the reference produit
	 */
	public String getReferenceProduit() {
		return referenceProduit;
	}

	/**
	 * Sets the reference produit.
	 * 
	 * @param referenceProduit
	 *            the new reference produit
	 */
	public void setReferenceProduit(String referenceProduit) {
		this.referenceProduit = referenceProduit;
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
	 * @return true si l'elemenContractuel est parent.
	 */
	@JsonIgnore
	public boolean isParent() {
		if (ecParentInfo == null) {
			return true;
		}
		return false;
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
	 * @return {@link #typeTVA}.
	 */
	public TypeTVA getTypeTVA() {
		return typeTVA;
	}

	/**
	 * @param typeTVA
	 *            {@link #typeTVA}.
	 */
	public void setTypeTVA(TypeTVA typeTVA) {
		this.typeTVA = typeTVA;
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
	public List<FraisMigrationSimulation> getFrais() {
		return frais;
	}

	/**
	 * @param frais
	 *            {@link #frais}.
	 */
	public void setFrais(List<FraisMigrationSimulation> frais) {
		this.frais = frais;
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
		if (!(obj instanceof ECMigrationSimulationInfo)) {
			return false;
		}
		ECMigrationSimulationInfo rhs = (ECMigrationSimulationInfo) obj;
		return new EqualsBuilder().append(referenceProduit, rhs.referenceProduit).append(numEC, rhs.numEC).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(43, 11).append(referenceProduit).append(numEC).toHashCode();
	}

	/**
	 * From json to bon preparation.
	 * 
	 * @param json
	 *            l'objet {@link ECMigrationSimulationInfo} en format json.
	 * @return {@link ECMigrationSimulationInfo}
	 * @throws IOException
	 *             {@link IOException}
	 */
	public static ECMigrationSimulationInfo fromJsonToResiliationBillingInfo(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, ECMigrationSimulationInfo.class);
	}

	/**
	 * From json String to array of {@link ECMigrationSimulationInfo}.
	 * 
	 * @param json
	 *            Collection de {@link ECMigrationSimulationInfo} en format json.
	 * @return tableau de {@link ECMigrationSimulationInfo}.
	 * @throws IOException
	 *             {@link IOException}.
	 */
	public static ECMigrationSimulationInfo[] fromJsonToResiliationBillingInfoArray(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, ECMigrationSimulationInfo[].class);
	}

	/**
	 * To json.
	 * 
	 * @param elementContractuelInfo
	 *            le {@link ECMigrationSimulationInfo} a transforme en objet {@link JSONObject}
	 * @return {@link JSONObject}.
	 * @throws JsonProcessingException
	 *             {@link JsonProcessingException}.
	 */
	public static String toJson(ECMigrationSimulationInfo elementContractuelInfo) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(elementContractuelInfo);
	}

	/**
	 * To json array.
	 * 
	 * @param elementsContractuelInfo
	 *            collection de {@link ECMigrationSimulationInfo}
	 * @return la colllection de {@link ECMigrationSimulationInfo} en format json.
	 * @throws JsonProcessingException
	 *             {@link JsonProcessingException}
	 */
	public static String toJsonArray(Collection<ECMigrationSimulationInfo> elementsContractuelInfo)
			throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(elementsContractuelInfo);
	}

	/**
	 * 
	 * @return list des {@link Frais} de resiliation associe au produit.
	 */
	public List<FraisMigrationSimulation> getFraisResiliations() {
		List<FraisMigrationSimulation> fraisResiliation = new ArrayList<>();
		for (FraisMigrationSimulation frais : this.frais) {
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
		return remboursable != null && remboursable ? remboursable : false;
	}

	/**
	 * @param remboursable
	 *            {@link #remboursable}.
	 */
	public void setRemboursable(Boolean remboursable) {
		this.remboursable = remboursable;
	}

	/**
	 * @return {@link #reductionInfoEC}.
	 */
	public ReductionInfo getReductionInfoContrat() {
		return reductionInfoContrat;
	}

	/**
	 * @param reductionInfoContrat
	 *            {@link #reductionInfoContrat}.
	 */
	public void setReductionInfoContrat(ReductionInfo reductionInfoContrat) {
		this.reductionInfoContrat = reductionInfoContrat;
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
	 * @return {@link #reductionSurFraisMigration}.
	 */
	public ReductionInfo getReductionSurFraisMigration() {
		return reductionSurFraisMigration;
	}

	/**
	 * @param reductionSurFraisMigration
	 *            {@link #reductionSurFraisMigration}.
	 */
	public void setReductionSurFraisMigration(ReductionInfo reductionSurFraisMigration) {
		this.reductionSurFraisMigration = reductionSurFraisMigration;
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
	 * @return {@link #ecParentInfo}.
	 */
	public ECMigrationSimulationInfo getEcParentInfo() {
		return ecParentInfo;
	}

	/**
	 * @param ecParentInfo
	 *            {@link #ecParentInfo}.
	 */
	public void setEcParentInfo(ECMigrationSimulationInfo ecParentInfo) {
		this.ecParentInfo = ecParentInfo;
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
		if (ecParentInfo == null) {
			return false;
		}
		return true;
	}

	/**
	 * @return {@link #ecSimulationResultat}.
	 */
	public ResultatSimulationEC getEcSimulationResultat() {
		if (ecSimulationResultat == null) {
			ecSimulationResultat = new ResultatSimulationEC();
			ecSimulationResultat.setReferenceProduit(referenceProduit);
			ecSimulationResultat.setNumEC(numEC);
		}
		return ecSimulationResultat;
	}

	/**
	 * @param ecSimulationResultat
	 *            {@link #ecSimulationResultat}.
	 */
	public void setEcSimulationResultat(ResultatSimulationEC ecSimulationResultat) {
		this.ecSimulationResultat = ecSimulationResultat;
	}

	/**
	 * Genere L'element de retour.
	 * 
	 * @return {@link RetourInfo}.
	 */
	public RetourInfo getRetourInfo() {
		RetourInfo retourInfo = new RetourInfo();
		retourInfo.setReferenceProduit(referenceProduit);
		retourInfo.setTypeElement(typeElement);
		if (typeElement.equals(TypeProduit.BIEN)) {
			retourInfo.setTypeOperation(TypeOperation.RETOUR);
		} else if (typeElement.equals(TypeProduit.SERVICE)) {
			retourInfo.setTypeOperation(TypeOperation.SUSPENSION);
		}
		return retourInfo;
	}

	/**
	 * Genere les informations d'echange.
	 * 
	 * @param referenceProduitDestination
	 *            reference produit destination.
	 * @return {@link EchangeInfo}.
	 */
	public EchangeInfo getEchangeInfo(String referenceProduitDestination) {
		EchangeInfo echangeInfo = new EchangeInfo();
		echangeInfo.setReferenceProduitDestination(referenceProduitDestination);
		echangeInfo.setReferenceProduitSource(referenceProduit);
		echangeInfo.setTypeElement(typeElement);
		echangeInfo.setTypeOperation(TypeOperation.ECHANGE);
		return echangeInfo;
	}

	/**
	 * Genere l'element de livraison.
	 * 
	 * @return {@link RetourInfo}.
	 */
	public RetourInfo getLivraisonInfo() {
		RetourInfo retourInfo = new RetourInfo();
		retourInfo.setReferenceProduit(referenceProduit);
		retourInfo.setTypeElement(typeElement);
		retourInfo.setTypeOperation(TypeOperation.LIVRAISON);

		return retourInfo;
	}

}