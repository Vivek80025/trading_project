package com.vivek.Treading_Backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JwtProvider {
    private static SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRETE_KEY.getBytes());
    public static String generateToken(Authentication auth){
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles = populateAuthorities(authorities);

        String jwt = Jwts.builder()
                .claim("authorities",roles)
                .claim("email",auth.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+86400000))
                .signWith(key)
                .compact();

        return jwt;
    }

    public static String getEmailFromToken(String token){
        String jwt  = token.substring(7);
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        String email = String.valueOf(claims.get("email"));
        return email;
    }


    public static String populateAuthorities(Collection<? extends GrantedAuthority> authorities){
        Set<String> auth = new HashSet<>();
        for(GrantedAuthority ga : authorities){
            auth.add(ga.getAuthority());
        }

        return String.join(",",auth);
    }
}
