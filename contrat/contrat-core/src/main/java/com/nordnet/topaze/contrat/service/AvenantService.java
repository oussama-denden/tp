package com.nordnet.topaze.contrat.service;

import java.util.List;

import com.nordnet.topaze.contrat.domain.Avenant;
import com.nordnet.topaze.contrat.domain.TypeAvenant;

/**
 * Contient les services liés au {@link Avenant}.
 * 
 * @author akram-moncer
 * 
 */
public interface AvenantService {

	/**
	 * retourner un {@link Avenant} par referenceContrat et version et typeAvenant.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param version
	 *            version du contrat
	 * @param typeAvenant
	 *            {@link TypeAvenant}.
	 * @return {@link Avenant}.
	 */
	public Avenant findByReferenceContratAndVersionAndTypeAvenant(String referenceContrat, Integer version,
			TypeAvenant typeAvenant);

	/**
	 * retourner un {@link Avenant} par referenceContrat et version.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param version
	 *            version du contrat
	 * @return {@link Avenant}.
	 */
	public Avenant findByReferenceContratAndVersion(String referenceContrat, Integer version);

	/**
	 * retourner les {@link Avenant} avec une demande de cession active.
	 * 
	 * @return list {@link Avenant}.
	 */
	public List<Avenant> findAvenantAvecCessionActive();

	/**
	 * retourner les references {@link Avenant} avec une demande de cession active.
	 * 
	 * @return list {@link Avenant}.
	 */
	public List<Integer> findReferenceAvenantAvecCessionActive();

	/**
	 * retourner les {@link Avenant} avec une demande de migration active.
	 * 
	 * @return list {@link Avenant}.
	 */
	public List<Avenant> findAvenantAvecMigrationActive();

	/**
	 * retourner les reference {@link Avenant} avec une demande de migration active.
	 * 
	 * @return list reference {@link Avenant}.
	 */
	public List<Integer> findReferenceAvenantAvecMigrationActive();

	/**
	 * retourner l' {@link Avenant} avec une demande de cession active.
	 * 
	 * @param referenceContrat
	 * @return
	 */
	public Avenant findAvenantAvecCessionActive(String referenceContrat);

	/**
	 * retourner l' {@link Avenant} avec une demande de migration active.
	 * 
	 * @param referenceContrat
	 * @return
	 */
	public Avenant findAvenantAvecMigrationActive(String referenceContrat);

	/**
	 * retourner les {@link Avenant} avec une demande de renouvellement active.
	 * 
	 * @return list {@link Avenant}.
	 */
	public List<Avenant> findAvenantAvecRenouvellementActive();

	/**
	 * retourner l' {@link Avenant} avec une demande de renouvellement active.
	 * 
	 * @param referenceContrat
	 * @return
	 */
	public Avenant findAvenantAvecRenouvellementActive(String referenceContrat);

	/**
	 * cherecher l'avenant par son id.
	 * 
	 * @param id
	 *            id d'avenant.
	 * @return {@link Avenant}.
	 */
	public Avenant findByID(Integer id);

}
