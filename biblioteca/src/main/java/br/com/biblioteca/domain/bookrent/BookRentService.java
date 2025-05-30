package br.com.biblioteca.domain.bookrent;

import br.com.biblioteca.domain.book.Book;
import br.com.biblioteca.domain.book.BookRepository;
import br.com.biblioteca.domain.exceptions.NotFoundException;
import br.com.biblioteca.domain.user.User;
import br.com.biblioteca.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@AllArgsConstructor
@Service
public class BookRentService {

    private final BookRepository bookRepository;
    private final BookRentRepository bookRentRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookRent createBookRent(Long bookId, Long userId, BookRent bookRent) {
        Book book = validateBook(bookId);
        User user = validateUser(userId);

        bookRent.setBookId(book);
        bookRent.setUserId(user);

        return bookRentRepository.save(bookRent);
    }
    private Book validateBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found"));
    }
    private User validateUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public Page<BookRent> searchAllBooksRents(Specification<BookRent> specification, Pageable pageable) {
        return bookRentRepository.findAll(specification, pageable);
    }

}
