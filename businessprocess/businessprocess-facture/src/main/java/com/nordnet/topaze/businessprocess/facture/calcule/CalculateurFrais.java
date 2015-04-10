package com.nordnet.topaze.businessprocess.facture.calcule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nordnet.topaze.businessprocess.facture.calcule.utils.CalculeUtils;
import com.nordnet.topaze.client.rest.business.facturation.ContratBillingInfo;
import com.nordnet.topaze.client.rest.business.facturation.Frais;
import com.nordnet.topaze.client.rest.business.facturation.PolitiqueMigration;
import com.nordnet.topaze.client.rest.business.facturation.ReductionInfo;
import com.nordnet.topaze.client.rest.enums.TypeContrat;
import com.nordnet.topaze.client.rest.enums.TypeFrais;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Gere le calcule des frais.
 * 
 * @author Oussama Denden
 * 
 */
@Component("calculateurFrais")
public class CalculateurFrais {

	/**
	 * {@link MouvementService}.
	 */
	@Autowired
	private MouvementService mouvementService;

	/**
	 * Facturer frais resiliation.
	 * 
	 * @param contratBillingInfo
	 *            the contrat billing info.
	 * @return le montant paye.
	 * @throws TopazeException
	 *             the topaze exception {@link ContratBillingInfo} {@link TopazeException}.
	 */
	public Double fraisResiliation(ContratBillingInfo contratBillingInfo) throws TopazeException {
		if ((contratBillingInfo.getTypeContrat().equals(TypeContrat.ABONNEMENT) || contratBillingInfo.getTypeContrat()
				.equals(TypeContrat.LOCATION))
				&& contratBillingInfo.getFrais() != null
				&& contratBillingInfo.getFrais().size() > 0) {
			Frais frais = CalculeUtils.getFraisResiliationActif(contratBillingInfo);
			if (frais != null) {
				mouvementService.sendMouvementPourFraisResiliation(contratBillingInfo, frais);

				return frais.getMontant();
			}
		}

		return null;
	}

	/**
	 * Facturer frais migration.
	 * 
	 * @param contratBillingInfosNouveau
	 *            liste des {@link ContratBillingInfo} associe au nouveau contrat.
	 * @param montantMigration
	 *            le montant du frais de migration.
	 * @param reductionSurFraisMigration
	 *            la reduction associe au frais de migration.
	 * @return le montant paye.
	 * @throws TopazeException
	 *             the topaze exception {@link ContratBillingInfo} {@link TopazeException}.
	 */
	public Double fraisMigration(ContratBillingInfo[] contratBillingInfosNouveau, Double montantMigration,
			ReductionInfo reductionSurFraisMigration) throws TopazeException {

		/*
		 * On a definit un frais de migration manuelement. Donc il faut envoyer un et un seul mouvement vers saphir.
		 * Pour sela il faut determiner le premier plus haut parent du contrat.
		 */
		ContratBillingInfo migrationParent = null;
		chercherParentMigration: for (ContratBillingInfo contratBillingInfo : contratBillingInfosNouveau) {
			/*
			 * dans le cas ou le resiliation est en cascade il faut envoyer un mouvement seulement pour
			 * l'elementContractuel parent.
			 */
			if (contratBillingInfo.isParent()) {
				migrationParent = contratBillingInfo;
				break chercherParentMigration;
			}
		}

		if (migrationParent != null) {
			mouvementService.sendMouvementPourFraisMigration(migrationParent, montantMigration,
					reductionSurFraisMigration);
			return montantMigration;

		}
		return montantMigration;
	}

	/**
	 * Calcule frais cession.
	 * 
	 * @param contratBillingInfos
	 *            {@link ContratBillingInfo}.
	 * @return frais de cession.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public Double fraisCession(ContratBillingInfo[] contratBillingInfos) throws TopazeException {

		double fraisCession = contratBillingInfos[0].getPolitiqueCession().getFraisCession();

		/*
		 * On a definit un frais de cession manuelement. Donc il faut envoyer un et un seul mouvement vers saphir. Pour
		 * sela il faut determiner le premier plus haut parent du contrat.
		 */
		ContratBillingInfo parent = null;
		chercherParent: for (ContratBillingInfo contratBillingInfo : contratBillingInfos) {
			/*
			 * dans le cas ou le resiliation est en cascade il faut envoyer un mouvement seulement pour
			 * l'elementContractuel parent.
			 */
			if (contratBillingInfo.isParent()) {
				parent = contratBillingInfo;
				break chercherParent;
			}
		}

		if (parent != null) {
			mouvementService.sendMouvementPourFraisCession(parent);
			return fraisCession;

		}
		return fraisCession;
	}

	/**
	 * Envoie des frais de creation pour le nouveau contrat apres une migration.
	 * 
	 * @param contratBillingInfosNouveau
	 *            list des {@link ContratBillingInfo} associe au nouveau contrat.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public void fraisCreationPourMigration(ContratBillingInfo[] contratBillingInfosNouveau) throws TopazeException {
		/*
		 * on a besoin de politque de migration pour la suite. La politique de migration est la meme pour tous les
		 * elementContractuels migre.
		 */
		PolitiqueMigration politiqueMigration = null;

		if (contratBillingInfosNouveau.length > 0) {
			politiqueMigration = contratBillingInfosNouveau[0].getPolitiqueMigration();
		} else {
			return;
		}
		if (politiqueMigration != null) {
			/*
			 * migration.
			 */

			// facturation du frais de creation de nv contrat
			if (politiqueMigration.isFraisCreation()) {
				for (ContratBillingInfo contratBillingInfo : contratBillingInfosNouveau) {
					if (contratBillingInfo.isNouveau() || contratBillingInfo.isMigre()) {
						if (contratBillingInfo.getPolitiqueValidation() != null
								&& !contratBillingInfo.getPolitiqueValidation().isFraisCreation()) {
							for (Frais frais : contratBillingInfo.getFrais()) {
								if (frais.getTypeFrais().equals(TypeFrais.CREATION)) {
									frais.setMontant(0.0);
								}
							}
						}
						mouvementService.sendMouvementPourFraisCreation(contratBillingInfo);
					}
				}
			}
		}
	}

}
