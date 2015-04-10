package com.nordnet.topaze.businessprocess.netdelivery.util;

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
	 * delai maximale de livraison.
	 */
	public static final Integer DELAI_LIVRAISON = 15;

	/**
	 * valeur de delai.
	 */
	public static final Integer EST_DELAI = Integer.valueOf(System.getProperties().getProperty("estDelai"));

	/**
	 * L'ID du produit.
	 */
	public static final String PRODUCT_ID = "productId";

	/**
	 * Lors de l'appel à NetDelivery, il faut fournir le paramètre suivant : packager.retailerPackagerId = {refContrat}.
	 */
	public static final String PACKAGER_RETAILER_PACKAGER_ID = "packager.RetailerPackagerId";

	/**
	 * Topaze User.
	 */
	public static final String INTERNAL_USER = "TOPAZE";

	/**
	 * product constant.
	 */
	public static final String PRODUCT = "product";

}
