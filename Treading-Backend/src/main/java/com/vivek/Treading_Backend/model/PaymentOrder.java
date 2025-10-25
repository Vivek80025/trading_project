package com.vivek.Treading_Backend.model;

import com.vivek.Treading_Backend.domain.PaymentMethod;
import com.vivek.Treading_Backend.domain.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PaymentOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long amount;

    private PaymentOrderStatus status = PaymentOrderStatus.PENDING;

    private PaymentMethod paymentMethod;

    @ManyToOne
    private User user;
}
