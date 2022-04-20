package com.ktxdev.facebookclone.users.api;

import com.ktxdev.facebookclone.users.model.User;
import com.ktxdev.facebookclone.users.dto.UserCreateDTO;
import com.ktxdev.facebookclone.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class UserRestController {

    private final UserService userService;

    @PostMapping("opn/v1/users/sign-up")
    public ResponseEntity<User> signUp(
            @RequestBody @Valid UserCreateDTO userCreateDTO,
            HttpServletRequest request
    ) {
        val uri = ServletUriComponentsBuilder.fromRequestUri(request).build().toUri();
        return ResponseEntity.created(uri)
                .body(userService.signUp(userCreateDTO));
    }
}
