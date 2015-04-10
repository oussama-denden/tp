package com.nordnet.topaze.businessprocess.facture.business;

import com.google.common.base.Optional;
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
	 * reference reduction.
	 */
	private String codeCatalogueReduction;

	/**
	 * Constructeur avec arguments.
	 * 
	 * @param titre
	 *            titre.
	 * @param discount
	 *            {@link Discount}.
	 * @param isAffichableSurFacture
	 *            boolean.
	 * @param codeCatalogueReduction
	 *            code catalogue de reduction.
	 */
	public DiscountInfo(String titre, Discount discount, boolean isAffichableSurFacture, String codeCatalogueReduction) {
		this.titre = titre;
		this.discount =
				new Discount(new Amount(discount.getAmount().getAmount().doubleValue()), discount.getDiscountType());
		this.isAffichableSurFacture = isAffichableSurFacture;
		this.codeCatalogueReduction = codeCatalogueReduction;
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
	public void setAffichableSurFacture(boolean isAffichableSurFacture) {
		this.isAffichableSurFacture = isAffichableSurFacture;
	}

	/**
	 * 
	 * @return {@link #codeCatalogueReduction}.
	 */
	public String getCodeCatalogueReduction() {
		return codeCatalogueReduction;
	}

	/**
	 * 
	 * @param codeCatalogueReduction
	 *            {@link #codeCatalogueReduction}.
	 */
	public void setCodeCatalogueReduction(String codeCatalogueReduction) {
		this.codeCatalogueReduction = codeCatalogueReduction;
	}

	/**
	 * indique si la reduction est automatique ou non.
	 * 
	 * @return true si la reduction est automatique.
	 */
	public boolean isReductionAutomatique() {
		if (Optional.fromNullable(codeCatalogueReduction).isPresent())
			return true;

		return false;
	}

}
