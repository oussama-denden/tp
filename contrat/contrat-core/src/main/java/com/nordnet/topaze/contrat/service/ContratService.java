package com.nordnet.topaze.contrat.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.nordnet.topaze.contrat.business.ContratAvenantInfo;
import com.nordnet.topaze.contrat.business.ContratBP;
import com.nordnet.topaze.contrat.business.ContratBillingInfo;
import com.nordnet.topaze.contrat.business.ContratChangerModePaiementInfo;
import com.nordnet.topaze.contrat.business.ContratValidationInfo;
import com.nordnet.topaze.contrat.business.ElementContractuelInfo;
import com.nordnet.topaze.contrat.business.Produit;
import com.nordnet.topaze.contrat.domain.Avenant;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ContratHistorique;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.exception.TopazeException;

/**
 * La service ContratService va contenir tous les operations en rapport avec les differents type de contrat.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public interface ContratService {

	/**
	 * @param reference
	 *            reference du contrat global.
	 * @return {@link Contrat}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public Contrat getContratByReference(String reference) throws TopazeException;

	/**
	 * Creates the contrat abonnement.
	 * 
	 * @param produit
	 *            le produit qui sera lié au contrat.
	 * @return {@link ElementContractuel}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ElementContractuel preparerElementContractuel(Produit produit) throws TopazeException;

	/**
	 * Generer une liste des contrats a partir d'une liste des produits.
	 * 
	 * @param produits
	 *            preparer contrat a partir d'une liste de {@link Produit}.
	 * @param user
	 *            the user
	 * @param isMigration
	 *            the is migration
	 * @param refContrat
	 *            the ref contrat
	 * @return {@link Contrat}.
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 */
	public Contrat preparerContrat(List<Produit> produits, String user, boolean isMigration, String refContratn,
			boolean preMigration) throws TopazeException;

	/**
	 * Validation du contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param contratValidationInfo
	 *            the contrat validation info
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 */
	public void validerContrat(String referenceContrat, ContratValidationInfo contratValidationInfo)
			throws TopazeException;

	/**
	 * Le date debut de facturation peut etre modifier manuellement.
	 * 
	 * @param referenceContrat
	 *            le reference de contrat.
	 * @param dateDebutFacturation
	 *            le date de debut de factruration.
	 * @return date debut facturation.
	 * @throws TopazeException
	 *             {@link {@link TopazeException}.}.
	 */
	public Date changerDateDebutFacturation(String referenceContrat, Date dateDebutFacturation) throws TopazeException;

	/**
	 * Chercher les contrat global valider.
	 * 
	 * @return {@link ElementContractuel}.
	 */
	public List<Contrat> findContratsGlobalValider();

	/**
	 * Chercher les reference contrat global valider.
	 * 
	 * @return {@link ElementContractuel}.
	 */
	public List<String> findReferenceContratsGlobalValider();

	/**
	 * Chercher les references du contrats global valider.
	 * 
	 * @return {@link ElementContractuel}.
	 */
	public List<String> getReferencesContratsGlobalValider();

	/**
	 * chercher la liste des references des produits d'un contrat.
	 * 
	 * @param reference
	 *            reference d'un contrat.
	 * @return Liste des references des produits.
	 */
	public List<String> getAssociatedProductsRefrences(String reference);

	/**
	 * Chercher les informations de contrat utils pour la premiere billing.
	 * 
	 * @param referenceContrat
	 *            reference de contrat global.
	 * @return {@link ContratBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public List<ContratBillingInfo> getContratBillingInformation(String referenceContrat, Integer numEC)
			throws TopazeException;

	/**
	 * Preparer les information d'un BP pour un contrat global.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return {@link ContratBP}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ContratBP getContratBP(String referenceContrat) throws TopazeException;

	/**
	 * Preparer les information d'un BP de un ancien contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return {@link ContratBP}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ContratBP getContratBPHistorique(String referenceContrat) throws TopazeException;

	/**
	 * 
	 * @return liste des references des contrat livres non resilier.
	 */
	public List<String> getReferencesContratLivrer();

	/**
	 * Modifier la date de derniere facture.
	 * 
	 * @param referenceContrat
	 *            reference d'un contrat global ou dans sous contrat.
	 * @param numEC
	 *            numero EL.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void changerDateDerniereFacture(String referenceContrat, Integer numEC) throws TopazeException;

	/**
	 * Modifier la date facture resiliation.
	 * 
	 * @param referenceContrat
	 *            reference d'un contrat global ou dans sous contrat.
	 * 
	 * @param numEC
	 *            numero EL.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void changerDateFactureResiliation(String referenceContrat, Integer numEC) throws TopazeException;

	/**
	 * changer date du fin du contrat.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void changerDateFinContrat(String referenceContrat, Integer numEC) throws TopazeException;

	/**
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return {@link ElementContractuel}
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public Integer getEngagement(String referenceContrat) throws TopazeException;

	/**
	 * retourner les infos des fils d'un {@link ElementContractuel}.
	 * 
	 * @param referenceContrat
	 *            reference {@link ElementContractuel}.
	 * @param numEC
	 *            numero {@link ElementContractuel}.
	 * @return liste des {@link ElementContractuelInfo} des element contractuel fils.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	List<ElementContractuelInfo> getFilsService(String referenceContrat, Integer numEC) throws TopazeException;

	/**
	 * Retourne la reference de {@link ElementContractuel} parent.
	 * 
	 * @param refContrat
	 *            reference du {@link Contrat}.
	 * @param numEC
	 *            numero de {@link ElementContractuel}.
	 * @return {@link ElementContractuelInfo} associe au parent.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ElementContractuelInfo getParentInfo(String refContrat, Integer numEC) throws TopazeException;

	/**
	 * retourne si le test 'isPackagerCreationPossible' doit étre fait avant de creer le package ou non.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @return true si le test de creation du package doit etre fait avant de lancer une demande de creation vers
	 *         packager.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public boolean checkIsPackagerCreationPossible(String referenceContrat) throws TopazeException;

	/**
	 * Changer le moyen de paiement.
	 * 
	 * @param refContratGlobal
	 * @param changerModePaiementInfo
	 * @throws TopazeException
	 */
	public void changerMoyenDePaiement(String refContratGlobal, ContratChangerModePaiementInfo changerModePaiementInfo)
			throws TopazeException;

	/**
	 * 
	 * @return List d'{@link Avenant}.
	 */
	public List<Avenant> findAll();

	/**
	 * historiser un contrat:
	 * <ul>
	 * <li>Supprimer la version en cours de la table contrat.</li>
	 * <li>Creation de copie du contrat dans la table contratHistorique.</li>
	 * </ul>
	 * 
	 * @param contrat
	 *            {@link Contrat}.
	 * @return {@link ContratHistorique}.
	 * 
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * 
	 */
	public ContratHistorique historiser(Contrat contrat) throws TopazeException;

	/**
	 * Preparer les information d'un Avenant necaissaire a la migration d'un contrat global.
	 * 
	 * @param referenceContrat
	 *            reference du contrat global.
	 * @return {@link ContratBP}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws IOException
	 *             {@link IOException}.
	 */
	public ContratAvenantInfo getContratAvenantInfo(String referenceContrat) throws TopazeException, IOException;

	/**
	 * chercher les information du derniere historisation du contrat.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @return liste des {@link ContratBillingInfo} lie au dernier historique du contrat.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public List<ContratBillingInfo> getContratBillingInformationHistorise(String referenceContrat)
			throws TopazeException;

	/**
	 * chercher contrat par reference.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @return {@link Contrat}.
	 */
	public Contrat findByReference(String referenceContrat);

	/**
	 * chercher les information du migration administrative du contrat.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @return liste des {@link ContratBillingInfo} lie au dernier historique du contrat.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public List<ContratBillingInfo> getContratBillingInformationMigrationAdministrative(String referenceContrat)
			throws TopazeException;

	/**
	 * retourner la reference de la commande associe.
	 * 
	 * @param referenceContrat
	 *            reference {@link Contrat}.
	 * @return reference commande.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public String getReferenceCommande(String referenceContrat) throws TopazeException;

	/**
	 * Ajouter un commantaire sur le contrat.
	 * 
	 * @param referenceContrat
	 *            reference {@link Contrat}.
	 * @param commentaire
	 *            commentaire.
	 * @param user
	 *            user.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void ajouterCommentaire(String referenceContrat, String commentaire, String user) throws TopazeException;

	/**
	 * recuperer l'element contractuelle par son id.
	 * 
	 * @param id
	 *            id de l'emement
	 * @return {@link ElementContractuel}
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public ElementContractuel getElementContractuelleParId(Integer id) throws TopazeException;

}
