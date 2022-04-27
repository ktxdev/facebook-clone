package com.ktxdev.facebookclone.users.service.impl;

import com.ktxdev.facebookclone.filestore.api.FileStoreRestController;
import com.ktxdev.facebookclone.filestore.service.FileStoreService;
import com.ktxdev.facebookclone.shared.exceptions.InvalidRequestException;
import com.ktxdev.facebookclone.shared.exceptions.RecordNotFoundException;
import com.ktxdev.facebookclone.tokens.service.TokenService;
import com.ktxdev.facebookclone.users.api.UserRestController;
import com.ktxdev.facebookclone.users.model.User;
import com.ktxdev.facebookclone.users.dao.UserDao;
import com.ktxdev.facebookclone.users.dto.UserCreateDTO;
import com.ktxdev.facebookclone.users.dto.UserPasswordUpdateDTO;
import com.ktxdev.facebookclone.users.dto.UserUpdateDTO;
import com.ktxdev.facebookclone.users.service.UserService;
import com.ktxdev.facebookclone.users.service.events.UserSignUpSuccessfulEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.Principal;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;

    private final FileStoreService fileStoreService;

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

        val token = tokenService.generateToken(user);

        val url = linkTo(methodOn(UserRestController.class)
                .verifyEmail(user.getUsername(), token.getToken()))
                .toUri()
                .toString();

        applicationEventPublisher.publishEvent(new UserSignUpSuccessfulEvent(user, url));

        return user;
    }

    @Override
    public User updateMyUsername(UserUpdateDTO userUpdateDTO) {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        val user = findByUsernameOrEmail(authentication.getName());

        if (userDao.existsByUsernameAndIdIsNot(userUpdateDTO.getUsername(), user.getId()))
            throw new InvalidRequestException("Username already in use");
        
        user.setUsername(userUpdateDTO.getUsername());
        return userDao.save(user);
    }

    @Override
    @Transactional
    public User verifyEmail(String username, String tokenString) {
        val user = userDao.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User with username: %s not found", username)));

        if (!tokenService.isValidToken(tokenString))
            throw new InvalidRequestException("Token is invalid");

        val token = tokenService.findByToken(tokenString);
        if (!token.getOwner().getEmail().equals(username))
            throw new InvalidRequestException("Token is invalid");

        user.setVerified(true);
        tokenService.useToken(token);

        return userDao.save(user);
    }

    @Override
    public User removeProfilePicture(long id) {
        return null;
    }

    @Override
    public User uploadProfilePicture(MultipartFile file, Principal principal) {
        val user = findByUsernameOrEmail(principal.getName());
        val filename =  fileStoreService.save(user.getUsername(), file);
        val profilePictureUrl = String.format("http://localhost:8080/api/opn/v1/filestore/%s?directory=%s", filename, user.getUsername());
//                linkTo(methodOn(FileStoreRestController.class)
//                .download(filename, user.getUsername()))
//                .toUri()
//                .toString();

        user.setProfilePictureUrl(profilePictureUrl);
        return userDao.save(user);
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
