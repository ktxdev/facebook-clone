package com.ktxdev.facebookclone.users.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserPasswordUpdateDTO {
    @NotNull(message = "Old password should be provided")
    private String oldPassword;

    @NotNull(message = "New password should be provided")
    private String newPassword;
}
