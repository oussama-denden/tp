package com.nordnet.topaze.contrat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ElementContractuel;

/**
 * Outils de persistence pour l'entite {@link Contrat}.
 * 
 * @author Denden-OUSSAMA
 */
@Repository("contratRepository")
public interface ContratRepository extends JpaRepository<Contrat, Integer> {

	/**
	 * cherecher le contrat par reference.
	 * 
	 * @param reference
	 *            reference du contrat.
	 * @return {@link Contrat}.
	 */
	public Contrat findByReference(String reference);

	/**
	 * Chercher les contrat global en cours (non resilier).
	 * 
	 * @return {@link ElementContractuel}.
	 */
	@Query(name = "Contrat.findContratsGlobalValider", value = "SELECT c From Contrat c WHERE c.dateValidation is not NULL AND c.typeResiliation = NULL")
	public List<Contrat> findContratsGlobalValider();

	/**
	 * Chercher les references contrat global en cours (non resilier).
	 * 
	 * @return {@link ElementContractuel}.
	 */
	@Query(name = "Contrat.findReferenceContratsGlobalValider", value = "SELECT c.reference From Contrat c WHERE c.dateValidation is not NULL AND c.typeResiliation = NULL")
	public List<String> findReferenceContratsGlobalValider();

	/**
	 * Find references contrat livrer.
	 * 
	 * @return the list
	 */
	@Query(name = "Contrat.findReferencesContratLivrer", value = "SELECT c.reference From Contrat c WHERE c.dateDebutFacturation is not NULL AND c.dateFinContrat is NULL")
	public List<String> findReferencesContratLivrer();

	/**
	 * 
	 * @return liste contrat
	 */
	@Query(name = "Contrat.findContratsResiliationFuture", value = "SELECT c From Contrat c WHERE YEAR(c.politiqueResiliation.dateResiliation)=YEAR(?1) and MONTH(c.politiqueResiliation.dateResiliation)=MONTH(?1) and DAY(c.politiqueResiliation.dateResiliation)=DAY(?1) AND c.politiqueResiliation.dateAnnulation=null AND  c.typeResiliation=null AND delaiDeSecurite=false ")
	public List<Contrat> findContratsResiliationFuture(String dateDuJour);

	/**
	 * 
	 * @return liste contrat
	 */
	@Query(name = "Contrat.findReferenceContratsResiliationFuture", value = "SELECT c.reference From Contrat c WHERE YEAR(c.politiqueResiliation.dateResiliation)=YEAR(?1) and MONTH(c.politiqueResiliation.dateResiliation)=MONTH(?1) and DAY(c.politiqueResiliation.dateResiliation)=DAY(?1) AND c.politiqueResiliation.dateAnnulation=null AND  c.typeResiliation=null AND delaiDeSecurite=false ")
	public List<String> findReferenceContratsResiliationFuture(String dateDuJour);

	/**
	 * cherecher le contrat prevu d'etre resilier en future par reference.
	 * 
	 * @param reference
	 *            reference du contrat.
	 * @return {@link Contrat}.
	 */
	@Query(name = "Contrat.findContratsResiliationFuture", value = "SELECT c From Contrat c WHERE c.politiqueResiliation!=null AND c.dateFinContrat is NULL AND c.reference LIKE ? AND c.politiqueResiliation.dateAnnulation=null AND  c.typeResiliation=null")
	public Contrat findContratResiliationFuture(String reference);

	/**
	 * 
	 * @return liste contrat
	 */
	@Query(name = "Contrat.findContratsResiliationDifferee", value = "SELECT c.reference From Contrat c WHERE YEAR(c.politiqueResiliation.dateResiliation)=YEAR(?1) and MONTH(c.politiqueResiliation.dateResiliation)=MONTH(?1) and DAY(c.politiqueResiliation.dateResiliation)=DAY(?1) AND c.politiqueResiliation.dateAnnulation=null AND  c.typeResiliation=null AND delaiDeSecurite=true ")
	public List<String> findContratsResiliationDifferee(String dateDuJour);

}
