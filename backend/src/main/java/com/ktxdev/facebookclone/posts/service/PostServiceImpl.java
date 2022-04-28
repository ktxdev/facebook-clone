package com.ktxdev.facebookclone.posts.service;

import com.ktxdev.facebookclone.filestore.service.FileStoreService;
import com.ktxdev.facebookclone.posts.dao.PostDao;
import com.ktxdev.facebookclone.posts.dto.PostDto;
import com.ktxdev.facebookclone.posts.model.Post;
import com.ktxdev.facebookclone.shared.exceptions.InvalidRequestException;
import com.ktxdev.facebookclone.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.apache.http.entity.ContentType.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostDao postDao;

    private final UserService userService;

    private final FileStoreService fileStoreService;

    @Override
    public Post createPost(PostDto postDto) {
        if (!StringUtils.hasText(postDto.getText()) && postDto.getMultipartFile().isEmpty())
            throw new InvalidRequestException("Post picture and/or text should be provided");

        val username = SecurityContextHolder.getContext().getAuthentication().getName();

        val user = userService.findByUsernameOrEmail(username);

        val post = Post.builder()
                .text(postDto.getText())
                .createdBy(user)
                .createdAt(LocalDateTime.now());

        String photoUrl = null;
        Optional<MultipartFile> multipartFile = postDto.getMultipartFile();
        if (multipartFile.isPresent()) {
            val file = multipartFile.get();

            if (!Arrays.asList(IMAGE_PNG.getMimeType(), IMAGE_JPEG.getMimeType(), IMAGE_GIF.getMimeType())
                    .contains(file.getContentType()))
                throw new InvalidRequestException("File must be an image");

            photoUrl = fileStoreService.save(username + "/posts", file);
        }

        post.photoUrl(photoUrl);

        return postDao.save(post.build());
    }
}
