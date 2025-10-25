package com.vivek.Treading_Backend.service;

import com.vivek.Treading_Backend.domain.VerificationType;
import com.vivek.Treading_Backend.model.ForgetPasswordToken;
import com.vivek.Treading_Backend.model.User;

public interface ForgotPasswordService {
    ForgetPasswordToken createToken(User user, String id, String otp, VerificationType verificationType,String sendTo);
    ForgetPasswordToken findById(String id);
    ForgetPasswordToken findByUser(Long userId);
    void DeleteToken(ForgetPasswordToken forgetPasswordToken);
}
