package com.sah.portfolio.project.ratemyuni.security;

import com.sah.portfolio.project.ratemyuni.model.User;
import com.sah.portfolio.project.ratemyuni.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class RateMyUniUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        User rateMyUniUser = userRepository.findByEmail(email);

        if(rateMyUniUser != null && passwordEncoder.matches(password, rateMyUniUser.getPassword())) {
            return new UsernamePasswordAuthenticationToken(email, null, getGrantedAuthorities(rateMyUniUser.getRole()));
        } else {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(User.Role role) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
