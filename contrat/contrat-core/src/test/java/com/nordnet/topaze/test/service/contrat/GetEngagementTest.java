package com.nordnet.topaze.test.service.contrat;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.contrat.service.ContratService;
import com.nordnet.topaze.contrat.test.GlobalTestCase;
import com.nordnet.topaze.exception.TopazeException;

/**
 * This class tests {@link ContratService#getEngagement(String)}.
 * 
 * @author Oussama Denden
 * 
 */
public class GetEngagementTest extends GlobalTestCase {

	/**
	 * The service used for tests.
	 */
	@Autowired
	private ContratService contratService;

	/**
	 * getEngagement test.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/test-topaze-contrat-engagement.xml" })
	public void testGetEngagement() {

		try {

			Integer maxEngagement = contratService.getEngagement("REF_GB");

			assertEquals(maxEngagement, new Integer(12));

		} catch (TopazeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
