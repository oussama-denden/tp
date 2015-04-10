package com.nordnet.topaze.billing.outil.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nordnet.common.valueObject.constants.DiscountType;
import com.nordnet.common.valueObject.date.TimePeriod;
import com.nordnet.common.valueObject.money.Discount;
import com.nordnet.common.valueObject.number.Amount;
import com.nordnet.topaze.billing.outil.business.DiscountInfo;
import com.nordnet.topaze.billing.outil.business.PenaliteBillingInfo;
import com.nordnet.topaze.billing.outil.util.Constants;
import com.nordnet.topaze.billing.outil.util.ReductionUtils;
import com.nordnet.topaze.client.rest.RestClientBillingOutil;
import com.nordnet.topaze.client.rest.business.facturation.ReductionInfo;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Implementation du services {@link PenaliteService}.
 * 
 * @author Oussama Denden
 * 
 */
@Service("penaliteService")
public class PenaliteServiceImpl implements PenaliteService {

	/**
	 * {@link RestClientBillingOutil}.
	 */
	@Autowired
	private RestClientBillingOutil restClientBillingOutil;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PenaliteBillingInfo getPenaliteBillingInfo(String referenceContrat, Integer numEC, Integer version,
			Integer engagement, Integer periodicite, Double montant, Date derniereFactureDate,
			Date debutFacturationDate, Date finEngagementDate, Date finContratDate) throws TopazeException {

		LocalDate dateFinEngagement = LocalDate.fromDateFields(finEngagementDate);
		LocalDate dateFinContrat = LocalDate.fromDateFields(finContratDate);
		LocalDate dateDebutFacturation = LocalDate.fromDateFields(debutFacturationDate);
		if (dateFinEngagement.compareTo(dateFinContrat) > Constants.ZERO) {
			Double penalite = null;
			TimePeriod periodFacturation = new TimePeriod(dateFinContrat, dateFinEngagement);
			penalite =
					calculPenalite(engagement, periodicite, montant, dateFinContrat, dateFinEngagement,
							dateDebutFacturation);

			Discount discountPenalite =
					getReductionPourPenalite(referenceContrat, numEC, version, engagement, periodicite, montant,
							derniereFactureDate, finContratDate, debutFacturationDate, finEngagementDate);
			double penaliteSansPromotion =
					calculPenalite(engagement, periodicite, montant, periodFacturation.getStartFrom(),
							periodFacturation.getEndTo(), dateDebutFacturation);

			PenaliteBillingInfo penaliteBillingInfo = new PenaliteBillingInfo();
			penaliteBillingInfo.setMontantPenalite(penalite);
			penaliteBillingInfo.setMontantPenaliteSansPromotion(penaliteSansPromotion);
			penaliteBillingInfo.setDiscount(discountPenalite);
			penaliteBillingInfo.setTimePeriod(periodFacturation);

			return penaliteBillingInfo;
		}

		return null;

	}

	/**
	 * Frais penalite.
	 * 
	 * @param contratBillingInfo
	 *            {@link ContratBillingInfo}.
	 * @param montant
	 *            montant a payer par periode.
	 * @param dateDebutPeriodePenalite
	 *            date debut de la periode de penalite.
	 * @param dateFinPeriodePenalite
	 *            date fin de la periode penalite.
	 * @return la penalite facture.
	 */

