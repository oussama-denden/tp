package com.nordnet.topaze.businessprocess.facture.service;

import org.apache.log4j.Logger;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nordnet.topaze.businessprocess.facture.calcule.CalculateurFrais;
import com.nordnet.topaze.businessprocess.facture.calcule.CalculateurPenalite;
import com.nordnet.topaze.businessprocess.facture.calcule.CalculateurRemboursement;
import com.nordnet.topaze.businessprocess.facture.calcule.MouvementService;
import com.nordnet.topaze.businessprocess.facture.calcule.utils.ReductionUtils;
import com.nordnet.topaze.businessprocess.facture.util.Constants;
import com.nordnet.topaze.businessprocess.facture.util.PropertiesUtil;
import com.nordnet.topaze.businessprocess.facture.utils.spring.ApplicationContextHolder;
import com.nordnet.topaze.client.rest.RestClientFacture;
import com.nordnet.topaze.client.rest.RestClientNetDelivery;
import com.nordnet.topaze.client.rest.business.facturation.ContratBillingInfo;
import com.nordnet.topaze.client.rest.business.facturation.PolitiqueMigration;
import com.nordnet.topaze.client.rest.business.facturation.PolitiqueResiliation;
import com.nordnet.topaze.client.rest.enums.TypeContrat;
import com.nordnet.topaze.client.rest.enums.TypeProduit;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.logger.service.TracageService;

/**
 * 
 * @author Ahmed-Mehdi-Laabidi
 * @author Denden-OUSSAMA
 * @author akram-moncer
 * 
 */
@Service("factureService")
public class FactureServiceImpl implements FactureService {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(FactureServiceImpl.class);

	/**
	 * {@link RestClientNetDelivery}.
	 */
	@Autowired
	private RestClientFacture restClientFacture;

	/**
	 * {@link MouvementService}.
	 */
	@Autowired
	private MouvementService mouvementService;

	/**
	 * {@link CalculateurPenalite}.
	 */
	@Autowired
	private CalculateurPenalite calculateurPenalite;

	/**
	 * {@link CalculateurRemboursement}.
	 */
	@Autowired
	private CalculateurRemboursement calculateurRemboursement;

	/**
	 * {@link CalculateurFrais}.
	 */
	@Autowired
	private CalculateurFrais calculateurFrais;

	/**
	 * {@link TracageService}.
	 */
	private TracageService tracageService;

