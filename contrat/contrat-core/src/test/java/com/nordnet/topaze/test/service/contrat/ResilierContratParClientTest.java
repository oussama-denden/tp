package com.nordnet.topaze.test.service.contrat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.contrat.business.PolitiqueResiliation;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.TypeResiliation;
import com.nordnet.topaze.contrat.repository.ContratRepository;
import com.nordnet.topaze.contrat.service.ContratService;
import com.nordnet.topaze.contrat.service.ResiliationService;
import com.nordnet.topaze.contrat.test.GlobalTestCase;
import com.nordnet.topaze.exception.TopazeException;

/**
 * C'est la classe test {@link ContratService#resilierContratParClient(String)}.
 * 
 * @author akram-moncer
 * 
 */
public class ResilierContratParClientTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Log LOGGER = LogFactory.getLog(ResilierContratParClientTest.class);

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
	 * contrat repository.
	 */
	@Autowired
	private ContratRepository contratRepository;

	/**
	 * Gets the politique resiliation.
	 * 
	 * @return the politique resiliation
	 */
	private static PolitiqueResiliation getPolitiqueResiliation() {
		PolitiqueResiliation politiqueResiliation = new PolitiqueResiliation();
		politiqueResiliation.setCommentaire("COMMENT");
		// politiqueResiliation.setDateResiliation(Constants.DEFAULT_DATE_WITHOUT_TIME_FORMAT.format(new Date()));
		politiqueResiliation.setTypeResiliation(TypeResiliation.RIC);
		politiqueResiliation.setFraisResiliation(true);
		politiqueResiliation.setPenalite(true);
		politiqueResiliation.setRemboursement(false);
		politiqueResiliation.setDelaiDeSecurite(false);
		return politiqueResiliation;

	}

	/**
	 * Ce test verifie la resiliation d'un contrat par le client.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/contrat-resiliation-partiel.xml" })
	public void testResilierContratParClientValid() {
		try {
			resiliationService.resilierContrat("REF_GB", getPolitiqueResiliation(), "USER_TST", false, false, false,
					false, false, false);

			Contrat contrat = contratRepository.findByReference("REF_GB");

			assertEquals(contrat.getTypeResiliation(), TypeResiliation.RIC);
			assertEquals(contrat.isResilier(), true);

		}

		catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie la resiliation dans le cas ou le contrat n'existe pas.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/contrat-resiliation-partiel.xml" })
	public void testResilierContratNOTExistParClient() {
		try {
			resiliationService.resilierContrat("REF_NOT", getPolitiqueResiliation(), "USER_TST", false, false, false,
					false, false, false);
			fail("Unexpected exception");
		} catch (TopazeException e) {
			assertEquals(e.getErrorCode(), "0.1.2");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}
}
