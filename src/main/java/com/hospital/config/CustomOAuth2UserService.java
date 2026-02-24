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
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
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

        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

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
                    newUser.setUsername(email.split("@")[0]);
                    newUser.setRole(Role.RECEPTIONIST);
                    newUser.setPassword("");
                    newUser.setStatus(true);
                    return userRepository.save(newUser);
                });

        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
        mappedAuthorities.add(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        return new DefaultOAuth2User(
                mappedAuthorities,
                oauthUser.getAttributes(),
                userNameAttributeName
        );
    }
}