	/**
	 * 
	 * @return {@link TracageService}.
	 */
	private TracageService getTracageService() {
		if (tracageService == null) {
			if (System.getProperty("log.useMock").equals("true")) {
				tracageService = (TracageService) ApplicationContextHolder.getBean("tracageServiceMock");
			} else {
				tracageService = (TracageService) ApplicationContextHolder.getBean("tracageService");
			}
		}
		return tracageService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void calculerPremierBilling(ContratBillingInfo contratBillingInfo, Boolean isMigration)
			throws TopazeException {
		// tracer l'operation
		getTracageService().ajouterTrace(
				Constants.PRODUCT,
				contratBillingInfo.getReferenceContrat().split("-")[0],
				"Facturation des frais de création du contrat "
						+ contratBillingInfo.getReferenceContrat().split("-")[0], Constants.INTERNAL_USER);
		if (!isMigration) {
			mouvementService.sendMouvementPourFraisCreation(contratBillingInfo);
		}
		if (contratBillingInfo.getTypeContrat().equals(TypeContrat.VENTE) && !isMigration) {
			mouvementService.sendMouvementPourVente(contratBillingInfo);
			// set date dernier facture.
			restClientFacture.changerDateDerniereFacture(contratBillingInfo.getReferenceContrat(),
					contratBillingInfo.getNumEC());
			// un contrat de vente est cloture lors de la premiere facture.
			if (contratBillingInfo.getTypeProduit() == TypeProduit.BIEN) {
				restClientFacture.changerDateFinContrat(contratBillingInfo.getReferenceContrat(),
						contratBillingInfo.getNumEC());
			}
		}
		if (contratBillingInfo.getTypeContrat().equals(TypeContrat.VENTE) && !contratBillingInfo.isMigre()
				&& isMigration) {
			mouvementService.sendMouvementPourVente(contratBillingInfo);
			// set date dernier facture.
			restClientFacture.changerDateDerniereFacture(contratBillingInfo.getReferenceContrat(),
					contratBillingInfo.getNumEC());
			// un contrat de vente est cloture lors de la premiere facture.
			if (contratBillingInfo.getTypeProduit() == TypeProduit.BIEN) {
				restClientFacture.changerDateFinContrat(contratBillingInfo.getReferenceContrat(),
						contratBillingInfo.getNumEC());
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double calculerBillingRecurrent(ContratBillingInfo contratBillingInfo) throws TopazeException {

		LOGGER.info("Debut methode calculerBillingRecurrent");

		LocalDate dateJour = PropertiesUtil.getInstance().getDateDuJour();
		LocalDate dateDebutFacturation = LocalDate.fromDateFields(contratBillingInfo.getDateDebutFacturation());
		Double montantPayer = null;
		Double montant = contratBillingInfo.getMontant();
		boolean isContratFacturee = false;
		boolean endContratc =
				contratBillingInfo.getDateFinContratProbable() != null ? dateJour.toDate().compareTo(
						contratBillingInfo.getDateFinContratProbable()) == 1 : false;

		if (!endContratc) {

			switch (contratBillingInfo.getModeFacturation()) {
			case DATE_ANNIVERSAIRE:
				if (contratBillingInfo.getDateDerniereFacture() == null) {
					// billing recurrent pour la premiere fois.
					if (dateDebutFacturation.getDayOfYear() == dateJour.getDayOfYear()) {
						/*
						 * la jour courant est le jour de debut de facturation. un mouvement est envoye pour le mode
						 * paiment DATE_ANNIVERSAIRE.
						 */
						mouvementService.sendMouvement(contratBillingInfo, montant);
						// tracer l'operation
						getTracageService().ajouterTrace(
								Constants.PRODUCT,
								contratBillingInfo.getReferenceContrat().split("-")[0],
								"Facturation récurrente pour la période du "
										+ PropertiesUtil.getInstance().getDateDuJour()
										+ " au "
										+ PropertiesUtil.getInstance().getDateDuJour()
												.plusMonths(contratBillingInfo.getPeriodicite()) + " du contrat "
										+ contratBillingInfo.getReferenceContrat().split("-")[0],
								Constants.INTERNAL_USER);
						montantPayer = contratBillingInfo.getMontant();
						isContratFacturee = true;
					}
				} else {
					LocalDate dateFinPeriode =
							LocalDate.fromDateFields(contratBillingInfo.getDateDerniereFacture()).plusMonths(
									contratBillingInfo.getPeriodicite());
					if (dateJour.getDayOfYear() == dateFinPeriode.getDayOfYear()
							&& dateJour.getYear() == dateFinPeriode.getYear()) {
						mouvementService.sendMouvement(contratBillingInfo, montant);
						// tracer l'operation
						getTracageService().ajouterTrace(
								Constants.CONTRAT,
								contratBillingInfo.getReferenceContrat().split("-")[0],
								"Facturation récurrente pour la période du "
										+ PropertiesUtil.getInstance().getDateDuJour()
										+ " au "
										+ PropertiesUtil.getInstance().getDateDuJour()
												.plusMonths(contratBillingInfo.getPeriodicite()) + " du contrat "
										+ contratBillingInfo.getReferenceContrat().split("-")[0],
								Constants.INTERNAL_USER);
						montantPayer = contratBillingInfo.getMontant();
						isContratFacturee = true;
					} else // verifier s'il n'y a pas eu de facture non envoyer précédemment.
					if (dateJour.compareTo(dateFinPeriode) > Constants.ZERO
							&& dateJour.getDayOfMonth() == dateDebutFacturation.getDayOfMonth()) {
						// il y a des facture non envoyer.
						int nbrMoisNonPayer = Months.monthsBetween(dateFinPeriode, dateJour).getMonths();
						if (nbrMoisNonPayer % contratBillingInfo.getPeriodicite() == 0) {
							Double montantNonPayer = (montant / contratBillingInfo.getPeriodicite()) * nbrMoisNonPayer;
							mouvementService.sendMouvementProrata(contratBillingInfo, montantNonPayer, dateFinPeriode,
									dateJour);
							mouvementService.sendMouvement(contratBillingInfo, montant);
							// tracer l'operation
							getTracageService().ajouterTrace(
									Constants.CONTRAT,
									contratBillingInfo.getReferenceContrat().split("-")[0],
									"Facturation récurrente pour la période du "
											+ PropertiesUtil.getInstance().getDateDuJour()
											+ " au "
											+ PropertiesUtil.getInstance().getDateDuJour()
													.plusMonths(contratBillingInfo.getPeriodicite()) + " du contrat "
											+ contratBillingInfo.getReferenceContrat().split("-")[0],
									Constants.INTERNAL_USER);
							montantPayer = contratBillingInfo.getMontant();
							isContratFacturee = true;
						}
					}
				}
				break;
			case PREMIER_MOIS:
				if (dateJour.getDayOfMonth() == Constants.UN && contratBillingInfo.getDateDerniereFacture() == null) {
					// billing recurrent pour la premiere fois.

					mouvementService.sendMouvement(contratBillingInfo, montant);
					// tracer l'operation
					getTracageService().ajouterTrace(
							Constants.CONTRAT,
							contratBillingInfo.getReferenceContrat().split("-")[0],
							"Facturation récurrente pour la période du "
									+ PropertiesUtil.getInstance().getDateDuJour()
									+ " au "
									+ PropertiesUtil.getInstance().getDateDuJour()
											.plusMonths(contratBillingInfo.getPeriodicite()) + " du contrat "
									+ contratBillingInfo.getReferenceContrat().split("-")[0], Constants.INTERNAL_USER);

					if (dateDebutFacturation.getDayOfMonth() != Constants.UN
							&& contratBillingInfo.getDateDerniereFacture() == null) {
						// calcul de prorata.
						int nbrJourConsomme = Days.daysBetween(dateDebutFacturation, dateJour).getDays();
						Double prixMois = null;
						prixMois = montant / contratBillingInfo.getPeriodicite();
						Double prixJour = prixMois / dateDebutFacturation.dayOfMonth().getMaximumValue();
						Double prorata = prixJour * nbrJourConsomme;
						mouvementService.sendMouvementProrata(contratBillingInfo, prorata, dateDebutFacturation,
								dateJour.minusDays(Constants.UN));
						montantPayer = contratBillingInfo.getMontant();
					}
					isContratFacturee = true;
				} else if (dateJour.getDayOfMonth() == Constants.UN
						&& dateJour.compareTo(new LocalDate(contratBillingInfo.getDateDerniereFacture())) != Constants.ZERO) {
					// billing recurrent pour les autre fois.
					LocalDate dateFinPeriode =
							LocalDate.fromDateFields(contratBillingInfo.getDateDerniereFacture()).plusMonths(
									contratBillingInfo.getPeriodicite());
					if (dateJour.getYear() == dateFinPeriode.getYear()
							&& dateJour.getDayOfYear() == dateFinPeriode.getDayOfYear()) {
						mouvementService.sendMouvement(contratBillingInfo, montant);
						// tracer l'operation
						getTracageService().ajouterTrace(
								Constants.CONTRAT,
								contratBillingInfo.getReferenceContrat().split("-")[0],
								"Facturation récurrente pour la période du "
										+ PropertiesUtil.getInstance().getDateDuJour()
										+ " au "
										+ PropertiesUtil.getInstance().getDateDuJour()
												.plusMonths(contratBillingInfo.getPeriodicite()) + " du contrat "
										+ contratBillingInfo.getReferenceContrat().split("-")[0],
								Constants.INTERNAL_USER);
						montantPayer = contratBillingInfo.getMontant();
						isContratFacturee = true;
					} else // verifier s'il n'y a pas eu de facture non envoyer précédemment.
					if (dateJour.compareTo(dateFinPeriode) > Constants.ZERO) {
						// il y a des facture non envoyer.
						int nbrMoisNonPayer = Months.monthsBetween(dateFinPeriode, dateJour).getMonths();
						if (nbrMoisNonPayer % contratBillingInfo.getPeriodicite() == Constants.ZERO) {
							Double montantNonPayer = (montant / contratBillingInfo.getPeriodicite()) * nbrMoisNonPayer;
							mouvementService.sendMouvementProrata(contratBillingInfo, montantNonPayer, dateFinPeriode,
									dateJour);
							mouvementService.sendMouvement(contratBillingInfo, montant);
							// tracer l'operation
							getTracageService().ajouterTrace(
									Constants.CONTRAT,
									contratBillingInfo.getReferenceContrat().split("-")[0],
									"Facturation récurrente pour la période du "
											+ PropertiesUtil.getInstance().getDateDuJour()
											+ " au "
											+ PropertiesUtil.getInstance().getDateDuJour()
													.plusMonths(contratBillingInfo.getPeriodicite()) + " du contrat "
											+ contratBillingInfo.getReferenceContrat().split("-")[0],
									Constants.INTERNAL_USER);
							montantPayer = contratBillingInfo.getMontant();
							isContratFacturee = true;
						}
					}

				}
				break;
			default:
				break;
			}

		}

		if (isContratFacturee) {
			restClientFacture.changerDateDerniereFacture(contratBillingInfo.getReferenceContrat(),
					contratBillingInfo.getNumEC());
			if (contratBillingInfo.getToutReductions().size() > Constants.ZERO
					|| ReductionUtils.getDiscount(contratBillingInfo.getReductionEC(),
							contratBillingInfo.getPeriodicite()) != null) {
				restClientFacture.utiliserReduction(contratBillingInfo.getReferenceContrat(),
						contratBillingInfo.getNumEC());
			}
		}

		return montantPayer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void calculeDernierBilling(ContratBillingInfo[] contratBillingInfos, boolean isResiliationPartiel,
			boolean calculRemboursement, boolean calculFraisResiliation, boolean calculFraisPenalite)
			throws TopazeException {

		/*
		 * on a besoin de politque de resiliation pour la suite. La politique de resiliation est la meme pour tous les
		 * elementContractuels resilies.
		 */
		PolitiqueResiliation politiqueResiliation = null;

		if (contratBillingInfos.length > 0) {
			// tracer l'operation
			getTracageService().ajouterTrace(
					Constants.PRODUCT,
					contratBillingInfos[Constants.ZERO].getReferenceContrat().split("-")[Constants.ZERO],
					"Facturation des frais de résiliation et/ou pénalités du contrat "
							+ contratBillingInfos[Constants.ZERO].getReferenceContrat().split("-")[Constants.ZERO],
					Constants.INTERNAL_USER);
			politiqueResiliation = contratBillingInfos[Constants.ZERO].getPolitiqueResiliation();
		} else {
			return;
		}

		/*
		 * le calcule des frais de resiliation et des rembourssement doit etre calcule selon la politique de
		 * resiliation.
		 */
		// traiter le cas de resiliation partiel apart.
		if (isResiliationPartiel) {
			if (politiqueResiliation != null) {
				/*
				 * La resiliation partiel est toujours en cascade.
				 */
				if (politiqueResiliation.isFraisResiliation() && calculFraisResiliation) {
					/*
					 * calcule de frais de resiliation.
					 */
					if (politiqueResiliation.getMontantResiliation() != null) {
						/*
						 * On a definit un frais de resiliation manuelement. Donc il faut envoyer un et un seul
						 * mouvement vers saphir. Pour sela il faut determiner le premier plus haut parent du contrat.
						 */
						ContratBillingInfo resiliationParent = null;
						chercherParentResiliation: for (ContratBillingInfo contratBillingInfo : contratBillingInfos) {
							/*
							 * dans le cas ou le resiliation est en cascade il faut envoyer un mouvement seulement pour
							 * l'elementContractuel parent.
							 */
							if (contratBillingInfo.isParent()) {
								resiliationParent = contratBillingInfo;
								break chercherParentResiliation;
							}
						}

						if (resiliationParent != null) {
							mouvementService.sendMouvementPourMontantResiliation(resiliationParent,
									politiqueResiliation.getMontantResiliation());
						}
					} else {

						/*
						 * si non pour chaque contrat billing info pour chaque frais de resiliation en envoi un
						 * mouvement.
						 */
						for (ContratBillingInfo contratBillingInfo : contratBillingInfos) {
							calculateurFrais.fraisResiliation(contratBillingInfo);
						}
					}
				}

				if (politiqueResiliation.isRemboursement() && calculRemboursement) {

					if (politiqueResiliation.getMontantRemboursement() != null) {
						/*
						 * Il faut envoyer un et un seul mouvement vers saphir.
						 */

						ContratBillingInfo remboursementParent = null;
						chercherParentRemboursement: for (ContratBillingInfo contratBillingInfo : contratBillingInfos) {
							/*
							 * On a definit un montant definit pour la rembourssement. Donc pour le cas d'un resiliation
							 * en cascade il faut envoyer un et un seul mouvement vers saphir. Pour sela il faut
							 * determiner l'elementContractuel parent.
							 */
							if (contratBillingInfo.isParent()) {
								/*
								 * Pour le cas d'un resiliation en cascade. Tester si l'element contractuel est parent
								 * ou non.
								 */
								remboursementParent = contratBillingInfo;
								break chercherParentRemboursement;
							}
						}

						if (remboursementParent != null) {
							calculateurRemboursement.remboursementMontantDefini(remboursementParent,
									politiqueResiliation.getMontantRemboursement());
						}

					} else {
						/*
						 * si non on clacule de manier normale le remboursement de chaque element contractuel apart.
						 */
						for (ContratBillingInfo contratBillingInfo : contratBillingInfos) {
							if (contratBillingInfo.isRemboursable()) {
								calculateurRemboursement.remboursement(contratBillingInfo);
							}
						}
					}
				}
			}

		} else {
			if (politiqueResiliation != null) {
				/*
				 * resiliation total.
				 */

				if (politiqueResiliation.isPenalite() && calculFraisPenalite) {
					/*
					 * Calculer les pénalités sur uniquement les élément parents les plus haut. Porté sur l'engagement
					 * maximale. On a autant de mouvements de pénalités que d’EC parent comportant un engagement.
					 */
					for (ContratBillingInfo billingInfo : contratBillingInfos) {
						if (billingInfo.isParent() && billingInfo.getEngagement() != null
								&& billingInfo.getEngagement() > Constants.ZERO
								&& billingInfo.getTypeContrat().equals(TypeContrat.ABONNEMENT)) {
							calculateurPenalite.penalite(billingInfo);
						}
					}
				}

				if (politiqueResiliation.isFraisResiliation() && calculFraisResiliation) {

					if (politiqueResiliation.getMontantResiliation() != null) {
						/*
						 * On a definit un frais de resiliation manuelement. Donc il faut envoyer un et un seul
						 * mouvement vers saphir. Pour sela il faut determiner le premier plus haut parent du contrat.
						 */

						// liste de elements final.
						ContratBillingInfo[] contratBillingInfoCalcule = contratBillingInfos;
						// liste de elements parent.
						ContratBillingInfo[] contratBillingInfoParent =
								new ContratBillingInfo[contratBillingInfos.length];
						int i = 0;
						for (ContratBillingInfo contratBillingInfo : contratBillingInfos) {
							if (contratBillingInfo.hasParent()) {
								contratBillingInfoParent[i] = contratBillingInfo.getContratBillingInfoParent();
								i++;
							}
						}

						// parcourire seulement la liste des element parent.
						if (i > 0) {
							contratBillingInfoCalcule = contratBillingInfoParent;
						}

						ContratBillingInfo resiliationParent = null;
						chercherParentResiliation: for (ContratBillingInfo contratBillingInfo : contratBillingInfoCalcule) {
							if (contratBillingInfo.isParent()) {
								/*
								 * determiner le premier plus haut parent du contrat.
								 */
								resiliationParent = contratBillingInfo;
								break chercherParentResiliation;
							}
						}

						if (resiliationParent != null) {
							mouvementService.sendMouvementPourMontantResiliation(resiliationParent,
									politiqueResiliation.getMontantResiliation());
						}
					} else {

						/*
						 * si non pour chaque contrat billing info pour chaque frais de resiliation en envoi un
						 * mouvement.
						 */
						for (ContratBillingInfo contratBillingInfo : contratBillingInfos) {
							calculateurFrais.fraisResiliation(contratBillingInfo);
						}
					}
				}

				if (politiqueResiliation.isRemboursement() && calculRemboursement) {

					if (politiqueResiliation.getMontantRemboursement() != null) {

						/*
						 * On a definit un montant definit pour la rembourssement. Donc il faut envoyer un et un seul
						 * mouvement vers saphir. Pour sela il faut determiner le premier plus haut parent du contrat.
						 */

						// liste de elements final.
						ContratBillingInfo[] contratBillingInfoCalcule = contratBillingInfos;
						// liste de elements parent.
						ContratBillingInfo[] contratBillingInfoParent =
								new ContratBillingInfo[contratBillingInfos.length];
						int i = 0;
						for (ContratBillingInfo contratBillingInfo : contratBillingInfos) {
							if (contratBillingInfo.hasParent()) {
								contratBillingInfoParent[i] = contratBillingInfo.getContratBillingInfoParent();
								i++;
							}
						}

						// parcourire seulement la liste des element parent.
						if (i > 0) {
							contratBillingInfoCalcule = contratBillingInfoParent;
						} else {
							i = contratBillingInfos.length;
						}

						ContratBillingInfo remboursementParent = null;
						chercherParentRemboursement: for (int j = 0; j < i; j++) {

							/*
							 * Il faut envoyer un et un seul mouvement vers saphir. Pour sela il faut determiner le
							 * premier plus haut parent du contrat.
							 */

							ContratBillingInfo contratBillingInfo = contratBillingInfoCalcule[j];

							if (contratBillingInfo.isParent()) {
								/*
								 * determiner le premier plus haut parent du contrat.
								 */
								remboursementParent = contratBillingInfo;
								break chercherParentRemboursement;
							}
						}

						if (remboursementParent != null) {
							calculateurRemboursement.remboursementMontantDefini(remboursementParent,
									politiqueResiliation.getMontantRemboursement());
						}

					} else {
						/*
						 * sinon on clacule de manier normale le remboursement de chaque element contractuel apart.
						 */
						for (ContratBillingInfo contratBillingInfo : contratBillingInfos) {
							if (contratBillingInfo.isRemboursable()) {
								calculateurRemboursement.remboursement(contratBillingInfo);
							}
						}
					}

				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void calculeBillingMigration(ContratBillingInfo[] contratBillingInfosHistorise,
			ContratBillingInfo[] contratBillingInfosNouveau) throws TopazeException {

		/*
		 * on a besoin de politque de migration pour la suite. La politique de migration est la meme pour tous les
		 * elementContractuels migre.
		 */
		PolitiqueMigration politiqueMigration = null;

		if (contratBillingInfosHistorise.length > Constants.ZERO) {
			politiqueMigration = contratBillingInfosHistorise[Constants.ZERO].getPolitiqueMigration();
		} else {
			return;
		}
		if (politiqueMigration != null) {
			/*
			 * migration.
			 */

			// facturation des frais de migration
			if (politiqueMigration.getFraisMigration() != null) {
				calculateurFrais.fraisMigration(contratBillingInfosNouveau, politiqueMigration.getFraisMigration(),
						contratBillingInfosHistorise[Constants.ZERO].getReductionSurFraisMigration());
			}

			calculateurFrais.fraisCreationPourMigration(contratBillingInfosNouveau);

		}

	}

	@Override
	public void calculeDernierBilligMigration(ContratBillingInfo[] contratBillingInfosHistorise, boolean penalite,
			boolean remboursement, boolean fraisResiliation, boolean remboursementAdmnistratif) throws TopazeException {

		/*
		 * on a besoin de politque de migration pour la suite. La politique de migration est la meme pour tous les
		 * elementContractuels migre.
		 */
		PolitiqueMigration politiqueMigration = null;

		if (contratBillingInfosHistorise.length > Constants.ZERO) {
			politiqueMigration = contratBillingInfosHistorise[Constants.ZERO].getPolitiqueMigration();
		} else {
			return;
		}
		if (politiqueMigration != null) {
			/*
			 * migration.
			 */

			if (penalite && politiqueMigration.isPenalite()) {
				/*
				 * Calculer les penalites sur uniquement les Ã©lÃ©ment parents les plus haut. On a autant de mouvements
				 * de pÃ©nalitÃ©s que dâ€™EC parent comportant un engagement.
				 */
				for (ContratBillingInfo billingInfo : contratBillingInfosHistorise) {
					if (billingInfo.isParent() && billingInfo.getTypeContrat().equals(TypeContrat.ABONNEMENT)
							&& billingInfo.getEngagement() != null) {
						calculateurPenalite.penalite(billingInfo);
					}
				}
			}

			// facturation des frais de resiliation
			if (fraisResiliation && politiqueMigration.isFraisResiliation()) {

				/*
				 * calcule de frais de resiliation.
				 */
				if (politiqueMigration.getMontantResiliation() != null) {
					/*
					 * On a definit un frais de resiliation manuelement. Donc il faut envoyer un et un seul mouvement
					 * vers saphir. Pour sela il faut determiner le premier plus haut parent du contrat.
					 */
					ContratBillingInfo resiliationParent = null;
					chercherParentResiliation: for (ContratBillingInfo contratBillingInfo : contratBillingInfosHistorise) {
						/*
						 * dans le cas ou le resiliation est en cascade il faut envoyer un mouvement seulement pour
						 * l'elementContractuel parent.
						 */
						if (contratBillingInfo.isParent()) {
							resiliationParent = contratBillingInfo;
							break chercherParentResiliation;
						}
					}

					if (resiliationParent != null) {
						mouvementService.sendMouvementPourMontantResiliation(resiliationParent,
								politiqueMigration.getMontantResiliation());
					}
				} else {

					/*
					 * si non pour chaque contrat billing info resilie ou migre pour chaque frais de resiliation en
					 * envoi un mouvement.
					 */
					for (ContratBillingInfo contratBillingInfo : contratBillingInfosHistorise) {
						if (contratBillingInfo.isMigre() || contratBillingInfo.isResilie()) {
							calculateurFrais.fraisResiliation(contratBillingInfo);
						}
					}
				}
			}

			if (remboursement && politiqueMigration.isRemboursement()) {
				if (politiqueMigration.getMontantRemboursement() != null) {
					/*
					 * Il faut envoyer un et un seul mouvement vers saphir.
					 */

					ContratBillingInfo remboursementParent = null;
					chercherParentRemboursement: for (ContratBillingInfo contratBillingInfo : contratBillingInfosHistorise) {
						/*
						 * On a definit un montant definit pour la rembourssement. Donc pour le cas d'un resiliation en
						 * cascade il faut envoyer un et un seul mouvement vers saphir. Pour sela il faut determiner
						 * l'elementContractuel parent.
						 */
						if (contratBillingInfo.isParent()) {
							/*
							 * Pour le cas d'un resiliation en cascade. Tester si l'element contractuel est parent ou
							 * non.
							 */
							remboursementParent = contratBillingInfo;
							break chercherParentRemboursement;
						}
					}

					if (remboursementParent != null) {
						calculateurRemboursement.remboursementMontantDefini(remboursementParent,
								politiqueMigration.getMontantRemboursement());
					}

				} else {
					/*
					 * si non on clacule de manier normale le remboursement de chaque element contractuel apart.
					 */
					for (ContratBillingInfo contratBillingInfo : contratBillingInfosHistorise) {
						boolean rembNormaleEligible =
								contratBillingInfo.isRemboursable()
										&& (contratBillingInfo.isResilie() || contratBillingInfo.isMigre())
										&& !remboursementAdmnistratif;
						boolean rembAdministratifEligible =
								remboursementAdmnistratif
										&& (contratBillingInfo.getIsCalculeRemboursementAdministratif());
						if (rembNormaleEligible || rembAdministratifEligible) {
							calculateurRemboursement.remboursement(contratBillingInfo);
						}
					}
				}
			}
		}
	}

	@Override
	public void calculeFraisCession(ContratBillingInfo[] contratBillingInfos) throws TopazeException {
		String referenceContrat = contratBillingInfos[0].getReferenceContrat();
		getTracageService().ajouterTrace(Constants.PRODUCT, referenceContrat,
				"facturation des frais de cession du contrat " + referenceContrat, Constants.INTERNAL_USER);
		calculateurFrais.fraisCession(contratBillingInfos);

	}

	@Override
	public void calculeBillingMigrationAdministrative(ContratBillingInfo[] contratBillingInfosHistorise,
			ContratBillingInfo[] contratBillingInfosMigrationAdministrative) throws TopazeException {

		// identifer les elements migres administrativement avec un montant change pour facturer le remboursement
		for (ContratBillingInfo contratBillingInfoHistorique : contratBillingInfosHistorise) {
			for (ContratBillingInfo contratBillingInfoNouveau : contratBillingInfosMigrationAdministrative) {
				if ((contratBillingInfoHistorique.getNumEC() == contratBillingInfoNouveau.getNumEC())
						&& contratBillingInfoNouveau.getDateDerniereFacture() == null) {
					contratBillingInfoHistorique.setIsCalculeRemboursementAdministratif(true);
				}
			}
		}

		calculeDernierBilligMigration(contratBillingInfosHistorise, true, true, true, true);

		calculeBillingMigration(contratBillingInfosHistorise, contratBillingInfosMigrationAdministrative);
		// calcule de facturation pour premier billing , prorata et billing recurrent
		for (ContratBillingInfo contratBillingInfo : contratBillingInfosMigrationAdministrative) {

			calculerPremierBilling(contratBillingInfo, true);

			// si contrat de vente(periodicite null) donc pas de billing recurrent.
			if (contratBillingInfo.getPeriodicite() != null) {
				calculerBillingRecurrent(contratBillingInfo);
			}
		}

	}

}
