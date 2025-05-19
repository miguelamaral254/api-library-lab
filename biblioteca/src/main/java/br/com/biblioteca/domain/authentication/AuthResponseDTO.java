package br.com.biblioteca.domain.authentication;

public record AuthResponseDTO(
        String token,
        String message
) {
}