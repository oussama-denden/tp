package com.nordnet.topaze.contrat.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Classe pour la serialisation de la date.
 * 
 * @author akram-moncer
 * 
 */
public class DateSerializer extends JsonSerializer<Date> {

	/**
	 * {@link SimpleDateFormat}.
	 */
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void serialize(Date date, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		String formattedDate = DATE_FORMAT.format(date);
		jgen.writeString(formattedDate);
	}

}
