package com.nordnet.topaze.test.service.migration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.livraison.core.domain.ElementLivraison;
import com.nordnet.topaze.livraison.core.domain.TypeBonPreparation;
import com.nordnet.topaze.livraison.core.domain.TypeProduit;
import com.nordnet.topaze.livraison.core.service.BonMigrationService;
import com.nordnet.topaze.livraison.core.service.BonMigrationServiceImpl;
import com.nordnet.topaze.livraison.test.GlobalTestCase;

/**
 * Classe de test de la methode {@link BonMigrationServiceImpl#getBiensMigrationEnCoursRetour()}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class GetBiensMigrationEnCoursRetourTest extends GlobalTestCase {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(GetBiensMigrationEnCoursRetourTest.class);

	/**
	 * Le service utiliser pour les tests.
	 */
	@Autowired
	private BonMigrationService bonMigrationService;

	/**
	 * Test la method pour retourner les élément de migration global en cours de retour.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-bon-migration.xml" })
	public void testGetBiensMigrationEnCoursRetourValid() {
		try {
			List<ElementLivraison> elementMigrations = bonMigrationService.getBiensMigrationEnCoursRetour();
			assertEquals(elementMigrations.size(), 3);
			for (ElementLivraison elementLivraison : elementMigrations) {
				assertNotNull(elementLivraison.getDatePreparation());
				assertNull(elementLivraison.getDateRetourTermine());
				assertNull(elementLivraison.getCauseNonlivraison());
				assertEquals(TypeBonPreparation.MIGRATION, elementLivraison.getBonPreparationParent()
						.getTypeBonPreparation());
				assertEquals(TypeProduit.BIEN, elementLivraison.getTypeElement());
			}
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected Exception :" + e.getMessage());
		}
	}

}
