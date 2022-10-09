package com.tsemkalo.library;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsemkalo.library.serializers.BookDeserializer;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class LibraryFactory {
	private GsonBuilder gsonBuilder;
	private Gson gson;

	public LibraryFactory(GsonBuilder gsonBuilder) {
		this.gsonBuilder = gsonBuilder;
		this.gson = gsonBuilder.setPrettyPrinting().create();
	}

	public Library createLibrary() {
		Library library = new Library();
		readAuthorsFromJsonFile(library);
		gsonBuilder.registerTypeAdapter(Book.class, new BookDeserializer(library));
		this.gson = gsonBuilder.setPrettyPrinting().create();
		readBooksFromJsonFile(library);
		return library;
	}

	private void readAuthorsFromJsonFile(Library library) {
		try {
			URL authorResource = this.getClass().getResource("/authors.json");
			assert authorResource != null;
			Reader authorReader = new FileReader(Paths.get(authorResource.toURI()).toFile());
			for (Author author : gson.fromJson(authorReader, Author[].class)) {
				library.addAuthor(author);
			}
		} catch (IOException | URISyntaxException exception) {
			exception.printStackTrace();
		}
	}

	private void readBooksFromJsonFile(Library library) {
		try {
			URL bookResource = this.getClass().getResource("/books.json");
			assert bookResource != null;
			Reader bookReader = new FileReader(Paths.get(bookResource.toURI()).toFile());
			for (Book book : gson.fromJson(bookReader, Book[].class)) {
				library.addBook(book);
			}
		} catch (IOException | URISyntaxException exception) {
			exception.printStackTrace();
		}
	}
}
