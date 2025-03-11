package br.com.biblioteca.domain.user;

import br.com.biblioteca.core.BaseEntity;
import br.com.biblioteca.domain.phone.Phone;
import br.com.biblioteca.domain.user.enums.Course;
import br.com.biblioteca.domain.user.enums.Institution;
import br.com.biblioteca.domain.user.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos")
    @Column(unique = true)
    private String cpf;

    @NotBlank
    @Column(nullable = false)
    @Email(message = "Email com formato inválido")
    private String email;

    Institution institution;
    Course course;

    @NotBlank
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones;

    @NotNull
    @Column(nullable = false)
    private String password;

    public void validateDocument() {
        if (role != null) {
            if (role == Role.SUPER_USER || role == Role.MONITOR || role == Role.USER) {
                if (cpf == null || cpf.isEmpty() || !cpf.matches("\\d{11}")) {
                    throw new IllegalArgumentException("CPF inválido. Deve ter 11 dígitos.");
                }
            }
        } else {
            throw new IllegalArgumentException("Role inválida.");
        }
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        validateDocument();
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();
        validateDocument();
    }
}