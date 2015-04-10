package com.nordnet.topaze.contrat.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nordnet.topaze.contrat.domain.MotifResiliation;

/**
 * Definir notre propre logique de deserialisation que sera utiliser par jackson lors de auto-population de
 * {@link MotifResiliation}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class MotifResiliationDeserializer extends JsonDeserializer<MotifResiliation> {

	@Override
	public MotifResiliation deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		return MotifResiliation.fromString(parser.getText().toUpperCase());
	}

}
