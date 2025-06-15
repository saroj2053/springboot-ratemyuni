package com.sah.portfolio.project.ratemyuni.service;

import com.sah.portfolio.project.ratemyuni.model.University;
import com.sah.portfolio.project.ratemyuni.repository.UniversityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public University getUniversityDetails(String universityId) {
        Optional<University> university = universityRepository.findById(universityId);
        return university.get();
    }

    public University updateUniversity(String universityId, University university) {
        University existingUniversity = universityRepository.findById(universityId)
                .orElseThrow(() -> new RuntimeException("University not found"));

        existingUniversity.setName(university.getName());
        existingUniversity.setDescription(university.getDescription());
        existingUniversity.setLocation(university.getLocation());
        existingUniversity.setLogo(university.getLogo());
        existingUniversity.setEstablishedYear(university.getEstablishedYear());
        existingUniversity.setWebsiteUrl(university.getWebsiteUrl());
        existingUniversity.setType(university.getType());
        return universityRepository.save(existingUniversity);

    }

    public void deleteUniversity(String universityId) {
        universityRepository.deleteById(universityId);
    }
}
