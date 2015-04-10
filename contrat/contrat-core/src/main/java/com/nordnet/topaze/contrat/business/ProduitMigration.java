package com.nordnet.topaze.contrat.business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nordnet.topaze.client.rest.enums.TypeProduit;
import com.nordnet.topaze.contrat.domain.ModePaiement;
import com.nordnet.topaze.contrat.util.ModePaiementDeserializer;
import com.nordnet.topaze.contrat.util.TypeProduitDeserializer;

/**
 * Cette classe regroupe les informations qui definissent un {@link ProduitMigration}.
 * 
 * @author anisselmane.
 * 
 */
public class ProduitMigration {

	/**
	 * Numero de l' element Contractuel.
	 */
	private Integer numEC;

	/**
	 * nom court de produit destination par exemple: SatMax10.
	 */
	private String referenceProduitDestination;

	/**
	 * nom court de produit source par exemple: SatMax10.
	 */
	private String referenceProduitSource;

	/**
	 * nom court de produit destination par exemple: Satellite.Jet.
	 */
	private String referenceGammeDestination;

	/**
	 * nom court de produit source par exemple: Satellite.Jet.
	 */
	private String referenceGammeSource;

	/**
	 * le nom de {@link ProduitMigration} par exemple: Pack Sat Max 10Go.
	 */
	private String label;

	/**
	 * Le champ "BillingGroup", lorsqu'il doit être rempli, le sera avec le numéro de la commande.
	 */
	private String numeroCommande;

	/**
	 * The type produit. {@link TypeProduit} .
	 */
	@JsonDeserialize(using = TypeProduitDeserializer.class)
	private TypeProduit typeProduit;

	/**
	 * The prix. {@link PrixMigration}.
	 */
	private PrixMigration prix;

	/**
	 * La Reduction par produit {@link ReductionMigration}.
	 */
	private List<ReductionMigration> reductions = new ArrayList<>();

	/**
	 * La Reduction globale {@link ReductionMigration}.
	 */
	private List<ReductionMigration> reductionsGlobale = new ArrayList<>();

	/**
	 * le numero de element contractuel parent.
	 */
	private Integer numECParent;
	/**
	 * un EL peut etre remboursable ou non
	 */
	private Boolean remboursable;

	/**
	 * The mode paiement. {@link ModePaiement}.
	 */
	@JsonDeserialize(using = ModePaiementDeserializer.class)
	private ModePaiement modePaiement;

	/**
	 * Reference de paiement.
	 */
	private String referenceDePaiement;

	/**
	 * constructeur par defaut.
	 */
	public ProduitMigration() {

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
		if (!(obj instanceof ProduitMigration)) {
			return false;
		}
		ProduitMigration rhs = (ProduitMigration) obj;

		boolean result = new EqualsBuilder().append(numEC, rhs.numEC).isEquals();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(43, 11).append(numEC).toHashCode();
	}

	/* Getters & Setters */

	/**
	 * Gets the num ec.
	 * 
	 * @return {@link #numEC}.
	 */
	public Integer getNumEC() {
		return numEC;
	}

	/**
	 * Gets the reference produit destination.
	 * 
	 * @return the reference produit destination
	 */
	public String getReferenceProduitDestination() {
		return referenceProduitDestination;
	}

	/**
	 * Sets the reference produit destination.
	 * 
	 * @param referenceProduitDestination
	 *            the new reference produit destination
	 */
	public void setReferenceProduitDestination(String referenceProduitDestination) {
		this.referenceProduitDestination = referenceProduitDestination;
	}

	/**
	 * Gets the reference produit source.
	 * 
	 * @return the reference produit source
	 */
	public String getReferenceProduitSource() {
		return referenceProduitSource;
	}

