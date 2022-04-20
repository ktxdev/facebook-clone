package com.ktxdev.facebookclone.users.dao;

import com.ktxdev.facebookclone.shared.jpa.BaseDao;
import com.ktxdev.facebookclone.users.model.User;

import java.util.Optional;

public interface UserDao extends BaseDao<User> {
    Optional<User> findByUsernameOrEmail(String username, String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
