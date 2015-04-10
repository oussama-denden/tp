package com.nordnet.topaze.contrat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nordnet.topaze.contrat.domain.Avenant;
import com.nordnet.topaze.contrat.domain.TypeAvenant;

/**
 * Outils de persistence pour l'entite {@link Avenant}.
 * 
 * @author anisselmane.
 */
@Repository("avenantRepository")
public interface AvenantRepository extends JpaRepository<Avenant, Integer> {

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
	@Query(name = "Avenant.findAvenantAvecCessionActive", value = "SELECT a From Avenant a WHERE a.version is NULL And a.politiqueCession is not NULL and a.dateAnnulation is NULL")
	public List<Avenant> findAvenantAvecCessionActive();

	/**
	 * retourner les references {@link Avenant} avec une demande de cession active.
	 * 
	 * @return list {@link Avenant}.
	 */
	@Query(name = "Avenant.findAvenantAvecCessionActive", value = "SELECT a.id From Avenant a WHERE a.version is NULL And a.politiqueCession is not NULL and a.dateAnnulation is NULL")
	public List<Integer> findReferenceAvenantAvecCessionActive();

	/**
	 * retourner les {@link Avenant} avec une demande de renouvellement active.
	 * 
	 * @return list {@link Avenant}.
	 */
	@Query(name = "Avenant.findAvenantAvecRenouvellementActive", value = "SELECT a From Avenant a WHERE a.version is NULL And a.politiqueRenouvellement is not NULL and a.dateAnnulation is NULL")
	public List<Avenant> findAvenantAvecRenouvellementActive();

	/**
	 * retourner les {@link Avenant} avec une demande de migration active.
	 * 
	 * @return list {@link Avenant}.
	 */
	@Query(name = "Avenant.findAvenantAvecMigrationActive", value = "SELECT a From Avenant a WHERE a.version is NULL And a.politiqueMigration is not NULL and a.dateAnnulation is NULL")
	public List<Avenant> findAvenantAvecMigrationActive();

	/**
	 * retourner les reference du {@link Avenant} avec une demande de migration active.
	 * 
	 * @return list {@link Avenant}.
	 */
	@Query(name = "Avenant.findReferenceAvenantAvecMigrationActive", value = "SELECT a.id From Avenant a WHERE a.version is NULL And a.politiqueMigration is not NULL and a.dateAnnulation is NULL")
	public List<Integer> findReferenceAvenantAvecMigrationActive();

	/**
	 * retourner les {@link Avenant} avec une demande de cession active.
	 * 
	 * @return list {@link Avenant}.
	 */
	@Query(name = "Avenant.findAvenantAvecCessionActive", value = "SELECT a From Avenant a WHERE a.version is NULL And a.politiqueCession is not NULL and a.dateAnnulation is NULL and a.referenceContrat LIKE ?")
	public Avenant findAvenantAvecCessionActive(String referenceContrat);

	/**
	 * retourner les {@link Avenant} avec une demande de migration active.
	 * 
	 * @return list {@link Avenant}.
	 */
	@Query(name = "Avenant.findAvenantAvecCessionActive", value = "SELECT a From Avenant a WHERE a.version is NULL And a.politiqueMigration is not NULL and a.dateAnnulation is NULL and a.referenceContrat LIKE ?")
	public Avenant findAvenantAvecMigrationActive(String referenceContrat);

	/**
	 * retourner l' {@link Avenant} avec une demande de renouvellement active.
	 * 
	 * @return list {@link Avenant}.
	 */
	@Query(name = "Avenant.findAvenantAvecRenouvellementActive", value = "SELECT a From Avenant a WHERE a.version is NULL And a.politiqueRenouvellement is not NULL and a.dateAnnulation is NULL and a.referenceContrat LIKE ?")
	public Avenant findAvenantAvecRenouvellementActive(String referenceContrat);

}
