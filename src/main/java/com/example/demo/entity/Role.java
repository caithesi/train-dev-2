package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@Table(name = "roles")
@NoArgsConstructor
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  @ManyToMany(mappedBy = "roles")
  private Collection<AppUser> appUsers;

  @ManyToMany
  @JoinTable(
    name = "roles_privileges",
    joinColumns = @JoinColumn(
      name = "role_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(
      name = "privilege_id", referencedColumnName = "id"))
  private Collection<Privilege> privileges;

  public Role(String name) {
    this.name = name;
  }
}
