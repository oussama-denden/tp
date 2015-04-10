package com.nordnet.topaze.finder.contrat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.finder.business.welcome.WelcomeAbonnementInfo;
import com.nordnet.topaze.finder.service.ContratService;
import com.nordnet.topaze.finder.test.GlobalTestCase;

/**
 * This class tests {@link TopazeClient#execute(String)}.
 * 
 * @author anisselmane.
 * 
 */
public class GetWelcomeAbonnementsClient extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(GetWelcomeAbonnementsClient.class);

	/**
	 * The topaze client.
	 */
	@Autowired
	ContratService contratService;

	/**
	 * Test execute.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/findContratTest.xml" })
	public void testGetWelcomeAbonnementsClientValide() {

		try {
			Date d1 = new Date();
			List<WelcomeAbonnementInfo> contrats = contratService.getWelcomeAbonnementsClient("10000");
			Date d2 = new Date();
			System.out.println("Find All Contrat in " + (d2.getTime() - d1.getTime()) + "ms");
			assertEquals(6, contrats.size());

		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}
}
