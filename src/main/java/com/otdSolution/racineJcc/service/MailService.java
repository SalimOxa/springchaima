package com.otdSolution.racineJcc.service;

import com.otdSolution.racineJcc.dao.AppUserRepository;
import com.otdSolution.racineJcc.dto.Contact;
import com.otdSolution.racineJcc.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private AppUserRepository appUserRepository;

    private JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        super();
        this.javaMailSender = javaMailSender;
    }


    public void sendEmail(Contact contact) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("otd.solution2021@gmail.com");
        mail.setSubject(contact.getSubject());
        mail.setText("Name : " + contact.getName()
                + "\n" +
                "Email : " + contact.getEmail()
                + "\n\n" +
                "Subject : " + contact.getMessage()

        );
        javaMailSender.send(mail);

    }
}
