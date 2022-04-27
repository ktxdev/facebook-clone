package com.ktxdev.facebookclone.users.service;

import com.ktxdev.facebookclone.users.model.User;
import com.ktxdev.facebookclone.users.dto.UserCreateDTO;
import com.ktxdev.facebookclone.users.dto.UserPasswordUpdateDTO;
import com.ktxdev.facebookclone.users.dto.UserUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

public interface UserService {
    User signUp(UserCreateDTO userCreateDTO);

    User updateMyUsername(UserUpdateDTO userUpdateDTO);

    User verifyEmail(String username, String token);

    User removeProfilePicture(Principal principal);

    User uploadProfilePicture(MultipartFile file, Principal principal);

    User updatePassword(UserPasswordUpdateDTO userPasswordUpdateDTO);

    void deleteMyAccount(Principal principal);

    User getMyAccountDetails(Principal principal);

    User findByUsernameOrEmail(String usernameOrEmail);
}
