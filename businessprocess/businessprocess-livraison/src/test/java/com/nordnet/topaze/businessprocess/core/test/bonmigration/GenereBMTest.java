package com.nordnet.topaze.businessprocess.core.test.bonmigration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.core.service.BusinessProcessService;
import com.nordnet.topaze.businessprocess.test.GlobalTestCase;
import com.nordnet.topaze.client.rest.business.contrat.Contrat;
import com.nordnet.topaze.client.rest.business.livraison.Avenant;
import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;
import com.nordnet.topaze.client.rest.business.livraison.ContratMigrationInfo;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.client.rest.enums.TypeBonPreparation;
import com.nordnet.topaze.client.rest.enums.TypeProduit;

/**
 * Class test de la method
 * {@link BusinessProcessService#genereBM(com.nordnet.topaze.businessprocess.core.business.ContratMigrationInfo)}.
 * 
 * @author Oussama Denden
 * 
 */
public class GenereBMTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Log LOGGER = LogFactory.getLog(GenereBMTest.class);

	/**
	 * The service used for tests.
	 */
	@Autowired
	private BusinessProcessService businessProcessService;

	/**
	 * Tester le cas d'association d'une resuction a un {@link Contrat} global.
	 */
	@Test
	public void testGenereBMValid() {

		try {
			ContratMigrationInfo migrationInfo = getMigrationInfo();
			BonPreparation bonMigration = businessProcessService.genereBM(migrationInfo);

			assertTrue(bonMigration.getTypeBonPreparation().equals(TypeBonPreparation.MIGRATION));
			assertTrue(bonMigration.getElementLivraisons().size() == 3);

			for (ElementLivraison el : bonMigration.getElementLivraisons()) {
				if (el.getReference().equals("ELEMENT_A_MIGRE")) {
					assertEquals(el.getTypeBonPreparation(), TypeBonPreparation.MIGRATION);
					assertTrue(el.getNumEC() == 1);
					assertEquals(el.getReferenceProduit(), "REF_NV_PROD");
				}

				if (el.getReference().equals("ELEMENT_A_AJOUTE")) {
					assertEquals(el.getTypeBonPreparation(), TypeBonPreparation.LIVRAISON);
					assertTrue(el.getNumEC() == 10);
				}

				if (el.getReference().equals("ELEMENT_A_RESILIE")) {
					assertEquals(el.getTypeBonPreparation(), TypeBonPreparation.RETOUR);
					assertTrue(el.getNumEC() == 2);
				}
			}

		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * @return {@link ContratMigrationInfo}.
	 */
	private static ContratMigrationInfo getMigrationInfo() {
		ContratMigrationInfo migrationInfo = new ContratMigrationInfo();

		Avenant avenantInfo = new Avenant();

		ElementLivraison elementAMigre = new ElementLivraison();
		elementAMigre.setReference("ELEMENT_A_MIGRE");
		elementAMigre.setReferenceProduit("REF_NV_PROD");
		elementAMigre.setTypeElement(TypeProduit.BIEN);
		elementAMigre.setNumEC(1);

		ElementLivraison elementAAjoute = new ElementLivraison();
		elementAAjoute.setReference("ELEMENT_A_AJOUTE");
		elementAAjoute.setTypeElement(TypeProduit.BIEN);
		elementAAjoute.setNumEC(10);

		List<ElementLivraison> elementsMigration = new ArrayList<>();
		elementsMigration.add(elementAMigre);
		elementsMigration.add(elementAAjoute);

		avenantInfo.setContratModifications(elementsMigration);

		migrationInfo.setAvenant(avenantInfo);

		BonPreparation ancienBonPreparation = new BonPreparation();
		ancienBonPreparation.setTypeBonPreparation(TypeBonPreparation.LIVRAISON);

		ElementLivraison ancienElementAMigre = new ElementLivraison();
		ancienElementAMigre.setReference("ELEMENT_A_MIGRE");
		ancienElementAMigre.setTypeElement(TypeProduit.BIEN);
		ancienElementAMigre.setNumEC(1);

		ElementLivraison ancienElementAResilie = new ElementLivraison();
		ancienElementAResilie.setReference("ELEMEN_A_RESILIE");
		ancienElementAResilie.setTypeElement(TypeProduit.BIEN);
		ancienElementAResilie.setNumEC(2);

		Set<ElementLivraison> ancienElements = new HashSet<>();
		ancienElements.add(ancienElementAMigre);
		ancienElements.add(ancienElementAResilie);

		ancienBonPreparation.setElementLivraison(ancienElements);

		migrationInfo.setBonPreparation(ancienBonPreparation);
		return migrationInfo;
	}

}
