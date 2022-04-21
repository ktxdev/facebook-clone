package com.ktxdev.facebookclone.users.service;

import com.ktxdev.facebookclone.users.model.User;
import com.ktxdev.facebookclone.users.dto.UserCreateDTO;
import com.ktxdev.facebookclone.users.dto.UserPasswordUpdateDTO;
import com.ktxdev.facebookclone.users.dto.UserUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User signUp(UserCreateDTO userCreateDTO);

    User updateMyUsername(UserUpdateDTO userUpdateDTO);

    User changeEmail(long id, String email);

    User verifyEmail(long id, String email);

    User removeProfilePicture(long id);

    User uploadProfilePicture(long id, MultipartFile file);

    User updatePassword(UserPasswordUpdateDTO userPasswordUpdateDTO);

    void deleteMyAccount();

    User getMyAccountDetails();

    User findByUsernameOrEmail(String usernameOrEmail);
}
