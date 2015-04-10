package com.nordnet.topaze.livraison.core.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nordnet.topaze.livraison.core.domain.TypeBonPreparation;

/**
 * Definir notre propre logique de deserialisation de l'enum {@link TypeBonPreparation}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class TypeBonPreparationDeserializer extends JsonDeserializer<TypeBonPreparation> {

	@Override
	public TypeBonPreparation deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		return TypeBonPreparation.fromString(parser.getText().toUpperCase());
	}

}
