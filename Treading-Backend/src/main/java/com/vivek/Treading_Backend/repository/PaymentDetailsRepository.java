package com.vivek.Treading_Backend.repository;

import com.vivek.Treading_Backend.model.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails,Long> {
    PaymentDetails findByUserId(Long id);
}
