package com.nordnet.topaze.contrat.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nordnet.topaze.contrat.domain.TypeResiliation;

/**
 * Definir notre propre logique de deserialisation que sera utiliser par jackson lors de auto-population de
 * {@link TypeResiliation}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class TypeResiliationDeserializer extends JsonDeserializer<TypeResiliation> {

	@Override
	public TypeResiliation deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		return TypeResiliation.fromString(parser.getText().toUpperCase());
	}

}
