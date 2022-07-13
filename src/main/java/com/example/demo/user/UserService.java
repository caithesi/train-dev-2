package com.example.demo.user;

import com.example.demo.user.dto.AppUserDTO;
import com.example.demo.user.dto.RegisterDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
  void register(RegisterDTO registerDTO);

  void enableUser(long userId);

  AppUserDTO getUser(long userId);
}
