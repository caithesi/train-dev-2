package com.example.demo;

import com.example.demo.entity.AppUser;
import com.example.demo.entity.Privilege;
import com.example.demo.entity.Role;
import com.example.demo.user.PrivilegeRepository;
import com.example.demo.user.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
  boolean alreadySetup = false;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PrivilegeRepository privilegeRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (alreadySetup)
      return;
    Privilege readPrivilege
      = createPrivilegeIfNotFound("READ_PRIVILEGE");
    Privilege writePrivilege
      = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

    List<Privilege> adminPrivileges = Arrays.asList(
      readPrivilege, writePrivilege);
    createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
    createRoleIfNotFound("ROLE_STAFF", Arrays.asList(readPrivilege));
    createRoleIfNotFound("ROLE_USER", List.of());

    Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElse(null);
    AppUser appUser = new AppUser();
    appUser.setUserName("Test");
    appUser.setPassword(passwordEncoder.encode("test"));
    appUser.setEmail("test@test.com");
    appUser.setRoles(Arrays.asList(adminRole));
    appUser.setEnable(true);
    userRepository.save(appUser);

    Role staff = roleRepository.findByName("ROLE_STAFF").orElse(null);

    AppUser appUser2 = new AppUser();
    appUser2.setUserName("Test2");
    appUser2.setPassword(passwordEncoder.encode("test"));
    appUser2.setEmail("test2@test.com");
    appUser2.setRoles(Arrays.asList(staff));
    appUser2.setEnable(true);
    userRepository.save(appUser2);


    Role user = roleRepository.findByName("ROLE_USER").orElse(null);

    AppUser useronly = new AppUser();
    useronly.setUserName("user");
    useronly.setPassword(passwordEncoder.encode("test"));
    useronly.setEmail("user@test.com");
    useronly.setRoles(Arrays.asList(user));
    useronly.setEnable(false);
    userRepository.save(useronly);

    alreadySetup = true;
  }


  @Transactional
  Privilege createPrivilegeIfNotFound(String name) {

    Privilege privilege = privilegeRepository.findByName(name).orElse(null);
    if (privilege == null) {
      privilege = new Privilege(name);
      privilegeRepository.save(privilege);
    }
    return privilege;
  }

  @Transactional
  Role createRoleIfNotFound(
    String name, Collection<Privilege> privileges) {

    Role role = roleRepository.findByName(name).orElse(null);
    if (role == null) {
      role = new Role(name);
      role.setPrivileges(privileges);
      roleRepository.save(role);
    }
    return role;
  }
}
