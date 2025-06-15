package com.sah.portfolio.project.ratemyuni.controller;


import com.sah.portfolio.project.ratemyuni.dto.UserDTO;
import com.sah.portfolio.project.ratemyuni.model.User;
import com.sah.portfolio.project.ratemyuni.service.AuthService;
import com.sah.portfolio.project.ratemyuni.utils.ResponseGenerator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.naming.java.javaURLContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin()
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
                return ResponseGenerator.response(false, "Email is required", 400);
            }

            if(password == null) {
                return ResponseGenerator.response(false, "Password is required", 400);
            }

            if(!email.contains("@")) {
                return ResponseGenerator.response(false, "Please enter a valid email address", 400);
            }

            if(password.length() < 6) {
                return ResponseGenerator.response(false, "Password must be at least 6 characters", 400);
            }

            UserDTO userDTO = authService.login(email, password);

            Cookie cookie = new Cookie("token", userDTO.getToken());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60); // for 7 days

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User logged in successfully");
            response.put("user", userDTO);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseGenerator.response(false, e.getMessage(), 400);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<?> logoutUser() {
        try {
            // Clearing the security context
            SecurityContextHolder.clearContext();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Logged out successfully");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            Map<String, Object> errResponse = new HashMap<>();
            errResponse.put("success", false);
            errResponse.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(errResponse);
        }
    }

}
