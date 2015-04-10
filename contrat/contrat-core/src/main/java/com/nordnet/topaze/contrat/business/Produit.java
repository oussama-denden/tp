package com.nordnet.topaze.contrat.business;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nordnet.topaze.client.rest.enums.TypeProduit;
import com.nordnet.topaze.contrat.domain.ModePaiement;
import com.nordnet.topaze.contrat.util.TypeProduitDeserializer;

/**
 * Cette classe regroupe les informations qui dï¿½finissent un {@link Produit}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@JsonIgnoreProperties({})
public class Produit {

	/**
	 * nom court de produit par exemple: SatMax10. C'est le nom utilisé pour référencer le produit dans le catalogue des
	 * biens.
	 */
	private String reference;

	/**
	 * reference du tarif associe au Ec dans le catalogue.
	 */
	private String referenceTarif;

	/**
	 * Numero de l' element Contractuel.
	 */
	private Integer numEC;

	/**
	 * le nom de {@link Produit} par exemple: Pack Sat Max 10Go. Ce libellé est utilisé pour l'affichage dans les
	 * interfaces, mais aussi la facturation.
	 */
	private String label;

	/**
	 * Le champ "BillingGroup", lorsqu'il doit être rempli, le sera avec le numéro de la commande.
	 */
	private String numeroCommande;

	/** The type produit. {@link TypeProduit} . */
	@JsonDeserialize(using = TypeProduitDeserializer.class)
	private TypeProduit typeProduit;

	/**
	 * Le prix. {@link Prix}.
	 */
	private Prix prix;

	/**
	 * le numero de element contractuel parent.
	 */
	private Integer numECParent;

	/**
	 * un EL peut etre remboursable ou non
	 */
	private Boolean remboursable;

	/**
	 * constructeur par defaut.
	 */
	public Produit() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Produit [reference=" + reference + ", numEC=" + numEC + ", label=" + label + ", numeroCommande="
				+ numeroCommande + ", typeProduit=" + typeProduit + ", prix=" + prix + ", refProduitParent="
				+ ", numECParent=" + numECParent + "]";
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
		if (!(obj instanceof Produit)) {
			return false;
		}
		Produit rhs = (Produit) obj;

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
	 * Gets the reference.
	 * 
	 * @return reference.
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * Sets the reference.
	 * 
	 * @param reference
	 *            reference.
	 */
	public void setReference(String reference) {
		this.reference = reference;
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
	 * Gets the prix.
	 * 
	 * @return {@link Prix}.
	 */
	public Prix getPrix() {
		return prix;
	}

	/**
	 * Sets the prix.
	 * 
	 * @param prix
	 *            the new prix {@link Prix}.
	 */
	public void setPrix(Prix prix) {
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
	 * 
	 * @return numero de l'element contractuel parent.
	 */
	public Integer getNumECParent() {
		return numECParent;
	}

	/**
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
	 * Test sur les contrat de type Abonnement.
	 * 
	 * @param prix
	 *            {@link Prix}.
	 * @return {@link #isAbonnement(Prix, TypeProduit)}
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
	 *            {@link Prix}.
	 * @return {@link #isLocation(Prix, TypeProduit)}
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
	 *            {@link Prix}.
	 * @return {@link #isVente(Prix, TypeProduit)}
	 */

	public boolean isVente(Prix prix) {
		// vente de Service ou Bien
		if ((prix.getPeriodicite() == null)
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
}
