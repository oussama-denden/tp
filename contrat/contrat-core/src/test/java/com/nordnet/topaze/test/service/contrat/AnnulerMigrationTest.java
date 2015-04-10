package com.nordnet.topaze.test.service.contrat;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.contrat.business.AnnulationInfo;
import com.nordnet.topaze.contrat.domain.Avenant;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.repository.AvenantRepository;
import com.nordnet.topaze.contrat.repository.ContratHistoriqueRepository;
import com.nordnet.topaze.contrat.service.ContratService;
import com.nordnet.topaze.contrat.service.MigrationService;
import com.nordnet.topaze.contrat.test.GlobalTestCase;
import com.nordnet.topaze.contrat.test.generator.MigrationInfoGenerator;

/**
 * This class tests
 * {@link MigrationService#annulerMigration(String, com.nordnet.topaze.contrat.business.AnnulationInfo))}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class AnnulerMigrationTest extends GlobalTestCase {

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
	 * annuler une migration
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/annuler-migration.xml" })
	public void annulerMigrationvalide() {
		try {
			Contrat contrat = contratService.findByReference("REF_GB_ANNULL");
			assertNotNull(contrat);
			AnnulationInfo annulationInfo =
					migrationInfoGenerator
							.getObjectFromJsonFile(AnnulationInfo.class, "./requests/annulationInfo.json");
			Avenant avenantNonAnnule = avenantRepository.findAvenantAvecMigrationActive("REF_GB_ANNULL");
			assertNotNull(avenantNonAnnule);
			assertNull(avenantNonAnnule.getDateAnnulation());
			migrationService.annulerMigration(contrat.getReference(), annulationInfo);

			Avenant avenantAnnule = avenantRepository.findByReferenceContratAndVersion("REF_GB_ANNULL", null);
			assertNotNull(avenantAnnule.getDateAnnulation());
		} catch (Exception exception) {
			LOGGER.error("erreur dans AnnulationMigrationTest");
			fail("unexpected state");
		}
	}

}
