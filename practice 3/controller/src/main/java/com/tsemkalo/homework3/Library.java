package com.tsemkalo.homework3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.tsemkalo.homework3.exceptions.BookNotFoundException;
import com.tsemkalo.homework3.exceptions.NoFreeCellsException;
import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class Library {
    private final Integer capacity;

    @Getter
    private final Map<Integer, Book> books = new HashMap<>();

    public Library(Integer capacity, BookFactory bookFactory) {
        this.capacity = capacity;
        int cellNumber = 0;
        Collection<Book> givenBooks = bookFactory.books();
        if (givenBooks.size() > capacity) {
            throw new NoFreeCellsException(capacity);
        }
        for (Book book : givenBooks) {
            books.put(cellNumber, book);
            cellNumber++;
        }
    }

    public void takeBook(Integer cellNumber) {
        if (!books.containsKey(cellNumber)) {
            throw new BookNotFoundException(cellNumber);
        }
        System.out.println("Cell " + cellNumber + " contains book " + books.get(cellNumber).getName());
        books.remove(cellNumber);
    }

    public void putBook(Book book) {
        for (int i = 0; i < capacity; i++) {
            if (books.get(i) == null || !books.containsKey(i)) {
                books.put(i, book);
                return;
            }
        }
        throw new NoFreeCellsException(book);
    }

    public void printAllBooks() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        System.out.println(gson.toJson(books));
    }
}
