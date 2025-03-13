package br.com.biblioteca.domain.user;


import br.com.biblioteca.core.BaseDTO;
import br.com.biblioteca.domain.phone.Phone;
import br.com.biblioteca.domain.user.enums.Course;
import br.com.biblioteca.domain.user.enums.Institution;
import br.com.biblioteca.domain.user.enums.Role;
import br.com.biblioteca.validations.groups.CreateValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public record UserDTO(

        @Null
        Long id,

        @NotBlank(groups = CreateValidation.class)
        String name,

        @NotNull(groups = CreateValidation.class)
        @Enumerated(EnumType.STRING)
        Role role,

        List<Phone> phones,

        @NotBlank(groups = CreateValidation.class)
        @Email(message = "Email com formato inválido")
        String email,

        @NotBlank(groups = CreateValidation.class)
        String password,

        @Null
        Boolean enabled,

        @NotNull(groups = CreateValidation.class)
        @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos")
        String cpf,

        @NotNull(groups = CreateValidation.class)
        Institution institution,

        @NotNull(groups = CreateValidation.class)
        Course course,

        @Null
        LocalDateTime createdDate,

        @Null
        LocalDateTime lastModifiedDate

) implements BaseDTO {}