package com.vivek.Treading_Backend.service.impl;

import com.vivek.Treading_Backend.domain.VerificationType;
import com.vivek.Treading_Backend.model.ForgetPasswordToken;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.repository.ForgotPasswordRepository;
import com.vivek.Treading_Backend.service.ForgotPasswordService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    private final ForgotPasswordRepository forgotPasswordRepository;
    @Override
    public ForgetPasswordToken createToken(User user, String id, String otp, VerificationType verificationType, String sendTo) {
        ForgetPasswordToken forgetPasswordToken = new ForgetPasswordToken();
        forgetPasswordToken.setId(id);
        forgetPasswordToken.setUser(user);
        forgetPasswordToken.setOtp(otp);
        forgetPasswordToken.setVerificationType(verificationType);
        forgetPasswordToken.setSendTo(sendTo);
        return forgotPasswordRepository.save(forgetPasswordToken);
    }

    @Override
    public ForgetPasswordToken findById(String id) {
        Optional<ForgetPasswordToken> token = forgotPasswordRepository.findById(id);

        return token.orElse(null);
    }

    @Override
    public ForgetPasswordToken findByUser(Long userId) {
        return forgotPasswordRepository.findByUserId(userId);
    }

    @Override
    public void DeleteToken(ForgetPasswordToken forgetPasswordToken) {
        forgotPasswordRepository.delete(forgetPasswordToken);
    }
}
