package com.nordnet.topaze.billing.outil.business;

import com.nordnet.common.valueObject.constants.DiscountType;
import com.nordnet.common.valueObject.money.Discount;
import com.nordnet.common.valueObject.number.Amount;

/**
 * Cette classe contient les info a envoyer vers saphir apres le calcul de la reduction.
 * 
 * @author akram-moncer
 * 
 */
public class DiscountInfo {

	/**
	 * titre de la reduction a envoye vers saphir.
	 */
	private String titre;

	/**
	 * le discont à envoye vers saphir.
	 */
	private Discount discount;

	/**
	 * indique si la reduction sera affiche dans une ligne distinct dans la facture ou pas.
	 */
	private boolean isAffichableSurFacture;

	/**
	 * Constructeur avec arguments.
	 * 
	 * @param titre
	 *            titre.
	 * @param discount
	 *            {@link Discount}.
	 * @param isAfichableSurFacture
	 *            boolean.
	 */
	public DiscountInfo(String titre, Discount discount, boolean isAfichableSurFacture) {
		this.titre = titre;
		this.discount =
				new Discount(new Amount(discount.getAmount().getAmount().doubleValue()), discount.getDiscountType());
		this.isAffichableSurFacture = isAfichableSurFacture;
	}

	/**
	 * 
	 * @return {@link #titre}.
	 */
	public String getTitre() {
		return titre;
	}

	/**
	 * 
	 * @param titre
	 *            {@link #titre}.
	 */
	public void setTitre(String titre) {
		this.titre = titre;
	}

	/**
	 * 
	 * @return {@link #discount}.
	 */
	public Discount getDiscount() {
		return discount;
	}

	/**
	 * 
	 * @param discount
	 *            {@link #discount}.
	 */
	public void setDiscount(Discount discount) {
		this.discount = discount;
	}

	/**
	 * 
	 * @param valeur
	 *            valeur du discount?
	 * @param discountType
	 *            {@link DiscountType}.
	 */
	public void setDiscount(double valeur, DiscountType discountType) {
		Amount amount = new Amount(valeur);
		this.discount = new Discount(amount, discountType);
	}

	/**
	 * 
	 * @return {@link #isAffichableSurFacture}.
	 */
	public boolean isAffichableSurFacture() {
		return isAffichableSurFacture;
	}

	/**
	 * 
	 * @param isAffichableSurFacture
	 *            {@link #isAffichableSurFacture}.
	 */
	public void setAfichableSurFacture(boolean isAffichableSurFacture) {
		this.isAffichableSurFacture = isAffichableSurFacture;
	}

}
