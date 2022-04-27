package com.ktxdev.facebookclone.auth.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ktxdev.facebookclone.auth.dto.AuthenticationRequestDto;
import com.ktxdev.facebookclone.auth.dto.AuthenticationResponseDto;
import com.ktxdev.facebookclone.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto requestDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getEmailOrUsername(), requestDto.getPassword()));
        } catch (LockedException | DisabledException | BadCredentialsException ex) {
            log.error("### Authentication failed: {}", ex.getMessage());
        }

        val user = (User) userDetailsService.loadUserByUsername(requestDto.getEmailOrUsername());

        Algorithm algorithm = Algorithm.HMAC256("SECRET".getBytes(StandardCharsets.UTF_8));

        val accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        val refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .sign(algorithm);

        return new AuthenticationResponseDto(accessToken, refreshToken);
    }
}
