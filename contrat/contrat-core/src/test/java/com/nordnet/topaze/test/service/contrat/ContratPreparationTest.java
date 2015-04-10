package com.nordnet.topaze.test.service.contrat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.client.rest.enums.TypeProduit;
import com.nordnet.topaze.contrat.business.Frais;
import com.nordnet.topaze.contrat.business.Prix;
import com.nordnet.topaze.contrat.business.Produit;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.domain.ModeFacturation;
import com.nordnet.topaze.contrat.domain.ModePaiement;
import com.nordnet.topaze.contrat.domain.TypeContrat;
import com.nordnet.topaze.contrat.domain.TypeFrais;
import com.nordnet.topaze.contrat.domain.TypeTVA;
import com.nordnet.topaze.contrat.service.ContratService;
import com.nordnet.topaze.contrat.test.GlobalTestCase;

/**
 * 
 * This class tests {@link ContratService#contratPreparation(java.util.List)}.
 * 
 * @author anisselmane
 * 
 */
public class ContratPreparationTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Log LOGGER = LogFactory.getLog(ContratPreparationTest.class);

	/**
	 * The service used for tests.
	 */
	@Autowired
	private ContratService contratService;

	/**
	 * Gets the product.
	 * 
	 * @param refProduit
	 *            the reference produit
	 * @param numEC
	 *            numero element contractuel.
	 * @param periodicite
	 *            periodicite.
	 * @param engagement
	 *            engagement.
	 * @param duree
	 *            duree.
	 * @param typeProduit
	 *            the type produit
	 * @param outilLivraison
	 *            the outil livraison
	 * @return the product
	 */
	private static Produit getProduct(String refProduit, Integer numEC, TypeProduit typeProduit, Integer periodicite,
			Integer engagement, Integer duree) {

		Produit produit = new Produit();
		produit.setLabel("LABEL_TST");
		produit.setReference(refProduit);
		produit.setNumEC(numEC);
		produit.setNumeroCommande("1");
		Prix prix = new Prix();
		prix.setMontant(1000d);
		prix.setDuree(duree);
		prix.setEngagement(engagement);
		prix.setTypeTVA(TypeTVA.P);
		prix.setPeriodicite(periodicite);
		prix.setModeFacturation(ModeFacturation.DATE_ANNIVERSAIRE);
		prix.setModePaiement(ModePaiement.CB);
		Set<Frais> fraisSet = new HashSet<>();
		Frais premierFrais = new Frais();
		premierFrais.setMontant(10d);
		premierFrais.setTypeFrais(TypeFrais.RESILIATION);
		premierFrais.setOrdre(1);
		premierFrais.setNombreMois(2);
		fraisSet.add(premierFrais);
		Frais secondFrais = new Frais();
		secondFrais.setMontant(10d);
		secondFrais.setTypeFrais(TypeFrais.RESILIATION);
		secondFrais.setOrdre(2);
		fraisSet.add(secondFrais);
		prix.setFrais(fraisSet);
		produit.setPrix(prix);
		produit.setTypeProduit(typeProduit);

		return produit;
	}

	/**
	 * This test verifies if we can prepare a {@link ElementContractuel}.
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
	 * if (sousContrat.getReferenceProduit().equals("REF-PRODUIT2")) { assertEquals(sousContrat.getTypeContrat(),
	 * TypeContrat.VENTE); assertEquals(sousContrat.getModeFacturation(), ModeFacturation.DATE_ANNIVERSAIRE);
	 * assertEquals(sousContrat.getPeriodicite(), new Integer(1)); assertEquals(sousContrat.getDuree(), new Integer(1));
	 * } if (sousContrat.getReferenceProduit().equals("REF-PRODUIT3")) { assertEquals(sousContrat.getTypeContrat(),
	 * TypeContrat.VENTE); assertEquals(sousContrat.getModeFacturation(), ModeFacturation.DATE_ANNIVERSAIRE);
	 * assertEquals(sousContrat.getPeriodicite(), new Integer(1)); assertEquals(sousContrat.getDuree(), new Integer(1));
	 * }
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-contrat-preparation.xml" })
	public void testContratPreparationValid() {
		try {
			List<Produit> produits = new ArrayList<>();
			produits.add(ContratPreparationTest.getProduct("REF-PRODUIT", 1, TypeProduit.BIEN, 1, 1, 1));
			produits.add(ContratPreparationTest.getProduct("REF-PRODUIT1", 2, TypeProduit.SERVICE, 1, 1, 1));
			produits.add(ContratPreparationTest.getProduct("REF-PRODUIT2", 3, TypeProduit.BIEN, null, null, null));
			produits.add(ContratPreparationTest.getProduct("REF-PRODUIT3", 4, TypeProduit.BIEN, null, null, null));
			Contrat contrat = contratService.preparerContrat(produits, "USER_TST", false, null, false);
			for (ElementContractuel sousContrat : contrat.getSousContrats()) {
				if (sousContrat.getReferenceProduit().equals("REF-PRODUIT")) {
					assertEquals(sousContrat.getTypeContrat(), TypeContrat.LOCATION);
					assertEquals(sousContrat.getModeFacturation(), ModeFacturation.DATE_ANNIVERSAIRE);
				}
				if (sousContrat.getReferenceProduit().equals("REF-PRODUIT1")) {
					assertEquals(sousContrat.getTypeContrat(), TypeContrat.ABONNEMENT);
					assertEquals(sousContrat.getModeFacturation(), ModeFacturation.DATE_ANNIVERSAIRE);
				}
				if (sousContrat.getReferenceProduit().equals("REF-PRODUIT2")) {
					assertEquals(sousContrat.getTypeContrat(), TypeContrat.VENTE);
					assertEquals(sousContrat.getModeFacturation(), ModeFacturation.DATE_ANNIVERSAIRE);

				}
				if (sousContrat.getReferenceProduit().equals("REF-PRODUIT3")) {
					assertEquals(sousContrat.getTypeContrat(), TypeContrat.VENTE);
					assertEquals(sousContrat.getModeFacturation(), ModeFacturation.DATE_ANNIVERSAIRE);

				}

			}

		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}
}
