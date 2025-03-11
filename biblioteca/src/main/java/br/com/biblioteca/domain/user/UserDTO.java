package br.com.biblioteca.domain.user;

import br.com.biblioteca.core.BaseDTO;
import br.com.biblioteca.domain.phone.Phone;
import br.com.biblioteca.domain.phone.PhoneDTO;
import br.com.biblioteca.domain.user.enums.Course;
import br.com.biblioteca.domain.user.enums.Institution;
import br.com.biblioteca.domain.user.enums.Role;
import br.com.biblioteca.validations.groups.CreateValidation;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public record UserDTO(
        @Null
        Long id,

        @NotBlank(groups = CreateValidation.class)
        String name,

        @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos")
        String cpf,

        @NotBlank(groups = CreateValidation.class)
        @Email(message = "Email com formato inválido")
        String email,

        @NotNull(groups = CreateValidation.class)
        @Enumerated(EnumType.STRING)
        Institution institution,

        @NotNull(groups = CreateValidation.class)
        @Enumerated(EnumType.STRING)
        Course course,

        @NotNull(groups = CreateValidation.class)
        @Enumerated(EnumType.STRING)
        Role role,

        List<PhoneDTO> phones,

        @NotBlank(groups = CreateValidation.class)
        String password,

        @NotNull(groups = CreateValidation.class)
        Boolean enabled,

        LocalDateTime createdDate,

        LocalDateTime lastModifiedDate
) implements BaseDTO {}