	/**
	 * Sets the reference produit source.
	 * 
	 * @param referenceProduitSource
	 *            the new reference produit source
	 */
	public void setReferenceProduitSource(String referenceProduitSource) {
		this.referenceProduitSource = referenceProduitSource;
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
	 * Sets the num ec.
	 * 
	 * @param numEC
	 *            the new num ec {@link #numEC}.
	 */
	public void setNumEC(Integer numEC) {
		this.numEC = numEC;
	}

	/**
	 * Gets the label.
	 * 
	 * @return {@link #label}.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the label.
	 * 
	 * @param label
	 *            the new label {@link #label}.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Gets the numero commande.
	 * 
	 * @return {@link #numeroCommande}.
	 */
	public String getNumeroCommande() {
		return numeroCommande;
	}

	/**
	 * Sets the numero commande.
	 * 
	 * @param numeroCommande
	 *            the new numero commande {@link #numeroCommande}.
	 */
	public void setNumeroCommande(String numeroCommande) {
		this.numeroCommande = numeroCommande;
	}

	/**
	 * Gets the prix.
	 * 
	 * @return {@link PrixMigration}.
	 */
	public PrixMigration getPrix() {
		return prix;
	}

	/**
	 * Sets the prix.
	 * 
	 * @param prix
	 *            the new prix {@link PrixMigration}.
	 */
	public void setPrix(PrixMigration prix) {
		this.prix = prix;
	}

	/**
	 * Gets the type produit.
	 * 
	 * @return {@link TypeProduit}.
	 */
	public TypeProduit getTypeProduit() {
		return typeProduit;
	}

	/**
	 * Sets the type produit.
	 * 
	 * @param typeProduit
	 *            the new type produit {@link TypeProduit}.
	 */
	public void setTypeProduit(TypeProduit typeProduit) {
		this.typeProduit = typeProduit;
	}

	/**
	 * Gets the num ec parent.
	 * 
	 * @return numero de l'element contractuel parent.
	 */
	public Integer getNumECParent() {
		return numECParent;
	}

	/**
	 * Sets the num ec parent.
	 * 
	 * @param numECParent
	 *            le numero de l'element contracteul parent.
	 */
	public void setNumECParent(Integer numECParent) {
		this.numECParent = numECParent;
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
	 * 
	 * @return List of {@link #reductions}.
	 */
	public List<ReductionMigration> getReductions() {
		return reductions;
	}

	/**
	 * 
	 * @param reductions
	 *            List of {@link #reductions}.
	 */
	public void setReductions(List<ReductionMigration> reductions) {
		this.reductions = reductions;
	}

	public List<ReductionMigration> getReductionsGlobale() {
		return reductionsGlobale;
	}

	public void setReductionsGlobale(List<ReductionMigration> reductionsGlobale) {
		this.reductionsGlobale = reductionsGlobale;
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
	 * 
	 * @return {@link ProduitMigration#referenceDePaiement}.
	 */
	public String getReferenceDePaiement() {
		return referenceDePaiement;
	}

	/**
	 * 
	 * @param referenceDePaiement
	 *            {@link ProduitMigration#referenceDePaiement}.
	 */
	public void setReferenceDePaiement(String referenceDePaiement) {
		this.referenceDePaiement = referenceDePaiement;
	}

	/**
	 * Test sur les contrat de type Abonnement.
	 * 
	 * @param prix
	 *            the prix
	 * @return {@link #isAbonnement(Prix, TypeProduit)} {@link Prix}. {@link TypeProduit}.
	 */

	public boolean isAbonnement(Prix prix) {
		if ((prix.getPeriodicite() != null && prix.getPeriodicite() > 0)) {
			if (prix.getModePaiement() != null && (prix.getModePaiement().equals(ModePaiement.TROIS_FOIS_SANS_FRAIS))) {
				if ((prix.getModeFacturation() != null)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Test sur les contrats de type Location.
	 * 
	 * @param prix
	 *            the prix
	 * @return {@link #isLocation(Prix, TypeProduit)} {@link Prix}. {@link TypeProduit}.
	 */

	public boolean isLocation(Prix prix) {
		if ((prix.getPeriodicite() != null && prix.getPeriodicite() > 0)
				&& (prix.getModePaiement() != null && prix.getModePaiement().equals(ModePaiement.TROIS_FOIS_SANS_FRAIS) && (prix
						.getModeFacturation() != null))) {
			return false;
		}
		return true;
	}

	/**
	 * Test sur les ventes des Bien et Service.
	 * 
	 * @param prix
	 *            the prix
	 * @return {@link #isVente(Prix, TypeProduit)} {@link Prix}. {@link TypeProduit}.
	 */

	public boolean isVente(Prix prix) {
		// vente de Service ou Bien
		if ((prix.getPeriodicite() == null || prix.getPeriodicite() == 0)
				&& (prix.getModePaiement() != null)
				&& (prix.getModeFacturation() != null)
				&& ((prix.getDuree() != null && prix.getDuree() > 0) || (prix.getEngagement() != null && prix
						.getEngagement() > 0))) {

			return false;
		}

		return true;
	}

	/**
	 * Tester si un produit a un parent ou pas.
	 * 
	 * @return true si le produit a un parent.
	 */
	public boolean hasParent() {
		if (numECParent != null) {
			return true;
		}
		return false;
	}

	/**
	 * From json to {@link ProduitMigration}.
	 * 
	 * @param json
	 *            l'objet {@link ProduitMigration} en format json.
	 * @return {@link ProduitMigration}
	 * @throws IOException
	 *             {@link IOException}
	 */
	public static ProduitMigration fromJsonToProduitMigration(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, ProduitMigration.class);
	}

	/**
	 * To json.
	 * 
	 * @param produitMigration
	 *            le {@link ProduitMigration} a transforme en objet {@link JSONObject}
	 * @return {@link JSONObject}
	 * @throws JsonProcessingException
	 *             {@link JsonProcessingException}
	 */
	public static String toJson(ProduitMigration produitMigration) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(produitMigration);
	}

	/**
	 * To json array.
	 * 
	 * @param produitMigrations
	 *            collection de {@link ProduitMigration}
	 * @return la colllection de {@link ProduitMigration} en format json.
	 * @throws JsonProcessingException
	 *             {@link JsonProcessingException}
	 */
	public static String toJsonArray(Collection<ProduitMigration> produitMigrations) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(produitMigrations);
	}

	public Produit toProduit() {
		Produit produit = new Produit();
		produit.setReference(referenceProduitDestination);
		produit.setNumEC(numEC);
		produit.setLabel(label);
		produit.setNumECParent(numECParent);
		produit.setNumeroCommande(numeroCommande);
		produit.setTypeProduit(typeProduit);
		produit.setRemboursable(remboursable);

		if (prix != null) {

			produit.setPrix(prix.toPrix(modePaiement));

		}
		return produit;
	}
}
