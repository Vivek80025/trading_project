package com.vivek.Treading_Backend.request;

import com.vivek.Treading_Backend.domain.VerificationType;
import com.vivek.Treading_Backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ForgotPasswordTokenReq {
    String sendTo;
    VerificationType verificationType;
}
