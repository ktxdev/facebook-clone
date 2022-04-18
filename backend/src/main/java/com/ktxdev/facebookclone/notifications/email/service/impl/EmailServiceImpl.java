package com.ktxdev.facebookclone.notifications.email.service.impl;

import com.ktxdev.facebookclone.notifications.email.model.EmailContent;
import com.ktxdev.facebookclone.notifications.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${fb-clone.email-sender}")
    private String emailSender;

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(EmailContent emailContent) {
        log.debug("### Sending email...");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailSender);
        message.setTo(emailContent.getTo());
        message.setSubject(emailContent.getSubject());
        message.setText(emailContent.getMessage());

        mailSender.send(message);
    }
}
