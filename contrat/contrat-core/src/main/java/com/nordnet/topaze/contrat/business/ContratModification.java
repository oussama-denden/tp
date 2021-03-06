package com.nordnet.topaze.contrat.business;

/**
 * Cette classe regroupe les informations qui definissent un {@link ContratModification}.
 * 
 * @author anisselmane.
 * 
 */
public class ContratModification {

	/**
	 * L action de modification.
	 */
	private String action;

	/**
	 * la reference du contrat a modifier.
	 */
	private String refContrat;

	/**
	 * numero element contractuelle
	 */
	private Integer numEC;

	/**
	 * La valeur a modifier.
	 */
	private String valeur;

	/**
	 * constructeur par defaut.
	 */
	public ContratModification() {

	}

	/**
	 * Getter l action de modification.
	 * 
	 * @return l action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Setter l action de modification.
	 * 
	 * @param action
	 *            action
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Getter la reference contrat.
	 * 
	 * @return reference contrat
	 */
	public String getRefContrat() {
		return refContrat;
	}

	/**
	 * Setter la reference contrat.
	 * 
	 * @param refContrat
	 *            reference contrat
	 */
	public void setRefContrat(String refContrat) {
		this.refContrat = refContrat;
	}

	/**
	 * gets the numero element contractuelle
	 * 
	 * @return {@link #numEC}
	 */
	public Integer getNumEC() {
		return numEC;
	}

	/**
	 * sets the numero element contractuelle
	 * 
	 * @param numEC
	 *            the new {@link #numEC}
	 */
	public void setNumEC(Integer numEC) {
		this.numEC = numEC;
	}

	/**
	 * Getter la valeur.
	 * 
	 * @return valeur
	 */
	public String getValeur() {
		return valeur;
	}

	/**
	 * Setter la valeur.
	 * 
	 * @param valeur
	 *            valeur
	 */
	public void setValeur(String valeur) {
		this.valeur = valeur;
	}

}
