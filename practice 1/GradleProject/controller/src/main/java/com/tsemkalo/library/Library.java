package com.tsemkalo.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library {
    private Map<Author, List<Book>> books = new HashMap<>();

    public void addAuthor(Author author) {
        books.put(author, new ArrayList<>());
    }

    public void addBook(Book book) {
        books.get(book.getAuthor()).add(book);
    }

    public Author getAuthorById(Long id) {
        for (Author author : books.keySet()) {
            if (id.equals(author.getId())) {
                return author;
            }
        }
        return null;
    }

    public Author getAuthorByName(String authorName) {
        for (Author author : books.keySet()) {
            if (authorName.equals(author.getName())) {
                return author;
            }
        }
        return null;
    }

    public List<Book> getBooksByAuthorName(String authorName) {
        return books.get(getAuthorByName(authorName));
    }
}
