package com.example.classroom_db.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String ADMIN_PASSWORD = "1111";

    @PostMapping("/login")
    public boolean login(@RequestBody String password) {
        return ADMIN_PASSWORD.equals(password);
    }

    @PostMapping("/logout")
    public void logout() {
        // Логика выхода (если нужна)
    }
}