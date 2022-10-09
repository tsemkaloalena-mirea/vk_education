package com.tsemkalo.library.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsemkalo.library.Author;
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
		Book book = new Book();
		book.setId(jsonObject.get("id").getAsLong());
		book.setTitle(jsonObject.get("title").getAsString());
		book.setAuthor(library.getAuthorById(authorId.getAsLong()));
		book.setLanguage(jsonObject.get("language").getAsString());
		book.setCreationDate(LocalDate.parse(jsonObject.get("creationDate").getAsString(), formatter.withLocale(Locale.ENGLISH)));
		return book;
	}
}
