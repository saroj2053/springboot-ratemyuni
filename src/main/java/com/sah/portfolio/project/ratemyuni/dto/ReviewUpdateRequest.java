package com.sah.portfolio.project.ratemyuni.dto;

import lombok.Data;

@Data
public class ReviewUpdateRequest {
    private Double rating;
    private String reviewText;
}
