package com.ktxdev.facebookclone.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ktxdev.facebookclone.users.dto.UserCreateDTO;
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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private boolean verified;

    private String profilePictureUrl;

    public static User fromDto(UserCreateDTO userCreateDTO) {
        return User.builder()
                .email(userCreateDTO.getEmail())
                .username(userCreateDTO.getUsername())
                .build();
    }
}
