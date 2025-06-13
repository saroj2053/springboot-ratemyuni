package com.sah.portfolio.project.ratemyuni.repository;

import com.sah.portfolio.project.ratemyuni.model.University;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniversityRepository extends MongoRepository<University, String> {

}
