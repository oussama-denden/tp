package com.nordnet.topaze.finder.business;

import com.nordnet.topaze.finder.enums.TypeFrais;

/**
 * Cette classe regroupe les informations qui definissent un {@link f}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class FraisRenouvellement {

	/**
	 * montant de frais.
	 */
	private Double montant;

	/**
	 * Type de frais.
	 */
	private TypeFrais typeFrais;

	/**
	 * l'order d'application des frais de resiliation.
	 */
	private Integer ordre;

	/**
	 * le nombre de mois que le frais de resiliation reste valable ex: pour un {@link #nombreMois} egal 12, si une
	 * resiliation est effectue lors des 12 premier mois ce frais sera applique.
	 */
	private Integer nombreMois;

	/**
	 * constructeur par defaut.
	 */
	public FraisRenouvellement() {

	}

	/* Getters & Setters */
	@Override
	public String toString() {
		return "Frais [montant=" + montant + ", typeFrais=" + typeFrais + ", ordre=" + ordre + ", nombreMois="
				+ nombreMois + "]";
	}

	/**
	 * Gets the montant.
	 * 
	 * @return {@link #montant}.
	 */
	public Double getMontant() {
		return montant;
	}

	/**
	 * Sets the montant.
	 * 
	 * @param montant
	 *            the new montant {@link #montant}.
	 */
	public void setMontant(Double montant) {
		this.montant = montant;
	}

	/**
	 * Gets the type frais.
	 * 
	 * @return {@link #typeFrais}.
	 */
	public TypeFrais getTypeFrais() {
		return typeFrais;
	}

	/**
	 * Sets the type frais.
	 * 
	 * @param typeFrais
	 *            the new type frais {@link #typeFrais}.
	 */
	public void setTypeFrais(TypeFrais typeFrais) {
		this.typeFrais = typeFrais;
	}

	/**
	 * 
	 * @return {@link #ordre}
	 */
	public Integer getOrdre() {
		return ordre;
	}

	/**
	 * @param ordre
	 *            {@link #ordre}.
	 */
	public void setOrdre(Integer ordre) {
		this.ordre = ordre;
	}

	/**
	 * 
	 * @return {@link #nombreMois}
	 */
	public Integer getNombreMois() {
		return nombreMois;
	}

	/**
	 * 
	 * @param nombreMois
	 *            {@link #nombreMois}
	 */
	public void setNombreMois(Integer nombreMois) {
		this.nombreMois = nombreMois;
	}

}