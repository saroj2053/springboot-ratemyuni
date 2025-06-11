package com.sah.portfolio.project.ratemyuni.dto;

import com.sah.portfolio.project.ratemyuni.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String fullName;
    private String email;
    private String profileAvatar;
    private User.UserType userType;
    private User.Role role;
    private String token;
}
