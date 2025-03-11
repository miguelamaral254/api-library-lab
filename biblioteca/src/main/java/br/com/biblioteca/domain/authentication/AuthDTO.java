package br.com.biblioteca.domain.authentication;

public record AuthDTO(
        Long idUser,
        String token,
        String email,
        String role,
        String message
) {
}