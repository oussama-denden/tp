package com.nordnet.topaze.client.rest.business.facturation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.nordnet.topaze.client.rest.business.util.Utils;
import com.nordnet.topaze.client.rest.enums.TypeFrais;

/**
 * Cette classe regroupe les informations qui definissent un {@link Frais}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class Frais implements Comparable<Frais> {

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
	 * Reduction sur les frais contrat.
	 */
	private ReductionInfo reductionInfoContrat;

	/**
	 * Reduction sur les frais element contractuel.
	 */
	private ReductionInfo reductionInfoEC;

	/**
	 * constructeur par defaut.
	 */
	public Frais() {

	}

	/* Getters & Setters */

	@Override
	public String toString() {
		return "Frais [montant=" + montant + ", typeFrais=" + typeFrais + ", ordre=" + ordre + ", nombreMois="
				+ nombreMois + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Frais)) {
			return false;
		}
		Frais rhs = (Frais) obj;
		return new EqualsBuilder().append(titre, rhs.titre).append(typeFrais, rhs.typeFrais)
				.append(montant, rhs.montant).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(43, 11).append(titre).append(typeFrais).append(montant).toHashCode();
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
	 * @return {@link #reductionInfoEC}.
	 */
	public ReductionInfo getReductionInfoContrat() {
		return reductionInfoContrat;
	}

	/**
	 * @param reductionInfoContrat
	 *            {@link #reductionInfoContrat}.
	 */
	public void setReductionInfoContrat(ReductionInfo reductionInfoContrat) {
		this.reductionInfoContrat = reductionInfoContrat;
	}

	/**
	 * @return {@link #reductionInfoEC}.
	 */
	public ReductionInfo getReductionInfoEC() {
		return reductionInfoEC;
	}

	/**
	 * @param reductionInfoEC
	 *            {@link #reductionInfoEC}.
	 */
	public void setReductionInfoEC(ReductionInfo reductionInfoEC) {
		this.reductionInfoEC = reductionInfoEC;
	}

	/**
	 * liste de toutes les {@link ReductionInfo} associe au frais.
	 * 
	 * @return liste des {@link ReductionInfo}.
	 */
	public List<ReductionInfo> getReductionsFrais() {
		List<ReductionInfo> reductionsFrais = new ArrayList<>();
		if (reductionInfoContrat != null) {
			reductionsFrais.add(reductionInfoContrat);
		}

		if (reductionInfoEC != null) {
			reductionsFrais.add(reductionInfoEC);
		}

		return reductionsFrais;
	}

	@Override
	public int compareTo(Frais frais) {
		return ordre - frais.getOrdre();
	}

}