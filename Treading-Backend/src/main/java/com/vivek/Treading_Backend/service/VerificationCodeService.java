package com.vivek.Treading_Backend.service;

import com.vivek.Treading_Backend.domain.VerificationType;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.model.VerificationCode;

public interface VerificationCodeService {

    VerificationCode sendVerificationCode(User user, VerificationType verificationType);
    VerificationCode getVerificationCodeById(Long id) throws Exception;
    VerificationCode getVerificationCodeByUserId(User user) throws Exception;
    boolean verifyOtp(String otp,VerificationCode verificationCode);
    void deleteVerificationCode(VerificationCode verificationCode);

}
