package com.spring.backend.Exceptions;


import com.spring.backend.Response.ApiResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request)
    {
        Map<String,String> validationErrors=new HashMap<>();
        List<ObjectError> validationErrorList=ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error)->{
            String fieldName=((FieldError) error).getField();
            String validationMessage=error.getDefaultMessage();
            validationErrors.put(fieldName,validationMessage);
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleArbitraryException(Exception e){
        ApiResponse apiResponse=new ApiResponse(
                e.getMessage(),
                null,
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<ApiResponse> handleForbiddenOperationException(ForbiddenOperationException e){
        ApiResponse apiResponse=new ApiResponse(
                e.getMessage(),
                null,
                HttpStatus.FORBIDDEN.value()
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExistingEmailException.class)
    public ResponseEntity<ApiResponse> handleExistingEmailException(ExistingEmailException e){
        ApiResponse apiResponse=new ApiResponse(
                e.getMessage(),
                null,
                HttpStatus.CONFLICT.value()
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException e){
        ApiResponse apiResponse=new ApiResponse(
                e.getMessage(),
                null,
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUsernameNotFoundException(UsernameNotFoundException e){
        ApiResponse apiResponse=new ApiResponse(
                e.getMessage(),
                null,
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProjectResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleProjectResourceNotFoundException(ProjectResourceNotFoundException e){
        ApiResponse apiResponse=new ApiResponse(
                e.getMessage(),
                null,
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ApiResponse> handleMessagingException(MessagingException e){
        ApiResponse apiResponse=new ApiResponse(
                e.getMessage(),
                null,
                HttpStatus.SERVICE_UNAVAILABLE.value()
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(MailSendException.class)
    public ResponseEntity<ApiResponse> handleMailSendException(MailSendException e){
        ApiResponse apiResponse=new ApiResponse(
                e.getMessage(),
                null,
                HttpStatus.SERVICE_UNAVAILABLE.value()
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }


    @ExceptionHandler(InvitationResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleInvitationResourceNotFound(InvitationResourceNotFoundException e){
        ApiResponse apiResponse=new ApiResponse(
                e.getMessage(),
                null,
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IssueResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleIssueResourceNotFoundException(IssueResourceNotFoundException e){
        ApiResponse apiResponse=new ApiResponse(
                e.getMessage(),
                null,
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommentResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleCommentResourceNotFoundException(CommentResourceNotFoundException e){
        ApiResponse apiResponse=new ApiResponse(
                e.getMessage(),
                null,
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MessageResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleMessageResourceNotFoundException(MessageResourceNotFoundException e){
        ApiResponse apiResponse=new ApiResponse(
                e.getMessage(),
                null,
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(SubscriptionResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleSubscriptionResourceNotFoundException(SubscriptionResourceNotFoundException e){
        ApiResponse apiResponse=new ApiResponse(
                e.getMessage(),
                null,
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }





}
