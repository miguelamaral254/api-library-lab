package br.com.biblioteca.domain.book;

import br.com.biblioteca.core.ApplicationResponse;
import br.com.biblioteca.validations.groups.UpdateValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Tag(name = "Book")
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @Tag(name = "Create Opportunity")
    @Operation(summary = "Create a new opportunity")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Void> createBook(
            @RequestPart("dto") BookDTO bookDto,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest request) throws IOException {

        Book book = bookMapper.toEntity(bookDto);
        Book savedBook = bookService.createBook(book, file, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBook.getId())
                .toUri();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(location)
                .build();
    }

    @Tag(name="Search Books with filter")
    @GetMapping
    @Operation(summary = "Search books with filters or all books")
    public ResponseEntity<ApplicationResponse<Page<BookDTO>>> searchBooks(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "userid", required = false) Long userId,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "available", required = false) Boolean available,
            Pageable pageable) {

        Specification<Book> specification = Specification.where(null);

        if (title != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }
        if (userId != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("userId").get("id"), userId));
        }
        if (gender != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(criteriaBuilder.lower(root.get("gender")), gender.toLowerCase()));
        }
        if (available != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("available"), available));
        }

        Page<Book> bookPage = bookService.searchBooks(specification, pageable);
        Page<BookDTO> bookDTOPage = bookMapper.toDto(bookPage);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApplicationResponse.ofSuccess(bookDTOPage));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Search Book by ID")
    public ResponseEntity<ApplicationResponse<BookDTO>> findById(
            @PathVariable Long id) {
        Book book = bookService.findById(id);
        BookDTO bookDTO = bookMapper.toDto(book);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApplicationResponse.ofSuccess(bookDTO));
    }
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing book")
    public ResponseEntity<ApplicationResponse<BookDTO>> updateBook(
            @PathVariable Long id,
            @Validated(UpdateValidation.class) @RequestBody BookDTO bookDtoUpdates) {

        Book updatedBook = bookService.updateBook(id, bookDtoUpdates);
        BookDTO updatedBookDto = bookMapper.toDto(updatedBook);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApplicationResponse.ofSuccess(updatedBookDto));
    }

    @Tag(name = "Update Book Status")
    @Operation(summary = "Update the availability status of a Book")
    @PatchMapping("/{id}")
    public ResponseEntity<ApplicationResponse<String>> updateAvailable(
            @PathVariable Long id,
            @RequestParam String available) {

        Boolean status = Boolean.valueOf(available);

        Book updatedBook = bookService.updateAvailability(id, status);

        return ResponseEntity
                .status(HttpStatus.PARTIAL_CONTENT)
                    .body(ApplicationResponse.ofSuccess(updatedBook.getAvailable().toString()));
    }

    @Tag(name = "Delete Book")
    @Operation(summary = "Delete a Book by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}