package com.tsemkalo.homework3.exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Integer cellNumber) {
        super("There is no book in the cell number " + cellNumber);
    }
}
