package com.nordnet.topaze.communication.adapter.business;

/**
 * contient les info client retourne par netcustomer.
 * 
 * @author akram-moncer
 * 
 */
public class ClientInfo {

	/**
	 * adresse email.
	 */
	private String adresseEmail;

	/**
	 * company.
	 */
	private String company;

	/**
	 * civilite.
	 */
	private String civilite;

	/**
	 * nom.
	 */
	private String nom;

	/**
	 * prenom.
	 */
	private String prenom;

	/**
	 * constructeur par defaut.
	 */
	public ClientInfo() {
	}

	/**
	 * 
	 * @return {@link #adresseEmail}.
	 */
	public String getAdresseEmail() {
		return adresseEmail;
	}

	/**
	 * 
	 * @param adresseEmail
	 *            {@link #adresseEmail}.
	 */
	public void setAdresseEmail(String adresseEmail) {
		this.adresseEmail = adresseEmail;
	}

	/**
	 * 
	 * @return {@link #company}.
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * 
	 * @param company
	 *            {@link #company}.
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * 
	 * @return {@link #civilite}.
	 */
	public String getCivilite() {
		return civilite;
	}

	/**
	 * 
	 * @param civilite
	 *            {@link #civilite}.
	 */
	public void setCivilite(String civilite) {
		this.civilite = civilite;
	}

	/**
	 * 
	 * @return {@link #nom}.
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * 
	 * @param nom
	 *            {@link #nom}.
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * 
	 * @return {@link #prenom}.
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * 
	 * @param prenom
	 *            {@link #prenom}.
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

}
