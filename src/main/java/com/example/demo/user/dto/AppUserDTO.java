package com.example.demo.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AppUserDTO {
  private String email;

  private String password;

  private LocalDate dateOfBirth;

  private String userName;
}
