package com.vivek.Treading_Backend.service.impl;

import com.vivek.Treading_Backend.config.JwtProvider;
import com.vivek.Treading_Backend.model.TwoFactorOtp;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.repository.TwoFactorOtpRepository;
import com.vivek.Treading_Backend.repository.UserRepository;
import com.vivek.Treading_Backend.response.AuthResponse;
import com.vivek.Treading_Backend.service.AuthService;
import com.vivek.Treading_Backend.service.CustomUserDetailsService;
import com.vivek.Treading_Backend.service.EmailService;
import com.vivek.Treading_Backend.service.TwoFactorOtpService;
import com.vivek.Treading_Backend.utils.OtpUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private final TwoFactorOtpService twoFactorOtpService;
    private final TwoFactorOtpRepository twoFactorOtpRepository;
    private final EmailService emailService;

    @Override
    public ResponseEntity<AuthResponse> register(User user) throws Exception {
        String email = user.getEmail();
        User isEmailExist = userRepository.findByEmail(email);
        if(isEmailExist!=null){
            throw new Exception("Email is already exist...");
        }
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setFullName(user.getFullName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(newUser);

        String role = String.valueOf(savedUser.getRole());
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(role);
        Authentication auth = new UsernamePasswordAuthenticationToken(savedUser.getEmail(),null,authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);
        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setMsg("register successfully...");
        res.setStatus(true);
        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<AuthResponse> login(User user) throws MessagingException {
        String username = user.getEmail();
        String password = user.getPassword();
        Authentication auth = authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = JwtProvider.generateToken(auth);

        User authUser = userRepository.findByEmail(username);
        if(authUser.getTwoFactorAuth().isEnabled()){
            AuthResponse res = new AuthResponse();
            res.setMsg("Two factor auth is enabled...");
            res.setTwoFactorAuthEnable(true);

            String otp = OtpUtils.generateOtp();
            TwoFactorOtp oldTwoFactorOtp = twoFactorOtpService.findByUser(authUser.getId());
            if(oldTwoFactorOtp!=null){
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOtp);
            }
            TwoFactorOtp newTwoFactorOtp = twoFactorOtpService.createTwoFactorOtp(authUser,otp,jwt);
            TwoFactorOtp newTwoFactorOtp2 = twoFactorOtpRepository.save(newTwoFactorOtp);
            res.setSession(newTwoFactorOtp2.getId());

            //send email to user email
            emailService.sendVerificationOtpEmail(username,otp);

            return ResponseEntity.ok(res);
        }
        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setMsg("login successfully...");
        res.setStatus(true);
        return ResponseEntity.ok(res);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(userDetails==null){
            throw new BadCredentialsException("Invalid username...");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Wrong password...");
        }
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(),null,userDetails.getAuthorities());

    }
    public ResponseEntity<AuthResponse> verifySigningOtp(String otp, String id){
        TwoFactorOtp twoFactorOtp = twoFactorOtpService.findById(id);

        if(twoFactorOtpService.verifyTwoFactorOtp(twoFactorOtp,otp)){
            AuthResponse response = new AuthResponse();
            response.setMsg("Two factor authentication verified...");
            response.setJwt(twoFactorOtp.getJwt());
            response.setTwoFactorAuthEnable(true);
            response.setStatus(true);
            return ResponseEntity.ok(response);
        }
        throw new BadCredentialsException("invalid otp...");
    }
}
