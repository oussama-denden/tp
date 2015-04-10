package com.nordnet.topaze.test.service.livraison;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.service.BonPreparationService;
import com.nordnet.topaze.livraison.core.service.BonPreparationServiceImpl;
import com.nordnet.topaze.livraison.core.util.Constants;
import com.nordnet.topaze.livraison.test.GlobalTestCase;

/**
 * Classe de test de la methode {@link BonPreparationServiceImpl#initierBP(BonPreparation)}.
 * 
 * @author akram-moncer
 * 
 */
public class InitierBPTest extends GlobalTestCase {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(InitierBPTest.class);

	/**
	 * Le service utiliser pour les tests.
	 */
	@Autowired
	private BonPreparationService bonPreparationService;

	/**
	 * Cette methode test le cas de l'initiation d'un bon de preparation reçue de la business process.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-initier-bon-preparation.xml" })
	public void testInitierBPValid() {
		try {
			System.setProperty(Constants.ENV_PROPERTY, "dev");
			BonPreparation bonPreparation = bonPreparationService.findByReference("REF_CGB");
			assertEquals(bonPreparation.getReference(), "REF_CGB");

			bonPreparationService.initierBP(bonPreparation);
			assertNotNull(bonPreparation.getDateInitiation());
			assertEquals(Constants.DEFAULT_DATE_FORMAT.format(bonPreparation.getDateInitiation()),
					Constants.DEFAULT_DATE_FORMAT.format(bonPreparation.getDateInitiation()));

		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected Exception :" + e.getMessage());
		}
	}
}
