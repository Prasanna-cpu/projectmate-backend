package com.spring.backend.Service.Abstraction;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendEmailWithToken(String userEmail,String link) throws MessagingException;

}
