package com.nordnet.topaze.migration.outil.enums;

/**
 * Enumeration que definit le type de {@link ModePaiement}.
 * <p>
 * C'est la maniere dont le client va payer le produit qu'il a acquis. Soit de payer par :
 * </p>
 * <ul>
 * <li><b>Virement</b></li>
 * <li><b>Cheque</b></li>
 * <li><b>sous forme de prelevement par mois</b></li>
 * <li><b>3 fois sans frais</b></li>
 * <li><b>3 fois sans frais</b></li>
 * </ul>
 * 
 * @author Denden-OUSSAMA
 * 
 */
public enum ModePaiement {

	/**
	 * Mode de payement sous forme de pr�l�vement par mois.
	 */
	SEPA,

	/**
	 * Mode de payement par virement.
	 */
	CB,

	/**
	 * Mode de payement par cheque.
	 */
	CHEQUE,

	/**
	 * .
	 */
	FACTURE,

	/**
	 * Mode de payement en 3 fois sans frais.
	 */
	TROIS_FOIS_SANS_FRAIS,

	/**
	 * Avec le mode de paiement facture fin de mois, la facturation s'effectue uniquement a la fin du mois.
	 */
	FACTURE_FIN_DE_MOIS;

	/**
	 * Cette methode sera utiliser par le {@link ModePaiementDeserializer} pour faire la deserialisation.
	 * 
	 * @param modePaiement
	 *            on retourne null si la valeur de string n'est pas l'un de valeur deja defini.
	 * @return null si la valeur de string n'est pas l'un de valeur deja defini.
	 */
	public static ModePaiement fromString(final String modePaiement) {
		switch (modePaiement) {
		case "SEPA":
			return SEPA;
		case "CB":
			return CB;
		case "CHEQUE":
			return CHEQUE;
		case "FACTURE":
			return FACTURE;
		case "TROIS_FOIS_SANS_FRAIS":
			return TROIS_FOIS_SANS_FRAIS;
		case "FACTURE_FIN_DE_MOIS":
			return FACTURE_FIN_DE_MOIS;
		}
		return null;
	}
}