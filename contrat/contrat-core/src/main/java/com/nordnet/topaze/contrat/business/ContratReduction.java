package com.nordnet.topaze.contrat.business;

import com.nordnet.topaze.contrat.domain.Reduction;

/**
 * Cette classe regroupe les informations qui definissent un {@link ContratReduction}.
 * 
 * @author anisselmane.
 * 
 */
public class ContratReduction {

	/**
	 * informations sur la reduction.
	 */
	private Reduction reduction;

	/**
	 * L'usager.
	 */
	private String user;

	/**
	 * constructeur par defaut.
	 */
	public ContratReduction() {

	}

	@Override
	public String toString() {
		return "ContratReduction [reduction=" + reduction + ", user=" + user + "]";
	}

	/**
	 * 
	 * @return {@link Reduction}.
	 */
	public Reduction getReduction() {
		return reduction;
	}

	/**
	 * 
	 * @param reduction
	 *            {@link Reduction}.
	 */
	public void setReduction(Reduction reduction) {
		this.reduction = reduction;
	}

	/**
	 * 
	 * @return {@link #user}.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * 
	 * @param user
	 *            {@link #user}.
	 */
	public void setUser(String user) {
		this.user = user;
	}

}
