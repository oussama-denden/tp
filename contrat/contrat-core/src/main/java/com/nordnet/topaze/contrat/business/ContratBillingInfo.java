package com.nordnet.topaze.contrat.business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;

import scala.util.parsing.json.JSONObject;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nordnet.topaze.client.rest.enums.TypeProduit;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.domain.ModeFacturation;
import com.nordnet.topaze.contrat.domain.ModePaiement;
import com.nordnet.topaze.contrat.domain.TypeContrat;
import com.nordnet.topaze.contrat.domain.TypeTVA;
import com.nordnet.topaze.contrat.util.DateSerializer;
import com.nordnet.topaze.contrat.util.ModeFacturationDeserializer;

/**
 * Cette classe regroupe les informations qui definissent un {@link ContratBillingInfo}.
 * 
 * @author akram-moncer
 * 
 */
public class ContratBillingInfo {

	/**
	 * reference du contrat global.
	 */
	private String referenceContrat;

	/**
	 * numero element contractuelle
	 */
	private Integer numEC;

	/**
	 * version du contrat associe.
	 */
	private Integer version;

	/**
	 * id du client.
	 */
	private String idClient;

	/**
	 * la reference du produit.
	 */
	private String referenceProduit;

	/**
	 * le ProductId que sera utilisé dans saphir.
	 */
	private String idProduit;

	/**
	 * Le champ "BillingGroup", lorsqu'il doit être rempli, le sera avec le numéro de la commande.
	 */
	private String numeroCommande;

	/**
	 * le titre du contrat.
	 */
	private String titre;

	/**
	 * Type d'operation= vente, location ou abonnement.
	 */
	private TypeContrat typeContrat;

	/**
	 * montant du produit.
	 */
	private double montant;

	/**
	 * engagement.
	 */
	private Integer engagement;

	/**
	 * durree du sous contrat.
	 */
	private Integer dureeContrat;

	/**
	 * periodicite d'un sous contrat.
	 */
	private Integer periodicite;

	/**
	 * mode de paiement.
	 */
	private ModePaiement modePaiement;

	/** The mode facturation. {@link ModeFacturation}. */
	@JsonDeserialize(using = ModeFacturationDeserializer.class)
	private ModeFacturation modeFacturation;

	/**
	 * liste des montants associe à un produit.
	 */
	private Set<Frais> frais;

	/** The type tva. {@link TypeTVA}. */
	private TypeTVA typeTVA;

	/**
	 * Par default le date debut de facturation est normalement 15 jours apres la validation de contrat. Mais il peut
	 * etre modifier manuellement.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateDebutFacturation;

	/**
	 * date du derniere facture.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateDerniereFacture;

	/**
	 * date de fin du contrat.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateFinContrat;

	/**
	 * reference de mode paiement.
	 */
	private String referenceModePaiement;

	/**
	 * contrat billing info parent.
	 */
	private ContratBillingInfo contratBillingInfoParent;

	/**
	 * La politique de validation.
	 */
	private PolitiqueValidation politiqueValidation;

	/**
	 * Politique de validation.
	 */
	private PolitiqueResiliationFacture politiqueResiliation;

	/**
	 * La politique de migration.
	 */
	private PolitiqueMigration politiqueMigration;

	/**
	 * {@link PolitiqueCession}.
	 */
	private PolitiqueCession politiqueCession;

	/**
	 * liste des {@link ReductionInfo} appliquer sur un {@link Contrat}.
	 */
	private List<ReductionInfo> reductionContrats = new ArrayList<>();

	/**
	 * {@link ReductionInfo} appliquer sur un {@link ElementContractuel}.
	 */
	private ReductionInfo reductionEC;

	/**
	 * la reduction appliquer pour les frais de 'MIGRATION'.
	 */
	private ReductionInfo reductionSurFraisMigration;

	/**
	 * un EL peut etre remboursable ou non.
	 */
	private Boolean remboursable;

