package com.nordnet.topaze.catalogue.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nordnet.topaze.catalogue.controller.ProduitController;
import com.nordnet.topaze.catalogue.domain.TypeProduit;

/**
 * Definir notre propre logique deserialisation que sera utiliser par jackson lors de auto-population de
 * {@link TypeProduit}. Cette classe sera utiliser notamment par les controlleur {@link ProduitController} pour
 * l'auto-population de {@link DetailProduit} a partir des parametres de requete HttpServletRequest.
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
