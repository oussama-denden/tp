package com.nordnet.topaze.businessprocess.packager.mock;

import java.io.IOException;
import java.io.Serializable;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.nordnet.packager.types.packager.TState;

/**
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class TPackagerInstance implements Serializable {

	protected String retailerPackagerId;
	protected String packagerModel;
	protected TState currentState;
	protected Products products;

	/**
	 * Obtient la valeur de la propriété retailerPackagerId.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRetailerPackagerId() {
		return retailerPackagerId;
	}

	/**
	 * Définit la valeur de la propriété retailerPackagerId.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRetailerPackagerId(String value) {
		this.retailerPackagerId = value;
	}

	/**
	 * Obtient la valeur de la propriété packagerModel.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPackagerModel() {
		return packagerModel;
	}

	/**
	 * Définit la valeur de la propriété packagerModel.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPackagerModel(String value) {
		this.packagerModel = value;
	}

	/**
	 * Obtient la valeur de la propriété currentState.
	 * 
	 * @return possible object is {@link TState }
	 * 
	 */
	public TState getCurrentState() {
		return currentState;
	}

	/**
	 * Définit la valeur de la propriété currentState.
	 * 
	 * @param value
	 *            allowed object is {@link TState }
	 * 
	 */
	public void setCurrentState(TState value) {
		this.currentState = value;
	}

	/**
	 * Obtient la valeur de la propriété products.
	 * 
	 * @return possible object is {@link Products }
	 * 
	 */
	public Products getProducts() {
		return products;
	}

	/**
	 * Définit la valeur de la propriété products.
	 * 
	 * @param value
	 *            allowed object is {@link Products }
	 * 
	 */
	public void setProducts(Products value) {
		this.products = value;
	}

	/**
	 * 
	 * @param tPackagerInstance
	 *            le {@link TPackagerInstance} a transforme en objet {@link JSONObject}
	 * @return {@link JSONObject}
	 * @throws IOException
	 *             {@link IOException}
	 */
	public static String toJson(TPackagerInstance tPackagerInstance) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(tPackagerInstance);

	}

	/**
	 * 
	 * @param json
	 *            l'objet {@link TPackagerInstance} en format json.
	 * @return {@link TPackagerInstance}
	 * @throws IOException
	 *             {@link IOException}
	 */
	public static TPackagerInstance fromJsonToTPackagerInstance(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(json, TPackagerInstance.class);
	}

}
