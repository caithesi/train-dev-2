package com.example.demo.user.dto;

import com.example.demo.user.validator.ValidEmail;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class RegisterDTO {
  @ValidEmail
  @NotNull
//  @Size(min = 1, message = "{Size.userDto.email}")
  private String email;

  @NotNull
//  @Size(min = 1, message = "{Size.userDto.firstName}")
  private String userName;

  @NotNull
  @Size(min = 1)
  private String password;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  @NotNull
  private LocalDate dateOfBirth;
}
