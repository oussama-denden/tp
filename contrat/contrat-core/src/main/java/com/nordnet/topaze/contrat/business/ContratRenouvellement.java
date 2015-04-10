package com.nordnet.topaze.contrat.business;

import java.util.ArrayList;
import java.util.List;

import com.nordnet.topaze.contrat.domain.PolitiqueRenouvellement;

/**
 * Cette classe regroupe les informations qui definissent un {@link ContratRenouvellement}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class ContratRenouvellement {

	/**
	 * utilisateur
	 */
	private String user;

	/**
	 * politique de renouvellement
	 */
	private PolitiqueRenouvellement politique;

	/**
	 * liste des produits pour le renouvellement
	 */
	private List<ProduitRenouvellement> produitRenouvellements = new ArrayList<>();

	/**
	 * constructeur par defaut
	 */

	public ContratRenouvellement() {

	}

	/* Getters and Setters */

	/**
	 * gets the user
	 * 
	 * @return {@link #user}
	 */
	public String getUser() {
		return user;
	}

	/**
	 * the new user
	 * 
	 * @param user
	 *            the new user {@link #user}
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * get the politique
	 * 
	 * @return {@link #politique}
	 */
	public PolitiqueRenouvellement getPolitique() {
		return politique;
	}

	/**
	 * set the new politique renouvellemnt
	 * 
	 * @param politique
	 *            the new politique {@link #politique}
	 */
	public void setPolitique(PolitiqueRenouvellement politique) {
		this.politique = politique;
	}

	/**
	 * gets the produit renouvellement
	 * 
	 * @return {@link #produitRenouvellements}
	 */
	public List<ProduitRenouvellement> getProduitRenouvellements() {
		return produitRenouvellements;
	}

	/**
	 * the new produits renouvellement
	 * 
	 * @param produitRenouvellements
	 *            {@link #produitRenouvellements}
	 */
	public void setProduitRenouvellements(List<ProduitRenouvellement> produitRenouvellements) {
		this.produitRenouvellements = produitRenouvellements;
	}

}
