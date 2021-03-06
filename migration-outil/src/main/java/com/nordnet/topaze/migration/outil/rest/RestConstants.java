package com.nordnet.topaze.migration.outil.rest;

/**
 * Test Constants.
 * 
 * @author Oussama Denden
 * 
 */
public final class RestConstants {

	/**
	 * constructeur par default.
	 */
	private RestConstants() {

	}

	/**
	 * Brique Contrat path.
	 */
	public static final String BRIQUE_CONTRAT = System.getProperty("contrat.url");

	/**
	 * Brique billing outil path.
	 */
	public static final String BILLING_OUTIL = System.getProperty("billingOutil.url");

	/**
	 * Service rest de la brique contrat: getContratInfoPourSimulationMigration.
	 */
	public static final String GET_CONTRAT_INFO_POUR_SIMULATION_MIGRATION =
			"GET_CONTRAT_INFO_POUR_SIMULATION_MIGRATION";

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

}
