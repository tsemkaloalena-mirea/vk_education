package com.tsemkalo.homework3;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public final class FileLibraryFactory implements LibraryFactory {
    @NotNull
    private static final Type listBooksType = new TypeToken<ArrayList<Book>>() {
    }.getType();
    @NotNull
    private final String fileName;

    public FileLibraryFactory(@NotNull String fileName) {
        this.fileName = fileName;
    }

    @NotNull
    @Override
    public Collection<Book> books() {
        try {
            return new Gson().fromJson(new BufferedReader(new FileReader(fileName)), listBooksType);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Library library(Integer capacity) {
        return new Library(capacity, this);
    }
}