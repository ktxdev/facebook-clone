package com.ktxdev.facebookclone.users.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserUpdateDTO {
    @NotNull(message = "Username should be provided")
    private String username;
}
