package com.nordnet.topaze.contrat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.domain.Reduction;
import com.nordnet.topaze.contrat.domain.TypeFrais;
import com.nordnet.topaze.contrat.domain.TypeReduction;

/**
 * Outils de persistence pour l'entite {@link Reduction}.
 * 
 * @author akram-moncer
 * 
 */
@Repository("reductionRepository")
public interface ReductionRepository extends JpaRepository<Reduction, Integer> {

	/**
	 * retourner les reductions global associé à un {@link Contrat}.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param version
	 *            version du contrat
	 * @return {@link Reduction}.
	 */
	@Query(name = "Reduction.findReductionGlobal", value = "SELECT r From Reduction r WHERE r.referenceContrat Like :referenceContrat AND (r.version = :version or (:version is null AND r.version is null)) AND typeReduction like 'CONTRAT'  AND r.contextReduction like 'GLOBAL'")
	public List<Reduction> findReductionGlobales(@Param("referenceContrat") String referenceContrat,
			@Param("version") Integer version);

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
	@Query(name = "Reduction.findReductionPartiel", value = "SELECT r From Reduction r WHERE r.referenceContrat Like :referenceContrat AND (r.version = :version or (:version is null AND r.version is null)) AND r.numEC like :numEC AND typeReduction like 'CONTRAT' AND r.contextReduction like 'PARTIEL'")
	public Reduction findReductionPartiel(@Param("referenceContrat") String referenceContrat,
			@Param("version") Integer version, @Param("numEC") Integer numEC);

	/**
	 * retourner toutes les reductions appliqué sur un element contractuel (associé directement ou associe au contrat
	 * global) sans compter les reductions sur les frais.
	 * 
	 * @param referenceContrat
	 *            reference {@link Contrat}
	 * @param version
	 *            version du contrat.
	 * @param numEC
	 *            numero {@link ElementContractuel}
	 * @param typeReduction
	 *            {@link TypeReduction}.
	 * @return listes {@link Reduction}.
	 */
	@Query(name = "Reduction.findReductionsSurElementCoontratctuel", value = "SELECT r From Reduction r WHERE r.referenceContrat Like :referenceContrat AND (r.version = :version or (:version is null AND r.version is null)) AND (r.numEC is null OR r.numEC like :numEC) AND typeReduction like :typeReduction")
	public List<Reduction> findReductionsSurElementContratctuel(@Param("referenceContrat") String referenceContrat,
			@Param("version") Integer version, @Param("numEC") Integer numEC,
			@Param("typeReduction") TypeReduction typeReduction);

	/**
	 * retourner la reduction sur les frais de creation associé à un contrat global.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param version
	 *            version du contrat.
	 * @param typeFrais
	 *            {@link TypeFrais}.
	 * @return {@link Reduction}.
	 */
	@Query(name = "Reduction.findReductionGlobalSurFraisCreation", value = "SELECT r From Reduction r WHERE r.referenceContrat Like :referenceContrat AND (r.version = :version or (:version is null AND r.version is null)) AND r.numEC is null AND r.typeFrais like :typeFrais")
	public Reduction findReductionGlobalSurFrais(@Param("referenceContrat") String referenceContrat,
			@Param("version") Integer version, @Param("typeFrais") TypeFrais typeFrais);

	/**
	 * retourner la reduction sur les frais de creation associé à un element contractuel.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param version
	 *            version du contrat.
	 * @param numEC
	 *            numero {@link ElementContractuel}.
	 * @param typeFrais
	 *            {@link TypeFrais}.
	 * @return {@link Reduction}.
	 */
	@Query(name = "Reduction.findReductionPartielSurFrais", value = "SELECT r From Reduction r WHERE r.referenceContrat Like :referenceContrat AND (r.version = :version or (:version is null AND r.version is null)) AND (r.numEC = :numEC or (:numEC is null AND r.numEC is null)) AND r.typeFrais like :typeFrais")
	public Reduction findReductionPartielSurFrais(@Param("referenceContrat") String referenceContrat,
			@Param("version") Integer version, @Param("numEC") Integer numEC, @Param("typeFrais") TypeFrais typeFrais);

	/**
	 * retourner une reduction par Id.
	 * 
	 * @param id
	 *            id de la reduction dans la base.
	 * @return {@link Reduction}.
	 */
	public Reduction findById(Integer id);

	/**
	 * chercher la reduction par reference.
	 * 
	 * @param referenceReduction
	 * @return {@link Reduction}
	 */
	public Reduction findByReference(String reference);

	/**
	 * chercher tous les reductions associe à un contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return {@link Reduction}.
	 */
	List<Reduction> findByReferenceContrat(String referenceContrat);

}
