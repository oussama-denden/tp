package com.nordnet.topaze.resiliation.outil.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.springframework.context.MessageSource;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.resiliation.outil.util.spring.ApplicationContextHolder;

/**
 * Singleton to handel topaze.proerties file.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class PropertiesUtil {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(PropertiesUtil.class);

	/**
	 * Instance unique de la classe.
	 */
	private static PropertiesUtil instance;

	/**
	 * Rest URL properties file.
	 */
	private Properties topazeExceptionsProperties = ApplicationContextHolder.getBean("topazeExceptionsProperties");

	/**
	 * Dynamic properties file.
	 */
	private MessageSource dynamicProperties = ApplicationContextHolder.getBean("dynamicProperties");

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
	 * @return date de jour definit dans la fichier env.properties.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public LocalDate getDateDuJour() throws TopazeException {
		String dateDuJourString =
				dynamicProperties.getMessage(Constants.DATE_DU_JOUR_PROPERTY, null,
						Constants.DEFAULT_DATE_FORMAT.format(new Date()), null);
		if (Utils.isStringNullOrEmpty(dateDuJourString) || dateDuJourString.equals(Constants.NOW)
				|| System.getProperty(Constants.ENV_PROPERTY).equals(Constants.PROD_ENV)) {
			return new LocalDate();
		}
		try {
			SimpleDateFormat formatter = Constants.DEFAULT_DATE_WITHOUT_TIME_FORMAT;
			Date dateDuJour = formatter.parse(dateDuJourString);
			return new LocalDate(dateDuJour);
		} catch (Exception e) {
			LOGGER.error("Error to format date", e);
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.1"), "0.1");
		}
	}
}
