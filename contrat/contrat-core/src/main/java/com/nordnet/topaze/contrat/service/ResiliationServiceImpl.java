package com.nordnet.topaze.contrat.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nordnet.topaze.contrat.business.AnnulationInfo;
import com.nordnet.topaze.contrat.business.ContratResiliationtInfo;
import com.nordnet.topaze.contrat.business.Frais;
import com.nordnet.topaze.contrat.business.PolitiqueResiliation;
import com.nordnet.topaze.contrat.business.ReductionInfo;
import com.nordnet.topaze.contrat.business.ResiliationBillingInfo;
import com.nordnet.topaze.contrat.domain.Avenant;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ContratHistorique;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.domain.FraisContrat;
import com.nordnet.topaze.contrat.domain.Reduction;
import com.nordnet.topaze.contrat.domain.TypeResiliation;
import com.nordnet.topaze.contrat.jms.ContratMessagesSender;
import com.nordnet.topaze.contrat.repository.AvenantRepository;
import com.nordnet.topaze.contrat.repository.ContratHistoriqueRepository;
import com.nordnet.topaze.contrat.repository.ContratRepository;
import com.nordnet.topaze.contrat.repository.ElementContractuelRepository;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.ContratUtils;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.contrat.util.Utils;
import com.nordnet.topaze.contrat.util.spring.ApplicationContextHolder;
import com.nordnet.topaze.contrat.validator.ContratValidator;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.logger.service.TracageService;

