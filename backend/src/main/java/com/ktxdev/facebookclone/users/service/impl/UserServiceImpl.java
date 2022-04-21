package com.ktxdev.facebookclone.users.service.impl;

import com.ktxdev.facebookclone.shared.exceptions.InvalidRequestException;
import com.ktxdev.facebookclone.shared.exceptions.RecordNotFoundException;
import com.ktxdev.facebookclone.users.model.User;
import com.ktxdev.facebookclone.users.dao.UserDao;
import com.ktxdev.facebookclone.users.dto.UserCreateDTO;
import com.ktxdev.facebookclone.users.dto.UserPasswordUpdateDTO;
import com.ktxdev.facebookclone.users.dto.UserUpdateDTO;
import com.ktxdev.facebookclone.users.service.UserService;
import com.ktxdev.facebookclone.users.service.events.UserSignUpSuccessfulEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public User signUp(UserCreateDTO userCreateDTO) {
        log.debug("### Signing up user: {}", userCreateDTO.getEmail());

        if (userDao.existsByEmail(userCreateDTO.getEmail()))
            throw new InvalidRequestException("Email is already registered.");

        if (userDao.existsByUsername(userCreateDTO.getUsername()))
            throw new InvalidRequestException("Username already taken");

        User user = User.fromDto(userCreateDTO);
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));

        userDao.saveAndFlush(user);

        applicationEventPublisher.publishEvent(new UserSignUpSuccessfulEvent(user));

        return user;
    }

    @Override
    public User updateMyUsername(UserUpdateDTO userUpdateDTO) {
        if (userDao.existsByUsername(userUpdateDTO.getUsername()))
            throw new InvalidRequestException("Username already in use");

        val authentication = SecurityContextHolder.getContext().getAuthentication();
        val user = findByUsernameOrEmail(authentication.getName());
        user.setUsername(userUpdateDTO.getUsername());
        return userDao.save(user);
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
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        val user = findByUsernameOrEmail(authentication.getName());
        if (passwordEncoder.matches(userPasswordUpdateDTO.getOldPassword(), user.getPassword())) {
            throw new InvalidRequestException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(userPasswordUpdateDTO.getNewPassword()));

        return userDao.save(user);
    }

    @Override
    public void deleteMyAccount() {
        val usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        val user = findByUsernameOrEmail(usernameOrEmail);

        userDao.delete(user);
    }

    @Override
    public User getMyAccountDetails() {
        val usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByUsernameOrEmail(usernameOrEmail);
    }

    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) {
        return userDao.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User with the email or username: %s not found", usernameOrEmail)));
    }
}
