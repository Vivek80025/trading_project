package com.vivek.Treading_Backend.service;

import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.response.AuthResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    public ResponseEntity<AuthResponse> register(User user) throws Exception;
    public ResponseEntity<AuthResponse> login(User user) throws MessagingException;
    public ResponseEntity<AuthResponse> verifySigningOtp(String otp,String id);
}
