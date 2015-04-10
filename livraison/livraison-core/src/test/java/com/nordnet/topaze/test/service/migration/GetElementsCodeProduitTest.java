package com.nordnet.topaze.test.service.migration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.livraison.core.business.ElementStateInfo;
import com.nordnet.topaze.livraison.core.business.ElementsRenouvellemtnInfo;
import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.domain.ElementLivraison;
import com.nordnet.topaze.livraison.core.domain.TypeProduit;
import com.nordnet.topaze.livraison.core.service.BonMigrationService;
import com.nordnet.topaze.livraison.core.service.BonPreparationService;
import com.nordnet.topaze.livraison.test.GlobalTestCase;
import com.nordnet.topaze.livraison.test.utils.ElementsRenouvellemtnInfoGanarator;

/**
 * Ce classe teste {@link BonMigrationService#getElementsCodeProduit(ElementsRenouvellemtnInfo)}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class GetElementsCodeProduitTest extends GlobalTestCase {

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
	 * The service used for tests.
	 */
	@Autowired
	private BonPreparationService bonPreparationService;

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
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-get-element-code-produit.xml" })
	public void testElementsCodeProduitValid() {
		try {
			List<ElementStateInfo> elementStateInfos = new ArrayList<>();
			BonPreparation bonPreparation = bonPreparationService.findBPByReference("REF_BP_GLOBAL");
			for (ElementLivraison elementLivraison : bonPreparation.getElementLivraisons()) {
				elementStateInfos.add(ElementsRenouvellemtnInfoGanarator.getElementStateInfo(elementLivraison));
			}

			ElementsRenouvellemtnInfo elementsRenouvellemtnInfo =
					ElementsRenouvellemtnInfoGanarator.getElementsRenouvellemtnInfo(TypeProduit.SERVICE,
							elementStateInfos);
			bonMigrationService.getElementsCodeProduit(elementsRenouvellemtnInfo);
			for (ElementStateInfo elementStateInfo : elementsRenouvellemtnInfo.getElementStateInfos()) {

				assertNotNull(elementStateInfo.getCodeProduit());
				if (elementStateInfo.getRefenceElementContractuelle().equals("REF_SBP1")) {
					assertEquals(elementStateInfo.getCodeProduit(), "CODE_PRODUIT_1");
				}
			}

		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}
}
