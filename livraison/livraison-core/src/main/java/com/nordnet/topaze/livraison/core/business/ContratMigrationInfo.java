package com.nordnet.topaze.livraison.core.business;

import com.nordnet.topaze.livraison.core.domain.BonPreparation;

/**
 * Les information necaissaire à l'initiation de processus de migration.
 * 
 * @author Oussama Denden
 * 
 */
public class ContratMigrationInfo {

	/**
	 * {@link ContratAvenant}.
	 */
	private ContratAvenant avenant;

	/**
	 * {@link ContratBP}.
	 */
	private BonPreparation bonPreparation;

	/**
	 * constructeur par défaut.
	 */
	public ContratMigrationInfo() {

	}

	/**
	 * @return {@link #avenant}.
	 */
	public ContratAvenant getAvenant() {
		return avenant;
	}

	/**
	 * @param avenant
	 *            {@link #avenant}.
	 */
	public void setAvenant(ContratAvenant avenant) {
		this.avenant = avenant;
	}

	/**
	 * @return {@link #bonPreparation}.
	 */
	public BonPreparation getBonPreparation() {
		return bonPreparation;
	}

	/**
	 * @param bonPreparation
	 *            {@link #bonPreparation}.
	 */
	public void setBonPreparation(BonPreparation bonPreparation) {
		this.bonPreparation = bonPreparation;
	}

}
