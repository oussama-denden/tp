package com.nordnet.topaze.livraison.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nordnet.topaze.livraison.core.domain.ElementLivraison;
import com.nordnet.topaze.livraison.core.domain.TypeBonPreparation;

/**
 * Outils de persistence pour l'entite {@link ElementLivraison}.
 * 
 * @author akram-moncer
 * 
 */
@Repository("elementLivraisonRepository")
public interface ElementLivraisonRepository extends JpaRepository<ElementLivraison, Integer> {

	/**
	 * Cherecher l' {@link ElementLivraison} avec le reference passée en parametre.
	 * 
	 * @param reference
	 *            reference element de livraison.
	 * @return element de livraiso avec le reference passée en parametre.
	 */
	@Query(name = "ElementLivraison.findEMByReference", value = "SELECT el From ElementLivraison el WHERE el.reference LIKE ? AND el.typeBonPreparation LIKE 'LIVRAISON'")
	public ElementLivraison findByReference(String reference);

	/**
	 * Cherecher l' {@link ElementLivraison} avec le reference passée en parametre.
	 * 
	 * @param reference
	 *            reference Bon de Préparation.
	 * @param referenceProduit
	 *            reference du produit.
	 * @return Bon de Préparation Global avec le reference passée en parametre.
	 */
	@Query(name = "ElementLivraison.findByReference", value = "SELECT el From ElementLivraison el WHERE el.reference LIKE :reference AND el.referenceProduit like :referenceProduit AND el.typeBonPreparation LIKE 'LIVRAISON'")
	public ElementLivraison findByReferenceAndReferenceProduit(@Param("reference") String reference,
			@Param("referenceProduit") String referenceProduit);

	/**
	 * Cherecher l' {@link ElementLivraison} de type {@link TypeBonPreparation#MIGRATION} avec le reference passée en
	 * parametre.
	 * 
	 * @param reference
	 *            reference Bon de Préparation.
	 * @return Bon de Préparation Global avec le reference passée en parametre.
	 */
	@Query(name = "ElementLivraison.findEMByReference", value = "SELECT el From ElementLivraison el WHERE el.reference LIKE ? AND el.typeBonPreparation LIKE 'MIGRATION'")
	public ElementLivraison findEMByReference(String reference);

	/**
	 * Cherecher l' {@link ElementLivraison} de type {@link TypeBonPreparation#CESSION} avec le reference passée en
	 * parametre.
	 * 
	 * @param reference
	 *            reference Bon de Préparation.
	 * @return Bon de Préparation Global avec le reference passée en parametre.
	 */
	@Query(name = "ElementLivraison.findECByReference", value = "SELECT el From ElementLivraison el WHERE el.reference LIKE ? AND el.typeBonPreparation LIKE 'CESSION'")
	public ElementLivraison findECByReference(String reference);

	/**
	 * Cherecher l'{@link ElementLivraison} de type {@link TypeBonPreparation#RETOUR} avec le reference passée en
	 * parametre.
	 * 
	 * @param referenceER
	 *            reference {@link ElementLivraison} de retour.
	 * @return Bon de Préparation Global avec le reference passée en parametre.
	 */
	@Query(name = "ElementLivraison.findERByReference", value = "SELECT el From ElementLivraison el WHERE el.reference LIKE ? AND el.typeBonPreparation LIKE 'RETOUR'")
	public ElementLivraison findERByReference(String referenceER);

	/**
	 * @return list des sous {@link ElementLivraison} de type bien non recupere.
	 */
	@Query(name = "ElementLivraison.findBiensEnCoursRecuperation", value = "SELECT el From ElementLivraison el WHERE el.datePreparation != null AND  el.dateRetourTermine = null AND el.causeNonlivraison = null AND el.typeBonPreparation LIKE 'RETOUR' AND el.acteur = 'NETRETOUR'")
	public List<ElementLivraison> findBiensEnCoursRecuperation();

	/**
	 * @return liste des sous {@link ElementLivraison} de type service en cours de livraison.
	 */
	@Query(name = "ElementLivraison.findServicesEnCoursActivation", value = "SELECT el From ElementLivraison el WHERE el.datePreparation != null AND  el.dateLivraisonTermine = null AND el.causeNonlivraison = null AND el.typeBonPreparation LIKE 'LIVRAISON' AND el.acteur = 'PACKAGER' AND el.bonPreparationParent.dateAnnulation = null")
	public List<ElementLivraison> findServicesEnCoursActivation();

	/**
	 * @return liste des sous {@link ElementLivraison} de type service en cours de livraison.
	 */
	@Query(name = "ElementLivraison.findBiensEnCoursLivraison", value = "SELECT el From ElementLivraison el WHERE el.datePreparation != null AND  el.dateLivraisonTermine = null AND el.causeNonlivraison = null AND el.typeBonPreparation LIKE 'LIVRAISON' AND el.acteur = 'NETDELIVERY' AND el.bonPreparationParent.dateAnnulation = null")
	public List<ElementLivraison> findBiensEnCoursLivraison();

