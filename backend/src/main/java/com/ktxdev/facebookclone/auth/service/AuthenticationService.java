package com.ktxdev.facebookclone.auth.service;

import com.ktxdev.facebookclone.auth.dto.AuthenticationRequestDto;
import com.ktxdev.facebookclone.auth.dto.AuthenticationResponseDto;

public interface AuthenticationService {
    AuthenticationResponseDto authenticate(AuthenticationRequestDto requestDto);
}
