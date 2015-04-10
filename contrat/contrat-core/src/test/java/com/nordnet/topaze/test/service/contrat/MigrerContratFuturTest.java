package com.nordnet.topaze.test.service.contrat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.contrat.business.ContratMigrationInfo;
import com.nordnet.topaze.contrat.domain.Avenant;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ContratHistorique;
import com.nordnet.topaze.contrat.repository.AvenantRepository;
import com.nordnet.topaze.contrat.repository.ContratHistoriqueRepository;
import com.nordnet.topaze.contrat.service.ContratService;
import com.nordnet.topaze.contrat.service.MigrationService;
import com.nordnet.topaze.contrat.test.GlobalTestCase;
import com.nordnet.topaze.contrat.test.generator.MigrationInfoGenerator;

/**
 * This class tests
 * {@link MigrationService#migrerContrat(String, com.nordnet.topaze.contrat.business.ContratMigrationInfo)}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class MigrerContratFuturTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Log LOGGER = LogFactory.getLog(ContratPreparationTest.class);

	/**
	 * {@link ContratService}
	 */
	@Autowired
	private ContratService contratService;

	/**
	 * {@link ContratService}
	 */
	@Autowired
	private MigrationService migrationService;

	/**
	 * {@link MigrationInfoGenerator}
	 */
	@Autowired
	private MigrationInfoGenerator migrationInfoGenerator;

	/**
	 * {@link ContratHistoriqueRepository}
	 */
	@Autowired
	private ContratHistoriqueRepository contratHistoriqueRepository;

	/**
	 * {@link AvenantRepository}
	 */
	@Autowired
	private AvenantRepository avenantRepository;

	/**
	 * tester la migration future.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/contrat-migration.xml" })
	public void migrerContratFuturTest() {

		try {
			Contrat contrat = contratService.findByReference("REF_GB_migre-2");
			assertNotNull(contrat);
			ContratMigrationInfo contratMigrationInfo =
					migrationInfoGenerator.getObjectFromJsonFile(ContratMigrationInfo.class,
							"./requests/contratMigrationInfo.json");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.YEAR, 1);
			Date dateAction = calendar.getTime();
			contratMigrationInfo.getPolitiqueMigration().setDateAction(dateAction);
			migrationService.migrerContrat(contrat.getReference(), contratMigrationInfo);
			ContratHistorique contratHistorique = contratHistoriqueRepository.findDerniereVersion("REF_GB_migre-2");
			assertNull(contratHistorique);
			Avenant avenant = avenantRepository.findAvenantAvecMigrationActive("REF_GB_migre-2");
			assertEquals(avenant.getModifContrat().size(), 1);

		} catch (Exception exception) {
			LOGGER.error("erreur dans MigrerContratTest");
			fail("unexpected state");
		}

	}

}
