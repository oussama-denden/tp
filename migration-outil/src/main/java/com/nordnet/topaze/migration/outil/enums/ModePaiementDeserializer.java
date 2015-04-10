package com.nordnet.topaze.migration.outil.enums;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Definir notre propre logique de deserialisation que sera utiliser par jackson lors de auto-population de
 * {@link ModePaiement}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class ModePaiementDeserializer extends JsonDeserializer<ModePaiement> {

	@Override
	public ModePaiement deserialize(JsonParser parser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		return ModePaiement.fromString(parser.getText().toUpperCase());
	}

}
