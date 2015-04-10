package com.nordnet.topaze.resiliation.outil.facturation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.resiliation.outil.business.ContratResiliationInfo;
import com.nordnet.topaze.resiliation.outil.business.PolitiqueResiliation;
import com.nordnet.topaze.resiliation.outil.business.ResiliationBillingInfo;
import com.nordnet.topaze.resiliation.outil.enums.TypeContrat;
import com.nordnet.topaze.resiliation.outil.util.Constants;

/**
 * Simuler le calcule de Dernier Billing.
 * 
 * @author Oussama Denden
 * 
 */
@Component("dernierBillingSimulator")
public class DernierBillingSimulator {

	/**
	 * {@link SimulationCalculeFraisResiliation}.
	 */
	@Autowired
	private SimulationCalculeFraisResiliation fraisResiliationCalculator;

	/**
	 * {@link SimulationCacluleRemboursement}.
	 */
	@Autowired
	private SimulationCacluleRemboursement remboursementCalculator;

	/**
	 * {@link SimulationCaclulePenalite}.
	 */
	@Autowired
	private SimulationCaclulePenalite penaliteCalculator;

	/**
	 * Methode qui genere les informations de facturation des elements de resiliation.
	 * 
	 * @param resiliationInfo
	 *            {@link ContratResiliationInfo}.
	 * @param resiliationBillingInfos
	 *            les information sur les elements resilie provenant de la brique contrat.
	 * @param politiqueResiliation
	 *            le politique de resiliation comme il est definit dans {@link PolitiqueResiliation}.
	 * @param isResiliationPartiel
	 *            resiliation partiel ou globla.
	 * @return {@link ContratResiliationInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ContratResiliationInfo calculeDernierBilling(ContratResiliationInfo resiliationInfo,
			ResiliationBillingInfo[] resiliationBillingInfos, PolitiqueResiliation politiqueResiliation,
			boolean isResiliationPartiel) throws TopazeException {

		/*
		 * le calcule des frais de resiliation et des rembourssement doit etre calcule selon la politique de
		 * resiliation.
		 */
		// traiter le cas de resiliation partiel apart.
		if (isResiliationPartiel) {
			/*
			 * La resiliation partiel est toujours en cascade.
			 */
			if (politiqueResiliation.isFraisResiliation()) {
				/*
				 * calcule de frais de resiliation.
				 */
				if (politiqueResiliation.getMontantResiliation() != null) {
					resiliationInfo.setFrais(fraisResiliationCalculator
							.fraisResiliationMontantDefinit(politiqueResiliation.getMontantResiliation()));
				} else {

					/*
					 * si non pour chaque contrat billing info pour chaque frais de resiliation en envoi un mouvement.
					 */
					for (ResiliationBillingInfo resiliationBillingInfo : resiliationBillingInfos) {
						fraisResiliationCalculator.fraisResiliation(resiliationBillingInfo);
					}
				}
			}

			if (politiqueResiliation.isRemboursement()) {

				if (politiqueResiliation.getMontantRemboursement() != null) {

					/*
					 * On a definit un montant definit pour la rembourssement. Il faut chercher l'elementContractuel
					 * parent pour determiner la periode de remboursement.
					 */
					ResiliationBillingInfo remboursementParent = null;
					chercherParentRemboursement: for (ResiliationBillingInfo contratBillingInfo : resiliationBillingInfos) {

						if (contratBillingInfo.isParent()) {
							remboursementParent = contratBillingInfo;
							break chercherParentRemboursement;
						}
					}

					if (remboursementParent != null && remboursementParent.getDateDerniereFacture() != null) {
						resiliationInfo.setRemboursementBillingInfo(remboursementCalculator.remboursementMontantDefini(
								remboursementParent, politiqueResiliation));
					}

				} else {
					/*
					 * si non on clacule de manier normale le remboursement de chaque element contractuel apart.
					 */
					for (ResiliationBillingInfo resiliationBillingInfo : resiliationBillingInfos) {
						if (resiliationBillingInfo.isRemboursable()
								&& resiliationBillingInfo.getDateDerniereFacture() != null) {
							remboursementCalculator.remboursement(resiliationBillingInfo, politiqueResiliation);
						}
					}
				}
			}

		} else {

			/*
			 * resiliation total.
			 */

			if (politiqueResiliation.isPenalite()) {
				/*
				 * Calculer les pénalités sur uniquement les élément parents les plus haut. Porté sur l'engagement
				 * maximale. On a autant de mouvements de pénalités que d’EC parent comportant un engagement.
				 */
				for (ResiliationBillingInfo resiliationBillingInfo : resiliationBillingInfos) {
					if (resiliationBillingInfo.isParent() && resiliationBillingInfo.getEngagement() != null
							&& resiliationBillingInfo.getEngagement() > Constants.ZERO
							&& resiliationBillingInfo.getTypeContrat().equals(TypeContrat.ABONNEMENT)) {
						penaliteCalculator.penalite(resiliationBillingInfo);
					}
				}
			}

			if (politiqueResiliation.isFraisResiliation()) {

				if (politiqueResiliation.getMontantResiliation() != null) {
					/*
					 * On a definit un frais de resiliation manuelement. Donc il faut envoyer un et un seul mouvement
					 * vers saphir. Pour sela il faut determiner le premier plus haut parent du contrat.
					 */

					// liste de elements final.
					ResiliationBillingInfo[] resiliationBillingInfoCalcule = resiliationBillingInfos;
					// liste de elements parent.
					ResiliationBillingInfo[] resiliationBillingInfoParent =
							new ResiliationBillingInfo[resiliationBillingInfos.length];
					int i = 0;
					for (ResiliationBillingInfo resiliationBillingInfo : resiliationBillingInfos) {
						if (resiliationBillingInfo.hasParent()) {
							resiliationBillingInfoParent[i] = resiliationBillingInfo.getResiliationBillingInfoParent();
							i++;
						}
					}

					// parcourire seulement la liste des element parent.
					if (i > 0) {
						resiliationBillingInfoCalcule = resiliationBillingInfoParent;
					}

					ResiliationBillingInfo resiliationParent = null;
					chercherParentResiliation: for (ResiliationBillingInfo resiliationBillingInfo : resiliationBillingInfoCalcule) {
						if (resiliationBillingInfo.isParent()) {
							/*
							 * determiner le premier plus haut parent du contrat.
							 */
							resiliationParent = resiliationBillingInfo;
							break chercherParentResiliation;
						}
					}

					if (resiliationParent != null) {
						resiliationInfo.setFrais(fraisResiliationCalculator
								.fraisResiliationMontantDefinit(politiqueResiliation.getMontantResiliation()));
					}
				} else {

					/*
					 * si non pour chaque contrat billing info pour chaque frais de resiliation en envoi un mouvement.
					 */
					for (ResiliationBillingInfo resiliationBillingInfo : resiliationBillingInfos) {
						fraisResiliationCalculator.fraisResiliation(resiliationBillingInfo);
					}
				}
			}

			if (politiqueResiliation.isRemboursement()) {

				if (politiqueResiliation.getMontantRemboursement() != null) {

					/*
					 * On a definit un montant definit pour la rembourssement. Donc il faut envoyer un et un seul
					 * mouvement vers saphir. Pour sela il faut determiner le premier plus haut parent du contrat.
					 */

					// liste de elements final.
					ResiliationBillingInfo[] resiliationBillingInfoCalcule = resiliationBillingInfos;
					// liste de elements parent.
					ResiliationBillingInfo[] resiliationBillingInfoParent =
							new ResiliationBillingInfo[resiliationBillingInfos.length];
					int i = 0;
					for (ResiliationBillingInfo resiliationBillingInfo : resiliationBillingInfos) {
						if (resiliationBillingInfo.hasParent()) {
							resiliationBillingInfoParent[i] = resiliationBillingInfo.getResiliationBillingInfoParent();
							i++;
						}
					}

					// parcourire seulement la liste des element parent.
					if (i > 0) {
						resiliationBillingInfoCalcule = resiliationBillingInfoParent;
					} else {
						i = resiliationBillingInfos.length;
					}

					ResiliationBillingInfo remboursementParent = null;
					chercherParentRemboursement: for (int j = 0; j < i; j++) {

						/*
						 * Il faut envoyer un et un seul mouvement vers saphir. Pour sela il faut determiner le premier
						 * plus haut parent du contrat.
						 */

						ResiliationBillingInfo resiliationBillingInfo = resiliationBillingInfoCalcule[j];

						if (resiliationBillingInfo.isParent()) {
							/*
							 * determiner le premier plus haut parent du contrat.
							 */
							remboursementParent = resiliationBillingInfo;
							break chercherParentRemboursement;
						}
					}

					if (remboursementParent != null && remboursementParent.getDateDerniereFacture() != null) {
						resiliationInfo.setRemboursementBillingInfo(remboursementCalculator.remboursementMontantDefini(
								remboursementParent, politiqueResiliation));
					}

				} else {
					/*
					 * sinon on clacule de manier normale le remboursement de chaque element contractuel apart.
					 */

					for (ResiliationBillingInfo resiliationBillingInfo : resiliationBillingInfos) {
						if (resiliationBillingInfo.isRemboursable()
								&& resiliationBillingInfo.getDateDerniereFacture() != null) {
							remboursementCalculator.remboursement(resiliationBillingInfo, politiqueResiliation);
						}
					}
				}

			}
		}

		for (ResiliationBillingInfo resiliationBillingInfo : resiliationBillingInfos) {
			resiliationInfo.getPaymentInfos().add(resiliationBillingInfo.getResiliationInfo());
		}

		return resiliationInfo;
	}
}
