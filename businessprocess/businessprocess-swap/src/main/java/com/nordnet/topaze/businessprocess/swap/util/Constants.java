package com.nordnet.topaze.businessprocess.swap.util;

import java.text.SimpleDateFormat;

/**
 * Test constants.
 * 
 * @author mahjoub-marzougui.
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
	 * l'index du label du produit dans le titre du contrat apres sa division par la method {@link String#split(String)}
	 * en utilisant le caractere ':'.
	 */
	public static final int INDEX_LABEL = 1;

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
	 * entier de valeur 1.
	 */
	public static final Integer UN = 1;

	/**
	 * entier de valeur 0.
	 */
	public static final Integer ZERO = 0;

	/**
	 * L'utilisateur interne de topaze.
	 */
	public static final String INTERNAL_USER = "TOPAZE";

	/**
	 * L'ID du produit.
	 */
	public static final String PRODUCT_ID = "productId";

	/**
	 * entier de valeur 15.
	 */
	public static final Integer QUINZE = 15;

	/**
	 * product constant.
	 */
	public static final String PRODUCT = "product";

}
