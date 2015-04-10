package com.nordnet.topaze.test.service.produit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.catalog.test.GlobalTestCase;
import com.nordnet.topaze.catalogue.domain.Produit;
import com.nordnet.topaze.catalogue.service.ProduitService;
import com.nordnet.topaze.exception.TopazeException;

/**
 * C'est la classe test {@link ProduitService#associerTemplate(String, String)}.
 * 
 * @author akram-moncer
 * 
 */
public class AssocierTemplateTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Log LOGGER = LogFactory.getLog(AssocierTemplateTest.class);

	/**
	 * The service used for tests.
	 */
	@Autowired
	private ProduitService produitService;

	/**
	 * Ce test verifie si l'association d'un template est valide.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testAssocierTemplateValid() {

		try {
			Produit produit = produitService.associerTemplate("REF_PRODUIT_B_TFSF", "Max");
			assertEquals("Max", produit.getXmlTemplatePath());
			assertEquals("Max", produit.getXsdTemplatePath());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas ou le produit à qui on desire associer une remplate n'existe pas.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testAssocierTemplateWithProduitNotExist() {

		try {
			produitService.associerTemplate("REF_", "Max");
			fail("Unexpected exception");
		} catch (TopazeException e) {
			assertEquals(e.getErrorCode(), "0.1.2");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Ce test verifie le cas ou le produit à qui on desire associer une remplate est de type bien.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testAssocierTemplateWithProduitTypeBien() {
		try {
			produitService.associerTemplate("REF_PRODUIT_B_OS", "Max");
			fail("Unexpected exception");
		} catch (TopazeException e) {
			assertEquals(e.getErrorCode(), "2.1.3");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas ou la template à associer n'existe pas.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testAssocierTemplateWithTemplateNotExists() {

		try {
			produitService.associerTemplate("REF_PRODUIT_B_TFSF", "Nax");
			fail("Unexpected exception");
		} catch (TopazeException e) {
			assertEquals(e.getErrorCode(), "2.1.1");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}
}
