package com.info6250.eventpawz.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class FileUploadUtil {

    public static String saveFile(String fileName, MultipartFile multipartFile)
            throws IOException {

        Resource resource = new ClassPathResource("static");
        String uploadDir = resource.getFile().getAbsolutePath();
        Path uploadPath = Paths.get(uploadDir);
        Path filePath = uploadPath.resolve(fileName);

        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath);
        }


        try (InputStream inputStream = multipartFile.getInputStream()) {

            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }

        return filePath.toString();
    }
}
