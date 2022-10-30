package com.tsemkalo.homework3;

import com.google.inject.Inject;

public final class LibraryFactoryImpl implements LibraryFactory {
    private BookFactory bookFactory;

    @Inject
    public LibraryFactoryImpl(BookFactory bookFactory) {
        this.bookFactory = bookFactory;
    }

    @Override
    public Library library(Integer capacity) {
        return new Library(capacity, bookFactory);
    }
}