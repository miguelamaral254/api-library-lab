package br.com.biblioteca.domain.user;


import br.com.biblioteca.core.GenericDataQuery;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>, GenericDataQuery {


    public Optional<User> findByEmail(String email);


    boolean existsByCpf(@Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos") String cpf);


    boolean existsByEmail(@NotBlank @Email(message = "Email com formato inválido") String email);
}
