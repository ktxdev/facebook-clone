package com.ktxdev.facebookclone.likes.model;

import com.ktxdev.facebookclone.posts.model.Post;
import com.ktxdev.facebookclone.users.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Post postLiked;

    @ManyToOne
    private User likedBy;
}
