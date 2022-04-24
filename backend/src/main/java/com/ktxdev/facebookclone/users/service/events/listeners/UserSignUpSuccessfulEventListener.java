package com.ktxdev.facebookclone.users.service.events.listeners;

import com.ktxdev.facebookclone.notifications.email.model.EmailContent;
import com.ktxdev.facebookclone.notifications.email.service.EmailService;
import com.ktxdev.facebookclone.users.api.UserRestController;
import com.ktxdev.facebookclone.users.model.User;
import com.ktxdev.facebookclone.users.service.events.UserSignUpSuccessfulEvent;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.ApplicationListener;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class UserSignUpSuccessfulEventListener implements ApplicationListener<UserSignUpSuccessfulEvent> {

    private final EmailService emailService;

    @Override
    public void onApplicationEvent(UserSignUpSuccessfulEvent event) {
        val user = (User) event.getSource();

        val verificationUrl = event.getVerificationUrl();

        EmailContent emailContent = EmailContent.builder()
                .to(user.getEmail())
                .subject("Facebook clone account verification")
                .message(String.format("Please use this link to verify your email.\n%s", verificationUrl))
                .build();

        emailService.sendEmail(emailContent);
    }
}
