package com.nordnet.topaze.contrat.business;

import java.util.Date;
import java.util.List;

/**
 * Cette classe regroupe les informations qui definissent les informations d'un {@link ContratAvenantInfo}.
 * 
 * @author Oussama Denden.
 * 
 */
public class ContratAvenantInfo {

	/**
	 * Les valeurs a modifier.
	 */
	private List<ContratBP> contratModifications;

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
	public ContratAvenantInfo() {

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
	public List<ContratBP> getContratModifications() {
		return contratModifications;
	}

	/**
	 * @param contratModifications
	 *            {@link #contratModifications}
	 */
	public void setContratModifications(List<ContratBP> contratModifications) {
		this.contratModifications = contratModifications;
	}

}