	/**
	 * @return la list des sous {@link ElementLivraison} de type service en cours de recuperation.
	 */
	@Query(name = "ElementLivraison.findServicesEnCoursSuspension", value = "SELECT el From ElementLivraison el WHERE el.datePreparation != null AND  el.dateRetourTermine = null AND el.causeNonlivraison = null AND el.typeBonPreparation LIKE 'RETOUR' AND el.acteur = 'PACKAGER'")
	public List<ElementLivraison> findServicesEnCoursSuspension();

	/**
	 * @return la list des sous {@link ElementLivraison} de type service qui sont suspendu.
	 */
	@Query(name = "ElementLivraison.findServicesSuspendu", value = "SELECT el From ElementLivraison el WHERE el.datePreparation != null AND  el.dateRetourTermine != null AND el.causeNonlivraison = null AND el.typeBonPreparation LIKE 'RETOUR' AND el.acteur = 'PACKAGER'")
	public List<ElementLivraison> findServicesSuspendu();

	/**
	 * @return la list des sous {@link ElementLivraison} migration de type bien en cours de livraison.
	 */
	@Query(name = "ElementLivraison.findBiensEnCoursMigration", value = "SELECT el From ElementLivraison el WHERE el.datePreparation != null AND  el.dateLivraisonTermine = null AND el.causeNonlivraison = null AND el.typeBonPreparation LIKE 'MIGRATION' AND el.acteur = 'SWAP'")
	public List<ElementLivraison> findBiensMigrationEnCoursLivraison();

	/**
	 * @return la list des sous {@link ElementLivraison} migration de type bien en cours de retour.
	 */
	@Query(name = "ElementLivraison.findBiensEnCoursMigration", value = "SELECT el From ElementLivraison el WHERE el.datePreparation != null AND  el.dateRetourTermine = null AND el.causeNonlivraison = null AND el.typeBonPreparation LIKE 'MIGRATION' AND el.acteur = 'SWAP'")
	public List<ElementLivraison> findBiensMigrationEnCoursRetour();

	/**
	 * Cherecher l' {@link ElementLivraison} avec le reference passée en parametre.
	 * 
	 * @param reference
	 *            reference Bon de Préparation.
	 * @return Bon de Préparation Global avec le reference passée en parametre.
	 */
	@Query(name = "ElementLivraison.findEMByReference", value = "SELECT el From ElementLivraison el WHERE el.reference LIKE ? ")
	public ElementLivraison findByReferenceGenerale(String reference);

	/**
	 * cherher la liste des éléments conractuelles de type service ordonnée par la date de livraison.
	 * 
	 * @param reference
	 *            reference element livraison.
	 * @return {@link ElementLivraison}.
	 */
	@Query(name = "ElementLivraison.findByreferenceOrderBydateLivraisonTermine", value = "SELECT el From ElementLivraison el WHERE el.reference LIKE ? AND (el.typeBonPreparation LIKE 'LIVRAISON' OR el.typeBonPreparation LIKE 'MIGRATION') AND dateLivraisonTermine!=null ORDER BY dateLivraisonTermine DESC")
	public List<ElementLivraison> findByreferenceOrderBydateLivraisonTermine(String reference);

	/**
	 * cherher la liste des éléments conractuelles de type service ordonnée par la date de livraison.
	 * 
	 * @param reference
	 *            reference element livraison.
	 * @return {@link ElementLivraison}.
	 */
	@Query(name = "ElementLivraison.findByreferenceOrderBydateRetourTermine", value = "SELECT el From ElementLivraison el WHERE el.reference LIKE ? AND (el.typeBonPreparation LIKE 'RETOUR' OR el.typeBonPreparation LIKE 'MIGRATION') AND dateRetourTermine!=null  ORDER BY dateRetourTermine DESC")
	public List<ElementLivraison> findByreferenceOrderBydateRetourTermine(String reference);

	/**
	 * Cherecher l' {@link ElementLivraison} de type {@link TypeBonPreparation#RENOUVELLEMENT} avec le reference passée
	 * en parametre.
	 * 
	 * @param reference
	 *            reference Bon de Préparation.
	 * @return Bon de Préparation Global avec le reference passée en parametre.
	 */
	@Query(name = "ElementLivraison.findEREByReference", value = "SELECT el From ElementLivraison el WHERE el.reference LIKE ? AND el.typeBonPreparation LIKE 'RENOUVELLEMENT'")
	public ElementLivraison findEREByReference(String reference);

}