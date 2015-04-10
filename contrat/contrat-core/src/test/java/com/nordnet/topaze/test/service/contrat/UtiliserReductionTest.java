package com.nordnet.topaze.test.service.contrat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.domain.Reduction;
import com.nordnet.topaze.contrat.repository.ContratRepository;
import com.nordnet.topaze.contrat.repository.ElementContractuelRepository;
import com.nordnet.topaze.contrat.service.ReductionService;
import com.nordnet.topaze.contrat.test.GlobalTestCase;
import com.nordnet.topaze.contrat.util.Constants;

/**
 * Classe de test de la methode {@link ReductionService#utiliserReduction(String)}.
 * 
 * @author akram-moncer
 * 
 */
public class UtiliserReductionTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Log LOGGER = LogFactory.getLog(AjouterReductionTest.class);

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
	 * Tester l'utilisation d'une reduction pour un contrat global.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/utiliser-reduction.xml" })
	public void testUtiliserReductionContrat() {

		try {
			reductionService.utiliserReduction("REF_GB", 0);
			Contrat contrat = contratRepository.findByReference("REF_GB");
			List<Reduction> reductionGlobales = reductionService.findReductionGlobales(contrat.getReference(), null);
			for (Reduction reductionGlobal : reductionGlobales) {
				assertEquals(Constants.UN, reductionGlobal.getNbUtilisationEnCours());
			}
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * Tester l'utilisation d'une reduction pour un element contractuel.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/utiliser-reduction.xml" })
	public void testUtiliserReductionElementContractuel() {

		try {
			reductionService.utiliserReduction("REF_GB", 1);
			ElementContractuel elementContractuel =
					elementContractuelRepository.findByNumECAndReferenceContrat(1, "REF_GB");
			List<Reduction> reductionGlobales =
					reductionService.findReductionGlobales(elementContractuel.getContratParent().getReference(), null);
			for (Reduction reductionGlobal : reductionGlobales) {
				assertEquals(Constants.UN, reductionGlobal.getNbUtilisationEnCours());
			}
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}
}
