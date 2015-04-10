package com.nordnet.topaze.test.service.contrat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.Reduction;
import com.nordnet.topaze.contrat.repository.ReductionRepository;
import com.nordnet.topaze.contrat.service.ContratService;
import com.nordnet.topaze.contrat.service.ReductionService;
import com.nordnet.topaze.contrat.test.GlobalTestCase;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Class test de la methode {@link ReductionService#annulerReductionElementContractuelle(String, Integer, Integer)}
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class AnnulerReductionElementContractuelleTest extends GlobalTestCase {

	/**
	 * {@link ContratService}.
	 */
	@Autowired
	private ContratService contratService;

	/**
	 * {@link ReductionService}.
	 */
	@Autowired
	private ReductionService reductionService;

	/**
	 * {@link ReductionRepository}
	 */
	@Autowired
	private ReductionRepository reductionRepository;

	/**
	 * Tester l'annulation d'une
	 * 
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/Annuler-reduction.xml" })
	public void annulerReductinElementContractuelleValide() throws TopazeException {
		Reduction reduction = null;
		Contrat contrat = contratService.findByReference("REF_GB");
		reduction = reductionRepository.findOne(3);
		assertFalse(reduction.isAnnule());
		reductionService.annulerReductionElementContractuelle(contrat.getReference(), 1, "REF_RED-3");
		reduction = reductionRepository.findOne(3);
		assertTrue(reduction.isAnnule());

	}

	/**
	 * Tester l'annulation d'une
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/Annuler-reduction.xml" })
	public void annulerReductinElementContractuelleInValide() {
		Reduction reduction = null;
		Contrat contrat = contratService.findByReference("REF_GB");
		reduction = reductionRepository.findOne(4);
		assertFalse(reduction.isAnnule());
		try {
			reductionService.annulerReductionElementContractuelle(contrat.getReference(), 2, "REF_RED-4");
			fail("comportement inattendu");
		} catch (TopazeException topazeException) {
			assertEquals(topazeException.getErrorCode(), "1.1.134");
		}

	}

}
