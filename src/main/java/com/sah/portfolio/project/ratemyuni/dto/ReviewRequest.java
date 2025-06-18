package com.sah.portfolio.project.ratemyuni.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private String universityId;
    private String userId;
    private double rating;
    private String title;
    private String comment;
    private String program;
    private String graduationYear;

}
