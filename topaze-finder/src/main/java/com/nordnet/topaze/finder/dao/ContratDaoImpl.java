package com.nordnet.topaze.finder.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.finder.business.ProduitRenouvellement;
import com.nordnet.topaze.finder.business.espaceclient.EspaceClientAbonnementInfo;
import com.nordnet.topaze.finder.business.welcome.Reduction;
import com.nordnet.topaze.finder.business.welcome.ReductionUtils;
import com.nordnet.topaze.finder.business.welcome.Tarif;
import com.nordnet.topaze.finder.business.welcome.WelcomeAbonnementInfo;
import com.nordnet.topaze.finder.business.welcome.WelcomeContratInfo;
import com.nordnet.topaze.finder.enums.ModePaiement;
import com.nordnet.topaze.finder.enums.TypeTVA;
import com.nordnet.topaze.finder.util.Constants;
import com.nordnet.topaze.finder.util.PropertiesUtil;
import com.nordnet.topaze.finder.util.ResultSetUtils;
import com.nordnet.topaze.finder.util.Utils;

/**
 * 
 * @author anisselmane.
 * 
 */
@Repository("contratDao")
public class ContratDaoImpl implements ContratDao {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(ContratDaoImpl.class);

	/**
	 * Data source.
	 */
	@Autowired
	DataSource dataSource;

