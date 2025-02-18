package com.example.tech_store.interceptor;


import com.example.tech_store.enums.Role;
import com.example.tech_store.exception.ForbiddenException;
import com.example.tech_store.exception.UnauthorizedException;
import com.example.tech_store.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.UUID;


@Component
@AllArgsConstructor
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        @SuppressWarnings("unchecked")
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        UUID targetUserId = extractUserIdFromPathVariables(pathVariables);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            throw new UnauthorizedException("User not authenticated");
        }

        UUID authenticatedUserId = userDetails.getId();

        if (canAccessProfile(targetUserId, authenticatedUserId, userDetails)) {
            return true;
        }

        throw new ForbiddenException("You don't have permission to access this profile");
    }

    private UUID extractUserIdFromPathVariables(Map<String, String> pathVariables) {
        try {
            return UUID.fromString(pathVariables.get("id"));
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("Invalid user ID format");
        }
    }

    private boolean canAccessProfile(UUID targetUserId, UUID authenticatedUserId, UserDetailsImpl userDetails) {
        return targetUserId.equals(authenticatedUserId) || userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
    }

}
