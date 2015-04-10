package com.nordnet.topaze.contrat.business;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe regroupe les informations qui definissent un {@link ContratMigrationInfo}. Ces informations seront
 * utilisée pour la migration d'un contrat.
 * 
 * @author anisselmane.
 * 
 */
public class ContratMigrationInfo {

	/**
	 * La politique de migration.
	 */
	private PolitiqueMigration politiqueMigration;

	/**
	 * Liste produits pour la migration.
	 */
	private List<ProduitMigration> produitsMigration = new ArrayList<>();

	/**
	 * Liste de reductions.
	 */
	private List<ReductionMigration> reductions = new ArrayList<>();

	/**
	 * l'usager.
	 */
	private String user;

	/**
	 * constructeur par defaut.
	 */
	public ContratMigrationInfo() {

	}

	/* Getters & Setters */

	/**
	 * Gets the politique migration.
	 * 
	 * @return the politique migration
	 */
	public PolitiqueMigration getPolitiqueMigration() {
		return politiqueMigration;
	}

	/**
	 * Sets the politique migration.
	 * 
	 * @param politiqueMigration
	 *            the new politique migration
	 */
	public void setPolitiqueMigration(PolitiqueMigration politiqueMigration) {
		this.politiqueMigration = politiqueMigration;
	}

	/**
	 * Gets the produits migration.
	 * 
	 * @return the produits migration
	 */
	public List<ProduitMigration> getProduitsMigration() {
		return produitsMigration;
	}

	/**
	 * Sets the produits migration.
	 * 
	 * @param produitsMigration
	 *            the new produits migration
	 */
	public void setProduitsMigration(List<ProduitMigration> produitsMigration) {
		this.produitsMigration = produitsMigration;
	}

	/**
	 * Gets the user.
	 * 
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
	 * 
	 * @return List de {@link #reductions}.
	 */
	public List<ReductionMigration> getReductions() {
		return reductions;
	}

	/**
	 * 
	 * @param reductions
	 *            List de {@link #reductions}.
	 */
	public void setReductions(List<ReductionMigration> reductions) {
		this.reductions = reductions;
	}

}
