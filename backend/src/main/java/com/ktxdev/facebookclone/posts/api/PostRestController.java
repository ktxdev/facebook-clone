package com.ktxdev.facebookclone.posts.api;

import com.ktxdev.facebookclone.posts.dto.PostDto;
import com.ktxdev.facebookclone.posts.model.Post;
import com.ktxdev.facebookclone.posts.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class PostRestController {

    private final PostService postService;

    @PostMapping(
            value = "v1/posts",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Post> createPost(
            @RequestPart(value = "caption", required = false) String caption,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest request
    ) {
        val uri = ServletUriComponentsBuilder.fromRequest(request).build().toUri();
        return ResponseEntity.created(uri)
                .body(postService.createPost(new PostDto(caption, Optional.ofNullable(file))));
    }
}
