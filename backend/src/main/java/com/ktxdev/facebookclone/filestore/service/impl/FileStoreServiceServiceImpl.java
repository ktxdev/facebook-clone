package com.ktxdev.facebookclone.filestore.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.IOUtils;
import com.ktxdev.facebookclone.filestore.service.FileStoreService;
import com.ktxdev.facebookclone.shared.exceptions.FileStorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStoreServiceServiceImpl implements FileStoreService {

    @Value("${aws.bucket}")
    private String bucket;
    private final AmazonS3 s3;

    @Override
    public String save(String directory, MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.addUserMetadata("Type", file.getContentType());
        metadata.addUserMetadata("Length", String.valueOf(file.getSize()));

        String originalFilename = nonNull(file.getOriginalFilename()) ? file.getOriginalFilename()
                .replaceAll(" ", "") : file.getName();

        String filename = String.format("%s-%s", UUID.randomUUID(), originalFilename);
        String path = String.format("%s/%s", bucket, directory);

        try {
            s3.putObject(path, filename, file.getInputStream(), metadata);
            return filename;
        } catch (IOException | AmazonServiceException e) {
            log.debug("### File upload failed: {}", e.getMessage());
            e.printStackTrace();
            throw new FileStorageException("File upload failed.");
        }
    }

    @Override
    public byte[] download(String directory, String filename) {
        String path = String.format("%s/%s", bucket, directory);

        try {
            val object = s3.getObject(path, filename);
            return IOUtils.toByteArray(object.getObjectContent());
        } catch (AmazonServiceException | IOException e) {
            log.debug("### File download failed: {}", e.getMessage());
            e.printStackTrace();
            throw new FileStorageException("Failed to download file");
        }
    }
}
