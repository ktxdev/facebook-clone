package com.ktxdev.facebookclone.users.service.events;

import com.ktxdev.facebookclone.users.model.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;

public class UserSignUpSuccessfulEvent extends ApplicationEvent {
    private final @Getter String verificationUrl;

    public UserSignUpSuccessfulEvent(User user, String verificationUrl) {
        super(user);
        this.verificationUrl = verificationUrl;
    }
}
