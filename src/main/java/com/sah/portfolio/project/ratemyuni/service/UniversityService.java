package com.sah.portfolio.project.ratemyuni.service;

import com.sah.portfolio.project.ratemyuni.model.University;
import com.sah.portfolio.project.ratemyuni.repository.UniversityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UniversityService {

    @Autowired
    UniversityRepository universityRepository;

    public List<University> getUniversities() {
        return universityRepository.findAll();
    }

    public University addUniversity(University university) {
        return universityRepository.save(university);
    }
}
