package com.nordnet.topaze.contrat.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nordnet.topaze.contrat.business.ContratReduction;
import com.nordnet.topaze.contrat.business.ReductionInfo;
import com.nordnet.topaze.contrat.business.ReductionInterface;
import com.nordnet.topaze.contrat.domain.ContextReduction;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ContratHistorique;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.domain.ElementContractuelHistorique;
import com.nordnet.topaze.contrat.domain.FraisContrat;
import com.nordnet.topaze.contrat.domain.Reduction;
import com.nordnet.topaze.contrat.domain.TypeFrais;
import com.nordnet.topaze.contrat.domain.TypeReduction;
import com.nordnet.topaze.contrat.repository.ContratRepository;
import com.nordnet.topaze.contrat.repository.ElementContractuelHistoriqueRepository;
import com.nordnet.topaze.contrat.repository.ElementContractuelRepository;
import com.nordnet.topaze.contrat.repository.ReductionRepository;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.contrat.util.spring.ApplicationContextHolder;
import com.nordnet.topaze.contrat.validator.ContratValidator;
import com.nordnet.topaze.contrat.validator.ReductionValidator;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.logger.service.TracageService;

/**
 * Implementation du {@link ReductionService}.
 * 
 * @author akram-moncer
 * 
 */
@Service("reductionService")
public class ReductionServiceImpl implements ReductionService {

	/**
	 * Log.
	 */
	private static final Logger LOGGER = Logger.getLogger(ReductionServiceImpl.class);

	/**
	 * The contrat repository.
	 */
	@Autowired
	private ContratRepository contratRepository;

	/**
	 * The element contractuel repository.
	 */
	@Autowired
	private ElementContractuelRepository elementContractuelRepository;

	/**
	 * The element contractuelHistorique repository.
	 */
	@Autowired
	private ElementContractuelHistoriqueRepository elementContractuelHistoriqueRepository;

	/**
	 * {@link ReductionRepository}.
	 */
	@Autowired
	private ReductionRepository reductionRepository;

	/**
	 * Le contrat service {@link ContratService} .
	 */
	@Autowired
	private ContratService contratService;

	/**
	 * {@link TracageService}.
	 */
	private TracageService tracageService;

	/**
	 * {@link KeygenService}.
	 */
	@Autowired
	private KeygenService keygenService;

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
	 * {@inheritDoc }.
	 * 
	 * @throws JSONException
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String ajouterReductionContrat(ContratReduction contratReduction, String referenceContrat)
			throws TopazeException, JSONException {

		LOGGER.info("Debut methode ajouterReductionContrat");
		String refrenceReduction = null;

		ContratValidator.checkUser(contratReduction.getUser());
		Reduction reduction = contratReduction.getReduction();
		boolean fraisExist = false;
		Contrat contrat = contratRepository.findByReference(referenceContrat);
		ReductionValidator.validerContratExist(contrat, referenceContrat);

		// tester si c'est une réduction sur un contrat global.
		if (contrat != null) {
			ReductionValidator.validerReduction(contrat, reduction);

			// tester si c'est une réduction sur les frais du contrat.
			if (reduction.getTypeReduction().equals(TypeReduction.FRAIS)) {
				reduction.setNbUtilisationMax(Constants.UN);
				if (reduction.getTypeFrais() != TypeFrais.MIGRATION) {
					for (ElementContractuel contractuel : contrat.getSousContrats()) {
						if (contractuel.getFrais().size() > 0) {
							for (FraisContrat fraisContrat : contractuel.getFrais()) {
								if (fraisContrat.getTypeFrais().equals(reduction.getTypeFrais())) {
									fraisExist = true;
								}
							}

						}
					}
					// tester si le frais de la réduction existe.
					if (!fraisExist) {
						ReductionValidator.isExistFraisEnReduction(contrat, reduction.getTypeFrais().name());
					}

					ReductionValidator.validerFraisEnReduction(contrat, reduction.getTypeFrais());
					ReductionValidator.validerOrdreReduction(contrat, reduction);
				} else {
					ReductionValidator.validerReductionPourFraisMigration(contrat);
					reduction.setReferenceContrat(contrat.getReference());
				}

			} else {

				/*
				 * un element ne peut avoir plusieurs reduction.
				 */
				ReductionValidator.validerElementEnReduction(contrat, referenceContrat);
				ReductionValidator.validerOrdreReduction(contrat, reduction);
				reduction.setContextReduction(ContextReduction.GLOBAL);
				reduction.setNbUtilisationEnCours(reduction.isReductionPourVente() ? null : Constants.ZERO);
			}
			reduction.setReferenceContrat(referenceContrat);
			refrenceReduction = keygenService.getNextKey(Reduction.class);
			reduction.setReference(refrenceReduction);
			reductionRepository.save(reduction);

			contratRepository.save(contrat);

			getTracageService().ajouterTrace(Constants.PRODUCT, referenceContrat,
					"Réduction créée pour le contrat " + referenceContrat, contratReduction.getUser());

		}

		return refrenceReduction;
	}

