package com.nordnet.topaze.catalogue.domain;

import java.util.List;

/**
 * Cette classe regroupe les informations qui definissent un
 * {@link ProduitBillingInfo}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class ProduitBillingInfo {

	/**
	 * id de produit.
	 */
	private Integer produitId;

	/**
	 * label de produit.
	 */
	private String label;

	/**
	 * {@link TypeTVA}.
	 */
	private TypeTVA typeTVA;

	/**
	 * prix du produit.
	 */
	private Double prix;

	/**
	 * liste des montants associe à un produit.
	 */
	private List<Double> frais;
	
	/**
	 * Outils de Livraison.
	 */
	private OutilLivraison outilLivraison;

	/**
	 * constructeur par defaut.
	 */
	public ProduitBillingInfo() {

	}

	/* Getters & Setters */

	/**
	 * @return {@link #produitId}.
	 */
	public Integer getProduitId() {
		return produitId;
	}

	/**
	 * @param produitId
	 *            {@link #produitId}.
	 */
	public void setProduitId(Integer produitId) {
		this.produitId = produitId;
	}

	/**
	 * @return {@link #label}.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            {@link #label}.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return {@link TypeTVA}.
	 */
	public TypeTVA getTypeTVA() {
		return typeTVA;
	}

	/**
	 * @param typeTVA
	 *            {@link TypeTVA}.
	 */
	public void setTypeTVA(TypeTVA typeTVA) {
		this.typeTVA = typeTVA;
	}

	/**
	 * @return {@link #prix}.
	 */
	public Double getPrix() {
		return prix;
	}

	/**
	 * @param prix
	 *            {@link #prix}.
	 */
	public void setPrix(Double prix) {
		this.prix = prix;
	}

	/**
	 * @return {@link #montants}.
	 */
	public List<Double> getFrais() {
		return frais;
	}

	/**
	 * @param frais
	 *            {@link #frais}.
	 */
	public void setFrais(List<Double> frais) {
		this.frais = frais;
	}

	
	/**
	 * 
	 * @return {@link #outilLivraison}
	 */
	public OutilLivraison getOutilLivraison() {
		return outilLivraison;
	}

	
	/**
	 * 
	 * @param outilLivraison {@link #outilLivraison}
	 */
	public void setOutilLivraison(OutilLivraison outilLivraison) {
		this.outilLivraison = outilLivraison;
	}
}
