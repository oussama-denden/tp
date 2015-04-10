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
import com.nordnet.topaze.livraison.core.repository.ElementLivraisonRepository;
import com.nordnet.topaze.livraison.core.service.BonPreparationService;
import com.nordnet.topaze.livraison.core.service.BonPreparationServiceImpl;
import com.nordnet.topaze.livraison.test.GlobalTestCase;

/**
 * Classe de test de la methode {@link BonPreparationServiceImpl#marquerServicePreparer(BonPreparation)}.
 * 
 * @author akram-moncer
 * 
 */
public class MarquerServicePreparerTest extends GlobalTestCase {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(MarquerServicePreparerTest.class);

	/**
	 * {@link ElementLivraisonRepository}.
	 */
	@Autowired
	private ElementLivraisonRepository elementLivraisonRepository;

	/**
	 * Le service utiliser pour les tests.
	 */
	@Autowired
	private BonPreparationService bonPreparationService;

	/**
	 * Test la methode pour marquer les sous bons de preparation de type service comme prepare.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-bon-preparation.xml" })
	public void testMarquerServicePreparerValid() {

		try {
			ElementLivraison elementLivraison = elementLivraisonRepository.findByReference("REF_SBP1");
			bonPreparationService.marquerServicePreparer(elementLivraison);
			assertNotNull(elementLivraison.getDatePreparation());
			assertNotNull(elementLivraison.getRetailerPackagerId());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected Exception :" + e.getMessage());
		}
	}

}
