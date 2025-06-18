package com.sah.portfolio.project.ratemyuni.service;

import com.sah.portfolio.project.ratemyuni.dto.ReviewRequest;
import com.sah.portfolio.project.ratemyuni.dto.ReviewUpdateRequest;
import com.sah.portfolio.project.ratemyuni.model.Review;
import com.sah.portfolio.project.ratemyuni.model.University;
import com.sah.portfolio.project.ratemyuni.repository.ReviewRepository;
import com.sah.portfolio.project.ratemyuni.repository.UniversityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UniversityRepository universityRepository;

    public Review addReview(ReviewRequest req) {
        // Create and save new review
        Review newReview = new Review();
        newReview.setUniversityId(req.getUniversityId());
        newReview.setUserId(req.getUserId());
        newReview.setRating(req.getRating());
        newReview.setTitle(req.getTitle());
        newReview.setComment(req.getComment());
        newReview.setProgram(req.getProgram());
        newReview.setGraduationYear(req.getGraduationYear());

        Review savedReview = reviewRepository.save(newReview);

        // adding review to university
        University university = universityRepository.findById(req.getUniversityId()).orElseThrow(() -> new RuntimeException("University not found"));

        if(university.getReviewIds() == null || university.getReviewIds().isEmpty()) {
            university.setReviewIds(new ArrayList<>());
        }

        university.getReviewIds().add(savedReview.getId());
        universityRepository.save(university);

        return savedReview;
    }

    public void deleteReview(String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Removing the review id from university's list
        String universityId = review.getUniversityId();
        if(universityId != null) {
            University university = universityRepository.findById(universityId)
                    .orElseThrow(() -> new RuntimeException("University not found"));
            if(university.getReviewIds() != null) {
                university.getReviewIds().removeIf( id -> id.equals(reviewId));
                universityRepository.save(university);
            }
        }

        // Deleting the review
        reviewRepository.deleteById(reviewId);
    }

    public Review updateReview(String reviewId, ReviewUpdateRequest request) {
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        existingReview.setRating(request.getRating());
        existingReview.setTitle(request.getTitle());
        existingReview.setComment(request.getComment());
        existingReview.setProgram(request.getProgram());
        existingReview.setGraduationYear(request.getGraduationYear());
        return reviewRepository.save(existingReview);
    }
}
