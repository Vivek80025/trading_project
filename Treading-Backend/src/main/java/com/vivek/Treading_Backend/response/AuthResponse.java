package com.vivek.Treading_Backend.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private String msg;
    private boolean status;
    private boolean isTwoFactorAuthEnable;
    private String session;
}
