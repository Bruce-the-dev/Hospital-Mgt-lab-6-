package com.hospital.config;

import com.hospital.model.Role;
import com.hospital.model.User;
import com.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // Did you know? This delegates to the default Spring implementation to get the
        // user raw info from Google!
        OAuth2User oauthUser = new DefaultOAuth2UserService().loadUser(userRequest);

        // Extract key details
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        // EDUCATIONAL: Here we persist the user.
        // If they don't exist, we create them. If they do, we update them.
        User user = userRepository.findByEmail(email)
                .map(existingUser -> {
                    // Update existing user details if needed
                    existingUser.setFullName(name);
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    // Create new user
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setUsername(email); // Use email as username for OAuth users
                    newUser.setFullName(name);
                    newUser.setRole(Role.RECEPTIONIST); // Default role as requested
                    newUser.setPassword(""); // No password for OAuth users
                    newUser.setStatus(true);
                    return userRepository.save(newUser);
                });

        // Copy existing authorities (scopes + OAUTH2_USER)
        Set<GrantedAuthority> mappedAuthorities = new HashSet<>(oauthUser.getAuthorities());

        // Add our app's role authority so Spring Security knows who they are
        mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        // Return a new user with your custom authorities
        return new DefaultOAuth2User(
                mappedAuthorities,
                oauthUser.getAttributes(),
                "sub" // Google's user id key is usually 'sub' (Subject)
        );
    }
}
