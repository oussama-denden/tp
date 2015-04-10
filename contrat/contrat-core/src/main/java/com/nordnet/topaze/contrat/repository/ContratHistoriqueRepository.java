package com.nordnet.topaze.contrat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nordnet.topaze.contrat.domain.ContratHistorique;

/**
 * Outils de persistence pour l'entite {@link ContratHistorique}.
 * 
 * @author akram-moncer
 * 
 */
@Repository("contratHistoriqueRepository")
public interface ContratHistoriqueRepository extends JpaRepository<ContratHistorique, Integer> {

	/**
	 * 
	 * @param reference
	 *            reference contrat global.
	 * @return list de contrat de meme reference.
	 */
	public List<ContratHistorique> findByReference(String reference);

	/**
	 * retourne la derniere version du contrat dans l'historique.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @return {@link ContratHistorique}.
	 */
	@Query(name = "findDerniereVersion", value = "SELECT c FROM ContratHistorique c WHERE (c.reference LIKE :referenceContrat) AND (c.version = (SELECT COUNT(*) FROM ContratHistorique c1 WHERE c1.reference LIKE :referenceContrat) - 1)")
	public ContratHistorique findDerniereVersion(@Param("referenceContrat") String referenceContrat);

}
