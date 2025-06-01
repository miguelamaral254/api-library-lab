package br.com.biblioteca.domain.bookrent;

import br.com.biblioteca.core.ApplicationResponse;
import br.com.biblioteca.validations.groups.CreateValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/book-rents")
@RequiredArgsConstructor
public class BookRentController {

    private final BookRentService bookRentService;
    private final BookRentMapper bookRentMapper;

    @Tag(name = "Crete Book Rent")
    @PostMapping
    @Operation(summary = "Create a new Book Rent")
    public ResponseEntity<Void> createBookRent(
            @Validated(CreateValidation.class)
            @RequestBody BookRentDTO bookRentDTO) {
        BookRent bookRent = bookRentMapper.toEntity(bookRentDTO);
        BookRent savedRent = bookRentService.createBookRent(bookRentDTO.bookId(), bookRentDTO.userId(), bookRent);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedRent.getId())
                .toUri();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(location)
                .build();

    }


    @Tag(name = "Search Book Rents with filter")
    @GetMapping
    @Operation(summary = "Search Book rents with filters or all rents")
    public ResponseEntity<ApplicationResponse<Page<BookRentDTO>>> searchBookRent(
            @RequestParam(value="bookId", required = false) Long bookId,
            @RequestParam(value="userId", required = false) Long userId,
            @RequestParam(value="late", required = false) Boolean late,
            Pageable pageable){
        Specification<BookRent> specification = Specification.where(null);

        if (bookId != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("book").get("id"), bookId));
        }

        if (userId != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("userId").get("id"), userId));
        }
        if (late != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("late"), late));
        }
        Page<BookRent> rentPage = bookRentService.searchAllBooksRents(specification, pageable);
        Page<BookRentDTO> rentDTOPage = bookRentMapper.toDto(rentPage);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApplicationResponse.ofSuccess(rentDTOPage));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Search Book Rent by ID")
    public ResponseEntity<ApplicationResponse<BookRentDTO>> findBookRentById(
            @PathVariable Long id) {
        BookRent rent = bookRentService.getBookRentById(id);
        BookRentDTO bookRentDTO = bookRentMapper.toDto(rent);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApplicationResponse.ofSuccess(bookRentDTO));

    }

    @Tag(name = "Devolver livro")
    @Operation(summary = "Devolver livro by ID")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> devolucao(@PathVariable Long id) {
        bookRentService.devolucao(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


}
