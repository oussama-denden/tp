package com.nordnet.topaze.businessprocess.packager.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

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
	 * Chaine de caractere de valeur "create".
	 */
	public static final String CREATE = "create";

	/**
	 * is packager creation possible.
	 */
	public static final Integer EST_POSSIBLE = Integer.valueOf(System.getProperties().getProperty("estPossible"));

	/**
	 * use QOS in packager request.
	 */
	public static final String USE_QOS_INPACKAGER_TRAME = "Process_useQoSInPackagerTrame";

	/**
	 * process.
	 */
	public static final String PROCESS = "Process";

	/**
	 * option plus.
	 */
	public static final String OPTION_PLUS = "Process_OptionPlus";

	/**
	 * freezone.
	 */
	public static final String FREEZONE = "freezone";

	/**
	 * la reference d'un produit pour l'option plus 20G.
	 */
	public static final String REFERENCE_OPTION_PLUS_20G = System.getProperty("reference.option.plus.20g").trim();

	/**
	 * la reference d'un produit pour l'option plus 50G.
	 */
	public static final String REFERENCE_OPTION_PLUS_50G = System.getProperty("reference.option.plus.50g").trim();

	/**
	 * la reference d'un produit domain.
	 */
	public static final String REFERENCE_DOMAIN = System.getProperty("reference.domain").trim();

	/**
	 * la reference d'un produit jet.
	 */
	public static final String REFERENCE_JET = System.getProperty("reference.jet").trim();

	/**
	 * la reference d'un produit max.
	 */
	public static final String REFERENCE_MAX = System.getProperty("reference.max").trim();

	/**
	 * la reference d'un produit jet.
	 */
	public static final String REFERENCE_KIT_SAT = System.getProperty("reference.kit.sat").trim();

	/**
	 * la reference d'un produit jet.
	 */
	public static final String REFERENCE_VOIP = System.getProperty("reference.voip").trim();

	/**
	 * liste des references des produit service option plus.
	 */
	public static final List<String> REFERENCES_OPTION_PLUS = Arrays.asList(System
			.getProperty("references.option.plus").trim().split(","));

	/**
	 * reference d'une freezone.
	 */
	public static final String REFERENCE_FREEZONE = System.getProperty("reference.option.freezone").trim();

	public static final String CLIENT_FIRST_NAME = "Client_FirstName";

	public static final String CLIENT_LAST_NAME = "Client_LastName";

	public static final String CLIENT_EMAIL = "Client_Email";

	public static final String CLIENT_ADDRESS_1 = "Client_Address1";

	public static final String CLIENT_ADDRESS_2 = "Client_Address2";

	public static final String CLIENT_ZIP = "Client_ZipCode";

	public static final String CLIENT_CITY = "Client_City";

	public static final String CLIENT_COUNTRY = "Client_Country";

	public static final String CLIENT_PROFILE_TYPE = "Client_ProfileType";

	public static final String CLIENT_CIVILITY = "Client_Civility";

	public static final String CLIENT = "Client";

	public static final String ORDER = "Order";

	/**
	 * entier de valeur 3.
	 */
	public static final int TROIS = 3;

	/**
	 * entier de valeur 0.
	 */
	public static final int ZERO = 0;

	/**
	 * cause de non livraison: template xml absente.
	 */
	public static final String TEMPLATE_XML_ABSENT = "templateXML du produit absent";

	/**
	 * cause de non livraison: parent non livrer.
	 */
	public static final String PARENT_NON_LIVRER = "parent_non_livrer";

	public static final String TEMPLATE_PATH_FOLDER = System.getProperty("templatePathFolder");

	public static final String PRODUCT_ID = "productId";

	public static final String INTERNAL_USER = "TOPAZE";

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
	 * Duree apres laquelle un cancel packager sera effectuer.
	 */
	public static final String DELAI_CANCEL = "_delai.cancel";

	/**
	 * Reference initiale.
	 */
	public static final String REF_INIT = "00000001";

	/**
	 * contenu d'une template vide.
	 */
	public static final String TEMPLATE_VIDE = "";

	/**
	 * prefix du retailer Packager Id.
	 */
	public static final String PREFIX_RETAILER_PACKAGER_ID = Utils.isStringNullOrEmpty(System
			.getProperty("retailer.packager.id.prefix")) ? "" : System.getProperty("retailer.packager.id.prefix") + "-";

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
