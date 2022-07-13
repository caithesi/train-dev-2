package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "users")
@Getter
@Setter
public class AppUser {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String email;

  private String password;

  @Column(name = "date_of_birth", columnDefinition = "DATE")
  private LocalDate dateOfBirth;

  @Column(name = "is_enable", columnDefinition = "tinyint(1) default 0")
  private boolean isEnable;

  @Column(name = "user_name", length = 50)
  private String userName;

  @UpdateTimestamp
  @Column(name = "update_at")
  private LocalDateTime updatedAt;

  @CreationTimestamp
  @Column(name = "create_at")
  private LocalDateTime createdAt;

  @ManyToMany
  @JoinTable(
    name = "users_roles",
    joinColumns = @JoinColumn(
      name = "user_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(
      name = "role_id", referencedColumnName = "id"))
  private Collection<Role> roles;
}
