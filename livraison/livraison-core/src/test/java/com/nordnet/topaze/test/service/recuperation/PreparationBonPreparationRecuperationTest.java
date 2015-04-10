package com.nordnet.topaze.test.service.recuperation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.domain.StatusBonPreparation;
import com.nordnet.topaze.livraison.core.service.BonPreparationService;
import com.nordnet.topaze.livraison.core.service.BonRecuperationService;
import com.nordnet.topaze.livraison.core.util.Constants;
import com.nordnet.topaze.livraison.test.GlobalTestCase;

/**
 * This class tests {@link BonRecuperationService#preparationBonPreparationRecuperation(BonPreparation)}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class PreparationBonPreparationRecuperationTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Log LOGGER = LogFactory.getLog(InitierBRTest.class);

	/**
	 * The service used for tests.
	 */
	@Autowired
	private BonRecuperationService bonRecuperationService;

	/**
	 * The service used for tests.
	 */
	@Autowired
	private BonPreparationService bonPreparationService;

	/**
	 * Ce test verifie que nous pouvons preparer un {@link BonPreparation} global pour le recuperation des biens et des
	 * services.
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
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-recuperation.xml" })
	public void preparationBonPreparationRecuperationTestValid() {
		try {

			System.setProperty(Constants.ENV_PROPERTY, "dev");
			BonPreparation bonPreparationGlobal = bonRecuperationService.findBRByReference("REF_BP_GLOBAL_RETOUR");
			assertEquals(bonPreparationGlobal.getStatut(), StatusBonPreparation.INITIER);
			// for (BonPreparation bonPreparation : bonPreparationGlobal.getSousBonPreparation()) {
			// assertEquals(bonPreparation.getStatut(), StatusBonPreparation.INITIER);
			// }
			bonRecuperationService.preparerBR(bonPreparationGlobal);
			bonPreparationGlobal = bonPreparationService.findByReference("REF_BP_GLOBAL_RETOUR");
			// for (BonPreparation bonPreparation : bonPreparationGlobal.getSousBonPreparation()) {
			// assertEquals(bonPreparation.getStatut(), StatusBonPreparation.PREPARER);
			// }
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

}
