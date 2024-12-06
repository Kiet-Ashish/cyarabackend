package com.cyara.tool.Exception;


import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(CustomException.class)
    public ProblemDetail exceptionHandler(CustomException exception){
        return ProblemDetail.forStatusAndDetail(exception.httpStatus, exception.getMessage());
    }
}
