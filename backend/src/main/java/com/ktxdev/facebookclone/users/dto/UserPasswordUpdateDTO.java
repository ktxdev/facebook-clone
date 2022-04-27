package com.ktxdev.facebookclone.users.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.security.Principal;

@Data
public class UserPasswordUpdateDTO {

    @JsonIgnore
    private Principal principal;

    @NotNull(message = "Old password should be provided")
    private String oldPassword;

    @NotNull(message = "New password should be provided")
    private String newPassword;
}
