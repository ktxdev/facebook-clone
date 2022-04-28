package com.ktxdev.facebookclone.likes.dao;

import com.ktxdev.facebookclone.likes.model.Like;
import com.ktxdev.facebookclone.shared.jpa.BaseDao;

public interface LikeDao extends BaseDao<Like> {
    boolean existsByLikedBy_UsernameAndPostLiked_Id(String username, long postId);
}
