package com.otdSolution.racineJcc.service;

import com.otdSolution.racineJcc.dao.AppUserRepository;
import com.otdSolution.racineJcc.dao.ConfirmationCodeRepository;
import com.otdSolution.racineJcc.entities.AppUser;
import com.otdSolution.racineJcc.entities.ConfirmationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

  private JavaMailSender mailSender;
  private SpringTemplateEngine templateEngine;
  @Autowired
  private AppUserRepository appUserRepository;
  @Autowired
  private ConfirmationCodeRepository confirmationCodeRepository;

  public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
    this.mailSender = mailSender;

    this.templateEngine = templateEngine;
  }

  public void sendEmail(AppUser user) throws MessagingException {
    AppUser  users=appUserRepository.findByEmail(user.getEmail());
    System.out.println(user.getEmail());

    ConfirmationCode confirmationCode = new ConfirmationCode(users);
    confirmationCodeRepository.save(confirmationCode);

    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
        StandardCharsets.UTF_8.name());

    Map<String, Object> props = new HashMap<>();
    props.put("userName", user.getFirstName());
    props.put("code",confirmationCode.getConfirmationCode());



    Context context = new Context();
    context.setVariables(props);

    messageHelper.setFrom("otd.solution2021@gmail.com");
    messageHelper.setSubject("Your RACINE GCC acount confirmation");

    String template = templateEngine.process("welcome-mail", context);
    messageHelper.setTo(user.getEmail());
    System.out.println(user.getEmail());

    messageHelper.setText(template, true);


    mailSender.send(mimeMessage);
  }
}
