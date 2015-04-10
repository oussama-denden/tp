package com.nordnet.topaze.contrat.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nordnet.topaze.contrat.domain.Actions;

/**
 * Definir notre propre logique de désérialisation.
 * 
 * @author anisselmane.
 * 
 */
public class ActionsDeserializer extends JsonDeserializer<Actions> {

	@Override
	public Actions deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		return Actions.fromString(parser.getText().toUpperCase());
	}

}
