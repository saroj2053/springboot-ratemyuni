package com.sah.portfolio.project.ratemyuni.utils;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseGenerator {
    public static ResponseEntity<?> response(boolean success, String message, int statusCode) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", success);
        responseBody.put("message", message);
        return ResponseEntity.status(statusCode).body(responseBody);
    }
}
