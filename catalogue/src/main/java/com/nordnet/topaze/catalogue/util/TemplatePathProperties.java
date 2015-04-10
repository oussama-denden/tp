package com.nordnet.topaze.catalogue.util;

import java.util.Properties;

import com.nordnet.topaze.catalogue.utils.spring.ApplicationContextHolder;

/**
 * recupere les parametre du fichier template-path.properties.
 * 
 * @author akram-moncer
 * 
 */
public class TemplatePathProperties {

	/**
	 * Instance unique de la classe.
	 */
	private static TemplatePathProperties instance;

	/**
	 * private constructor.
	 */
	private TemplatePathProperties() {

	}

	/**
	 * @return a single instance of the {@link TemplatePathProperties}.
	 */
	public static TemplatePathProperties getInstance() {
		if (instance == null)
			instance = new TemplatePathProperties();
		return instance;
	}

	/**
	 * template path properties file.
	 */
	private Properties templatePathProperties = ApplicationContextHolder.getBean("templatePathProperties");

	/**
	 * 
	 * @return path du fichier qui contient les templates xml.
	 */
	public String getXMLTemplatePath() {
		return templatePathProperties.getProperty("TEMPLATE_XML_PATH");
	}

	/**
	 * 
	 * @return path du fichier qui contient les templates xsd.
	 */
	public String getXSDTemplatePath() {
		return templatePathProperties.getProperty("TEMPLATE_XSD_PATH");
	}
}
