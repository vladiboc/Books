package org.example.books.web.controller.core;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.books.aop.Loggable;
import org.example.books.util.ErrorMsg;
import org.example.books.web.dto.ErrorMsgResponse;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Общий обработчик ошибок
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {
  @Loggable
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorMsgResponse> resourceNotFound(NoResourceFoundException e) {

    log.error("ExceptionHandlerController.resourceNotFound: {}", ErrorMsg.NO_RESOURCE_FOUND, e);

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorMsgResponse(ErrorMsg.NO_RESOURCE_FOUND));
  }

  @Loggable
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorMsgResponse> notFound(EntityNotFoundException e) {

    log.error("ExceptionHandlerController.notFound():", e);

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorMsgResponse(ErrorMsg.BOOK_NOT_FOUND));
  }

  @Loggable
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMsgResponse> badRequest(MethodArgumentNotValidException e) {

    log.error("ExceptionHandlerController.badRequest():", e);

    BindingResult bindingResult = e.getBindingResult();
    List<String> errorMessages = bindingResult.getAllErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .toList();
    String errorMessage = String.join("; ", errorMessages);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorMsgResponse(errorMessage));
  }

  @Loggable
  @ExceptionHandler(HandlerMethodValidationException.class)
  public ResponseEntity<ErrorMsgResponse> illegalPathVariable(HandlerMethodValidationException e) {

    log.error("ExceptionHandlerController.illegalPathVariable():", e);

    List<ParameterValidationResult> results = e.getAllValidationResults();
    List<String> errorMessages = new ArrayList<>();
    for(ParameterValidationResult result : results) {
      errorMessages.addAll(
          result.getResolvableErrors().stream()
              .map(MessageSourceResolvable::getDefaultMessage)
              .toList());
    }
    String errorMessage = String.join("; ", errorMessages);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorMsgResponse(errorMessage));
  }

  @Loggable
  @ExceptionHandler(Throwable.class)
  public ResponseEntity<ErrorMsgResponse> serverError(Throwable e) {

    log.error("ExceptionHandlerController.serverError():", e);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorMsgResponse(
            MessageFormat.format(ErrorMsg.INTERNAL_SERVER_ERROR, e.getClass(), e.getMessage())));
  }
}
