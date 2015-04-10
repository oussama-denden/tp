package com.nordnet.topaze.client.rest.business.contrat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * objet contenent les infos du contrat.
 * 
 * @author akram-moncer
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contrat {

	/**
	 * reference contrat.
	 */
	private String reference;

	/**
	 * titre du contrat.
	 */
	private String titre;

	/**
	 * constructeur par defaut.
	 */
	public Contrat() {
	}

	/**
	 * 
	 * @return {@link #reference}.
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * 
	 * @param reference
	 *            {@link #reference}.
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * 
	 * @return {@link #titre}.
	 */
	public String getTitre() {
		return titre;
	}

	/**
	 * 
	 * @param titre
	 *            {@link #titre}.
	 */
	public void setTitre(String titre) {
		this.titre = titre;
	}

}
