package com.nordnet.topaze.businessprocess.test.service.mouvement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.facture.service.FactureService;
import com.nordnet.topaze.businessprocess.facture.test.GlobalTestCase;
import com.nordnet.topaze.businessprocess.facture.test.utils.ContratBillingInfoGenerator;
import com.nordnet.topaze.businessprocess.facture.util.Constants;
import com.nordnet.topaze.client.rest.business.facturation.ContratBillingInfo;
import com.nordnet.topaze.client.rest.enums.ModeFacturation;

/**
 * 
 * This class tests {@link FactureService#premierBilling(String)}.
 * 
 * @author anisselmane
 * 
 */
public class BillingRecurrentTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Log LOGGER = LogFactory.getLog(BillingRecurrentTest.class);

	/**
	 * 
	 * The service used for tests.
	 */
	@Autowired
	private FactureService factureService;

	/**
	 * This test verifies if we can create a {@link PremierBilling}.
	 * <p>
	 * Entering data :
	 * <ul>
	 * <p>
	 * </p>
	 * </ul>
	 * <p>
	 * Expected result :
	 * <ul>
	 * <li>Nothing</li>
	 * </ul>
	 */

	@SuppressWarnings("deprecation")
	@Test
	public void testBillingRecurrentValid() {
		try {
			System.setProperty(Constants.ENV_PROPERTY, "dev");
			ContratBillingInfo contrat =
					ContratBillingInfoGenerator.getContratBillingInformation("Ref_Contrat_1", null);

			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2014);
			cal.set(Calendar.MONTH, 11);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			Date date = cal.getTime();

			contrat.setDateDebutFacturation(date);
			assertEquals(contrat.getDateDebutFacturation(), date);

			assertEquals(contrat.getModeFacturation(), ModeFacturation.DATE_ANNIVERSAIRE);

			assertEquals(contrat.getDateDebutFacturation().getDay(), Constants.UN);

			assertEquals(contrat.getDateDerniereFacture(), null);

			Date dateDebutFacturation = contrat.getDateDebutFacturation();
			Calendar dateDebut = Calendar.getInstance();
			dateDebut.setTime(dateDebutFacturation);

			int nombreDateDebut = dateDebut.get(Calendar.DAY_OF_MONTH);

			int nombreJourMois = dateDebut.getActualMaximum(Calendar.DAY_OF_MONTH);

			// Nombre du jour de Consommation
			int nombreJourConsommation = (nombreJourMois - nombreDateDebut);
			System.out.println("nombre jour :" + nombreDateDebut);
			System.out.println("nombre jour du mois :" + nombreJourMois);
			System.out.println("nombre jour Consommation:" + nombreJourConsommation);
			System.out.println("Montant:" + contrat.getMontant());
			System.out.println("Periodicité" + contrat.getPeriodicite());

			Double montant = new Double(1000.0d);

			assertEquals(Double.doubleToLongBits(contrat.getMontant()), Double.doubleToLongBits(montant));
			assertEquals(contrat.getPeriodicite(), new Integer(12));

			Double prorata =
					nombreJourConsommation * ((contrat.getMontant() / contrat.getPeriodicite()) / nombreJourMois);
			contrat.setDateDerniereFacture(null);
			assertEquals(factureService.calculerBillingRecurrent(contrat), null);

			System.out.println("Prorata:" + prorata);
			System.out.println("Prorata 2:" + factureService.calculerBillingRecurrent(contrat));

			// assertEquals(factureService.billingRecurrent(contrat),prorata);

			System.out.println("la date de debut facturation: " + contrat.getDateDebutFacturation());
			System.out.println("reference Produit: " + contrat.getReferenceProduit());
			System.out.println("type contrat:" + contrat.getTypeContrat());

			factureService.calculerBillingRecurrent(ContratBillingInfoGenerator.getContratBillingInformation(
					"Ref_Contrat_1", null));
			factureService.calculerBillingRecurrent(ContratBillingInfoGenerator.getContratBillingInformation(
					"Ref_Contrat_2", null));

			factureService.calculerBillingRecurrent(ContratBillingInfoGenerator.getContratBillingInformation(
					"Ref_Contrat_1", null));
			factureService.calculerBillingRecurrent(ContratBillingInfoGenerator.getContratBillingInformation(
					"Ref_Contrat_2", ContratBillingInfoGenerator.getContratBillingInformation("Ref_Contrat_1", null)));

		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

}