	/**
	 * {@inheritDoc }.
	 * 
	 * @throws JSONException
	 */
	@Override
	public String ajouterReductionElementContractuelle(ContratReduction contratReduction, String referenceContrat,
			Integer numEC) throws TopazeException, JSONException {

		LOGGER.info("Debut methode ajouterReductionElementContractuelle");

		String refrenceReduction = null;
		ContratValidator.checkUser(contratReduction.getUser());
		Reduction reduction = contratReduction.getReduction();
		boolean fraisExist = false;
		ElementContractuel elementContractuel =
				elementContractuelRepository.findByNumECAndReferenceContrat(numEC, referenceContrat);
		ReductionValidator.validerElementExist(elementContractuel, referenceContrat);

		if (elementContractuel != null) {

			reduction.setReferenceContrat(referenceContrat);
			reduction.setNumEC(elementContractuel.getNumEC());

			String referenceElementContractuelle = referenceContrat + "-" + elementContractuel.getNumEC();

			ReductionValidator.validerReduction(elementContractuel, reduction);

			// tester si c'est une réduction sur les frais du EC.
			if (reduction.getTypeReduction().equals(TypeReduction.FRAIS)) {
				if (elementContractuel.getFrais().size() > 0) {
					for (FraisContrat fraisContrat : elementContractuel.getFrais()) {
						if (fraisContrat.getTypeFrais().equals(reduction.getTypeFrais())) {
							fraisExist = true;
						}
					}

					// tester si le frais de la réduction existe.
					if (!fraisExist) {
						ReductionValidator.isExistFraisEnReduction(elementContractuel, reduction.getTypeFrais().name());
					}

					ReductionValidator.validerFraisEnReduction(elementContractuel, reduction.getTypeFrais());
					ReductionValidator.validerOrdreReduction(elementContractuel, reduction);
				}

			} else {
				ReductionValidator.validerElementEnReduction(elementContractuel, referenceElementContractuelle);
				ReductionValidator.validerOrdreReduction(elementContractuel, reduction);
				reduction.setContextReduction(ContextReduction.PARTIEL);
			}
			reduction.setNbUtilisationEnCours(reduction.isReductionPourVente() ? null : Constants.ZERO);
			reduction.setReferenceContrat(referenceContrat);
			reduction.setNumEC(elementContractuel.getNumEC());
			refrenceReduction = keygenService.getNextKey(Reduction.class);
			reduction.setReference(refrenceReduction);
			reductionRepository.save(reduction);

			getTracageService().ajouterTrace(Constants.PRODUCT, referenceContrat,
					"Réduction créée pour le contrat " + referenceContrat, contratReduction.getUser());

		}

		return refrenceReduction;
	}

