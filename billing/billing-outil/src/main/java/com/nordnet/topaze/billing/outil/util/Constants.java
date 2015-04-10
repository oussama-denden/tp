package com.nordnet.topaze.billing.outil.util;

import java.text.SimpleDateFormat;

/**
 * Test constants.
 * 
 * @author anisselmane.
 * 
 */
public final class Constants {

	/**
	 * Default constructor.
	 */
	private Constants() {
		super();
	}

	/**
	 * Default date format : yyyy-MM-dd.
	 */
	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * motive de non livraison.
	 */
	public static final String MOTIVE = "motive de non livraison";

	/**
	 * Clé de la propertie de la date de jour.
	 */
	public static final String DATE_DU_JOUR_PROPERTY = "dateDuJour";

	/**
	 * set date du jour au date system.
	 */
	public static final String NOW = "[now]";

	/**
	 * Clé de la propertie env.
	 */
	public static final String ENV_PROPERTY = "env";

	/**
	 * Clé de la propertie prod.
	 */
	public static final Object PROD_ENV = "prod";

	/**
	 * L'utilisateur interne de topaze.
	 */
	public static final String INTERNAL_USER = "TOPAZE";

	/**
	 * product Id.
	 */
	public static final String PRODUCT_ID = "productId";

	/**
	 * entier de valeur 0.
	 */
	public static final Integer ZERO = 0;

	/**
	 * Entier de valeur 1.
	 */
	public static final int UN = 1;

	/**
	 * Entier de valeur 2.
	 */
	public static final int DEUX = 2;

	/**
	 * Entier de valeur 3.
	 */
	public static final int TROIS = 3;

	/**
	 * Entier de valeur 12.
	 */
	public static final int DOUZE = 12;

	/**
	 * Entier de valeur 100.
	 */
	public static final int CENT = 100;

	/**
	 * le pourcentage que le client paye en penalite apres les 12 premier mois d'engagement est egal a 25%.
	 */
	public static final double POURCENTAGE_ENGAGEMENT = 0.25;

	/**
	 * le premier jour d'un mois et qui vaut 1.
	 */
	public static final int PREMIER_JOUR_DU_MOIS = 1;

	/**
	 * le code erreur par defaut si une exception interne est leve.
	 */
	public static final String CODE_ERREUR_PAR_DEFAUT = "0.3";

	/**
	 * le code erreur pour une requette syntaxiquement incorrect.
	 */
	public static final String CODE_ERREUR_SYNTAXE = "0.4";

}