package br.com.biblioteca.domain.bookartefact;

import br.com.biblioteca.core.BaseDTO;
import br.com.biblioteca.domain.book.Book;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;

public record BookArtifactDTO(
        @Null
        Long id,

        Long bookId,

        String description,

        @Null
        Boolean enabled,

        @Null
        LocalDateTime createdDate,

        @Null
        LocalDateTime lastModifiedDate

) implements BaseDTO {
}