	/**
	 * indique si un el est migre.
	 */
	private Boolean migre;

	/**
	 * indiquer si l'element est resilier lors de la migration.
	 */
	private Boolean resilie;

	/**
	 * indiquer si l'element est nouveau lors de la migration.
	 */
	private Boolean nouveau;

	/**
	 * Type de Produit BIEN ou SERVICE.
	 */
	private TypeProduit typeProduit;

	/**
	 * duree du sous contrat en mois.
	 */
	private Integer duree;

	/**
	 * date de fin engagement pour le contrat.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateFinEngagement;

	/**
	 * date de fin contrat pour le contrat.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateFinContratProbable;

	/**
	 * indisuer si on doit calculer le remboursement lors de la migration administratif.
	 */
	private boolean isCalculeRemboursementAdministratif;

	/**
	 * constructeur par defaut.
	 */
	public ContratBillingInfo() {
		super();
	}

	/**
	 * Gets the reference contrat.
	 * 
	 * @return {@link #referenceContrat}.
	 */
	public String getReferenceContrat() {
		return referenceContrat;
	}

	/**
	 * Sets the reference contrat.
	 * 
	 * @param referenceContrat
	 *            the new reference contrat {@link #referenceContrat}.
	 */
	public void setReferenceContrat(String referenceContrat) {
		this.referenceContrat = referenceContrat;
	}

	/**
	 * gets the numero element contractuelle.
	 * 
	 * @return {@link #numEC}
	 */
	public Integer getNumEC() {
		return numEC;
	}

	/**
	 * Sets the new numero element contractuelle.
	 * 
	 * @param numEC
	 *            the new {@link #numEC}
	 */
	public void setNumEC(Integer numEC) {
		this.numEC = numEC;
	}

