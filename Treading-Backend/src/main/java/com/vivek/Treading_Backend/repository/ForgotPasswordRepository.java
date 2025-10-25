package com.vivek.Treading_Backend.repository;

import com.vivek.Treading_Backend.model.ForgetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepository extends JpaRepository<ForgetPasswordToken,String> {
   ForgetPasswordToken findByUserId(Long userId);
}
