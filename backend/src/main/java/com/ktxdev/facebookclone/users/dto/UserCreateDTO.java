package com.ktxdev.facebookclone.users.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserCreateDTO {
    @NotNull(message = "Email should be provided")
    private String email;

    @NotNull(message = "Username should be provided")
    private String username;

    @NotNull(message = "Password should be provided")
    private String password;
}
