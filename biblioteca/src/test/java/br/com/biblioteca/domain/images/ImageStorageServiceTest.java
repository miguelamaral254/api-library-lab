package br.com.biblioteca.domain.images;

import br.com.biblioteca.domain.image.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import static br.com.biblioteca.domain.images.factories.ImageFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageStorageServiceTest {

    private ImageStorageService imageStorageService;

    private DirectoryManager directoryManager;
    private FileNameGenerator fileNameGenerator;
    private FileWriterService fileWriterService;
    private ImageUrlBuilder imageUrlBuilder;

    private MultipartFile multipartFile;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() throws Exception {
        directoryManager = mock(DirectoryManager.class);
        fileNameGenerator = mock(FileNameGenerator.class);
        fileWriterService = mock(FileWriterService.class);
        imageUrlBuilder = mock(ImageUrlBuilder.class);

        multipartFile = mockMultipartFile();
        request = mockHttpRequest();

        imageStorageService = new ImageStorageService(
                directoryManager,
                fileNameGenerator,
                fileWriterService,
                imageUrlBuilder
        );
    }

    @Test
    @DisplayName("Should save image successfully and return its URL")
    void saveImage_success() throws IOException {
        when(fileNameGenerator.generateUniqueName(ORIGINAL_FILE_NAME)).thenReturn(GENERATED_FILE_NAME);
        when(imageUrlBuilder.buildUrl(request, GENERATED_FILE_NAME)).thenReturn(IMAGE_URL);

        String imageUrl = imageStorageService.saveImage(multipartFile, request);

        verify(directoryManager).ensureDirectoryExists("uploads/");
        verify(fileNameGenerator).generateUniqueName(ORIGINAL_FILE_NAME);
        verify(fileWriterService).writeFile(any(File.class), eq(FILE_CONTENT));
        verify(imageUrlBuilder).buildUrl(request, GENERATED_FILE_NAME);

        assertEquals(IMAGE_URL, imageUrl);
    }

    @Test
    @DisplayName("Should throw IOException when original file name is invalid")
    void saveImage_invalidFileName_throwsException() throws IOException {
        MultipartFile invalidFile = mock(MultipartFile.class);
        when(invalidFile.getOriginalFilename()).thenReturn(null);
        when(fileNameGenerator.generateUniqueName(null)).thenThrow(new IOException("Invalid file name."));

        IOException exception = assertThrows(IOException.class, () -> {
            imageStorageService.saveImage(invalidFile, request);
        });

        assertEquals("Invalid file name.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IOException when writing file fails")
    void saveImage_fileWriteFails_throwsException() throws IOException {
        when(fileNameGenerator.generateUniqueName(ORIGINAL_FILE_NAME)).thenReturn(GENERATED_FILE_NAME);
        doThrow(new IOException("Failed to write file")).when(fileWriterService).writeFile(any(File.class), eq(FILE_CONTENT));

        IOException exception = assertThrows(IOException.class, () -> {
            imageStorageService.saveImage(multipartFile, request);
        });

        assertEquals("Failed to write file", exception.getMessage());
        verify(fileWriterService).writeFile(any(File.class), eq(FILE_CONTENT));
    }
}