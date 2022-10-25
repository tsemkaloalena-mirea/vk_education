package com.tsemkalo.homework3;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.tsemkalo.homework3.exceptions.NoFreeCellsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class LibraryInjectionTest {
    @Inject
    private LibraryFactory libraryFactory;

    @BeforeEach
    public void init() throws URISyntaxException {
        Path filePath = Paths.get(this.getClass().getResource("/books.json").toURI());
        final Injector injector = Guice.createInjector(new InjectionModule(filePath.toString()));
        injector.injectMembers(this);
    }

    @Test
    public void capacityIsLessThenAmountOFBooksThenThrowException() {
        assertNotNull(libraryFactory);
        assertThrows(NoFreeCellsException.class, () -> new Library(6, libraryFactory));
    }
}
