package br.com.biblioteca.domain.book;

import br.com.biblioteca.core.BusinessException;
import br.com.biblioteca.domain.user.User;
import br.com.biblioteca.domain.user.UserRepository;
import br.com.biblioteca.domain.user.UserService;
import br.com.biblioteca.domain.user.enums.UserExceptionCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Transactional
    public Book createBook(Book book) {
        validateBusinessRules(book);
        return bookRepository.save(book);
    }

    private void validateBusinessRules(Book book) {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new BusinessException(BookExceptionCodeEnum.INVALID_BOOK_TITLE);
        }
        if (book.getUserId() == null || book.getUserId().getId() == null) {
            throw new BusinessException(BookExceptionCodeEnum.INVALID_USER);
        }

        User user = userRepository.findById(book.getUserId().getId())
                .orElseThrow(() -> new BusinessException(UserExceptionCodeEnum.USER_NOT_FOUND));

        book.setUserId(user);

    }

    @Transactional(readOnly = true)
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BookExceptionCodeEnum.BOOK_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<Book> searchBooks(Specification<Book> specification, Pageable pageable) {
        return bookRepository.findAll(specification, pageable);
    }

    @Transactional
    public Book updateBook(Long id, Book bookUpdates) {
        Book existingBook = findById(id);
        validateUpdateRules(existingBook, bookUpdates);
        return bookRepository.save(existingBook);
    }

    private void validateUpdateRules(Book existingBook, Book bookUpdates) {
        if (bookUpdates.getTitle() != null) {
            existingBook.setTitle(bookUpdates.getTitle());
        }
        if (bookUpdates.getAvailable() != null) {
            existingBook.setAvailable(bookUpdates.getAvailable());
        }
        if (bookUpdates.getEnabled() != null) {
            existingBook.setEnabled(bookUpdates.getEnabled());
        }
        if (bookUpdates.getDescription() != null) {
            existingBook.setDescription(bookUpdates.getDescription());
        }
        if (bookUpdates.getGender() != null) {
            existingBook.setGender(bookUpdates.getGender());
        }
        if (bookUpdates.getUserId() != null) {
            existingBook.setUserId(bookUpdates.getUserId());
        }
    }

    @Transactional
    public Book updateAvailability(Long id, Boolean available) {
        Book book = findById(id);
        book.setAvailable(available);
        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = findById(id);
        bookRepository.delete(book);
    }


}