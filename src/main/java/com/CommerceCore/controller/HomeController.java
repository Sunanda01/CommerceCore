package com.CommerceCore.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping
    public String home(){
        return "Welcome To CommerceCore\n"+"Go to http://localhost:8080/oauth2/authorization/google";
    }
}
