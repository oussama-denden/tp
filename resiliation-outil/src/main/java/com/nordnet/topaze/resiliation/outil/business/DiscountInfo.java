package com.nordnet.topaze.resiliation.outil.business;

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
	private boolean isAfichableSurFacture;

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
		this.isAfichableSurFacture = isAfichableSurFacture;
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
	 * @return {@link #isAfichableSurFacture}.
	 */
	public boolean isAfichableSurFacture() {
		return isAfichableSurFacture;
	}

	/**
	 * 
	 * @param isAfichableSurFacture
	 *            {@link #isAfichableSurFacture}.
	 */
	public void setAfichableSurFacture(boolean isAfichableSurFacture) {
		this.isAfichableSurFacture = isAfichableSurFacture;
	}

}
