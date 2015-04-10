package com.nordnet.topaze.client.rest.business.livraison;

import java.util.Date;

/**
 * Changement de date debut facturation.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class ChangerDateFacturationInfos {

	/**
	 * Date debut facturation.
	 */
	private Date dateDebutFacturation;

	/**
	 * Constructeur.
	 * 
	 * @param dateDebutFacturation
	 *            date debut fact.
	 */
	public ChangerDateFacturationInfos(final Date dateDebutFacturation) {
		this.dateDebutFacturation = dateDebutFacturation;
	}

	/**
	 * constructeur par defaut.
	 */
	public ChangerDateFacturationInfos() {

	}

	/* Getters & Setters */

	/**
	 * @return {@link dateDebutFacturation}.
	 */
	public Date getDateDebutFacturation() {
		return dateDebutFacturation;
	}

	/**
	 * @param dateDebutFacturation
	 *            {@link dateDebutFacturation}.
	 */
	public void setDateDebutFacturation(Date dateDebutFacturation) {
		this.dateDebutFacturation = dateDebutFacturation;
	}

}