package com.nordnet.topaze.test.service.livraison;

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
import com.nordnet.topaze.livraison.core.service.BonPreparationService;
import com.nordnet.topaze.livraison.test.GlobalTestCase;

/**
 * 
 * @author akram-moncer
 * 
 */
public class MarquerSousBPLivreTest extends GlobalTestCase {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(MarquerSousBPLivreTest.class);

	/**
	 * Le service utiliser pour les tests.
	 */
	@Autowired
	private BonPreparationService bonPreparationService;

	/**
	 * Cette method test le cas pour marquer un sous bon de preparation comme livrer.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-bon-preparation.xml" })
	public void testMarquerSousBonPreparationLivrerValid() {
		try {
			ElementLivraison elementLivraison =
					bonPreparationService.findElementLivraisonByReferenceAndReferenceProduit("REF_SBP_A_PREPARER2",
							"REF_PRODUIT");
			bonPreparationService.marquerLivre(elementLivraison);
			ElementLivraison elementLivraisonLivrer =
					bonPreparationService.findElementLivraisonByReferenceAndReferenceProduit("REF_SBP_A_PREPARER2",
							"REF_PRODUIT");
			assertNotNull(elementLivraisonLivrer.getDateLivraisonTermine());
			assertEquals(elementLivraisonLivrer.getTypeBonPreparation(), TypeBonPreparation.LIVRAISON);
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected Exception :" + e.getMessage());
		}
	}
}
