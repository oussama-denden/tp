package com.nordnet.topaze.livraison.core.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nordnet.topaze.livraison.core.domain.OutilLivraison;

/**
 * Definir notre propre logique de d�s�rialisation que sera utiliser par jackson lors de auto-population de
 * {@link OutilLivraison}.
 * 
 * 
 * @author Ahmed-Mehdi-Laabidi
 * 
 */
public class OutilLivraisonDeserializer extends JsonDeserializer<OutilLivraison> {

	@Override
	public OutilLivraison deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		return OutilLivraison.fromString(parser.getText().toUpperCase());
	}

}
