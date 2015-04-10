package com.nordnet.topaze.test.service.contrat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.contrat.business.ContratMigrationInfo;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ContratHistorique;
import com.nordnet.topaze.contrat.repository.ContratHistoriqueRepository;
import com.nordnet.topaze.contrat.service.ContratService;
import com.nordnet.topaze.contrat.service.MigrationService;
import com.nordnet.topaze.contrat.test.GlobalTestCase;
import com.nordnet.topaze.contrat.test.generator.MigrationInfoGenerator;
import com.nordnet.topaze.exception.TopazeException;

/**
 * This class tests
 * {@link MigrationService#migrerContrat(String, com.nordnet.topaze.contrat.business.ContratMigrationInfo)}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class MigrerContratTest extends GlobalTestCase {

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
	 * tester la migration d'un contrat.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/contrat-migration.xml" })
	public void TestMigrerContratValid() {

		try {
			Contrat contrat = contratService.findByReference("REF_GB_migre");
			assertNotNull(contrat);
			ContratMigrationInfo contratMigrationInfo =
					migrationInfoGenerator.getObjectFromJsonFile(ContratMigrationInfo.class,
							"./requests/contratMigrationInfo.json");
			migrationService.migrerContrat(contrat.getReference(), contratMigrationInfo);
			ContratHistorique contratHistorique = contratHistoriqueRepository.findDerniereVersion("REF_GB_migre");
			assertNotNull(contratHistorique);
			assertEquals(contratHistorique.getSousContrats().size(), 1);

		} catch (Exception exception) {
			LOGGER.error("erreur dans MigrerContratTest");
			fail("unexpected state");
		}

	}

	/**
	 * tester la migration d'un contrat.
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws CloneNotSupportedException
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/contrat-migration.xml" })
	public void TestMigrerContratInvalid() {

		try {
			Contrat contrat = contratService.findByReference("REF_GB_2");
			assertNotNull(contrat);
			ContratMigrationInfo contratMigrationInfo =
					migrationInfoGenerator.getObjectFromJsonFile(ContratMigrationInfo.class,
							"./requests/contratMigrationInfo.json");
			migrationService.migrerContrat(contrat.getReference(), contratMigrationInfo);
			// fail("unexpected state");

		} catch (Exception exception) {
			LOGGER.error("erreur dans MigrerContratTest");
			if (exception instanceof TopazeException) {
				assertEquals(((TopazeException) exception).getErrorCode(), "1.1.74");
			} else {
				LOGGER.error("erreur dans MigrerContratTest");
				fail("unexpected state");
			}
		}

	}

}