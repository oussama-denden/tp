package com.nordnet.topaze.contrat.domain;

/**
 * Enumération qui définit le context dans lequel la reduction a été affecté.
 * 
 * <p>
 * Deux types de valeur:
 * </p>
 * <ul>
 * <li><b>GLOBAL</b> : la reduction est affecté à un contrat global.</li>
 * <li><b>PARTIEL</b> : la reduction est affecté un contrat partiel (element contractuel).</li>
 * </ul>
 * 
 * @author akram-moncer
 * 
 */
public enum ContextReduction {

	/**
	 * la reduction est affecté à un contrat global.
	 */
	GLOBAL,

	/**
	 * la reduction est affecté un contrat partiel (element contractuel).
	 */
	PARTIEL;

}
