package com.ktxdev.facebookclone.users;

import com.ktxdev.facebookclone.filestore.service.FileStoreService;
import com.ktxdev.facebookclone.shared.exceptions.InvalidRequestException;
import com.ktxdev.facebookclone.tokens.model.Token;
import com.ktxdev.facebookclone.tokens.service.TokenService;
import com.ktxdev.facebookclone.users.dao.UserDao;
import com.ktxdev.facebookclone.users.dto.UserCreateDTO;
import com.ktxdev.facebookclone.users.model.User;
import com.ktxdev.facebookclone.users.service.UserService;
import com.ktxdev.facebookclone.users.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private UserDao userDao;

    @Mock
    private TokenService tokenService;

    @Mock
    private FileStoreService fileStoreService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(
                userDao,
                tokenService,
                passwordEncoder,
                fileStoreService,
                applicationEventPublisher);
    }

    @Test
    void givenValidUserDto_whenSignUp_thenShouldSucceed() {
        // Given
        String email = "sean@ktxdev.com";
        String password = "Password";
        String username = "sean";

        String encodedPassword = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        val expectedUser = User.builder()
                .email(email)
                .username(username)
                .password(encodedPassword)
                .build();

        when(userDao.saveAndFlush(expectedUser)).thenReturn(expectedUser);


        val token = Token.builder()
                .token("64DAS5")
                .owner(expectedUser)
                .build();

        when(tokenService.generateToken(expectedUser)).thenReturn(token);

        val userCreateDto = UserCreateDTO.builder()
                .email(email)
                .password(password)
                .username(username)
                .build();

        // When
        val actualUser = underTest.signUp(userCreateDto);

        // Then
        assertThat(actualUser.getId()).isEqualTo(expectedUser.getId());
        assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(actualUser.getUsername()).isEqualTo(expectedUser.getUsername());
        verify(applicationEventPublisher).publishEvent(any());
    }

    @Test
    void givenExistingEmail_whenSignUp_shouldThrowException() {
        // Given
        String email = "sean@ktxdev.com";
        String password = "Password";
        String username = "sean";

        val userCreateDto = UserCreateDTO.builder()
                .email(email)
                .password(password)
                .username(username)
                .build();

        when(userDao.existsByEmail(email)).thenReturn(true);

        // When Then
        assertThatThrownBy(() -> underTest.signUp(userCreateDto))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("Email is already registered");

    }

    @Test
    void givenExistingUsername_whenSignUp_shouldThrowException() {
        // Given
        String email = "sean@ktxdev.com";
        String password = "Password";
        String username = "sean";

        val userCreateDto = UserCreateDTO.builder()
                .email(email)
                .password(password)
                .username(username)
                .build();

        when(userDao.existsByUsername(username)).thenReturn(true);

        // When Then
        assertThatThrownBy(() -> underTest.signUp(userCreateDto))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("Username already taken");
    }

    // TODO Test secure methods
}