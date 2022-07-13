package com.example.demo.user;

import com.example.demo.entity.AppUser;
import com.example.demo.entity.Privilege;
import com.example.demo.entity.Role;
import com.example.demo.error.CustomException;
import com.example.demo.repository.UserRepository;
import com.example.demo.user.dto.AppUserDTO;
import com.example.demo.user.dto.RegisterDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final ModelMapper modelMapper;

  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(
    UserRepository userRepository,
    RoleRepository roleRepository,
    ModelMapper modelMapper,
    PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.modelMapper = modelMapper;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByEmail(username)
             .map(e -> new User(
               e.getEmail(),
               e.getPassword(),
               e.isEnable(),
               true, true, true,
               e.getRoles()
                 .stream()
                 .flatMap(roleToPrivileges().andThen(Collection::stream))
                 .map(privilegeToAuthority())
                 .collect(Collectors.toList())
             ))
             .orElseGet(() -> new User(
               "",
               "",
               true,
               true,
               true,
               true,
               roleRepository.findByName("ROLE_USER")
                 .map(roleToPrivileges())
                 .map(e -> e.stream().map(privilegeToAuthority()))
                 .orElseGet(Stream::of)
                 .collect(Collectors.toList())
             ));
  }

  Function<Role, List<String>> roleToPrivileges() {
    return role -> {
      List<String> privileges = new ArrayList<>();
      privileges.add(role.getName());
      List<Privilege> collection = new ArrayList<>(role.getPrivileges());
      for (Privilege item : collection) {
        privileges.add(item.getName());
      }
      return privileges;
    };
  }

  Function<String, GrantedAuthority> privilegeToAuthority() {
    return SimpleGrantedAuthority::new;
  }

  @Override
  public void register(RegisterDTO registerDTO) {
    if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
      throw new CustomException("user_name_already_exist", HttpStatus.BAD_REQUEST.value());
    }
    Role user = roleRepository.findByName("ROLE_USER").orElse(null);

    AppUser appUser = modelMapper.map(registerDTO, AppUser.class);
    appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
    assert user != null;
    appUser.setRoles(List.of(user));
    userRepository.save(appUser);
  }

  @Override
  @Transactional
  public void enableUser(long userId) {
    AppUser appUser = userRepository.findById(userId)
                        .filter(e -> !e.isEnable())
                        .orElseThrow(() -> new UsernameNotFoundException("not_found"));
    appUser.setEnable(true);
    appUser.setRoles(roleRepository
                       .findByName("ROLE_STAFF")
                       .map(List::of)
                       .orElse(null));
  }

  @Override
  public AppUserDTO getUser(long userId) {
    return
      userRepository.findById(userId)
        .map(e -> modelMapper.map(e, AppUserDTO.class))
        .orElseThrow(() -> new UsernameNotFoundException("not_found"));
  }
}
