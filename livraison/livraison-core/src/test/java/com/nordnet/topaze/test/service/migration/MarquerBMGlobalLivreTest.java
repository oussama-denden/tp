package com.nordnet.topaze.test.service.migration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.service.BonMigrationService;
import com.nordnet.topaze.livraison.test.GlobalTestCase;

/**
 * This class tests {@link BonMigrationServiceImpl#marquerBMGlobalLivre(BonPreparation))}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class MarquerBMGlobalLivreTest extends GlobalTestCase {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(MarquerBMGlobalLivreTest.class);

	/**
	 * Le service utiliser pour les tests.
	 */
	@Autowired
	private BonMigrationService bonMigrationService;

	/**
	 * tester la methode qui marque un bon de migation comme livré.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-bon-migration.xml" })
	public void testmarquerBMGlobalLivreValid() {
		try {
			bonMigrationService.marquerBMGlobalLivre("REF_BM_GLOBAL");
			BonPreparation bonPreparation = bonMigrationService.findBMByReference("REF_BM_GLOBAL");
			assertNotNull(bonPreparation.getDateLivraisonTermine());
			// assertEquals(bonPreparation.getStatut(), StatusBonPreparation.LIVRER);
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected Exception :" + e.getMessage());
		}
	}

}
