package ru.demo.app.restapp.model;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class ValidationErrorResponse extends ErrorResponse {

  private final List<ErrorDescription> errors;

  public ValidationErrorResponse(String message, List<ErrorDescription> errors) {
    super(message);
    this.errors = errors;
  }
}