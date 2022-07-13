package com.example.demo.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernameAndPasswordAuthenticationRequest {
  private String userName;

  private String password;
}
