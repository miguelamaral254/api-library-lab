package br.com.biblioteca.domain.bookartefact;

import br.com.biblioteca.core.BusinessException;
import br.com.biblioteca.domain.book.Book;
import br.com.biblioteca.domain.book.BookRepository;
import br.com.biblioteca.domain.book.BookExceptionCodeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BookArtifactService {

    private final BookArtifactRepository bookArtifactRepository;
    private final BookRepository bookRepository;

    public BookArtifactService(BookArtifactRepository bookArtifactRepository, BookRepository bookRepository) {
        this.bookArtifactRepository = bookArtifactRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public BookArtifact createArtifact(Long bookId, BookArtifact artifact) {
        Book book = validateBusinessRules(bookId);

        artifact.setBookId(book);
        return bookArtifactRepository.save(artifact);
    }

    private Book validateBusinessRules(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException(BookExceptionCodeEnum.BOOK_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public BookArtifact getById(Long id) {
        return bookArtifactRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BookArtifactExceptionCodeEnum.ARTIFACT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<BookArtifact> searchAllArtifacts(Specification<BookArtifact> specification, Pageable pageable) {
        return bookArtifactRepository.findAll(specification, pageable);
    }
}