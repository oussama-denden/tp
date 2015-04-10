package com.nordnet.topaze.client.rest.business.livraison;

/**
 * Classe sert a envoye les donnees vers la brique businessprocess-packager.
 * 
 * @author akram-moncer
 * 
 */
public class PackagerInfo {

	/**
	 * Model du produit dans Packager.
	 */
	private String model;

	/**
	 * La configuration de la template xml.
	 */
	private String configuration;

	/**
	 * Constructeur par defaut.
	 */
	public PackagerInfo() {

	}

	/**
	 * 
	 * @return {@link #model}
	 */
	public String getModel() {
		return model;
	}

	/**
	 * 
	 * @param model
	 *            {@link #model}
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * 
	 * @return {@link #configuration}
	 */
	public String getConfiguration() {
		return configuration;
	}

	/**
	 * 
	 * @param configuration
	 *            {@link #configuration}
	 */
	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

}
