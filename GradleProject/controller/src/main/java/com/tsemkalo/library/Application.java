package com.tsemkalo.library;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsemkalo.library.serializers.LocalDateDeserializer;
import com.tsemkalo.library.serializers.LocalDateSerializer;

import java.time.LocalDate;

public class Application {
	public static void main(String[] args) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
		gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());

		LibraryFactory libraryFactory = new LibraryFactory(gsonBuilder);
		Library library = libraryFactory.createLibrary();
		Gson gson = gsonBuilder.setPrettyPrinting().create();

		System.out.println(gson.toJson(library.getBooksByAuthorName("Dina Rubina")));
	}
}