/**
 * L'implementation de service {@link ResiliationService}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Service("resiliationService")
public class ResiliationServiceImpl implements ResiliationService {

	/**
	 * Loggeur sur la Classe ContratServiceImpl.
	 */
	private static final Logger LOGGER = Logger.getLogger(ResiliationServiceImpl.class);

	/**
	 * {@link ContratRepository}.
	 */
	@Autowired
	private ContratRepository contratRepository;

	/**
	 * {@link ContratHistoriqueRepository}.
	 */
	@Autowired
	private ContratHistoriqueRepository contratHistoriqueRepository;

	/**
	 * {@link ElementContractuelRepository}.
	 */
	@Autowired
	private ElementContractuelRepository elementContractuelRepository;

	/**
	 * {@link ReductionService}.
	 */
	@Autowired
	public ReductionService reductionService;

	/**
	 * {@link ContratMessagesSender}.
	 */
	@Autowired
	private ContratMessagesSender contratMessagesSender;

	/**
	 * {@link TracageService}.
	 */
	private TracageService tracageService;
	/**
	 * l avenant repository. {@link AvenantRepository}.
	 */
	@Autowired
	private AvenantRepository avenantRepository;

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
	public void resilierContrat(String referenceContrat, PolitiqueResiliation politiqueResiliation, String user,
			boolean isMigration, boolean isCession, boolean isMigrationFuture, boolean isCessionFuture,
			boolean isCronResiliationFutur, boolean isRenouvellement) throws TopazeException {

		// tracer l'opération de résiliation global
		if (!isMigration) {
			getTracageService().ajouterTrace(
					Constants.PRODUCT,
					referenceContrat,
					"Demande de résiliaton pour le contrat " + referenceContrat + " "
							+ politiqueResiliation.getCommentaire(), user);
		}
		ContratValidator.checkPolitiqueResiliation(politiqueResiliation);

		// tester si la cession est possible :pas de migration ,resiliation,cession ou renouvellement future
		checkActionInFuture(referenceContrat, isMigrationFuture, isCessionFuture, isCronResiliationFutur,
				isRenouvellement, Constants.ZERO, false, false, isMigration);

		ContratValidator.checkUser(user);

		Contrat contrat = contratRepository.findByReference(referenceContrat);
		ContratValidator.checkExist(referenceContrat, contrat);

		if (contrat.isPreparer()) {
			contrat.resilier(politiqueResiliation.getTypeResiliation(), politiqueResiliation.toDomain(), PropertiesUtil
					.getInstance().getDateDuJour().toDate(), false, false);
			contratRepository.save(contrat);
			return;
		}

		// test si c'est une résiliation future.
		boolean isResiliationFuture = false;
		String dateResiliation = politiqueResiliation.getDateResiliation();
		if ((dateResiliation == null && politiqueResiliation.getDelaiDeSecurite())
				|| !Utils.isStringNullOrEmpty(dateResiliation)) {
			isResiliationFuture =
					dateResiliation != null ? Utils.compareDate(
							ContratValidator.verifierDateWithoutTimeValide(dateResiliation), PropertiesUtil
									.getInstance().getDateDuJour().toDate()) == 1 : false;

			if (isResiliationFuture) {
				politiqueResiliation.setDelaiDeSecurite(false);
			}

			if (politiqueResiliation.getDelaiDeSecurite() && !isCronResiliationFutur) {
				Date localDate = PropertiesUtil.getInstance().getDateDuJourWithTime().toDate();
				politiqueResiliation.setDateResiliation(Utils.formatDateWithTime(localDate));
				isResiliationFuture = true;
			}

			// verifer que la date de resilaiation ne depasse pas la date du fin contrat
			if (dateResiliation != null) {
				ContratValidator.checkActionPossibleInDate(
						ContratValidator.verifierDateWithoutTimeValide(politiqueResiliation.getDateResiliation()),
						ContratUtils.getDateFinContrat(contrat), referenceContrat, "Resiliation", isRenouvellement);
			}

		}

		ContratValidator.checkDateRemboursement(politiqueResiliation, contrat);
		ContratValidator.checkResiliationPossible(contrat, isResiliationFuture);
		ContratValidator.checkRemboursementTotal(contrat, politiqueResiliation);

		if (politiqueResiliation.getTypeResiliation().name().equals("RIN")) {
			resilierContratParNordNet(contrat, politiqueResiliation, isResiliationFuture);
		} else {
			resilierContratParClient(contrat, politiqueResiliation, isResiliationFuture, isMigration, isCession,
					isRenouvellement);
		}
		// tracer l'opération de résiliation global
		if (!isMigration) {
			getTracageService().ajouterTrace(Constants.PRODUCT, referenceContrat,
					"Résiliation du contrat " + referenceContrat, user);
		}

	}

	/**
	 * Resilier contrat par client.
	 * 
	 * @param contrat
	 *            contrat a resilie.
	 * @param politiqueResiliation
	 *            the politique resiliation
	 * @param isMigration
	 *            the is migration l'usager.
	 * @param isResiliationFuture
	 *            the is resiliation future
	 * @param isCession
	 *            true si la resiliation se fait dans la cadre d'une cession du contrat.
	 * @throws TopazeException
	 *             the description contrat non valide exception
	 * @throws ContratResiliationException
	 *             the contrat resiliation exception {@link TopazeException}.
	 * @throws TopazeException
	 *             the contrat exception {@link ContratResiliationException}.
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 */
	private void resilierContratParClient(Contrat contrat, PolitiqueResiliation politiqueResiliation,
			boolean isResiliationFuture, boolean isMigration, boolean isCession, boolean isRenouvellement)
			throws TopazeException {

		contrat.resilier(TypeResiliation.RIC, politiqueResiliation.toDomain(), PropertiesUtil.getInstance()
				.getDateDuJour().toDate(), isResiliationFuture, isMigration);

		contratRepository.save(contrat);

		// vérifier si ce n'est pas une résiliation future ou un applle du batch résiliation future.

		if ((politiqueResiliation.getDateResiliation() == null || !isResiliationFuture) && !isMigration && !isCession
				&& !isRenouvellement) {
			contratMessagesSender.sendContractResiliatedEvent(contrat.getReference(), null, false);
		}
	}

	/**
	 * Resilier contrat par nord net.
	 * 
	 * @param contrat
	 *            contrat a resilie.
	 * @param politiqueResiliation
	 *            the politique resiliation
	 * @param isResiliationFuture
	 *            the is resiliation future
	 * @throws TopazeException
	 *             the description contrat non valide exception
	 * @throws ContratResiliationException
	 *             the contrat resiliation exception {@link TopazeException}.
	 * @throws TopazeException
	 *             the topaze exception {@link ContratResiliationException}. {@link TopazeException}.
	 */
	private void resilierContratParNordNet(Contrat contrat, PolitiqueResiliation politiqueResiliation,
			boolean isResiliationFuture) throws TopazeException {

		contrat.resilier(TypeResiliation.RIN, politiqueResiliation.toDomain(), PropertiesUtil.getInstance()
				.getDateDuJour().toDate(), isResiliationFuture, false);

		contratRepository.save(contrat);

		// vérifier si ce n'est pas une résiliation future ou un applle du batch résiliation future.
		if ((politiqueResiliation.getDateResiliation() == null || !isResiliationFuture)) {
			contratMessagesSender.sendContractResiliatedEvent(contrat.getReference(), null, false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resiliationPartiel(String referenceContrat, Integer numEC, PolitiqueResiliation politiqueResiliation,
			String user, boolean isRenouvellement, boolean isResiliationFuturePartiel) throws TopazeException {

		// tracer l'opération de résiliation global
		getTracageService().ajouterTrace(Constants.PRODUCT, referenceContrat,
				"Demande de résiliaton pour le contrat " + referenceContrat, user);
		ContratValidator.checkPolitiqueResiliation(politiqueResiliation);
		ContratValidator.checkUser(user);

		Contrat contrat = contratRepository.findByReference(referenceContrat);
		ContratValidator.checkExist(referenceContrat, contrat);

		ElementContractuel elementContractuelResilier =
				elementContractuelRepository.findByNumECAndReferenceContrat(numEC, referenceContrat);
		ContratValidator.checkExist(referenceContrat, numEC, elementContractuelResilier);
		// tester si la resilaiation est possible :pas de migration ,resiliation,cession ou renouvellement future
		checkActionInFuture(referenceContrat, false, false, false, isRenouvellement, numEC, true,
				isResiliationFuturePartiel, false);

		// test si c'est une résiliation future.
		boolean isResiliationFuture = false;
		String dateResiliation = politiqueResiliation.getDateResiliation();
		if ((dateResiliation == null && politiqueResiliation.getDelaiDeSecurite()) || dateResiliation != null) {
			isResiliationFuture =
					dateResiliation != null ? Utils.compareDate(
							ContratValidator.verifierDateWithoutTimeValide(politiqueResiliation.getDateResiliation()),
							PropertiesUtil.getInstance().getDateDuJour().toDate()) == 1 : false;

			// verifer que la date de migration ne depasse pas la date du fin contrat
			if (dateResiliation != null) {
				ContratValidator.checkActionPossibleInDate(
						ContratValidator.verifierDateWithoutTimeValide(politiqueResiliation.getDateResiliation()),
						ContratUtils.getDateFinContrat(contrat), referenceContrat, "Resiliation", isRenouvellement);
			}
		}

		ContratValidator.checkDateRemboursement(politiqueResiliation, contrat);
		ContratValidator.checkResiliationPartielPossible(referenceContrat, numEC, contrat, isResiliationFuture);
		ContratValidator.checkRemboursementPartiel(elementContractuelResilier, politiqueResiliation);

		if (politiqueResiliation.getTypeResiliation().name().equals("RIN")) {
			resiliationPartielParNordNet(contrat, numEC, politiqueResiliation, isResiliationFuture);
		} else {
			resiliationPartielParClient(contrat, numEC, politiqueResiliation, isResiliationFuture);
		}
		// tracer l'opération de résiliation global
		getTracageService().ajouterTrace(Constants.PRODUCT, referenceContrat,
				"Résiliation partielle du contrat " + referenceContrat + " pour le produit " + numEC, user);
	}

	/**
	 * Resiliation partiel par client.
	 * 
	 * @param contrat
	 *            contrat global.
	 * @param numEC
	 *            numero {@link ElementContractuel}.
	 * @param politiqueResiliation
	 *            the politique resiliation.
	 * @param isResiliationFuture
	 *            the is resiliation future
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 */
	private void resiliationPartielParClient(Contrat contrat, Integer numEC, PolitiqueResiliation politiqueResiliation,
			boolean isResiliationFuture) throws TopazeException {

		List<Integer> referenceContratsResilier =
				contrat.resilierCascade(PropertiesUtil.getInstance().getDateDuJour().toDate(), TypeResiliation.RIC,
						numEC, politiqueResiliation.toDomain(), PropertiesUtil.getInstance().getDateDuJour().toDate(),
						isResiliationFuture);

		contratRepository.save(contrat);

		// vérifier si ce n'est pas une résiliation future ou un applle du batch résiliation future.
		if (politiqueResiliation.getDateResiliation() == null || !isResiliationFuture) {
			for (Integer numECElement : referenceContratsResilier) {

				// tracer l'opération de résiliation partielle
				// ElementContractuel elementContractuel =
				// elementContractuelRepository.findByReference(referenceContratResilier);
				// creerTrace("ElementContractuel", elementContractuel.getId(), user, TypeOperation.MODIFICATION);
				contratMessagesSender.sendContractResiliatedEvent(contrat.getReference(), numECElement, true);
				contratMessagesSender.sendContractPartialResiliatedEvent(contrat.getReference(), numECElement);
			}
		}
	}

	/**
	 * Resiliation partiel par nord net.
	 * 
	 * @param contrat
	 *            contrat global.
	 * @param numEC
	 *            numero {@link ElementContractuel}.
	 * @param politiqueResiliation
	 *            the politique resiliation.
	 * @param isResiliationFuture
	 *            the is resiliation future
	 * @throws TopazeException
	 *             the description contrat non valide exception
	 * @throws InformationValidationContratNonValideException
	 *             the information validation contrat non valide exception
	 * @throws ContratResiliationException
	 *             the contrat resiliation exception {@link TopazeException}.
	 * @throws TopazeException
	 *             the topaze exception {@link TopazeException}.
	 */
	private void resiliationPartielParNordNet(Contrat contrat, Integer numEC,
			PolitiqueResiliation politiqueResiliation, boolean isResiliationFuture) throws TopazeException {

		List<Integer> referenceContratsResilier =
				contrat.resilierCascade(PropertiesUtil.getInstance().getDateDuJour().toDate(), TypeResiliation.RIN,
						numEC, politiqueResiliation.toDomain(), PropertiesUtil.getInstance().getDateDuJour().toDate(),
						isResiliationFuture);

		contratRepository.save(contrat);
		// vérifier si ce n'est pas une résiliation future ou un applle du batch résiliation future.
		if (politiqueResiliation.getDateResiliation() == null || !isResiliationFuture) {
			for (Integer numECElement : referenceContratsResilier) {

				// tracer l'opération de résiliation partielle
				// ElementContractuel elementContractuel =
				// elementContractuelRepository.findByReference(referenceContratResilier);
				// creerTrace("ElementContractuel", elementContractuel.getId(), user, TypeOperation.MODIFICATION);
				contratMessagesSender.sendContractResiliatedEvent(contrat.getReference(), numECElement, true);
				contratMessagesSender.sendContractPartialResiliatedEvent(contrat.getReference(), numECElement);

			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resiliationAutomatique(ElementContractuel sousContrat) {
		sousContrat.setTypeResiliation(TypeResiliation.RIC);
		contratRepository.save(sousContrat.getContratParent());

		// contratMessagesSender.sendContractResiliatedEvent(sousContrat.getReference(), true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ResiliationBillingInfo> getResiliationBillingIformation(String referenceContrat) throws TopazeException {
		List<ResiliationBillingInfo> resiliationBillingInfos = new ArrayList<>();
		Contrat contrat = contratRepository.findByReference(referenceContrat);
		ContratValidator.checkExist(referenceContrat, contrat);
		ContratValidator.checkResiliationPossible(contrat, false);
		if (contrat.getDateDebutFacturation() != null) {
			Integer version = null;
			ContratHistorique contratHistorique = contratHistoriqueRepository.findDerniereVersion(referenceContrat);
			if (contratHistorique != null) {
				version = contratHistorique.getVersion();
			}

			for (ElementContractuel elementContractuel : contrat.getSousContrats()) {
				if (elementContractuel.isResiliable()) {
					ResiliationBillingInfo resiliationBillingInfo = creerResiliationBillingInfo(elementContractuel);
					resiliationBillingInfo.setVersion(version);
					if (elementContractuel.getElementContractuelParent() != null) {
						for (ResiliationBillingInfo resiliationBillingInfoParent : resiliationBillingInfos) {
							if (elementContractuel.getElementContractuelParent().getContratParent().getReference()
									.equals(resiliationBillingInfoParent.getReferenceContrat())
									&& elementContractuel.getElementContractuelParent().getNumEC()
											.equals(resiliationBillingInfoParent.getNumEC())) {
								resiliationBillingInfo.setResiliationBillingInfoParent(resiliationBillingInfoParent);
							}
						}
					}

					if (elementContractuel.getSousElementContractuel() != null) {
						for (ElementContractuel sousElementContractuel : elementContractuel.getSousElementContractuel()) {
							for (ResiliationBillingInfo resiliationBillingInfoFils : resiliationBillingInfos) {
								if (resiliationBillingInfoFils.getReferenceContrat().equals(
										sousElementContractuel.getContratParent().getReference())
										&& resiliationBillingInfoFils.getNumEC().equals(
												sousElementContractuel.getNumEC())) {
									resiliationBillingInfoFils.setResiliationBillingInfoParent(resiliationBillingInfo);
								}
							}
						}
					}

					resiliationBillingInfos.add(resiliationBillingInfo);
				}
			}
		}

		return resiliationBillingInfos;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ResiliationBillingInfo> getResiliationBillingIformation(String referenceContrat, Integer numEC)
			throws TopazeException {
		List<ResiliationBillingInfo> resiliationBillingInfos = new ArrayList<>();
		Contrat contrat = contratRepository.findByReference(referenceContrat);
		ContratValidator.checkResiliationPartielPossible(referenceContrat, numEC, contrat, false);
		Integer version = null;
		ContratHistorique contratHistorique = contratHistoriqueRepository.findDerniereVersion(referenceContrat);
		if (contratHistorique != null) {
			version = contratHistorique.getVersion();
		}
		resiliationBillingInfos = getElementsResilie(contrat.getSousContrats(), numEC);

		for (ResiliationBillingInfo resiliationBillingInfo : resiliationBillingInfos) {
			resiliationBillingInfo.setVersion(version);
		}
		return resiliationBillingInfos;
	}

	/**
	 * Creer la liste de {@link ResiliationBillingInfo} pour chaque {@link ElementContractuel}.
	 * 
	 * @param sousContrats
	 *            {@link ElementContractuel}.
	 * @param numEC
	 *            numEC de produit.
	 * @return {@link ResiliationBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	private List<ResiliationBillingInfo> getElementsResilie(Set<ElementContractuel> sousContrats, Integer numEC)
			throws TopazeException {
		List<ResiliationBillingInfo> resiliationBillingInfos = new ArrayList<>();
		for (ElementContractuel elementContractuel : sousContrats) {
			if (elementContractuel.getNumEC() == numEC && elementContractuel.isResiliable()) {
				resiliationBillingInfos.add(creerResiliationBillingInfo(elementContractuel));
			}

			if (elementContractuel.isResiliable() && elementContractuel.getElementContractuelParent() != null
					&& elementContractuel.getElementContractuelParent().getNumEC() == numEC) {
				resiliationBillingInfos.add(creerResiliationBillingInfo(elementContractuel));
				resiliationBillingInfos.addAll(getElementsResilie(elementContractuel.getSousElementContractuel(),
						elementContractuel.getNumEC()));
			}
		}

		return resiliationBillingInfos;
	}

	/**
	 * generer le {@link ResiliationBillingInfo} d'un {@link ElementContractuel}.
	 * 
	 * @param elementContractuel
	 *            {@link ElementContractuel}.
	 * @return {@link ResiliationBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	private ResiliationBillingInfo creerResiliationBillingInfo(ElementContractuel elementContractuel)
			throws TopazeException {

		ResiliationBillingInfo resiliationBillingInfo = new ResiliationBillingInfo();
		resiliationBillingInfo.setReferenceProduit(elementContractuel.getReferenceProduit());
		resiliationBillingInfo.setReferenceContrat(elementContractuel.getContratParent().getReference());
		resiliationBillingInfo.setNumEC(elementContractuel.getNumEC());
		resiliationBillingInfo.setDateDebutFacturation(elementContractuel.getContratParent().getDateDebutFacturation());
		resiliationBillingInfo.setDateDerniereFacture(elementContractuel.getDateDerniereFacture());
		resiliationBillingInfo.setDateFinContrat(PropertiesUtil.getInstance().getDateDuJour().toDate());
		resiliationBillingInfo.setTypeElement(elementContractuel.getTypeProduit());
		resiliationBillingInfo.setTypeContrat(elementContractuel.getTypeContrat());
		resiliationBillingInfo.setRemboursable(elementContractuel.isRemboursable());
		resiliationBillingInfo.setDateValidation(elementContractuel.getDateValidation());

		Reduction reductionEC =
				reductionService.findReductionPartiel(elementContractuel.getContratParent().getReference(), null,
						elementContractuel.getNumEC());

		if (reductionEC != null && reductionEC.isEligible() && !reductionEC.isAnnule()) {
			ReductionInfo reductionInfo = reductionEC.toReductionInfo();
			resiliationBillingInfo.setReductionInfoEC(reductionInfo);
		}

		List<Reduction> reductionGlobales =
				reductionService.findReductionGlobales(elementContractuel.getContratParent().getReference(), null);
		for (Reduction reductionGlobale : reductionGlobales) {
			if (reductionGlobale.isEligible() && !reductionGlobale.isAnnule()) {
				ReductionInfo reductionInfo = reductionGlobale.toReductionInfo();
				resiliationBillingInfo.addReductionInfoContrat(reductionInfo);
			}
		}

		if (elementContractuel.getMontant() != null) {
			resiliationBillingInfo.setMontant(elementContractuel.getMontant());
			resiliationBillingInfo.setEngagement(elementContractuel.getEngagement());
			resiliationBillingInfo.setDateFinEngagement(elementContractuel.getDateFinEngagement());
			resiliationBillingInfo.setDureeContrat(elementContractuel.getDuree());
			resiliationBillingInfo.setPeriodicite(elementContractuel.getPeriodicite());
			resiliationBillingInfo.setModeFacturation(elementContractuel.getModeFacturation());
		}

		resiliationBillingInfo.getFrais().addAll(getFrais(elementContractuel));

		return resiliationBillingInfo;
	}

	@Override
	public Boolean isElementContractuelResilier(String referenceContrat, Integer numEC) throws TopazeException {
		ElementContractuel elementContractuel =
				elementContractuelRepository.findByNumECAndReferenceContrat(numEC, referenceContrat);
		ContratValidator.checkExist(referenceContrat, elementContractuel);
		return elementContractuel.isResilier();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Contrat> findContratsResiliationFuture(String dateDuJour) {

		return contratRepository.findContratsResiliationFuture(dateDuJour);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Integer> findECResiliationFuture(String dateDuJour) {
		return elementContractuelRepository.findECResiliationFutureWithDate(dateDuJour);
	}

	/**
	 * Creation de liste de {@link Frais} à partir des frais associe a l {@link ElementContractuel}.
	 * 
	 * @param elementContractuel
	 *            {@link ElementContractuel}.
	 * @return liste des {@link Frais}.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	private List<Frais> getFrais(ElementContractuel elementContractuel) throws TopazeException {
		List<Frais> fraisSet = new ArrayList<>();
		ReductionInfo info = null;
		for (FraisContrat fraisContrat : elementContractuel.getFrais()) {
			Reduction reductionGlobal =
					reductionService.findReductionGlobalSurFrais(elementContractuel.getContratParent().getReference(),
							null, fraisContrat.getTypeFrais());
			Reduction reductionPartiel =
					reductionService.findReductionPartielSurFrais(elementContractuel.getContratParent().getReference(),
							null, elementContractuel.getNumEC(), fraisContrat.getTypeFrais());
			Frais f = new Frais();
			f.setMontant(fraisContrat.getMontant());
			f.setNombreMois(fraisContrat.getNombreMois());
			f.setOrdre(fraisContrat.getOrdre());
			f.setTypeFrais(fraisContrat.getTypeFrais());
			f.setTitre(fraisContrat.getTitre());
			if (reductionGlobal != null && reductionGlobal.isEligible() && !reductionGlobal.isAnnule()) {
				info = reductionGlobal.toReductionInfo();
				f.setReductionInfoContrat(info);
			}

			if (reductionPartiel != null && reductionPartiel.isEligible() && !reductionPartiel.isAnnule()) {
				info = reductionPartiel.toReductionInfo();
				f.setReductionInfoEC(info);
			}

			fraisSet.add(f);
		}

		return fraisSet;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("null")
	@Override
	public void annulerResiliation(String referenceContrat, AnnulationInfo annulationResiliationInfo)
			throws TopazeException {
		LOGGER.info("Entrer methode annulerResiliation");

		Contrat contratGlobal = contratRepository.findByReference(referenceContrat);
		ContratValidator.checkExist(referenceContrat, contratGlobal);
		// traçage de l'operation.
		getTracageService().ajouterTrace(
				Constants.PRODUCT,
				referenceContrat,
				"Annulation de la demande de resiliation pour le contrat " + referenceContrat + " "
						+ annulationResiliationInfo.getCommentaire(), annulationResiliationInfo.getUser());
		Contrat contratResilier = contratRepository.findContratResiliationFuture(referenceContrat);
		com.nordnet.topaze.contrat.domain.PolitiqueResiliation politiqueResiliation =
				contratResilier != null ? contratResilier.getPolitiqueResiliation() : null;
		ContratValidator.checkPolitiqueResiliationExist(politiqueResiliation, referenceContrat, true);
		ContratValidator.checkAnnulationResiliationPossible(contratResilier);
		politiqueResiliation.setDateAnnulation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		politiqueResiliation.setCommentaireAnnulation(annulationResiliationInfo.getCommentaire());
		contratRepository.save(contratResilier);
		LOGGER.info("Fin methode annulerResiliation");

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("null")
	@Override
	public void annulerResiliationPartiel(String referenceContrat, Integer numEC,
			AnnulationInfo annulationResiliationInfo) throws TopazeException {
		LOGGER.info("Entrer methode annulerResiliationPartiel");

		ElementContractuel elementContractuel =
				elementContractuelRepository.findByNumECAndReferenceContrat(numEC, referenceContrat);
		ContratValidator.checkExist(referenceContrat, elementContractuel);
		// traçage de l'operation.
		getTracageService().ajouterTrace(
				Constants.PRODUCT,
				referenceContrat,
				"Annulation de la demande de resiliation pour l'element "
						+ elementContractuel.getContratParent().getReference() + "-" + elementContractuel.getNumEC(),
				annulationResiliationInfo.getUser());

		ElementContractuel elementContractuelResilier =
				elementContractuelRepository.findECResiliationFuture(referenceContrat, numEC);
		com.nordnet.topaze.contrat.domain.PolitiqueResiliation politiqueResiliation =
				elementContractuelResilier != null ? elementContractuelResilier.getPolitiqueResiliation() : null;
		ContratValidator.checkPolitiqueResiliationExist(politiqueResiliation, elementContractuel.getContratParent()
				.getReference() + "-" + elementContractuel.getNumEC(), false);
		// ContratValidator.checkAnnulationResiliationPossible(politiqueResiliation);
		politiqueResiliation.setDateAnnulation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		politiqueResiliation.setCommentaireAnnulation(annulationResiliationInfo.getCommentaire());
		elementContractuelRepository.save(elementContractuelResilier);
		LOGGER.info("Fin methode annulerResiliationPartiel");
	}

	/**
	 * etre sur q'une seule action active existe pour un contrat
	 * 
	 * @param referenceContrat
	 * @throws TopazeException
	 */
	private void checkActionInFuture(String referenceContrat, boolean isMigrationFuture, boolean isCessionFuture,
			boolean isResiliationFuture, boolean isRenouvenouvellement, Integer numEC, boolean isResiliationPartiel,
			boolean isResiliationFuturPartiel, boolean isMigration) throws TopazeException {
		// tester si le renouvellement est possible :pas de migration ,cession ou renouvellement future
		Avenant avenantMigration = avenantRepository.findAvenantAvecMigrationActive(referenceContrat);
		Avenant avenantCession = avenantRepository.findAvenantAvecCessionActive(referenceContrat);
		Avenant avenantRenouvellement = avenantRepository.findAvenantAvecRenouvellementActive(referenceContrat);
		Contrat contratResilier = contratRepository.findContratResiliationFuture(referenceContrat);
		ElementContractuel elementContractuel =
				elementContractuelRepository.findECResiliationFuture(referenceContrat, numEC);
		List<ElementContractuel> elementContractuels =
				elementContractuelRepository.findListECResiliationFuture(referenceContrat);
		if (isMigrationFuture) {
			ContratValidator.checkIsActionPossible(referenceContrat, null, avenantCession, avenantRenouvellement,
					contratResilier, elementContractuel, numEC, elementContractuels, true, false);
		} else if (isCessionFuture) {
			ContratValidator.checkIsActionPossible(referenceContrat, avenantMigration, null, avenantRenouvellement,
					contratResilier, elementContractuel, numEC, elementContractuels, false, false);
		} else if (isResiliationFuture) {
			ContratValidator.checkIsActionPossible(referenceContrat, avenantMigration, avenantCession,
					avenantRenouvellement, null, elementContractuel, numEC, elementContractuels, false, false);
		} else if (isRenouvenouvellement) {
			ContratValidator.checkIsActionPossible(referenceContrat, avenantMigration, avenantCession, null,
					contratResilier, elementContractuel, numEC, elementContractuels, false, true);
		} else if (isResiliationFuturPartiel) {
			ContratValidator.checkIsActionPossible(referenceContrat, avenantMigration, avenantCession,
					avenantRenouvellement, contratResilier, null, numEC, null, false, false);
		} else if (isResiliationPartiel) {
			ContratValidator.checkIsActionPossible(referenceContrat, avenantMigration, avenantCession,
					avenantRenouvellement, contratResilier, elementContractuel, numEC, null, false, false);
		} else {
			ContratValidator.checkIsActionPossible(referenceContrat, avenantMigration, avenantCession,
					avenantRenouvellement, contratResilier, elementContractuel, numEC, elementContractuels,
					isMigration, false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resilierContrats(ContratResiliationtInfo contratResiliationtInfo) throws TopazeException {
		for (String referenceContrat : contratResiliationtInfo.getContrats()) {
			resilierContrat(referenceContrat, contratResiliationtInfo.getPolitiqueResiliation(),
					contratResiliationtInfo.getUser(), false, false, false, false, false, false);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> findContratsResiliationDifferee(String dateDuJour) {
		return contratRepository.findContratsResiliationDifferee(dateDuJour);

	}

	@Override
	public List<String> findReferenceContratsResiliationFuture(String dateDuJour) {
		return contratRepository.findReferenceContratsResiliationFuture(dateDuJour);
	}
}
