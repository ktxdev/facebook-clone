package com.ktxdev.facebookclone.users.service.events.listeners;

import com.ktxdev.facebookclone.notifications.email.model.EmailContent;
import com.ktxdev.facebookclone.notifications.email.service.EmailService;
import com.ktxdev.facebookclone.users.model.User;
import com.ktxdev.facebookclone.users.service.events.UserSignUpSuccessfulEvent;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSignUpSuccessfulEventListener implements ApplicationListener<UserSignUpSuccessfulEvent> {

    private final EmailService emailService;

    @Override
    public void onApplicationEvent(UserSignUpSuccessfulEvent event) {
        val user = (User) event.getSource();

        EmailContent emailContent = EmailContent.builder()
                .to(user.getEmail())
                .subject("Facebook clone account verification")
                .message("Please use this link to verify your email.")
                .build();

        emailService.sendEmail(emailContent);
    }
}
