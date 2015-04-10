package com.nordnet.topaze.contrat.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nordnet.topaze.contrat.domain.TypeOperation;

/**
 * Definir notre propre logique de deserialisation.
 * 
 * @author anisselmane.
 * 
 */
public class TypeOperationDeserializer extends JsonDeserializer<TypeOperation> {

	@Override
	public TypeOperation deserialize(JsonParser parser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		return TypeOperation.fromString(parser.getText().toUpperCase());
	}

}
