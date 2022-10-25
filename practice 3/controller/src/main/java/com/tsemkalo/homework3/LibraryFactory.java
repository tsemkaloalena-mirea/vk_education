package com.tsemkalo.homework3;

import java.util.Collection;

public interface LibraryFactory {
    Collection<Book> books();

    Library library(Integer capacity);
}
