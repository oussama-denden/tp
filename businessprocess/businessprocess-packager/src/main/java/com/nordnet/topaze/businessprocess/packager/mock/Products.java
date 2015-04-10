package com.nordnet.topaze.businessprocess.packager.mock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class Products implements Serializable {

	/**
	 * liste de produits
	 */
	protected List<TProductInstance> product;

	/**
	 * constructeur par defaut
	 */

	public Products() {

	}

	/* GETTERS AND SETTERS */

	/**
	 * get the product
	 * 
	 * @return {@link #product}
	 */
	public List<TProductInstance> getProduct() {
		if (product == null) {
			product = new ArrayList<TProductInstance>();
		}
		return this.product;
	}
}
