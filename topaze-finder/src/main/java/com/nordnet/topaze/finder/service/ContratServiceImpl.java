package com.nordnet.topaze.finder.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.finder.business.espaceclient.EspaceClientAbonnementInfo;
import com.nordnet.topaze.finder.business.welcome.Tarif;
import com.nordnet.topaze.finder.business.welcome.WelcomeAbonnementInfo;
import com.nordnet.topaze.finder.business.welcome.WelcomeContratInfo;
import com.nordnet.topaze.finder.dao.ContratDao;

/**
 * L'implementation de service {@link ContratService}.
 * 
 * @author anisselmane.
 * 
 */
@Service("contratService")
public class ContratServiceImpl implements ContratService {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(ContratServiceImpl.class);

	/**
	 * contrat DAO.
	 */
	@Autowired
	ContratDao contratDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<WelcomeAbonnementInfo> getWelcomeAbonnementsClient(String idClient) throws TopazeException {
		LOGGER.info("Entrer methode getWelcomeAbonnementsClient");
		Date d1 = new Date();
		List<WelcomeAbonnementInfo> abonnementsClient = contratDao.getWelcomeAbonnementsClient(idClient);
		Date d2 = new Date();
		LOGGER.info("Find All Abonnements Client in " + (d2.getTime() - d1.getTime()) + "ms");
		LOGGER.info("Fin methode getWelcomeAbonnementsClient");
		return abonnementsClient;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<EspaceClientAbonnementInfo> getEspaceClientAbonnementsClient(String idClient) throws TopazeException {
		LOGGER.info("Entrer methode getEspaceClientAbonnementsClient");
		Date d1 = new Date();
		List<EspaceClientAbonnementInfo> espaceClientabonnementsinfo =
				contratDao.getEspaceClientAbonnementsClient(idClient);
		Date d2 = new Date();
		LOGGER.info("Find All Abonnements Client in " + (d2.getTime() - d1.getTime()) + "ms");
		LOGGER.info("Fin methode getEspaceClientAbonnementsClient");
		return espaceClientabonnementsinfo;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WelcomeContratInfo getWelcomeContratInfo(String refContrat) throws TopazeException {
		LOGGER.info("Entrer methode getWelcomeContratInfo");
		Date d1 = new Date();
		WelcomeContratInfo contratInfo = contratDao.getWelcomeContratInfo(refContrat);
		Date d2 = new Date();
		LOGGER.info("GET WELCOME CONTRAT INFO in " + (d2.getTime() - d1.getTime()) + "ms");
		LOGGER.info("Fin methode getWelcomeContratInfo");
		return contratInfo;
	}

	@Override
	public List<Tarif> getTarifs(String refContrat) throws TopazeException {
		LOGGER.info("Entrer methode getTarifs");
		Date d1 = new Date();
		List<Tarif> tarifs = contratDao.getTarifs(refContrat);
		Date d2 = new Date();
		LOGGER.info("GET TARIFS in " + (d2.getTime() - d1.getTime()) + "ms");
		LOGGER.info("Fin methode getTarifs");
		return tarifs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRPID(String refContrat) throws TopazeException {
		LOGGER.info("Entrer methode getRPID");
		Date d1 = new Date();
		String rpid = contratDao.getRPID(refContrat);
		Date d2 = new Date();
		LOGGER.info("Find RPID in " + (d2.getTime() - d1.getTime()) + "ms");
		LOGGER.info("Fin methode getRPID");
		return rpid;
	}

}