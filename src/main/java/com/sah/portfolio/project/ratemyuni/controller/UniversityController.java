package com.sah.portfolio.project.ratemyuni.controller;

import com.sah.portfolio.project.ratemyuni.model.University;
import com.sah.portfolio.project.ratemyuni.service.CloudinaryService;
import com.sah.portfolio.project.ratemyuni.service.UniversityService;
import com.sah.portfolio.project.ratemyuni.utils.ResponseGenerator;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/university")
public class UniversityController {

    @Autowired
    private UniversityService universityService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllUniversities() {
        try {
            List<University> universities = universityService.getUniversities();
            if(universities.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(universities);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @PostMapping("/addUniversity")
    public ResponseEntity<?> addUniversity(@RequestPart("university") University university, @RequestPart(value="file", required=false) MultipartFile file ) {
        try {

            if (university.getName().isEmpty() || university.getDescription().isEmpty() || university.getLocation().isEmpty() || university.getEstablishedYear().isEmpty() || university.getType().describeConstable().isEmpty() || university.getWebsiteUrl().isEmpty()) {
                return ResponseGenerator.response(false, "Please enter values in all fields", 400);
            }

            if(file != null && !file.isEmpty()) {
                String logoUrl = cloudinaryService.uploadFile(file);
                university.setLogo(logoUrl);
            } else {
                return ResponseGenerator.response(false, "Error uploading file", 400);
            }

            University savedUniversity = universityService.addUniversity(university);
            Map<String, Object> res = new HashMap<>();
            res.put("university", savedUniversity);
            res.put("message", "University added successfully");
            return ResponseEntity.status(201).body(res);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @GetMapping("/{universityId}")
    public ResponseEntity<?> getUniversityById(@PathVariable String universityId) {
        try {
            University universityResponse = universityService.getUniversityDetails(universityId);

            if (universityResponse == null) {
                return ResponseEntity.noContent().build();
            }

            Map<String, Object> res = new HashMap<>();
            res.put("university", universityResponse);
            return ResponseEntity.status(200).body(res);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/update/{universityId}")
    public ResponseEntity<?> updateUniversity(@PathVariable String universityId,
                                              @RequestPart University university,
                                              @RequestPart(value = "file", required = false) MultipartFile file) {

        try{

            if(!ObjectId.isValid(universityId)) {
                return ResponseGenerator.response(false, "Invalid University Id", 400);
            }

            if (university.getName().isEmpty() || university.getDescription().isEmpty() || university.getLocation().isEmpty() || university.getEstablishedYear().isEmpty() || university.getType().describeConstable().isEmpty() || university.getWebsiteUrl().isEmpty()) {
                return ResponseGenerator.response(false, "Please enter values in all fields", 400);
            }

            if(file != null && !file.isEmpty()) {
                String logoUrl = cloudinaryService.uploadFile(file);
                university.setLogo(logoUrl);
            } else {
                return ResponseGenerator.response(false, "Error uploading file", 400);
            }

            University updatedUniversity = universityService.updateUniversity(universityId, university);

            if(updatedUniversity == null) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "university", updatedUniversity,
                    "message", "University updated successfully"
            ));

        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{universityId}")
    public ResponseEntity<?> deleteUniversity(@PathVariable String universityId) {
        try {
            universityService.deleteUniversity(universityId);
            Map<String, Object> res = new HashMap<>();
            res.put("success", true);
            res.put("message", "University deleted successfully");
            return ResponseEntity.status(200).body(res);
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
}
