package com.fluxhydravault.restbackend.utils;

import com.fluxhydravault.restbackend.InputFormatException;
import com.fluxhydravault.restbackend.InternalServerErrorException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploader {
    private Path assetsPath;
    private Path imagesPath;

    public FileUploader() {
        String os = System.getProperty("os.name");
        String rootLocation, assetsLocation, imagesLocation;
        if (os.startsWith("Windows")) {
            rootLocation = "C:\\server\\";
            assetsLocation = "C:\\server\\assets\\";
            imagesLocation = "C:\\server\\images\\";
        }
        else {
            rootLocation = System.getenv("HOME") + "/server/";
            assetsLocation = System.getenv("HOME") + "/server/assets/";
            imagesLocation = System.getenv("HOME") + "/server/images/";
        }

        assetsPath = Paths.get(assetsLocation);
        imagesPath = Paths.get(imagesLocation);

        try {
            Files.createDirectory(Paths.get(rootLocation));
        } catch (IOException e) {
            System.out.println("Path already exists: " + e.getMessage());
        }
        try {
            Files.createDirectory(assetsPath);
        } catch (IOException e) {
            System.out.println("Path already exists: " + e.getMessage());
        }
        try {
            Files.createDirectory(imagesPath);
        } catch (IOException e) {
            System.out.println("Path already exists: " + e.getMessage());
        }
    }

    public void uploadImage(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (filename.isEmpty()) throw new InputFormatException("File is empty.");

        String type = file.getContentType();
        if (type != null && (type.equals("image/jpeg") || type.equals("image/png"))) {
            try {
                Files.copy(file.getInputStream(), imagesPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new InternalServerErrorException("An error has occurred when uploading your file.");
            }
        } else throw new InputFormatException("Invalid image format!");
    }

    public void uploadAsset(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (filename.isEmpty()) throw new InputFormatException("File is empty.");

        try {
            Files.copy(file.getInputStream(), assetsPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new InternalServerErrorException("An error has occurred when uploading your file.");
        }
    }
}
