package com.fluxhydravault.restbackend.services;

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
public class FileUploadServiceImpl implements FileUploadService {
    private Path assetsPath;
    private Path playerImagesPath;
    private Path adminImagesPath;

    public FileUploadServiceImpl() {
        String os = System.getProperty("os.name");
        String rootLocation, assetsLocation, playerImagesLocation, adminImagesLocation;
        if (os.startsWith("Windows")) {
            rootLocation = "C:\\server\\";
            assetsLocation = "C:\\server\\assets\\";
            playerImagesLocation = "C:\\server\\images\\";
            adminImagesLocation = "C:\\server\\images\\admin\\";

        }
        else {
            rootLocation = System.getenv("HOME") + "/server/";
            assetsLocation = System.getenv("HOME") + "/server/assets/";
            playerImagesLocation = System.getenv("HOME") + "/server/images/";
            adminImagesLocation = System.getenv("HOME") + "/server/images/admin/";
        }

        assetsPath = Paths.get(assetsLocation);
        playerImagesPath = Paths.get(playerImagesLocation);
        adminImagesPath = Paths.get(adminImagesLocation);

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
            Files.createDirectory(playerImagesPath);
        } catch (IOException e) {
            System.out.println("Path already exists: " + e.getMessage());
        }
        try {
            Files.createDirectory(adminImagesPath);
        } catch (IOException e) {
            System.out.println("Path already exists: " + e.getMessage());
        }
    }

    public void uploadImage(String playerID, boolean isAdmin, MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (filename.isEmpty()) throw new InputFormatException("File is empty.");

        String type = file.getContentType();
        if (type != null && (type.equals("image/jpeg") || type.equals("image/png"))) {
            try {
                if (isAdmin) {
                    Files.copy(file.getInputStream(), adminImagesPath.resolve(playerID + filename), StandardCopyOption.REPLACE_EXISTING);
                }
                else {
                    Files.copy(file.getInputStream(), playerImagesPath.resolve(playerID + filename), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new InternalServerErrorException("An error has occurred when uploading your file.");
            }
        } else throw new InputFormatException("Invalid image format!");
    }

    public void uploadAsset(String itemID, MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (filename.isEmpty()) throw new InputFormatException("File is empty.");

        try {
            Files.copy(file.getInputStream(), assetsPath.resolve(itemID + filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new InternalServerErrorException("An error has occurred when uploading your file.");
        }
    }
}
