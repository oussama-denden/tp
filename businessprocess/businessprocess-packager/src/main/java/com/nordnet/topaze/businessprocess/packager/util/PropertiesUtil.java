package com.nordnet.topaze.businessprocess.packager.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.springframework.context.MessageSource;

import com.nordnet.topaze.businessprocess.packager.util.spring.ApplicationContextHolder;
import com.nordnet.topaze.exception.TopazeException;

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
	 * Exceptions properties file.
	 */
	private Properties topazeExceptionsProperties = ApplicationContextHolder.getBean("topazeExceptionsProperties");

	/**
	 * Packager properties file.
	 */
	private Properties packagerProperties = ApplicationContextHolder.getBean("packagerProperties");

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
	 * @return packager properties.
	 */
	public Properties getPackagerProperties() {
		return packagerProperties;
	}

	/**
	 * @return LocalDate de jour definit dans la fichier env.properties.
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
			SimpleDateFormat formatter = Constants.DEFAULT_DATE_FORMAT;
			Date dateDuJour = formatter.parse(dateDuJourString);
			return new LocalDate(dateDuJour);
		} catch (Exception e) {
			LOGGER.error("Error to format date", e);
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.1"), "0.1");
		}
	}

	/**
	 * Recuperer le delai apres lequel un cancel packager sera effectuer.
	 * 
	 * @param referenceProduit
	 *            si null le default delai cancel sera appliquer.
	 * @return nombre de jours de delai.
	 */
	public Integer getDelaieCancel(String referenceProduit) {
		if (Utils.isStringNullOrEmpty(referenceProduit)) {
			Integer delaiCancel = Integer.valueOf(dynamicProperties.getMessage(Constants.DELAI_CANCEL, null, null));

			return delaiCancel;
		}
		Integer delaiCancel =
				Integer.valueOf(dynamicProperties.getMessage(referenceProduit + Constants.DELAI_CANCEL, null, null));

		return delaiCancel;
	}
}