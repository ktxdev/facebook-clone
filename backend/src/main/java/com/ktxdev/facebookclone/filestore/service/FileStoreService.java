package com.ktxdev.facebookclone.filestore.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileStoreService {
    String save(String directory, MultipartFile file);

    byte[] download(String directory, String filename);
}
