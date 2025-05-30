package br.com.biblioteca.domain.bookrent;

import br.com.biblioteca.validations.groups.CreateValidation;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Column;
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
        LocalDateTime devolucao,

        @NotBlank(groups = {CreateValidation.class})
        Boolean atrasado,

        @Null
        Boolean enabled,

        @Null
        LocalDateTime createdDate,

        @Null
        LocalDateTime lastModifiedDate
){
}
