package com.example.tech_store.security;

import com.example.tech_store.enums.Role;
import com.example.tech_store.model.User;
import com.example.tech_store.repository.UserRepository;
import com.example.tech_store.services.BloomFilterService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final BloomFilterService bloomFilterService;
    public CustomOAuth2UserService(UserRepository userRepository, BloomFilterService bloomFilterService) {
        this.userRepository = userRepository;
        this.bloomFilterService = bloomFilterService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String avatarUrl = oAuth2User.getAttribute("picture");
        if (!bloomFilterService.mightContain(email)) {
            User newUser = User.builder()
                    .email(email)
                    .username(oAuth2User.getAttribute("name"))
                    .role(Role.USER)
                    .avatar(avatarUrl)
                    .build();
            userRepository.save(newUser);
            bloomFilterService.add(email);
        }

        return oAuth2User;
    }
}