	/**
	 * Calcule montant penalite.
	 * 
	 * @param engagement
	 *            engagement.
	 * @param periodicite
	 *            periodicite.
	 * @param montant
	 *            montant.
	 * @param dateDebutPeriodePenalite
	 *            date debut periode penalite.
	 * @param dateFinPeriodePenalite
	 *            date fin periode penalite.
	 * @param dateDebutFacturation
	 *            date debut facturation.
	 * @return montant penalite.
	 */
	public Double calculPenalite(Integer engagement, Integer periodicite, Double montant,
			LocalDate dateDebutPeriodePenalite, LocalDate dateFinPeriodePenalite, LocalDate dateDebutFacturation) {

		double prorataDebut = Constants.ZERO;
		if (dateDebutPeriodePenalite.getDayOfMonth() != Constants.PREMIER_JOUR_DU_MOIS) {
			int nbrJourMoisDebut = dateDebutPeriodePenalite.dayOfMonth().getMaximumValue();
			int nbrJourPenaliteDebut = nbrJourMoisDebut - dateDebutPeriodePenalite.getDayOfMonth() + Constants.UN;

			prorataDebut = (montant / (periodicite * nbrJourMoisDebut)) * nbrJourPenaliteDebut;

		}
		double prorataFin = Constants.ZERO;
		if (dateFinPeriodePenalite.getDayOfMonth() != dateFinPeriodePenalite.dayOfMonth().getMaximumValue()
				&& dateFinPeriodePenalite.getDayOfMonth() != Constants.PREMIER_JOUR_DU_MOIS) {
			int nbrJourMoisFin = dateFinPeriodePenalite.dayOfMonth().getMaximumValue();
			int nbrJourPenaliteFin = dateFinPeriodePenalite.getDayOfMonth() - Constants.UN;

			prorataFin = (montant / (periodicite * nbrJourMoisFin)) * nbrJourPenaliteFin;

		}
		LocalDate dateDebut = null;
		if (prorataDebut != Constants.ZERO) {
			dateDebut = dateDebutPeriodePenalite.dayOfMonth().withMaximumValue();
		} else {
			dateDebut = dateDebutPeriodePenalite;
		}
		LocalDate dateFin = null;
		if (prorataFin != Constants.ZERO) {
			dateFin = dateFinPeriodePenalite.dayOfMonth().withMinimumValue();
		} else {
			dateFin = dateFinPeriodePenalite;
		}
		int nbrMoisPenalite = Months.monthsBetween(dateDebut, dateFin).getMonths();
		double prixParMois = Constants.ZERO;

		prixParMois = montant / periodicite;

		double penalite = prixParMois * nbrMoisPenalite + prorataDebut + prorataFin;
		if (engagement > Constants.DOUZE) {
			LocalDate dateFinAnneeEngagement = dateDebutFacturation.plusMonths(Constants.DOUZE);
			if (dateDebutPeriodePenalite.compareTo(dateFinAnneeEngagement) < Constants.ZERO) {

				if (prorataFin != Constants.ZERO) {
					dateFin = dateFinAnneeEngagement.dayOfMonth().withMinimumValue();
				}

				int nbrMoisPenaliteComplete = Months.monthsBetween(dateDebut, dateFin).getMonths();
				double penaliteComplete = nbrMoisPenaliteComplete * prixParMois + prorataDebut + prorataFin;
				if (prorataDebut != Constants.ZERO) {
					dateDebut = dateFinAnneeEngagement.dayOfMonth().withMaximumValue();
				}
				if (prorataFin != Constants.ZERO) {
					dateFin = dateFinPeriodePenalite.dayOfMonth().withMinimumValue();
				}
				int nbrMoisPenalitePartielle = Months.monthsBetween(dateDebut, dateFin).getMonths();
				double penalitePartielle =
						nbrMoisPenalitePartielle * prixParMois * Constants.POURCENTAGE_ENGAGEMENT + prorataDebut
								+ prorataFin * Constants.POURCENTAGE_ENGAGEMENT;
				penalite = penalitePartielle + penaliteComplete;
			} else {
				penalite = penalite * Constants.POURCENTAGE_ENGAGEMENT;
			}

		}

		return penalite;
	}

