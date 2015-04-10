package com.nordnet.topaze.test.service.recuperation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.domain.StatusBonPreparation;
import com.nordnet.topaze.livraison.core.domain.TypeBonPreparation;
import com.nordnet.topaze.livraison.core.service.BonPreparationService;
import com.nordnet.topaze.livraison.core.service.BonRecuperationService;
import com.nordnet.topaze.livraison.core.util.Constants;
import com.nordnet.topaze.livraison.test.GlobalTestCase;

/**
 * 
 * This class tests {@link BonRecuperationService#initiationBonPreparationRecuperation(String)}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class InitierBRTest extends GlobalTestCase {

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
	 * Ce test verifie que nous pouvons editer un {@link BonPreparation} pour le recuperation.
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
	@DatabaseSetup(value = { "/dataset/test-initiation-recuperation.xml" })
	public void testInitierBRValid() {
		try {
			System.setProperty(Constants.ENV_PROPERTY, "dev");
			bonRecuperationService.initierBR("REF_BP_GLOBAL_LIVRER");
			BonPreparation bonPreparationRecuperation =
					bonRecuperationService.findBRByReference("REF_BP_GLOBAL_LIVRER");
			assertNotNull(bonPreparationRecuperation);
			assertNotNull(bonPreparationRecuperation.getId());
			assertEquals(bonPreparationRecuperation.getTypeBonPreparation(), TypeBonPreparation.RETOUR);
			// assertEquals(bonPreparationRecuperation.getSousBonPreparation().size(), 3);
			assertEquals(bonPreparationRecuperation.getStatut(), StatusBonPreparation.INITIER);
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Ce test verifie que nous ne pouvons pas editer un {@link BonPreparation} de recuperation lorsque on donne en
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
	@DatabaseSetup(value = { "/dataset/test-initiation-recuperation.xml" })
	public void givenReferenceVideThenFail() {
		try {
			bonRecuperationService.initierBR("");
			fail("Unexpected exception");
		} catch (TopazeException le) {
			assertEquals("0.1.4", le.getErrorCode());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception");
		}

	}

	/**
	 * Ce test verifie que nous ne pouvons pas editer un {@link BonPreparation} de recuperation pour un bon
	 * {@link BonPreparation} qui n'existe pas.
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
	@DatabaseSetup(value = { "/dataset/test-initiation-recuperation.xml" })
	public void givenBonPreparationNonExistantThenFail() {
		try {
			bonRecuperationService.initierBR("REF_NOT_EXIST");
			fail("Unexpected exception");
		} catch (TopazeException de) {
			try {
				LOGGER.error(de);
				assertEquals("0.1.2", de.getErrorCode());
				BonPreparation bonPreparation = bonPreparationService.findByReference("REF_NOT_EXIST");
				assertEquals(bonPreparation, null);
			} catch (Exception e) {
				LOGGER.error(e);
				fail("Unexpected exception");
			}
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception");
		}

	}

	/**
	 * Ce test verifie que nous ne pouvons pas initier un {@link BonPreparation} de recuperation pour un bon
	 * {@link BonPreparation} dont le proecessus de recuperation a deja commencee.
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
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-initiation-recuperation.xml" })
	public void givenBonPreparationDejaRecupererThenFail() {
		try {
			bonRecuperationService.initierBR("REF_BP_RETOURNER");
			fail("Unexpected exception");
		} catch (TopazeException le) {
			LOGGER.error(le);
			assertEquals("3.1.7", le.getErrorCode());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}
}
