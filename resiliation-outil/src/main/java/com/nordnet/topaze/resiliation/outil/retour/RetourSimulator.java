package com.nordnet.topaze.resiliation.outil.retour;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.nordnet.topaze.resiliation.outil.business.ContratResiliationInfo;
import com.nordnet.topaze.resiliation.outil.business.ResiliationBillingInfo;
import com.nordnet.topaze.resiliation.outil.business.RetourInfo;
import com.nordnet.topaze.resiliation.outil.enums.TypeOperation;
import com.nordnet.topaze.resiliation.outil.enums.TypeProduit;

/**
 * Simuler le retour des biens et la suspension des services.
 * 
 * @author Oussama Denden
 * 
 */
@Component("retourSimulator")
public class RetourSimulator {

	/**
	 * Genere les bien a retourner et les services a suspendre.
	 * 
	 * @param resiliationInfo
	 *            {@link ContratResiliationInfo}.
	 * @param resiliationBillingInfos
	 *            les information sur les elements resilie provenant de la brique contrat.
	 * @return {@link ContratResiliationInfo}.
	 */
	public ContratResiliationInfo simulerRetour(ContratResiliationInfo resiliationInfo,
			ResiliationBillingInfo[] resiliationBillingInfos) {
		ArrayList<RetourInfo> retourInfos = new ArrayList<>();
		for (ResiliationBillingInfo resiliationBillingInfo : resiliationBillingInfos) {
			RetourInfo retourInfo = new RetourInfo();
			retourInfo.setReferenceProduit(resiliationBillingInfo.getReferenceProduit());
			retourInfo.setTypeElement(resiliationBillingInfo.getTypeElement());
			if (resiliationBillingInfo.getTypeElement().equals(TypeProduit.BIEN)) {
				retourInfo.setTypeOperation(TypeOperation.RETOUR);
			} else if (resiliationBillingInfo.getTypeElement().equals(TypeProduit.SERVICE)) {
				retourInfo.setTypeOperation(TypeOperation.SUSPENSION);
			}
			retourInfos.add(retourInfo);
		}

		resiliationInfo.setRetourInfos(retourInfos);

		return resiliationInfo;
	}
}
