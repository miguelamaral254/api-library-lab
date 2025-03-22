package br.com.biblioteca.domain.bookartefact;

import br.com.biblioteca.core.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;

public record BookArtifactDTO(
        @Null
        Long id,

        @NotNull
        Long bookId,

        @NotBlank
        String description,

        @Null
        Boolean enabled,

        @Null
        LocalDateTime createdDate,

        @Null
        LocalDateTime lastModifiedDate

) implements BaseDTO {
}
