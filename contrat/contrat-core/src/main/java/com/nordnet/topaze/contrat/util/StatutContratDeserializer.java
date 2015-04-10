package com.nordnet.topaze.contrat.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nordnet.topaze.contrat.domain.StatutContrat;

/**
 * Definir notre propre logique de deserialisation.
 * 
 * @author akram-moncer
 * 
 */
public class StatutContratDeserializer extends JsonDeserializer<StatutContrat> {

	@Override
	public StatutContrat deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		return StatutContrat.fromString(parser.getText().toUpperCase());
	}

}
