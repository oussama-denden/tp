package com.nordnet.topaze.client.rest.outil;

/**
 * REST Constants.
 * 
 * @author anisselmane.
 * 
 */
public final class RestConstants {

	/**
	 * constructeur par default.
	 */
	private RestConstants() {

	}

	/**
	 * Service rest de la brique contart: changerDateDebutFacturation.
	 */
	public static final String CHANGER_DATE_FACTURATION = "CHANGER_DATE_FACTURATION";

	/**
	 * Brique catalogue path.
	 */
	public static final String BRIQUE_CONTRAT = System.getProperty("contrat.url");

	/**
	 * Brique livraison core path.
	 */
	public static final String BRIQUE_LIVRAISON_CORE = System.getProperty("livraisonCore.url");

	/**
	 * l'url de la prique 'contrat-outil'.
	 */
	public static final String BRIQUE_CONTRAT_OUTIL = System.getProperty("contratOutil.url");

	/**
	 * Brique livraison outil path.
	 */
	public static final String BRIQUE_LIVRAISON_OUTIL = System.getProperty("livraisonOutil.url");

	/**
	 * Brique billing outil path.
	 */
	public static final String BILLING_OUTIL = System.getProperty("billingOutil.url");

	/**
	 * Service rest de la brique contrat: initierBR.
	 */
	public static final String INITIER_BR = "INITIER_BR";

	/**
	 * Service rest de la brique contrat: initierER.
	 */
	public static final String INITIER_ER = "INITIER_ER";

	/**
	 * Service rest de la brique livraison: preparerBR.
	 */
	public static final String PREPARER_BR = "PREPARER_BR";

	/**
	 * Service rest de la brique livraison: preparerER.
	 */
	public static final String PREPARER_ER = "PREPARER_ER";

	/**
	 * Service rest de la brique contrat: getContratBP.
	 */
	public static final String GET_CONTRATBP = "GET_CONTRATBP";

	/**
	 * Service rest de la brique livraison core: getBonPreparation.
	 */
	public static final String GET_BONPREPARATION = "GET_BONPREPARATION";

	/**
	 * Service rest de la brique livraison core: getBonCession.
	 */
	public static final String GET_BONCESSION = "GET_BONCESSION";

	/**
	 * Service rest de la brique livraison outil: getBonPreparation.
	 */
	public static final String GET_BONPREPARATION_INFO = "GET_BONPREPARATION_INFO";

	/**
	 * Service rest de la brique livraison core: initierBP.
	 */
	public static final String INITIER_BP = "INITIER_BP";

	/**
	 * Service rest de la brique livraison core: preparerBP.
	 */
	public static final String PREPARER_BP = "PREPARER_BP";

	/**
	 * Service rest de la brique livraison core: preparerBM.
	 */
	public static final String PREPARER_BM = "PREPARER_BM";

	/**
	 * Service rest de la brique livraison core: marquerBienPreparer.
	 */
	public static final String MARQUER_BPGLOBAL_PREPARE = "MARQUER_BPGLOBAL_PREPARE";

	/**
	 * Service rest de la brique livraison core: marquerBPGlobalLivre.
	 */
	public static final String MARQUER_BPGLOBAL_LIVRE = "MARQUER_BPGLOBAL_LIVRE";

	/**
	 * Service rest de la brique livraison core: getBRGlobalEncoursRecuperation.
	 */
	public static final String GET_BRGLOBAL_ENCOURS_RECUPERATION = "GET_BRGLOBAL_ENCOURS_RECUPERATION";

	/**
	 * Service rest de la brique livraison core: marquerRecupere.
	 */
	public static final String MARQUER_RECUPERE = "MARQUER_RECUPERE";

	/**
	 * Service rest de la brique livraison outil: getContratMigrationInfo.
	 */
	public static final String GET_CONTRAT_MIGRATION_INFO = "GET_CONTRAT_MIGRATION_INFO";

	/**
	 * Service rest de la brique contrat: initierBM.
	 */
	public static final String INITIER_BM = "INITIER_BM";

	/**
	 * Service rest de la brique livraison core: getBRGlobalEncoursRecuperation.
	 */
	public static final String GET_BMGLOBAL_ENCOURS_MIGRATION = "GET_BMGLOBAL_ENCOURS_MIGRATION";

	/**
	 * Service rest de la brique livraison core: marquerBMLivre.
	 */
	public static final String MARQUER_BM_LIVRE = "MARQUER_BM_LIVRE";

	/**
	 * Service rest de la brique livraison core: marquerBMRetourne.
	 */
	public static final String MARQUER_BM_RETOURNE = "MARQUER_BM_RETOURNE";

