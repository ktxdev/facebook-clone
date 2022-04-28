package com.ktxdev.facebookclone.tokens;

import com.ktxdev.facebookclone.shared.exceptions.RecordNotFoundException;
import com.ktxdev.facebookclone.tokens.dao.TokenDao;
import com.ktxdev.facebookclone.tokens.model.Token;
import com.ktxdev.facebookclone.tokens.service.TokenService;
import com.ktxdev.facebookclone.tokens.service.impl.TokenServiceImpl;
import com.ktxdev.facebookclone.users.model.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TokenServiceUnitTest {
    @Mock
    private TokenDao tokenDao;

    private TokenService underTest;

    @BeforeEach
    public void setup() {
        underTest = new TokenServiceImpl(tokenDao);
    }

    @Test
    public void givenUser_whenGenerateToken_shouldReturnToken() {
        // Given
        val user = User.builder()
                .email("sean@gmail.com")
                .password("password")
                .verified(true)
                .build();

        val token = Token.builder()
                .id(1L)
                .token("D31D43")
                .owner(user)
                .used(false)
                .build();

        when(tokenDao.save(any())).thenReturn(token);

        // When
        val actual = underTest.generateToken(user);

        // Then
        assertThat(actual.getToken()).isNotNull();
        assertThat(actual.getOwner().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void givenExistingToken_whenFindByToken_thenReturnToken() {
        // Given
        String tokenString = "D31D43";
        val token = Token.builder()
                .id(1L)
                .token(tokenString)
                .used(false)
                .build();

        when(tokenDao.findByToken(tokenString))
                .thenReturn(Optional.of(token));

        // When
        val actual = underTest.findByToken(tokenString);

        // Then
        assertThat(actual.getToken()).isEqualTo(tokenString);
    }

    @Test
    public void givenNonExistingToken_whenFindByToken_thenShouldThrow() {
        // Given
        String tokenString = "D31D43";
        when(tokenDao.findByToken(tokenString))
                .thenReturn(Optional.empty());
        // When Then
        assertThatThrownBy(() -> underTest.findByToken(tokenString))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessage("Token not found");
    }

    @Test
    public void givenValidToken_whenIsValidToken_thenReturnTrue() {
        // Given
        String tokenString = "D31D43";
        val token = Token.builder()
                .id(1L)
                .token(tokenString)
                .used(false)
                .build();

        when(tokenDao.findByToken(tokenString))
                .thenReturn(Optional.of(token));

        // When
        val actual = underTest.isValidToken(tokenString);

        // Then
        assertTrue(actual);
    }

    @Test
    public void givenUsedToken_whenIsValidToken_thenReturnFalse() {
        // Given
        String tokenString = "D31D43";
        val token = Token.builder()
                .id(1L)
                .token(tokenString)
                .used(true)
                .build();

        when(tokenDao.findByToken(tokenString))
                .thenReturn(Optional.of(token));

        // When
        val actual = underTest.isValidToken(tokenString);

        // Then
        assertFalse(actual);
    }
}