package com.ktxdev.facebookclone.auth.api;

import com.ktxdev.facebookclone.auth.dto.AuthenticationRequestDto;
import com.ktxdev.facebookclone.auth.dto.AuthenticationResponseDto;
import com.ktxdev.facebookclone.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/opn/v1/authenticate")
public class AuthenticationRestController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @RequestBody AuthenticationRequestDto requestDto
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(requestDto));
    }

}
