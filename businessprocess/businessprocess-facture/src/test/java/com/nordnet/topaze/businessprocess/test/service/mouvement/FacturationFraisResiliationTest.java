package com.nordnet.topaze.businessprocess.test.service.mouvement;

import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.facture.calcule.CalculateurFrais;
import com.nordnet.topaze.businessprocess.facture.service.FactureService;
import com.nordnet.topaze.businessprocess.facture.test.GlobalTestCase;
import com.nordnet.topaze.businessprocess.facture.test.utils.ContratBillingInfoGenerator;
import com.nordnet.topaze.businessprocess.facture.util.Constants;

/**
 * 
 * This class tests {@link FactureService#premierBilling(String)}.
 * 
 * @author anisselmane
 * 
 */
public class FacturationFraisResiliationTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Log LOGGER = LogFactory.getLog(FacturationFraisResiliationTest.class);

	/**
	 * The service used for tests.
	 */
	@Autowired
	private CalculateurFrais calculateurFrais;

	/**
	 * This test verifies if we can create a {@link FraisResiliation}.
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
	public void testFacturationFraisResiliationValid() {
		try {
			System.setProperty(Constants.ENV_PROPERTY, "dev");
			calculateurFrais.fraisResiliation(ContratBillingInfoGenerator.getContratBillingInformation("REF_C_A_PM_M",
					ContratBillingInfoGenerator.getContratBillingInformation("REF_C_A_PM_M", null)));

		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

}
