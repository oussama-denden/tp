package com.nordnet.topaze.contrat.service;

import java.util.List;

import org.json.JSONException;

import com.nordnet.topaze.contrat.business.ContratReduction;
import com.nordnet.topaze.contrat.business.ReductionInfo;
import com.nordnet.topaze.contrat.business.ReductionInterface;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.domain.Reduction;
import com.nordnet.topaze.contrat.domain.TypeFrais;
import com.nordnet.topaze.exception.TopazeException;

/**
 * contient tous les operations en rapport avec les reduction.
 * 
 * @author akram-moncer
 * 
 */
public interface ReductionService {

	/**
	 * ajouter reduction au {@link Contrat}.
	 * 
	 * @param contratReduction
	 *            {@link ContratReduction}.
	 * @param referenceContrat
	 *            reference du Contrat.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public String ajouterReductionContrat(ContratReduction contratReduction, String referenceContrat)
			throws TopazeException, JSONException;

	/**
	 * ajouter reduction a l {@link ElementContractuel}.
	 * 
	 * @param contratReduction
	 *            {@link ContratReduction}.
	 * @param referenceContrat
	 *            reference du Contrat.
	 * @param numEC
	 *            numero element contractuel.
	 * @return reference reduction.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws JSONException
	 *             {@link JSONException}.
	 */
	public String ajouterReductionElementContractuelle(ContratReduction contratReduction, String referenceContrat,
			Integer numEC) throws TopazeException, JSONException;

	/**
	 * utiliser une reduction pour un {@link Contrat} ou un {@link ElementContractuel}: augmenter le nombre
	 * d'utilisation de 1.
	 * 
	 * @param referenceContrat
	 *            reference {@link Contrat} ou {@link ElementContractuel}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void utiliserReduction(String referenceContrat, Integer numEC) throws TopazeException;

	/**
	 * Retoune la liste des reduction associe a un {@link ElementContractuel}.
	 * 
	 * @param referenceElementContractuel
	 *            reference {@link ElementContractuel}.
	 * @param version
	 *            version du contrat.
	 * @return list des reductions associe a un {@link ElementContractuel}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public List<ReductionInfo> getReductionAssocie(String referenceContrat, Integer numECel, Integer version)
			throws TopazeException;

	/**
	 * retourner la reduction global associé à un {@link ElementContractuel}.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param version
	 *            version du contrat
	 * @return liste des {@link Reduction} global.
	 */
	public List<Reduction> findReductionGlobales(String referenceContrat, Integer version);

	/**
	 * retourner la reduction partielle associé à un {@link ElementContractuel}.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param version
	 *            version du contrat
	 * @param numEC
	 *            numero {@link ElementContractuel}.
	 * @return {@link Reduction}.
	 */
	public Reduction findReductionPartiel(String referenceContrat, Integer version, Integer numEC);

	/**
	 * sauver une reduction.
	 * 
	 * @param reduction
	 *            {@link Reduction}.
	 */
	public void save(Reduction reduction);

	/**
	 * recuperer les reduction global sur un type de frais specific.
	 * 
	 * @param referenceContrat
	 *            reference contrat global
	 * @param version
	 *            version du contrat.
	 * @param typeFrais
	 *            {@link TypeFrais}.
	 * @return {@link Reduction}.
	 */
	public Reduction findReductionGlobalSurFrais(String referenceContrat, Integer version, TypeFrais typeFrais);

	/**
	 * recuperer les reduction partiel sur un type de frais specific.
	 * 
	 * @param referenceContrat
	 *            reference contrat global
	 * @param version
	 *            version du contrat.
	 * @param numEC
	 *            numero element contratctuel.
	 * @param typeFrais
	 *            {@link TypeFrais}.
	 * @return {@link Reduction}.
	 */
	public Reduction findReductionPartielSurFrais(String referenceContrat, Integer version, Integer numEC,
			TypeFrais typeFrais);

	/**
	 * annuler une reduction associe a un contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param referenceReduction
	 *            reference du reduction.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void annulerReductionContrat(String referenceContrat, String referenceReduction) throws TopazeException;

	/**
	 * annuler une reduction associe a un element contractuelle.
	 * 
	 * @param referenceContrat
	 *            . reference contrat.
	 * @param numEC
	 *            numero element contractuelle.
	 * @param referenceReduction
	 *            reference du reduction
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void annulerReductionElementContractuelle(String referenceContrat, Integer numEC, String referenceReduction)
			throws TopazeException;

	/**
	 * Chercher la liste de reduction valide pour un contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return {@link ReductionInterface}.
	 */
	public List<ReductionInterface> getReductionsValide(String referenceContrat) throws TopazeException;

}
