package com.spring.backend.Exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentResourceNotFoundException extends RuntimeException {
    public CommentResourceNotFoundException(String message) {
        super(message);
    }
}
