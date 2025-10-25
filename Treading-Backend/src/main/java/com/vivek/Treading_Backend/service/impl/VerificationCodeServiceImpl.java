package com.vivek.Treading_Backend.service.impl;

import com.vivek.Treading_Backend.domain.VerificationType;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.model.VerificationCode;
import com.vivek.Treading_Backend.repository.VerificationCodeRepository;
import com.vivek.Treading_Backend.service.VerificationCodeService;
import com.vivek.Treading_Backend.utils.OtpUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {
    private final VerificationCodeRepository verificationCodeRepository;
    @Override
    public VerificationCode sendVerificationCode(User user, VerificationType verificationType) {
        VerificationCode verificationCode1 = new VerificationCode();
        verificationCode1.setOtp(OtpUtils.generateOtp());
        verificationCode1.setVerificationType(verificationType);
        verificationCode1.setUser(user);
        verificationCodeRepository.save(verificationCode1);
        return null;
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) throws Exception {
        Optional<VerificationCode> verificationCode = verificationCodeRepository.findById(id);
        if(verificationCode==null){
            throw new Exception("Verification code not found...");
        }

        return verificationCode.get();
    }

    @Override
    public VerificationCode getVerificationCodeByUserId(User user) throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findByUserId(user.getId());
        if(verificationCode==null){
            throw new Exception("Verification code not found...");
        }
        return verificationCode;
    }

    @Override
    public boolean verifyOtp(String otp,VerificationCode verificationCode){
        if(otp.equals(verificationCode.getOtp())){
            return true;
        }
        return false;
    }

    @Override
    public void deleteVerificationCode(VerificationCode verificationCode) {
        verificationCodeRepository.delete(verificationCode);
    }
}
