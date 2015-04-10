package com.nordnet.topaze.test.service.contrat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.contrat.business.ContratCession;
import com.nordnet.topaze.contrat.domain.Avenant;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ContratHistorique;
import com.nordnet.topaze.contrat.domain.TypeAvenant;
import com.nordnet.topaze.contrat.repository.ContratHistoriqueRepository;
import com.nordnet.topaze.contrat.service.AvenantService;
import com.nordnet.topaze.contrat.service.ContratService;
import com.nordnet.topaze.contrat.service.MigrationService;
import com.nordnet.topaze.contrat.test.GlobalTestCase;
import com.nordnet.topaze.contrat.test.utils.ContratCessionUtils;
import com.nordnet.topaze.contrat.util.Constants;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Class test de la methode
 * {@link MigrationService#cederContrat(String, com.nordnet.topaze.contrat.business.ContratCessionInfo)}.
 * 
 * @author akram-moncer
 * 
 */
public class CederContratTest extends GlobalTestCase {

	/**
	 * declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(CederContratTest.class);

	/**
	 * {@link ContratService}.
	 */
	@Autowired
	private ContratService contratService;

	/**
	 * {@link MigrationService}.
	 */
	@Autowired
	private MigrationService migrationService;

	/**
	 * {@link AvenantService}.
	 */
	@Autowired
	private AvenantService avenantService;

	/**
	 * {@link ContratHistoriqueRepository}.
	 */
	@Autowired
	private ContratHistoriqueRepository contratHistoriqueRepository;

	/**
	 * teste de la cession d'un {@link Contrat}.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ceder-contrat.xml" })
	public void cederContratValid() {

		ContratCession contratCessionInfo = ContratCessionUtils.creerContratCessionInfo("idCL", "idCL01", null);
		try {
			migrationService.cederContrat("REF_GB_cede", contratCessionInfo);
			ContratHistorique contratHistorique = contratHistoriqueRepository.findDerniereVersion("REF_GB_cede");
			assertTrue(contratHistorique.getId() > Constants.ZERO);
			Avenant avenant =
					avenantService.findByReferenceContratAndVersionAndTypeAvenant("REF_GB_cede",
							contratHistorique.getVersion(), TypeAvenant.CESSION);
			Contrat contrat = contratService.findByReference("REF_GB_cede");
			assertEquals(contratHistorique.getIdClient(), "idCL");
			assertTrue(avenant.getId() > Constants.ZERO);
			assertTrue(avenant.getPolitiqueCession().getId() > Constants.ZERO);
			assertEquals(contrat.getIdClient(), "idCL01");
		} catch (TopazeException | JsonProcessingException e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

	/**
	 * tester le cas ou la dateAction est dans le futur.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ceder-contrat.xml" })
	public void cederContratAvecDateActionFutur() {
		try {
			ContratCession contratCessionInfo =
					ContratCessionUtils.creerContratCessionInfo("idCL", "idCL01", PropertiesUtil.getInstance()
							.getDateDuJour().plusMonths(Constants.UN).toDate());
			migrationService.cederContrat("REF_GB_cede", contratCessionInfo);
			Avenant avenant =
					avenantService.findByReferenceContratAndVersionAndTypeAvenant("REF_GB_cede", null,
							TypeAvenant.CESSION);
			Contrat contrat = contratService.findByReference("REF_GB_cede");
			ContratHistorique contratHistorique = contratHistoriqueRepository.findDerniereVersion("REF_GB_cede");
			assertNull(contratHistorique);
			assertTrue(avenant.getId() > Constants.ZERO);
			assertTrue(avenant.getPolitiqueCession().getId() > Constants.ZERO);
			assertEquals(new LocalDate(avenant.getPolitiqueCession().getDateAction()), new LocalDate(PropertiesUtil
					.getInstance().getDateDuJour().plusMonths(Constants.UN)));
			assertEquals(contrat.getIdClient(), "idCL");
		} catch (TopazeException | JsonProcessingException e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}
	}

}
