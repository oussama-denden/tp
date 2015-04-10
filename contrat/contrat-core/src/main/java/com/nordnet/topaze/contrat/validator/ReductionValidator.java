package com.nordnet.topaze.contrat.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;

import com.google.common.base.Optional;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.domain.PolitiqueResiliation;
import com.nordnet.topaze.contrat.domain.Reduction;
import com.nordnet.topaze.contrat.domain.TypeContrat;
import com.nordnet.topaze.contrat.domain.TypeFrais;
import com.nordnet.topaze.contrat.domain.TypeReduction;
import com.nordnet.topaze.contrat.domain.TypeValeurReduction;
import com.nordnet.topaze.contrat.repository.ReductionRepository;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.contrat.util.Utils;
import com.nordnet.topaze.contrat.util.spring.ApplicationContextHolder;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Valider la reduction.
 * 
 * @author akram-moncer
 * 
 */
public class ReductionValidator {

	/**
	 * {@link PropertiesUtil}.
	 */
	private static PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();

	/**
	 * Valider une reduction.
	 * 
	 * @param element
	 *            the element
	 * @param reduction
	 *            the reduction
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validerReduction(Object element, Reduction reduction) throws TopazeException {

		PolitiqueResiliation politiqueResiliation = null;
		if (element instanceof Contrat) {
			if (((Contrat) element).isResilier()) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.64"), "1.1.64");
			}
			politiqueResiliation = ((Contrat) element).getPolitiqueResiliation();

		} else {
			if (((ElementContractuel) element).isResilier()) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.64"), "1.1.64");
			}

			politiqueResiliation = ((ElementContractuel) element).getPolitiqueResiliation();
		}

		if (reduction.getIsAffichableSurFacture() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "Reduction.isAffichableSurFacture"),
					"0.1.1");

		}
		if (reduction.getTypeReduction() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "Reduction.typeReduction"), "0.1.1");
		} else if (reduction.getTypeReduction().equals(TypeReduction.FRAIS)) {

			if (reduction.getTypeFrais() == null) {
				throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "Reduction.typeFrais"), "0.1.1");
			} else if (reduction.getTypeFrais().equals(TypeFrais.CREATION)) {

				if (element instanceof Contrat) {
					if (((Contrat) element).isValider()) {
						throw new TopazeException(propertiesUtil.getErrorMessage("1.1.66"), "1.1.66");
					}
				} else {
					if (((ElementContractuel) element).isValider()) {
						throw new TopazeException(propertiesUtil.getErrorMessage("1.1.66"), "1.1.66");
					}
				}
			}

			if (reduction.getTypeValeur().equals(TypeValeurReduction.MOIS)) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.146"), "1.1.146");
			}

		} else if (reduction.getTypeReduction().equals(TypeReduction.CONTRAT)) {
			if (reduction.getTypeFrais() != null) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.126"), "1.1.126");
			}
		}

		if (reduction.getTypeReduction() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.65"), "1.1.65");
		}

		if (reduction.getValeur() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.4", "Reduction.valeur"), "0.1.4");
		}

		if (reduction.getTypeValeur() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "Reduction.typeValeur"), "0.1.1");
		}

		if (element instanceof Contrat) {
			Contrat contrat = (Contrat) element;
			if (contrat.isVente()) {

				/*
				 * la reduction pour un contrat global de vente, les champs [dateDebut, dateFin, nbUtilisationMax]
				 * doivent etre null.
				 */
				if (reduction.getDateDebut() != null || reduction.getDateFin() != null
						|| reduction.getNbUtilisationMax() != null) {
					throw new TopazeException(propertiesUtil.getErrorMessage("1.1.143", "contrat"), "1.1.143");
				}

