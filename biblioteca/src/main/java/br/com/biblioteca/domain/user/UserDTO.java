package br.com.biblioteca.domain.user;


import br.com.biblioteca.core.BaseDTO;
import br.com.biblioteca.domain.phone.Phone;
import br.com.biblioteca.validations.groups.CreateValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.List;

public record UserDTO(
        Long id,

        @NotBlank(groups = CreateValidation.class)
        String name,

        @NotBlank(groups = CreateValidation.class)
        String registration,

        @NotNull(groups = CreateValidation.class)
        @Enumerated(EnumType.STRING)
        Role role,

        List<Phone> phones,

        @NotBlank(groups = CreateValidation.class)
        @Email(message = "Email com formato inválido")
        String email,

        @NotBlank(groups = CreateValidation.class)
        String password,

        @NotNull(groups = CreateValidation.class)
        Boolean enabled,

        @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos")
        String cpf,

        @Pattern(regexp = "\\d{14}", message = "CNPJ deve ter 14 dígitos")
        String cnpj,

        LocalDateTime createdDate,

        LocalDateTime lastModifiedDate
) implements BaseDTO {}