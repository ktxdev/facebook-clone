package com.ktxdev.facebookclone.posts.api;

import com.ktxdev.facebookclone.posts.dto.PostDto;
import com.ktxdev.facebookclone.posts.model.Post;
import com.ktxdev.facebookclone.posts.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class PostRestController {

    private final PostService postService;

    @PostMapping("v1/posts")
    public ResponseEntity<Post> createPost(
            @RequestBody PostDto postDto,
            HttpServletRequest request
    ) {
        val uri = ServletUriComponentsBuilder.fromRequest(request).build().toUri();
        return ResponseEntity.created(uri).body(postService.createPost(postDto));
    }
}
