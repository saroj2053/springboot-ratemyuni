package com.sah.portfolio.project.ratemyuni.service;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) throws IOException {
        Map<String, Object> options = Map.of("folder", "UniversityLogos");
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options, null);
        return uploadResult.get("url").toString();
    }
}
