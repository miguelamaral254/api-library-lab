package br.com.biblioteca.domain.phone;

import br.com.biblioteca.core.BaseDTO;
import br.com.biblioteca.validations.groups.CreateValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;

public record PhoneDTO(
        @Null
        Long id,

        @NotBlank(message = "Phone number cannot be blank")
        String number,

        @NotBlank(message = "Country code cannot be blank")
        String countryCode,

        @NotNull(groups = CreateValidation.class)
        Boolean enabled,

        LocalDateTime createdDate,

        LocalDateTime lastModifiedDate
) implements BaseDTO {}
