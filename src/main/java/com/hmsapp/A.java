package com.hmsapp;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class A {
    public static void main(String[] args) {
        String encodedPassword = BCrypt.hashpw("testing", BCrypt.gensalt(10));
        System.out.println(encodedPassword);

    }
}
