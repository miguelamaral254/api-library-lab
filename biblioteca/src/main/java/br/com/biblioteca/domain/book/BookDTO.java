package br.com.biblioteca.domain.book;

import br.com.biblioteca.core.BaseDTO;
import br.com.biblioteca.validations.groups.CreateValidation;
import br.com.biblioteca.validations.groups.UpdateValidation;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;

public record BookDTO(

        @Null
        Long id,

        @NotBlank(groups = {CreateValidation.class})
        String urlImage,

        @NotBlank(groups = {CreateValidation.class})
        String title,

        @NotBlank(groups = {CreateValidation.class})
        Long userId,

        @NotBlank(groups = {CreateValidation.class})
        String description,

        @NotBlank(groups = {CreateValidation.class})
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