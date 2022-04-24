package com.ktxdev.facebookclone.tokens.dao;

import com.ktxdev.facebookclone.shared.jpa.BaseDao;
import com.ktxdev.facebookclone.tokens.model.Token;

import java.util.Optional;

public interface TokenDao extends BaseDao<Token> {
    Optional<Token> findByToken(String token);

    boolean existsByToken(String token);
}
