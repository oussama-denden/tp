package com.nordnet.topaze.resiliation.outil.service;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.resiliation.outil.business.ContratResiliationInfo;
import com.nordnet.topaze.resiliation.outil.business.PolitiqueResiliation;

/**
 * Service qui gere les outils de resiliation d'un contrat.
 * 
 * @author Oussama Denden
 * 
 */
public interface ContratResiliationService {

	/**
	 * Simuler la resiliation d'un contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param politiqueResiliation
	 *            {@link PolitiqueResiliation}.
	 * @return {@link ContratResiliationInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	ContratResiliationInfo simulerResiliation(String referenceContrat, PolitiqueResiliation politiqueResiliation)
			throws TopazeException;

	/**
	 * Simuler la resiliation partiel d'un contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param numEC
	 *            numEC de produit.
	 * @param politiqueResiliation
	 *            {@link PolitiqueResiliation}.
	 * @return {@link ContratResiliationInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	ContratResiliationInfo simulerResiliationPartiel(String referenceContrat, Integer numEC,
			PolitiqueResiliation politiqueResiliation) throws TopazeException;

}