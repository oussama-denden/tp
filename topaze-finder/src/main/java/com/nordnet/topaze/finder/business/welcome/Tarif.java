package com.nordnet.topaze.finder.business.welcome;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nordnet.common.valueObject.constants.VatType;
import com.nordnet.topaze.finder.enums.TypeTVA;

/**
 * Vue detaillee du tarif.
 * 
 * @author Oussama Denden
 * 
 */
@JsonIgnoreProperties({ "vat" })
public class Tarif {

	/**
	 * reference de produit.
	 */
	private String referenceProduit;

	/**
	 * num elementcontractuel.
	 */
	private Integer numEC;

	/**
	 * periodicite.
	 */
	private Integer periodicite;

	/**
	 * {@link TypeTVA}.
	 */
	private TypeTVA TVA;

	/**
	 * tarif de tarif TTC.
	 */
	private double montantTTC;

	/**
	 * montant de tarif HT.
	 */
	private double montantHT;

	/**
	 * montant de reduction sur tarif global.
	 */
	private Double montantReductionHT;

	/**
	 * montant de reduction sur tarif global.
	 */
	private Double montantReductionTTC;

	/**
	 * Constructeur par defaut.
	 */
	public Tarif() {

	}

	/**
	 * @return {@link #referenceProduit}.
	 */
	public String getReferenceProduit() {
		return referenceProduit;
	}

	/**
	 * @param referenceProduit
	 *            {@link #referenceProduit}.
	 */
	public void setReferenceProduit(String referenceProduit) {
		this.referenceProduit = referenceProduit;
	}

	/**
	 * @return {@link #numEC}.
	 */
	public Integer getNumEC() {
		return numEC;
	}

	/**
	 * @param numEC
	 *            {@link #numEC}.
	 */
	public void setNumEC(Integer numEC) {
		this.numEC = numEC;
	}

	/**
	 * @return {@link #periodicite}.
	 */
	public Integer getPeriodicite() {
		return periodicite;
	}

	/**
	 * @param periodicite
	 *            {@link #periodicite}.
	 */
	public void setPeriodicite(Integer periodicite) {
		this.periodicite = periodicite;
	}

	/**
	 * @return {@link #TVA}.
	 */
	public TypeTVA getTVA() {
		return TVA;
	}

	/**
	 * @param TVA
	 *            {@link #TVA}.
	 */
	public void setTVA(TypeTVA TVA) {
		this.TVA = TVA;
	}

	/**
	 * @return {@link #montantTTC}.
	 */
	public double getMontantTTC() {
		return montantTTC;
	}

	/**
	 * @param montantTTC
	 *            {@link #montantTTC}.
	 */
	public void setMontantTTC(double montantTTC) {
		this.montantTTC = montantTTC;
	}

	/**
	 * @return {@link #montantHT}.
	 */
	public double getMontantHT() {
		return montantHT;
	}

	/**
	 * @param montantHT
	 *            {@link #montantHT}.
	 */
	public void setMontantHT(double montantHT) {
		this.montantHT = montantHT;
	}

	/**
	 * @return {@link #montantReductionHT}.
	 */
	public Double getMontantReductionHT() {
		return montantReductionHT;
	}

	/**
	 * @param montantReductionHT
	 *            {@link #montantReductionHT}.
	 */
	public void setMontantReductionHT(Double montantReductionHT) {
		this.montantReductionHT = montantReductionHT;
	}

	/**
	 * @return {@link #montantReductionTTC}.
	 */
	public Double getMontantReductionTTC() {
		return montantReductionTTC;
	}

	/**
	 * @param montantReductionTTC
	 *            {@link #montantReductionTTC}.
	 */
	public void setMontantReductionTTC(Double montantReductionTTC) {
		this.montantReductionTTC = montantReductionTTC;
	}

	/**
	 * @return Transormer le {@link TypeTVA} en {@link VatType}.
	 */
	public VatType getVat() {
		switch (TVA) {
		case P:
			return VatType.P;
		case R:
			return VatType.R;
		case S:
			return VatType.S;
		case SR:
			return VatType.SR;
		default:
			return VatType.NA;
		}
	}

}