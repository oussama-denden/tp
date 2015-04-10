package com.nordnet.topaze.businessprocess.netretour.service;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.topaze.businessprocess.netretour.mock.WebServiceNetRetourMock;
import com.nordnet.topaze.businessprocess.netretour.util.Constants;
import com.nordnet.topaze.businessprocess.netretour.util.spring.ApplicationContextHolder;
import com.nordnet.topaze.client.rest.RestClientNetRetour;
import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.client.rest.enums.OutilLivraison;
import com.nordnet.topaze.client.rest.enums.TypeBonPreparation;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.logger.service.TracageService;

/**
 * implementation du {@link NetRetourService}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Service("netRetourService")
public class NetRetourServiceImpl implements NetRetourService {

	/**
	 * default logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(NetRetourServiceImpl.class.getName());

	/**
	 * Alert service.
	 */
	SendAlert sendAlert;

	/**
	 * {@link TracageService}.
	 */
	private TracageService tracageService;

	/**
	 * {@link RestClientNetRetour}.
	 */
	@Autowired
	private RestClientNetRetour restClientNetRetour;

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
	 * 
	 * @throws TopazeException
	 */
	@Override
	public void traductionNetRetour(BonPreparation bonPreparation) throws TopazeException {

		// tracer l'opération de résiliation global
		getTracageService()
				.ajouterTrace(Constants.PRODUCT, bonPreparation.getReference(),
						"Retour du contrat " + bonPreparation.getReference() + " – Demande de retour ",
						Constants.INTERNAL_USER);

		WebServiceNetRetourMock netRetourMock = new WebServiceNetRetourMock();
		ArrayList<String> equipmentsList = new ArrayList<>();
		if (bonPreparation.getElementLivraisons() != null && bonPreparation.getElementLivraisons().size() > 0) {
			// traitement de cas du Bon de preparation global.
			for (ElementLivraison elementLivraison : bonPreparation.getElementLivraisons()) {
				if (elementLivraison.getTypeBonPreparation().equals(TypeBonPreparation.RETOUR)
						&& elementLivraison.getActeur().equals(OutilLivraison.NETRETOUR)) {
					equipmentsList.add(elementLivraison.getReferenceProduit());
				}
			}
			String[] l = new String[equipmentsList.size()];
			if (equipmentsList.size() > 0) {
				LOGGER.info("Traduction vers NetRetour");
				netRetourMock.createBienRetourRequest(bonPreparation.getIdClient(), equipmentsList.toArray(l));
			}
		}
	}

	@Override
	public Boolean checkBienRecupere(ElementLivraison elementLivraison) throws TopazeException {
		LOGGER.info("Entrer BonPreparationServiceImpl.checkBienRecupere méthode.");
		boolean isBienRecupere = false;
		// tracer l'opération de résiliation global
		getTracageService().ajouterTrace(
				Constants.PRODUCT,
				elementLivraison.getReference().split("-")[0],
				"Retour du contrat " + elementLivraison.getReference() + " – Demande de retour du produit "
						+ elementLivraison.getReferenceProduit(), Constants.INTERNAL_USER);
		try {
			WebServiceNetRetourMock netRetourMock = new WebServiceNetRetourMock();
			isBienRecupere = netRetourMock.checkBienRecupere(elementLivraison.getCodeColis());
		} catch (Exception e) {
			try {
				getSendAlert().send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
						"error occurs during call of " + "NetRetourServiceImpl.checkBienRecupere()", e.getMessage());
			} catch (Exception ex) {
				LOGGER.error("fail to send alert", ex);
				throw new TopazeException("fail to send alert", ex);
			}
			throw new TopazeException("error pendant l'appel de NetRetourServiceImpl.checkBienRecupere()", e);
		}
		return isBienRecupere;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activerBienRenouvellement(BonPreparation bonRenouvellement) throws TopazeException {

		for (ElementLivraison elementLivraison : bonRenouvellement.getElementLivraisons()) {
			if (elementLivraison.getActeur() == OutilLivraison.NETDELIVERY) {
				restClientNetRetour.marquerELRenouvele(elementLivraison);
			}
		}
	}

	/**
	 * @param restClientNetRetour
	 *            {@link RestClientNetRetour}.
	 */
	public void setRestClientNetRetour(RestClientNetRetour restClientNetRetour) {
		this.restClientNetRetour = restClientNetRetour;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void traductionNetRetour(String referenceBP, TypeBonPreparation typeBonPreparation) throws TopazeException {
		BonPreparation bonPreparation = null;
		if (typeBonPreparation.equals(TypeBonPreparation.MIGRATION)) {
			bonPreparation = restClientNetRetour.getBonMigration(referenceBP);
		} else if (typeBonPreparation.equals(TypeBonPreparation.RETOUR)) {
			bonPreparation = restClientNetRetour.getBonRecuperation(referenceBP);
		}
		// marker un BR preparer on cas ou on fait une traduction Netretour.
		traductionNetRetour(bonPreparation);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activerBienRenouvellement(String referenceBP) throws TopazeException {
		// recuperation de bon de renouvellement
		BonPreparation bonRecuperation = restClientNetRetour.getBonPreparation(referenceBP);

		// marker un BRE preparer on cas ou on fait une traduction Netretour.
		activerBienRenouvellement(bonRecuperation);

	}

	/**
	 * Get send alert.
	 * 
	 * @return {@link #sendAlert}
	 */
	private SendAlert getSendAlert() {
		if (this.sendAlert == null) {
			if (System.getProperty("alert.useMock").equals("true")) {
				sendAlert = (SendAlert) ApplicationContextHolder.getBean("sendAlertMock");
			} else {
				sendAlert = (SendAlert) ApplicationContextHolder.getBean("sendAlert");
			}
		}
		return sendAlert;
	}

}