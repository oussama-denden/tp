package com.nordnet.topaze.businessprocess.swap.mock;

public enum TRequestStatus {
	/**
	 * status de EM indique la bonne livraison
	 */
	INRETURNPROGRESS,

	/**
	 * status de EM indique la fin de retour
	 */
	CLOSED;

	public static TRequestStatus getEtatDeEM(String etat) {
		switch (etat) {
		case "INRETURNPROGRESS":
			return INRETURNPROGRESS;

		case "CLOSED":
			return CLOSED;

		}

		return null;

	}

}
