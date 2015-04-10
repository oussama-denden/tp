package com.nordnet.topaze.client.rest.business.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class DateDeserialiser extends JsonDeserializer<Date> {

	/**
	 * {@link SimpleDateFormat}.
	 */
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		try {
			String dateValue = jp.getText();
			return DATE_FORMAT.parse(dateValue);
		} catch (ParseException e) {
			throw new InvalidFormatException("Failed to parse Date value " + jp.getText(), null, Date.class);
		}
	}
}
