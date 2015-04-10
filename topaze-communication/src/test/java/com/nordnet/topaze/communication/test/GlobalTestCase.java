package com.nordnet.topaze.communication.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.unitils.UnitilsJUnit4;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.spring.annotation.SpringApplicationContext;

import com.nordnet.topaze.communication.util.spring.ApplicationContextHolder;

/**
 * Abstract class for Test classes.
 * <p>
 * All test classes must inherit this class.
 * 
 * @author anisselmane.
 * 
 */
@SpringApplicationContext(value = { "applicationContextTest.xml" })
@Transactional(TransactionMode.DISABLED)
public abstract class GlobalTestCase extends UnitilsJUnit4 {

	/**
	 * Logger de la classe.
	 */
	private static final Log LOGGER = LogFactory.getLog(GlobalTestCase.class);

	/**
	 * Constructeur par défaut.
	 */
	public GlobalTestCase() {
		super();
		init();
	}

	/**
	 * Spring context.
	 */
	public static void beforeClass() {

		LOGGER.info("Spring context : " + ApplicationContextHolder.getContext());
	}

	/**
	 * Memory dump.
	 */
	public static void afterClass() {

		LOGGER.info("Free memory  : " + Runtime.getRuntime().freeMemory() + " bytes");
		LOGGER.info("Max memory   : " + Runtime.getRuntime().maxMemory() + " bytes");
		LOGGER.info("Total memory : " + Runtime.getRuntime().totalMemory() + " bytes");
	}

	/**
	 * init methode.
	 */
	public void init() {
		System.setProperty("ws.netEquipment.useMock", "true");

	}

}