package com.ktxdev.facebookclone.auth.dto;

import lombok.Data;

@Data
public class AuthenticationRequestDto {
    private String emailOrUsername;
    private String password;
}
