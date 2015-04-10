package com.nordnet.topaze.migration.outil.enums;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Definir notre propre logique de deserialisation.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class ModeFacturationDeserializer extends JsonDeserializer<ModeFacturation> {

	@Override
	public ModeFacturation deserialize(JsonParser parser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		return ModeFacturation.fromString(parser.getText().toUpperCase());
	}

}
