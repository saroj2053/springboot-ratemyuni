package com.sah.portfolio.project.ratemyuni.controller;


import com.sah.portfolio.project.ratemyuni.dto.UserDTO;
import com.sah.portfolio.project.ratemyuni.model.User;
import com.sah.portfolio.project.ratemyuni.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.naming.java.javaURLContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        log.info("User object for registration: {}", user);

        try {
            if (user.getFullName() == null) {
                return ResponseEntity.badRequest().body("Full Name is required");
            }
            if (user.getEmail() == null) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (user.getPassword() == null) {
                return ResponseEntity.badRequest().body("Password is required");
            }

            if (user.getFullName().length() < 6) {
                return ResponseEntity.badRequest().body("Full Name must be at least 6 characters");
            }

            if (!user.getEmail().contains("@")) {
                return ResponseEntity.badRequest().body("Please enter a valid email address");
            }

            if (user.getPassword().length() < 6) {
                return ResponseEntity.badRequest().body("Password must be at least 6 characters");
            }

            if (!user.getPassword().equals(user.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("Passwords do not match");
            }

            UserDTO userDTO = authService.register(user);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User registration successful!!");
            response.put("user", userDTO);
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> requestBody) {

        String email = requestBody.get("email");
        String password = requestBody.get("password");

        log.info("User object for login: {}", email + ", " + password);
        try {
            if(email == null) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if(password == null) {
                return ResponseEntity.badRequest().body("Password is required");
            }

            if(!email.contains("@")) {
                return ResponseEntity.badRequest().body("Please enter a valid email address");
            }

            if(password.length() < 6) {
                return ResponseEntity.badRequest().body("Password must be at least 6 characters");
            }

            UserDTO userDTO = authService.login(email, password);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User logged in successfully");
            response.put("user", userDTO);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Authentication failed: " + e.getMessage());
        }
    }
}
