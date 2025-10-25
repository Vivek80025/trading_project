package com.vivek.Treading_Backend.model;

import com.vivek.Treading_Backend.domain.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ForgetPasswordToken {
    @Id
    private String id;

    @OneToOne
    private User user;

    private String otp;

    private VerificationType verificationType;

    private String sendTo;

}
