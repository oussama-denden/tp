package com.nordnet.topaze.livraison.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nordnet.topaze.livraison.core.domain.BonPreparation;

/**
 * Outils de persistence pour l'entit� {@link BonPreparation}.
 * 
 * @author akram-moncer
 * @author Ahmed-Mehdi-Laabidi
 * 
 */
@Repository("bonPreparationRepository")
public interface BonPreparationRepository extends JpaRepository<BonPreparation, Integer> {

	/**
	 * Cherecher le Bon de Préparation avec le reference passée en parametre.
	 * 
	 * @param reference
	 *            reference Bon de Préparation.
	 * @return Bon de Préparation Global avec le reference passée en parametre.
	 */
	public BonPreparation findByReference(String reference);

	// /**
	// * Cherecher le Bon de Préparation avec le reference passée en parametre.
	// *
	// * @param bonPreparationParent
	// * reference Bon de Préparation parent.
	// * @return liste de sous bon de Préparation avec le reference bon de préparation parent passée en parametre.
	// */
	// public Set<BonPreparation> findByBonPreparationParent(BonPreparation bonPreparationParent);

	/**
	 * Chercher l'ensemble de bon de prepartion global dont le status est preparer.
	 * 
	 * @return list des bon de prepartions global preparer.
	 */
	@Query(name = "BonPreparation.findBonPreparationGlobalPreparer", value = "SELECT bp From BonPreparation bp WHERE bp.datePreparation != null AND bp.dateLivraisonTermine = null AND bp.causeNonlivraison = null AND bp.typeBonPreparation LIKE 'LIVRAISON'")
	public List<BonPreparation> findBonPreparationGlobalPreparer();

	/**
	 * @return liste de BR Global non recupere.
	 */
	@Query(name = "BonPreparation.findBRGlobalEncoursRecuperation", value = "SELECT bp From BonPreparation bp WHERE bp.datePreparation != null AND  bp.dateRetourTermine = null AND bp.causeNonlivraison = null AND bp.typeBonPreparation LIKE 'RETOUR'")
	public List<BonPreparation> findBRGlobalEncoursRecuperation();

	/**
	 * Chercher les bons de migrations en cours de migration.
	 * 
	 * @return {@link BonPreparation}.
	 */
	@Query(name = "BonPreparation.findBMGlobalEncoursMigration", value = "SELECT bp From BonPreparation bp WHERE bp.datePreparation != null AND  bp.dateLivraisonTermine = null AND bp.causeNonlivraison = null AND bp.typeBonPreparation LIKE 'MIGRATION'")
	public List<BonPreparation> findBMGlobalEncoursMigration();

	/**
	 * Chercher un Bon de migration par reference.
	 * 
	 * @param reference
	 *            reference de bon de migration.
	 * @return {@link BonPreparation}.
	 */
	@Query(name = "BonPreparation.findBMByReference", value = "SELECT bp From BonPreparation bp WHERE bp.reference LIKE ? AND bp.typeBonPreparation LIKE 'MIGRATION'")
	public BonPreparation findBMByReference(String reference);

	/**
	 * Chercher un Bon de migration par reference.
	 * 
	 * @param referenceBP
	 *            reference de bon de preparation.
	 * @return {@link BonPreparation}.
	 */
	@Query(name = "BonPreparation.findBPByReference", value = "SELECT bp From BonPreparation bp WHERE bp.reference LIKE ? AND bp.typeBonPreparation LIKE 'LIVRAISON'")
	public BonPreparation findBPByReference(String referenceBP);

	/**
	 * Chercher un Bon de migration par reference.
	 * 
	 * @param referenceBP
	 *            reference de bon de preparation.
	 * @return {@link BonPreparation}.
	 */
	@Query(name = "BonPreparation.findBPByReference", value = "SELECT bp From BonPreparation bp WHERE bp.reference LIKE ? AND bp.typeBonPreparation LIKE 'RETOUR'")
	public BonPreparation findBRByReference(String referenceBP);

	/**
	 * Chercher un Bon de cession par reference.
	 * 
	 * @param referenceBP
	 *            reference de bon de preparation.
	 * @return {@link BonPreparation}.
	 */
	@Query(name = "BonPreparation.findBCByReference", value = "SELECT bp From BonPreparation bp WHERE bp.reference LIKE ? AND bp.typeBonPreparation LIKE 'CESSION'")
	public BonPreparation findBCByReference(String referenceBP);

	/**
	 * Chercher un Bon de renouvellement par reference.
	 * 
	 * @param referenceBP
	 *            reference de bon de preparation.
	 * @return {@link BonPreparation}.
	 */
	@Query(name = "BonPreparation.findBREByReference", value = "SELECT bp From BonPreparation bp WHERE bp.reference LIKE ? AND bp.typeBonPreparation LIKE 'RENOUVELLEMENT'")
	public BonPreparation findBREByReference(String referenceBP);

}
