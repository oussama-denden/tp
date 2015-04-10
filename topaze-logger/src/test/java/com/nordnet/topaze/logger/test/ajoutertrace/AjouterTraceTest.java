package com.nordnet.topaze.logger.test.ajoutertrace;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.nordnet.topaze.logger.domain.Tracage;
import com.nordnet.topaze.logger.service.TracageService;
import com.nordnet.topaze.logger.test.GlobalTestCase;

/**
 * This class tests {@link TopazeClient#execute(String)}.
 * 
 * @author anisselmane.
 * 
 */
public class AjouterTraceTest extends GlobalTestCase {

	/**
	 * The topaze client.
	 */
	@Autowired
	TracageService tracageService;

	/**
	 * Test execute.
	 */
	@Test
	@DatabaseSetup(value = { "/dataset/emptyDB.xml", "/dataset/ajouterTrace-valide.xml" })
	public void testAjouterTraceValide() {

		try {
			tracageService.ajouterTrace(Tracage.class.getSimpleName(), "ref contrat", "Contrat yyy préparé", "user");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
