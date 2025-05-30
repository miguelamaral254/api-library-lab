package br.com.biblioteca.domain.image;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class FileNameGenerator {

    public String generateUniqueName(String originalName) throws IOException {
        if (originalName == null || !originalName.contains(".")) {
            throw new IOException("Nome de arquivo inválido.");
        }

        String extension = originalName.substring(originalName.lastIndexOf("."));
        return UUID.randomUUID().toString() + extension;
    }
}