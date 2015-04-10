package com.nordnet.topaze.livraison.test.utils;

import java.util.List;

import com.nordnet.topaze.livraison.core.business.ElementStateInfo;
import com.nordnet.topaze.livraison.core.business.ElementsRenouvellemtnInfo;
import com.nordnet.topaze.livraison.core.domain.ElementLivraison;
import com.nordnet.topaze.livraison.core.domain.TypeProduit;

/**
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class ElementsRenouvellemtnInfoGanarator {

	/**
	 * creer {@link ElementsRenouvellemtnInfo}.
	 * 
	 * @param typeProduit
	 *            {@link TypeProduit}.
	 * @param elementStateInfo
	 *            {@link elementStateInfo}.
	 * @return {@link ElementsRenouvellemtnInfo}.
	 */
	public static ElementsRenouvellemtnInfo getElementsRenouvellemtnInfo(TypeProduit typeProduit,
			List<ElementStateInfo> elementStateInfo) {

		ElementsRenouvellemtnInfo elementsRenouvellemtnInfo = new ElementsRenouvellemtnInfo();
		elementsRenouvellemtnInfo.setTypeProduit(typeProduit);
		elementsRenouvellemtnInfo.setElementStateInfos(elementStateInfo);

		return elementsRenouvellemtnInfo;

	}

	/**
	 * creer {@link ElementStateInfo}.
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison}.
	 * @return {@link ElementStateInfo}.
	 */
	public static ElementStateInfo getElementStateInfo(ElementLivraison elementLivraison) {
		ElementStateInfo elementStateInfo = new ElementStateInfo();
		elementStateInfo.setRefenceElementContractuelle(elementLivraison.getReference());
		elementStateInfo.setRetourne(false);
		elementStateInfo.setPreparerPourRetour(false);
		elementStateInfo.setState(null);
		elementStateInfo.setCodeProduit(null);
		elementStateInfo.setCodeColis(null);
		return elementStateInfo;

	}

}
