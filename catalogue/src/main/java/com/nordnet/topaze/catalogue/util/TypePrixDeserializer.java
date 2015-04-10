package com.nordnet.topaze.catalogue.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nordnet.topaze.catalogue.controller.ProduitController;
import com.nordnet.topaze.catalogue.domain.Produit;
import com.nordnet.topaze.catalogue.domain.TypePrix;

/**
 * Definir notre propre logique deserialisation que sera utiliser par jackson lors de auto-population de
 * {@link TypePrix}. Cette classe sera utiliser notamment par les controlleur {@link ProduitController} pour
 * l'auto-population de {@link Produit} a partir des parametres de requete HttpServletRequest.
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
