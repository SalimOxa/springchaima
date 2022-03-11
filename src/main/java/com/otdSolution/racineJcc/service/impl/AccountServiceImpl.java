package com.otdSolution.racineJcc.service.impl;

import com.otdSolution.racineJcc.dao.*;
import com.otdSolution.racineJcc.entities.*;
import com.otdSolution.racineJcc.service.AccountService;
import com.otdSolution.racineJcc.service.EmailService;
import com.otdSolution.racineJcc.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;


import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    private AppUserRepository<AppMaster> masterRepository;
    @Autowired
    private AppUserRepository<AppAdmin> adminRepository;
    @Autowired
    private AppUserRepository<AppCustomer> customerRepository;
    @Autowired
    private AppRoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ConfirmationCodeRepository confirmationCodeRepository;
    @Autowired
    private EmailService emailService ;
    @Autowired
    private NotificationService notificationService;

    @Override
    public AppUser loadUserByUsername(String username) {

        return appUserRepository.findByUsername(username);
    }

    /* @Override
     public AppUser save(AppUser user) throws MessagingException {

         if (user.getRoles().isEmpty()) {
             throw new IllegalArgumentException();
         }
         user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
         user.setActivated(true);
         AppUser savedUser = null;
         if ("MASTER".equals(user.getRoles().get(0).getRoleName())) {

             savedUser = masterRepository.save(AppUser.toMasterEntity(user));


         } else if ("ADMIN".equals(user.getRoles().get(0).getRoleName())) {


             savedUser = adminRepository.save(AppUser.toAdminEntity(user));


         } else if ("CUSTOMER".equals(user.getRoles().get(0).getRoleName())) {

             savedUser =  customerRepository.save(AppUser.toCustomerEntity(user));

         }
         return user;
     }

 */




    @Override
    public AppCustomer saveCustomer(AppCustomer user) throws MessagingException {
        if (customerRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("User already exists");

        }
        if (user.getRoles().isEmpty()) {
            throw new IllegalArgumentException();
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        customerRepository.save(user);
        HashSet<String> listIdReceiver = new HashSet<String>();
    masterRepository.findAllMasters().forEach(master->{
        listIdReceiver.add(master.getId());
    });
        notificationService.sendNotificataionAddCostumer(listIdReceiver,user);
        if ("CUSTOMER".equals(user.getRoles().get(0).getRoleName())) {
            try {
                emailService.sendEmail(user);


            } catch (MessagingException mailException) {


                customerRepository.delete(user);


            }



        }


        return user;
    }

    @Override
    public AppUser confirmUserAccount(String codeConfirmation) {
        AppUser user = new AppUser();
        ConfirmationCode code = confirmationCodeRepository.findByConfirmationCode(codeConfirmation);

        if (code != null) {

            user = appUserRepository.findByEmail(code.getUser().getEmail());
            Calendar calendar = Calendar.getInstance();
            if ((code.getExpiryDate().getTime() - calendar.getTime().getTime()) >= 0) {

                confirmationCodeRepository.delete(code);
                user.setActivated(true);
                appUserRepository.save(user);


            } else {
                System.out.println("token null");


            }
        }
        return user;
    }

    @Override
    public AppUser confirmUserAccountSms(String email, String phoneNumber) {
        System.out.println(email);
        AppUser user;
        user = appUserRepository.findByEmail(email);
        user.setActivated(true);
        user.setPhoneNumber(phoneNumber);
        appUserRepository.save(user);
        return user;
    }


}
