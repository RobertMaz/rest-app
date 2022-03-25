package ru.demo.app.restapp.web;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

import io.swagger.v3.oas.annotations.Hidden;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.demo.app.restapp.model.ErrorDescription;
import ru.demo.app.restapp.model.ErrorResponse;
import ru.demo.app.restapp.model.ValidationErrorResponse;

@Hidden
@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionController {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ValidationErrorResponse badRequest(MethodArgumentNotValidException exception) {
    final BindingResult bindingResult = exception.getBindingResult();
    log.error("", exception);
    return new ValidationErrorResponse(buildMessage(bindingResult), buildErrors(bindingResult));
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public ErrorResponse error(EntityNotFoundException exception) {
    log.error("", exception);
    return new ErrorResponse(exception.getMessage());
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(IllegalStateException.class)
  public ErrorResponse conflict(IllegalStateException exception) {
    log.error("", exception);
    return new ErrorResponse(exception.getMessage());
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(RuntimeException.class)
  public ErrorResponse error(RuntimeException exception) {
    log.error("", exception);
    return new ErrorResponse(exception.getMessage());
  }

  private String buildMessage(BindingResult bindingResult) {
    return String.format("Error on %s, rejected errors [%s]",
        bindingResult.getTarget(),
        bindingResult.getAllErrors()
                     .stream()
                     .map(DefaultMessageSourceResolvable::getDefaultMessage)
                     .collect(joining(";")));
  }

  private List<ErrorDescription> buildErrors(BindingResult bindingResult) {
    return bindingResult.getFieldErrors()
                        .stream()
                        .map(e -> new ErrorDescription(e.getField(), e.getDefaultMessage()))
                        .collect(toList());
  }
}
