package com.nordnet.topaze.test.service.contrat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.contrat.business.ContratReduction;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.domain.Reduction;
import com.nordnet.topaze.contrat.domain.TypeFrais;
import com.nordnet.topaze.contrat.domain.TypeReduction;
import com.nordnet.topaze.contrat.domain.TypeValeurReduction;
import com.nordnet.topaze.contrat.repository.ContratRepository;
import com.nordnet.topaze.contrat.repository.ElementContractuelRepository;
import com.nordnet.topaze.contrat.service.ReductionService;
import com.nordnet.topaze.contrat.test.GlobalTestCase;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Class test de la method
 * {@link ReductionService#ajouterReduction(com.nordnet.topaze.contrat.domain.Reduction, String)}.
 * 
 * @author akram-moncer
 * 
 */
public class AjouterReductionTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(AjouterReductionTest.class);

	/**
	 * The service used for tests.
	 */
	@Autowired
	private ReductionService reductionService;

	/**
	 * {@link ContratRepository}.
	 */
	@Autowired
	private ContratRepository contratRepository;

	/**
	 * {@link ElementContractuelRepository}.
	 */
	@Autowired
	private ElementContractuelRepository elementContractuelRepository;

	/**
	 * Tester le cas d'association d'une resuction a un {@link Contrat} global.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ajouter-reduction.xml" })
	public void testAjouterReductionContratValid() {

		try {
			reductionService.ajouterReductionContrat(
					getReduction(5d, TypeReduction.CONTRAT, TypeValeurReduction.POURCENTAGE, 1, null, null), "REF_GB");
			Contrat contrat = contratRepository.findByReference("REF_GB");
			List<Reduction> reductionGlobales = reductionService.findReductionGlobales(contrat.getReference(), null);
			for (Reduction reductionGlobal : reductionGlobales) {
				assertTrue(reductionGlobal.getId() > Constants.ZERO);
			}
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Tester le cas d'association d'une resuction a un {@link ElementContractuel}.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ajouter-reduction.xml" })
	public void testAjouterReductionElementContractuelValid() {

		try {
			reductionService.ajouterReductionElementContractuelle(
					getReduction(5d, TypeReduction.CONTRAT, TypeValeurReduction.POURCENTAGE, 1, null, null), "REF_GB",
					1);
			ElementContractuel elementContractuel =
					elementContractuelRepository.findByNumECAndReferenceContrat(1, "REF_GB");
			Reduction reduction =
					reductionService.findReductionPartiel(elementContractuel.getContratParent().getReference(), null,
							elementContractuel.getNumEC());
			assertTrue(reduction.getId() > Constants.ZERO);
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Tester le cas d'association d'une resuction a un element non existant.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ajouter-reduction.xml" })
	public void testAjouterReductionElementNOTExist() {

		try {
			reductionService.ajouterReductionElementContractuelle(
					getReduction(5d, TypeReduction.CONTRAT, TypeValeurReduction.POURCENTAGE, 1, null, null), "REF", 1);
			fail("Test fail.");
		} catch (TopazeException e) {
			assertEquals("0.1.2", e.getErrorCode());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Tester le cas d'association d'une resuction avec une valeur null.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ajouter-reduction.xml" })
	public void testAjouterReductionWithValeurNull() {

		try {
			reductionService.ajouterReductionElementContractuelle(
					getReduction(null, TypeReduction.CONTRAT, TypeValeurReduction.POURCENTAGE, 1, null, null),
					"REF_GB", 1);
			fail("Test fail.");
		} catch (TopazeException e) {
			assertEquals("0.1.4", e.getErrorCode());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Tester le cas d'association d'une resuction avec un type valeur null.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ajouter-reduction.xml" })
	public void testAjouterReductionWithTypeValeurNull() {

		try {
			reductionService.ajouterReductionElementContractuelle(
					getReduction(5d, TypeReduction.CONTRAT, null, 1, null, null), "REF_GB", 1);
			fail("Test fail.");
		} catch (TopazeException e) {
			assertEquals("0.1.1", e.getErrorCode());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Tester le cas d'association d'une reduction de type mois avec une valeur non valid.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ajouter-reduction.xml" })
	public void testAjouterReductionMoisWithValeurNonValid() {

		try {
			reductionService.ajouterReductionElementContractuelle(
					getReduction(5.5d, TypeReduction.CONTRAT, TypeValeurReduction.MOIS, 1, null, null), "REF_GB", 1);
			fail("Test fail.");
		} catch (TopazeException e) {
			assertEquals("1.1.51", e.getErrorCode());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Tester le cas d'association d'une resuction de type pourcentage avec une valeur non valid.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ajouter-reduction.xml" })
	public void testAjouterReductionPourcentageWithValeurNonValid() {

		try {
			reductionService.ajouterReductionElementContractuelle(
					getReduction(101d, TypeReduction.CONTRAT, TypeValeurReduction.POURCENTAGE, 1, null, null),
					"REF_GB", 1);
			fail("Test fail.");
		} catch (TopazeException e) {
			assertEquals("1.1.53", e.getErrorCode());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Tester le cas d'association d'une reduction de type euro avec une valeur non valid.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ajouter-reduction.xml" })
	public void testAjouterReductionEuroWithValeurNonValid() {

		try {
			reductionService.ajouterReductionElementContractuelle(
					getReduction(-1d, TypeReduction.CONTRAT, TypeValeurReduction.MONTANT, 1, null, null), "REF_GB", 1);
			fail("Test fail.");
		} catch (TopazeException e) {
			assertEquals("1.1.52", e.getErrorCode());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Tester le cas d'association d'une reduction de type euro avec une valeur non valid.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ajouter-reduction.xml" })
	public void testAjouterReductionWithNbUtilisationNegative() {

		try {
			reductionService.ajouterReductionElementContractuelle(
					getReduction(5d, TypeReduction.CONTRAT, TypeValeurReduction.MONTANT, -1, null, null), "REF_GB", 1);
			fail("Test fail.");
		} catch (TopazeException e) {
			assertEquals("0.1.10", e.getErrorCode());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Tester le cas d'association d'une reduction de type euro avec une valeur non valid.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ajouter-reduction.xml" })
	public void testAjouterReductionWithNbUtilisationZero() {

		try {
			reductionService.ajouterReductionElementContractuelle(
					getReduction(5d, TypeReduction.CONTRAT, TypeValeurReduction.MONTANT, 0, null, null), "REF_GB", 1);
			fail("Test fail.");
		} catch (TopazeException e) {
			assertEquals("0.1.10", e.getErrorCode());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Tester le cas d'association d'une reduction de type euro avec une valeur non valid.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ajouter-reduction.xml" })
	public void testAjouterReductionWithDateDebutAfterDateFin() {

		try {
			Calendar c = Calendar.getInstance();
			Date dateFin = c.getTime();
			c.add(Calendar.DAY_OF_YEAR, 20);
			Date dateDebut = c.getTime();
			reductionService.ajouterReductionElementContractuelle(
					getReduction(5d, TypeReduction.CONTRAT, TypeValeurReduction.MONTANT, null, dateDebut, dateFin),
					"REF_GB", 1);
			fail("Test fail.");
		} catch (TopazeException e) {
			assertEquals("1.1.60", e.getErrorCode());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Tester le cas d'association d'une reduction de type euro avec une valeur non valid.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ajouter-reduction.xml" })
	public void testAjouterReductionWithPeriodNonValid() {

		try {
			Calendar c = Calendar.getInstance();
			Date dateFin = c.getTime();
			reductionService.ajouterReductionElementContractuelle(
					getReduction(5d, TypeReduction.CONTRAT, TypeValeurReduction.MONTANT, null, null, dateFin),
					"REF_GB", 1);
			fail("Test fail.");
		} catch (TopazeException e) {
			assertEquals("1.1.60", e.getErrorCode());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Tester le cas d'association d'une reduction de type euro avec une valeur non valid.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ajouter-reduction.xml" })
	public void testAjouterReductionWithPeriodNullAndNbUtilisationNull() {

		try {
			reductionService
					.ajouterReductionElementContractuelle(
							getReduction(5d, TypeReduction.CONTRAT, TypeValeurReduction.MONTANT, null, null, null),
							"REF_GB", 1);
			fail("Test fail.");
		} catch (TopazeException e) {
			assertEquals("1.1.59", e.getErrorCode());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Tester le cas d'association d'une resuction a un element deja en reduction.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ajouter-reduction.xml" })
	public void testAjouterReductionWithElementEnReductionWithoutOrdre() {

		try {
			reductionService.ajouterReductionContrat(
					getReduction(100d, TypeReduction.CONTRAT, TypeValeurReduction.MONTANT, 1, null, null), "REF_GB2");
			fail("Test fail.");
		} catch (TopazeException e) {
			assertEquals("1.1.144", e.getErrorCode());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Tester le cas d'association d'une resuction a un contrat de vente.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ajouter-reduction.xml" })
	public void testAjouterReductionContratVente() {

		try {
			reductionService.ajouterReductionElementContractuelle(
					getReduction(11d, TypeReduction.CONTRAT, TypeValeurReduction.MOIS, 1, null, null), "REF_GB", 3);
			fail("Test fail.");
		} catch (TopazeException e) {
			assertEquals("1.1.58", e.getErrorCode());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Tester le cas d'association d'une resuction a un {@link Contrat} avec une valeur non valid.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ajouter-reduction.xml" })
	public void testAjouterReductionContratWithValeurNonValid() {

		try {
			reductionService.ajouterReductionContrat(
					getReduction(11d, TypeReduction.CONTRAT, TypeValeurReduction.MOIS, 1, null, null), "REF_GB");
			fail("Test fail.");
		} catch (TopazeException e) {
			assertEquals("1.1.56", e.getErrorCode());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Tester le cas d'association d'une resuction a un {@link ElementContractuel} avec une valeur non valid.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ajouter-reduction.xml" })
	public void testAjouterReductionElementContractuelWithValeurNonValid() {

		try {
			reductionService.ajouterReductionElementContractuelle(
					getReduction(11d, TypeReduction.CONTRAT, TypeValeurReduction.MOIS, 1, null, null), "REF_GB", 2);
			fail("Test fail.");
		} catch (TopazeException e) {
			assertEquals("1.1.57", e.getErrorCode());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Tester le cas d'association d'une resuction a un {@link ElementContractuel} avec une valeur non valid.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ajouter-reduction.xml" })
	public void testAjouterReductionFraisWithTypeValeurMois() {

		try {
			reductionService.ajouterReductionContrat(
					getReduction(5d, TypeReduction.FRAIS, TypeValeurReduction.MOIS, 1, null, null), "REF_GB");
			fail("Test fail.");
		} catch (TopazeException e) {
			assertEquals("1.1.146", e.getErrorCode());
		} catch (Exception e) {
			// LOGGER.error(e);
			// fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Creer une reduction.
	 * 
	 * @param valeur
	 *            valeur de reduction.
	 * @param typeValeurReduction
	 *            {@link TypeValeurReduction}.
	 * @param nbUtilisationMax
	 *            nombre maximale d'utilisation.
	 * @param dateDebut
	 *            date debut reduction.
	 * @param dateFin
	 *            date fin reduction.
	 * @return {@link Reduction}
	 */
	private static ContratReduction getReduction(Double valeur, TypeReduction typeReduction,
			TypeValeurReduction typeValeurReduction, Integer nbUtilisationMax, Date dateDebut, Date dateFin) {
		Reduction reduction = new Reduction();
		reduction.setValeur(valeur);
		reduction.setTypeValeur(typeValeurReduction);
		reduction.setNbUtilisationMax(nbUtilisationMax);
		reduction.setTypeReduction(typeReduction);
		if (typeReduction.equals(TypeReduction.FRAIS)) {
			reduction.setTypeFrais(TypeFrais.CREATION);
		}
		reduction.setDateDebut(dateDebut);
		reduction.setIsAffichableSurFacture(true);
		reduction.setDateFin(dateFin);
		ContratReduction contratReduction = new ContratReduction();
		contratReduction.setUser(Constants.INTERNAL_USER);
		contratReduction.setReduction(reduction);
		return contratReduction;
	}

}
