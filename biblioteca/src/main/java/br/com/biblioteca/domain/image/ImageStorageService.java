package br.com.biblioteca.domain.image;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Service
public class ImageStorageService {

    private static final String UPLOAD_DIR = "uploads/";

    private final DirectoryManager directoryManager;
    private final FileNameGenerator fileNameGenerator;
    private final FileWriterService fileWriterService;
    private final ImageUrlBuilder imageUrlBuilder;

    public ImageStorageService(
            DirectoryManager directoryManager,
            FileNameGenerator fileNameGenerator,
            FileWriterService fileWriterService,
            ImageUrlBuilder imageUrlBuilder
    ) {
        this.directoryManager = directoryManager;
        this.fileNameGenerator = fileNameGenerator;
        this.fileWriterService = fileWriterService;
        this.imageUrlBuilder = imageUrlBuilder;
    }

    public String saveImage(MultipartFile file, HttpServletRequest request) throws IOException {
        directoryManager.ensureDirectoryExists(UPLOAD_DIR);

        String fileName = fileNameGenerator.generateUniqueName(file.getOriginalFilename());
        File imageFile = new File(UPLOAD_DIR, fileName);

        fileWriterService.writeFile(imageFile, file.getBytes());

        return imageUrlBuilder.buildUrl(request, fileName);
    }
}