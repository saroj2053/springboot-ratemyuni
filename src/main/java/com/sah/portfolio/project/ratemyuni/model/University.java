package com.sah.portfolio.project.ratemyuni.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "universities")
public class University {
    @Id
    private String id;

    private String name;

    @Field(name = "logo")
    private String logo = "";

    private String location;

    private String establishedYear;

    private Category type;

    public enum Category {
        national, provincial, autonomous
    }

    private String description;

    private String websiteUrl;

    private List<String> reviewIds;
}
