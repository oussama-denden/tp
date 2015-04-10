package com.nordnet.topaze.livraison.core.util;

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
	 * product id.
	 */
	public static final String PRODUCT_ID = "productId";

	/**
	 * entier de valeur 0.
	 */
	public static final Integer ZERO = 0;

	/**
	 * le code erreur par defaut si une exception interne est leve.
	 */
	public static final String CODE_ERREUR_PAR_DEFAUT = "0.3";

	/**
	 * le code erreur pour une requette syntaxiquement incorrect.
	 */
	public static final String CODE_ERREUR_SYNTAXE = "0.4";

	/**
	 * product constant.
	 */
	public static final String PRODUCT = "product";

}
