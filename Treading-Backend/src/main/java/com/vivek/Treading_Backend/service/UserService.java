package com.vivek.Treading_Backend.service;

import com.vivek.Treading_Backend.domain.VerificationType;
import com.vivek.Treading_Backend.model.User;

public interface UserService {
    User findUserProfileByJwt(String jwt) throws Exception;
    User findUserByEmail(String email) throws Exception;
    User findUserById(Long id) throws Exception;
    User enableTwoFactorAuthentication(VerificationType sendTo,User user);
    User updatePassword(User user,String newPassword);
}
