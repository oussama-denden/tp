package com.nordnet.topaze.test.service.livraison;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.livraison.core.domain.ElementLivraison;
import com.nordnet.topaze.livraison.core.service.BonPreparationService;
import com.nordnet.topaze.livraison.core.service.BonPreparationServiceImpl;
import com.nordnet.topaze.livraison.test.GlobalTestCase;

/**
 * Classe de test de la methode {@link BonPreparationServiceImpl#marquerSousBonPreparationNonLivrer(String, String)}.
 * 
 * @author akram-moncer
 * 
 */
public class MarquerSousBPNonLivreTest extends GlobalTestCase {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(MarquerSousBPNonLivreTest.class);

	/**
	 * Le service utiliser pour les tests.
	 */
	@Autowired
	private BonPreparationService bonPreparationService;

	/**
	 * Test la methode pour marquer un sous bon de preparation comme NonLivrer.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-bon-preparation.xml" })
	public void testMarquerSousBPNonLivreValid() {

		try {
			bonPreparationService.marquerSousBPNonLivre("REF_SBP3", "CAUSE_NON_LIVRAISON");
			fail("Unexpected Exception");
		} catch (TopazeException e) {
			ElementLivraison elementLivraison;
			try {
				elementLivraison =
						bonPreparationService.findElementLivraisonByReferenceAndReferenceProduit("REF_SBP3",
								"REF_PRODUIT_TEMPLATE");
				assertEquals("CAUSE_NON_LIVRAISON", elementLivraison.getCauseNonlivraison());
				assertEquals("3.1.2", e.getErrorCode());
			} catch (TopazeException e1) {
				LOGGER.error(e);
				fail("Unexpected Exception :" + e.getMessage());
			}

		} catch (Exception e) {
			fail("Unexpected Exception :" + e.getMessage());
		}
	}
}