	/**
	 * Service rest de la brique livraison core: initierBC.
	 */
	public static final String INITIER_BC = "INITIER_BC";

	/**
	 * Service rest de la brique livraison core: marquerBCCede.
	 */
	public static final String MARQUER_BC_CEDE = "MARQUER_BC_CEDE";

	/**
	 * Service rest de la brique livraison core: getBonMigration.
	 */
	public static final String GET_BONMIGRATION = "GET_BONMIGRATION";

	/**
	 * Service rest de la brique livraison core: marquerBienMigrationPrepare.
	 */
	public static final String MARQUER_BMGLOBAL_PREPARE = "MARQUER_BMGLOBAL_PREPARE";

	/**
	 * Service rest de la brique livraison core: initierBRE.
	 */
	public static final String INITIER_BRE = "INITIER_BRE";

	/**
	 * Service rest de la brique livraison core: getBonRenouvellement.
	 */
	public static final String GET_BONRENOUVELLEMENT = "GET_BONRENOUVELLEMENT";

	/**
	 * Service rest de la brique livraison core: marquerBCCede.
	 */
	public static final String MARQUER_BRE_RENOUVELE = "MARQUER_BRE_RENOUVELE";

	/**
	 * Service rest de la brique livraison core: annulerBonPreparation.
	 */
	public static final String ANNUER_BONPREPARATION = "ANNUER_BONPREPARATION";

	/*******************************************************************************/
	/***************************** Facturation *************************************/
	/*******************************************************************************/

	/**
	 * Service rest de la brique contrat: getReferencesContratsGlobalValider.
	 */
	public static final String GET_REFERENCES_CONTRATS_GLOBAL_VALIDER = "GET_REFERENCES_CONTRATS_GLOBAL_VALIDER";

	/**
	 * Service rest de la brique contrat: getContratBillingInfo.
	 */
	public static final String GET_CONTRAT_BILLING_INFORMATION = "GET_CONTRAT_BILLING_INFORMATION";

	/**
	 * Service rest de la brique contrat: getReferencesContratLivrer.
	 */
	public static final String GET_REFERENCES_CONTRAT_LIVRER = "GET_REFERENCES_CONTRAT_LIVRER";

	/**
	 * Service rest de la brique contrat: changerDateDerniereFacture.
	 */
	public static final String CHANGER_DATE_DERNIERE_FACTURE = "CHANGER_DATE_DERNIERE_FACTURE";

	/**
	 * Service rest de la brique contrat: changerDateFactureResiliation.
	 */
	public static final String CHANGER_DATE_FACTURE_RESILIATION = "CHANGER_DATE_FACTURE_RESILIATION";

	/**
	 * Service rest de la brique contrat: changerDateFinContrat.
	 */
	public static final String CHANGER_DATE_FIN_CONTRAT = "CHANGER_DATE_FIN_CONTRAT";

	/**
	 * Service rest de la brique contrat: utiliserReduction.
	 */
	public static final String UTILISER_REDUCTION = "UTILISER_REDUCTION";

	/**
	 * Service rest de la brique contrat: getReduction.
	 */
	public static final String GET_REDUCTION = "GET_REDUCTION";

	/**
	 * Service rest de la brique contrat: getReduction.
	 */
	public static final String GET_REDUCTION_SANS_VERSION = "GET_REDUCTION_SANS_VERSION";

	/**
	 * Service rest de la brique contrat: getReductionParPeriode.
	 */
	public static final String GET_REDUCTION_PAR_PERIODE = "GET_REDUCTION_PAR_PERIODE";

	/**
	 * Service rest de la brique contrat: getContratBillingInformationResilierEnMigration.
	 */
	public static final String GET_CONTRAT_BILLING_INFORMATION_RESILIER_EN_MIGRATION =
			"GET_CONTRAT_BILLING_INFORMATION_RESILIER_EN_MIGRATION";

	/**
	 * Service rest de la brique contrat: getContratBillingInfoPourCession.
	 */
	public static final String GET_CONTRAT_BILLING_INFORMATION_POUR_CESSION =
			"GET_CONTRAT_BILLING_INFORMATION_POUR_CESSION";

	/**
	 * Service rest de la brique contrat: getContratBillingInformationPourMigration.
	 */
	public static final String GET_CONTRAT_BILLING_INFORMATION_POUR_MIGRATION =
			"GET_CONTRAT_BILLING_INFORMATION_POUR_MIGRATION";

	/**
	 * Service rest de la brique contrat: getContratBillingInformationHistorise.
	 */
	public static final String GET_CONTRAT_BILLING_INFORMATION_HISTORISE = "GET_CONTRAT_BILLING_INFORMATION_HISTORISE";

