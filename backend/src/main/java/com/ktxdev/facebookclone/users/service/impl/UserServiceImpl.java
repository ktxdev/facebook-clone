package com.ktxdev.facebookclone.users.service.impl;

import com.ktxdev.facebookclone.shared.exceptions.InvalidRequestException;
import com.ktxdev.facebookclone.users.model.User;
import com.ktxdev.facebookclone.users.dao.UserDao;
import com.ktxdev.facebookclone.users.dto.UserCreateDTO;
import com.ktxdev.facebookclone.users.dto.UserPasswordUpdateDTO;
import com.ktxdev.facebookclone.users.dto.UserUpdateDTO;
import com.ktxdev.facebookclone.users.service.UserService;
import com.ktxdev.facebookclone.users.service.events.UserSignUpSuccessfulEvent;
import com.ktxdev.facebookclone.users.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public User signUp(UserCreateDTO userCreateDTO) {
        log.debug("### Signing up user: {}", userCreateDTO.getEmail());

        if (userDao.existsByEmail(userCreateDTO.getEmail()))
            throw new InvalidRequestException("Email is already registered.");

        if (userDao.existsByUsername(userCreateDTO.getUsername()))
            throw new InvalidRequestException("Username already taken");

        User user = userMapper.userCreateDTOToUser(userCreateDTO);
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));

        userDao.saveAndFlush(user);

        applicationEventPublisher.publishEvent(new UserSignUpSuccessfulEvent(user));

        return user;
    }

    @Override
    public User updateUser(UserUpdateDTO userUpdateDTO) {
        return null;
    }

    @Override
    public User changeEmail(long id, String email) {
        return null;
    }

    @Override
    public User verifyEmail(long id, String email) {
        return null;
    }

    @Override
    public User removeProfilePicture(long id) {
        return null;
    }

    @Override
    public User uploadProfilePicture(long id, MultipartFile file) {
        return null;
    }

    @Override
    public User updatePassword(UserPasswordUpdateDTO userPasswordUpdateDTO) {
        return null;
    }

    @Override
    public User deleteMyAccount() {
        return null;
    }

    @Override
    public User getMyAccountDetails() {
        return null;
    }
}