	/**
	 * Calculer la reduction pour la penalite.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param numEC
	 *            numero elementcontractuel.
	 * @param version
	 *            version.
	 * @param engagement
	 *            engagement.
	 * @param periodicite
	 *            periodicite.
	 * @param montant
	 *            montant.
	 * @param derniereFactureDate
	 *            date derniere facture.
	 * @param finContratDate
	 *            date fin contrat.
	 * @param debutFacturationDate
	 *            date debut facturation.
	 * @param finEngagementDate
	 *            date fin engagement.
	 * @return le discount de la penalite.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public Discount getReductionPourPenalite(String referenceContrat, Integer numEC, Integer version,
			Integer engagement, Integer periodicite, Double montant, Date derniereFactureDate, Date finContratDate,
			Date debutFacturationDate, Date finEngagementDate) throws TopazeException {

		List<ReductionInfo> reductionInfos =
				restClientBillingOutil.getReductionAssocie(referenceContrat, numEC, version);

		LocalDate dateDerniereFacture = LocalDate.fromDateFields(derniereFactureDate);
		LocalDate dateFinContrat = LocalDate.fromDateFields(finContratDate);
		LocalDate dateDebutFacturation = LocalDate.fromDateFields(debutFacturationDate);
		LocalDate dateFinEngagement = LocalDate.fromDateFields(finEngagementDate);
		LocalDate dateFinAnneeEngagement =
				engagement > Constants.DOUZE ? dateDebutFacturation.plusMonths(Constants.DOUZE).minusDays(Constants.UN)
						: null;
		Discount discountPenalite = null;
		Discount discountDerniereFacture = null;
		/*
		 * list qui contiendra tout les discounts dans la periode de penalite.
		 */
		List<Discount> penaliteDiscountsList = new ArrayList<>();
		/*
		 * on suppose que la resiliation est lors de la derniere facture donc dans un premier temps on calcule la
		 * reduction de penalite sur la periode de [dateDerniereFacture - DateFinEngagement].
		 */
		LocalDate dateFacture = dateDerniereFacture;
		while (dateFacture.compareTo(dateFinEngagement) < Constants.ZERO) {
			/*
			 * une liste des reduction eligible a la date 'dateFacture'.
			 */
			List<ReductionInfo> reductionEligible = new ArrayList<>();
			/*
			 * on boucle sur toute la periode pour calculer la reduction a chaque suppose payement.
			 */
			for (ReductionInfo reductionInfo : reductionInfos) {
				if (reductionInfo.isEligible(dateDebutFacturation, dateFacture, periodicite)) {
					reductionEligible.add(reductionInfo);
				}
			}
			/*
			 * le calcue de discount se fait pour chaque mois pas pour chaque periode pour traiter les cas ou
			 * l'engagement n'est pas un multiple de la periodicite..
			 */
			for (int i = 0; i < periodicite; i++) {
				/*
				 * il ne faut pas depasser la dateFinEngagement.
				 */
				if (dateFacture.compareTo(dateFinEngagement) < Constants.ZERO
						&& reductionEligible.size() != Constants.ZERO) {

					Discount discountMois = null;
					Amount amountMois = null;

					List<DiscountInfo> discountInfos =
							ReductionUtils.calculerReduction(periodicite, montant, reductionEligible);
					for (DiscountInfo discountInfo : discountInfos) {
						Discount discountPeriode = discountInfo.getDiscount();

						if (discountPeriode.getDiscountType() == DiscountType.VALUE_HT) {
							/*
							 * vue qu'on calcule la reduction pour chaque mois alors la reductionMois = reductionPeriode
							 * / periodecite.
							 */
							amountMois =
									new Amount(discountPeriode.getAmount().getAmount().doubleValue() / periodicite);
							discountMois =
									new Discount(discountMois != null ? discountMois.getAmount().add(amountMois)
											: amountMois, discountPeriode.getDiscountType());
						} else {
							/*
							 * si la reduction est en '%' alors la valeur reduction pour une periode est la meme que
							 * pour un mois.
							 */
							discountMois =
									new Discount(discountMois != null ? discountMois.getAmount().add(
											discountPeriode.getAmount()) : discountPeriode.getAmount(),
											discountPeriode.getDiscountType());
						}
					}

					if (discountMois != null) {
						/*
						 * apres la 1er annee d'engagement la reduction est egal à 25%.
						 */
						if (dateFinAnneeEngagement != null && dateFacture.isAfter(dateFinAnneeEngagement)) {
							amountMois =
									new Amount(discountMois.getAmount().getAmount().doubleValue()
											* Constants.POURCENTAGE_ENGAGEMENT);
							discountMois = new Discount(amountMois, DiscountType.VALUE_HT);

						}
						penaliteDiscountsList.add(discountMois);
						/*
						 * on sauve le discount etabli lors de la derniere facture.
						 */
						if (dateFacture.equals(dateDerniereFacture)) {
							discountDerniereFacture = discountMois;
						}
					}
				}
				dateFacture = dateFacture.plusMonths(Constants.UN);
			}
		}

