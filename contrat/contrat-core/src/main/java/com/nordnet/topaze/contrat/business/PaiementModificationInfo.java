package com.nordnet.topaze.contrat.business;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nordnet.topaze.contrat.domain.ModePaiement;
import com.nordnet.topaze.contrat.util.ModePaiementDeserializer;

/**
 * Cette classe regroupe les informations qui definissent un {@link PaiementModificationInfo}.
 * 
 * a savoir le Mode de paiement et le reference de mode de paiement par exmple le RUM.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class PaiementModificationInfo {

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
	 * {@link ModePaiement}.
	 */
	@JsonDeserialize(using = ModePaiementDeserializer.class)
	private ModePaiement modePaiement;

	/**
	 * constructeur par defaut.
	 */
	public PaiementModificationInfo() {

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
		if (!(obj instanceof PaiementModificationInfo)) {
			return false;
		}
		PaiementModificationInfo rhs = (PaiementModificationInfo) obj;
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
	 * @return {@link #ModePaiement}.
	 */
	public ModePaiement getModePaiement() {
		return modePaiement;
	}

	/**
	 * @param modePaiement
	 *            {@link #modePaiement}.
	 */
	public void setModePaiement(ModePaiement modePaiement) {
		this.modePaiement = modePaiement;
	}

}
