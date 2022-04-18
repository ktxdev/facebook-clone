package com.ktxdev.facebookclone.users.service.events;

import com.ktxdev.facebookclone.users.model.User;
import org.springframework.context.ApplicationEvent;

public class UserSignUpSuccessfulEvent extends ApplicationEvent {
    public UserSignUpSuccessfulEvent(User user) {
        super(user);
    }
}
