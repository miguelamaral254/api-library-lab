package br.com.biblioteca.domain.user;

import br.com.biblioteca.core.BaseEntity;
import br.com.biblioteca.domain.user.enums.Course;
import br.com.biblioteca.domain.user.enums.Institution;
import br.com.biblioteca.domain.user.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_users")
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String imageUrl;

    @Column(unique = true)
    private String cpf;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Course course;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Institution institution;

    @NotNull
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Column(nullable = false)
    private String number;

    @NotBlank
    @Column(nullable = false)
    private String countryCode;
}