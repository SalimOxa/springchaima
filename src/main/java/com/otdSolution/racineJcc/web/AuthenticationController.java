package com.otdSolution.racineJcc.web;

import com.otdSolution.racineJcc.dao.AppUserRepository;
import com.otdSolution.racineJcc.entities.*;
import com.otdSolution.racineJcc.service.AccountService;
import com.otdSolution.racineJcc.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;

import static com.otdSolution.racineJcc.utils.ApplicationConstants.APP_ROOT;

@RestController

public class AuthenticationController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AppUserRepository<AppMaster> masterRepository;
    @Autowired
    private AppUserRepository<AppAdmin> adminRepository;
    @Autowired
    private AppUserRepository<AppCustomer> customerRepository;
    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AppCustomer appUser) throws MessagingException {

        if (appUser.getRoles().isEmpty()) {
            throw new IllegalArgumentException();
        }
        appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
        appUser.setActivated(true);
         if (appUser.getRoles().get(0).getRoleName().equals("MASTER")) {
                AppMaster master = masterRepository.save(AppUser.toMasterEntity(appUser));
                        masterRepository.save(master);
                    return new ResponseEntity<AppMaster>(master, HttpStatus.OK);

            } else if (appUser.getRoles().get(0).getRoleName().equals("ADMIN")) {
                AppAdmin admin = adminRepository.save(AppUser.toAdminEntity(appUser));

                    return new ResponseEntity<AppAdmin>(admin, HttpStatus.OK);

            } else  if (appUser.getRoles().get(0).getRoleName().equals("CUSTOMER")) {
                AppCustomer customer =customerRepository.save(appUser) ;

                    return new ResponseEntity< AppCustomer>(customer, HttpStatus.OK);


        }
        return new ResponseEntity<AppUser>(appUser, HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/register-costumer")
    public ResponseEntity<AppCustomer> registerCustomer(@RequestBody AppCustomer appUser) throws MessagingException, UnknownHostException {
       Boolean testConnection= "127.0.0.1".equals(InetAddress.getLocalHost().getHostAddress().toString());
        if (testConnection ==true) {

            return new ResponseEntity<AppCustomer>(new AppCustomer(), HttpStatus.REQUEST_TIMEOUT);

        }
        AppCustomer user = accountService.saveCustomer(appUser);

        if (user != null) {
            return new ResponseEntity<AppCustomer>(user, HttpStatus.OK);
        }
        return new ResponseEntity<AppCustomer>(user, HttpStatus.BAD_REQUEST);
    }
}
