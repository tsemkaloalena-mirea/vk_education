package com.tsemkalo.library.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	@Override
	public LocalDate deserialize(JsonElement json, Type typeOfSrc, JsonDeserializationContext context) {
		return LocalDate.parse(json.getAsString(), formatter.withLocale(Locale.ENGLISH));
	}
}