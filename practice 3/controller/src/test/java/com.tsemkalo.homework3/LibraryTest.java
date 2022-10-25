package com.tsemkalo.homework3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsemkalo.homework3.exceptions.BookNotFoundException;
import com.tsemkalo.homework3.exceptions.NoFreeCellsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public final class LibraryTest extends AbstractTest {
    @Mock
    private FileLibraryFactory fileLibraryFactory;

//    @InjectMocks
    private Library library;

    @BeforeEach
    public void init() {
        Mockito.when(fileLibraryFactory.books()).thenReturn(getBookCollection());
        library = new Library(20, fileLibraryFactory);
    }

    @Test
    public void checkIfOrderIsCorrect() {
        List<Book> fabricBooks = getBookCollection().stream().toList();
        Map<Integer, Book> libraryBooks = library.getBooks();
        for (int i = 0; i < 20; i++) {
            if (i < 14) {
                assertEquals(fabricBooks.get(i), libraryBooks.get(i));
            } else {
                assertFalse(libraryBooks.containsKey(i));
            }
        }
    }

    @Test
    public void whenBookIsTakenThenInfoIsPrintedAndCellBecomesEmpty() {
        Integer cellNumber = 1;
        library.takeBook(cellNumber);
        String text = "Cell " + cellNumber + " contains book " + getBookCollection().stream().toList().get(cellNumber).getName();
        assertEquals(text, getOutputStreamCaptor().toString().trim());
        assertFalse(library.getBooks().containsKey(cellNumber));
    }

    @Test
    public void whenBookIsTakenFromEmptyCellThemExceptionIsThrown() {
        Integer cellNumber = 15;
        assertThrows(BookNotFoundException.class, () -> library.takeBook(cellNumber));
    }

    @Test
    public void whenBookIsAddedThenFirstEmptyCellIsChosen() {
        library.getBooks().remove(2);
        library.getBooks().remove(5);
        library.getBooks().remove(8);
        Book book = new Book("Book 0", new Author("Author 2"));
        library.putBook(book);
        assertTrue(library.getBooks().containsKey(2));
        assertEquals(book, library.getBooks().get(2));
    }

    @Test
    public void whenNoFreeCellsLeftThenExceptionIsThrown() {
        library.putBook(new Book("Book 0", new Author("Author 2")));
        library.putBook(new Book("Book 1", new Author("Author 2")));
        library.putBook(new Book("Book 2", new Author("Author 2")));
        library.putBook(new Book("Book 3", new Author("Author 2")));
        library.putBook(new Book("Book 4", new Author("Author 2")));
        library.putBook(new Book("Book 5", new Author("Author 2")));
        assertThrows(NoFreeCellsException.class, () -> library.putBook(new Book("Book 6", new Author("Author 2"))));
    }

    @Test
    public void allCellContentIsPrinted() {
        Map<Integer, Book> expectedContent = new HashMap<>();
        List<Book> givenBooks = getBookCollection().stream().toList();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        for (int i = 0; i < givenBooks.size(); i++) {
            expectedContent.put(i, givenBooks.get(i));
        }
        String expectedOutput = gson.toJson(expectedContent);
        library.printAllBooks();
        assertEquals(expectedOutput, getOutputStreamCaptor().toString().trim());
    }
}