	/**
	 * Rest URL properties file.
	 */
	@Autowired
	private Properties sqlQueryProperties;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<WelcomeAbonnementInfo> getWelcomeAbonnementsClient(String idClient) throws TopazeException {

		List<WelcomeAbonnementInfo> abonnementsClient = null;

		String sql = String.format(sqlQueryProperties.getProperty(Constants.FIND_ABONNEMENTS_CLIENT), idClient);
		try (Connection connection = dataSource.getConnection();
				Statement stmt =
						connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

			ResultSet res = stmt.executeQuery(sql);
			abonnementsClient = getWelcomeAbonnementsClientFromResultSet(res);

			return abonnementsClient;
		} catch (SQLException e) {
			LOGGER.error("Finder :Erreur lors de la recuperation des abonnements client", e);
			throw new TopazeException("Finder :Erreur lors de la recuperationdes abonnements client",
					e.getLocalizedMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<EspaceClientAbonnementInfo> getEspaceClientAbonnementsClient(String idClient) throws TopazeException {
		List<EspaceClientAbonnementInfo> espaceClientAbonnementsInfo = null;

		String sql = String.format(sqlQueryProperties.getProperty(Constants.FIND_ABONNEMENTS_CLIENT), idClient);

		try (Connection connection = dataSource.getConnection();
				Statement stmt =
						connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
			ResultSet res = stmt.executeQuery(sql);
			espaceClientAbonnementsInfo = getEspaceClientAbonnementsClientFromResultSet(res);

			return espaceClientAbonnementsInfo;
		} catch (SQLException e) {
			LOGGER.error("Finder :Erreur lors de la recuperation des abonnements client", e);
			throw new TopazeException("Finder :Erreur lors de la recuperationdes abonnements client",
					e.getLocalizedMessage());
		}
	}

	/**
	 * Creer la liste des {@link WelcomeAbonnementInfo} a partir du resultat de requette.
	 * 
	 * @param resultSet
	 *            {@link ResultSet}.
	 * @return liste de {@link WelcomeAbonnementInfo}.
	 * @throws SQLException
	 *             {@link SQLException}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	private List<EspaceClientAbonnementInfo> getEspaceClientAbonnementsClientFromResultSet(ResultSet resultSet)
			throws SQLException, TopazeException {
		List<EspaceClientAbonnementInfo> espaceClientAbonnementsInfo = new ArrayList<>();
		Calendar c = Calendar.getInstance();
		while (resultSet.next()) {
			EspaceClientAbonnementInfo espaceClientAbonnementInfo = new EspaceClientAbonnementInfo();
			espaceClientAbonnementInfo.setReference(resultSet.getString("c.reference"));
			espaceClientAbonnementInfo.setLabel(resultSet.getString("ec.titre"));
			espaceClientAbonnementInfo.setReferenceProduit(resultSet.getString("ec.referenceProduit"));
			espaceClientAbonnementInfo.setFrequence(resultSet.getInt("ec.periodicite"));
			if (resultSet.wasNull()) {
				espaceClientAbonnementInfo.setFrequence(null);
			}
			String modePaiementStr = resultSet.getString("ec.modePaiement");
			espaceClientAbonnementInfo.setModePaiement(Utils.isStringNullOrEmpty(modePaiementStr) ? null : ModePaiement
					.fromString(modePaiementStr));
			espaceClientAbonnementInfo.setDateCreation(resultSet.getDate("c.datePreparation", c));
			espaceClientAbonnementInfo.setDateDebutFacturation(resultSet.getDate("c.dateDebutFacturation", c));

			Date dateFinDuree = resultSet.getDate("ecd.dateFinDuree", c);
			Integer avenantId = resultSet.getInt("av.idAvenant");
			if (dateFinDuree != null) {
				if (avenantId != null && avenantId > 0) {
					espaceClientAbonnementInfo.setDateFin(getDateFin(avenantId, dateFinDuree));
				} else {
					espaceClientAbonnementInfo.setDateFin(dateFinDuree);
				}
			}

			espaceClientAbonnementInfo.setDuree(resultSet.getInt("ecd.duree"));
			if (resultSet.wasNull()) {
				espaceClientAbonnementInfo.setDuree(null);
			}
			espaceClientAbonnementInfo.setDateActionResiliation(resultSet.getDate("pr.dateResiliation"));
			espaceClientAbonnementInfo.setDateAnnulationResiliation(resultSet.getDate("pr.dateAnnulation"));
			espaceClientAbonnementInfo.setRemboursementResiliation(resultSet.getBoolean("pr.remboursement"));
			espaceClientAbonnementInfo.setDateActionMigration(resultSet.getDate("apm.dateAction"));

			espaceClientAbonnementsInfo.add(espaceClientAbonnementInfo);
		}
		return espaceClientAbonnementsInfo;
	}

	/**
	 * Creer la liste des {@link WelcomeAbonnementInfo} a partir du resultat de requette.
	 * 
	 * @param resultSet
	 *            {@link ResultSet}.
	 * @return liste de {@link WelcomeAbonnementInfo}.
	 * @throws SQLException
	 *             {@link SQLException}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	private List<WelcomeAbonnementInfo> getWelcomeAbonnementsClientFromResultSet(ResultSet resultSet)
			throws SQLException, TopazeException {
		List<WelcomeAbonnementInfo> abonnementsClient = new ArrayList<>();
		Calendar c = Calendar.getInstance();
		while (resultSet.next()) {
			WelcomeAbonnementInfo abonnementInfo = new WelcomeAbonnementInfo();
			abonnementInfo.setReference(resultSet.getString("c.reference"));
			abonnementInfo.setLabel(resultSet.getString("ec.titre"));
			abonnementInfo.setReferenceProduit(resultSet.getString("ec.referenceProduit"));
			abonnementInfo.setDateCreation(resultSet.getDate("c.datePreparation", c));
			abonnementInfo.setDateDebutFacturation(resultSet.getDate("c.dateDebutFacturation", c));

			Date dateFinDuree = resultSet.getDate("ecd.dateFinDuree", c);
			Integer avenantId = resultSet.getInt("av.idAvenant");
			if (dateFinDuree != null) {
				if (avenantId != null && avenantId > 0) {
					abonnementInfo.setDateFin(getDateFin(avenantId, dateFinDuree));
				} else {
					abonnementInfo.setDateFin(dateFinDuree);
				}
			}

			abonnementInfo.setDuree(resultSet.getInt("ecd.duree"));
			if (resultSet.wasNull()) {
				abonnementInfo.setDuree(null);
			}
			abonnementInfo.setDateActionResiliation(resultSet.getDate("pr.dateResiliation"));
			abonnementInfo.setDateAnnulationResiliation(resultSet.getDate("pr.dateAnnulation"));
			abonnementInfo.setRemboursementResiliation(resultSet.getBoolean("pr.remboursement"));
			abonnementInfo.setDateActionMigration(resultSet.getDate("apm.dateAction"));

			abonnementsClient.add(abonnementInfo);
		}
		return abonnementsClient;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WelcomeContratInfo getWelcomeContratInfo(String refContrat) throws TopazeException {

		String sql = String.format(sqlQueryProperties.getProperty(Constants.WELCOME_CONTRAT_INFO), refContrat);

		try (Connection connection = dataSource.getConnection();
				Statement stmt =
						connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
			ResultSet res = stmt.executeQuery(sql);
			WelcomeContratInfo welcomeContratInfo = getWelcomeContratInfo(res);
			if (welcomeContratInfo == null) {
				throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("4.1.2", refContrat), "4.1.2");
			}
			List<Tarif> tarifs = getTarifs(refContrat);
			welcomeContratInfo.setTarifs(tarifs);
			return welcomeContratInfo;
		} catch (SQLException e) {
			LOGGER.error("Finder :Erreur lors de la recuperation des abonnements client", e);
			throw new TopazeException("Finder :Erreur lors de la recuperationdes abonnements client",
					e.getLocalizedMessage());
		}
	}

	/**
	 * Creer le {@link WelcomeContratInfo} a partir du resultat de requette.
	 * 
	 * @param resultSet
	 *            {@link ResultSet}.
	 * @return {@link WelcomeContratInfo}.
	 * @throws SQLException
	 *             {@link SQLException}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	private WelcomeContratInfo getWelcomeContratInfo(ResultSet resultSet) throws SQLException, TopazeException {
		WelcomeContratInfo welcomeContratInfo = null;
		Reduction reductionContrat = null;
		Reduction reductionEC = null;
		Calendar c = Calendar.getInstance();
		c.setTime(PropertiesUtil.getInstance().getDateDuJour().toDate());
		if (resultSet.next()) {
			welcomeContratInfo = new WelcomeContratInfo();
			String reference = resultSet.getString("c.reference");
			if (Utils.isStringNullOrEmpty(reference)) {
				return null;
			}
			welcomeContratInfo.setNumAbonnement(reference);
			welcomeContratInfo.setNumeroCommande(resultSet.getString("ec.numeroCommande"));
			welcomeContratInfo.setOffre(resultSet.getString("ec.referenceProduit"));
			welcomeContratInfo.setLabel(resultSet.getString("ec.titre"));
			welcomeContratInfo.setIdClient(resultSet.getString("c.idClient"));
			welcomeContratInfo.setDateCreation(resultSet.getDate("c.datePreparation", c));
			welcomeContratInfo.setDateDebutFacturation(resultSet.getDate("c.dateDebutFacturation", c));
			welcomeContratInfo.setDateFin(resultSet.getDate("c.dateFinContrat", c));
			welcomeContratInfo.setDateActionResiliation(resultSet.getDate("pr.dateResiliation", c));
			welcomeContratInfo.setDateAnnulationResiliation(resultSet.getDate("pr.dateAnnulation", c));
			welcomeContratInfo.setDateActionMigration(resultSet.getDate("apm.dateAction", c));
			welcomeContratInfo.setDateFinEngagement(resultSet.getDate("ec.dateFinEngagement", c));
			welcomeContratInfo.setPeriodicite(resultSet.getInt("ec.periodicite"));
			if (resultSet.wasNull()) {
				welcomeContratInfo.setPeriodicite(null);
			}
			welcomeContratInfo.setDuree(resultSet.getInt("ecd.duree"));
			if (resultSet.wasNull()) {
				welcomeContratInfo.setDuree(null);
			}
			String modePaiementStr = resultSet.getString("ec.modePaiement");
			welcomeContratInfo.setTypePaiement(Utils.isStringNullOrEmpty(modePaiementStr) ? null : ModePaiement
					.fromString(modePaiementStr));
			welcomeContratInfo.setMontantTarif(resultSet.getDouble("montantTarif"));

			String typeTVAStr = resultSet.getString("ec.typeTVA");
			welcomeContratInfo.setTVA(Utils.isStringNullOrEmpty(typeTVAStr) ? null : TypeTVA.fromString(typeTVAStr));
			welcomeContratInfo.setFrais(false);
			welcomeContratInfo.setRemboursementResiliation(resultSet.getBoolean("pr.remboursement"));
			boolean isResiliation = resultSet.getBoolean("pr.remboursement");
			Double montantResiliation = resultSet.getDouble("pr.montantResiliation");
			Double montantFraisResiliation = resultSet.getDouble("montantFrais");
			if (isResiliation && montantResiliation <= Constants.ZERO && montantFraisResiliation != null) {
				welcomeContratInfo.setFrais(true);
			}

			reductionContrat = ResultSetUtils.getReductionContrat(resultSet);

			reductionEC = ResultSetUtils.getReductionEC(resultSet);

			Double montantReduction =
					ReductionUtils.getMeilleurReduction(reductionContrat, reductionEC,
							welcomeContratInfo.getPeriodicite(), welcomeContratInfo.getMontantTarif());
			welcomeContratInfo.setMontantReduction(montantReduction);

			Date dateFinDuree = resultSet.getDate("ecd.dateFinDuree", c);
			Integer avenantId = resultSet.getInt("av.idAvenant");
			if (dateFinDuree == null) {
				LocalDate dateDerniereFacture = new LocalDate(resultSet.getDate("ec.dateDerniereFacture", c));
				welcomeContratInfo.setDateRenouvellement(dateDerniereFacture.plusMonths(
						welcomeContratInfo.getPeriodicite()).toDate());

			} else if (avenantId != null && avenantId > 0) {
				welcomeContratInfo.setDateFin(getDateFin(avenantId, dateFinDuree));
				welcomeContratInfo.setDateRenouvellement(dateFinDuree);
			} else {
				welcomeContratInfo.setDateFin(dateFinDuree);
			}

			if (resultSet.next()) {
				int rowCount = Constants.DEUX;
				if (resultSet.last()) {
					rowCount = resultSet.getRow();
				}
				throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.5", Constants.UN, rowCount),
						"0.5");
			}

		}
		return welcomeContratInfo;
	}

	/**
	 * Determiner la date fin contrat.
	 * 
	 * @param avenantId
	 *            id de l'avenant.
	 * @param dateFinActuel
	 *            date fin contrat actuel.
	 * @return date fin contrat.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	private Date getDateFin(Integer avenantId, Date dateFinActuel) throws TopazeException {
		LocalDate dateFin = new LocalDate(dateFinActuel);
		List<String> modifications = getModifications(avenantId);
		for (String modification : modifications) {
			ProduitRenouvellement produitRenouvellement;
			try {
				produitRenouvellement = ProduitRenouvellement.fromJsonToProduitRenouvellement(modification);
				if (produitRenouvellement.isPaiementRecurrent()) {
					return null;
				} else if (produitRenouvellement.isPaiementComptant()) {
					return dateFin.plusMonths(produitRenouvellement.getPrix().getDuree()).toDate();
				} else {
					return dateFinActuel;
				}
			} catch (IOException e) {
				throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.6"), "0.6");
			}

		}
		return dateFinActuel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Tarif> getTarifs(String refContrat) throws TopazeException {
		String sql = String.format(sqlQueryProperties.getProperty(Constants.GET_TARIFS), refContrat);

		try (Connection connection = dataSource.getConnection();
				Statement stmt =
						connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
			ResultSet res = stmt.executeQuery(sql);
			List<Tarif> tarifs = ResultSetUtils.getTarifs(res);

			return tarifs;
		} catch (SQLException e) {
			LOGGER.error("Finder :Erreur lors de la recuperation de bilan de reduction", e);
			throw new TopazeException("Finder :Erreur lors de la recuperationdes de bilan de reduction",
					e.getLocalizedMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getModifications(Integer idAvenant) throws TopazeException {
		String sql = String.format(sqlQueryProperties.getProperty(Constants.GET_MODIFICATIONS), idAvenant);

		try (Connection connection = dataSource.getConnection();
				Statement stmt =
						connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
			ResultSet res = stmt.executeQuery(sql);
			List<String> modifications = ResultSetUtils.getModifications(res);

			return modifications;
		} catch (SQLException e) {
			LOGGER.error(
					"Finder :Erreur lors de la recuperation de la liste des modifications pour une renouvellement future.",
					e);
			throw new TopazeException(
					"Finder :Erreur lors de la recuperationdes de la liste des modifications pour une renouvellement future.",
					e.getLocalizedMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRPID(String refContrat) throws TopazeException {
		String sql = String.format(sqlQueryProperties.getProperty(Constants.GET_RPID), refContrat);

		try (Connection connection = dataSource.getConnection();
				Statement stmt =
						connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
			ResultSet resultSet = stmt.executeQuery(sql);
			String rpid = null;
			if (resultSet.next()) {
				rpid = resultSet.getString("el.retailerPackagerId");
			}
			return rpid;
		} catch (SQLException e) {
			LOGGER.error("Finder :Erreur lors de la recuperation de RPID", e);
			throw new TopazeException("Finder :Erreur lors de RPID", e.getLocalizedMessage());
		}
	}

}