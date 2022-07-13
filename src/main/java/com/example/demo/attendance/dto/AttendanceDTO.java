package com.example.demo.attendance.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AttendanceDTO {
  private LocalDate checkDate;

  private LocalTime checkIn;

  private LocalTime checkOut;
}