				if (reduction.getTypeValeur().equals(TypeValeurReduction.MOIS)) {
					throw new TopazeException(propertiesUtil.getErrorMessage("1.1.58", TypeValeurReduction.MOIS,
							TypeContrat.VENTE), "1.1.58");
				}

			} else {
				validerEligibiliteReduction(reduction);
			}
		}

		if (element instanceof ElementContractuel) {
			ElementContractuel elementContractuel = (ElementContractuel) element;
			if (elementContractuel.getTypeContrat().equals(TypeContrat.VENTE)) {

				/*
				 * un contrat de vente ne peut pas avoir une reduction de type mois.
				 */
				if (reduction.getTypeValeur().equals(TypeValeurReduction.MOIS)) {
					throw new TopazeException(propertiesUtil.getErrorMessage("1.1.58", TypeValeurReduction.MOIS,
							TypeContrat.VENTE), "1.1.58");
				}

				/*
				 * une reduction pour un element contractuel de vente, les champs [dateDebut, dateFin, nbUtilisationMax]
				 * doivent etre null.
				 */
				if (reduction.getDateDebut() != null || reduction.getDateFin() != null
						|| reduction.getNbUtilisationMax() != null) {
					throw new TopazeException(propertiesUtil.getErrorMessage("1.1.143", "element contractuel"),
							"1.1.143");
				}
			} else {
				validerEligibiliteReduction(reduction);
			}

			/*
			 * impossible de definir une reduction sur les frais de migration pour un element contratctuel.
			 */
			if (reduction.getTypeReduction() == TypeReduction.FRAIS && reduction.getTypeFrais() == TypeFrais.MIGRATION) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.109"), "1.1.109");
			}
		}

		switch (reduction.getTypeValeur()) {
		case MOIS:
			/*
			 * verifier si la valeur est entier ou non.
			 */
			Integer intValeur = reduction.getValeur().intValue();
			if (reduction.getValeur().compareTo(intValeur.doubleValue()) != 0 || reduction.getValeur() <= 0) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.51"), "1.1.51");
			}
			break;
		case MONTANT:
			/*
			 * pour une reduction de type mois la valeur de reduction doit etre positive.
			 */
			if (reduction.getValeur() <= 0) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.52"), "1.1.52");
			}
			break;
		case POURCENTAGE:
			/*
			 * pour une reduction de type pourcentage la valeur de reduction doit etre comprise entre 0 et 100.
			 */
			if (reduction.getValeur() <= 0 || reduction.getValeur() > 100) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.53"), "1.1.53");
			}
			break;
		default:
			break;
		}

		validerValeurReduction(element, reduction);

		/*
		 * verifier si la reduction n'est pas programmé aprés une résiliation future.
		 */
		if (politiqueResiliation != null) {
			if (Utils.compareDate(reduction.getDateDebut(), politiqueResiliation.getDateResiliation()) >= Constants.ZERO) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.101",
						LocalDate.fromDateFields(politiqueResiliation.getDateResiliation())), "1.1.101");
			}
		}

	}

	/**
	 * Tester si la periode et le normbre d'utilisation maximale de reduction sont coherents ou non.
	 * 
	 * @param reduction
	 *            {@link Reduction}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	private static void validerEligibiliteReduction(Reduction reduction) throws TopazeException {

		if (reduction.getNbUtilisationMax() != null && reduction.getNbUtilisationMax() <= 0) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.10", "Reduction.NbUtilisationMax"), "0.1.10");
		}

		if ((reduction.getDateDebut() != null && reduction.getDateFin() != null && reduction.getDateDebut().after(
				reduction.getDateFin()))
				|| (reduction.getDateDebut() == null && reduction.getDateFin() != null)) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.60"), "1.1.60");
		} else if (reduction.getDateDebut() != null
				&& reduction.getDateFin() != null
				&& Utils.compareDate(reduction.getDateDebut(), PropertiesUtil.getInstance().getDateDuJour().toDate()) < 0) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.89"), "1.1.89");
		} else if (reduction.getDateDebut() != null
				&& Utils.compareDate(reduction.getDateDebut(), PropertiesUtil.getInstance().getDateDuJour().toDate()) < 0) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.89"), "1.1.89");
		}

		if (reduction.getDateDebut() == null && reduction.getNbUtilisationMax() == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.59"), "1.1.59");
		}

	}

	/**
	 * Valider si le contrat dont la reduction va s'appliquer existe.
	 * 
	 * @param contrat
	 *            {@link Contrat}.
	 * @param referenceContrat
	 *            reference du contrat.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validerContratExist(Contrat contrat, String referenceContrat) throws TopazeException {
		if (contrat == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.2", referenceContrat), "0.1.2");
		}
	}

	/**
	 * Valider si le element dont la reduction va s'appliquer existe.
	 * 
	 * @param elementContractuel
	 *            {@link ElementContractuel}.
	 * @param referenceContrat
	 *            reference du contrat.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validerElementExist(ElementContractuel elementContractuel, String referenceContrat)
			throws TopazeException {
		if (elementContractuel == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.2", referenceContrat), "0.1.2");
		}
	}

	/**
	 * Valider si l'element (contrat ou element contractuel) n'a pas deja une reduction.
	 * 
	 * @param element
	 *            element dont la reduction sera appliquer.
	 * @param referenceContrat
	 *            reference contrat.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validerElementEnReduction(Object element, String referenceContrat) throws TopazeException {
		ReductionRepository reductionRepository = ApplicationContextHolder.getBean("reductionRepository");
		if (element instanceof ElementContractuel) {
			ElementContractuel elementContractuel = (ElementContractuel) element;
			Reduction reduction =
					reductionRepository.findReductionPartiel(elementContractuel.getContratParent().getReference(),
							null, elementContractuel.getNumEC());
			if (reduction != null && !reduction.isAnnule()) {
				if (reduction.getTypeReduction().equals(TypeReduction.CONTRAT)) {
					throw new TopazeException(propertiesUtil.getErrorMessage("1.1.50", referenceContrat), "1.1.50");
				}
			}
		}
	}

	/**
	 * Valider si l'element (contrat ou element contractuel) n'a pas deja une reduction.
	 * 
	 * @param element
	 *            contrat ou element contractuel.
	 * @param typeFrais
	 *            {@link TypeFrais}.
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 */
	public static void validerFraisEnReduction(Object element, TypeFrais typeFrais) throws TopazeException {
		ReductionRepository reductionRepository = ApplicationContextHolder.getBean("reductionRepository");
		Reduction reduction = null;
		if (element instanceof Contrat) {
			Contrat contrat = (Contrat) element;
			reduction = reductionRepository.findReductionGlobalSurFrais(contrat.getReference(), null, typeFrais);
			if (reduction != null) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.61"), "1.1.61");
			}
		} else {
			ElementContractuel elementContractuel = (ElementContractuel) element;
			reduction =
					reductionRepository.findReductionPartielSurFrais(elementContractuel.getContratParent()
							.getReference(), null, elementContractuel.getNumEC(), typeFrais);
			if (reduction != null) {
				throw new TopazeException(propertiesUtil.getErrorMessage("1.1.61"), "1.1.61");
			}
		}

	}

	/**
	 * Valider si l'element (contrat ou element contractuel) n'a pas de frais.
	 * 
	 * @param element
	 *            the element
	 * @param typeFrais
	 *            the type frais
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 */
	public static void isExistFraisEnReduction(Object element, String typeFrais) throws TopazeException {

		if (element instanceof Contrat) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.62", ((Contrat) element).getReference(),
					typeFrais), "1.1.62");
		}
		throw new TopazeException(propertiesUtil.getErrorMessage("1.1.63", ((ElementContractuel) element)
				.getContratParent().getReference() + "-" + ((ElementContractuel) element).getNumEC(), typeFrais),
				"1.1.63");

	}

	/**
	 * Valider les valeur de reduction.
	 * 
	 * @param element
	 *            element dont la reduction sera appliquer.
	 * @param reduction
	 *            {@link Reduction}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validerValeurReduction(Object element, Reduction reduction) throws TopazeException {
		if (reduction.getTypeValeur().equals(TypeValeurReduction.MOIS)) {
			if (element instanceof Contrat) {
				Contrat contrat = (Contrat) element;
				if (reduction.getValeur() > contrat.getMinPeriodicite()) {
					throw new TopazeException(propertiesUtil.getErrorMessage("1.1.56"), "1.1.56");
				}
			} else if (element instanceof ElementContractuel) {
				ElementContractuel elementContractuel = (ElementContractuel) element;
				if (reduction.getValeur() > elementContractuel.getPeriodicite()) {
					throw new TopazeException(propertiesUtil.getErrorMessage("1.1.57"), "1.1.57");
				}
			}
		}

	}

	/**
	 * valider le reduction pour les frais de migration.
	 * 
	 * @param contrat
	 *            {@link Contrat}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validerReductionPourFraisMigration(Contrat contrat) throws TopazeException {
		ReductionRepository reductionRepository = ApplicationContextHolder.getBean("reductionRepository");
		Reduction reduction =
				reductionRepository.findReductionPartielSurFrais(contrat.getReference(), null, null,
						TypeFrais.MIGRATION);
		if (reduction != null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.108", contrat.getReference(),
					reduction.getTypeFrais()), "1.1.108");
		}
	}

	/**
	 * valider si la reduction existe ou pas.
	 * 
	 * @param idReduction
	 *            l'id de la reduction
	 * @param reduction
	 *            {@link Reduction}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void checkReductionExist(String referenceReduction, Reduction reduction) throws TopazeException {
		if (reduction == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.135", referenceReduction), "1.1.135");
		}
	}

	/**
	 * verifer que la reduction du catalogue.
	 * 
	 * @param reduction
	 *            {@link Reduction}
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void checkAnnulationReductionpossible(Reduction reduction) throws TopazeException {

		if (reduction.isReductionCatalogue()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("1.1.134"), "1.1.134");
		}
	}

	/**
	 * validation de l'ordre d'application des reduction.
	 * 
	 * @param element
	 *            {@link Contrat} ou {@link ElementContractuel}
	 * @param reduction
	 *            {@link Reduction}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validerOrdreReduction(Object element, Reduction reduction) throws TopazeException {
		if (element instanceof Contrat) {
			Contrat contrat = (Contrat) element;
			for (ElementContractuel elementContractuel : contrat.getSousContrats()) {
				validerOrdreReductionPourElementContractuel(elementContractuel, reduction);
			}
		} else {
			ElementContractuel elementContractuel = (ElementContractuel) element;
			validerOrdreReductionPourElementContractuel(elementContractuel, reduction);
		}
	}

	/**
	 * validation de l'ordre d'application des reduction associe a un {@link ElementContractuel}.
	 * 
	 * @param elementContractuel
	 *            {@link ElementContractuel}.
	 * @param reduction
	 *            {@link Reduction}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validerOrdreReductionPourElementContractuel(ElementContractuel elementContractuel,
			Reduction reduction) throws TopazeException {
		ReductionRepository reductionRepository = ApplicationContextHolder.getBean("reductionRepository");
		List<Reduction> reductions =
				reductionRepository.findReductionsSurElementContratctuel(elementContractuel.getContratParent()
						.getReference(), null, elementContractuel.getNumEC(), reduction.getTypeReduction());
		reductions.add(reduction);
		Map<Integer, Reduction> reductionsMap = new HashMap<>();
		if (reductions.size() > Constants.UN) {
			for (Reduction reductionElement : reductions) {
				if (reductionElement.getOrdre() == null) {
					throw new TopazeException(propertiesUtil.getErrorMessage("1.1.144"), "1.1.144");
				}
				Optional<Reduction> optional = Optional.fromNullable(reductionsMap.get(reductionElement.getOrdre()));
				if (optional.isPresent()) {
					throw new TopazeException(propertiesUtil.getErrorMessage("1.1.145"), "1.1.145");
				}
				reductionsMap.put(reductionElement.getOrdre(), reductionElement);
			}
		}
	}
}
