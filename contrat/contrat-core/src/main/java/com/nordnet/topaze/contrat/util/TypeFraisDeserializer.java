package com.nordnet.topaze.contrat.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nordnet.topaze.contrat.domain.TypeFrais;


/**
 * 
 * @author Ahmed-Mehdi-Laabidi
 *
 */
public class TypeFraisDeserializer extends JsonDeserializer<TypeFrais> {

	@Override
	public TypeFrais deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		
		return TypeFrais.fromSting(parser.getText().toUpperCase());
	}
	
	

}
