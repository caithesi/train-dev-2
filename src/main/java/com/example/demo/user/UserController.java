package com.example.demo.user;

import com.example.demo.user.dto.AppUserDTO;
import com.example.demo.user.dto.RegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@ControllerAdvice
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/sign-up")
  public ResponseEntity<Object> singUp(@RequestBody RegisterDTO registerDTO) {
    userService.register(registerDTO);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public AppUserDTO getUser(@PathVariable Long id) {
    return userService.getUser(id);
  }

  @PutMapping("/manager/{id}/enable")
  public String updateUser(@PathVariable Long id) {
    userService.enableUser(id);
    return "true";
  }
}
