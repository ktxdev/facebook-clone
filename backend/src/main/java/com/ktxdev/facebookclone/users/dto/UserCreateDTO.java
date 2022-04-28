package com.ktxdev.facebookclone.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {
    @NotBlank(message = "Email should be provided")
    private String email;

    @NotBlank(message = "Username should be provided")
    private String username;

    @NotBlank(message = "Password should be provided")
    private String password;
}
