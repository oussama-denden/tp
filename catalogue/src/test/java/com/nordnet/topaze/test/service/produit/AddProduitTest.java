package com.nordnet.topaze.test.service.produit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.catalog.test.GlobalTestCase;
import com.nordnet.topaze.catalogue.domain.FraisProduit;
import com.nordnet.topaze.catalogue.domain.OutilLivraison;
import com.nordnet.topaze.catalogue.domain.Prix;
import com.nordnet.topaze.catalogue.domain.Produit;
import com.nordnet.topaze.catalogue.domain.TypeFrais;
import com.nordnet.topaze.catalogue.domain.TypePrix;
import com.nordnet.topaze.catalogue.domain.TypeProduit;
import com.nordnet.topaze.catalogue.domain.TypeTVA;
import com.nordnet.topaze.catalogue.service.ProduitService;
import com.nordnet.topaze.exception.TopazeException;

/**
 * 
 * C'est la classe test {@link ProduitService#addProduit(Produit)}.
 * 
 * @author anisselmane
 * 
 */
public class AddProduitTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Log LOGGER = LogFactory.getLog(AddProduitTest.class);

	/**
	 * The service used for tests.
	 */
	@Autowired
	private ProduitService produitService;

	/**
	 * Gets the product.
	 * 
	 * @param refProduit
	 *            the reference produit
	 * @param typePrix
	 *            the type prix
	 * @param typeProduit
	 *            the type produit
	 * @param outilLivraison
	 *            the outil livraison
	 * @return the product
	 */
	private static Produit getProduct(String refProduit, TypePrix typePrix, TypeProduit typeProduit,
			OutilLivraison outilLivraison) {
		Produit produit = new Produit();
		produit.setLabel("LABEL_TST");
		produit.setReference(refProduit);
		Prix prix = new Prix();
		prix.setMontant(1000d);
		prix.setTypePrix(typePrix);
		prix.setEngagement(1);
		produit.setPrix(prix);
		produit.setOutilsLivraison(outilLivraison);
		produit.setTypeProduit(typeProduit);
		produit.setTypeTVA(TypeTVA.P);
		FraisProduit fraisProduit = new FraisProduit();
		fraisProduit.setMontant(3.0);
		fraisProduit.setTypeFrais(TypeFrais.CREATION);
		Set<FraisProduit> fraisList = new HashSet<>();
		fraisList.add(fraisProduit);
		produit.setFrais(fraisList);
		prix.setProduit(produit);
		return produit;
	}

	/**
	 * This test verifies if we can add {@link Produit}.
	 * <p>
	 * Entering data :
	 * <ul>
	 * <p>
	 * </p>
	 * </ul>
	 * <p>
	 * Expected result :
	 * <ul>
	 * <li>Nothing</li>
	 * </ul>
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testAddProduitValid() {
		try {

			Produit produit = getProduct("equipment", TypePrix.RECURRENT, TypeProduit.SERVICE, OutilLivraison.PACKAGER);

			produitService.addProduit(produit);
			produit = produitService.getProduit("equipment");
			Assert.assertTrue(produit.getId() != null);
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas le prix du produit est null.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testAddProduitWithPrixNull() {
		try {
			Produit produit =
					AddProduitTest.getProduct("equipment", TypePrix.RECURRENT, TypeProduit.SERVICE,
							OutilLivraison.PACKAGER);
			produit.setPrix(null);
			produitService.addProduit(produit);
			fail("Unexpected exception");
		} catch (TopazeException e) {
			assertEquals(e.getMessage(), "un des parametres est mal formate : Prix.");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Ce test verifie le cas ou le montant du prix du produit est non valid.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testAddProduitWithMontantNonValid() {
		try {
			Produit produit = getProduct("equipment", TypePrix.RECURRENT, TypeProduit.SERVICE, OutilLivraison.PACKAGER);
			produit.getPrix().setMontant(null);
			produitService.addProduit(produit);
		} catch (TopazeException e) {
			assertEquals(e.getMessage(), "un des parametres est mal formate : Prix.montant.");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas ou le typePrix du produit est null.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testAddProduitWithTypePrixNull() {
		try {
			Produit produit = getProduct("equipment", TypePrix.RECURRENT, TypeProduit.SERVICE, OutilLivraison.PACKAGER);
			produit.getPrix().setTypePrix(null);
			produitService.addProduit(produit);
		} catch (TopazeException e) {
			assertEquals(e.getMessage(), "un des parametres est mal formate : Prix.typePrix.");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas ou les frais du produit sont non valid.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testAddProduitWithFraisNonValid() {
		try {
			Produit produit = getProduct("equipment", TypePrix.RECURRENT, TypeProduit.SERVICE, OutilLivraison.PACKAGER);
			for (FraisProduit fraisProduit : produit.getFrais()) {
				fraisProduit.setMontant(-3.0);
			}
			produitService.addProduit(produit);
		} catch (TopazeException e) {
			assertEquals(e.getMessage(), "un des parametres est mal formate : Produit.Frais.montant.");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas ou la reference du produit est non valid.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testAddProduitWithReferenceNonValid() {
		try {
			Produit produit = getProduct("equipment", TypePrix.RECURRENT, TypeProduit.SERVICE, OutilLivraison.PACKAGER);
			produit.setReference(null);
			produitService.addProduit(produit);
		} catch (TopazeException e) {
			assertEquals(e.getMessage(), "un des parametres est mal formate : Produit.reference.");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas ou la label du produit est non valid.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testAddProduitWithLabelNonValid() {
		try {
			Produit produit = getProduct("equipment", TypePrix.RECURRENT, TypeProduit.SERVICE, OutilLivraison.PACKAGER);
			produit.setLabel(null);
			produitService.addProduit(produit);
		} catch (TopazeException e) {
			assertEquals(e.getMessage(), "un des parametres est mal formate : Produit.label.");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas ou le type du produit est null.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testAddProduitWithTypeProduitNull() {
		try {
			Produit produit = getProduct("equipment", TypePrix.RECURRENT, TypeProduit.SERVICE, OutilLivraison.PACKAGER);
			produit.setTypeProduit(null);
			produitService.addProduit(produit);
		} catch (TopazeException e) {
			assertEquals(e.getMessage(), "un des parametres est mal formate : Produit.typeProduit.");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas ou le typeTVA du produit est null.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testAddProduitWithTypeTVANull() {
		try {
			Produit produit = getProduct("equipment", TypePrix.RECURRENT, TypeProduit.SERVICE, OutilLivraison.PACKAGER);
			produit.setTypeTVA(null);
			produitService.addProduit(produit);
		} catch (TopazeException e) {
			assertEquals(e.getMessage(), "Produit.typeTVA ne peut pas etre null.");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas ou l'outils livraison du produit est null.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testAddProduitWithOutilLivraisonNull() {
		try {
			Produit produit = getProduct("equipment", TypePrix.RECURRENT, TypeProduit.SERVICE, OutilLivraison.PACKAGER);
			produit.setOutilsLivraison(null);
			produitService.addProduit(produit);
		} catch (TopazeException e) {
			assertEquals(e.getMessage(), "Produit.outilsLivraison ne peut pas etre null.");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas ou l'outils livraison est Packager et le produit est de type bien.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testAddProduitWithTypeBienOutilLivraisonPackager() {
		try {
			Produit produit = getProduct("equipment", TypePrix.RECURRENT, TypeProduit.BIEN, OutilLivraison.PACKAGER);
			produitService.addProduit(produit);
		} catch (TopazeException e) {
			assertEquals(e.getMessage(),
					"Pour Produit de type BIEN, Produit.outilsLivraison ne pas etre de type PACKAGER.");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas ou l'outils livraison est Netdelivery et le produit est de type service.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testAddProduitWithTypeServiceOutilLivraisonNetDelivery() {
		try {
			Produit produit =
					getProduct("equipment", TypePrix.RECURRENT, TypeProduit.SERVICE, OutilLivraison.NETDELIVERY);
			produitService.addProduit(produit);
		} catch (TopazeException e) {
			assertEquals(e.getMessage(),
					"Pour Produit de type SERVICE, Produit.outilsLivraison ne pas etre de type NETDELIVERY.");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas ou le type de prix est ONE_SHOT et le produit est de type service.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testAddProduitWithTypeServiceTypePrixOneShot() {
		try {
			Produit produit = getProduct("equipment", TypePrix.RECURRENT, TypeProduit.SERVICE, OutilLivraison.PACKAGER);
			produit.getPrix().setTypePrix(TypePrix.ONE_SHOT);
			produitService.addProduit(produit);
		} catch (TopazeException e) {
			assertEquals(e.getMessage(),
					"Pour Produit de type SERVICE, Produit.Prix.typePrix ne pas etre de type ONE_SHOT.");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas ou le type de prix est TROIS_FOIS_SANS_FRAIS et le produit est de type service.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testAddProduitWithTypeServiceTypePrixTroisFoisSansFrais() {
		try {
			Produit produit = getProduct("equipment", TypePrix.RECURRENT, TypeProduit.SERVICE, OutilLivraison.PACKAGER);
			produit.getPrix().setTypePrix(TypePrix.TROIS_FOIS_SANS_FRAIS);
			produitService.addProduit(produit);
		} catch (TopazeException e) {
			assertEquals(e.getMessage(),
					"Pour Produit de type SERVICE, Produit.Prix.typePrix ne pas etre de type TROIS_FOIS_SANS_FRAIS.");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

	/**
	 * Ce test verifie le cas le produit exist deja.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-catalogue-add-produit.xml" })
	public void testAddProduitExist() {
		try {
			Produit produit =
					getProduct("PRODUIT_TST", TypePrix.RECURRENT, TypeProduit.SERVICE, OutilLivraison.PACKAGER);
			produitService.addProduit(produit);
		} catch (TopazeException e) {
			assertEquals(e.getErrorCode(), "0.1.3");
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}
}
