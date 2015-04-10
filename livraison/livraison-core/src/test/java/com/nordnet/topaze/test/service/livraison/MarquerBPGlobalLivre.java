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
import com.nordnet.topaze.livraison.core.domain.TypeBonPreparation;
import com.nordnet.topaze.livraison.core.service.BonPreparationService;
import com.nordnet.topaze.livraison.test.GlobalTestCase;

/**
 * 
 * This class tests {@link BonPreparationServiceImpl#marquerNonLivrerGlobal(BonPreparation))}.
 * 
 * @author anisselmane
 * 
 */
public class MarquerBPGlobalLivre extends GlobalTestCase {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(MarquerBPGlobalLivre.class);

	/**
	 * Le service utiliser pour les tests.
	 */
	@Autowired
	private BonPreparationService bonPreparationService;

	/**
	 * Ce test verifie que nous pouvons marker un {@link BonPreparation} LIVRER.
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
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-bon-preparation.xml" })
	public void testMarquerBonPreparationGlobalLivrerValid() {
		try {
			bonPreparationService.marquerLivre("REF_BP_GLOBAL_A_PREPARER");
			BonPreparation bonPreparation = bonPreparationService.findByReference("REF_BP_GLOBAL_A_PREPARER");
			assertNotNull(bonPreparation.getDateLivraisonTermine());
			assertEquals(bonPreparation.getTypeBonPreparation(), TypeBonPreparation.LIVRAISON);
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected Exception :" + e.getMessage());
		}
	}

}