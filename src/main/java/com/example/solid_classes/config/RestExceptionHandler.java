package com.example.solid_classes.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.solid_classes.common.exception.BusinessRuleException;
import com.example.solid_classes.common.exception.UserRuleException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", "400");
        errorResponse.put("error", "Bad Request");
        errorResponse.put("message", "Corpo da requisição mal formatado ou ilegível.");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "400");
        errorResponse.put("error", "Validation Failed");
        errorResponse.put("messages", errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(
            DataIntegrityViolationException exception,
            WebRequest request) {

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", String.valueOf(HttpStatus.CONFLICT.value()));
        errorResponse.put("error", "Conflict");
        errorResponse.put("message", "Conflito de dados");

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Object> handleBusinessRuleException (
        BusinessRuleException exception,
        WebRequest request
    ) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        errorResponse.put("error", "Regra de Negócio Violada");
        errorResponse.put("message", exception.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserRuleException.class)
    public ResponseEntity<Object> handleUserRuleException (
        UserRuleException exception,
        WebRequest request
    ) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        errorResponse.put("error", "Regra de Usuário Violada");
        errorResponse.put("message", exception.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
