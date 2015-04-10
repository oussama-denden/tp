package com.nordnet.topaze.contrat.outil.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nordnet.topaze.client.rest.enums.TypeProduit;

/**
 * Definir notre propre logique de deserialisation que sera utiliser par jackson lors de auto-population de
 * {@link TypeProduit}. Cette classe sera utiliser notamment par les controlleur pour l'auto-population de
 * {@link Produit} a partir des parametres de requete HttpServletRequest.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class TypeProduitDeserializer extends JsonDeserializer<TypeProduit> {

	@Override
	public TypeProduit deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		return TypeProduit.fromString(parser.getText().toUpperCase());
	}

}
