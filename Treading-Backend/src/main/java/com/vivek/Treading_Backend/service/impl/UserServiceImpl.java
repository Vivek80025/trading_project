package com.vivek.Treading_Backend.service.impl;

import com.vivek.Treading_Backend.config.JwtProvider;
import com.vivek.Treading_Backend.domain.VerificationType;
import com.vivek.Treading_Backend.model.TwoFactorAuth;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.repository.UserRepository;
import com.vivek.Treading_Backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);
        if(user==null){
            throw new Exception("user not found...");
        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if(user==null){
            throw new Exception("user not found...");
        }
        return user;
    }

    @Override
    public User findUserById(Long id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new Exception("user not found...");
        }
        return user.get();
    }

    @Override
    public User enableTwoFactorAuthentication(VerificationType sendTo,User user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSendTo(sendTo);
        user.setTwoFactorAuth(twoFactorAuth);

        return userRepository.save(user);
    }

    @Override
    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
}
