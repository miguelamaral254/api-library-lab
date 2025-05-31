package br.com.biblioteca.domain.bookrents;

import br.com.biblioteca.domain.book.Book;
import br.com.biblioteca.domain.book.BookRepository;
import br.com.biblioteca.domain.bookrent.BookRent;
import br.com.biblioteca.domain.bookrent.BookRentRepository;
import br.com.biblioteca.domain.bookrent.BookRentService;
import br.com.biblioteca.domain.bookrents.factories.BookRentFactory;
import br.com.biblioteca.domain.books.factories.BookFactory;
import br.com.biblioteca.domain.user.User;
import br.com.biblioteca.domain.user.UserRepository;
import br.com.biblioteca.domain.user.factories.UserFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookRentServiceTest {

    @InjectMocks
    private BookRentService bookRentService;

    @Mock
    private BookRentRepository bookRentRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("Deve criar BookRent com sucesso")
    void shouldCreateBookRentSuccessfully() {

        Long bookId = 1L;
        Long userId = 2L;

        Book book = BookFactory.savedBook(bookId);
        User user = UserFactory.savedUser(userId);
        BookRent inputBookRent = BookRentFactory.validBookRent();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        BookRent savedBookRent = BookRentFactory.savedBookRent(10L, user, book, inputBookRent.getReturnDate(), inputBookRent.isLate(), inputBookRent.getEnabled());
        when(bookRentRepository.save(any(BookRent.class))).thenReturn(savedBookRent);

        BookRent result = bookRentService.createBookRent(bookId, userId, inputBookRent);

        assertNotNull(result);
        assertEquals(savedBookRent.getId(), result.getId());
        assertEquals(book, result.getBookId());
        assertEquals(user, result.getUserId());

        verify(bookRepository).findById(bookId);
        verify(userRepository).findById(userId);
        verify(bookRentRepository).save(inputBookRent);
    }
}
