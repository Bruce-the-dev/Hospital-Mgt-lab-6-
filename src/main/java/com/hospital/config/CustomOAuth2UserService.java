package com.hospital.config;

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
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        //Let Spring fetch user info from Google
        OAuth2User oauthUser = new DefaultOAuth2UserService().loadUser(userRequest);

        // Copy existing authorities (scopes + OAUTH2_USER)
        Set<GrantedAuthority> mappedAuthorities = new HashSet<>(oauthUser.getAuthorities());

        //  Add your app's default role
        mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_RECEPTIONIST"));

        // Return a new user with your custom authorities
        return new DefaultOAuth2User(
                mappedAuthorities,
                oauthUser.getAttributes(),
                "sub" // Google's user id key
        );
    }
}