	/**
	 * {@inheritDoc }.
	 */
	@SuppressWarnings("null")
	@Override
	public void utiliserReduction(String referenceContrat, Integer numEC) throws TopazeException {
		Contrat contrat = contratRepository.findByReference(referenceContrat);
		ContratValidator.checkExist(referenceContrat, contrat);
		if (contrat != null && numEC == 0) {
			List<Reduction> reductionGlobals = reductionRepository.findReductionGlobales(referenceContrat, null);
			for (Reduction reductionGlobal : reductionGlobals) {
				if (reductionGlobal != null && reductionGlobal.isEligible() && !reductionGlobal.isAnnule()) {
					if (reductionGlobal.getNbUtilisationRestant() > Constants.ZERO) {
						reductionGlobal.setNbUtilisationEnCours(reductionGlobal.getNbUtilisationEnCours()
								+ Constants.UN);
						reductionRepository.save(reductionGlobal);
						getTracageService().ajouterTrace(
								Constants.PRODUCT,
								referenceContrat,
								"Réduction utilisée le " + Constants.DEFAULT_DATE_FORMAT.format(new Date())
										+ ", pour un montant de " + reductionGlobal.getValeur() + "€, pour le contrat "
										+ referenceContrat, Constants.INTERNAL_USER);
					}
				}
			}

		} else {
			ElementContractuel elementContractuel =
					elementContractuelRepository.findByNumECAndReferenceContrat(numEC, referenceContrat);
			Reduction reduction =
					reductionRepository.findReductionPartiel(elementContractuel.getContratParent().getReference(),
							null, elementContractuel.getNumEC());
			if (reduction != null && reduction.isEligible() && !reduction.isAnnule()) {
				if (reduction.getNbUtilisationRestant() > Constants.ZERO) {
					reduction.setNbUtilisationEnCours(reduction.getNbUtilisationEnCours() + Constants.UN);
					reductionRepository.save(reduction);
					getTracageService().ajouterTrace(
							Constants.PRODUCT,
							referenceContrat,
							"Réduction utilisée le " + Calendar.getInstance().getTime() + ", pour un montant de "
									+ reduction.getValeur() + "€, pour l'élément contractuel "
									+ elementContractuel.getNumEC(), Constants.INTERNAL_USER);
				}
			}
			List<Reduction> reductionGlobals =
					reductionRepository.findReductionGlobales(elementContractuel.getContratParent().getReference(),
							null);
			for (Reduction reductionGlobal : reductionGlobals) {
				if (reductionGlobal != null && reductionGlobal.isEligible() && !reductionGlobal.isAnnule()) {
					if (reductionGlobal.getNbUtilisationRestant() != null
							&& reductionGlobal.getNbUtilisationRestant() > Constants.ZERO
							&& contrat.isElementfirst(elementContractuel)) {
						reductionGlobal.setNbUtilisationEnCours(reductionGlobal.getNbUtilisationEnCours()
								+ Constants.UN);
						reductionRepository.save(reductionGlobal);
						getTracageService().ajouterTrace(
								Constants.PRODUCT,
								referenceContrat,
								"Réduction utilisée le " + Calendar.getInstance().getTime() + ", pour un montant de "
										+ reductionGlobal.getValeur() + "€, pour le contrat " + referenceContrat,
								Constants.INTERNAL_USER);
					}
				}
			}
		}

	}

