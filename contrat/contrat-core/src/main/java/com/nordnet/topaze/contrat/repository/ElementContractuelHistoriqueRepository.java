package com.nordnet.topaze.contrat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nordnet.topaze.contrat.domain.ElementContractuelHistorique;

/**
 * Outils de persistence pour l'entite {@link ElementContractuelHistorique}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
@Repository("elementContractuelHistoriqueRepository")
public interface ElementContractuelHistoriqueRepository extends JpaRepository<ElementContractuelHistorique, Integer> {

	/**
	 * cherecher l' {@link ElementContractuelHistorique} par reference.
	 * 
	 * @param reference
	 *            reference du l'element contractuelHistorique.
	 * @return {@link ElementContractuelHistorique}.
	 */
	@Query(name = "ElementContractuelHistorique.findByReferenceAndVersion", value = "SELECT e From ElementContractuelHistorique e WHERE e.contratParent.reference Like :reference AND e.numEC like:numEC")
	public ElementContractuelHistorique findByReferenceContratAndNumEC(@Param("reference") String reference,
			@Param("numEC") Integer numEC);

	/**
	 * chercher un {@link ElementContractuelHistorique} avec la reference et la version du contrat parent.
	 * 
	 * @param reference
	 *            reference contrat.
	 * @param version
	 *            version du contrat parent.
	 * @return {@link ElementContractuelHistorique}.
	 */
	@Query(name = "ElementContractuelHistorique.findByReferenceAndVersion", value = "SELECT e From ElementContractuelHistorique e WHERE e.contratParent.reference Like :reference AND e.numEC like:numEC AND (e.contratParent.version = :version or (:version is null AND e.contratParent.version is null))")
	public ElementContractuelHistorique findByReferenceAndVersion(@Param("reference") String reference,
			@Param("numEC") Integer numEC, @Param("version") Integer version);

}
