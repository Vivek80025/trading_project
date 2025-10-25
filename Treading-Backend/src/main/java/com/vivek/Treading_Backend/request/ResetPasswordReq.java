package com.vivek.Treading_Backend.request;

import lombok.Data;

@Data
public class ResetPasswordReq {
    String otp;
    String password;
}
