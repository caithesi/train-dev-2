package com.example.demo.user.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
  private static final String EMAIL_PATTERN = "^[_A-Za-z\\d-\\+]+(\\.[_A-Za-z\\d-]+)*@" + "[A-Za-z\\d-]+(\\.[A-Za-z\\d]+)*(\\.[A-Za-z]{2,})$";

  private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);

  private final Function<String, Matcher> emailValid = PATTERN::matcher;

  @Override

  public boolean isValid(final String username, final ConstraintValidatorContext context) {
    return emailValid
             .andThen(Matcher::matches)
             .apply(username);
  }
}
