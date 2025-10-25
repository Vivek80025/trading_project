package com.vivek.Treading_Backend.repository;

import com.vivek.Treading_Backend.model.TwoFactorOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOtp,String> {
    TwoFactorOtp findByUserId(Long userId);
}
