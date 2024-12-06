package com.cyara.tool.Exception;


import org.springframework.http.HttpStatus;

public class CustomException extends Exception {
    public HttpStatus httpStatus;

    public CustomException(){
        super();
    }

    public CustomException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }

    public CustomException(String message, Throwable cause, HttpStatus httpStatus){
        super(message, cause);
        this.httpStatus = httpStatus;
    }
}
