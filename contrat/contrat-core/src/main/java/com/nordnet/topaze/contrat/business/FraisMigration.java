package com.nordnet.topaze.contrat.business;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nordnet.topaze.contrat.domain.TypeFrais;
import com.nordnet.topaze.contrat.util.TypeFraisDeserializer;
import com.nordnet.topaze.contrat.util.Utils;

/**
 * Cette classe regroupe les informations qui definissent un {@link FraisMigration}.
 * 
 * @author anisselmane.
 * 
 */
public class FraisMigration {

	/**
	 * titre de frais par exmemple: Frais de dossier.
	 */
	private String titre;

	/**
	 * montant de frais.
	 */
	private Double montant;

	/**
	 * Type de frais.
	 */
	@JsonDeserialize(using = TypeFraisDeserializer.class)
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
	public FraisMigration() {

	}

	/* Getters & Setters */

	@Override
	public String toString() {
		return "Frais [montant=" + montant + ", typeFrais=" + typeFrais + ", ordre=" + ordre + ", nombreMois="
				+ nombreMois + "]";
	}

	/**
	 * @return {@link #titre}.
	 */
	public String getTitre() {
		if (Utils.isStringNullOrEmpty(titre)) {
			switch (typeFrais) {
			case CREATION:
				return "Frais Creation";
			case RESILIATION:
				return "Frais Resiliation";
			case MIGRATION:
				return "Frais Migration";

			}
		}
		return titre;
	}

	/**
	 * @param titre
	 *            {@link #titre}.
	 */
	public void setTitre(String titre) {
		this.titre = titre;
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

	/**
	 * Mapping frais migration à frais.
	 * 
	 * @param fraisMigration
	 *            the frais migration
	 * @param reductionAncienne
	 * @return the frais
	 */
	public Frais toFrais() {
		Frais frais = new Frais();
		frais.setMontant(montant);
		frais.setNombreMois(nombreMois);
		frais.setOrdre(ordre);

		frais.setReductionInfoContrat(null);
		frais.setReductionInfoEC(null);

		frais.setTitre(titre);
		frais.setTypeFrais(typeFrais);
		return frais;

	}

}
