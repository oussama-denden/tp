package com.nordnet.topaze.contrat.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nordnet.topaze.contrat.business.Produit;
import com.nordnet.topaze.contrat.domain.TypePrix;
import com.nordnet.topaze.contrat.domain.TypeTVA;

/**
 * Definir notre propre logique de d�s�rialisation que sera utiliser par jackson lors de auto-population de
 * {@link TypePrix}. Cette classe sera utiliser notamment par les controlleur {@link ProduitController} pour
 * l'auto-population de {@link Produit} � partir des parametres de requ�te HttpServletRequest.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class TypeTVADeserializer extends JsonDeserializer<TypeTVA> {

	@Override
	public TypeTVA deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		return TypeTVA.fromString(parser.getText().toUpperCase());
	}

}
