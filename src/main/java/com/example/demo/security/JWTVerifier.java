package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JWTVerifier extends OncePerRequestFilter {
  private final JWTConfig jwtConfig;

  private final SecretKey secretKey;


  public JWTVerifier(JWTConfig jwtConfig, SecretKey secretKey) {
    this.jwtConfig = jwtConfig;
    this.secretKey = secretKey;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    String authHeader = request.getHeader(jwtConfig.getAuthorHeader());

    String token = authHeader.replace(jwtConfig.getTokenPrefix(), "");

    try {
      Jws<Claims> claimJws =
        Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token);

      Claims body = claimJws.getBody();
      String userName = body.getSubject();

      var authorities = body.get("authorities")
                          .toString()
                          .split("\\|");
      var simpleGrantedAuthorities =
        Arrays.stream(authorities)
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());
      Authentication authentication = new UsernamePasswordAuthenticationToken(
        userName,
        null,
        simpleGrantedAuthorities
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (JwtException e) {
      throw new IllegalStateException(String.format("Token %s is invalid", token));
    }
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String authHeader = request.getHeader(jwtConfig.getAuthorHeader());
    return authHeader == null || authHeader.length() == 0 || !authHeader.startsWith(jwtConfig.getTokenPrefix());
  }
}
