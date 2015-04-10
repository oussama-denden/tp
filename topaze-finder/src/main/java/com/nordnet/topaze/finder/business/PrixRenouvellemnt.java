package com.nordnet.topaze.finder.business;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.nordnet.topaze.finder.enums.ModeFacturation;
import com.nordnet.topaze.finder.enums.ModePaiement;
import com.nordnet.topaze.finder.enums.TypeTVA;

/**
 * Cette classe regroupe les informations qui definissent un {@link PrixRenouvellemnt}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class PrixRenouvellemnt {

	/**
	 * La période de facturation.
	 */
	private Integer periodicite;

	/**
	 * The type tva. {@link TypeTVA}.
	 */
	private TypeTVA typeTVA;

	/**
	 * montant de prix.
	 */
	private Double montant;

	/**
	 * liste des frais.
	 */
	private Set<FraisRenouvellement> frais = new HashSet<>();

	/**
	 * The mode paiement. {@link ModePaiement}.
	 */
	private ModePaiement modePaiement;

	/**
	 * duree du sous contrat en mois.
	 */
	private Integer duree;

	/**
	 * reference de mode du paiement par exemple le RUM.
	 */
	private String referenceModePaiement;

	/**
	 * The mode facturation. {@link ModeFacturation}.
	 */
	private ModeFacturation modeFacturation;

	/**
	 * constructeur par defaut.
	 */
	public PrixRenouvellemnt() {

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
		if (!(obj instanceof PrixRenouvellemnt)) {
			return false;
		}
		PrixRenouvellemnt rhs = (PrixRenouvellemnt) obj;
		return new EqualsBuilder().append(montant, rhs.montant).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(43, 11).append(montant).toHashCode();
	}

	/* Getters & Setters */

	/**
	 * Gets the mode paiement.
	 * 
	 * @return {@link #modePaiement}.
	 */
	public ModePaiement getModePaiement() {
		return modePaiement;
	}

	/**
	 * Sets the mode paiement.
	 * 
	 * @param modePaiement
	 *            the new mode paiement {@link #modePaiement}.
	 */
	public void setModePaiement(ModePaiement modePaiement) {
		this.modePaiement = modePaiement;
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
	 * Gets the periodicite.
	 * 
	 * @return {@link #periodicite}.
	 */
	public Integer getPeriodicite() {
		return periodicite;
	}

	/**
	 * Sets the periodicite.
	 * 
	 * @param periodicite
	 *            the new periodicite {@link #periodicite}.
	 */
	public void setPeriodicite(Integer periodicite) {
		this.periodicite = periodicite;
	}

	/**
	 * Gets the type tva.
	 * 
	 * @return {@link #typeTVA}.
	 */
	public TypeTVA getTypeTVA() {
		return typeTVA;
	}

	/**
	 * Sets the type tva.
	 * 
	 * @param typeTVA
	 *            the new type tva {@link #typeTVA}.
	 */
	public void setTypeTVA(TypeTVA typeTVA) {
		this.typeTVA = typeTVA;
	}

	/**
	 * Gets the frais.
	 * 
	 * @return the frais
	 */
	public Set<FraisRenouvellement> getFrais() {
		return frais;
	}

	/**
	 * Sets the frais.
	 * 
	 * @param frais
	 *            the new frais
	 */
	public void setFrais(Set<FraisRenouvellement> frais) {
		this.frais = frais;
	}

	/**
	 * 
	 * @return {@link PrixRenouvellemnt#duree}.
	 */
	public Integer getDuree() {
		return duree;
	}

	/**
	 * 
	 * @param duree
	 *            {@link PrixRenouvellemnt#duree}.
	 */
	public void setDuree(Integer duree) {
		this.duree = duree;
	}

	/**
	 * @return {@link #referenceModePaiement}.
	 */
	public String getReferenceModePaiement() {
		return referenceModePaiement;
	}

	/**
	 * @param referenceModePaiement
	 *            {@link #referenceModePaiement}.
	 */
	public void setReferenceModePaiement(String referenceModePaiement) {
		this.referenceModePaiement = referenceModePaiement;
	}

	/**
	 * get the mode de facturation.
	 * 
	 * @return {@link #modeFacturation}
	 */
	public ModeFacturation getModeFacturation() {
		return modeFacturation;
	}

	/**
	 * set the mode de facturation.
	 * 
	 * @param modeFacturation
	 *            th new {@link #modeFacturation}
	 */
	public void setModeFacturation(ModeFacturation modeFacturation) {
		this.modeFacturation = modeFacturation;
	}

}