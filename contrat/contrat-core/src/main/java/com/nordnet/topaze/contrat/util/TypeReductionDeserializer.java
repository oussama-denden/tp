package com.nordnet.topaze.contrat.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nordnet.topaze.contrat.domain.TypeReduction;

/**
 * Definir notre propre logique de deserialisation.
 * 
 * @author akram-moncer
 * 
 */
public class TypeReductionDeserializer extends JsonDeserializer<TypeReduction> {

	@Override
	public TypeReduction deserialize(JsonParser parser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		return TypeReduction.fromString(parser.getText().toUpperCase());
	}

}
