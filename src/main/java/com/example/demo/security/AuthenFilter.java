package com.example.demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.stream.Collectors;

public class AuthenFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;

  private final JWTConfig jwtConfig;

  private final SecretKey secretKey;


  public AuthenFilter(AuthenticationManager authenticationManager, JWTConfig jwtConfig, SecretKey secretKey) {
    this.authenticationManager = authenticationManager;
    this.jwtConfig = jwtConfig;
    this.secretKey = secretKey;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
                                              HttpServletResponse response) throws AuthenticationException {
    try {
      UsernameAndPasswordAuthenticationRequest authenticationRequest =
        new ObjectMapper().readValue(request.getInputStream(),
          UsernameAndPasswordAuthenticationRequest.class
        );

      Authentication authentication = new UsernamePasswordAuthenticationToken(
        authenticationRequest.getUserName(),
        authenticationRequest.getPassword()
      );

      return authenticationManager.authenticate(authentication);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request,
                                          HttpServletResponse response,
                                          FilterChain chain,
                                          Authentication authResult) throws IOException, ServletException {
    String token = Jwts.builder()
                     .setSubject(authResult.getName())
                     .claim("authorities", authResult.getAuthorities()
                                             .stream()
                                             .map(Object::toString)
                                             .collect(Collectors.joining("|")))
                     .claim("id", 1)
                     .setIssuedAt(new Date())
                     .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpiredAfterDay())))
                     .signWith(secretKey)
                     .compact();
    response.addHeader(jwtConfig.getAuthorHeader(), jwtConfig.getTokenPrefix() + token);
  }
}
