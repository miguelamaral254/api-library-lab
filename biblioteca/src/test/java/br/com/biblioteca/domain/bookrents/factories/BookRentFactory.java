package br.com.biblioteca.domain.bookrents.factories;

import br.com.biblioteca.domain.book.Book;
import br.com.biblioteca.domain.bookrent.BookRent;
import br.com.biblioteca.domain.user.User;

import java.time.LocalDateTime;

import static br.com.biblioteca.domain.books.factories.BookFactory.savedBook;
import static br.com.biblioteca.domain.user.factories.UserFactory.savedUser;

public class BookRentFactory {

    public static final Long DEFAULT_ID = 1L;
    public static final User DEFAULT_USER = savedUser();
    public static final Book DEFAULT_BOOK = savedBook();
    public static final LocalDateTime DEFAULT_RETURN_DATE = LocalDateTime.now().plusDays(7);
    public static final Boolean DEFAULT_LATE = false;
    public static final Boolean DEFAULT_ENABLED = true;

    private BookRentFactory() {}

    private static BookRent baseBookRent() {
        BookRent bookRent = new BookRent();
        bookRent.setUserId(DEFAULT_USER);
        bookRent.setBookId(DEFAULT_BOOK);
        bookRent.setReturnDate(DEFAULT_RETURN_DATE);
        bookRent.setLate(DEFAULT_LATE);
        bookRent.setEnabled(DEFAULT_ENABLED);
        return bookRent;
    }

    public static BookRent validBookRent() {
        return baseBookRent();
    }

    public static BookRent savedBookRent(Long id, User user, Book book, LocalDateTime returnDate, Boolean late, Boolean enabled) {
        BookRent bookRent = new BookRent();
        bookRent.setId(id);
        bookRent.setUserId(user);
        bookRent.setBookId(book);
        bookRent.setReturnDate(returnDate);
        bookRent.setLate(late);
        bookRent.setEnabled(enabled);
        bookRent.setCreatedDate(LocalDateTime.now());
        bookRent.setLastModifiedDate(LocalDateTime.now());
        return bookRent;
    }

    public static BookRent savedBookRent(Long id) {
        return savedBookRent(id, DEFAULT_USER, DEFAULT_BOOK, DEFAULT_RETURN_DATE, DEFAULT_LATE, DEFAULT_ENABLED);
    }

    public static BookRent savedBookRent() {
        return savedBookRent(DEFAULT_ID);
    }
}