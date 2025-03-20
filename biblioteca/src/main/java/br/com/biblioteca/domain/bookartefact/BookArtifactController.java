package br.com.biblioteca.domain.bookartefact;

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

@Tag(name = "BookArtifact")
@RestController
@RequestMapping("/book-artifacts")
@RequiredArgsConstructor
public class BookArtifactController {

    private final BookArtifactService bookArtifactService;
    private final BookArtifactMapper bookArtifactMapper;

    @Tag(name = "Create Book Artifact")
    @PostMapping
    @Operation(summary = "Create a new book artifact")
    public ResponseEntity<Void> createArtifact(
            @Validated(CreateValidation.class)
            @RequestBody BookArtifactDTO bookArtifactDto) {

        BookArtifact bookArtifact = bookArtifactMapper.toEntity(bookArtifactDto);
        BookArtifact savedArtifact = bookArtifactService.createArtifact(bookArtifactDto.bookId(), bookArtifact);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedArtifact.getId())
                .toUri();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(location)
                .build();
    }

    @Tag(name = "Search Book Artifacts with filter")
    @GetMapping
    @Operation(summary = "Search book artifacts with filters or all artifacts")
    public ResponseEntity<ApplicationResponse<Page<BookArtifactDTO>>> searchArtifacts(
            @RequestParam(value = "bookId", required = false) Long bookId,
            @RequestParam(value = "description", required = false) String description,
            Pageable pageable) {

        Specification<BookArtifact> specification = Specification.where(null);

        if (bookId != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("book").get("id"), bookId));
        }
        if (description != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
        }

        Page<BookArtifact> artifactPage = bookArtifactService.searchAllArtifacts(specification, pageable);
        Page<BookArtifactDTO> artifactDTOPage = bookArtifactMapper.toDto(artifactPage);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApplicationResponse.ofSuccess(artifactDTOPage));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Search Book Artifact by ID")
    public ResponseEntity<ApplicationResponse<BookArtifactDTO>> findById(
            @PathVariable Long id) {
        BookArtifact bookArtifact = bookArtifactService.getById(id);
        BookArtifactDTO bookArtifactDTO = bookArtifactMapper.toDto(bookArtifact);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApplicationResponse.ofSuccess(bookArtifactDTO));
    }


}