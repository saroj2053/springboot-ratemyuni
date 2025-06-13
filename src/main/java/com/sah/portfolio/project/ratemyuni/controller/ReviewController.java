package com.sah.portfolio.project.ratemyuni.controller;

import com.sah.portfolio.project.ratemyuni.dto.ReviewRequest;
import com.sah.portfolio.project.ratemyuni.dto.ReviewUpdateRequest;
import com.sah.portfolio.project.ratemyuni.model.Review;
import com.sah.portfolio.project.ratemyuni.service.ReviewService;
import com.sah.portfolio.project.ratemyuni.utils.ResponseGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/")
    public ResponseEntity<?> addReview(@RequestBody ReviewRequest reviewRequest) {
        try {
            if (reviewRequest.getUniversityId() == null || reviewRequest.getUniversityId().isEmpty()) {
                return ResponseGenerator.response(false, "University ID is required", 400);
            }

            if (reviewRequest.getUserId() == null || reviewRequest.getUserId().isEmpty()) {
                return ResponseGenerator.response(false, "User must be logged in to add review", 400);
            }

            if (reviewRequest.getRating() == 0) {
                return ResponseGenerator.response(false, "Rating is required", 400);
            }

            if (reviewRequest.getRating() < 1 || reviewRequest.getRating() > 5) {
                return ResponseGenerator.response(false, "Rating must be between 1 and 5", 400);
            }

            if (reviewRequest.getReviewText() == null || reviewRequest.getReviewText().isEmpty()) {
                return ResponseGenerator.response(false, "Review text must be at least 50 characters", 400);
            }

            Review review = reviewService.addReview(reviewRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(review);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseGenerator.response(false, "Error creating review: " + e.getMessage(), 500));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable("id") String reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity
                .ok()
                .body(ResponseGenerator.response(true, "Review deleted successfully", 200));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateReview(@PathVariable("id") String reviewId,
                                          @RequestBody ReviewUpdateRequest request) {
        log.info("control here...");
        try {
            Review updatedReview = reviewService.updateReview(reviewId, request);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("updatedReview", updatedReview);
            responseMap.put("message", "Review updated successfully");
            responseMap.put("success", true);

            return ResponseEntity.status(200).body(responseMap);

        } catch(Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(ResponseGenerator.response(false, "Error updating review: " + e.getMessage(), 500));
        }

    }
}
