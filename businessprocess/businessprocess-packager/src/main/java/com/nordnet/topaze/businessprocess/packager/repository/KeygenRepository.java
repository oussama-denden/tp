package com.nordnet.topaze.businessprocess.packager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nordnet.topaze.businessprocess.packager.domain.Keygen;

/**
 * Outils de persistence pour l'entite {@link Keygen}.
 * 
 * @author anisselmane.
 */
@Repository("keygenRepository")
public interface KeygenRepository extends JpaRepository<Keygen, Integer> {

	/**
	 * cherecher la dernier référence.
	 * 
	 * @param nom
	 *            le nom de l entite.
	 * @return {@link Keygen}.
	 */
	@Query(name = "findDernier", value = "SELECT k FROM Keygen k WHERE k.entite =:nom")
	public Keygen findDernier(@Param("nom") String nom);

	/**
	 * cherecher la dernier référence.
	 * 
	 * @param nom
	 *            le nom de l entite.
	 * @return {@link Keygen}.
	 */
	@Query(value = "SELECT getReference(?1)", nativeQuery = true)
	public String getReference(String nom);
}
