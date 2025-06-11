package com.sah.portfolio.project.ratemyuni.service;

import com.sah.portfolio.project.ratemyuni.dto.UserDTO;
import com.sah.portfolio.project.ratemyuni.exception.UserAlreadyExistsException;
import com.sah.portfolio.project.ratemyuni.model.User;
import com.sah.portfolio.project.ratemyuni.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserDTO register(User user) throws UserAlreadyExistsException {
        User ifExistingUser = userRepository.findByEmail(user.getEmail());
        if (ifExistingUser != null) {
            throw new UserAlreadyExistsException("User already exists, please login...!");
        }

        /* hash passwords before saving*/
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        String queryParamsForAvatar = user.getFullName().replace(" ", "+");
        String profileAvatar = "https://avatar.iran.liara.run/username?username=" + queryParamsForAvatar;
        user.setProfileAvatar(profileAvatar);

        log.info("User object before saving: {}", user);
        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser.getEmail());

        return new UserDTO(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getProfileAvatar(),
                savedUser.getType(),
                savedUser.getRole(),
                token
        );
    }

    public UserDTO login(String email, String password) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            String token = null;
            if (authentication.isAuthenticated()) {
                token = jwtService.generateToken(email);
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User foundUser = userRepository.findByEmail(email);

            return new UserDTO(
                    foundUser.getId(),
                    foundUser.getFullName(),
                    foundUser.getEmail(),
                    foundUser.getProfileAvatar(),
                    foundUser.getType(),
                    foundUser.getRole(),
                    token
            );
        } catch(Exception e) {
            throw new Exception("Invalid email or password");
        }

    }
}
