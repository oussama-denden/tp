package com.nordnet.topaze.livraison.core.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nordnet.topaze.livraison.core.domain.TypeContrat;

/**
 * Definir notre propre logique de deserialisation.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class TypeContratDeserializer extends JsonDeserializer<TypeContrat> {

	@Override
	public TypeContrat deserialize(JsonParser parser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		return TypeContrat.fromString(parser.getText().toUpperCase());
	}

}
