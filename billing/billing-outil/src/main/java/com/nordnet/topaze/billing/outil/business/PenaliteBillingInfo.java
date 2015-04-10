package com.nordnet.topaze.billing.outil.business;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Les information des facturation de penalite.
 * 
 * @author Oussama Denden
 * 
 */
public class PenaliteBillingInfo {

	/**
	 * montant de penalite.
	 */
	private Double montantPenalite;

	/**
	 * montant penalite sans promotion.
	 */
	private Double montantPenaliteSansPromotion;

	/**
	 * Reduction sur la penalite {@link Discount}.
	 */
	private Discount reductionPenalite;

	/**
	 * periode de penalite {@link TimePeriod}.
	 */
	private TimePeriod periodePenalite;

	/**
	 * constructeur par default.
	 */
	public PenaliteBillingInfo() {

	}

	/**
	 * @return {@link #montantPenalite}.
	 */
	public Double getMontantPenalite() {
		return montantPenalite;
	}

	/**
	 * @param montantPenalite
	 *            {@link #montantPenalite}.
	 */
	public void setMontantPenalite(Double montantPenalite) {
		this.montantPenalite = montantPenalite;
	}

	/**
	 * @return {@link #montantPenaliteSansPromotion}.
	 */
	public Double getMontantPenaliteSansPromotion() {
		return montantPenaliteSansPromotion;
	}

	/**
	 * @param montantPenaliteSansPromotion
	 *            {@link #montantPenaliteSansPromotion}.
	 */
	public void setMontantPenaliteSansPromotion(Double montantPenaliteSansPromotion) {
		this.montantPenaliteSansPromotion = montantPenaliteSansPromotion;
	}

	/**
	 * @return {@link #reductionPenelaite}.
	 */
	public Discount getReductionPenalite() {
		return reductionPenalite;
	}

	/**
	 * @param reductionPenalite
	 *            {@link #reductionPenalite}.
	 */
	public void setReductionPenalite(Discount reductionPenalite) {
		this.reductionPenalite = reductionPenalite;
	}

	/**
	 * @return discount.
	 */
	@JsonIgnore
	public com.nordnet.common.valueObject.money.Discount getDiscount() {
		if (reductionPenalite != null) {
			return reductionPenalite.getDiscount();
		}
		return null;
	}

	/**
	 * @param discount
	 *            set dicount.
	 */
	@JsonIgnore
	public void setDiscount(com.nordnet.common.valueObject.money.Discount discount) {
		if (discount != null) {
			Discount discountInfo = new Discount();
			discountInfo.setDiscount(discount);
			this.reductionPenalite = discountInfo;
		}

	}

	/**
	 * @return {@link #periodePenalite}.
	 */
	public TimePeriod getPeriodePenalite() {
		return periodePenalite;
	}

	/**
	 * @param periodePenalite
	 *            {@link #periodePenalite}.
	 */
	public void setPeriodePenalite(TimePeriod periodePenalite) {
		this.periodePenalite = periodePenalite;
	}

	/**
	 * @return periode de penalite.
	 */
	@JsonIgnore
	public com.nordnet.common.valueObject.date.TimePeriod getTimePeriod() {
		if (periodePenalite != null) {
			return periodePenalite.getTimePeriod();
		}
		return null;
	}

	/**
	 * @param timePeriod
	 *            set info {@link #periodePenalite}.
	 */
	@JsonIgnore
	public void setTimePeriod(com.nordnet.common.valueObject.date.TimePeriod timePeriod) {
		if (timePeriod != null) {
			TimePeriod period = new TimePeriod();
			period.setTimePeriod(timePeriod);
			this.periodePenalite = period;
		}

	}

}
