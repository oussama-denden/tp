package com.nordnet.topaze.businessprocess.core.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

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
	 * L'ID du produit.
	 */
	public static final String PRODUCT_ID = "productId";

	public static final String OPTION_PLUS = "Process_OptionPlus";

	/**
	 * la reference d'un produit pour l'option plus 20G.
	 */
	public static final String REFERENCE_OPTION_PLUS_20G = System.getProperty("reference.option.plus.20g");

	/**
	 * la reference d'un produit pour l'option plus 50G.
	 */
	public static final String REFERENCE_OPTION_PLUS_50G = System.getProperty("reference.option.plus.50g");

	/**
	 * la reference d'un produit domain.
	 */
	public static final String REFERENCE_DOMAIN = System.getProperty("reference.domain");

	/**
	 * la reference d'un produit jet.
	 */
	public static final String REFERENCE_JET = System.getProperty("reference.jet");

	/**
	 * la reference d'un produit max.
	 */
	public static final String REFERENCE_MAX = System.getProperty("reference.max");

	/**
	 * la reference d'un produit jet.
	 */
	public static final String REFERENCE_KIT_SAT = System.getProperty("reference.kit.sat");

	/**
	 * la reference d'un produit jet.
	 */
	public static final String REFERENCE_VOIP = System.getProperty("reference.voip");

	/**
	 * liste des references des produit service option plus.
	 */
	public static final List<String> REFERENCES_OPTION_PLUS = Arrays.asList(System
			.getProperty("references.option.plus").trim().split(","));

	/**
	 * le code erreur par defaut si une exception interne est leve.
	 */
	public static final String CODE_ERREUR_PAR_DEFAUT = "0.3";

	/**
	 * le code erreur pour une requette syntaxiquement incorrect.
	 */
	public static final String CODE_ERREUR_SYNTAXE = "0.4";

}
