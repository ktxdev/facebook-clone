package com.ktxdev.facebookclone.posts.model;

import com.ktxdev.facebookclone.users.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    private String text;

    private String photoUrl;

    @ManyToOne
    private User createdBy;

    private LocalDateTime createdAt;
}
