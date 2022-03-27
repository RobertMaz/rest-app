package ru.demo.app.restapp.web;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import io.swagger.v3.oas.annotations.Hidden;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.Path.Node;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.demo.app.restapp.web.dto.ErrorDescription;
import ru.demo.app.restapp.web.dto.ErrorResponse;
import ru.demo.app.restapp.web.dto.ValidationErrorResponse;

@Hidden
@Slf4j
@RestControllerAdvice(annotations = Controller.class)
public class ExceptionController {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ValidationErrorResponse badRequest(MethodArgumentNotValidException exception) {
    final BindingResult bindingResult = exception.getBindingResult();
    log.error("", exception);
    return createValidationResponse(buildMessage(bindingResult), buildErrors(bindingResult));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ValidationErrorResponse badRequest(DataIntegrityViolationException exception) {
    Throwable cause = exception.getCause();
    String message = "";
    if (cause instanceof ConstraintViolationException) {
      ConstraintViolationException cause1 = (ConstraintViolationException) cause;
      SQLException sqlException = cause1.getSQLException();
      message = sqlException.getMessage();
    }
    log.error("", exception);
    return createValidationResponse(message, List.of());
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public ErrorResponse error(EntityNotFoundException exception) {
    log.error("", exception);
    return getErrorResponse(exception);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(EmptyResultDataAccessException.class)
  public ErrorResponse error(EmptyResultDataAccessException exception) {
    log.error("", exception);
    return getErrorResponse(exception);
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(IllegalStateException.class)
  public ErrorResponse conflict(IllegalStateException exception) {
    log.error("", exception);
    return getErrorResponse(exception);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public ErrorResponse conflict(IllegalArgumentException exception) {
    log.error("", exception);
    return getErrorResponse(exception);
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(javax.validation.ConstraintViolationException.class)
  public ValidationErrorResponse conflict(javax.validation.ConstraintViolationException exception) {
    log.error("", exception);
    Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
    List<ErrorDescription> collect = violations
        .stream()
        .map(a -> createErrorDescription(getLatestFieldName(a), a.getMessage()))
        .collect(Collectors.toUnmodifiableList());
    return createValidationResponse(exception.getMessage(), collect);
  }

  private ErrorDescription createErrorDescription(String node, String errorMessage) {
    ErrorDescription errorDescription = new ErrorDescription();
    errorDescription.setField(node);
    errorDescription.setError(errorMessage);
    return errorDescription;
  }

  private String getLatestFieldName(ConstraintViolation<?> a) {
    Iterator<Node> iterator = a.getPropertyPath().iterator();
    String node = null;
    while (iterator.hasNext()) {
      node = iterator.next().getName();
    }
    return node;
  }

  private String buildMessage(BindingResult bindingResult) {
    return String.format("Error on %s, rejected errors [%s]", bindingResult.getTarget(), bindingResult
        .getAllErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(joining(";")));
  }

  private List<ErrorDescription> buildErrors(BindingResult bindingResult) {
    return bindingResult.getFieldErrors().stream().map(this::createErrorDescription).collect(toList());
  }

  private ErrorDescription createErrorDescription(FieldError e) {
    return createErrorDescription(e.getField(), e.getDefaultMessage());
  }

  private ErrorResponse getErrorResponse(Throwable throwable) {
    ErrorResponse response = new ErrorResponse();
    response.setMessage(throwable.getMessage());
    return response;
  }

  private ValidationErrorResponse createValidationResponse(String message, List<ErrorDescription> errors) {
    ValidationErrorResponse response = new ValidationErrorResponse();
    response.setMessage(message);
    response.setErrors(errors);
    return response;
  }
}
