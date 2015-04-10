package com.nordnet.topaze.test.service.contrat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.contrat.business.PolitiqueResiliation;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.domain.StatutContrat;
import com.nordnet.topaze.contrat.domain.TypeResiliation;
import com.nordnet.topaze.contrat.service.ContratService;
import com.nordnet.topaze.contrat.service.ResiliationService;
import com.nordnet.topaze.contrat.test.GlobalTestCase;
import com.nordnet.topaze.exception.TopazeException;

/**
 * C'est la classe test {@link ContratService#resilierProduitParContratParNordNet(String, String)}.
 * 
 * @author akram-moncer
 * 
 */
public class ResiliationPartielEnCascadeParNordNetTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Log LOGGER = LogFactory.getLog(ResiliationPartielEnCascadeParNordNetTest.class);

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
	 * Gets the politique resiliation.
	 * 
	 * @return the politique resiliation
	 */
	private static PolitiqueResiliation getPolitiqueResiliation() {
		PolitiqueResiliation politiqueResiliation = new PolitiqueResiliation();
		politiqueResiliation.setCommentaire("COMMENT");
		// politiqueResiliation.setDateResiliation(Constants.DEFAULT_DATE_WITHOUT_TIME_FORMAT.format(new Date()));
		politiqueResiliation.setEnCascade(true);
		politiqueResiliation.setFraisResiliation(true);
		politiqueResiliation.setPenalite(true);
		politiqueResiliation.setRemboursement(false);
		politiqueResiliation.setTypeResiliation(TypeResiliation.RIN);
		return politiqueResiliation;

	}

	/**
	 * Ce test verifie la resiliation d'un sous contrat par le client.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/contrat-resiliation-partiel.xml" })
	public void testResiliationPartielParNordNetValid() {
		try {
			Contrat contrat = contratService.getContratByReference("REF_GB");
			assertEquals(contrat.getStatut(), StatutContrat.VALIDER);
			assertNull(contrat.getTypeResiliation());
			for (ElementContractuel c : contrat.getSousContrats()) {
				assertEquals(c.getStatut(), StatutContrat.VALIDER);
				assertNull(c.getTypeResiliation());
			}
			resiliationService.resiliationPartiel("REF_GB", 1, getPolitiqueResiliation(), "USER_TST", false, false);

			contrat = contratService.getContratByReference("REF_GB");

			for (ElementContractuel c : contrat.getSousContrats()) {
				if (c.getReferenceProduit().equals("PROD-01")
						|| (c.getElementContractuelParent() != null && c.getElementContractuelParent()
								.getReferenceProduit().equals("PROD-01"))) {
					assertEquals(c.getStatut(), StatutContrat.RESILIER);
					assertEquals(c.getTypeResiliation(), TypeResiliation.RIN);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie la resiliation d'un contrat qui n'existe pas par le client.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/contrat-resiliation-partiel.xml" })
	public void testResilierProduitParContratNOTExistParNordNet() {
		try {
			resiliationService.resiliationPartiel("REF", 1, getPolitiqueResiliation(), "USER_TST", false, false);

			fail("Unexpected exception");
		} catch (TopazeException e) {
			assertEquals(e.getErrorCode(), "0.1.2");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie la resiliation d'un contrat qui n'existe pas par le client.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/contrat-resiliation-partiel.xml" })
	public void testResilierProduitNOTExistParContratParNordNet() {
		try {
			resiliationService.resiliationPartiel("REF_GB", 2, getPolitiqueResiliation(), "USER_TST", false, false);

			fail("Unexpected exception");
		} catch (TopazeException e) {
			assertEquals(e.getErrorCode(), "1.1.71");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

}
