package com.nordnet.topaze.finder.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.finder.business.welcome.Reduction;
import com.nordnet.topaze.finder.business.welcome.ReductionUtils;
import com.nordnet.topaze.finder.business.welcome.Tarif;
import com.nordnet.topaze.finder.business.welcome.TypeValeurReduction;
import com.nordnet.topaze.finder.enums.TypeTVA;
import com.nordnet.topaze.finder.vat.client.VatClient;

/**
 * Useful methods to extract data from a result set.
 * 
 * @author Oussama Denden
 * 
 */
public class ResultSetUtils {

	/**
	 * Construction du reduction globale a partir de data set.
	 * 
	 * @param resultSet
	 *            result set.
	 * @return {@link Reduction}.
	 * @throws SQLException
	 *             {@link SQLException}.
	 */
	public static Reduction getReductionContrat(ResultSet resultSet) throws SQLException {
		Reduction reductionContrat = null;
		Calendar c = Calendar.getInstance();
		double reductionContratValeur = resultSet.getDouble("rcValeur");
		if (reductionContratValeur > Constants.ZERO) {
			reductionContrat = new Reduction();
			reductionContrat.setValeur(reductionContratValeur);
			reductionContrat.setDateAnnulation(resultSet.getDate("rcDateAnnulation", c));
			reductionContrat.setDateDebut(resultSet.getDate("rcDateDebut", c));
			reductionContrat.setDateFin(resultSet.getDate("rcDateFin", c));
			reductionContrat.setNbUtilisationEnCours(resultSet.getInt("rcNbUC"));
			reductionContrat.setNbUtilisationMax(resultSet.getInt("rcNbUM"));
			if (resultSet.wasNull()) {
				reductionContrat.setNbUtilisationMax(null);
			}
			String typeValeurStr = resultSet.getString("rcTypeValeur");
			reductionContrat.setTypeValeur(Utils.isStringNullOrEmpty(typeValeurStr) ? null : TypeValeurReduction
					.fromString(typeValeurStr));
		}
		return reductionContrat;
	}

	/**
	 * Construction du reduction d'un elementcontractuel a partir de data set.
	 * 
	 * @param resultSet
	 *            result set.
	 * @return {@link Reduction}.
	 * @throws SQLException
	 *             {@link SQLException}.
	 */
	public static Reduction getReductionEC(ResultSet resultSet) throws SQLException {
		Reduction reductionEC = null;
		Calendar c = Calendar.getInstance();
		double reductionECValeur = resultSet.getDouble("recValeur");
		if (reductionECValeur > Constants.ZERO) {
			reductionEC = new Reduction();
			reductionEC.setValeur(reductionECValeur);
			reductionEC.setDateAnnulation(resultSet.getDate("recDateAnnulation", c));
			reductionEC.setDateDebut(resultSet.getDate("recDateDebut", c));
			reductionEC.setDateFin(resultSet.getDate("recDateFin", c));
			reductionEC.setNbUtilisationEnCours(resultSet.getInt("recNbUC"));
			reductionEC.setNbUtilisationMax(resultSet.getInt("recNbUM"));
			if (resultSet.wasNull()) {
				reductionEC.setNbUtilisationMax(null);
			}
			String typeValeurStr = resultSet.getString("recTypeValeur");
			reductionEC.setTypeValeur(Utils.isStringNullOrEmpty(typeValeurStr) ? null : TypeValeurReduction
					.fromString(typeValeurStr));
		}
		return reductionEC;
	}

	/**
	 * Get liste des tarifs d'un contrat a partir de result set.
	 * 
	 * @param resultSet
	 *            result set.
	 * @return liste de {@link Tarif}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws SQLException
	 *             {@link SQLException}.
	 */
	public static List<Tarif> getTarifs(ResultSet resultSet) throws TopazeException, SQLException {
		List<Tarif> tarifs = new ArrayList<>();
		while (resultSet.next()) {
			tarifs.add(ResultSetUtils.getTarif(resultSet));
		}
		return tarifs;
	}

	/**
	 * Get tarif a partir de result set.
	 * 
	 * @param resultSet
	 *            result set.
	 * @return {@link Tarif}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws SQLException
	 *             {@link SQLException}.
	 */
	public static Tarif getTarif(ResultSet resultSet) throws TopazeException, SQLException {
		Tarif tarif = null;
		Double montant = resultSet.getDouble("montant");
		if (montant != null) {
			tarif = new Tarif();
			tarif.setMontantHT(montant);
			String typeTVAStr = resultSet.getString("ec.typeTVA");
			tarif.setTVA(Utils.isStringNullOrEmpty(typeTVAStr) ? null : TypeTVA.fromString(typeTVAStr));
			tarif.setMontantTTC(Utils.round(VatClient.appliquerTVA(tarif.getMontantHT(), tarif.getVat()),
					Constants.DEUX));
			tarif.setReferenceProduit(resultSet.getString("ec.referenceProduit"));
			tarif.setNumEC(resultSet.getInt("ec.numEC"));
			if (resultSet.wasNull()) {
				tarif.setNumEC(null);
			}
			tarif.setPeriodicite(resultSet.getInt("ec.periodicite"));
			if (resultSet.wasNull()) {
				tarif.setPeriodicite(null);
			}

			Reduction reductionContrat = ResultSetUtils.getReductionContrat(resultSet);

			Reduction reductionEC = ResultSetUtils.getReductionEC(resultSet);

			Double montantReduction =
					ReductionUtils.getMeilleurReduction(reductionContrat, reductionEC, tarif.getPeriodicite(),
							tarif.getMontantHT());
			if (montantReduction != null) {
				tarif.setMontantReductionHT(montantReduction);
				tarif.setMontantReductionTTC(Utils.round(VatClient.appliquerTVA(montantReduction, tarif.getVat()),
						Constants.DEUX));
			}
		}
		return tarif;
	}

	/**
	 * Get liste des modifications pour une renouvellement future..
	 * 
	 * @param resultSet
	 *            result set.
	 * @return liste des trames json des modifications.
	 * @throws SQLException
	 *             {@link SQLException}.
	 */
	public static List<String> getModifications(ResultSet resultSet) throws SQLException {
		List<String> modifications = new ArrayList<>();
		while (resultSet.next()) {
			modifications.add(resultSet.getString("md.trameJson"));
		}
		return modifications;
	}
}
