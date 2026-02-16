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

        OAuth2User oauthUser = new DefaultOAuth2UserService().loadUser(userRequest);

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        User user = userRepository.findByEmail(email)
                .map(existingUser -> {

                    existingUser.setFullName(name);
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setFullName(name);
                    if (name != null) {
                        String formattedUsername = name.trim().toLowerCase().replace(" ", ".");
                        newUser.setUsername(formattedUsername);
                    } else {
                        newUser.setUsername(email); // Fallback just in case Google name is null
                    }
                    newUser.setRole(Role.RECEPTIONIST);
                    newUser.setPassword("");
                    newUser.setStatus(true);
                    return userRepository.save(newUser);
                });

        Set<GrantedAuthority> mappedAuthorities = new HashSet<>(oauthUser.getAuthorities());

        mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        return new DefaultOAuth2User(
                mappedAuthorities,
                oauthUser.getAttributes(),
                "sub"
        );
    }
}
