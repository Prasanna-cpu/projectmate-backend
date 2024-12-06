package com.spring.backend.Service.Abstraction;

import com.spring.backend.Model.Invitation;
import jakarta.mail.MessagingException;

public interface InvitationService {

    public void sendInvitation(String email,Long projectId) throws MessagingException;

    public Invitation acceptInvitation(String token , Long userId);

    public String getTokenByUserMail(String userEmail);

    void deleteToken(String token);


}
