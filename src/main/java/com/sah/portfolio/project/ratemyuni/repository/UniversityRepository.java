package com.sah.portfolio.project.ratemyuni.repository;

import com.sah.portfolio.project.ratemyuni.model.University;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends MongoRepository<University, String> {
    public University findById(int id);
}
