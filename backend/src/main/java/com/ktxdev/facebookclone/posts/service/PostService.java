package com.ktxdev.facebookclone.posts.service;

import com.ktxdev.facebookclone.posts.dto.PostDto;
import com.ktxdev.facebookclone.posts.model.Post;

public interface PostService {
    Post createPost(PostDto postDto);
}
