package com.nordnet.topaze.businessprocess.netretour.util;

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
	 * L'ID du produit.
	 */
	public static final String PRODUCT_ID = "productId";

	/**
	 * User TOPAZE.
	 */
	public static final String INTERNAL_USER = "TOPAZE";

	/**
	 * product constant.
	 */
	public static final String PRODUCT = "product";

}
