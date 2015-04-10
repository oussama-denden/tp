package com.nordnet.topaze.contrat.outil.mock;

import java.io.Serializable;

/**
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class TProductInstance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * produit id
	 */
	protected Long productId;

	/**
	 * constructeur par defaut
	 */
	public TProductInstance() {

	}

	/* Getters ans Setters */

	/**
	 * Obtient la valeur de la propriété productId.
	 * 
	 * @return possible object is {@link Long }
	 * 
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * Définit la valeur de la propriété productId.
	 * 
	 * @param value
	 *            allowed object is {@link Long }
	 * 
	 */
	public void setProductId(Long value) {
		this.productId = value;
	}

}
