package com.ktxdev.facebookclone.users.api;

import com.ktxdev.facebookclone.users.model.User;
import com.ktxdev.facebookclone.users.dto.UserCreateDTO;
import com.ktxdev.facebookclone.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

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

    @PostMapping("opn/v1/users/verify-email")
    public ResponseEntity<User> verifyEmail(
            @RequestParam String username,
            @RequestParam String token
    ) {
        return ResponseEntity.ok(userService.verifyEmail(username, token));
    }

    @PostMapping(
            value = "/v1/users/upload-profile-picture",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> uploadProfilePicture(
            @RequestPart(name = "file")MultipartFile file,
            Principal principal
    ) {
        return ResponseEntity.ok(userService.uploadProfilePicture(file, principal));
    }
}
