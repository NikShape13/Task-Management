package com.example.demo.models;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class PasswordEncoder extends BCryptPasswordEncoder{
}
