package com.tsemkalo.library.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsemkalo.library.Book;
import com.tsemkalo.library.Library;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class BookDeserializer implements JsonDeserializer<Book> {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private Library library;

	public BookDeserializer(Library library) {
		this.library = library;
	}

	@Override
	public Book deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		JsonObject jsonObject = json.getAsJsonObject();
		JsonElement authorId = jsonObject.get("authorId");
		Book book = new Book(jsonObject.get("title").getAsString(),
				library.getAuthorById(authorId.getAsLong()),
				jsonObject.get("language").getAsString(),
				LocalDate.parse(jsonObject.get("creationDate").getAsString(), formatter.withLocale(Locale.ENGLISH)));
		book.setId(jsonObject.get("id").getAsLong());
		return book;
	}
}
