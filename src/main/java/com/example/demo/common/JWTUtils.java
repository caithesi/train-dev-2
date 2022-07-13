package com.example.demo.common;

import com.example.demo.security.JWTConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class JWTUtils {
  private final HttpServletRequest request;

  private final JWTConfig jwtConfig;

  private final SecretKey secretKey;

  public JWTUtils(HttpServletRequest request, JWTConfig jwtConfig, SecretKey secretKey) {
    this.request = request;
    this.jwtConfig = jwtConfig;
    this.secretKey = secretKey;
  }

}
