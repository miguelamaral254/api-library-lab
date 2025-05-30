package br.com.biblioteca.domain.image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class DirectoryManager {

    private static final Logger logger = LoggerFactory.getLogger(DirectoryManager.class);

    public void ensureDirectoryExists(String path) throws IOException {
        File directory = new File(path);
        if (!directory.exists()) {
            logger.info("Criando diretório: {}", path);
            if (!directory.mkdirs()) {
                logger.error("Falha ao criar o diretório: {}", path);
                throw new IOException("Falha ao criar diretório.");
            }
        } else {
            logger.info("Diretório já existe: {}", path);
        }
    }
}
