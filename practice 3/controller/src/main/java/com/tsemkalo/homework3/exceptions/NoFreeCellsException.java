package com.tsemkalo.homework3.exceptions;

import com.tsemkalo.homework3.Book;

public class NoFreeCellsException extends RuntimeException {
    public NoFreeCellsException(Book book) {
        super("Can not put book " + book.getName() + " to the library. No free cells left.");
    }

    public NoFreeCellsException(Integer capacity) {
        super("Too many books, the capacity of this library is " + capacity + " books.");
    }
}
