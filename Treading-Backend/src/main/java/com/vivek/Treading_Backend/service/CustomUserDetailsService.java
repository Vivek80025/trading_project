package com.vivek.Treading_Backend.service;

import com.vivek.Treading_Backend.domain.USER_ROLE;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("User not found..."+username);
        }
        String authority = String.valueOf(user.getRole());
        List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(authority);

        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorityList);
    }
}
