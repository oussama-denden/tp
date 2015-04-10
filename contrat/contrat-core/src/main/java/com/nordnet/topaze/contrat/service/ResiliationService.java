package com.nordnet.topaze.contrat.service;

import java.util.List;

import com.nordnet.topaze.contrat.business.AnnulationInfo;
import com.nordnet.topaze.contrat.business.ContratResiliationtInfo;
import com.nordnet.topaze.contrat.business.PolitiqueResiliation;
import com.nordnet.topaze.contrat.business.ResiliationBillingInfo;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.exception.TopazeException;

/**
 * La service ContratService contient tous les operations en rapport avec la resiliation du contrat.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public interface ResiliationService {

	/**
	 * resilier un contrat global.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param politiqueResiliation
	 *            politique resiliation.
	 * @param user
	 *            l'usager.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void resilierContrat(String referenceContrat, PolitiqueResiliation politiqueResiliation, String user,
			boolean isMigration, boolean isCession, boolean isMigrationFuture, boolean isCessionFuture,
			boolean isResiliationFuture, boolean isRenouvellement) throws TopazeException;

	/**
	 * Resiliation partiel.
	 * 
	 * @param referenceContrat
	 *            reference du contrat global.
	 * @param numEC
	 *            numero de {@link ElementContractuel}.
	 * @param politiqueResiliation
	 *            the politique resiliation
	 * @param user
	 *            l'usager.
	 * @throws TopazeException
	 *             the topaze exception {@link PolitiqueResiliation}. {@link TopazeException}.
	 */
	public void resiliationPartiel(String referenceContrat, Integer numEC, PolitiqueResiliation politiqueResiliation,
			String user, boolean isRenouvellement, boolean isResiliationFuturePartiel) throws TopazeException;

	/**
	 * Resiliation automatique.
	 * 
	 * @param sousContrat
	 *            contrat à resilier automatiquement par le system.
	 */
	public void resiliationAutomatique(ElementContractuel sousContrat);

	/**
	 * Preparer les information necessaire pour la simulation d'un resiliation global.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return {@link ResiliationBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	List<ResiliationBillingInfo> getResiliationBillingIformation(String referenceContrat) throws TopazeException;

	/**
	 * Preparer les information necessaire pour la simulation d'un resiliation partiel.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param numEC
	 *            numEC de produit.
	 * @return {@link ResiliationBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	List<ResiliationBillingInfo> getResiliationBillingIformation(String referenceContrat, Integer numEC)
			throws TopazeException;

	/**
	 * Liste des contrat pour résiliation future.
	 * 
	 * @param dateDuJour
	 *            la date du jour
	 * @return liste contrat.
	 */
	public List<Contrat> findContratsResiliationFuture(String dateDuJour);

	/**
	 * Liste des references contrat pour résiliation future.
	 * 
	 * @param dateDuJour
	 *            la date du jour
	 * @return liste contrat.
	 */
	public List<String> findReferenceContratsResiliationFuture(String dateDuJour);

	/**
	 * Chercher les résiliation future à échiance.
	 * 
	 * @param dateDuJour
	 *            la date du jour
	 * @return {@link List<ElementContractuel> }
	 */
	public List<Integer> findECResiliationFuture(String dateDuJour);

	/**
	 * Verifier si un {@link ElementContractuel} est resilier ou non.
	 * 
	 * @param referenceElementContractuel
	 *            reference de {@link ElementContractuel}.
	 * @return true si l'{@link ElementContractuel} est resilier.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public Boolean isElementContractuelResilier(String referenceContrat, Integer numEC) throws TopazeException;

	/**
	 * annuler la résilation d'un contrat
	 * 
	 * @param refereceContrat
	 * @param annulationResiliationInfo
	 * @throws TopazeException
	 */
	public void annulerResiliation(String referenceContrat, AnnulationInfo annulationResiliationInfo)
			throws TopazeException;

	/**
	 * annuler la résilation partielle d'un element contractuelle
	 * 
	 * @param referenceContrat
	 * @param numEC
	 * @param annulationResiliationInfo
	 * @throws TopazeException
	 */
	public void annulerResiliationPartiel(String referenceContrat, Integer numEC,
			AnnulationInfo annulationResiliationInfo) throws TopazeException;

	/**
	 * Resilier plusieurs contrats d'un seul coup.
	 * 
	 * @param contratResiliationtInfo
	 *            the contrat resiliationt info {@link ContratResiliationtInfo}.
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 */
	public void resilierContrats(ContratResiliationtInfo contratResiliationtInfo) throws TopazeException;

	/**
	 * Liste des contrat pour résiliation differee.
	 * 
	 * @param dateDuJour
	 *            la date du jour
	 * @return liste contrat.
	 */
	public List<String> findContratsResiliationDifferee(String dateDuJour);
}
