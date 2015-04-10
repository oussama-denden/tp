package com.nordnet.topaze.client.rest.business.livraison;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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
	 * le numero de element contractuel parent.
	 */
	private Integer numECParent;

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
