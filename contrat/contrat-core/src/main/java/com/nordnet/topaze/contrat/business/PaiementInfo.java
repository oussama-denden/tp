package com.nordnet.topaze.contrat.business;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Cette classe regroupe les informations qui definissent un {@link PaiementInfo}.
 * 
 * a savoir le Mode de paiement et le reference de mode de paiement par exmple le RUM.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class PaiementInfo {

	/**
	 * reference du produit.
	 */
	private String referenceProduit;

	/**
	 * numero element contractuel.
	 */
	private Integer numEC;

	/**
	 * reference de mode du paiement par exemple le RUM.
	 */
	private String referenceModePaiement;

	/**
	 * adresse de livraison.
	 */
	private String idAdrLivraison;

	/**
	 * constructeur par defaut.
	 */
	public PaiementInfo() {

	}

	@Override
	public String toString() {
		return "PaiementInfo [referenceProduit=" + referenceProduit + ", numEC=" + numEC + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof PaiementInfo)) {
			return false;
		}
		PaiementInfo rhs = (PaiementInfo) obj;
		return new EqualsBuilder().append(referenceProduit, rhs.referenceProduit).append(numEC, rhs.numEC).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(43, 11).append(referenceProduit).append(numEC).toHashCode();
	}

	/* Getters & Setters */

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
	 * @return {@link #referenceModePaiement}.
	 */
	public String getReferenceModePaiement() {
		return referenceModePaiement;
	}

	/**
	 * @param referenceModePaiement
	 *            {@link #referenceModePaiement}.
	 */
	public void setReferenceModePaiement(String referenceModePaiement) {
		this.referenceModePaiement = referenceModePaiement;
	}

	/**
	 * @return {@link #idAdrLivraison}.
	 */
	public String getIdAdrLivraison() {
		return idAdrLivraison;
	}

	/**
	 * @param idAdrLivraison
	 *            {@link idAdrLivraison}.
	 */
	public void setIdAdrLivraison(String idAdrLivraison) {
		this.idAdrLivraison = idAdrLivraison;
	}

}
