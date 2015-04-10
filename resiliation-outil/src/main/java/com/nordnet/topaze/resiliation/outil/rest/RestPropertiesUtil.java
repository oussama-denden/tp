package com.nordnet.topaze.resiliation.outil.rest;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Singleton to handel topaze.proerties file.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Component("restPropertiesUtil")
public class RestPropertiesUtil {

	/**
	 * Rest URL properties file.
	 */
	@Autowired
	private Properties restURLProperties;

	/**
	 * private constructor.
	 */
	private RestPropertiesUtil() {

	}

	/**
	 * @param briqueURL
	 *            url du brique.
	 * @param methode
	 *            nome du methode.
	 * @param params
	 *            les parametres de methode.
	 * @return l'url de la service REST.
	 */
	public String getRestURL(String briqueURL, String methode, Object... params) {
		return briqueURL + String.format(restURLProperties.getProperty(methode), params);
	}

	/**
	 * @param restURLProperties
	 *            restURLProperties.
	 */
	public void setRestURLProperties(Properties restURLProperties) {
		this.restURLProperties = restURLProperties;
	}

	/**
	 * Return Path.
	 * 
	 * @param methode
	 *            methode name.
	 * @param params
	 *            params
	 * @return path.
	 */
	public String getPath(String methode, Object... params) {
		return String.format(restURLProperties.getProperty(methode), params);
	}

}
