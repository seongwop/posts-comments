package com.example.postscomments.exception;

import com.example.postscomments.dto.ResponseEntityDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseEntityDto> exceptionHandler(Exception e) {
        String msg = e.getMessage();
        return new ResponseEntity<>(ResponseEntityDto.exceptionStatus(msg), HttpStatus.BAD_REQUEST);
    }
}
