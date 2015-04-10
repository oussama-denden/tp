package com.nordnet.topaze.client.rest.business.util;

import java.text.SimpleDateFormat;

/**
 * Des Constantes relier a la facturation.
 * 
 * @author akram-moncer.
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
	 * l'index du label du produit dans le titre du contrat apres sa division par la method {@link String#split(String)}
	 * en utilisant le caractere ':'.
	 */
	public static final int INDEX_LABEL = 1;

	/**
	 * le premier jour d'un mois et qui vaut 1.
	 */
	public static final int PREMIER_JOUR_DU_MOIS = 1;

	/**
	 * Le chemin du fichier env.properties.
	 */
	public static final String ENVIRONEMENT = "/webapps/env.properties";

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

	public static final String CHAINE_VIDE = "";

	/**
	 * L'ID du produit.
	 */
	public static final String PRODUCT_ID = "productId";

	public static final String INTERNAL_USER = "TOPAZE";
}
