package com.nordnet.topaze.contrat.outil.mock;

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
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
			product = new ArrayList<>();
		}
		return this.product;
	}
}
