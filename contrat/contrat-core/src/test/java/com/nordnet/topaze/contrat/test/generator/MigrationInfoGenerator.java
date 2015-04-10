package com.nordnet.topaze.contrat.test.generator;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nordnet.topaze.client.rest.business.livraison.ContratMigrationInfo;

/**
 * * classe pour generer des info a stocker dans un {@link ContratMigrationInfo}pour un contrat.
 * 
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
@Component("migrationInfoGenerator")
public class MigrationInfoGenerator {

	/**
	 * Retourne une {@link ContratMigrationInfo}.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param valueType
	 *            the value type
	 * @param jsonFilePath
	 *            the json file path
	 * @return {@link DraftLigneInfo}.
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public <T> T getObjectFromJsonFile(Class<T> valueType, String jsonFilePath)
			throws JsonParseException, JsonMappingException, IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File json = new File(classLoader.getResource(jsonFilePath).getFile());
		return new ObjectMapper().readValue(json, valueType);
	}

}
