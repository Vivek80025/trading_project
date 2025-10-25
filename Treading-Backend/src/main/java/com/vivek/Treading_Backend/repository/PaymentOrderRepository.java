package com.vivek.Treading_Backend.repository;

import com.vivek.Treading_Backend.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder,Long> {

}
