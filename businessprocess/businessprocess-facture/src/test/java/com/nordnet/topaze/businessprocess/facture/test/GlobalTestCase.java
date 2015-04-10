package com.nordnet.topaze.businessprocess.facture.test;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * Abstract class for Test classes.
 * 
 * All test classes must inherit this class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContextTest.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@Ignore
public abstract class GlobalTestCase {

	/** Default logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalTestCase.class);

	/**
	 * Constructeur par défaut.
	 */
	public GlobalTestCase() {
		super();
		init();
	}

	/** Memory dump. */
	public static void afterClass() {
		LOGGER.info("Free memory  : " + Runtime.getRuntime().freeMemory() + " bytes");
		LOGGER.info("Max memory   : " + Runtime.getRuntime().maxMemory() + " bytes");
		LOGGER.info("Total memory : " + Runtime.getRuntime().totalMemory() + " bytes");
	}

	/**
	 * init methode.
	 */
	public void init() {
		System.setProperty("productId", "topaze");
		System.setProperty("ws.saphir.useMock", "true");
		System.setProperty("netcatalog.useMock", "true");
		System.setProperty("log.useMock", "true");
	}

}