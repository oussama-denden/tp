package com.nordnet.topaze.businessprocess.test.service.mouvement;

import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.facture.service.FactureService;
import com.nordnet.topaze.businessprocess.facture.test.GlobalTestCase;
import com.nordnet.topaze.businessprocess.facture.test.utils.ContratBillingInfoGenerator;

/**
 * 
 * This class tests {@link FactureService#premierBilling(String)}.
 * 
 * @author anisselmane
 * 
 */
public class PremierBillingTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Log LOGGER = LogFactory.getLog(PremierBillingTest.class);

	/**
	 * Separateur de l'arborescence.
	 */
	@SuppressWarnings("unused")
	private final String separator = System.setProperty("separator", "/");

	/**
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

	@Test
	public void testPremierBillingValid() {
		try {
			System.setProperty("env", "dev");

			factureService.calculerPremierBilling(
					ContratBillingInfoGenerator.getContratBillingInformation("Ref_Contrat_1", null), false);
			factureService.calculerPremierBilling(ContratBillingInfoGenerator.getContratBillingInformation(
					"Ref_Contrat_2", ContratBillingInfoGenerator.getContratBillingInformation("Ref_Contrat_1", null)),
					false);

			factureService.calculerPremierBilling(ContratBillingInfoGenerator.getContratBillingInformation(
					"Ref_Contrat_3", ContratBillingInfoGenerator.getContratBillingInformation("Ref_Contrat_1", null)),
					false);
			factureService.calculerPremierBilling(ContratBillingInfoGenerator.getContratBillingInformation(
					"Ref_Contrat_4", ContratBillingInfoGenerator.getContratBillingInformation("Ref_Contrat_1", null)),
					false);
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

}
