package com.nordnet.topaze.test.service.contrat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.contrat.business.ContratValidationInfo;
import com.nordnet.topaze.contrat.business.PaiementInfo;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.domain.ElementContractuel;
import com.nordnet.topaze.contrat.domain.PolitiqueValidation;
import com.nordnet.topaze.contrat.service.ContratService;
import com.nordnet.topaze.contrat.test.GlobalTestCase;

/**
 * The Class ValiderContratTest.
 */
public class ValiderContratTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Log LOGGER = LogFactory.getLog(ResilierContratParNordNetTest.class);

	/**
	 * The service used for tests.
	 */
	@Autowired
	private ContratService contratService;

	/**
	 * Tester la validation d'un contrat.
	 * <p>
	 * Entering data :
	 * <ul>
	 * <p>
	 * </p>
	 * </ul>
	 * <p>
	 * Expected result :
	 * <ul>
	 * <li>Nothing</li>
	 * </ul>
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/valider-contrat.xml" })
	public void testValiderContratValid() {
		try {
			ContratValidationInfo validationInfo = new ContratValidationInfo();
			validationInfo.setIdAdrFacturation("address_facturation");
			validationInfo.setIdClient("id_client");

			ArrayList<PaiementInfo> paiementInfos = new ArrayList<>();

			PaiementInfo paiementInfo1 = new PaiementInfo();
			paiementInfo1.setIdAdrLivraison("idAdrLivr_1");
			paiementInfo1.setNumEC(1);
			paiementInfo1.setReferenceModePaiement("referenceModePaiement_1");
			paiementInfo1.setReferenceProduit("PROD-01");
			paiementInfos.add(paiementInfo1);

			PaiementInfo paiementInfo2 = new PaiementInfo();
			paiementInfo2.setIdAdrLivraison("idAdrLivr_2");
			paiementInfo2.setNumEC(2);
			paiementInfo2.setReferenceModePaiement("referenceModePaiement_2");
			paiementInfo2.setReferenceProduit("PROD-01");
			paiementInfos.add(paiementInfo2);

			PaiementInfo paiementInfo3 = new PaiementInfo();
			paiementInfo3.setIdAdrLivraison("idAdrLivr_3");
			paiementInfo3.setReferenceModePaiement("referenceModePaiement_3");
			paiementInfo3.setReferenceProduit("PROD-02");
			paiementInfos.add(paiementInfo3);

			PaiementInfo paiementInfo4 = new PaiementInfo();
			paiementInfo4.setIdAdrLivraison("idAdrLivr_4");
			paiementInfo4.setReferenceModePaiement("referenceModePaiement_4");
			paiementInfo4.setReferenceProduit("PROD-03");
			paiementInfos.add(paiementInfo4);

			validationInfo.setPaiementInfos(paiementInfos);
			validationInfo.setPolitiqueValidation(new PolitiqueValidation());
			Contrat contratGlobal = contratService.getContratByReference("REF_GB");

			assertTrue(contratGlobal.isPreparer());
			assertFalse(contratGlobal.isValider());
			assertFalse(contratGlobal.isResilier());
			assertNull(contratGlobal.getIdClient());
			for (ElementContractuel sousContrat : contratGlobal.getSousContrats()) {
				assertNull(sousContrat.getIdAdrFacturation());
				assertNull(sousContrat.getIdAdrLivraison());
			}

			contratService.validerContrat("REF_GB", validationInfo);

			contratGlobal = contratService.getContratByReference("REF_GB");

			assertFalse(contratGlobal.isPreparer());
			assertTrue(contratGlobal.isValider());
			assertFalse(contratGlobal.isResilier());
			assertEquals(contratGlobal.getIdClient(), "id_client");

			for (ElementContractuel sousContrat : contratGlobal.getSousContrats()) {
				assertEquals(sousContrat.getIdAdrFacturation(), "address_facturation");
				assertFalse(sousContrat.isPreparer());
				assertTrue(sousContrat.isValider());
				assertFalse(sousContrat.isResilier());
				if (sousContrat.getReferenceProduit().equals("PROD-01") && sousContrat.getNumEC() == 1) {
					assertEquals(sousContrat.getIdAdrLivraison(), "idAdrLivr_1");
					assertEquals(sousContrat.getReferenceModePaiement(), "referenceModePaiement_1");

				}

				if (sousContrat.getReferenceProduit().equals("PROD-01") && sousContrat.getNumEC() == 2) {
					assertEquals(sousContrat.getIdAdrLivraison(), "idAdrLivr_2");
					assertEquals(sousContrat.getReferenceModePaiement(), "referenceModePaiement_2");
				}

				if (sousContrat.getReferenceProduit().equals("PROD-02")) {
					assertEquals(sousContrat.getIdAdrLivraison(), "idAdrLivr_3");
					assertEquals(sousContrat.getReferenceModePaiement(), "referenceModePaiement_3");
				}

				if (sousContrat.getReferenceProduit().equals("PROD-03")) {
					assertEquals(sousContrat.getIdAdrLivraison(), "idAdrLivr_4");
					assertEquals(sousContrat.getReferenceModePaiement(), "referenceModePaiement_4");
				}

			}

		} catch (Exception e) {
			LOGGER.error(e);

		}

	}
}
