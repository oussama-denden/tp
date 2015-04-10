package com.nordnet.topaze.contrat.business;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe regroupe les informations qui definissent un {@link ContratResiliationtInfo}. Ces informations seront
 * utilisée pour la résiliation d'un contrat.
 * 
 * @author anisselmane.
 * 
 */
public class ContratResiliationtInfo {

	/**
	 * liste de contrats a resilier a la fois.
	 */
	List<String> contrats = new ArrayList<>();

	/**
	 * politique résiliation.
	 */
	private PolitiqueResiliation politiqueResiliation;

	/**
	 * l'usager.
	 */
	private String user;

	/**
	 * constructeur par defaut.
	 */
	public ContratResiliationtInfo() {

	}

	/* Getters & Setters */

	/**
	 * @return {@link #contrats}.
	 */
	public List<String> getContrats() {
		return contrats;
	}

	/**
	 * @param contrats
	 *            {@link #contrats}.
	 */
	public void setContrats(List<String> contrats) {
		this.contrats = contrats;
	}

	/**
	 * Gets the politique resiliation.
	 * 
	 * @return the politique resiliation
	 */
	public PolitiqueResiliation getPolitiqueResiliation() {
		return politiqueResiliation;
	}

	/**
	 * Sets the politique resiliation.
	 * 
	 * @param politiqueResiliation
	 *            the new politique resiliation
	 */
	public void setPolitiqueResiliation(PolitiqueResiliation politiqueResiliation) {
		this.politiqueResiliation = politiqueResiliation;
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

}
