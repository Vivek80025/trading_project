package com.vivek.Treading_Backend.service.impl;

import com.vivek.Treading_Backend.model.TwoFactorOtp;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.repository.TwoFactorOtpRepository;
import com.vivek.Treading_Backend.service.TwoFactorOtpService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TwoFactorOtpServiceImpl implements TwoFactorOtpService {
    private final TwoFactorOtpRepository twoFactorOtpRepository;
    @Override
    public TwoFactorOtp createTwoFactorOtp(User user, String otp, String jwt) {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        TwoFactorOtp twoFactorOtp = new TwoFactorOtp();
        twoFactorOtp.setId(id);
        twoFactorOtp.setOtp(otp);
        twoFactorOtp.setUser(user);
        twoFactorOtp.setJwt(jwt);
        return twoFactorOtpRepository.save(twoFactorOtp);
    }

    @Override
    public TwoFactorOtp findByUser(Long userId) {
        return twoFactorOtpRepository.findByUserId(userId);
    }

    @Override
    public TwoFactorOtp findById(String id) {
        Optional<TwoFactorOtp> byId = twoFactorOtpRepository.findById(id);
        return byId.orElse(null);
    }

    @Override
    public boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp, String otp) {
        return twoFactorOtp.getOtp().equals(otp);
    }

    @Override
    public void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp) {
        twoFactorOtpRepository.delete(twoFactorOtp);
    }
}
