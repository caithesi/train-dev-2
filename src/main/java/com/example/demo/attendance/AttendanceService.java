package com.example.demo.attendance;

import com.example.demo.attendance.dto.AttendanceDTO;
import com.example.demo.common.AuthenticationFacade;
import com.example.demo.entity.AppUser;
import com.example.demo.entity.Attendance;
import com.example.demo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.function.BiFunction;

@Service
public class AttendanceService {
  private final UserRepository userRepository;

  private final AuthenticationFacade authenticationFacade;

  private final AttendanceRepository attendanceRepository;

  private final ModelMapper modelMapper;

  @Autowired
  public AttendanceService(
    UserRepository userRepository,
    AuthenticationFacade authenticationFacade,
    AttendanceRepository attendanceRepository, ModelMapper modelMapper) {
    this.userRepository = userRepository;
    this.authenticationFacade = authenticationFacade;
    this.attendanceRepository = attendanceRepository;
    this.modelMapper = modelMapper;
  }

  @Transactional
  public AttendanceDTO attendanceUpdate() {
    AppUser user = authenticationFacade
                     .getCurrentUserEmail()
                     .flatMap(userRepository::findByEmail)
                     .orElseThrow(() -> new UsernameNotFoundException("not_found"));

    LocalDate localDate = LocalDate.now();

    Attendance attendance =
      attendanceRepository.findByAppUserAndCheckDate(user, localDate)
        .map(e -> {
          e.setCheckOut(LocalTime.now());
          return e;
        })
        .orElseGet(() -> {
          Attendance attendance1 = new Attendance();
          attendance1.setAppUser(user);
          attendance1.setCheckDate(localDate);
          attendance1.setCheckIn(LocalTime.now());
          return attendance1;
        });
    attendanceRepository.save(attendance);
    return modelMapper.map(attendance, AttendanceDTO.class);
  }

  public AttendanceDTO getAttendance(LocalDate localDate) {
    AppUser user =
      authenticationFacade
        .getCurrentUserEmail()
        .flatMap(userRepository::findByEmail)
        .orElseThrow(() -> new UsernameNotFoundException("not_found"));

    return
      attendanceRepository.findByAppUserAndCheckDate(user, localDate)
        .map(e -> modelMapper.map(e, AttendanceDTO.class))
        .orElse(null);
  }
}
