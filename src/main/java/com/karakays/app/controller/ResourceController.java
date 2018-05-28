package com.karakays.app.controller;

import java.util.Date;
import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class ResourceController {
    
    @GetMapping("/random")
    public Integer randomNumber() {
        return new Random().nextInt();
    }
    
    @GetMapping("/time")
    public String current() {
        return new Date().toString();
    }
}
