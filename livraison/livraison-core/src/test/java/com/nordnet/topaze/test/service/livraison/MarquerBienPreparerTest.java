package com.nordnet.topaze.test.service.livraison;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.domain.ElementLivraison;
import com.nordnet.topaze.livraison.core.domain.TypeProduit;
import com.nordnet.topaze.livraison.core.service.BonPreparationService;
import com.nordnet.topaze.livraison.core.service.BonPreparationServiceImpl;
import com.nordnet.topaze.livraison.test.GlobalTestCase;

/**
 * Classe de test de la methode {@link BonPreparationServiceImpl#marquerBienPreparer(BonPreparation)}.
 * 
 * @author akram-moncer
 * 
 */
public class MarquerBienPreparerTest extends GlobalTestCase {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(MarquerBienPreparerTest.class);

	/**
	 * Le service utiliser pour les tests.
	 */
	@Autowired
	private BonPreparationService bonPreparationService;

	/**
	 * Test la methode pour marquer les sous bons de preparation de type bien comme prepare.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-bon-preparation.xml" })
	public void testMarquerBienPreparerValid() {

		try {
			BonPreparation bonPreparation = bonPreparationService.findByReference("REF_BP_GLOBAL_A_PREPARER");
			bonPreparationService.marquerBienPreparer(bonPreparation);
			bonPreparation = bonPreparationService.findByReference("REF_BP_GLOBAL_A_PREPARER");
			for (ElementLivraison elementLivraison : bonPreparation.getElementLivraisons()) {
				if (elementLivraison.getTypeElement() == TypeProduit.BIEN) {
					assertNotNull(elementLivraison.getDatePreparation());
				}
			}
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected Exception :" + e.getMessage());
		}
	}

}
