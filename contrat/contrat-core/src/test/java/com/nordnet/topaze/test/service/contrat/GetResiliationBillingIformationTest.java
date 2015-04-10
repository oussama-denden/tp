package com.nordnet.topaze.test.service.contrat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.contrat.business.ResiliationBillingInfo;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.service.ContratService;
import com.nordnet.topaze.contrat.service.ResiliationService;
import com.nordnet.topaze.contrat.test.GlobalTestCase;

/**
 * 
 * @author Oussama Denden
 * 
 */
public class GetResiliationBillingIformationTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Log LOGGER = LogFactory.getLog(ContratPreparationTest.class);

	/**
	 * The service used for tests.
	 */
	@Autowired
	private ContratService contratService;

	/**
	 * The service used for tests.
	 */
	@Autowired
	private ResiliationService resiliationService;

	/**
	 * This test verifies if we can prepare a {@link ElementContractuel}.
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
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/get-resiliation-billing-information.xml" })
	public void testGetResiliationBillingIformationValid() {
		try {

			Contrat contrat = contratService.getContratByReference("REF_GB_REISL");

			assertEquals(contrat.getSousContrats().size(), 3);
			List<ResiliationBillingInfo> infos = resiliationService.getResiliationBillingIformation("REF_GB_REISL");
			// ResiliationBillingInfo ne concerne pas les contrats de vente.
			assertEquals(infos.size(), 2);

			for (ElementContractuel elementContractuel : contrat.getSousContrats()) {
				for (ResiliationBillingInfo resiliationBillingInfo : infos) {
					if (resiliationBillingInfo.getReferenceProduit().equals(elementContractuel.getReferenceProduit())) {
						assertEquals(resiliationBillingInfo.getDureeContrat(), elementContractuel.getDuree());
						assertEquals(resiliationBillingInfo.getEngagement(), elementContractuel.getEngagement());
						assertEquals(resiliationBillingInfo.getModeFacturation(),
								elementContractuel.getModeFacturation());
						assertEquals(resiliationBillingInfo.getMontant(), elementContractuel.getMontant());
						assertEquals(resiliationBillingInfo.getPeriodicite(), elementContractuel.getPeriodicite());

					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}
}
