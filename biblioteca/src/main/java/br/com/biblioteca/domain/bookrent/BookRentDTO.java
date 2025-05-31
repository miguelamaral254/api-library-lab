package br.com.biblioteca.domain.bookrent;

import br.com.biblioteca.validations.groups.CreateValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;

public record BookRentDTO (
        @Null
        Long id,

        @NotNull(groups = {CreateValidation.class})
        Long bookId,

        @NotNull(groups = {CreateValidation.class})
        Long userId,

        @NotBlank(groups = {CreateValidation.class})
        LocalDateTime returnDate,

        @NotNull(groups = {CreateValidation.class})
        Boolean late,

        @Null
        Boolean enabled,

        @Null
        LocalDateTime createdDate,

        @Null
        LocalDateTime lastModifiedDate
){
}
