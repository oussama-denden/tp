package com.nordnet.topaze.businessprocess.packager.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class InitVariables.
 */
public class InitEnveronement implements ServletContextListener {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(InitEnveronement.class);

	@Override
	public void contextDestroyed(final ServletContextEvent event) {
		LOGGER.info("context destroyed");
	}

	@Override
	public void contextInitialized(final ServletContextEvent event) {

		final String props = new File(System.getProperty("catalina.base")) + "/webapps/env.properties";

		final Properties propsFromFile = new Properties();
		try {
			final FileInputStream in = new FileInputStream(props);
			propsFromFile.load(in);
		} catch (final IOException e) {
			LOGGER.error("Le fichier env.properties n'existe pas sous le serveur", e);
		}
		for (String prop : propsFromFile.stringPropertyNames()) {
			if (System.getProperty(prop) == null) {
				System.setProperty(prop, propsFromFile.getProperty(prop));
			}
		}
	}
}
