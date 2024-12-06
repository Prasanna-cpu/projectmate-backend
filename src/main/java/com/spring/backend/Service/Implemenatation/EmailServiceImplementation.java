package com.spring.backend.Service.Implemenatation;

import com.spring.backend.Service.Abstraction.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = {Exception.class})
@RequiredArgsConstructor
public class EmailServiceImplementation implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmailWithToken(String userEmail, String link) throws MessagingException {

        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,"utf-8");

        String subject="Join Project Invitation";
        String text="Click the link to join the project team: "+link;

        helper.setSubject(subject);
        helper.setText(text,true);
        helper.setTo(userEmail);

        try{
            javaMailSender.send(mimeMessage);
        }catch(MailSendException e){
            throw new MailSendException("Failed to send email");
        }



    }
}
