package com.nordnet.topaze.catalogue.service;

import com.nordnet.topaze.catalogue.domain.Produit;
import com.nordnet.topaze.catalogue.domain.ProduitBillingInfo;
import com.nordnet.topaze.catalogue.domain.TypeTVA;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Outils de services metier pour l'entite {@link Produit}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public interface ProduitService {

	/**
	 * Creer et persister l'entite {@link Produit}.
	 * 
	 * @param produit
	 *            l'entite Produit.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void addProduit(Produit produit) throws TopazeException;

	/**
	 * Cherecher le produit avec le reference passée en parametre.
	 * 
	 * @param reference
	 *            reference produit.
	 * @return produit avec le reference passée en parametre.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public TypeTVA getTypeTVAProduit(String reference) throws TopazeException;

	/**
	 * chercher le produit avec le reference donnee et retourner son {@link ProduitBillingInfo}.
	 * 
	 * @param reference
	 *            reference d'un produit.
	 * @return {@link ProduitBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ProduitBillingInfo getProduitBillingInformation(String reference) throws TopazeException;

	/**
	 * get Produit.
	 * 
	 * @param reference
	 *            reference du produit.
	 * @return {@link Produit}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public Produit getProduit(String reference) throws TopazeException;

	/**
	 * 
	 * @param referenceProduit
	 *            reference du produit.
	 * @param referenceTemplate
	 *            reference du template xml.
	 * @return {@link Produit}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public Produit associerTemplate(String referenceProduit, String referenceTemplate) throws TopazeException;

	/**
	 * 
	 * @param referenceProduit
	 *            reference du produit.
	 * @return contenu du template xsd.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public String getTemplateXSD(String referenceProduit) throws TopazeException;

	/**
	 * 
	 * @param referenceProduit
	 *            reference du produit.
	 * @return contenu du template xml.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public String getTemplateXML(String referenceProduit) throws TopazeException;

}
