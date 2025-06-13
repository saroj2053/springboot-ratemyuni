package com.sah.portfolio.project.ratemyuni.repository;

import com.sah.portfolio.project.ratemyuni.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findByUserId(String userId);

    List<Review> findByUniversityId(String universityId);
}
