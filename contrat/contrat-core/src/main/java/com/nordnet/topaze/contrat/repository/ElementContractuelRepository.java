package com.nordnet.topaze.contrat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ElementContractuel;

/**
 * Outils de persistence pour l'entite {@link ElementContractuel}.
 * 
 * @author anisselmane.
 */
@Repository("elementContractuelRepository")
public interface ElementContractuelRepository extends JpaRepository<ElementContractuel, Integer> {

	/**
	 * 
	 * @param elementContractuelParent
	 * @return
	 */
	@Query(name = "ElementContractuel.findFils", value = "SELECT e FROM ElementContractuel e WHERE e.elementContractuelParent LIKE :elementContractuelParent")
	public List<ElementContractuel> findFils(
			@Param("elementContractuelParent") ElementContractuel elementContractuelParent);

	/**
	 * Find element contratctuel pour resiliation future.
	 * 
	 * @return the list
	 */
	@Query(name = "ElementContractuel.findECResiliationFutureWithDate", value = "SELECT e.id FROM ElementContractuel e WHERE YEAR(e.politiqueResiliation.dateResiliation)=YEAR(?1) and MONTH(e.politiqueResiliation.dateResiliation)=MONTH(?1) and DAY(e.politiqueResiliation.dateResiliation)=DAY(?1) AND e.politiqueResiliation.dateAnnulation=null AND e.typeResiliation=null")
	public List<Integer> findECResiliationFutureWithDate(String dateDuJour);

	/**
	 * Find element contratctuel pour resiliation future.
	 * 
	 * @return the {@link ElementContractuel}
	 */
	@Query(name = "ElementContractuel.findECResiliationFuture", value = "SELECT e FROM ElementContractuel e WHERE e.politiqueResiliation!=null AND e.politiqueResiliation.dateAnnulation=null AND e.typeResiliation=null AND e.contratParent.reference LIKE ? AND e.numEC LIKE ?")
	public ElementContractuel findECResiliationFuture(String reference, Integer numEC);

	/**
	 * Find list element contratctuel pour resiliation future.
	 * 
	 * @return the @
	 */
	@Query(name = "ElementContractuel.findECResiliationFuture", value = "SELECT e FROM ElementContractuel e WHERE e.politiqueResiliation!=null AND e.politiqueResiliation.dateAnnulation=null AND e.typeResiliation=null AND e.contratParent.reference LIKE ? ")
	public List<ElementContractuel> findListECResiliationFuture(String reference);

	/**
	 * cherecher l' {@link ElementContractuel} par numEC et reference contrat parent.
	 * 
	 * @param numEC
	 *            numEC du l'element contractuel.
	 * @param reference
	 *            the reference
	 * @return {@link Contrat}.
	 */
	@Query(name = "ElementContractuel.findByNumECAndReferenceContrat", value = "SELECT e FROM ElementContractuel e WHERE e.numEC LIKE ? AND e.contratParent.reference LIKE ?")
	public ElementContractuel findByNumECAndReferenceContrat(Integer numEC, String reference);

	/**
	 * cherecher l' {@link ElementContractuel} prevu d'etre resilie par numEC et reference.
	 * 
	 * @param numEC
	 *            numEC du l'element contractuel.
	 * @param reference
	 *            the reference
	 * @return {@link ElementContractuel}.
	 */
	@Query(name = "ElementContractuel.findByNumECAndReferenceContrat", value = "SELECT e FROM ElementContractuel e WHERE e.numEC LIKE ? AND e.contratParent.reference LIKE ? AND e.politiqueResiliation!=null")
	public ElementContractuel findElementResiliationByNumECAndReferenceContrat(Integer numEC, String reference);
}
