package com.nordnet.topaze.test.service.produit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.catalog.test.GlobalTestCase;
import com.nordnet.topaze.catalogue.service.ProduitService;
import com.nordnet.topaze.exception.TopazeException;

/**
 * C'est la classe test {@link ProduitService#getTemplateXSD(String)}.
 * 
 * @author akram-moncer
 * 
 */
public class GetXSDTemplateTest extends GlobalTestCase {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(GetXSDTemplateTest.class);

	/**
	 * Reference du service utilise pour les tests.
	 */
	@Autowired
	private ProduitService produitService;

	/**
	 * Ce test verifie le renvoi du contenu du template xsd valid.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testGetXSDTemplateValid() {

		try {
			String xsdContent = produitService.getTemplateXSD("REF_PRODUIT_TEMPLATE");
			assertEquals(xsdContent,
					"<?xml version=\"1.0\" encoding=\"utf-8\" ?><xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"></xs:schema>");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas ou le produit n'existe pas.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testGetXSDTemplateProduitNotExist() {

		try {
			produitService.getTemplateXSD("REF_");
			fail("Unexpected exception");
		} catch (TopazeException e) {
			assertEquals(e.getErrorCode(), "0.1.2");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas ou le produit n'a pas de template xsd.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testGetXSDTemplateNotAssocier() {

		try {
			produitService.getTemplateXSD("REF_PRODUIT_S_OC");
			fail("Unexpected exception");
		} catch (TopazeException e) {
			assertEquals(e.getErrorCode(), "2.1.5");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas ou la template xsd n'existe pas.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testGetXSDTemplateNotExist() {

		try {
			produitService.getTemplateXSD("REF_PRODUIT_TEMPLATE_N");
			fail("Unexpected exception");
		} catch (TopazeException e) {
			assertEquals(e.getErrorCode(), "2.1.2");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

}
