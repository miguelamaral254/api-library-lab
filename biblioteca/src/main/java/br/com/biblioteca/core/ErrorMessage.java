package br.com.biblioteca.core;

public record ErrorMessage(
        String code,
        String message
) {

    public static final String NO_MESSAGE_AVAILABLE = "No message available";

    public ErrorMessage() {
        this("NO_MESSAGE_AVAILABLE", NO_MESSAGE_AVAILABLE);
    }
}
