package com.example.tech_store.security;

import com.example.tech_store.model.User;
import com.example.tech_store.repository.UserRepository;
import com.example.tech_store.services.BloomFilterService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final BloomFilterService bloomFilterService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if(bloomFilterService.mightContain(email)) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword(), user.getRole());

        }
        throw new UsernameNotFoundException("User not found");
    }
}