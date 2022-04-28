package com.ktxdev.facebookclone.users.service.impl;

import com.ktxdev.facebookclone.filestore.service.FileStoreService;
import com.ktxdev.facebookclone.shared.exceptions.InvalidRequestException;
import com.ktxdev.facebookclone.shared.exceptions.RecordNotFoundException;
import com.ktxdev.facebookclone.tokens.service.TokenService;
import com.ktxdev.facebookclone.users.api.UserRestController;
import com.ktxdev.facebookclone.users.dao.UserDao;
import com.ktxdev.facebookclone.users.dto.UserCreateDTO;
import com.ktxdev.facebookclone.users.dto.UserPasswordUpdateDTO;
import com.ktxdev.facebookclone.users.dto.UserUpdateDTO;
import com.ktxdev.facebookclone.users.model.User;
import com.ktxdev.facebookclone.users.service.UserService;
import com.ktxdev.facebookclone.users.service.events.UserSignUpSuccessfulEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Arrays;

import static org.apache.http.entity.ContentType.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
            throw new InvalidRequestException("Email is already registered");

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

        log.info("### Verification Link: {}", url);

        applicationEventPublisher.publishEvent(new UserSignUpSuccessfulEvent(user, url));

        return user;
    }

    @Override
    public User updateMyUsername(UserUpdateDTO userUpdateDTO) {
        val username = SecurityContextHolder.getContext().getAuthentication().getName();
        val user = findByUsernameOrEmail(username);

        if (userDao.existsByUsernameAndIdIsNot(userUpdateDTO.getUsername(), user.getId()))
            throw new InvalidRequestException("Username already in use");

        user.setUsername(userUpdateDTO.getUsername());
        return userDao.save(user);
    }

    @Override
    @Transactional
    public User verifyEmail(String username, String tokenString) {
        val user = findByUsernameOrEmail(username);

        if (!tokenService.isValidToken(tokenString))
            throw new InvalidRequestException("Token is invalid");

        val token = tokenService.findByToken(tokenString);
        if (!token.getOwner().getUsername().equals(username))
            throw new InvalidRequestException("Token is invalid");

        user.setVerified(true);
        tokenService.useToken(token);

        return userDao.save(user);
    }

    @Override
    public User removeProfilePicture(Principal principal) {
        val user = findByUsernameOrEmail(principal.getName());
        user.setProfilePictureUrl(null);
        return userDao.save(user);
    }

    @Override
    public User uploadProfilePicture(MultipartFile file, Principal principal) {

        if (!Arrays.asList(IMAGE_PNG.getMimeType(), IMAGE_JPEG.getMimeType(), IMAGE_GIF.getMimeType())
                .contains(file.getContentType()))
            throw new InvalidRequestException("File must be an image");

        val user = findByUsernameOrEmail(principal.getName());

        val filename =  fileStoreService.save(user.getUsername(), file);

        val profilePictureUrl = String.format(
                "http://localhost:8080/api/opn/v1/filestore/%s?directory=%s",
                filename,
                user.getUsername());

        user.setProfilePictureUrl(profilePictureUrl);
        return userDao.save(user);
    }

    @Override
    public User updatePassword(UserPasswordUpdateDTO userPasswordUpdateDTO) {
        val user = findByUsernameOrEmail(userPasswordUpdateDTO.getPrincipal().getName());

        if (passwordEncoder.matches(userPasswordUpdateDTO.getOldPassword(), user.getPassword())) {
            throw new InvalidRequestException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(userPasswordUpdateDTO.getNewPassword()));
        return userDao.save(user);
    }

    @Override
    public void deleteMyAccount(Principal principal) {
        val user = findByUsernameOrEmail(principal.getName());
        userDao.delete(user);
    }

    @Override
    public User getMyAccountDetails(Principal principal) {
        return findByUsernameOrEmail(principal.getName());
    }

    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) {
        return userDao.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User with the email or username: %s not found", usernameOrEmail)));
    }
}
