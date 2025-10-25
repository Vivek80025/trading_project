package com.vivek.Treading_Backend.controller;

import com.vivek.Treading_Backend.domain.VerificationType;
import com.vivek.Treading_Backend.model.ForgetPasswordToken;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.model.VerificationCode;
import com.vivek.Treading_Backend.request.ForgotPasswordTokenReq;
import com.vivek.Treading_Backend.request.ResetPasswordReq;
import com.vivek.Treading_Backend.response.ApiResponse;
import com.vivek.Treading_Backend.response.AuthResponse;
import com.vivek.Treading_Backend.service.EmailService;
import com.vivek.Treading_Backend.service.ForgotPasswordService;
import com.vivek.Treading_Backend.service.UserService;
import com.vivek.Treading_Backend.service.VerificationCodeService;
import com.vivek.Treading_Backend.utils.OtpUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final VerificationCodeService verificationCodeService;
    private final EmailService emailService;
    private final ForgotPasswordService forgotPasswordService;

    @GetMapping("api/users/profile")
    public ResponseEntity<User> getUserProfileByJwt(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<String> sendVerificationCodeOtp(@RequestHeader("Authorization") String jwt,
                                                                    @RequestParam VerificationType verificationType) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUserId(user);
        if(verificationCode==null){
            verificationCode = verificationCodeService.sendVerificationCode(user,verificationType);
        }
        if(verificationType.equals(VerificationType.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(),verificationCode.getOtp());
        }

        return ResponseEntity.ok("send verification otp successfully...");
    }

    @PatchMapping("api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(@RequestHeader("Authorization") String jwt,
                                                              @PathVariable String otp) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUserId(user);
        if(verificationCodeService.verifyOtp(otp,verificationCode)){
            User updatedUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(),user);
            verificationCodeService.deleteVerificationCode(verificationCode);
            return ResponseEntity.ok(updatedUser);
        }

        throw new BadCredentialsException("invalid otp...");
    }

    @PostMapping("auth/users/reset-password/otp-sent")
    public ResponseEntity<AuthResponse> sendForgetPasswordOtp(
            @RequestBody ForgotPasswordTokenReq req
            ) throws Exception {
        User user = userService.findUserByEmail(req.getSendTo());
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        String otp = OtpUtils.generateOtp();
        ForgetPasswordToken token = forgotPasswordService.findByUser(user.getId());
        if(token==null){
            token = forgotPasswordService.createToken(user,id,otp,req.getVerificationType(),req.getSendTo());
        }
        if(req.getVerificationType().equals(VerificationType.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(),token.getOtp());
        }
        AuthResponse response = new AuthResponse();
        response.setSession(token.getId());
        response.setMsg("forgot password otp sent successfully...");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordReq req,
                                                     @RequestParam String id) throws Exception {
        ForgetPasswordToken token = forgotPasswordService.findById(id);
        boolean isVerified = token.getOtp().equals(req.getOtp());
        if(isVerified){
            userService.updatePassword(token.getUser(),req.getPassword());
            ApiResponse response = new ApiResponse();
            response.setMsg("password reset successfully...");
            return ResponseEntity.ok(response);
        }
        throw new Exception("wrong otp...");
    }
}
