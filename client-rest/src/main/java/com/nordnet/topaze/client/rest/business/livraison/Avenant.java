package com.nordnet.topaze.client.rest.business.livraison;

import java.util.Date;
import java.util.List;

/**
 * Cette classe regroupe les informations qui definissent les informations d'un {@link Avenant}.
 * 
 * @author Oussama Denden.
 * 
 */
public class Avenant {

	/**
	 * Les valeurs a modifier.
	 */
	private List<ElementLivraison> contratModifications;

	/**
	 * Date modification.
	 */
	private Date dateAction;

	/**
	 * L'usager.
	 */
	private String user;

	/**
	 * constructeur par defaut.
	 */
	public Avenant() {

	}

	/**
	 * 
	 * @return {@link #dateAction}.
	 */
	public Date getDateAction() {
		return dateAction;
	}

	/**
	 * 
	 * @param dateAction
	 *            {@link #dateAction}.
	 * 
	 */
	public void setDateAction(Date dateAction) {
		this.dateAction = dateAction;
	}

	/**
	 * Gets the user.
	 * 
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 * 
	 * @param user
	 *            the new user
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return {@link #contratModifications}.
	 */
	public List<ElementLivraison> getContratModifications() {
		return contratModifications;
	}

	/**
	 * @param contratModifications
	 *            {@link #contratModifications}
	 */
	public void setContratModifications(List<ElementLivraison> contratModifications) {
		this.contratModifications = contratModifications;
	}

}
