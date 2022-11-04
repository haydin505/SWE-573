package com.infrasave.config;

import com.infrasave.bean.AppResponse;
import com.infrasave.exception.AbstractException;
import java.util.ArrayList;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author huseyinaydin
 */
@ControllerAdvice
public class ExceptionController {

  @ExceptionHandler(AbstractException.class)
  public ResponseEntity<AppResponse> exception(AbstractException exception) {
    AppResponse appResponse = new AppResponse();
    appResponse.setSuccessful(false);
    appResponse.setErrorTitle(exception.getMessage());
    appResponse.setErrorDetail(exception.getMessage());
    appResponse.setErrorCode(exception.getCode());
    return ResponseEntity.ok(appResponse);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  AppResponse onConstraintValidationException(
      ConstraintViolationException e) {
    AppResponse response = new AppResponse();
    response.setViolations(new ArrayList<>());
    response.setSuccessful(false);
    for (ConstraintViolation violation : e.getConstraintViolations()) {
      response.getViolations().add(
          new AppResponse.Violation(violation.getPropertyPath().toString(), violation.getMessage(), null, null));
    }
    return response;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  AppResponse onMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    AppResponse response = new AppResponse<>();
    response.setViolations(new ArrayList<>());
    response.setSuccessful(false);
    for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
      response.getViolations().add(
          new AppResponse.Violation(fieldError.getField(), fieldError.getDefaultMessage(), null, null));
    }
    return response;
  }
}
