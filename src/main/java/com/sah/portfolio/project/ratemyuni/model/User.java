package com.sah.portfolio.project.ratemyuni.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;


@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id;

    private String fullName;

    private String email;

    private String password;

    @Transient
    private String confirmPassword;

    @Field(name = "profileAvatar")
    private String profileAvatar = ""; // default value

    private UserType type;

    public enum UserType {
        Student, Teacher, Administrator
    }

    private Role role = Role.user;

    public enum Role {
        user, admin
    }

    private List<Review> reviews;

}
