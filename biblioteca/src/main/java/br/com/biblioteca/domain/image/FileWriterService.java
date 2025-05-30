package br.com.biblioteca.domain.image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class FileWriterService {

    private static final Logger logger = LoggerFactory.getLogger(FileWriterService.class);

    public void writeFile(File file, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
            logger.info("Arquivo escrito com sucesso: {}", file.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Erro ao escrever arquivo: {}", e.getMessage());
            throw new IOException("Erro ao escrever arquivo.", e);
        }
    }
}