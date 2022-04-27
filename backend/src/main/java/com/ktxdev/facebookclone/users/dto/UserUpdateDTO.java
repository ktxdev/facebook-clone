package com.ktxdev.facebookclone.users.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.security.Principal;

@Data
public class UserUpdateDTO {
    @JsonIgnore
    private Principal principal;

    @NotNull(message = "Username should be provided")
    private String username;
}
