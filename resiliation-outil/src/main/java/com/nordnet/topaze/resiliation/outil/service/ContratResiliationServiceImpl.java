package com.nordnet.topaze.resiliation.outil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.resiliation.outil.business.ContratResiliationInfo;
import com.nordnet.topaze.resiliation.outil.business.PolitiqueResiliation;
import com.nordnet.topaze.resiliation.outil.business.ResiliationBillingInfo;
import com.nordnet.topaze.resiliation.outil.facturation.DernierBillingSimulator;
import com.nordnet.topaze.resiliation.outil.rest.RestClient;
import com.nordnet.topaze.resiliation.outil.retour.RetourSimulator;
import com.nordnet.topaze.resiliation.outil.validator.ResiliationOutilValidator;

/**
 * L'implementation de service {@link ContratResiliationService}.
 * 
 * @author Oussama Denden
 * 
 */
@Service("contratResiliationService")
public class ContratResiliationServiceImpl implements ContratResiliationService {

	/**
	 * {@link RestClient}.
	 */
	@Autowired
	private RestClient restClient;

	/**
	 * {@link DernierBillingSimulator}.
	 */
	@Autowired
	private DernierBillingSimulator dernierBillingSimulator;

	/**
	 * {@link RetourSimulator}.
	 */
	@Autowired
	private RetourSimulator retourSimulator;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContratResiliationInfo simulerResiliation(String referenceContrat, PolitiqueResiliation politiqueResiliation)
			throws TopazeException {

		/*
		 * Valide les donnees de simulation.
		 */
		ResiliationOutilValidator.validerSimulerResiliation(referenceContrat, politiqueResiliation);

		/*
		 * Cherche les informations utiles pour la simulation de resiliation global, a partir de la brique contrat.
		 */
		ResiliationBillingInfo[] resiliationBillingInfos = restClient.getResiliationBillingInfo(referenceContrat);

		return getContratResiliationInfo(resiliationBillingInfos, referenceContrat, politiqueResiliation, false);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContratResiliationInfo simulerResiliationPartiel(String referenceContrat, Integer numEC,
			PolitiqueResiliation politiqueResiliation) throws TopazeException {
		/*
		 * Valide les donnees de simulation.
		 */
		ResiliationOutilValidator.validerSimulerResiliationPartiel(referenceContrat, numEC, politiqueResiliation);

		/*
		 * Cherche les informations utiles pour la simulation de resiliation partiel, a partir de la brique contrat.
		 */
		ResiliationBillingInfo[] resiliationBillingInfos =
				restClient.getResiliationBillingInfo(referenceContrat, numEC);

		return getContratResiliationInfo(resiliationBillingInfos, referenceContrat, politiqueResiliation, true);
	}

	/**
	 * Genere le {@link ContratResiliationInfo}.
	 * 
	 * @param resiliationBillingInfos
	 *            {@link ResiliationBillingInfo}.
	 * @param referenceContrat
	 *            reference de contrat.
	 * @param politiqueResiliation
	 *            {@link PolitiqueResiliation}.
	 * @param isPartiel
	 *            true si resiliation partiel.
	 * @return {@link ContratResiliationInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	private ContratResiliationInfo getContratResiliationInfo(ResiliationBillingInfo[] resiliationBillingInfos,
			String referenceContrat, PolitiqueResiliation politiqueResiliation, boolean isPartiel)
			throws TopazeException {
		ContratResiliationInfo contratResiliationInfo = new ContratResiliationInfo();
		contratResiliationInfo.setReferenceContrat(referenceContrat);

		ResiliationOutilValidator.checkDateRemboursement(politiqueResiliation, resiliationBillingInfos);

		contratResiliationInfo =
				dernierBillingSimulator.calculeDernierBilling(contratResiliationInfo, resiliationBillingInfos,
						politiqueResiliation, isPartiel);

		contratResiliationInfo = retourSimulator.simulerRetour(contratResiliationInfo, resiliationBillingInfos);

		return contratResiliationInfo;
	}

}