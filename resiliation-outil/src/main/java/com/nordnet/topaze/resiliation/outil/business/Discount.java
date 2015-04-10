package com.nordnet.topaze.resiliation.outil.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nordnet.common.valueObject.constants.DiscountType;
import com.nordnet.common.valueObject.number.Amount;

/**
 * Information de reduction.
 * 
 * @author Oussama Denden
 * 
 */
public class Discount {

	/**
	 * montant de reduction.
	 */
	private Double montantReduction;

	/**
	 * type de reduction.
	 */
	private DiscountType typeReduction;

	/**
	 * constructeur par defaut.
	 */
	public Discount() {

	}

	/**
	 * @return {@link #montantReduction}.
	 */
	public Double getMontantReduction() {
		return montantReduction;
	}

	/**
	 * @param montantReduction
	 *            {@link #montantReduction}.
	 */
	public void setMontantReduction(Double montantReduction) {
		this.montantReduction = montantReduction;
	}

	/**
	 * @return {@link #typeReduction}.
	 */
	public DiscountType getTypeReduction() {
		return typeReduction;
	}

	/**
	 * @param typeReduction
	 *            {@link #typeReduction}.
	 */
	public void setTypeReduction(DiscountType typeReduction) {
		this.typeReduction = typeReduction;
	}

	/**
	 * @return reduction.
	 */
	@JsonIgnore
	public com.nordnet.common.valueObject.money.Discount getDiscount() {
		Amount amount = new Amount(this.montantReduction);

		com.nordnet.common.valueObject.money.Discount discount =
				new com.nordnet.common.valueObject.money.Discount(amount, this.typeReduction);

		return discount;
	}

	/**
	 * @param discount
	 *            set Discount Info.
	 */
	@JsonIgnore
	public void setDiscount(com.nordnet.common.valueObject.money.Discount discount) {
		this.montantReduction = discount.getAmount().getAmount().doubleValue();
		this.typeReduction = discount.getDiscountType();
	}

}
