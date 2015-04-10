package com.nordnet.topaze.contrat.test.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nordnet.topaze.contrat.business.ClientInfo;
import com.nordnet.topaze.contrat.business.ContratCession;
import com.nordnet.topaze.contrat.business.PolitiqueCession;
import com.nordnet.topaze.contrat.business.ProduitCession;
import com.nordnet.topaze.contrat.domain.ModePaiement;
import com.nordnet.topaze.contrat.util.Constants;

/**
 * creation d'une instance {@link ContratCessionInfo}.
 * 
 * @author akram-moncer
 * 
 */
public class ContratCessionUtils {

	/**
	 * creation d'un {@link ContratCessionInfo}.
	 * 
	 * @param idClientSource
	 *            id de l'ancient client.
	 * @param idClientDestination
	 *            id du nouveau client.
	 * @param dateAction
	 *            date action.
	 * @return {@link ContratCession}.
	 */
	public static ContratCession creerContratCessionInfo(String idClientSource, String idClientDestination,
			Date dateAction) {
		ContratCession contratCessionInfo = new ContratCession();
		ClientInfo clientSource = new ClientInfo();
		clientSource.setIdClient(idClientSource);
		clientSource.setIdAdrFacturation("adrFact");

		ClientInfo clientDestination = new ClientInfo();
		clientDestination.setIdClient(idClientDestination);
		clientDestination.setIdAdrFacturation("adrFact1");

		contratCessionInfo.setClientSource(clientSource);
		contratCessionInfo.setClientDestination(clientDestination);

		ProduitCession produitCession = new ProduitCession();
		produitCession.setNumEC(1);
		produitCession.setReferenceModePaiement("RUM");
		produitCession.setModePaiement(ModePaiement.CB);
		produitCession.setEngagement(10);
		produitCession.setDuree(13);
		List<ProduitCession> produitsCession = new ArrayList<>();
		produitsCession.add(produitCession);
		contratCessionInfo.setProduitsCession(produitsCession);

		PolitiqueCession politiqueCession = new PolitiqueCession();
		politiqueCession.setMontantRemboursement(10d);
		politiqueCession.setFraisCession(10d);
		politiqueCession.setRemboursement(true);
		politiqueCession.setDateAction(dateAction);
		politiqueCession.setConserverAncienneReduction(false);
		contratCessionInfo.setPolitiqueCession(politiqueCession);

		contratCessionInfo.setIdAdrLivraison("adrLiv");
		contratCessionInfo.setUser(Constants.INTERNAL_USER);

		return contratCessionInfo;

	}

}
