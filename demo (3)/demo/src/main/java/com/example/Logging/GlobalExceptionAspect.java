package com.example.demo.Logging;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Aspect
@Component
public class GlobalExceptionAspect {

    @AfterThrowing(pointcut = "execution(* com.example.orders.Controllers..*(..))", throwing = "ex")
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validationException = (MethodArgumentNotValidException) ex;
            String errorMessage = "Invalid input: " + validationException.getBindingResult().getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }

        String errorMessage = "An unexpected error occurred: " + ex.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
}