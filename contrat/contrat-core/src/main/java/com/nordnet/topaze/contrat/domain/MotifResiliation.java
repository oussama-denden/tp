package com.nordnet.topaze.contrat.domain;

/**
 * Enumaration qui définit les motifes d'un {@link PolitiqueResiliation}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public enum MotifResiliation {

	/**
	 * remoursement.
	 */
	REMBOURSEMENT,

	/**
	 * Résiliation sans Remboursement.
	 */
	RESILIATION_SANS_REMBOURSEMENT,

	/**
	 * Impayé.
	 */
	IMPAYE,

	/**
	 * Saisie Judiciaire.
	 */
	SAISIE_JUDICIAIRE,

	/**
	 * Client Insolvable.
	 */
	CLIENT_INSOLVABLE,

	/**
	 * Demande de Non Renouvellement.
	 */
	DEMANDE_DE_NON_RENOUVELLEMENT,

	/**
	 * annuation de la commande.
	 */
	ANNULATION_COMMANDE,

	/**
	 * Autre.
	 */
	AUTRE;

	/**
	 * Motif resilition a partir d un string.
	 * 
	 * @param motifResiliation
	 *            MotifResiliation
	 * @return {@link MotifResiliation}
	 */
	public static MotifResiliation fromString(final String motifResiliation) {
		switch (motifResiliation) {
		case "REMBOURSEMENT":
			return REMBOURSEMENT;
		case "RESILIATION_SANS_REMBOURSEMENT":
			return RESILIATION_SANS_REMBOURSEMENT;
		case "CLIENT_INSOLVABLE":
			return CLIENT_INSOLVABLE;
		case "IMPAYE":
			return IMPAYE;
		case "SAISIE_JUDICIAIRE":
			return SAISIE_JUDICIAIRE;
		case "DEMANDE_DE_NON_RENOUVELLEMENT":
			return DEMANDE_DE_NON_RENOUVELLEMENT;
		case "ANNULATION_COMMANDE":
			return ANNULATION_COMMANDE;
		case "AUTRE":
			return AUTRE;
		}
		return null;
	}

}
