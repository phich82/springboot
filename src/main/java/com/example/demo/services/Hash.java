package com.example.demo.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Hash {

    private static String encode(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

}
