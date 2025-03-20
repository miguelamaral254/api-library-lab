package br.com.biblioteca.domain.book;

import br.com.biblioteca.core.BaseDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;

public record BookDTO(

        @Null
        Long id,

        @NotBlank
        String title,

        @NotNull
        Long userId,

        @NotBlank
        String description,

        @NotNull
        @Enumerated(EnumType.STRING)
        Gender gender,

        @Null
        Boolean available,

        @Null
        Boolean enabled,

        @Null
        LocalDateTime createdDate,

        @Null
        LocalDateTime lastModifiedDate

) implements BaseDTO {
}