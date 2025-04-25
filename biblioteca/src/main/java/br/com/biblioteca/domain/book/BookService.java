package br.com.biblioteca.domain.book;

import br.com.biblioteca.core.BusinessException;
import br.com.biblioteca.domain.user.User;
import br.com.biblioteca.domain.user.UserRepository;
import br.com.biblioteca.domain.user.enums.UserExceptionCodeEnum;
import br.com.biblioteca.infrastructure.conf.ImageConf;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;
    private final ImageConf imageConf;

    @Transactional
    public Book createBook(Book book, MultipartFile file, HttpServletRequest request) {
        validateImageCreateRules(book ,file, request);

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
    private void validateImageCreateRules(Book book, MultipartFile file, HttpServletRequest request) {
        try {
            validateBusinessRules(book);

            if (file != null && !file.isEmpty()) {
                String urlImage = imageConf.saveImage(file, request);
                book.setUrlImage(urlImage);
            }

        } catch (IOException e) {
            throw new BusinessException(BookExceptionCodeEnum.IMAGE_CREATION_FAILED);
        }
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
    public Book updateBook(Long id, BookDTO bookDtoUpdates) {
        Book existingBook = findById(id);
        bookMapper.mergeNonNull(bookDtoUpdates, existingBook);

        validateUpdateBusiness(existingBook);
        return bookRepository.save(existingBook);
    }

    @Transactional
    public Book updateAvailability(Long id, Boolean available) {
        Book book = findById(id);
        book.setAvailable(available);

        validateUpdateBusiness(book);

        return bookRepository.save(book);
    }

    private void validateUpdateBusiness(Book book) {
        if (book == null) {
            throw new BusinessException(BookExceptionCodeEnum.BOOK_NOT_FOUND);
        }

        if (book.getAvailable() == null) {
            throw new BusinessException(BookExceptionCodeEnum.INVALID_AVAILABILITY_STATUS);
        }

        if (!book.getAvailable().equals(Boolean.TRUE) && !book.getAvailable().equals(Boolean.FALSE)) {
            throw new BusinessException(BookExceptionCodeEnum.INVALID_AVAILABILITY_STATUS);
        }

        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new BusinessException(BookExceptionCodeEnum.INVALID_BOOK_TITLE);
        }

        if (book.getUserId() == null) {
            throw new BusinessException(BookExceptionCodeEnum.INVALID_USER);
        }

        if (book.getGender() == null || !Enum.valueOf(Gender.class, book.getGender().name()).equals(book.getGender())) {
            throw new BusinessException(BookExceptionCodeEnum.INVALID_BOOK_GENDER);
        }
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = findById(id);
        bookRepository.delete(book);
    }


}