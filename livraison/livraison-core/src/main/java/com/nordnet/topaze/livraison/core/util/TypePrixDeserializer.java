package com.nordnet.topaze.livraison.core.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nordnet.topaze.livraison.core.domain.Produit;
import com.nordnet.topaze.livraison.core.domain.TypePrix;

/**
 * Definir notre propre logique de d�s�rialisation que sera utiliser par jackson lors de auto-population de
 * {@link TypePrix}. Cette classe sera utiliser notamment par les controlleur pour l'auto-population de {@link Produit}
 * � partir des parametres de requ�te HttpServletRequest.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class TypePrixDeserializer extends JsonDeserializer<TypePrix> {

	@Override
	public TypePrix deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		return TypePrix.fromString(parser.getText().toUpperCase());
	}

}
