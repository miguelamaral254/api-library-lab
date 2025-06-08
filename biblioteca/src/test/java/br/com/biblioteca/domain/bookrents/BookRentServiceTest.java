package br.com.biblioteca.domain.bookrents;

import br.com.biblioteca.domain.book.Book;
import br.com.biblioteca.domain.book.BookRepository;
import br.com.biblioteca.domain.book.BookService;
import br.com.biblioteca.domain.bookrent.BookRent;
import br.com.biblioteca.domain.bookrent.BookRentRepository;
import br.com.biblioteca.domain.bookrent.BookRentService;
import br.com.biblioteca.domain.bookrents.factories.BookRentFactory;
import br.com.biblioteca.domain.books.factories.BookFactory;
import br.com.biblioteca.domain.exceptions.NotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class BookRentServiceTest {

    @InjectMocks
    private BookRentService bookRentService;

    @Mock
    private BookRentRepository bookRentRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookService bookService;

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

        verify(bookRepository, times(1)).findById(bookId);
        verify(userRepository, times(1)).findById(userId);
        verify(bookRentRepository, times(1)).save(inputBookRent);

        assertNotNull(result);
        assertEquals(savedBookRent.getId(), result.getId());
        assertEquals(book, result.getBookId());
        assertEquals(user, result.getUserId());
        assertEquals(inputBookRent.getReturnDate(), result.getReturnDate());
        assertEquals(inputBookRent.isLate(), result.isLate());
        assertEquals(inputBookRent.getEnabled(), result.getEnabled());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o livro não for encontrado")
    void shouldThrowExceptionWhenBookNotFound() {
        Long bookId = 1L;
        Long userId = 2L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        BookRent inputBookRent = BookRentFactory.validBookRent();

        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookRentService.createBookRent(bookId, userId, inputBookRent));

        verify(bookRepository, times(1)).findById(bookId);
        verify(userRepository, never()).findById(any());
        verify(bookRentRepository, never()).save(any());

        assertEquals("Book not found", exception.getMessage());
    }
    @Test
    @DisplayName("Deve lançar exceção quando o usuário não for encontrado")
    void shouldThrowExceptionWhenUserNotFound() {
        Long bookId = 1L;
        Long userId = 2L;

        Book book = BookFactory.savedBook(bookId);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        BookRent inputBookRent = BookRentFactory.validBookRent();

        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookRentService.createBookRent(bookId, userId, inputBookRent));

        verify(bookRepository, times(1)).findById(bookId);
        verify(userRepository, times(1)).findById(userId);
        verify(bookRentRepository, never()).save(any());

        assertEquals("User not found", exception.getMessage());
    }
    @Test
    @DisplayName("Deve desabilitar o aluguel com sucesso")
    void shouldDisableRentSuccessfully() {
        Long rentId = 1L;
        BookRent rent = BookRentFactory.savedBookRent(rentId, null, null, null, false, true);

        when(bookRentRepository.findById(rentId)).thenReturn(Optional.of(rent));
        when(bookRentRepository.save(rent)).thenReturn(rent);

        BookRent result = bookRentService.disableRent(rentId, false);

        verify(bookRentRepository, times(1)).findById(rentId);
        verify(bookRentRepository, times(1)).save(rent);

        assertNotNull(result);
        assertEquals(false, result.getEnabled());
        assertEquals(false, result.isLate());
    }
    @Test
    @DisplayName("Deve lançar exceção ao tentar desabilitar aluguel não encontrado")
    void shouldThrowExceptionWhenRentNotFoundForDisable() {
        Long rentId = 1L;

        when(bookRentRepository.findById(rentId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookRentService.disableRent(rentId, false));

        verify(bookRentRepository, times(1)).findById(rentId);
        verify(bookRentRepository, never()).save(any());

        assertEquals("BookRent not found", exception.getMessage());
    }

    @Test
    @DisplayName("Deve processar devolução do livro com sucesso")
    void shouldProcessBookReturnSuccessfully() {
        Long rentId = 1L;
        Book book = BookFactory.savedBook(1L);
        BookRent rent = BookRentFactory.savedBookRent(rentId, UserFactory.savedUser(1L), book, null, false, true);

        when(bookRentRepository.findById(rentId)).thenReturn(Optional.of(rent));
        bookRentService.bookReturn(rentId);

        verify(bookService, times(1)).updateAvailability(book.getId(), true);
        verify(bookRentRepository, times(1)).save(rent);

        assertEquals(false, rent.getEnabled(), "O status de disponibilidade do aluguel deveria ser 'false' após devolução");
        assertEquals(null, rent.getReturnDate(), "A data de devolução deveria ser nula após a devolução");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar devolver aluguel não encontrado")
    void shouldThrowExceptionWhenRentNotFoundForReturn() {
        Long rentId = 1L;

        when(bookRentRepository.findById(rentId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookRentService.bookReturn(rentId));

        verify(bookRentRepository, times(1)).findById(rentId);
        verify(bookService, never()).updateAvailability(any(), any());

        assertEquals("BookRent not found", exception.getMessage());
    }
}