package com.nordnet.topaze.livraison.core.business;

import java.util.List;

import com.nordnet.topaze.livraison.core.domain.TypeProduit;

/**
 * cette classe lie un element contractuelle avec son etat dans packager et dans netretour.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class ElementsRenouvellemtnInfo {

	/**
	 * type de produit.
	 */
	private TypeProduit typeProduit;

	/**
	 * liste de element state info.
	 */
	private List<ElementStateInfo> elementStateInfos;

	/**
	 * constructueur par defaut.
	 */

	public ElementsRenouvellemtnInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* Getters ans Setters */

	/**
	 * get the type produit.
	 * 
	 * @return {@link #typeProduit}
	 */
	public TypeProduit getTypeProduit() {
		return typeProduit;
	}

	/**
	 * sets the type produit.
	 * 
	 * @param typeProduit
	 *            the new {@link #typeProduit}
	 */
	public void setTypeProduit(TypeProduit typeProduit) {
		this.typeProduit = typeProduit;
	}

	/**
	 * gets the element state info.
	 * 
	 * @return {@link #elementStateInfos}
	 */
	public List<ElementStateInfo> getElementStateInfos() {
		return elementStateInfos;
	}

	/**
	 * set the element state info.
	 * 
	 * @param elementStateInfos
	 *            set the new {@link #elementStateInfos}
	 */
	public void setElementStateInfos(List<ElementStateInfo> elementStateInfos) {
		this.elementStateInfos = elementStateInfos;
	}

}
