package com.nordnet.topaze.test.service.livraison;

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
import com.nordnet.topaze.livraison.test.GlobalTestCase;

/**
 * Classe de test de la methode {@link BonPreparationServiceImpl#marquerBonPreparationGlobalPreparer(String)}.
 * 
 * @author akram-moncer
 * 
 */
public class MarquerBPGlobalPrepareTest extends GlobalTestCase {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(MarquerBPGlobalPrepareTest.class);

	/**
	 * Le service utiliser pour les tests.
	 */
	@Autowired
	private BonPreparationService bonPreparationService;

	/**
	 * Test la method pour marquer un bon de preparation global comme prepare.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-bon-preparation.xml" })
	public void testMarquerBPGlobalPrepareValid() {

		try {
			bonPreparationService.marquerBPGlobalPrepare("REF_BP_GLOBAL_A_PREPARER");
			BonPreparation bonPreparation = bonPreparationService.findByReference("REF_BP_GLOBAL_A_PREPARER");
			assertNotNull(bonPreparation.getDatePreparation());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected Exception :" + e.getMessage());
		}
	}
}
