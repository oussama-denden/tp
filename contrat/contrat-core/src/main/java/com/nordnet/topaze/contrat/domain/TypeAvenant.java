package com.nordnet.topaze.contrat.domain;

/**
 * Enumaration qui définit les type d'un {@link Avenant}:
 * <ul>
 * <li><b>{@link #MIGRATION}: avenant de migration.</b></li>
 * <li><b>{@link #CESSION}: avenant de cession.</b></li>
 * </ul>
 * 
 * @author akram-moncer
 * 
 */
public enum TypeAvenant {

	/**
	 * avenant de migration.
	 */
	MIGRATION,

	/**
	 * avenant de cession.
	 */
	CESSION,

	/**
	 * 
	 */
	RENOUVELLEMENT;

}
