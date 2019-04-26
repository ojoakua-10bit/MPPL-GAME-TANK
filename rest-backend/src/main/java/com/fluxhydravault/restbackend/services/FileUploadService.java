package com.fluxhydravault.restbackend.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    void uploadImage(String playerID, MultipartFile file);
    void uploadAsset(String itemID, MultipartFile file);
}
