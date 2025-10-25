package com.vivek.Treading_Backend.service;

import com.vivek.Treading_Backend.model.TwoFactorOtp;
import com.vivek.Treading_Backend.model.User;

public interface TwoFactorOtpService {
    TwoFactorOtp createTwoFactorOtp(User user,String otp,String jwt);

    TwoFactorOtp findByUser(Long userId);

    TwoFactorOtp findById(String id);

    boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp,String otp);

    void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp);
}
