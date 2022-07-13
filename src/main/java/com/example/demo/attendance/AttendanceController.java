package com.example.demo.attendance;

import com.example.demo.attendance.dto.AttendanceDTO;
import com.example.demo.entity.Attendance;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/attendances")
public class AttendanceController {
  private final AttendanceService attendanceService;

  public AttendanceController(AttendanceService attendanceService) {
    this.attendanceService = attendanceService;
  }

  @PostMapping
  public AttendanceDTO checkIn() {
    return attendanceService.attendanceUpdate();
  }

  @GetMapping("/{checkDate}")
  public AttendanceDTO getAttendance(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkDate) {
    return attendanceService.getAttendance(checkDate);
  }
}