		if (penaliteDiscountsList.size() > Constants.ZERO) {
			/*
			 * si les reduction applique lors de la periode de penalite sont tous égaux et de type 'POURCENTAGE' alors
			 * pas besoin de calculer la reduction en montant.
			 */
			boolean calculeReductionEnMontant = true;
			if (penaliteDiscountsList.get(Constants.ZERO).getDiscountType() == DiscountType.PERCENT) {
				int frequency = Collections.frequency(penaliteDiscountsList, penaliteDiscountsList.get(Constants.ZERO));
				if (frequency == penaliteDiscountsList.size()
						&& penaliteDiscountsList.size() == Months.monthsBetween(dateDerniereFacture, dateFinEngagement)
								.getMonths()) {
					calculeReductionEnMontant = false;
				}
			}

			if (calculeReductionEnMontant) {
				/*
				 * a partir de la list des discount on calcule la reduction total.
				 */
				double discountTotal = 0d;
				for (Discount discount : penaliteDiscountsList) {
					if (discount.getDiscountType().equals(DiscountType.PERCENT)) {
						discountTotal += (montant * discount.getAmount().getAmount().doubleValue()) / Constants.CENT;
					} else {
						discountTotal += discount.getAmount().getAmount().doubleValue();
					}
				}
				/*
				 * lors des calcul precedent la resiliation est suppose faite a la meme date que la derniere facture or
				 * ça peut ne pas etre vrai alors la periode [dateDerniereFacture-DateFinContrat] doit etre deduite des
				 * calcule.
				 */
				if (discountDerniereFacture != null) {
					double discountDeduit = 0d;
					int nbMois = 0;
					int nbJour = 0;
					/*
					 * varifier si la dateFinAnneeEngegement n'est pas dans la periode [dateDerniereFacture -
					 * dateFinContrat]
					 */
					if (dateFinAnneeEngagement != null && dateFinAnneeEngagement.isAfter(dateDerniereFacture)
							&& dateFinAnneeEngagement.isBefore(dateFinContrat)) {
						/*
						 * calcule du montant de la reduction dans a periode [dateDerniereFacture -
						 * dateFinAnneeEngagement]
						 */
						nbMois = Months.monthsBetween(dateDerniereFacture, dateFinAnneeEngagement).getMonths();
						nbJour =
								Days.daysBetween(dateDerniereFacture.plusMonths(nbMois), dateFinAnneeEngagement)
										.getDays();
						if (discountDerniereFacture.getDiscountType().equals(DiscountType.VALUE_HT)) {
							double discountJour =
									discountDerniereFacture.getAmount().getAmount().doubleValue()
											/ dateFinContrat.dayOfMonth().getMaximumValue();
							discountDeduit =
									discountDerniereFacture.getAmount().getAmount().doubleValue() * nbMois + nbJour
											* discountJour;
							discountTotal -= discountDeduit;
						} else {
							discountDeduit =
									montant
											* (discountDerniereFacture.getAmount().getAmount().doubleValue() / Constants.CENT)
											* (nbMois + (nbJour / dateFinContrat.dayOfMonth().getMaximumValue()));
							discountTotal -= discountDeduit;
						}

						/*
						 * calcule du montant de la reduction dans a periode [dateFinAnneeEngagement - dateFinContrat]
						 */
						nbMois = Months.monthsBetween(dateFinAnneeEngagement, dateFinContrat).getMonths();
						nbJour = Days.daysBetween(dateFinAnneeEngagement.plusMonths(nbMois), dateFinContrat).getDays();
						if (discountDerniereFacture.getDiscountType().equals(DiscountType.VALUE_HT)) {
							double discountJour =
									discountDerniereFacture.getAmount().getAmount().doubleValue()
											/ dateFinContrat.dayOfMonth().getMaximumValue();
							discountDeduit =
									discountDerniereFacture.getAmount().getAmount().doubleValue() * nbMois + nbJour
											* discountJour;
							discountTotal -= discountDeduit * Constants.POURCENTAGE_ENGAGEMENT;
						} else {
							discountDeduit =
									montant
											* (discountDerniereFacture.getAmount().getAmount().doubleValue() / Constants.CENT)
											* (nbMois + (nbJour / dateFinContrat.dayOfMonth().getMaximumValue()));
							discountTotal -= discountDeduit * Constants.POURCENTAGE_ENGAGEMENT;
						}
					} else {
						/*
						 * calcule du montant de la reduction dans la periode [dateDernireFacture - dateFinContrat]
						 */
						nbMois = Months.monthsBetween(dateDerniereFacture, dateFinContrat).getMonths();
						nbJour = Days.daysBetween(dateDerniereFacture.plusMonths(nbMois), dateFinContrat).getDays();
						if (discountDerniereFacture.getDiscountType().equals(DiscountType.VALUE_HT)) {
							double discountJour =
									(discountDerniereFacture.getAmount().getAmount().doubleValue())
											/ dateFinContrat.dayOfMonth().getMaximumValue();
							discountDeduit =
									discountDerniereFacture.getAmount().getAmount().doubleValue() * nbMois + nbJour
											* discountJour;
							discountTotal -= discountDeduit;
						} else {
							discountDeduit =
									(montant * nbMois)
											+ (nbJour * (montant / dateFinContrat.dayOfMonth().getMaximumValue()))
											* (discountDerniereFacture.getAmount().getAmount().doubleValue() / Constants.CENT);
							discountTotal -= discountDeduit;
						}
					}
				}

				if (discountTotal != 0d) {
					Amount amount = new Amount(discountTotal);
					discountPenalite = new Discount(amount, DiscountType.VALUE_HT);
				}
			} else {
				discountPenalite = penaliteDiscountsList.get(Constants.ZERO);
			}
		}
		return discountPenalite;
	}
}
