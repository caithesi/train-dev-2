package com.example.demo.attendance;

import com.example.demo.entity.AppUser;
import com.example.demo.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>, JpaSpecificationExecutor<Attendance> {
  Optional<Attendance> findByAppUserAndCheckDate(AppUser appUser, LocalDate checkDate);
}
