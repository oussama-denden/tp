package com.nordnet.topaze.client.rest.business.contrat;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.nordnet.topaze.client.rest.enums.TState;

/**
 * cette classe lie un element contractuelle avec son etat dans packager
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class ElementStateInfo {

	/**
	 * reference de l'element contractuelle
	 */
	private String refenceElementContractuelle;

	/**
	 * etat de l'element dans packager pour les services
	 */
	private TState state;

	/**
	 * le code de produit pour un element contractuelle
	 */
	private String codeProduit;

	/**
	 * le code de colis pour un element contractuelle
	 */
	private String codeColis;

	/**
	 * indique si un element contractuelle de type bien est prepare pour etre retourné
	 */
	private Boolean preparerPourRetour;

	/**
	 * indique si un element contractuelle de type bien est deja retourné
	 */
	private Boolean retourne;

	/**
	 * constructeur par default.
	 */
	public ElementStateInfo() {

	}

	/**
	 * gets the refrence element contractuelle
	 * 
	 * @return {@link #RefenceElementContractuelle}
	 */
	public String getRefenceElementContractuelle() {
		return refenceElementContractuelle;
	}

	/**
	 * sets the reference element contractuelle
	 * 
	 * @param refenceElementContractuelle
	 *            {@link #RefenceElementContractuelle}
	 */
	public void setRefenceElementContractuelle(String refenceElementContractuelle) {
		this.refenceElementContractuelle = refenceElementContractuelle;
	}

	/**
	 * get the state
	 * 
	 * @return {@link #state}
	 */
	public TState getState() {
		return state;
	}

	/**
	 * set the state
	 * 
	 * @param state
	 *            {@link #state}
	 */
	public void setState(TState state) {
		this.state = state;
	}

	/**
	 * set the state
	 * 
	 * @param state
	 *            {@link #state}
	 */
	@JsonIgnore
	public void setTState(String state) {
		this.state = TState.fromValue(state);
	}

	/**
	 * get the code produit
	 * 
	 * @return {@link #codeProduit}
	 */
	public String getCodeProduit() {
		return codeProduit;
	}

	/**
	 * sets the code produit
	 * 
	 * @param codeProduit
	 *            the new {@link #codeProduit}
	 */
	public void setCodeProduit(String codeProduit) {
		this.codeProduit = codeProduit;
	}

	/**
	 * gets the code colis
	 * 
	 * @return {@link #codeColis}
	 */
	public String getCodeColis() {
		return codeColis;
	}

	/**
	 * sets new code colis
	 * 
	 * @param codeColis
	 *            the new {@link #codeColis}
	 */
	public void setCodeColis(String codeColis) {
		this.codeColis = codeColis;
	}

	/**
	 * get the preparerPourRetour
	 * 
	 * @return {@link #preparerPourRetour}
	 */
	public Boolean getPreparerPourRetour() {
		return preparerPourRetour;
	}

	/**
	 * set the preparerPourRetour
	 * 
	 * @param preparerPourRetour
	 *            the new {@link #preparerPourRetour}
	 */
	public void setPreparerPourRetour(Boolean preparerPourRetour) {
		this.preparerPourRetour = preparerPourRetour;
	}

	/**
	 * get the retourne
	 * 
	 * @return {@link #retourne}
	 */
	public Boolean getRetourne() {
		return retourne;
	}

	/**
	 * set the new retourne
	 * 
	 * @param retourne
	 *            the new retourne {@link #retourne}
	 */
	public void setRetourne(Boolean retourne) {
		this.retourne = retourne;
	}

}
