package com.nordnet.topaze.contrat.domain;

import com.nordnet.topaze.contrat.util.ActionsDeserializer;

/**
 * Enumeration que définit les actions de modification d'un contrat .
 * <p>
 * Actions :
 * </p>
 * <ul>
 * <li><b>MODIFADRFACTURATION</b></li>
 * <li><b>MODIFADRFACTURATION </b></li>
 * <li><b>MODIFADRLIVRAISON </b></li>
 * <li><b>MODIFTITRECONTRAT</b></li>
 * <li><b>MODIFMODEPAIEMENT</b></li>
 * <li><b>MODIFREFMODEPAIEMENT</b></li>
 * <li><b>MODIFENGAGEMENT</b></li>
 * <li><b>MODIFDUREE</b></li>
 * <li><b>MIGRATIONCONTRAT</b></li>
 * <li><b>MODIFDATEFACTURATION</b></li>
 * <li><b>MODIFDATEFINCONTRAT</b></li>
 * 
 * </ul>
 * 
 * @author anisselmane.
 * 
 */
public enum Actions {
	/**
	 * Indique que le {@link Actions} de modification est {@link #MODIFADRFACTURATION}.
	 */
	MODIFADRFACTURATION,
	/**
	 * Indique que le {@link Actions} de modification est {@link #MODIFADRLIVRAISON}.
	 */
	MODIFADRLIVRAISON,
	/**
	 * Indique que le {@link Actions} de modification est {@link #MODIFTITRECONTRAT}.
	 */
	MODIFTITRECONTRAT,
	/**
	 * Indique que le {@link Actions} de modification est {@link #MODIFMODEPAIEMENT}.
	 */
	MODIFMODEPAIEMENT,
	/**
	 * Indique que le {@link Actions} de modification est {@link #MODIFREFMODEPAIEMENT}.
	 */
	MODIFREFMODEPAIEMENT,
	/**
	 * Indique que le {@link Actions} de modification est {@link #MODIFENGAGEMENT}.
	 */
	MODIFENGAGEMENT,
	/**
	 * Indique que le {@link Actions} de modification est {@link #MODIFDUREE}.
	 */
	MODIFDUREE,
	/**
	 * Indique que le {@link Actions} de modification est {@link #MIGRATIONCONTRAT}.
	 */
	MIGRATIONCONTRAT,
	/**
	 * Indique que le {@link Actions} de modification est {@link #MODIFIERDATEFACTURATION}.
	 */
	MODIFDATEFACTURATION,
	/**
	 * Indique que le {@link Actions} de modification est {@link #MODIFDATEFINCONTRAT}.
	 */
	MODIFDATEFINCONTRAT;

	/**
	 * Cette methode sera utiliser par le {@link ActionsDeserializer} pour faire la désérialisation.
	 * 
	 * @param action
	 *            l action
	 * @return null si la valeur de string n'est pas MODIFADRFACTURATION,MIGRATIONCONTRAT ou MODIFIERDATEFACTURATION.
	 */
	public static Actions fromString(String action) {
		switch (action) {
		case "MODIFADRFACTURATION":
			return MODIFADRFACTURATION;

		case "MODIFADRLIVRAISON":
			return MODIFADRLIVRAISON;

		case "MODIFTITRECONTRAT":
			return MODIFTITRECONTRAT;

		case "MODIFMODEPAIEMENT":
			return MODIFMODEPAIEMENT;

		case "MODIFREFMODEPAIEMENT":
			return MODIFREFMODEPAIEMENT;

		case "MODIFENGAGEMENT":
			return MODIFENGAGEMENT;

		case "MODIFDUREE":
			return MODIFDUREE;

		case "MIGRATIONCONTRAT":
			return MIGRATIONCONTRAT;

		case "MODIFDATEFACTURATION":
			return MODIFDATEFACTURATION;

		case "MODIFDATEFINCONTRAT":
			return MODIFDATEFINCONTRAT;

		}
		return null;
	}

}
