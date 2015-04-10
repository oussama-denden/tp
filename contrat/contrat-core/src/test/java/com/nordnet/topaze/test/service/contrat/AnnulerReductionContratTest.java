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
 * Class test de la methode {@link ReductionService#annulerReductionContrat(String, Integer)}
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class AnnulerReductionContratTest extends GlobalTestCase {

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
	public void annulerReductinContratValide() throws TopazeException {
		Reduction reduction = null;
		Contrat contrat = contratService.findByReference("REF_GB");
		reduction = reductionRepository.findOne(1);
		assertFalse(reduction.isAnnule());
		reductionService.annulerReductionContrat(contrat.getReference(), "REF_RED-1");
		reduction = reductionRepository.findOne(1);
		assertTrue(reduction.isAnnule());

	}

	/**
	 * Tester l'annulation d'une
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/Annuler-reduction.xml" })
	public void annulerReductinContratInValide() {
		Reduction reduction = null;
		Contrat contrat = contratService.findByReference("REF_GB");
		reduction = reductionRepository.findOne(2);
		assertFalse(reduction.isAnnule());
		try {
			reductionService.annulerReductionContrat(contrat.getReference(), "REF_RED-2");
			fail("comportement inattendu");
		} catch (TopazeException topazeException) {
			assertEquals(topazeException.getErrorCode(), "1.1.134");
		}

	}

}
