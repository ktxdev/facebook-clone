package com.ktxdev.facebookclone.notifications.email.service;

import com.ktxdev.facebookclone.notifications.email.model.EmailContent;

public interface EmailService {
    void sendEmail(EmailContent emailContent);
}
