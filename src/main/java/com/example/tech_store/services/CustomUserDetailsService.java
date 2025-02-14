//package com.example.tech_store.service;
//
//import com.example.tech_store.model.User;
//import com.example.tech_store.repository.UserRepository;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    public CustomUserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    // Nếu bạn có thể load user theo username:
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return new CustomUserDetails(user); // CustomUserDetails implements UserDetails
//    }
//
//    // Hoặc bạn có thể định nghĩa một phương thức load user theo userId:
//    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return new CustomUserDetails(user);
//    }
//}
//
//
//
