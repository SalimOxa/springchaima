package com.otdSolution.racineJcc.web;

import com.otdSolution.racineJcc.dao.AppProjectRepository;
import com.otdSolution.racineJcc.dao.AppUserRepository;
import com.otdSolution.racineJcc.entities.*;
import com.otdSolution.racineJcc.sendSmsConfiguration.SmsRequest;
import com.otdSolution.racineJcc.service.AccountService;
import com.otdSolution.racineJcc.service.MailService;
import com.otdSolution.racineJcc.service.SendSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.otdSolution.racineJcc.utils.ApplicationConstants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT)
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AppUserRepository appUserRepository;





    @Autowired
    private SendSmsService sendSmsService;
    @Autowired
    private AppProjectRepository appProjectRepository;
    @Autowired
    private MailService mailService;
    @PutMapping("/confirmAccount")
    public ResponseEntity<AppUser> confirmUserAccount(@RequestBody String codeConfirmation) {

        AppUser user = accountService.confirmUserAccount(codeConfirmation);
        if (user != null) {
            return new ResponseEntity<AppUser>(user, HttpStatus.OK);
        }
        return new ResponseEntity<AppUser>(user, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/confirmAccountSms/{email}/{phoneNumber}")
    public ResponseEntity<AppUser> confirmUserAccountSms(@PathVariable("email") String email, @PathVariable("phoneNumber") String phoneNumber) {

        AppUser user = accountService.confirmUserAccountSms(email,phoneNumber);
        if (user != null) {
            return new ResponseEntity<AppUser>(user, HttpStatus.OK);
        }
        return new ResponseEntity<AppUser>(user, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("api/sms")
    public void sendSms(@RequestBody SmsRequest smsRequest) {
        sendSmsService.sendSms(smsRequest);
    }


}








