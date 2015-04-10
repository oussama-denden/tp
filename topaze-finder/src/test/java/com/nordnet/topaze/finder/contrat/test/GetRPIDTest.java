package com.nordnet.topaze.finder.contrat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.finder.service.ContratService;
import com.nordnet.topaze.finder.test.GlobalTestCase;

/**
 * This class tests {@link ContratService#getWelcomeContratInfo(String)}.
 * 
 * @author Oussama Denden
 * 
 */
public class GetRPIDTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(GetRPIDTest.class);

	/**
	 * The topaze client.
	 */
	@Autowired
	ContratService contratService;

	/**
	 * Test execute.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/getRPIDTest.xml" })
	public void testGetRPIDValide() {

		try {
			String rpid = contratService.getRPID("00010000");
			assertEquals(rpid, "RPID");

		} catch (Exception e) {
			LOGGER.error(e);
			fail("Unexpected exception : " + e.getMessage());
		}

	}

}