	/**
	 * {@inheritDoc }.
	 */
	@Override
	public List<ReductionInfo> getReductionAssocie(String referenceContrat, Integer numEC, Integer version)
			throws TopazeException {
		ElementContractuel elementContractuel = null;
		ElementContractuelHistorique elementContractuelHistorique = null;
		if (version == null) {
			elementContractuel = elementContractuelRepository.findByNumECAndReferenceContrat(numEC, referenceContrat);
		} else {
			elementContractuelHistorique =
					elementContractuelHistoriqueRepository.findByReferenceAndVersion(referenceContrat, numEC, version);
		}
		List<ReductionInfo> reductionInfos = new ArrayList<>();

		if (elementContractuel != null) {
			ContratValidator.checkExist(referenceContrat, elementContractuel);
			Reduction reduction =
					reductionRepository.findReductionPartiel(elementContractuel.getContratParent().getReference(),
							null, elementContractuel.getNumEC());
			if (reduction != null) {
				reductionInfos.add(reduction.toReductionInfo());
			}
			List<Reduction> reductionGlobals =
					reductionRepository.findReductionGlobales(elementContractuel.getContratParent().getReference(),
							null);
			for (Reduction reductionGlobal : reductionGlobals) {
				reductionInfos.add(reductionGlobal.toReductionInfo());
			}
		} else if (elementContractuelHistorique != null) {
			ContratHistorique contratHistorique = elementContractuelHistorique.getContratParent();
			Reduction reduction =
					reductionRepository.findReductionPartiel(contratHistorique.getReference(),
							contratHistorique.getVersion(), elementContractuelHistorique.getNumEC());
			if (reduction != null) {
				reductionInfos.add(reduction.toReductionInfo());
			}
			List<Reduction> reductionGlobals =
					reductionRepository.findReductionGlobales(contratHistorique.getReference(),
							contratHistorique.getVersion());
			for (Reduction reductionGlobal : reductionGlobals) {
				reductionInfos.add(reductionGlobal.toReductionInfo());
			}
		}

		return reductionInfos;
	}

	@Override
	public List<Reduction> findReductionGlobales(String referenceContrat, Integer version) {
		return reductionRepository.findReductionGlobales(referenceContrat, version);
	}

	@Override
	public Reduction findReductionPartiel(String referenceContrat, Integer version, Integer numEC) {
		return reductionRepository.findReductionPartiel(referenceContrat, version, numEC);
	}

	@Override
	public void save(Reduction reduction) {
		reductionRepository.save(reduction);
	}

	@Override
	public Reduction findReductionGlobalSurFrais(String referenceContrat, Integer version, TypeFrais typeFrais) {
		return reductionRepository.findReductionGlobalSurFrais(referenceContrat, version, typeFrais);
	}

	@Override
	public Reduction findReductionPartielSurFrais(String referenceContrat, Integer version, Integer numEC,
			TypeFrais typeFrais) {
		return reductionRepository.findReductionPartielSurFrais(referenceContrat, version, numEC, typeFrais);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void annulerReductionContrat(String referenceContrat, String referenceReduction) throws TopazeException {
		LOGGER.info("Debut methode annulerReductionContrat");
		Contrat contrat = contratRepository.findByReference(referenceContrat);
		ContratValidator.checkExist(referenceContrat, contrat);

		Reduction reduction = reductionRepository.findByReference(referenceReduction);
		ReductionValidator.checkReductionExist(referenceReduction, reduction);
		ReductionValidator.checkAnnulationReductionpossible(reduction);

		reduction.setDateAnnulation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		reductionRepository.save(reduction);

		LOGGER.info("Fin methode annulerReductionContrat");

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void annulerReductionElementContractuelle(String referenceContrat, Integer numEC, String referenceReduction)
			throws TopazeException {

		LOGGER.info("Debut methode annulerReductionContrat");
		Contrat contrat = contratRepository.findByReference(referenceContrat);
		ContratValidator.checkExist(referenceContrat, contrat);

		Reduction reduction = reductionRepository.findByReference(referenceReduction);
		ReductionValidator.checkReductionExist(referenceReduction, reduction);
		ReductionValidator.checkAnnulationReductionpossible(reduction);

		reduction.setDateAnnulation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		reductionRepository.save(reduction);

		LOGGER.info("Fin methode annulerReductionContrat");

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ReductionInterface> getReductionsValide(String referenceContrat) throws TopazeException {
		List<ReductionInterface> reductionsInterface = new ArrayList<>();
		List<Reduction> reductions = reductionRepository.findByReferenceContrat(referenceContrat);
		for (Reduction reduction : reductions) {
			if (!reduction.isAnnule() && reduction.isEligible()) {
				reductionsInterface.add(reduction.toReductionInterface());
			}
		}
		return reductionsInterface;
	}
}
