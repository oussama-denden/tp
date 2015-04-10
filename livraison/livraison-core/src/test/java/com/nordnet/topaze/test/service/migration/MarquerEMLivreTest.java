package com.nordnet.topaze.test.service.migration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.livraison.core.domain.ElementLivraison;
import com.nordnet.topaze.livraison.core.domain.TypeBonPreparation;
import com.nordnet.topaze.livraison.core.service.BonMigrationService;
import com.nordnet.topaze.livraison.test.GlobalTestCase;

/**
 * Cette classs teste {@link BonMigrationServiceImpl#marquerEMLivre(ElementLivraison))}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class MarquerEMLivreTest extends GlobalTestCase {

	/**
	 * /** Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(MarquerEMRetourneTest.class);

	/**
	 * Le service utiliser pour les tests.
	 */
	@Autowired
	private BonMigrationService bonMigrationService;

	/**
	 * tester la methode qui marque un element de migation comme livré.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-bon-migration.xml" })
	public void testmarquerEMRetourneValid() {
		try {
			ElementLivraison elementMigration = bonMigrationService.findEMByReference("REF_BM_A_RETOUR");
			bonMigrationService.marquerEMLivre(elementMigration);
			elementMigration = bonMigrationService.findEMByReference("REF_BM_A_RETOUR");
			assertNotNull(elementMigration.getDateLivraisonTermine());
			assertEquals(elementMigration.getTypeBonPreparation(), TypeBonPreparation.MIGRATION);
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected Exception :" + e.getMessage());
		}
	}

}
