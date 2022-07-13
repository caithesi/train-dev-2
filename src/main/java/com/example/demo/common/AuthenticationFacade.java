package com.example.demo.common;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

public interface AuthenticationFacade {
  default Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  default Optional<String> getCurrentUserEmail() {
    return Optional.of(SecurityContextHolder.getContext().getAuthentication())
             .filter(e -> !(e instanceof AnonymousAuthenticationToken))
             .map(Authentication::getName);
  }
}
