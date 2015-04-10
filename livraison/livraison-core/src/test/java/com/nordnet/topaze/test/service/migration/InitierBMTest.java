package com.nordnet.topaze.test.service.migration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.domain.ElementLivraison;
import com.nordnet.topaze.livraison.core.domain.StatusBonPreparation;
import com.nordnet.topaze.livraison.core.domain.TypeBonPreparation;
import com.nordnet.topaze.livraison.core.domain.TypeContrat;
import com.nordnet.topaze.livraison.core.service.BonMigrationService;
import com.nordnet.topaze.livraison.test.GlobalTestCase;

/**
 * This class tests {@link BonMigrationService#initierBM(com.nordnet.topaze.livraison.core.domain.BonPreparation)}.
 * 
 * @author Oussama Denden
 * 
 */
public class InitierBMTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Log LOGGER = LogFactory.getLog(InitierBMTest.class);

	/**
	 * The service used for tests.
	 */
	@Autowired
	private BonMigrationService bonMigrationService;

	/**
	 * Ce test verifie l'initiation d'un bon de migration.
	 * <p>
	 * Entering data :
	 * <ul>
	 * <p>
	 * </p>
	 * </ul>
	 * <p>
	 * Expected result :
	 * <ul>
	 * <li>{@link BonPreparation}</li>
	 * </ul>
	 * 
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-initier-bm.xml" })
	public void testInitierBMValid() {
		try {
			BonPreparation bonMigrationAvantInitiation = getBonMigration();

			assertEquals(bonMigrationAvantInitiation.getDateInitiation(), null);

			bonMigrationService.initierBM(bonMigrationAvantInitiation);

			BonPreparation bonMigrationApresInitiation = bonMigrationService.findBMByReference("REF_BM_GLOBAL");
			assertNotNull(bonMigrationApresInitiation);
			assertNotNull(bonMigrationApresInitiation.getId());
			assertEquals(bonMigrationApresInitiation.getTypeBonPreparation(), TypeBonPreparation.MIGRATION);
			assertEquals(bonMigrationApresInitiation.getStatut(), StatusBonPreparation.INITIER);
			for (ElementLivraison elementLivraison : bonMigrationApresInitiation.getElementLivraisons()) {
				assertEquals(elementLivraison.getStatut(), StatusBonPreparation.INITIER);
			}
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * @return bon de migration non initie.
	 */
	private static BonPreparation getBonMigration() {
		BonPreparation bonMigration = new BonPreparation();
		bonMigration.setReference("REF_BM_GLOBAL");
		bonMigration.setIdClient("ID_CLIENT");
		bonMigration.setTypeBonPreparation(TypeBonPreparation.MIGRATION);

		ElementLivraison elementLivraison = new ElementLivraison();
		elementLivraison.setBonPreparationParent(bonMigration);
		elementLivraison.setReference("REF_EL");
		elementLivraison.setReferenceProduit("REF_PROD");
		elementLivraison.setTypeBonPreparation(TypeBonPreparation.MIGRATION);
		elementLivraison.setTypeContrat(TypeContrat.ABONNEMENT);
		bonMigration.getElementLivraisons().add(elementLivraison);
		return bonMigration;
	}

}
