package com.nordnet.topaze.migration.outil.service;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.migration.outil.business.ContratMigrationInfo;
import com.nordnet.topaze.migration.outil.business.ResultatSimulation;

/**
 * Service qui gere la simulation de migration d'un contrat.
 * 
 * @author Oussama Denden
 * 
 */
public interface SimulateurMigrationService {

	/**
	 * Simuler la migration d'un contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param contratMigrationInfo
	 *            {@link ContratMigrationInfo}.
	 * @return {@link ResultatSimulation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	ResultatSimulation simulerMigration(String referenceContrat, ContratMigrationInfo contratMigrationInfo)
			throws TopazeException;

}