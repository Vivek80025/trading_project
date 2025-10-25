package com.vivek.Treading_Backend.controller;

import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.response.AuthResponse;
import com.vivek.Treading_Backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    //constructor injection
    private final AuthService authService;

    /*
    not write this for all field use lombok,
    constructor injection<--recommended,(allows easier unit testing, and avoids hidden null issues.)
    @Autowired<--field injection(works, but less preferred)
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
     */

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {

//        return new ResponseEntity<>(newUser, HttpStatus.OK);
        return authService.register(user);
    }

    @GetMapping("/signing")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {
        return authService.login(user);
    }

    @PostMapping("two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySigningOtp(@PathVariable String otp, @RequestParam String id){
        return authService.verifySigningOtp(otp,id);
    }

}
