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
 * This class tests {@link BonMigrationService#findBMByReference(String)}.
 * 
 * @author Oussama Denden
 * 
 */
public class FindBMByReferenceTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Log LOGGER = LogFactory.getLog(FindBMByReferenceTest.class);

	/**
	 * The service used for tests.
	 */
	@Autowired
	private BonMigrationService bonMigrationService;

	/**
	 * Ce test verifie la recherche d'un bon de migration par reference.
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
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-find-bm-par-reference.xml" })
	public void testFindBMByReferenceValid() {
		try {
			BonPreparation bonMigration = bonMigrationService.findBMByReference("REF_BM_GLOBAL");

			assertNotNull(bonMigration);

		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

}
