package com.vivek.Treading_Backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class Greet {
    @GetMapping("/greet")
    public String greet(){
        return "Welcome to treading platform!";
    }
    @GetMapping("/api")
    public String secure(){
        return "this is the secure api!";
    }
}
