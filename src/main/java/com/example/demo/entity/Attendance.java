package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "attendances")
public class Attendance {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(name = "check_date", columnDefinition = "DATE")
  private LocalDate checkDate;

  @Column(name = "check_in", columnDefinition = "TIME")
  private LocalTime checkIn;

  @Column(name = "check_out", columnDefinition = "TIME")
  private LocalTime checkOut;

  @UpdateTimestamp
  @Column(name = "update_at")
  private LocalDateTime updatedAt;

  @CreationTimestamp
  @Column(name = "create_at")
  private LocalDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private AppUser appUser;
}
