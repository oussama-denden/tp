package com.nordnet.topaze.migration.outil.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Les informations d'un element contractuel apres migration.
 * 
 * @author Oussama Denden
 * 
 */
public class ResultatSimulationEC {

	/**
	 * reference du contrat global.
	 */
	private String referenceProduit;

	/**
	 * Numero de l' element Contractuel.
	 */
	private Integer numEC;

	/**
	 * {@link PenaliteBillingInfo}.
	 */
	private PenaliteBillingInfo penaliteBillingInfo;

	/**
	 * {@link RemboursementBillingInfo}.
	 */
	private RemboursementBillingInfo remboursementBillingInfo;

	/**
	 * frais de resiliation.
	 */
	private List<FraisMigrationSimulation> frais = new ArrayList<>();

	/**
	 * constructeur par defaut.
	 */
	public ResultatSimulationEC() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public String toString() {
		return "ECSimulationResultat [referenceProduit=" + referenceProduit + ", numEC=" + numEC
				+ ", penaliteBillingInfo=" + penaliteBillingInfo + ", remboursementBillingInfo="
				+ remboursementBillingInfo + ", frais=" + frais + "]";
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
		if (!(obj instanceof ResultatSimulationEC)) {
			return false;
		}
		ResultatSimulationEC rhs = (ResultatSimulationEC) obj;
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
	 * @return {@link Frais}.
	 */
	public List<FraisMigrationSimulation> getFrais() {
		return frais;
	}

	/**
	 * @param frais
	 *            {@link Frais}.
	 */
	public void setFrais(List<FraisMigrationSimulation> frais) {
		this.frais = frais;
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
	 * @return {@link #penaliteBillingInfo}.
	 */
	public PenaliteBillingInfo getPenaliteBillingInfo() {
		return penaliteBillingInfo;
	}

	/**
	 * @param penaliteBillingInfo
	 *            {@link #penaliteBillingInfo}.
	 */
	public void setPenaliteBillingInfo(PenaliteBillingInfo penaliteBillingInfo) {
		this.penaliteBillingInfo = penaliteBillingInfo;
	}

	/**
	 * @return {@link #remboursementBillingInfo}.
	 */
	public RemboursementBillingInfo getRemboursementBillingInfo() {
		return remboursementBillingInfo;
	}

	/**
	 * @param remboursementBillingInfo
	 *            {@link #remboursementBillingInfo}.
	 */
	public void setRemboursementBillingInfo(RemboursementBillingInfo remboursementBillingInfo) {
		this.remboursementBillingInfo = remboursementBillingInfo;
	}
}
