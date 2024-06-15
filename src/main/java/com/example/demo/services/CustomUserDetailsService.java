package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.utils.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final String USER_EXISTS_MESSAGE = "Username '%s' already exists";
    private static final String EMAIL_IN_USE_MESSAGE = "Email '%s' is already in use";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public void registerUser(User user) {
        String username = user.getUsername();
        if (userRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException(String.format(USER_EXISTS_MESSAGE, username));
        }

        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException(String.format(EMAIL_IN_USE_MESSAGE, email));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole(Roles.ROLE_USER);
        userRepository.save(user);
    }
}