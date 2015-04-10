package com.nordnet.topaze.livraison.core.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nordnet.topaze.livraison.core.domain.Produit;
import com.nordnet.topaze.livraison.core.domain.TypeProduit;

/**
 * Definir notre propre logique de d�s�rialisation que sera utiliser par jackson lors de auto-population de
 * {@link TypeProduit}. Cette classe sera utiliser notamment par les controlleur pour l'auto-population de
 * {@link Produit} � partir des parametres de requ�te HttpServletRequest.
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
