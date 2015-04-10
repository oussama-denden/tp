package com.nordnet.topaze.resiliation.outil.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nordnet.topaze.resiliation.outil.util.Constants;
import com.nordnet.topaze.resiliation.outil.util.Utils;

/**
 * Les information des facturation de remboursement.
 * 
 * @author Oussama Denden
 * 
 */
public class RemboursementBillingInfo {

	/**
	 * montant de remboursement.
	 */
	private Double montantRemboursement;

	/**
	 * Reduction sur la remboursement {@link Discount}.
	 */
	private Discount reductionRemboursement;

	/**
	 * periode de remboursement {@link TimePeriod}.
	 */
	private TimePeriod periodeRemboursement;

	/**
	 * constructeur par default.
	 */
	public RemboursementBillingInfo() {

	}

	/**
	 * @return {@link #montantRemboursement}.
	 */
	public Double getMontantRemboursement() {
		return Utils.round(montantRemboursement, Constants.DEUX);
	}

	/**
	 * @param montantRemboursement
	 *            {@link #montantRemboursement}.
	 */
	public void setMontantRemboursement(Double montantRemboursement) {
		this.montantRemboursement = montantRemboursement;
	}

	/**
	 * @return {@link #reductionRemboursement}.
	 */
	public Discount getReductionRemboursement() {
		return reductionRemboursement;
	}

	/**
	 * @param reductionRemboursement
	 *            {@link #reductionRemboursement}.
	 */
	public void setReductionRemboursement(Discount reductionRemboursement) {
		this.reductionRemboursement = reductionRemboursement;
	}

	/**
	 * @return discount.
	 */
	@JsonIgnore
	public com.nordnet.common.valueObject.money.Discount getDiscount() {
		if (reductionRemboursement != null) {
			return reductionRemboursement.getDiscount();
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
			this.reductionRemboursement = discountInfo;
		}

	}

	/**
	 * @return {@link #periodeRemboursement}.
	 */
	public TimePeriod getPeriodeRemboursement() {
		return periodeRemboursement;
	}

	/**
	 * @param periodeRemboursement
	 *            {@link #periodeRemboursement}.
	 */
	public void setPeriodeRemboursement(TimePeriod periodeRemboursement) {
		this.periodeRemboursement = periodeRemboursement;
	}

	/**
	 * @return periode de remboursement.
	 */
	public com.nordnet.common.valueObject.date.TimePeriod getTimePeriod() {
		if (periodeRemboursement != null) {
			return periodeRemboursement.getTimePeriod();
		}
		return null;
	}

	/**
	 * @param timePeriod
	 *            set info {@link #periodeRemboursement}.
	 */
	public void setTimePeriod(com.nordnet.common.valueObject.date.TimePeriod timePeriod) {
		if (timePeriod != null) {
			TimePeriod period = new TimePeriod();
			period.setTimePeriod(timePeriod);
			this.periodeRemboursement = period;
		}

	}

}