	/**
	 * 
	 * @return {@link #version}.
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * 
	 * @param version
	 *            {@link #version}.
	 */
	public void setVersion(Integer version) {
		this.version = version;
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
	 * @return {@link #idProduit}.
	 */
	public String getIdProduit() {
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
	 * Gets the montant.
	 * 
	 * @return {@link #montant}.
	 */
	public double getMontant() {
		return montant;
	}

	/**
	 * Sets the montant.
	 * 
	 * @param montant
	 *            the new montant {@link #montant}.
	 */
	public void setMontant(double montant) {
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
	 * Gets the mode paiement.
	 * 
	 * @return {@link #modePaiement}.
	 */
	public ModePaiement getModePaiement() {
		return modePaiement;
	}

	/**
	 * Sets the mode paiement.
	 * 
	 * @param modePaiement
	 *            the new mode paiement {@link #modePaiement}.
	 */
	public void setModePaiement(ModePaiement modePaiement) {
		this.modePaiement = modePaiement;
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
	 * Gets the frais.
	 * 
	 * @return {@link #frais}.
	 */
	public Set<Frais> getFrais() {
		return frais;
	}

	/**
	 * Sets the frais.
	 * 
	 * @param frais
	 *            the new frais {@link #frais}.
	 */
	public void setFrais(Set<Frais> frais) {
		this.frais = frais;
	}

	/**
	 * Gets the type tva.
	 * 
	 * @return {@link TypeTVA}.
	 */
	public TypeTVA getTypeTVA() {
		return typeTVA;
	}

	/**
	 * Sets the type tva.
	 * 
	 * @param typeTVA
	 *            the new type tva {@link TypeTVA}.
	 */
	public void setTypeTVA(TypeTVA typeTVA) {
		this.typeTVA = typeTVA;
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
	 * From json to bon preparation.
	 * 
	 * @param json
	 *            l'objet {@link ContratBillingInfo} en format json.
	 * @return {@link ContratBillingInfo}
	 * @throws IOException
	 *             {@link IOException}
	 */
	public static ContratBillingInfo fromJsonToContratBillingInfo(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(json, ContratBillingInfo.class);
	}

	/**
	 * To json.
	 * 
	 * @param contratBillingInfo
	 *            le {@link ContratBillingInfo} a transforme en objet {@link JSONObject}
	 * @return {@link JSONObject}
	 * @throws IOException
	 *             {@link IOException}
	 */
	public static String toJson(ContratBillingInfo contratBillingInfo) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(contratBillingInfo);
	}

	/**
	 * To json array.
	 * 
	 * @param contratBillingInfos
	 *            collection de {@link ContratBillingInfo}.
	 * @return le vrachar representant la collection de {@link ContratBillingInfo}.
	 * @throws IOException
	 *             {@link IOException}.
	 */
	public static String toJsonArray(Collection<ContratBillingInfo> contratBillingInfos) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(contratBillingInfos);
	}

	/**
	 * Checks for parent.
	 * 
	 * @return true, if successful
	 */
	public boolean hasParent() {
		if (contratBillingInfoParent == null) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the contrat billing info parent.
	 * 
	 * @return the contrat billing info parent
	 */
	public ContratBillingInfo getContratBillingInfoParent() {
		return contratBillingInfoParent;
	}

	/**
	 * Sets the contrat billing info parent.
	 * 
	 * @param contratBillingInfoParent
	 *            the new contrat billing info parent
	 */
	public void setContratBillingInfoParent(ContratBillingInfo contratBillingInfoParent) {
		this.contratBillingInfoParent = contratBillingInfoParent;
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
	 * Gets the politique resiliation.
	 * 
	 * @return the politique resiliation
	 */
	public PolitiqueResiliationFacture getPolitiqueResiliation() {
		return politiqueResiliation;
	}

	/**
	 * Sets the politique resiliation.
	 * 
	 * @param politiqueResiliation
	 *            the new politique resiliation
	 */
	public void setPolitiqueResiliation(PolitiqueResiliationFacture politiqueResiliation) {
		this.politiqueResiliation = politiqueResiliation;
	}

	/**
	 * Gets the politique migration
	 * 
	 * @return migration
	 */
	public PolitiqueMigration getPolitiqueMigration() {
		return politiqueMigration;
	}

	/**
	 * Sets the politique migration
	 * 
	 * @param politiqueMigration
	 *            the new politique migration
	 */
	public void setPolitiqueMigration(PolitiqueMigration politiqueMigration) {
		this.politiqueMigration = politiqueMigration;
	}

	/**
	 * 
	 * @return {@link PolitiqueCession}.
	 */
	public PolitiqueCession getPolitiqueCession() {
		return politiqueCession;
	}

	/**
	 * 
	 * @param politiqueCession
	 *            {@link PolitiqueCession}.
	 */
	public void setPolitiqueCession(PolitiqueCession politiqueCession) {
		this.politiqueCession = politiqueCession;
	}

	/**
	 * @return {@link reductionContrats}.
	 */
	public List<ReductionInfo> getReductionContrats() {
		return reductionContrats;
	}

	/**
	 * @param reductionContrats
	 *            {@link #reductionContrats}.
	 */
	public void setReductionContrats(List<ReductionInfo> reductionContrats) {
		this.reductionContrats = reductionContrats;
	}

	/**
	 * 
	 * @param reductionContrat
	 *            {@link ReductionInfo}.
	 */
	public void addReductionContrat(ReductionInfo reductionContrat) {
		this.reductionContrats.add(reductionContrat);
	}

	/**
	 * @return {@link #reductionEC}.
	 */
	public ReductionInfo getReductionEC() {
		return reductionEC;
	}

	/**
	 * @param reductionEC
	 *            {@link #reductionEC}.
	 */
	public void setReductionEC(ReductionInfo reductionEC) {
		this.reductionEC = reductionEC;
	}

	/**
	 * 
	 * @return {@link #reductionSurFraisMigration}.
	 */
	public ReductionInfo getReductionSurFraisMigration() {
		return reductionSurFraisMigration;
	}

	/**
	 * 
	 * @param reductionSurFraisMigration
	 *            {@link #reductionSurFraisMigration}.
	 */
	public void setReductionSurFraisMigration(ReductionInfo reductionSurFraisMigration) {
		this.reductionSurFraisMigration = reductionSurFraisMigration;
	}

	/**
	 * 
	 * @return {@link #typeProduit} Type de Produit.
	 */
	public TypeProduit getTypeProduit() {
		return typeProduit;
	}

	/**
	 * 
	 * @param typeProduit
	 *            {@link #typeProduit} Type de Produit.
	 */
	public void setTypeProduit(TypeProduit typeProduit) {
		this.typeProduit = typeProduit;
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
	 * Gets date de fin engagement.
	 * 
	 * @return {@link #dateFinEngagement}.
	 */
	public Date getDateFinEngagement() {
		return dateFinEngagement;
	}

	/**
	 * Sets date de fin engagement
	 * 
	 * @param dateFinEngagment
	 *            the new date de fin engagement
	 */

	public void setDateFinEngagement(Date dateFinEngagement) {
		this.dateFinEngagement = dateFinEngagement;
	}

	/**
	 * 
	 * @return indique que l'element est remboursable ou non.
	 */
	public Boolean isRemboursable() {
		return remboursable;
	}

	/**
	 * set isMigre
	 * 
	 * @param remboursable
	 *            remboursable.
	 */
	public void setRemboursable(Boolean remboursable) {
		this.remboursable = remboursable;
	}

	/**
	 * 
	 * @return {@link #migre}.
	 */
	public Boolean isMigre() {
		return migre;
	}

	/**
	 * 
	 * @param migre
	 *            {@link #migre}.
	 */
	public void setMigre(Boolean migre) {
		this.migre = migre;
	}

	/**
	 * 
	 * @return {@link #resilie}.
	 */
	public Boolean isResilie() {
		return resilie;
	}

	/**
	 * 
	 * @param resilie
	 *            {@link #resilie}.
	 */
	public void setResilie(Boolean resilie) {
		this.resilie = resilie;
	}

	/**
	 * 
	 * @return {@link #nouveau}.
	 */
	public Boolean isNouveau() {
		return nouveau;
	}

	/**
	 * 
	 * @param nouveau
	 *            {@link #nouveau}.
	 */
	public void setNouveau(Boolean nouveau) {
		this.nouveau = nouveau;
	}

	/**
	 * get date fin contrat probable
	 * 
	 * @return {@link dateFinContratProbable}
	 */
	public Date getDateFinContratProbable() {
		return dateFinContratProbable;
	}

	/**
	 * set date fin contrat probalble
	 * 
	 * @param dateFinContratProbable
	 *            the new {@link dateFinContratProbable}
	 */
	public void setDateFinContratProbable(Date dateFinContratProbable) {
		this.dateFinContratProbable = dateFinContratProbable;
	}

	/**
	 * get the is calcule remboursement administratif
	 * 
	 * @return {@link #isCalculeRemboursementAdministratif}
	 */
	public boolean getIsCalculeRemboursementAdministratif() {
		return isCalculeRemboursementAdministratif;
	}

	/**
	 * set is calcule remboursement administratif
	 * 
	 * @param isCalculeRemboursementAdministratif
	 *            the new {@link #isCalculeRemboursementAdministratif}
	 */
	public void setIsCalculeRemboursementAdministratif(boolean isCalculeRemboursementAdministratif) {
		this.isCalculeRemboursementAdministratif = isCalculeRemboursementAdministratif;
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
		if (!(obj instanceof ContratBillingInfo)) {
			return false;
		}
		ContratBillingInfo rhs = (ContratBillingInfo) obj;
		return new EqualsBuilder().append(referenceContrat, rhs.referenceContrat).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(43, 11).append(referenceContrat).toHashCode();
	}

}