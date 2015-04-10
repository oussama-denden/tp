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
 * C'est la classe test {@link ProduitService#getTemplateXML(String)}.
 * 
 * @author akram-moncer
 * 
 */
public class GetXMLTemplateTest extends GlobalTestCase {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(GetXMLTemplateTest.class);

	/**
	 * Reference du service utilise pour les tests.
	 */
	@Autowired
	private ProduitService produitService;

	/**
	 * Ce test verifie le renvoi du contenu du template xml valid.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testGetXMLTemplateValid() {

		try {
			String xmlContent = produitService.getTemplateXML("REF_PRODUIT_TEMPLATE");
			assertEquals(xmlContent, "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><test>test xml template</test>");
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
	public void testGetXMLTemplateProduitNotExist() {

		try {
			produitService.getTemplateXML("REF_");
			fail("Unexpected exception");
		} catch (TopazeException e) {
			assertEquals(e.getErrorCode(), "0.1.2");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas ou le produit n'a pas de template xml.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testGetXMLTemplateNotAssocier() {

		try {
			produitService.getTemplateXML("REF_PRODUIT_S_OC");
			fail("Unexpected exception");
		} catch (TopazeException e) {
			assertEquals(e.getErrorCode(), "2.1.4");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas ou la template xml n'existe pas.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testGetXMLTemplateNotExist() {

		try {
			produitService.getTemplateXML("REF_PRODUIT_TEMPLATE_N");
			fail("Unexpected exception");
		} catch (TopazeException e) {
			assertEquals(e.getErrorCode(), "2.1.1");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}
}
