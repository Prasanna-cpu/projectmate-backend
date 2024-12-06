package com.spring.backend.Exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvitationResourceNotFoundException extends RuntimeException {
    public InvitationResourceNotFoundException(String message) {
        super(message);
    }
}