	/**
	 * Service rest de la brique contrat: getContratMigrationAdministrativeBillingInfo.
	 */
	public static final String GET_CONTRAT_MIGRATION_ADMINISTRATIVE_BILLING_INFORMATION =
			"GET_CONTRAT_MIGRATION_ADMINISTRATIVE_BILLING_INFORMATION";

	/*******************************************************************************/
	/***************************** NetDelivery *************************************/
	/*******************************************************************************/

	/**
	 * Service rest de la brique livraison core: marquerBienPreparer.
	 */
	public static final String MARQUER_BIEN_PREPARER = "MARQUER_BIEN_PREPARER";

	/**
	 * Service rest de la brique livraison core: getBiensEnCoursLivraison.
	 */
	public static final String GET_BIENS_ENCOURS_LIVRAISON = "GET_BIENS_ENCOURS_LIVRAISON";

	/**
	 * Service rest de la brique livraison core: marquerLivre(BonPreparation).
	 */
	public static final String MARQUER_SOUSBP_LIVRE = "MARQUER_SOUSBP_LIVRE";

	/**
	 * Service rest de la brique livraison core: marquerNonLivre.
	 */
	public static final String MARQUER_NON_LIVRE = "MARQUER_NON_LIVRE";

	/**
	 * Service rest de la brique billing outil: getPenaliteBillingInfo.
	 */
	public static final String GET_PENALITE_BILLING_INFO = "GET_PENALITE_BILLING_INFO";

	/**
	 * Service rest de la brique billing outil: getRemboursementBillingInfoMontantDefinit.
	 */
	public static final String GET_REMBOURSEMENT_BILLING_INFO_MONTANT_DEFINIT =
			"GET_REMBOURSEMENT_BILLING_INFO_MONTANT_DEFINIT";

	/**
	 * Service rest de la brique billing outil: getRemboursementBillingInfo.
	 */
	public static final String GET_REMBOURSEMENT_BILLING_INFO = "GET_REMBOURSEMENT_BILLING_INFO";

	/*******************************************************************************/
	/****************************** NetRetour **************************************/
	/*******************************************************************************/

	/**
	 * Service rest de la brique livraison core: getBiensEnCoursRecuperation.
	 */
	public static final String GET_BIENS_ENCOURS_RECUPERATION = "GET_BIENS_ENCOURS_RECUPERATION";

	/**
	 * Service rest de la brique livraison core: marquerRetourner.
	 */
	public static final String MARQUER_RETOURNER = "MARQUER_RETOURNER";

	/**
	 * Service rest de la brique livraison core: marquerECCede.
	 */
	public static final String MARQUER_EL_RENOUVELE = "MARQUER_EL_RENOUVELE";

	/*******************************************************************************/
	/****************************** Packager ***************************************/
	/*******************************************************************************/

	/**
	 * Service rest de la brique livraison core: marquerBienPreparer.
	 */
	public static final String MARQUER_SERVICE_PREPARER = "MARQUER_SERVICE_PREPARER";

	/**
	 * Service rest de la brique livraison core: getServicesEnCoursActivation.
	 */
	public static final String GET_SERVICES_ENCOURS_ACTIVATION = "GET_SERVICES_ENCOURS_ACTIVATION";

	/**
	 * Service rest de la brique livraison core: getServicesEnCoursSuspension.
	 */
	public static final String GET_SERVICES_ENCOURS_SUSPENSION = "GET_SERVICES_ENCOURS_SUSPENSION";

	/**
	 * Service rest de la brique livraison core: getServicesSuspendu.
	 */
	public static final String GET_SERVICES_SUSPENDU = "GET_SERVICES_SUSPENDU";

	/**
	 * Service rest de la brique livraison core: marquerSuspendu.
	 */
	public static final String MARQUER_SUSPENDU = "MARQUER_SUSPENDU";

	/**
	 * Service rest de la brique livraison core: marquerNonMigre.
	 */
	public static final String MARQUER_NON_MIGRE = "MARQUER_NON_MIGRE";

	/**
	 * Service rest de la brique contrat core: getPackagerInfo.
	 */
	public static final String GET_PACKAGER_INFO = "GET_PACKAGER_INFO";

	/**
	 * Service rest de la brique livraison core: getElementLivraison.
	 */
	public static final String GET_ELEMENT_LIVRAISON = "GET_ELEMENT_LIVRAISON";

	/**
	 * Service rest de la brique livraison core: findElementRecuperation.
	 */
	public static final String GET_ER = "GET_ER";

