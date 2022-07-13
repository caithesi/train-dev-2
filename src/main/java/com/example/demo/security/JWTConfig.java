package com.example.demo.security;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import javax.crypto.SecretKey;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "application.jwt")
public class JWTConfig {
  private String secretKey;

  private String tokenPrefix;

  private Integer tokenExpiredAfterDay;

  public JWTConfig() {
  }

  public String getAuthorHeader() {
    return HttpHeaders.AUTHORIZATION;
  }

  @Bean
  public SecretKey secretKey() {
    return Keys.hmacShaKeyFor(getSecretKey().getBytes());
  }
}
