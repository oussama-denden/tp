package com.nordnet.topaze.test.service.recuperation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.domain.StatusBonPreparation;
import com.nordnet.topaze.livraison.core.service.BonPreparationService;
import com.nordnet.topaze.livraison.core.service.BonRecuperationService;
import com.nordnet.topaze.livraison.core.util.Constants;
import com.nordnet.topaze.livraison.test.GlobalTestCase;

/**
 * This class tests {@link BonRecuperationService#markerRecuperer(String)}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class MarquerRecupererTest extends GlobalTestCase {

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
	@DatabaseSetup(value = { "/dataset/test-recuperation.xml" })
	public void preparationBonPreparationRecuperationTestValid() {
		try {
			System.setProperty(Constants.ENV_PROPERTY, "dev");
			BonPreparation bonPreparationGlobal =
					bonRecuperationService.findBRByReference("REF_BP_GLOBAL_RETOUR-PREPARER");
			assertEquals(bonPreparationGlobal.getStatut(), StatusBonPreparation.PREPARER);
			bonRecuperationService.marquerRecupere("REF_BP_GLOBAL_RETOUR-PREPARER");
			bonPreparationGlobal = bonRecuperationService.findBRByReference("REF_BP_GLOBAL_RETOUR-PREPARER");
			assertEquals(bonPreparationGlobal.getStatut(), StatusBonPreparation.TERMINER);
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie que nous ne pouvons pas marker un {@link BonPreparation} comme recuperer lorsque on donne en
	 * parametre une reference vide.
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
	@DatabaseSetup(value = { "/dataset/test-recuperation.xml" })
	public void givenReferenceVideThenFail() {
		try {
			bonRecuperationService.marquerRecupere("");
			fail("Unexpected exception");
		} catch (TopazeException le) {
			assertEquals("0.1.4", le.getErrorCode());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception");
		}

	}

	/**
	 * Ce test verifie que nous ne pouvons pas marker un {@link BonPreparation} comme recuperer si le
	 * {@link BonPreparation} n'est pas preparer.
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
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-recuperation.xml" })
	public void givenBonPreparationNonPreparerThenFail() {
		try {
			bonRecuperationService.marquerRecupere("REF_BP_GLOBAL_RETOUR_NON_PREPARER");
			fail("Unexpected exception");
		} catch (TopazeException le) {
			try {
				LOGGER.error(le);
				assertEquals("3.1.6", le.getErrorCode());
			} catch (Exception e) {
				LOGGER.error(e);
				fail("Unexpected exception");
			}
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception");
		}

	}

}
