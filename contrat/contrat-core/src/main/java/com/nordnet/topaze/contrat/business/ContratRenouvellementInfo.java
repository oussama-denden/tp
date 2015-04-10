package com.nordnet.topaze.contrat.business;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe regroupe les informations qui definissent un {@link ContratRenouvellementInfo}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class ContratRenouvellementInfo {

	/**
	 * utilisateur.
	 */
	private String user;

	/**
	 * politique de renouvellement.
	 */
	private PolitiqueRenouvellement politiqueRenouvellement;

	/**
	 * liste des produits pour le renouvellement.
	 */
	private List<ProduitRenouvellement> produitRenouvellements = new ArrayList<>();

	/**
	 * constructeur par defaut.
	 */

	public ContratRenouvellementInfo() {

	}

	/* Getters and Setters */

	/**
	 * 
	 * @return {@link #user}
	 */
	public String getUser() {
		return user;
	}

	/**
	 * 
	 * @param user
	 *            the new user {@link #user}
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * 
	 * @return {@link #politiqueRenouvellement}
	 */
	public PolitiqueRenouvellement getPolitiqueRenouvellement() {
		return politiqueRenouvellement;
	}

	/**
	 * 
	 * @param politique
	 *            {@link #politiqueRenouvellement}
	 */
	public void setPolitiqueRenouvellement(PolitiqueRenouvellement politique) {
		this.politiqueRenouvellement = politique;
	}

	/**
	 * 
	 * @return {@link #produitRenouvellements}
	 */
	public List<ProduitRenouvellement> getProduitRenouvellements() {
		return produitRenouvellements;
	}

	/**
	 * 
	 * @param produitRenouvellements
	 *            {@link #produitRenouvellements}
	 */
	public void setProduitRenouvellements(List<ProduitRenouvellement> produitRenouvellements) {
		this.produitRenouvellements = produitRenouvellements;
	}

}
