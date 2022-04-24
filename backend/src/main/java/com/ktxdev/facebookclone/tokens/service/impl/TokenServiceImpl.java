package com.ktxdev.facebookclone.tokens.service.impl;

import com.ktxdev.facebookclone.shared.exceptions.InvalidRequestException;
import com.ktxdev.facebookclone.shared.exceptions.RecordNotFoundException;
import com.ktxdev.facebookclone.tokens.dao.TokenDao;
import com.ktxdev.facebookclone.tokens.model.Token;
import com.ktxdev.facebookclone.tokens.service.TokenService;
import com.ktxdev.facebookclone.users.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenDao tokenDao;

    @Override
    public Token generateToken(User user) {
        String tokenString = generate();

        while (tokenDao.existsByToken(tokenString))
            tokenString = generate();

        val token = Token.builder()
                .token(tokenString)
                .owner(user)
                .build();

        return tokenDao.save(token);
    }

    @Override
    public Token findByToken(String token) {
        return tokenDao.findByToken(token)
                .orElseThrow(() -> new RecordNotFoundException("Token not found"));
    }

    @Override
    public boolean isValidToken(String tokenString) {
        val tokenOptional = tokenDao.findByToken(tokenString);
        return tokenOptional.isPresent() && !tokenOptional.get().isUsed();
    }

    @Override
    public void useToken(Token token) {
        token.setUsed(true);
        tokenDao.save(token);
    }

    private String generate() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 6);
    }
}
