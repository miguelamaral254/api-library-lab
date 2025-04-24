package br.com.biblioteca.infrastructure.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageConf {

    private static final String UPLOAD_DIR = "uploads/";
    private static final String PROD_BASE_URL = "https://porto-library.com.br/appevento/uploads/";
    private static final Logger logger = LoggerFactory.getLogger(ImageConf.class);

    public String saveImage(MultipartFile file, HttpServletRequest request) throws IOException {
        logger.info("Iniciando o processo de salvamento da imagem...");

        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            logger.info("Diretório não encontrado. Criando diretório: {}", UPLOAD_DIR);
            boolean dirCreated = directory.mkdirs();
            if (dirCreated) {
                logger.info("Diretório criado com sucesso!");
            } else {
                logger.error("Falha ao criar o diretório {}", UPLOAD_DIR);
            }
        } else {
            logger.info("Diretório já existe.");
        }

        String originalFileName = file.getOriginalFilename();
        logger.info("Nome do arquivo original: {}", originalFileName);

        if (originalFileName == null || !originalFileName.contains(".")) {
            logger.error("Nome de arquivo inválido: {}", originalFileName);
            throw new IOException("Nome de arquivo inválido.");
        }

        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileExtension;
        logger.info("Nome único gerado para o arquivo: {}", fileName);

        File imageFile = new File(UPLOAD_DIR, fileName);
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            logger.info("Salvando a imagem no arquivo: {}", imageFile.getAbsolutePath());
            fos.write(file.getBytes());
            logger.info("Imagem salva com sucesso.");
        } catch (IOException e) {
            logger.error("Erro ao salvar o arquivo de imagem: {}", e.getMessage());
            throw new IOException("Erro ao salvar o arquivo de imagem.", e);
        }

        String baseUrl = request.getScheme() + "://" + request.getServerName() +
                (request.getServerPort() != 80 && request.getServerPort() != 443 ? ":" + request.getServerPort() : "");

        if (baseUrl.contains("porto-library.com.br")) {
            logger.info("Ambiente de produção detectado. URL de produção: {}", PROD_BASE_URL + fileName);
            return PROD_BASE_URL + fileName;
        }

        logger.info("URL gerada para o arquivo: {}", baseUrl + "/uploads/" + fileName);
        return baseUrl + "/uploads/" + fileName;
    }
}
