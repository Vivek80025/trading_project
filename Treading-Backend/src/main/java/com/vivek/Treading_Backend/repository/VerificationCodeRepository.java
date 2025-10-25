package com.vivek.Treading_Backend.repository;


import com.vivek.Treading_Backend.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode,Long> {
    public VerificationCode findByUserId(Long id);
}
