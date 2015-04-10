package com.nordnet.topaze.test.service.contrat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.contrat.business.ReductionInterface;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.service.ReductionService;
import com.nordnet.topaze.contrat.test.GlobalTestCase;

/**
 * Class test de la method
 * {@link ReductionService#ajouterReduction(com.nordnet.topaze.contrat.domain.Reduction, String)}.
 * 
 * @author akram-moncer
 * 
 */
public class GetReductionValideTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(GetReductionValideTest.class);

	/**
	 * The service used for tests.
	 */
	@Autowired
	private ReductionService reductionService;

	/**
	 * Tester le cas d'association d'une resuction a un {@link Contrat} global.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/reductions-valide.xml" })
	public void testGetReductionValideTestValid() {

		try {
			List<ReductionInterface> reductionsInterface = reductionService.getReductionsValide("REF_CONTRAT");
			assertEquals(reductionsInterface.size(), 3);
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

}