	/**
	 * Service rest de la brique contrat: getParentInfo.
	 */
	public static final String GET_PARENT_INFO = "GET_PARENT_INFO";

	/**
	 * Service rest de la brique contrat: isElementContractuelResilier.
	 */
	public static final String IS_ELEMENT_CONTRACTUEL_RESILIER = "IS_ELEMENT_CONTRACTUEL_RESILIER";

	/**
	 * Service rest de la brique contrat: getFilsService.
	 */
	public static final String GET_FILS_SERVICE = "GET_FILS_SERVICE";

	/**
	 * Service rest de la brique contrat: checkIsPackagerCreationPossible.
	 */
	public static final String CHECK_IS_PACKAGER_CREATION_POSSIBLE = "CHECK_IS_PACKAGER_CREATION_POSSIBLE";

	/**
	 * Service rest de la brique livraison core: marquerECCede.
	 */
	public static final String MARQUER_EC_CEDE = "MARQUER_EC_CEDE";

	/**
	 * Service rest de la brique livraison core pour marquer un élément de migration comme livré.
	 */
	public static final String MARQUER_EM_LIVRE = "MARQUER_EM_LIVRE";

	/**
	 * Service rest de la brique livraison core pour marquer un élément de migration comme retourne.
	 */
	public static final String MARQUER_EM_RETOURNE = "MARQUER_EM_RETOURNE";

	/**
	 * recherche le reference du bon de preparation parent.
	 */
	public static final String GET_REF_PARENT = "GET_REF_PARENT";

	/**
	 * Service de la brique contrat core : getReferenceCommande.
	 */
	public static final String GET_REFERENCE_COMMANDE = "GET_REFERENCE_COMMANDE";

	/*******************************************************************************/
	/****************************** Packager ***************************************/
	/*******************************************************************************/

	/**
	 * Service rest de la brique livraison core pour recuperer la liste des elements de migration.
	 */
	public static final String GET_BIEN_ENCOURS_MIGRATION = "GET_BIEN_ENCOURS_MIGRATION";

	/**
	 * Service rest de la brique livraison core pour recuperer la liste des elements de migration.
	 */
	public static final String GET_BIEN_ENCOURS_RETOUR = "GET_BIEN_ENCOURS_RETOUR";

	/**
	 * Service rest de la brique livraison core pour marquer un élément de migration comme retourné.
	 */
	public static final String MARQUER_EM_RETOUR = "MARQUER_EM_RETOUR";

	/**
	 * Service rest de la brique livraison core: marquerBienMigrationPrepare.
	 */
	public static final String MARQUER_BIEN_MIGRATION_PREPARE = "MARQUER_BIEN_MIGRATION_PREPARE";

	/**
	 * Service rest de la brique contrat core: getInfoAvantCession.
	 */
	public static final String GET_INFO_AVANT_CESSION = "GET_INFO_AVANT_CESSION";

	/**
	 * Service rest de la brique livraison core: getBonRecuperation.
	 */
	public static final String GET_BONRECUPERATION = "GET_BONRECUPERATION";

	/*******************************************************************************/
	/****************************** Contrat ****************************************/
	/*******************************************************************************/

	/**
	 * Service de la brique contrat-outil : validerSerialNumber.
	 */
	public static final String VALIDER_SERIAL_NUMBER = "VALIDER_SERIAL_NUMBER";

	/**
	 * Service rest de la brique contrat util: getElementStateInformation.
	 */
	public static final String GET_ELEMENTS_STATE = "GET_ELEMENTS_STATE";

	/**
	 * Service reset de la brique contrat core: getContratByReference.
	 */
	public static final String GET_CONTRAT_BY_REFERENCE = "GET_CONTRAT_BY_REFERENCE";

	/*******************************************************************************/
	/**************************** Contrat Outil *************************************/
	/*******************************************************************************/

	/**
	 * Service rest de la brique contrat: getContratAvenantInfo.
	 */
	public static final String GET_CONTRAT_AVENANT_INFO = "GET_CONTRAT_AVENANT_INFO";

	/**
	 * Service rest de la brique contrat: getContratBPHistorique.
	 */
	public static final String GET_CONTRATBP_HISTORIQUE = "GET_CONTRATBP_HISTORIQUE";

	/**
	 * Service rest de la brique livraison core: getElementsCodeProduit.
	 */
	public static final String GET_ELEMENTS_CODE_PRODUIT = "GET_ELEMENTS_CODE_PRODUIT";

	/**
	 * Service rest de la brique livraison core: getElementsCodeColis.
	 */
	public static final String GET_ELEMENTS_CODE_COLIS = "GET_ELEMENTS_CODE_COLIS";

}
