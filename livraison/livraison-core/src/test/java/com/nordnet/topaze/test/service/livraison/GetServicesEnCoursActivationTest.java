package com.nordnet.topaze.test.service.livraison;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.livraison.core.domain.ElementLivraison;
import com.nordnet.topaze.livraison.core.domain.TypeBonPreparation;
import com.nordnet.topaze.livraison.core.domain.TypeProduit;
import com.nordnet.topaze.livraison.core.service.BonPreparationService;
import com.nordnet.topaze.livraison.core.service.BonPreparationServiceImpl;
import com.nordnet.topaze.livraison.test.GlobalTestCase;

/**
 * Classe de test de la methode {@link BonPreparationServiceImpl#getServicesEnCoursActivation()}.
 * 
 * @author akram-moncer
 * 
 */
public class GetServicesEnCoursActivationTest extends GlobalTestCase {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(GetServicesEnCoursActivationTest.class);

	/**
	 * Le service utiliser pour les tests.
	 */
	@Autowired
	private BonPreparationService bonPreparationService;

	/**
	 * Test la method pour retourner les sous bons de preparation de type service en cours d'activation.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-bon-preparation.xml" })
	public void testGetServicesEnCoursActivationTestValid() {
		try {
			List<ElementLivraison> elementLivraisons = bonPreparationService.getServicesEnCoursActivation();
			for (ElementLivraison elementLivraison : elementLivraisons) {
				assertNotNull(elementLivraison.getDatePreparation());
				assertNull(elementLivraison.getDateLivraisonTermine());
				assertNull(elementLivraison.getCauseNonlivraison());
				assertEquals(TypeBonPreparation.LIVRAISON, elementLivraison.getTypeBonPreparation());
				assertEquals(TypeProduit.SERVICE, elementLivraison.getTypeElement());
			}
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected Exception :" + e.getMessage());
		}
	}
}
