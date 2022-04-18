package com.ktxdev.facebookclone.users.dto;

import com.ktxdev.facebookclone.auth.model.TwoFactorAuthType;
import lombok.Data;

@Data
public class UserUpdateDTO {
    private String displayName;

    private boolean twoFactorEnabled;

    private TwoFactorAuthType twoFactorAuthType;
}
