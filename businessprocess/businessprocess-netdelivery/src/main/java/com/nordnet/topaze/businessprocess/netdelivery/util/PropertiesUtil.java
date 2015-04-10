package com.nordnet.topaze.businessprocess.netdelivery.util;

import java.util.Properties;

import com.nordnet.topaze.businessprocess.netdelivery.util.spring.ApplicationContextHolder;

/**
 * Singleton to handel topaze.proerties file.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class PropertiesUtil {

	/**
	 * Instance unique de la classe.
	 */
	private static PropertiesUtil instance;

	/**
	 * Rest URL properties file.
	 */
	private Properties topazeExceptionsProperties = ApplicationContextHolder.getBean("topazeExceptionsProperties");

	/**
	 * Packager properties file.
	 */
	private Properties packagerProperties = ApplicationContextHolder.getBean("netDeliveryProperties");

	/**
	 * private constructor.
	 */
	private PropertiesUtil() {

	}

	/**
	 * @return a single instance of the {@link PropertiesUtil}.
	 */
	public static PropertiesUtil getInstance() {
		if (instance == null)
			instance = new PropertiesUtil();
		return instance;
	}

	/**
	 * retourner un message d'erreur.
	 * 
	 * @param errorMessageKey
	 *            cle du message d'erreur.
	 * @param parameters
	 *            paramerers du message
	 * @return message d'erreur.
	 */
	public String getErrorMessage(String errorMessageKey, Object... parameters) {
		return String.format(topazeExceptionsProperties.getProperty(errorMessageKey), parameters);
	}

	/**
	 * chercher le code d'erreur a partir d'un fichier properties "error-code.properties".
	 * 
	 * @param errorCodeKey
	 *            cle de code erreur.
	 * @return code d'erreur.
	 */
	public String getErrorCode(String errorCodeKey) {
		return topazeExceptionsProperties.getProperty(errorCodeKey);
	}

	/**
	 * @return {@link #packagerProperties}.
	 */
	public Properties getPackagerProperties() {
		return packagerProperties;
	}
}