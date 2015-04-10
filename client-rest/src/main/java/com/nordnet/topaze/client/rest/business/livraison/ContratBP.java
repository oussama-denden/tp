package com.nordnet.topaze.client.rest.business.livraison;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.nordnet.topaze.client.rest.enums.TypeContrat;
import com.nordnet.topaze.client.rest.enums.TypeProduit;

/**
 * Les information de BP.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class ContratBP {

	/**
	 * reference du contrat.
	 */
	private String referenceContrat;

	/**
	 * reference du produit.
	 */
	private String referenceProduit;

	/**
	 * nom court de produit destination par exemple: Satellite.Jet.
	 */
	private String referenceGammeDestination;

	/**
	 * nom court de produit source par exemple: Satellite.Jet.
	 */
	private String referenceGammeSource;

	/**
	 * le numero de EC.
	 */
	private Integer numEC;

	/**
	 * adresse de livraison.
	 */
	private String idAdrLivraison;

	/**
	 * id du client.
	 */
	private String idClient;

	/**
	 * {@link TypeContrat}.
	 */
	private TypeContrat typeContrat;

	/**
	 * {@link TypeProduit}.
	 */
	private TypeProduit typeProduit;

	/**
	 * sous BP.
	 */
	private List<ContratBP> sousContratsBP;

	/**
	 * le parent de {@link ContratBP}.
	 */
	private ContratBP contratBPParent;

	/**
	 * constructeur par defaut.
	 */
	public ContratBP() {

	}

	/**
	 * @return {@link #referenceContrat}.
	 */
	public String getReferenceContrat() {
		return referenceContrat;
	}

	/**
	 * @param referenceContrat
	 *            {@link #referenceContrat}.
	 */
	public void setReferenceContrat(String referenceContrat) {
		this.referenceContrat = referenceContrat;
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
	 * @return {@link #idAdrLivraison}.
	 */
	public String getIdAdrLivraison() {
		return idAdrLivraison;
	}

	/**
	 * @param idAdrLivraison
	 *            {@link #idAdrLivraison}.
	 */
	public void setIdAdrLivraison(String idAdrLivraison) {
		this.idAdrLivraison = idAdrLivraison;
	}

	/**
	 * @return {@link #idClient}.
	 */
	public String getIdClient() {
		return idClient;
	}

	/**
	 * @param idClient
	 *            {@link #idClient}.
	 */
	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}

	/**
	 * @return {@link TypeContrat}.
	 */
	public TypeContrat getTypeContrat() {
		return typeContrat;
	}

	/**
	 * @param typeContrat
	 *            {@link TypeContrat}.
	 */
	public void setTypeContrat(TypeContrat typeContrat) {
		this.typeContrat = typeContrat;
	}

	/**
	 * Gets the type produit.
	 * 
	 * @return the type produit
	 */
	public TypeProduit getTypeProduit() {
		return typeProduit;
	}

	/**
	 * Sets the type produit.
	 * 
	 * @param typeProduit
	 *            the new type produit
	 */
	public void setTypeProduit(TypeProduit typeProduit) {
		this.typeProduit = typeProduit;
	}

	/**
	 * @return {@link #sousContratsBP}.
	 */
	public List<ContratBP> getSousContratsBP() {
		return sousContratsBP;
	}

	/**
	 * @param sousContratsBP
	 *            {@link #sousContratsBP}.
	 */
	public void setSousContratsBP(List<ContratBP> sousContratsBP) {
		this.sousContratsBP = sousContratsBP;
	}

	/**
	 * @return {@link #contratBPParent}.
	 */
	public ContratBP getContratBPParent() {
		return contratBPParent;
	}

	/**
	 * @param contratBPParent
	 *            {@link #contratBPParent}.
	 */
	public void setContratBPParent(ContratBP contratBPParent) {
		this.contratBPParent = contratBPParent;
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
		if (!(obj instanceof ContratBP)) {
			return false;
		}
		ContratBP rhs = (ContratBP) obj;
		return new EqualsBuilder().append(referenceContrat, rhs.referenceContrat)
				.append(referenceProduit, rhs.referenceProduit).append(idClient, rhs.idClient).append(numEC, rhs.numEC)
				.append(idAdrLivraison, rhs.idAdrLivraison).append(typeContrat, rhs.typeContrat)
				.append(typeProduit, rhs.typeProduit).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(43, 11).append(referenceContrat).append(referenceProduit).append(idClient)
				.append(numEC).append(idAdrLivraison).append(typeContrat).append(typeProduit).toHashCode();
	}

}