package com.tsemkalo.library;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Library {
	private Map<Long, Book> books = new HashMap<>();
	private Map<Long, Author> authors = new HashMap<>();

	public void addAuthor(Author author) {
		authors.put(author.getId(), author);
	}

	public void addBook(Book book) {
		books.put(book.getId(), book);
	}

	public Author getAuthorById(Long id) {
		return authors.get(id);
	}

	public List<Book> getBooksByAuthorName(String authorName) {
		return books.values().stream().filter(book -> authorName.equals(book.getAuthor().getName())).collect(Collectors.toList());
	}
}
