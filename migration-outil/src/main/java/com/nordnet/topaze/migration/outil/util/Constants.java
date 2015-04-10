package com.nordnet.topaze.migration.outil.util;

import java.text.SimpleDateFormat;

/**
 * Test Constants.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public final class Constants {

	/**
	 * constructeur par default.
	 */
	private Constants() {

	}

	/**
	 * DEFAULT_DATE_DEBUT_FACTURATION.
	 */
	public static final String DEFAULT_DATE_DEBUT_FACTURATION = "DEFAULT_DATE_DEBUT_FACTURATION";

	/**
	 * Default date format : yyyy-MM-dd HH:mm:ss.
	 */
	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * Default date format witout time : yyyy-MM-dd.
	 */
	public static final SimpleDateFormat DEFAULT_DATE_WITHOUT_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * Entier de valeur 0.
	 */
	public static final int ZERO = 0;

	/**
	 * Entier de valeur 1.
	 */
	public static final int UN = 1;

	/**
	 * Entier de valeur 2.
	 */
	public static final int DEUX = 2;

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
	public static final String PROD_ENV = "prod";

	/**
	 * L'ID du produit.
	 */
	public static final String PRODUCT_ID = "productId";

	/**
	 * le code erreur par defaut si une exception interne est leve.
	 */
	public static final String CODE_ERREUR_PAR_DEFAUT = "0.3";

	/**
	 * le code erreur pour une requette syntaxiquement incorrect.
	 */
	public static final String CODE_ERREUR_SYNTAXE = "0.4";

}
