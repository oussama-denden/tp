package com.nordnet.topaze.contrat.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nordnet.topaze.contrat.domain.Engagement;
import com.nordnet.topaze.contrat.domain.PolitiqueMigration;

/**
 * Definir notre propre logique de deserialisation que sera utiliser par jackson lors de auto-population de
 * {@link Engagement}. Cette classe sera utiliser notamment par les controlleur pour l'auto-population de
 * {@link PolitiqueMigration} a partir des parametres de requete HttpServletRequest.
 * 
 * @author anisselmane.
 * 
 */
public class EngagementDeserializer extends JsonDeserializer<Engagement> {

	@Override
	public Engagement deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		return Engagement.fromString(parser.getText().toUpperCase());
	}

}
