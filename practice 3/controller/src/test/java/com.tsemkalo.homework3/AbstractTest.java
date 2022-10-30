package com.tsemkalo.homework3;

import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractTest {
    @Getter
    private static final Collection<Book> bookCollection = new ArrayList<>();

    private final PrintStream standardOut = System.out;

    @Getter
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @BeforeAll
    public static void initFactory() {
        Author author0 = new Author("author0");
        Author author1 = new Author("author1");
        bookCollection.add(new Book("Book 0", author0));
        bookCollection.add(new Book("Book 1", author0));
        bookCollection.add(new Book("Book 2", author0));
        bookCollection.add(new Book("Book 3", author0));
        bookCollection.add(new Book("Book 4", author0));
        bookCollection.add(new Book("Book 5", author0));
        bookCollection.add(new Book("Book 0", author1));
        bookCollection.add(new Book("Book 1", author1));
        bookCollection.add(new Book("Book 2", author1));
        bookCollection.add(new Book("Book 3", author1));
        bookCollection.add(new Book("Book 4", author1));
        bookCollection.add(new Book("Book 5", author1));
        bookCollection.add(new Book("Book 6", author1));
        bookCollection.add(new Book("Book 7", author1));
    }
}
