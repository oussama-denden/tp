package com.nordnet.topaze.finder.contrat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.finder.business.welcome.WelcomeContratInfo;
import com.nordnet.topaze.finder.service.ContratService;
import com.nordnet.topaze.finder.test.GlobalTestCase;

/**
 * This class tests {@link ContratService#getWelcomeContratInfo(String)}.
 * 
 * @author Oussama Denden
 * 
 */
public class GetWelcomeContratInfoTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(GetWelcomeContratInfoTest.class);

	/**
	 * The topaze client.
	 */
	@Autowired
	ContratService contratService;

	/**
	 * Test execute.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/getWelcomeContratInfoTest.xml" })
	public void testGetWelcomeContratInfoValide() {

		try {
			Date d1 = new Date();
			WelcomeContratInfo welcomeContratInfo = contratService.getWelcomeContratInfo("00010000");
			Date d2 = new Date();
			System.out.println("Find All Contrat in " + (d2.getTime() - d1.getTime()) + "ms");
			assertTrue(welcomeContratInfo.isFrais());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Test execute.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/getWelcomeContratInfoTest.xml" })
	public void testGetWelcomeContratInfoNotExit() {

		try {
			contratService.getWelcomeContratInfo("NOT_EXIT");
			fail("Test fail.");
		} catch (TopazeException e) {
			assertEquals("4.1.2", e.getErrorCode());
		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

}
