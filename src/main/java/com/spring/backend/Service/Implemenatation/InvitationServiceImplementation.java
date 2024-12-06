package com.spring.backend.Service.Implemenatation;

import com.spring.backend.Exceptions.InvitationResourceNotFoundException;
import com.spring.backend.Model.Invitation;
import com.spring.backend.Repository.InvitationRepository;
import com.spring.backend.Service.Abstraction.EmailService;
import com.spring.backend.Service.Abstraction.InvitationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, InvitationResourceNotFoundException.class})
public class InvitationServiceImplementation implements InvitationService {

    private final EmailService emailService;

    private final InvitationRepository invitationRepository;

    @Override
    public void sendInvitation(String email, Long projectId) throws MessagingException {

        String invitationToken= UUID.randomUUID().toString();

        Invitation invitation=new Invitation();
        invitation.setToken(invitationToken);
        invitation.setProjectId(projectId);
        invitation.setEmail(email);
        invitationRepository.save(invitation);

        String invitationLink="http://localhost:2000/accept_invitation?token="+invitationToken;
        emailService.sendEmailWithToken(email,invitationLink);

    }

    @Override
    public Invitation acceptInvitation(String token, Long userId) {

        Invitation invitation=invitationRepository.findByToken(token).orElseThrow(
                ()->new InvitationResourceNotFoundException("Could not find Invitation")
        );


        return invitation;
    }

    @Override
    public String getTokenByUserMail(String userEmail) {
        Invitation invitation=invitationRepository.findByEmail(userEmail).orElseThrow(
                ()->new InvitationResourceNotFoundException("Could not find Invitation")
        );
        return invitation.getToken();
    }

    @Override
    public void deleteToken(String token) {
        invitationRepository.findByToken(token).ifPresentOrElse(invitationRepository::delete,()->{
            throw new InvitationResourceNotFoundException("Could not find Invitation");
        });
    }
}
