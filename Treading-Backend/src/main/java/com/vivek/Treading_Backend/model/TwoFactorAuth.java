package com.vivek.Treading_Backend.model;

import com.vivek.Treading_Backend.domain.VerificationType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Data;

@Embeddable
@Data
public class TwoFactorAuth {
    private boolean isEnabled;
    private VerificationType sendTo;
}
