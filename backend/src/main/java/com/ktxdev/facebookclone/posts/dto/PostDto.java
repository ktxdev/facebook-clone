package com.ktxdev.facebookclone.posts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private String text;

    private Optional<MultipartFile> multipartFile;
}
