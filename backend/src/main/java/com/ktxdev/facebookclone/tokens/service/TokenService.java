package com.ktxdev.facebookclone.tokens.service;

import com.ktxdev.facebookclone.tokens.model.Token;
import com.ktxdev.facebookclone.users.model.User;

public interface TokenService {
    Token generateToken(User user);

    Token findByToken(String token);

    boolean isValidToken(String tokenString);

    void useToken(Token token);
}
