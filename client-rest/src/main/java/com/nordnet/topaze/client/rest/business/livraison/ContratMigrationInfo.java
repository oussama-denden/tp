package com.nordnet.topaze.client.rest.business.livraison;

/**
 * Les information necaissaire à l'initiation de processus de migration.
 * 
 * @author Oussama Denden
 * 
 */
public class ContratMigrationInfo {

	/**
	 * {@link Avenant}.
	 */
	private Avenant avenant;

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
	public Avenant getAvenant() {
		return avenant;
	}

	/**
	 * @param avenant
	 *            {@link #avenant}.
	 */
	public void setAvenant(Avenant avenant) {
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
